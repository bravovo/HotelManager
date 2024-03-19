package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.BookingHolder;

public class ConfirmingBookingDialogFormController {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();

    @FXML private Button cancel_btn;

    @FXML private Button confirm_btn;

    @FXML
    void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    @FXML
    void confirmButtonClick(ActionEvent event) {
        mongoDatabaseConnection.deleteBooking(BookingHolder.getInstance().getBooking());
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

}
