package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class VenueUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Update the FXML file path to match the provided file
        Parent root = FXMLLoader.load(getClass().getResource("/lancaster/ui/VenueCalendar.fxml"));
        primaryStage.setTitle("Lancaster Venue");

        // Increase the window size to better fit desktop screens
        primaryStage.setScene(new Scene(root, 1024, 768));

        // Make the window start maximized for desktop use
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}