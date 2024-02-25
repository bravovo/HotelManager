package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;

import java.io.IOException;

public class AdminRoomsFormController {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();
    Stage stage = new Stage();
    public void createRoomClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("admin-forms/create-room-form.fxml", "Створення кімнати", 700, 500);
        //mongoDatabaseConnection.createRoom();
    }
}
