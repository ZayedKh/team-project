package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * The TheaterSeatUI class launches the JavaFX application for displaying the theater seating layout.
 * <p>
 * It loads the FXML layout for the theater seating from the resource folder and displays it in a maximized window.
 * </p>
 */
public class TheaterSeatUI extends Application {

    /**
     * The main entry point for all JavaFX applications.
     * <p>
     * This method loads the FXML file containing the theater seating layout, sets up the scene with a fixed size,
     * maximizes the window, and displays it.
     * </p>
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML resource cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/lancaster/ui/TheaterSeatingLayout.fxml"));
        primaryStage.setTitle("Main Hall");
        primaryStage.setScene(new Scene(root, 1024, 768));
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    /**
     * The main method which launches the Theater seating application.
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
