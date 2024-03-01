package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;
import org.example.hotelmanager.objects.Room;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminRoomsFormController  implements Initializable {
    private AdminFormController adminFormController = new AdminFormController();
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    @FXML private Label count_available;

    @FXML private Label rooms_count;

    @FXML private TableView<Room> room_table;

    @FXML private TableColumn<Room, String> room_name;

    @FXML private TableColumn<Room, Integer> room_number;

    @FXML private TableColumn<Room, String> status;

    @FXML private TableColumn<Room, Integer> type_name;

    @FXML private TableColumn<Room, String> from_date;

    @FXML private TableColumn<Room, String> to_date;
    FormBuilder formBuilder = new FormBuilder();

    public void initialize(URL url, ResourceBundle resourceBundle){
        setRoomsTable();
    }
    public void createRoomClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("admin-forms/create-room-form.fxml", "Створення кімнати", 700, 500);
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
        room_table.setItems(hotel.getRoomsForList());

        rooms_count.setText(String.valueOf(hotel.getRoomsForList().size()));
        count_available.setText(String.valueOf(hotel.countAvailableRooms()));
    }
}
