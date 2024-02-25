package org.example.hotelmanager.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminLeftFormController implements Initializable {
    @FXML private Label hotel_label;
    AdminFormController adminFormController = new AdminFormController();
    private Hotel hotel = new Hotel();
    public void initialize(URL url, ResourceBundle resourceBundle){
        hotel = adminFormController.returnHotel();
        hotel_label.setText(hotel.getHotel_name());
    }
}
