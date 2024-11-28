package group5.reservation_management;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ReservationManagerGUI reservationManagerGUI = new ReservationManagerGUI();
        Scene scene = new Scene(reservationManagerGUI.getRoot(), 800, 600);
        primaryStage.setTitle("Restaurant Reservation System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
