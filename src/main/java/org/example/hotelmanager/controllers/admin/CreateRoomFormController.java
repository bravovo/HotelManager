package org.example.hotelmanager.controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateRoomFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();

    @FXML
    private Button cancel_btn;

    @FXML
    private Button create_room_btn;

    @FXML
    private TextArea room_description;

    @FXML
    private TextField room_name;
    @FXML
    private TextField room_price;

    @FXML
    private ChoiceBox<String> type_choice;
    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<String> typeNames = mongoDatabaseConnection.getRoomTypesNames();
        ((ChoiceBox)type_choice).setItems(typeNames);
        type_choice.setValue(typeNames.get(0));

        room_price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                room_price.setText(oldValue);
            }
        });
    }

    public void cancelButtonClick(ActionEvent event){
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void createButtonClick(ActionEvent event){
        TextField[] textFields = {
                room_name,
                room_price,
        };
        for(TextField textField : textFields){
            if (textField.getText().length() == 0){
                formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповнені");
                return;
            }
        }
        mongoDatabaseConnection.createRoom(type_choice.getValue(), room_name.getText(),
                room_description.getText(), room_price.getText());
        mongoDatabaseConnection.updateRoomList();
        Stage stage = (Stage) create_room_btn.getScene().getWindow();
        stage.close();
    }
}
