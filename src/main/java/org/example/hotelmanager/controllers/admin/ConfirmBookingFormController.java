package org.example.hotelmanager.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.model.Booking;
import org.example.hotelmanager.model.BookingHolder;
import org.example.hotelmanager.model.Room;
import org.example.hotelmanager.model.RoomHolder;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmBookingFormController implements Initializable {
    FormBuilder formBuilder = new FormBuilder();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    RoomHolder roomHolder = RoomHolder.getInstance();
    ObservableList<Room> availableRoomList = FXCollections.observableArrayList();
    Booking booking;
    Room room;
    int roomID = 0;
    public ChoiceBox room_type_name;
    public TextField room_name;
    public TextField room_number;
    public TextArea booking_add_info;
    public TextField guest_first_name;
    public TextField guest_second_name;
    public DatePicker checkIN_date;
    public DatePicker checkOUT_date;
    public TextField people_count;
    public TextField total_price;
    public Button confirm_booking_btn;
    public Button next_available_room_btn;
    public Button go_back_btn;
    public Button previous_available_room_btn;

    public void initialize(URL url, ResourceBundle resourceBundle){
        availableRoomList = roomHolder.getRoomsList();
        booking = bookingHolder.getBooking();
        room = availableRoomList.get(0);
        setBookingFields();
    }
    public void nextAvailableRoomButtonClick(javafx.event.ActionEvent e){
        roomID++;
        if(roomID >= availableRoomList.size()){
            formBuilder.errorValidation("Більше не залишилось доступних кімнат");
            roomID--;
            return;
        }
        room = availableRoomList.get(roomID);
        setBookingFields();
    }
    public void previousAvailableRoomButtonClick(javafx.event.ActionEvent e){
        roomID--;
        if(roomID >= availableRoomList.size() || roomID < 0){
            formBuilder.errorValidation("Більше не залишилось доступних кімнат");
            roomID++;
            return;
        }
        room = availableRoomList.get(roomID);
        setBookingFields();
    }
    public void confirmBookingButtonClick(javafx.event.ActionEvent e){
        System.out.println("Yeah");
    }
    public void setBookingFields(){
        room_type_name.setValue(room.getType_name());
        room_name.setText(room.getRoom_name());
        room_number.setText(String.valueOf(room.getRoom_number()));
        booking_add_info.setWrapText(true);
        booking_add_info.setText(booking.getAdditionalInfo());
        guest_first_name.setText(booking.getGuestFirstName());
        guest_second_name.setText(booking.getGuestSecondName());
        checkIN_date.setValue(booking.getCheckIN_date());
        checkOUT_date.setValue(booking.getCheckOUT_date());
        people_count.setText(String.valueOf(booking.getPeopleCount()));
        total_price.setText(String.valueOf(booking.getNightCount() * room.getPrice()));
    }
    public void goBackButtonClick(javafx.event.ActionEvent e) throws IOException {
        Stage stage = (Stage) go_back_btn.getScene().getWindow();
        stage.close();
    }
}