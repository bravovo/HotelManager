package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

public class ConfirmDeletingAccFormController {
    public Button confirm_btn;
    public Button cancel_btn;

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    public void confirmButtonClick(ActionEvent event) {
        hotel = hotelHolder.getUser();
        mongoDatabaseConnection.deleteAdminAccount();
        hotelHolder.setDone(true);
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
