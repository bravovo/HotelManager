package org.example.hotelmanager.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminLeftFormController implements Initializable {
    @FXML private Button bookings_btn;
    @FXML private Button guests_btn;
    @FXML private Label hotel_label;
    @FXML private ImageView image_view;
    @FXML private Button logout_btn;
    @FXML private Button rooms_btn;
    @FXML private Button settings_btn;
    @FXML private Button staff_btn;
    AdminFormController adminFormController = new AdminFormController();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    private Hotel hotel = new Hotel();
    public void initialize(URL url, ResourceBundle resourceBundle){
        hotel = hotelHolder.getUser();
        hotel_label.setText(hotel.getHotel_name());
    }
    public void logoutButtonClick(javafx.event.ActionEvent event){
        System.exit(0);
    }
}
