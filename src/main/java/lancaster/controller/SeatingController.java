package lancaster.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SeatingController {
    @FXML
    private VBox seatingContainer;

    @FXML
    private Button btnSeatArrangement;

    @FXML
    private Label lblSeatStatus;

    // Event handler for seating arrangement button
    @FXML
    private void handleSeatArrangement() {
        // Placeholder logic for seating arrangement
        System.out.println("Seating Arrangement Button Pressed!");
    }

    //more methods here
}