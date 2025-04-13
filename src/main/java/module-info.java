/*
 * This file is part of the project.
 * This file defines the module for the project.
 * This file specifies the dependencies of the project.
 */

module lancaster.lancasterfx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;
    requires mysql.connector.j;

    opens lancaster to javafx.fxml;
    exports lancaster.model;
    opens lancaster.model to javafx.fxml;
    exports lancaster.controller;
    opens lancaster.controller to javafx.fxml;
    opens lancaster.ui to javafx.fxml;
    exports lancaster.ui;

    exports lancaster.api.marketing;
    exports lancaster.boxOfficeInterface;
    exports lancaster.marketingAPI;
    exports lancaster.utils;
}