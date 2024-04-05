package org.example.hotelmanager.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Booking;
import org.example.hotelmanager.model.BookingHolder;

public class ConfirmingBookingEditingFormController {
    public Button confirm_btn;
    public Button cancel_btn;

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Booking booking = new Booking();
    BookingHolder bookingHolder = BookingHolder.getInstance();

    public void confirmButtonClick(ActionEvent event) {
        booking = bookingHolder.getBooking();
        mongoDatabaseConnection.editClientBooking(booking, true);
        bookingHolder.setBookingDone(true);
        Stage stage = (Stage) confirm_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        bookingHolder.setBookingDone(false);
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
