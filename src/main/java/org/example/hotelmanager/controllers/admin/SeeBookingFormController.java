package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hotelmanager.model.Booking;
import org.example.hotelmanager.model.BookingHolder;
import org.example.hotelmanager.model.Room;
import org.example.hotelmanager.model.RoomHolder;

import java.net.URL;
import java.util.ResourceBundle;

public class SeeBookingFormController implements Initializable {
    Booking booking = new Booking();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    Room room = new Room();
    RoomHolder roomHolder = RoomHolder.getInstance();
    public TextField guest_first_name;
    public TextField guest_second_name;
    public TextField guest_phone_number;
    public TextField guest_email;
    public TextField people_count;
    public DatePicker checkIN;
    public DatePicker checkOUT;
    public TextArea add_info;
    public Button ok_btn;

    public void initialize(URL url, ResourceBundle resourceBundle){
        booking = bookingHolder.getBooking();
        room = roomHolder.getRoom();

        guest_first_name.setText(booking.getGuestFirstName());
        guest_second_name.setText(booking.getGuestSecondName());
        guest_phone_number.setText(booking.getGuestPhoneNumber());
        guest_email.setText(booking.getGuestEmail());
        people_count.setText(String.valueOf(booking.getPeopleCount()));
        checkIN.setValue(booking.getCheckIN_date());
        checkOUT.setValue(booking.getCheckOUT_date());
        add_info.setWrapText(true);
        add_info.setText(booking.getAdditionalInfo());
    }

    public void okButtonClick(ActionEvent event) {
        Stage stage = (Stage)ok_btn.getScene().getWindow();
        stage.close();
    }
}
