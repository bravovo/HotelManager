package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.controllers.LoginFormController;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;
import org.example.hotelmanager.objects.Room;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminRoomsFormController  implements Initializable {
    private AdminFormController adminFormController = new AdminFormController();
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();

    @FXML private TableView<Room> room_table;

    @FXML private TableColumn<Room, String> room_name;

    @FXML private TableColumn<Room, Integer> room_number;

    @FXML private TableColumn<Room, String> status;

    @FXML private TableColumn<Room, Integer> type_name;
    FormBuilder formBuilder = new FormBuilder();

    public void initialize(URL url, ResourceBundle resourceBundle){
        hotel = hotelHolder.getUser();
        room_number.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        type_name.setCellValueFactory(new PropertyValueFactory<>("type_name"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        room_table.setItems(hotel.getRoomsForList());
    }
    public void createRoomClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("admin-forms/create-room-form.fxml", "Створення кімнати", 700, 500);
        room_number.setCellValueFactory(new PropertyValueFactory<>("room_number"));
        type_name.setCellValueFactory(new PropertyValueFactory<>("type_name"));
        room_name.setCellValueFactory(new PropertyValueFactory<>("room_name"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        room_table.setItems(hotel.getRoomsForList());
    }
}
