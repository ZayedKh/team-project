/*
 * This file is part of the Lancaster University FX project.
 * This file defines the module for the Lancaster University FX project.
 * This file specifies the dependencies of the Lancaster University FX project.
 */



module com.lancaster.lancasterfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;

    opens com.lancaster to javafx.fxml;
    exports com.lancaster.model;
    opens com.lancaster.model to javafx.fxml;
    exports com.lancaster.controller;
    opens com.lancaster.controller to javafx.fxml;
    opens com.lancaster.ui to javafx.fxml;
    exports com.lancaster.ui;
}