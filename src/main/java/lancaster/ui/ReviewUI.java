package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

/**
 * Launches the review interface for the Lancaster application.
 * <p>
 * This class loads the FXML layout for the review page and configures the primary stage with a fixed window size.
 * It serves as the entry point for reviewing functionality in the application.
 * </p>
 */
public class ReviewUI extends Application {

    /**
     * Starts the review UI by loading the review layout from FXML and setting up the primary stage.
     * <p>
     * The FXML file is loaded from the resource path <code>/lancaster/ui/Review.fxml</code>. The primary stage is
     * titled "Lancaster Review" and given a scene size of 1200x600 pixels.
     * </p>
     *
     * @param primaryStage the primary stage for this application.
     * @throws Exception if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/lancaster/ui/Review.fxml"));
        primaryStage.setTitle("Lancaster Review");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.show();
    }

    /**
     * The main method which launches the review UI application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
