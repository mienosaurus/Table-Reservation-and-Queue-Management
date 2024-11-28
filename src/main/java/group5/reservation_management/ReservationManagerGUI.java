package group5.reservation_management;

import group5.reservation_management.Reservation;
import group5.reservation_management.ReservationManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationManagerGUI {
    private BorderPane root;
    private TableView<Reservation> reservationTable;
    private ObservableList<Reservation> reservationList;
    private ReservationManager reservationManager;

    private TextField nameField;
    private TextField partySizeField;
    private DatePicker datePicker;
    private TextField timeField;
    private ComboBox<String> statusComboBox;

    private Button deleteButton;
    private Button updateButton;

    private static final int MAX_CUSTOMERS = 20;
    private static final int TOTAL_TABLES = 10;

    public ReservationManagerGUI() {
        reservationManager = new ReservationManager();
        reservationList = FXCollections.observableArrayList(reservationManager.getReservations());

        root = new BorderPane();
        setupUI();
    }

    public BorderPane getRoot() {
        return root;
    }

    private void setupUI() {
        reservationTable = new TableView<>();
        reservationTable.setItems(reservationList);

        TableColumn<Reservation, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Reservation, Integer> partySizeColumn = new TableColumn<>("Party Size");
        partySizeColumn.setCellValueFactory(new PropertyValueFactory<>("partySize"));

        TableColumn<Reservation, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Reservation, LocalTime> timeColumn = new TableColumn<>("Time");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("time"));

        TableColumn<Reservation, String> statusColumn = new TableColumn<>("Status");
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        reservationTable.getColumns().addAll(nameColumn, partySizeColumn, dateColumn, timeColumn, statusColumn);

        VBox form = createForm();

        root.setCenter(reservationTable);
        root.setRight(form);
        root.setPadding(new Insets(10));

        setupTableListeners();
    }

    private VBox createForm() {
        VBox form = new VBox(10);
        form.setPadding(new Insets(10));

        nameField = new TextField();
        nameField.setPromptText("Name");

        partySizeField = new TextField();
        partySizeField.setPromptText("Party Size");

        datePicker = new DatePicker();
        datePicker.setPromptText("Select a date");

        datePicker.setEditable(false);
        ((TextField) datePicker.getEditor()).setDisable(true);

        timeField = new TextField();
        timeField.setPromptText("Time (HH:MM)");

        statusComboBox = new ComboBox<>();
        statusComboBox.setItems(FXCollections.observableArrayList("Waitlisted", "Seated", "Reserved"));
        statusComboBox.setPromptText("Select Status");

        Button addButton = new Button("Add Reservation");
        addButton.setOnAction(e -> addReservation());

        updateButton = new Button("Update");
        updateButton.setDisable(true);
        updateButton.setOnAction(e -> updateReservation());

        deleteButton = new Button("Delete");
        deleteButton.setDisable(true);
        deleteButton.setOnAction(e -> deleteReservation());

        form.getChildren().addAll(nameField, partySizeField, datePicker, timeField, statusComboBox, addButton, updateButton, deleteButton);
        return form;
    }

    private void setupTableListeners() {
        reservationTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                deleteButton.setDisable(false);
                updateButton.setDisable(false);
                populateFields(newSelection);
            } else {
                deleteButton.setDisable(true);
                updateButton.setDisable(true);
            }
        });
    }

    private void addReservation() {
        String name = nameField.getText();
        String partySizeText = partySizeField.getText();
        LocalDate date = datePicker.getValue();
        String timeText = timeField.getText();
        String status = statusComboBox.getValue();

        if (name.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Name field cannot be empty.");
            return;
        }
        if (reservationList.stream().anyMatch(r -> r.getName().equalsIgnoreCase(name))) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Entry", "A reservation with this name already exists.");
            return;
        }
        if (partySizeText.isEmpty() || !partySizeText.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Party Size must be a number.");
            return;
        }
        if (date == null) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a valid date.");
            return;
        }
        if (timeText.isEmpty() || !timeText.matches("\\d{2}:\\d{2}")) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Time must be in the format HH:MM.");
            return;
        }
        if (status == null || status.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please select a valid status.");
            return;
        }

        LocalTime time = LocalTime.parse(timeText);
        if (reservationList.stream().anyMatch(r -> r.getDate().equals(date) && r.getTime().equals(time))) {
            showAlert(Alert.AlertType.ERROR, "Duplicate Reservation", "A reservation already exists at this date and time.");
            return;
        }

        int partySize = Integer.parseInt(partySizeText);

        if (reservationList.size() >= MAX_CUSTOMERS) {
            showAlert(Alert.AlertType.WARNING, "Capacity Full", "Maximum customer capacity reached. Please adjust the time.");
            return;
        }
        if (partySize > TOTAL_TABLES) {
            showAlert(Alert.AlertType.WARNING, "Table Capacity Exceeded", "Party size exceeds available table capacity.");
            return;
        }

        Reservation reservation = new Reservation(name, partySize, date, time, status);
        reservationList.add(reservation);
        reservationManager.addReservation(reservation);
        clearFields();
    }

    private void populateFields(Reservation reservation) {
        nameField.setText(reservation.getName());
        partySizeField.setText(String.valueOf(reservation.getPartySize()));
        datePicker.setValue(reservation.getDate());
        timeField.setText(reservation.getTime().toString());
        statusComboBox.setValue(reservation.getStatus());
    }

    private void updateReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            try {
                String name = nameField.getText();
                int partySize = Integer.parseInt(partySizeField.getText());
                LocalDate date = datePicker.getValue();
                LocalTime time = LocalTime.parse(timeField.getText());
                String status = statusComboBox.getValue();

                if (reservationList.stream().filter(r -> !r.equals(selected)).anyMatch(r -> r.getName().equalsIgnoreCase(name))) {
                    showAlert(Alert.AlertType.ERROR, "Duplicate Entry", "A reservation with this name already exists.");
                    return;
                }

                selected.setName(name);
                selected.setPartySize(partySize);
                selected.setDate(date);
                selected.setTime(time);
                selected.setStatus(status);

                reservationTable.refresh();
                clearFields();
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Ensure all fields are valid before updating.");
            }
        }
    }

    private void deleteReservation() {
        Reservation selected = reservationTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            reservationManager.removeReservation(selected);
            reservationList.remove(selected);
            clearFields();
        }
    }

    private void clearFields() {
        nameField.clear();
        partySizeField.clear();
        datePicker.setValue(null);
        timeField.clear();
        statusComboBox.setValue(null);
        reservationTable.getSelectionModel().clearSelection();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}