package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the Small Hall seating interface for the Lancaster application.
 * <p>
 * This class loads the FXML layout for the Small Hall seating view and configures the primary stage
 * with a fixed scene size and a maximized window state. It serves as the entry point for users to interact
 * with the Small Hall seating arrangement.
 * </p>
 */
public class SmallHallUI extends Application {

    /**
     * Starts the Small Hall seating UI by loading the FXML layout and setting up the primary stage.
     * <p>
     * The FXML resource is located at <code>/lancaster/ui/SmallHallSeating.fxml</code>. The primary stage is set with
     * the title "Small Hall", configured with a scene size of 1024x768 pixels, and is maximized upon launching.
     * </p>
     *
     * @param primaryStage the primary stage provided by the JavaFX runtime.
     * @throws Exception if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/lancaster/ui/SmallHallSeating.fxml"));
        primaryStage.setTitle("Small Hall");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * The main entry point for the SmallHallUI application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
