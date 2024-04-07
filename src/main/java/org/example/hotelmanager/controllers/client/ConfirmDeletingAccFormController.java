package org.example.hotelmanager.controllers.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

public class ConfirmDeletingAccFormController {
    public Button confirm_btn;
    public Button cancel_btn;

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();
    public void confirmButtonClick(ActionEvent event) {
        client = clientHolder.getUser();
        mongoDatabaseConnection.deleteClientAccount();
        clientHolder.setDone(true);
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
