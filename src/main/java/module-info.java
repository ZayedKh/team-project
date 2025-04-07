/*
 * This file is part of the Lancaster University FX project.
 * This file defines the module for the Lancaster University FX project.
 * This file specifies the dependencies of the Lancaster University FX project.
 */



module lancaster.lancasterfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;
    requires operationsInterface;

    opens lancaster to javafx.fxml;
    exports lancaster.model;
    opens lancaster.model to javafx.fxml;
    exports lancaster.controller;
    opens lancaster.controller to javafx.fxml;
    opens lancaster.ui to javafx.fxml;
    exports lancaster.ui;
}