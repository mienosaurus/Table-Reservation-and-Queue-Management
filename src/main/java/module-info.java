module group5.reservation_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens group5.reservation_management to javafx.fxml;
    exports group5.reservation_management;
}