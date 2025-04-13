// File: lancaster/ui/LoginPage.java
package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The entry point for the Lancaster FX application.
 * <p>
 * This class launches the application by loading the login page from an FXML file.
 * The primary stage is configured with an initial scene, window title, and display settings.
 * </p>
 */
public class LoginPage extends Application {

    /**
     * Starts the application by setting up and showing the primary stage.
     * <p>
     * Loads the user interface from the FXML resource located at <code>/lancaster/ui/logged-in.fxml</code>,
     * sets the stage title to "Lancaster FX", configures the initial scene size, and maximizes the window.
     * </p>
     *
     * @param primaryStage the primary stage for this application.
     * @throws IOException if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/lancaster/ui/logged-in.fxml"));
        primaryStage.setTitle("Lancaster FX");

        // Increase the window size to better fit desktop screens
        primaryStage.setScene(new Scene(root, 1024, 768));

        // Make the window start maximized for desktop use
        primaryStage.setMaximized(true);

        primaryStage.show();
    }

    /**
     * The main method which launches the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
