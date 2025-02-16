module com.lancaster.lancasterfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.lancaster.lancasterfx to javafx.fxml;
    exports com.lancaster.lancasterfx;
    exports controller;
    opens controller to javafx.fxml;
    exports view;
    opens view to javafx.fxml;
}