/*
    * This file is part of the Lancaster University FX project.
    * This file defines the module for the Lancaster University FX project.
    * This file specifies the dependencies of the Lancaster University FX project.
 */



module com.lancaster.lancasterfx {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.lancaster.lancasterfx to javafx.fxml;
    exports com.lancaster.lancasterfx.model;
    opens com.lancaster.lancasterfx.model to javafx.fxml;
    exports com.lancaster.lancasterfx.api;
    opens com.lancaster.lancasterfx.api to javafx.fxml;
    exports com.lancaster.lancasterfx.controller;
    opens com.lancaster.lancasterfx.controller to javafx.fxml;
    exports com.lancaster.lancasterfx.view;
    opens com.lancaster.lancasterfx.view to javafx.fxml;
}