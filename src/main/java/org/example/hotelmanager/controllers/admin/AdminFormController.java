package org.example.hotelmanager.controllers.admin;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.Main;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
public class AdminFormController implements Initializable {
    public VBox profile_vbox;
    public Button settings_btn;
    public Button reviews_btn;
    @FXML private Label hotel_label;
    @FXML private Button logout_btn;
    @FXML public BorderPane admin_board_pane;
    HotelHolder hotelHolder = HotelHolder.getInstance();
    private Hotel hotel = new Hotel();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hotel = hotelHolder.getUser();
        hotel_label.setText(hotel.getHotel_name());
        changeCenter("admin-forms/admin-rooms-form.fxml");
        profile_vbox.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                FormBuilder formBuilder = new FormBuilder();
                try{
                    formBuilder.openDialog("admin-forms/admin-profile-form.fxml",
                            "Профіль готелю | Hotelis",
                            450, 550);
                }catch (Exception e){
                    e.printStackTrace();
                }
                hotel = hotelHolder.getUser();
                hotel_label.setText(hotel.getHotel_name());
            }
        });
    }
    public void changeCenter(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
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
    public void guestsButtonClick(ActionEvent event) {
        changeCenter("admin-forms/admin-guests-form.fxml");
    }
    public void logoutButtonClick(javafx.event.ActionEvent event) throws IOException{
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openWindow((Stage)logout_btn.getScene().getWindow(),"login-form.fxml", "Авторизація | Hotelis", 700, 500);
    }
    public void settingsButtonClick(ActionEvent event) throws IOException {
        hotelHolder.setUser(hotel);
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openWindow("admin-forms/admin-settings-form.fxml",
                "Налаштування акаунта",
                450, 500);
        if(hotelHolder.isDone()){
            hotelHolder.setDone(false);
            formBuilder.openWindow((Stage)settings_btn.getScene().getWindow(),"login-form.fxml", "Авторизація | Hotelis", 700, 500);
        }
    }
    public void reviewsButtonClick(ActionEvent actionEvent) {
        changeCenter("admin-forms/admin-reviews-form.fxml");
    }
}
