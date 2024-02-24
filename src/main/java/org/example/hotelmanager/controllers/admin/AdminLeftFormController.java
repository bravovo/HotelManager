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
    private Hotel hotel = new Hotel();
    public void initialize(URL url, ResourceBundle resourceBundle){
        getHotel();
        hotel_label.setText(hotel.getHotel_name());
    }
    public void getHotel(){
        HotelHolder hotelHolder = HotelHolder.getInstance();
        hotel = hotelHolder.getUser();
    }
}
