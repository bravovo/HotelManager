package org.example.hotelmanager.controllers.client;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class WatchAndConfirmBookingFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Booking booking = new Booking();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();
    Room room = new Room();
    RoomHolder roomHolder = RoomHolder.getInstance();
    public VBox hotel_info_vbox;
    public Text hotel_name;
    public Text admin_mobile_phone;
    public Text room_name;
    public Text room_capacity;
    public Text room_desc;
    public VBox booking_info_vbox;
    public Text guest_firstname;
    public Text guest_secondname;
    public Text guest_mobile_phone;
    public Text people_count;
    public TextArea notes;
    public Button create_booking_btn;
    public Button cancel_btn;
    public Text total_price;
    public Text checkIN;
    public Text checkOUT;
    public void initialize(URL url, ResourceBundle resourceBundle){
        client = clientHolder.getUser();
        room = roomHolder.getRoom();
        booking = bookingHolder.getBooking();
        hotel_name.setText(room.getHotel().getHotel_name());
        admin_mobile_phone.setText(room.getHotel().getPhone_number());
        room_name.setText(room.getRoom_name());
        room_capacity.setText(String.valueOf(room.getCapacity()));
        checkIN.setText(booking.getCheckIN_date().toString());
        checkOUT.setText(booking.getCheckOUT_date().toString());
        room_desc.setWrappingWidth(300);
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
                guest_firstname.getText(),
                guest_secondname.getText(),
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
