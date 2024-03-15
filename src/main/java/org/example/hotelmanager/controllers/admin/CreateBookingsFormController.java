package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CreateBookingsFormController {
    public Button create_room_btn;
    public Button cancel_btn;

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    public void createButtonClick(ActionEvent event) {
    }
}
