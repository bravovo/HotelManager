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
    requires java.desktop;
    requires java.sql;
    requires bcrypt;

    opens org.example.hotelmanager to javafx.fxml;
    opens org.example.hotelmanager.model to javafx.base;
    exports org.example.hotelmanager;
    exports org.example.hotelmanager.controllers;
    exports org.example.hotelmanager.controllers.admin;
    exports org.example.hotelmanager.controllers.client;
    exports org.example.hotelmanager.controllers.superAdmin;
    opens org.example.hotelmanager.controllers to javafx.fxml;
    opens org.example.hotelmanager.controllers.client;
    opens org.example.hotelmanager.controllers.admin;
}