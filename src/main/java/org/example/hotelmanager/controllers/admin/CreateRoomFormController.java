package org.example.hotelmanager.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;
import org.example.hotelmanager.model.RoomType;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CreateRoomFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();
    @FXML private Button cancel_btn;
    @FXML private Button create_room_btn;
    @FXML private TextArea room_description;
    @FXML private TextField room_name;
    @FXML private TextField room_price;
    @FXML private ChoiceBox<String> type_choice;
    Hotel hotel = HotelHolder.getInstance().getUser();
    List<String> roomTypes = new ArrayList<>();
    public void initialize(URL url, ResourceBundle resourceBundle){
        room_description.setWrapText(true);
        ObservableList<String> typeNames = FXCollections.observableArrayList();
        for(RoomType roomType : hotel.getRoomTypes()){
            typeNames.add(roomType.getTypeName());
            roomTypes.add(roomType.getTypeName());
        }
        type_choice.setItems(typeNames);
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
            if (textField.getText().isEmpty()){
                formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповнені");
                return;
            }
        }
        mongoDatabaseConnection.createRoom(type_choice.getValue(),
                roomTypes.indexOf(type_choice.getValue()),
                room_name.getText(),
                room_description.getText(),
                room_price.getText()
        );
        Stage stage = (Stage) create_room_btn.getScene().getWindow();
        stage.close();
    }
}
