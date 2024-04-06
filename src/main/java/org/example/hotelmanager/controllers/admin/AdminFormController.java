package org.example.hotelmanager.controllers.admin;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.example.hotelmanager.ManagerApplication;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminFormController implements Initializable {
    @FXML private Button bookings_btn;
    @FXML private Button guests_btn;
    @FXML private Label hotel_label;
    @FXML private Button logout_btn;
    @FXML private Button rooms_btn;
    public Button profile_btn;
    @FXML public BorderPane admin_board_pane;
    HotelHolder hotelHolder = HotelHolder.getInstance();
    private Hotel hotel = new Hotel();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hotel = hotelHolder.getUser();
        hotel_label.setText(hotel.getHotel_name());
        changeCenter("admin-forms/admin-rooms-form.fxml");
    }
    public void changeCenter(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(ManagerApplication.class.getResource(fxmlFile));
            Node centerContent = loader.load();
            admin_board_pane.setCenter(centerContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void bookingsButtonClick(javafx.event.ActionEvent event){
        changeCenter("admin-forms/admin-bookings-form.fxml");
    }
    public void roomsButtonClick(javafx.event.ActionEvent event){
        changeCenter("admin-forms/admin-rooms-form.fxml");
    }
    public void logoutButtonClick(javafx.event.ActionEvent event){
        System.exit(0);
    }
}
