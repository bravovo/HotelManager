package org.example.hotelmanager.controllers.admin;

import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Booking;
import org.example.hotelmanager.model.BookingHolder;
import org.example.hotelmanager.model.Room;
import org.example.hotelmanager.model.RoomHolder;

import java.io.IOException;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class EditBookingFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    RoomHolder roomHolder = RoomHolder.getInstance();
    Booking booking = new Booking();
    Room room = new Room();
    public TextField guest_first_name;
    public TextField guest_second_name;
    public TextField guest_phone_number;
    public TextField guest_email;
    public TextField people_count;
    public DatePicker checkIN;
    public DatePicker checkOUT;
    public TextArea add_info;
    public Button save_btn;
    public Button cancel_btn;

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

        guest_phone_number.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\d*)?")) {
                guest_phone_number.setText(oldValue);
            }
        });

        people_count.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\d*)?")) {
                people_count.setText(oldValue);
            }
        });
    }

    public void saveButtonClick(javafx.event.ActionEvent event) throws IOException {
        TextField[] textFields = {
                guest_first_name,
                guest_second_name,
                guest_phone_number,
                guest_email,
                people_count
        };
        for(TextField textField : textFields){
            if (textField.getText().length() == 0){
                formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповнені");
                return;
            }
        }
        long nightPeriod = ChronoUnit.DAYS.between(checkIN.getValue(), checkOUT.getValue());
        Booking bookingToEdit = new Booking(
                guest_first_name.getText(),
                guest_second_name.getText(),
                guest_phone_number.getText(),
                guest_email.getText(),
                checkIN.getValue(),
                checkOUT.getValue(),
                Integer.parseInt(people_count.getText()),
                nightPeriod,
                add_info.getText()
        );
        if (checkOUT.getValue().isBefore(checkIN.getValue())
                || checkOUT.getValue().isEqual(checkIN.getValue())){
            formBuilder.errorValidation("Дата заїзду повинна бути раніше дати виїзду");
            return;
        }
        if((checkIN.getValue().isEqual(booking.getCheckIN_date())
                && checkOUT.getValue().isEqual(booking.getCheckOUT_date())) &&
        Integer.parseInt(people_count.getText()) == booking.getPeopleCount()){
            mongoDatabaseConnection.editAdminBooking(bookingToEdit, false);
        }
        else {
            mongoDatabaseConnection.editAdminBooking(bookingToEdit, true);
            bookingHolder.setBooking(bookingToEdit);
            if (roomHolder.getRoomsList().size() == 0){
                formBuilder.errorValidation("Не вдалось знайти вільних кімнат");
                return;
            }
            formBuilder.openWindow(
                    "admin-forms/confirm-booking-form.fxml",
                    "Підтвердити бронювання",
                    900,
                    600
            );
        }
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    public void cancelButtonClick(javafx.event.ActionEvent event){
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
