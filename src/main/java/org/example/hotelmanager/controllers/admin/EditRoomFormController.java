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
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Room;
import org.example.hotelmanager.model.RoomHolder;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ResourceBundle;

public class EditRoomFormController implements Initializable {
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
    RoomHolder roomHolder = RoomHolder.getInstance();
    Room room = new Room();

    public void initialize(URL url, ResourceBundle resourceBundle){
        room_description.setWrapText(true);
        room = roomHolder.getRoom();
        ObservableList<Integer> roomTypesIDs = mongoDatabaseConnection.getRoomTypesIDs();
        ObservableList<String> roomTypesNames = mongoDatabaseConnection.getRoomTypesNames();
        ObservableList<Pair<Integer, String>> roomTypes = FXCollections.observableArrayList();
        for (int i = 0; i < roomTypesIDs.size(); i++){
            roomTypes.add(new Pair<>(i, roomTypesNames.get(i)));
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
        mongoDatabaseConnection.editRoom(roomToEdit);
        roomHolder.setRoom(roomToEdit);
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
    }
}
