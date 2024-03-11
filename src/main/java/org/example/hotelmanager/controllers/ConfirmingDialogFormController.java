package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;

public class ConfirmingDialogFormController {
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
        mongoDatabaseConnection.deleteRoom();
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

}
