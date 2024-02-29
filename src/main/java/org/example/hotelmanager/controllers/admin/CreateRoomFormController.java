package org.example.hotelmanager.controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoomFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();

    @FXML
    private Button cancel_btn;

    @FXML
    private Button create_room_btn;

    @FXML
    private TextArea room_description;

    @FXML
    private TextField room_name;

    @FXML
    private ChoiceBox<String> type_choice;
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> items = mongoDatabaseConnection.getRoomTypesNames();
        ((ChoiceBox)type_choice).setItems(items);
        type_choice.setValue(items.get(0));
    }

    public void cancelButtonClick(ActionEvent event){
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void createButtonClick(ActionEvent event){ //TODO перевірка коректності введених даних
        mongoDatabaseConnection.createRoom(type_choice.getValue(), room_name.getText(),
                room_description.getText());
        mongoDatabaseConnection.updateRoomList();
        Stage stage = (Stage) create_room_btn.getScene().getWindow();
        stage.close();
    }
}
