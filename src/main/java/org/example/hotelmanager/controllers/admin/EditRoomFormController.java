package org.example.hotelmanager.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class EditRoomFormController implements Initializable {
    FormBuilder formBuilder = new FormBuilder();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    @FXML private Button cancel_btn;
    @FXML private TextArea room_description;
    @FXML private TextField room_name;
    @FXML private TextField room_number;
    @FXML private TextField room_price;
    @FXML private TextField room_status;
    @FXML private Button save_btn;
    @FXML private ChoiceBox<Integer> type_id;
    @FXML private ChoiceBox<String> type_name;
    Hotel hotel = HotelHolder.getInstance().getUser();
    RoomHolder roomHolder = RoomHolder.getInstance();
    Room room = new Room();

    public void initialize(URL url, ResourceBundle resourceBundle){
        room_description.setWrapText(true);
        room = roomHolder.getRoom();
        ObservableList<Integer> roomTypesIDs = FXCollections.observableArrayList();
        ObservableList<String> roomTypesNames = FXCollections.observableArrayList();
        ObservableList<Pair<Integer, String>> roomTypes = FXCollections.observableArrayList();
        for(RoomType roomType : hotel.getRoomTypes()){
            roomTypesIDs.add(roomType.getTypeID());
            roomTypesNames.add(roomType.getTypeName());
            roomTypes.add(new Pair<>(roomType.getTypeID(), roomType.getTypeName()));
        }
        for (int i = 0; i < roomTypesIDs.size(); i++){
        }
        type_id.setItems(roomTypesIDs);
        type_id.setValue(room.getType_id());
        type_name.setItems(roomTypesNames);
        type_name.setValue(room.getType_name());
        room_price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                room_price.setText(oldValue);
            }
        });
        type_name.setOnAction(e -> {
            String selectedName = type_name.getSelectionModel().getSelectedItem();
            int index = roomTypesNames.indexOf(selectedName);
            if (index >= 0) {
                type_id.setValue(roomTypes.get(index).getKey());
            }
        });
        room_name.setText(room.getRoom_name());
        room_status.setText(room.getStatus());
        if (!room.getStatus().equals("Доступна")
                && !room.getStatus().equals("Прибирання")
                && !room.getStatus().equals("Недоступна")){
            room_status.setDisable(true);
        }
        room_price.setText(String.valueOf(room.getPrice()));
        room_number.setText(String.valueOf(room.getRoom_number()));
        room_description.setText(room.getRoom_description());
    }

    public void cancelButtonClick(javafx.event.ActionEvent e){
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    public void saveChangesButtonClick(javafx.event.ActionEvent e){
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
        Room roomToEdit = new Room(
                room.getRoom_id(),
                room.getHotel_id(),
                type_id.getValue(),
                type_name.getValue(),
                room_name.getText(),
                room_description.getText(),
                room.getRoom_number(),
                room_status.getText(),
                room.getDateFrom(),
                room.getDateTo(),
                Double.parseDouble(room_price.getText())
        );
        if(!roomToEdit.equals(room)){
            mongoDatabaseConnection.editRoom(roomToEdit);
            roomHolder.setRoom(roomToEdit);
        }
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
    }
}
