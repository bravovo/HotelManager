package org.example.hotelmanager.controllers.superAdmin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;

public class SuperConfirmHotelDeleteFormController {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    public Button confirm_btn;
    public Button cancel_btn;

    public void confirmButtonClick(ActionEvent actionEvent) {
        mongoDatabaseConnection.deleteAdminAccount();
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent actionEvent) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
