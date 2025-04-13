package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Launches the Room Seating interface for the Lancaster application.
 * <p>
 * This class loads the FXML layout for the room seating view and configures the primary stage
 * with a fixed scene size and a maximized window state. It serves as the entry point for users
 * to interact with the room seating layout.
 * </p>
 */
public class RoomsSeatingUI extends Application {

    /**
     * Starts the Room Seating UI by loading the room layout from an FXML file and initializing the primary stage.
     * <p>
     * The FXML resource is located at <code>/lancaster/ui/RoomLayout.fxml</code>. The primary stage is set with
     * the title "Room Seating", configured with a scene size of 1024x768 pixels, and is maximized upon launching.
     * </p>
     *
     * @param primaryStage the primary stage provided by the JavaFX runtime.
     * @throws Exception if the FXML resource cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/lancaster/ui/RoomLayout.fxml"));
        primaryStage.setTitle("Room Seating");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * The main entry point for the application.
     *
     * @param args command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
