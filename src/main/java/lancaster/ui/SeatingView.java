package lancaster.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SeatingView {

    public void showSeatingView(Stage primaryStage) throws IOException {
        // Load the FXML for the Seating View
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/lancaster/ui/seatingView.fxml"));
        Parent root = loader.load();

        // Set up the Scene for the Seating View
        primaryStage.setTitle("Seating View");
        primaryStage.setScene(new Scene(root, 1024, 768));

        // Make the window start maximized for desktop use
        primaryStage.setMaximized(true);

        primaryStage.show();
    }
}