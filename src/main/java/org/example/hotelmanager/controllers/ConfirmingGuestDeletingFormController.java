package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Guest;
import org.example.hotelmanager.model.GuestHolder;

public class ConfirmingGuestDeletingFormController {
    @FXML private Button cancel_btn;
    @FXML private Button confirm_btn;
    Guest guest = new Guest();
    GuestHolder guestHolder = GuestHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();


    @FXML
    void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    @FXML
    void confirmButtonClick(ActionEvent event) {
        guest = guestHolder.getUser();
        mongoDatabaseConnection.deleteGuest(guest);
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

}
