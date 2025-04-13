package lancaster.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lancaster.utils.DBUtils;

import java.io.IOException;

/**
 * The main application class for the Lancaster FX application.
 * <p>
 * This class loads the selection pane from an FXML file and initializes the primary stage
 * with the resulting scene.
 * </p>
 */
public class MainApp extends Application {

    /**
     * Starts the application by loading the selection pane and setting it as the primary scene.
     * <p>
     * The FXML file is loaded using the resource from {@link DBUtils}. If any errors occur during
     * loading, they are printed to the standard error output.
     * </p>
     *
     * @param primaryStage the primary stage provided by the JavaFX runtime.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource("/lancaster/ui/selectionPane.fxml"));
            Parent selectionPane = loader.load();
            Scene scene = new Scene(selectionPane);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The main entry point for the application.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
