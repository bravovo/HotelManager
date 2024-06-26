package org.example.hotelmanager.controllers.admin;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Booking;
import org.example.hotelmanager.model.BookingHolder;
import org.example.hotelmanager.model.Room;
import org.example.hotelmanager.model.RoomHolder;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class FindAvailableRoomFormController implements Initializable {
    BookingHolder bookingHolder = BookingHolder.getInstance();
    RoomHolder roomHolder = RoomHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();
    public ProgressBar email_valid_bar;
    public ProgressBar phone_valid_bar;
    public Button cancel_btn;
    public TextField guest_first_name;
    public TextField guest_second_name;
    public TextField people_count;
    public DatePicker checkIN_date;
    public DatePicker checkOUT_date;
    public TextField guest_phone_number;
    public TextField guest_email;
    public DatePicker guest_birthday;
    public TextArea add_info;
    public Button find_available_room;

    public void initialize(URL url, ResourceBundle resourceBundle){
        add_info.setWrapText(true);
        guest_birthday.setValue(LocalDate.now().minusYears(18));
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

        //Валідація електронної пошти
        guest_email.textProperty().addListener((observable, oldValue, newValue) -> {
            checkEmail(guest_email.getText(), email_valid_bar, find_available_room);
        });

        //Валідація номера телефону
        guest_phone_number.textProperty().addListener((observable, oldValue, newValue) -> {
            checkPhoneNumber(guest_phone_number.getText(), phone_valid_bar, find_available_room);
        });
    }
    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
    public void findAvailableRoomClick(ActionEvent event) throws IOException {
        if (checkIN_date.getValue() == null || checkOUT_date.getValue() == null){
            formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповненні");
            return;
        }
        if (checkOUT_date.getValue().isBefore(checkIN_date.getValue())
                || checkOUT_date.getValue().isEqual(checkIN_date.getValue())){
            formBuilder.errorValidation("Дата заїзду повинна бути раніше дати виїзду");
            return;
        }
        if(guest_birthday.getValue().isAfter(LocalDate.now().minusYears(18))){
            formBuilder.errorValidation("Лише повнолітні особи можуть створити бронювання");
            return;
        }
        TextField[] textFields = {
                guest_first_name,
                guest_second_name,
                guest_phone_number,
                guest_email,
                people_count
        };
        for(TextField textField : textFields){
            if (textField.getText().length() == 0){
                formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповненні");
                return;
            }
        }
        if (checkOUT_date.getValue().isBefore(LocalDate.now()) || checkIN_date.getValue().isBefore(LocalDate.now())){
            formBuilder.errorValidation("Не можна вибирати дати, які раніше ніж сьогоднішня");
            return;
        }
        long nightPeriod = ChronoUnit.DAYS.between(checkIN_date.getValue(), checkOUT_date.getValue());
        Booking newBooking = new Booking(
                guest_first_name.getText(),
                guest_second_name.getText(),
                guest_phone_number.getText(),
                guest_email.getText(),
                checkIN_date.getValue(),
                checkOUT_date.getValue(),
                Integer.parseInt(people_count.getText()),
                nightPeriod,
                add_info.getText()
        );
        ObservableList<Room> availableRooms = mongoDatabaseConnection.adminFindAvailableRoomsForBooking(newBooking);
        roomHolder.setRoomList(availableRooms);
        if (availableRooms.size() == 0){
            formBuilder.errorValidation("Не вдалось знайти вільних кімнат");
            return;
        }
        bookingHolder.setBooking(newBooking);
        formBuilder.openWindow(
                "admin-forms/confirm-booking-form.fxml",
                "Підтвердити бронювання",
                900,
                600
        );
    }

    public void checkEmail(String text, ProgressBar progressBar, Button button){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (pattern.matcher(text).matches()) {
            progressBar.setVisible(true);
            progressBar.setStyle("-fx-control-inner-background: green; -fx-accent: green;");
            button.setDisable(false);
        } else {
            progressBar.setVisible(true);
            progressBar.setStyle("-fx-control-inner-background: red; -fx-accent: red;");
            button.setDisable(true);
        }
    }

    public void checkPhoneNumber(String phone, ProgressBar progressBar, Button button){
        String phoneRegex = "((\\d{3} ?)|(\\d{3}-))?\\d{3}\\d{4}";
        Pattern pattern = Pattern.compile(phoneRegex);
        if(pattern.matcher(phone).matches()){
            progressBar.setVisible(true);
            progressBar.setStyle("-fx-control-inner-background: green; -fx-accent: green;");
            button.setDisable(false);
        } else {
            progressBar.setVisible(true);
            progressBar.setStyle("-fx-control-inner-background: red; -fx-accent: red;");
            button.setDisable(true);
        }
    }
}
