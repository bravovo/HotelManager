package org.example.hotelmanager.controllers.admin;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;

public class AdminFormController {
    @FXML
    private Label hotel_label;
    private Hotel hotel = new Hotel();

    public Hotel returnHotel(){
        getHotel();
        return hotel;
    }
    public void getHotel(){
        HotelHolder hotelHolder = HotelHolder.getInstance();
        hotel = hotelHolder.getUser();
    }
}
