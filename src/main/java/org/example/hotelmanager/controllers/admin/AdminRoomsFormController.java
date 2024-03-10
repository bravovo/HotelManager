package org.example.hotelmanager.controllers.admin;

import javafx.animation.TranslateTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;
import org.example.hotelmanager.model.Room;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminRoomsFormController  implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();

    @FXML private VBox find_room_vbox;
    @FXML private AnchorPane rooms_anchor_pane;

    @FXML private Label count_available;
    @FXML private Label rooms_count;
    @FXML private TableView<Room> room_table;
    @FXML private TableColumn<Room, String> room_name;
    @FXML private TableColumn<Room, Integer> room_number;
    @FXML private TableColumn<Room, String> status;
    @FXML private TableColumn<Room, Integer> type_name;
    @FXML private TableColumn<Room, String> from_date;
    @FXML private TableColumn<Room, String> to_date;
    @FXML private ToggleButton find_room_btn;

    @FXML private ChoiceBox<String> filter_choice;
    @FXML private Button find_button;
    @FXML private TextField find_input;
    @FXML private Button reset_button;
    FormBuilder formBuilder = new FormBuilder();

    public void initialize(URL url, ResourceBundle resourceBundle){
        setRoomsTable();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Статус", "Тип кімнати", "Номер кімнати", "Назва кімнати", "Ціна"
        );
        ((ChoiceBox)filter_choice).setItems(items);
        filter_choice.setValue("Фільтр для пошуку");
    }
    public void createRoomClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("admin-forms/create-room-form.fxml", "Створення кімнати", 700, 500);
        setRoomsTable();
    }

    public void findRoomButtonClick(ActionEvent event){
        if(find_room_btn.isSelected()){
            AnchorPane.setTopAnchor(room_table, 80.0);
            find_room_vbox.setVisible(true);
            room_table.setPrefHeight(541);
        }else {
            resetTable();
            AnchorPane.setTopAnchor(room_table, 0.0);
            find_room_vbox.setVisible(false);
            room_table.setPrefHeight(621);
        }
    }

    public void findButtonClick(ActionEvent event){
        String filter = "";
        String value = "";
        switch (filter_choice.getValue()){
            case "Статус" -> {
                String status = find_input.getText();
                filter = "Статус";
                value = status;
            }
            case "Тип кімнати" -> {
                String room_type = find_input.getText();
                filter = "Тип кімнати";
                value = room_type;
            }
            case "Номер кімнати" -> {
                int room_number = 0;
                try{
                    room_number = Integer.parseInt(find_input.getText());
                }catch (Exception e){
                    FormBuilder formBuilder = new FormBuilder();
                    formBuilder.errorValidation("Номер кімнати повинен бути числом!");
                }
                filter = "Номер кімнати";
                value = String.valueOf(room_number);
            }
            case "Назва кімнати" -> {
                String room_name = find_input.getText();
                filter = "Назва кімнати";
                value = room_name;
            }
            case "Ціна" -> {
                double price = 0;
                try{
                    price = Double.parseDouble(find_input.getText());
                }catch (Exception e){
                    FormBuilder formBuilder = new FormBuilder();
                    formBuilder.errorValidation("Ціна повинна бути числом!");
                }
                filter = "Ціна";
                value = String.valueOf(price);
            }
        }
        ObservableList<Room> roomList = mongoDatabaseConnection.findRoomByFilter(filter, value);
        setRoomsTable(roomList);
    }

    public void resetTableButtonClick(ActionEvent event){
        resetTable();
    }
    public void resetTable(){
        find_input.setText("");
        mongoDatabaseConnection.updateRoomList();
        setRoomsTable();
    }
    public void setRoomsTable(){
        hotel = hotelHolder.getUser();
        room_number.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        type_name.setCellValueFactory(new PropertyValueFactory<>("type_name"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        from_date.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
        to_date.setCellValueFactory(new PropertyValueFactory<>("dateTo"));
        ObservableList<Room> rooms = hotel.getRoomsForList();
        room_table.setItems(rooms);

        rooms_count.setText(String.valueOf(rooms.size()));
        count_available.setText(String.valueOf(hotel.countAvailableRooms()));
    }
    public void setRoomsTable(ObservableList<Room> roomList){
        room_number.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        type_name.setCellValueFactory(new PropertyValueFactory<>("type_name"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        from_date.setCellValueFactory(new PropertyValueFactory<>("dateFrom"));
        to_date.setCellValueFactory(new PropertyValueFactory<>("dateTo"));
        room_table.setItems(roomList);
    }

}
