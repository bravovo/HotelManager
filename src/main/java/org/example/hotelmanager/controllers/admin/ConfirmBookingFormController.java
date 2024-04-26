package org.example.hotelmanager.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.bson.types.ObjectId;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmBookingFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
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
        roomHolder.setRoom(room);
        setBookingFields();
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
        if(roomID == 0){
            previous_available_room_btn.setDisable(true);
        }else{
            previous_available_room_btn.setDisable(false);
        }
        if(roomID == availableRoomList.size() - 1){
            next_available_room_btn.setDisable(true);
        }else {
            next_available_room_btn.setDisable(false);
        }
    }

    public void nextAvailableRoomButtonClick(javafx.event.ActionEvent e){
        roomID++;
        room = availableRoomList.get(roomID);
        roomHolder.setRoom(room);
        setBookingFields();
    }
    public void previousAvailableRoomButtonClick(javafx.event.ActionEvent e){
        roomID--;
        room = availableRoomList.get(roomID);
        roomHolder.setRoom(room);
        setBookingFields();
    }
    public void confirmBookingButtonClick(javafx.event.ActionEvent e){
        room = roomHolder.getRoom();
        Booking adminBooking = new Booking(
                room.getHotel_id(),
                room.getRoom_id(),
                guest_first_name.getText(),
                guest_second_name.getText(),
                booking.getGuestPhoneNumber(),
                booking.getGuestEmail(),
                Integer.parseInt(room_number.getText()),
                String.valueOf(room_type_name.getValue()),
                Integer.parseInt(people_count.getText()),
                checkIN_date.getValue(),
                checkOUT_date.getValue(),
                Double.parseDouble(total_price.getText()),
                booking_add_info.getText()
        );
        mongoDatabaseConnection.createAdminBooking(adminBooking);
        Stage stage = (Stage) go_back_btn.getScene().getWindow();
        stage.close();
    }
    public void goBackButtonClick(javafx.event.ActionEvent e) {
        Stage stage = (Stage) go_back_btn.getScene().getWindow();
        stage.close();
    }
}
