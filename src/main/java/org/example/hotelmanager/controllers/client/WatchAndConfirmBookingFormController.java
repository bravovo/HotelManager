package org.example.hotelmanager.controllers.client;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class WatchAndConfirmBookingFormController implements Initializable {
    public TextField hotel_name;
    public TextField admin_mobile_phone;
    public TextField room_name;
    public TextField room_capacity;
    public TextField checkIN;
    public TextField checkOUT;
    public TextArea room_desc;
    public VBox booking_info_vbox;
    public TextField guest_firstname;
    public TextField guest_secondname;
    public TextField guest_mobile_phone;
    public TextField people_count;
    public TextField total_price;
    public TextArea notes;
    public Button create_booking_btn;
    public Button cancel_btn;
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Booking booking = new Booking();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();
    Room room = new Room();
    RoomHolder roomHolder = RoomHolder.getInstance();
    public VBox hotel_info_vbox;

    public void initialize(URL url, ResourceBundle resourceBundle){
        room_desc.setWrapText(true);
        client = clientHolder.getUser();
        room = roomHolder.getRoom();
        booking = bookingHolder.getBooking();
        hotel_name.setText(room.getHotel().getHotel_name());
        admin_mobile_phone.setText(room.getHotel().getPhone_number());
        room_name.setText(room.getRoom_name());
        room_capacity.setText(String.valueOf(room.getCapacity()));
        checkIN.setText(booking.getCheckIN_date().toString());
        checkOUT.setText(booking.getCheckOUT_date().toString());
        room_desc.setText(room.getRoom_description());
        guest_firstname.setText(client.getFirstName());
        guest_secondname.setText(client.getLastName());
        guest_mobile_phone.setText(client.getPhoneNumber());
        people_count.setText(String.valueOf(booking.getPeopleCount()));
        total_price.setText(String.valueOf(booking.getNightCount() * room.getPrice()));
        notes.setWrapText(true);
        notes.setText(bookingHolder.getBooking().getAdditionalInfo());
    }

    public void createBookingButtonClick(ActionEvent event) {
        Booking clientBooking = new Booking(
                room.getHotel_id(),
                room.getRoom_id(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getEmail(),
                room.getRoom_number(),
                room.getType_name(),
                Integer.parseInt(people_count.getText()),
                booking.getCheckIN_date(),
                booking.getCheckOUT_date(),
                booking.getNightCount() * room.getPrice(),
                notes.getText()
        );
        mongoDatabaseConnection.createClientBooking(clientBooking);
        bookingHolder.setBookingDone(true);
        Stage stage = (Stage) create_booking_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
