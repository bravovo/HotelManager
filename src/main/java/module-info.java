module org.example.hotelmanager {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;

    opens org.example.hotelmanager to javafx.fxml;
    exports org.example.hotelmanager;
    exports org.example.hotelmanager.controllers;
    opens org.example.hotelmanager.controllers to javafx.fxml;
}