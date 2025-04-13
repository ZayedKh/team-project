package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The VenueUI class is the main entry point for launching the venue calendar
 * interface of the Lancaster application.
 * <p>
 * It loads the FXML layout for the venue calendar, sets up the primary stage with an appropriate title,
 * window size, and displays the application in a maximized window.
 * </p>
 */
public class VenueUI extends Application {

    /**
     * The start method is called after the application is launched.
     * <p>
     * It loads the FXML file located at "/lancaster/ui/VenueCalendar.fxml", sets the stage title to
     * "Lancaster Venue", initializes the scene with a resolution of 1024x768, maximizes the window,
     * and then displays the primary stage.
     * </p>
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML resource cannot be loaded
     */
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

    /**
     * The main method which launches the venue application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
