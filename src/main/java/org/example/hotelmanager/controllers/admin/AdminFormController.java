package org.example.hotelmanager.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.hotelmanager.ManagerApplication;
import org.example.hotelmanager.objects.Hotel;

import java.awt.event.WindowEvent;
import java.io.IOException;

public class AdminFormController {
    @FXML
    private Label hotel_label;
    private Hotel hotel;

    public AdminFormController() {
    }

}
