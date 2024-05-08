package org.example.hotelmanager.controllers.client;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientMyBookingsFormController implements Initializable {

    public Label your_bookings_text;
    public VBox edit_booking_1;
    public VBox hotel_name_vbox;
    public Text hotel_name;
    public VBox room_number_vbox;
    public Text room_number;
    public VBox notes_vbox;
    public TextArea notes_area;
    public VBox edit_booking_2;
    public VBox checkIN_date_vbox;
    public DatePicker checkIN_picker;
    public VBox checkOUT_date_vbox;
    public DatePicker checkOUT_picker;
    public VBox people_count_vbox;
    public TextField people_count_field;
    public VBox total_price_vbox;
    public Text total_price;
    public Button edit_booking_btn;
    public Button delete_booking_btn;
    public Separator separator;
    public ScrollPane scroll_pane;
    public HBox bookings_hbox;
    FormBuilder formBuilder = new FormBuilder();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    Booking bookingToEdit = new Booking();
    Booking chosenBooking = new Booking();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    ObservableList<Booking> bookings;
    ObservableList<Hotel> hotels;
    Map<VBox, Booking> bookingVBoxMap = new HashMap<>();
    ChangeListener<String> changeListener;
    ChangeListener<LocalDate> changeListenerForDate;
    String addInfoFirst = "";
    String peopleCountFirst = "";
    LocalDate checkINFirst = LocalDate.now();
    LocalDate checkOUTFirst = LocalDate.now();
    public void initialize(URL url, ResourceBundle resourceBundle) {
        scroll_pane.setStyle("-fx-background-color: white; -fx-border-width: 0px;");
        changeListener = (observable, oldValue, newValue) -> {
            if (Objects.equals(addInfoFirst, notes_area.getText())
                    && Objects.equals(peopleCountFirst, people_count_field.getText())
                    && Objects.equals(checkINFirst, checkIN_picker.getValue())
                    && Objects.equals(checkOUTFirst, checkOUT_picker.getValue())
            ) {
                edit_booking_btn.setDisable(true);
            }
            else {
                edit_booking_btn.setDisable(false);
            }
        };
        changeListenerForDate = (observable, oldValue, newValue) -> {
            if (Objects.equals(addInfoFirst, notes_area.getText())
                    && Objects.equals(peopleCountFirst, people_count_field.getText())
                    && Objects.equals(checkINFirst, checkIN_picker.getValue())
                    && Objects.equals(checkOUTFirst, checkOUT_picker.getValue())) {
                edit_booking_btn.setDisable(true);
            }
            else {
                edit_booking_btn.setDisable(false);
            }
        };
        people_count_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\d*)?")) {
                people_count_field.setText(oldValue);
            }
        });
        setPage();
    }
    public void editBookingButtonClick(ActionEvent event) throws IOException{
        if (checkIN_picker.getValue() == null
                || checkOUT_picker.getValue() == null
                || people_count_field.getText().length() == 0){
            formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповненні");
            return;
        }
        if (checkOUT_picker.getValue().isBefore(checkIN_picker.getValue())
                || checkOUT_picker.getValue().isEqual(checkIN_picker.getValue())){
            formBuilder.errorValidation("Дата заїзду повинна бути раніше дати виїзду");
            return;
        }
        if (checkOUT_picker.getValue().isBefore(LocalDate.now())
                || checkIN_picker.getValue().isBefore(LocalDate.now())){
            formBuilder.errorValidation("Не можна вибирати дати, які раніше ніж сьогоднішня");
            return;
        }
        bookingToEdit = bookingHolder.getBooking();
        bookingToEdit.setAdditionalInfo(notes_area.getText());
        bookingToEdit.setCheckIN_date(checkIN_picker.getValue());
        bookingToEdit.setCheckOUT_date(checkOUT_picker.getValue());
        bookingToEdit.setPeopleCount(Integer.parseInt(people_count_field.getText()));

        long nightPeriod = ChronoUnit.DAYS.between(checkIN_picker.getValue(), checkOUT_picker.getValue());
        bookingToEdit.setNightCount(nightPeriod);
        bookingHolder.setBooking(bookingToEdit);

        if(peopleCountFirst.equals(people_count_field.getText())
                && checkINFirst.isEqual(checkIN_picker.getValue())
                && checkOUTFirst.isEqual(checkOUT_picker.getValue())){
            mongoDatabaseConnection.editClientBooking(bookingToEdit, false);
            bookingHolder.setBookingDone(true);
        }
        else {
            formBuilder.openDialog("confirming-booking-editing-form.fxml", "Редагування бронювання", 400, 250);
        }
        if(bookingHolder.getBookingDone()){
            setPage();
            bookingHolder.setBookingDone(false);
        }
    }

    public void deleteBookingButtonClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("confirming-booking-dialog-form.fxml", "Видалити бронювання", 300, 200);
        if(bookingHolder.getBookingDone()) {
            bookingHolder.setBookingDone(false);
            setPage();
        }
    }
    private void setPage() {
        notes_area.textProperty().addListener(changeListener);
        people_count_field.textProperty().addListener(changeListener);
        checkIN_picker.valueProperty().addListener(changeListenerForDate);
        checkOUT_picker.valueProperty().addListener(changeListenerForDate);
        bookings_hbox.getChildren().clear();

        hotel_name.setText("");
        room_number.setText("");
        total_price.setText("");
        checkIN_picker.setValue(null);
        checkOUT_picker.setValue(null);
        people_count_field.setText("");
        notes_area.setText("");

        bookings = mongoDatabaseConnection.getClientBookings();
        hotels = mongoDatabaseConnection.getHotels();

        if(bookings.size() != 0){
            for(Booking booking : bookings){
                String hotelName = "";
                for(Hotel hotel : hotels){
                    if(hotel.getHotel_id().toString().equals(booking.getHotelID().toString())){
                        hotelName = hotel.getHotel_name();
                    }
                }
                Label hotelNameLabel = new Label("Готель: " + hotelName);
                Label checkIN = new Label("Дата заїзду: " + booking.getCheckIN_date());
                Label checkOUT = new Label("Дата виїзду: " + booking.getCheckOUT_date());
                Label peopleCountLabel = new Label("Кількість людей: " + booking.getPeopleCount());
                Label totalPrice = new Label("Загальна вартість проживання:\n" + booking.getTotalPrice());

                VBox vbox = new VBox();
                double prefWidth = 250;
                double prefHeight = 200;
                vbox.setPrefWidth(prefWidth);
                vbox.setMinWidth(prefWidth);
                vbox.setPrefHeight(prefHeight);
                vbox.setMaxHeight(prefHeight);
                vbox.setSpacing(12.5);
                vbox.getChildren().addAll(
                        hotelNameLabel,
                        checkIN,
                        checkOUT,
                        peopleCountLabel,
                        totalPrice
                );

                // Стилі маленьких форм бронювань
                vbox.getStyleClass().add("booking-form");
                hotelNameLabel.getStyleClass().add("booking-form-label");
                checkIN.getStyleClass().add("booking-form-label");
                checkOUT.getStyleClass().add("booking-form-label");
                peopleCountLabel.getStyleClass().add("booking-form-label");
                totalPrice.getStyleClass().add("total-price-label");

                vbox.setOnMouseEntered(event -> vbox.setCursor(Cursor.HAND));
                if(bookingVBoxMap.size() >= 4){
                    scroll_pane.setFitToHeight(false);
                }
                vbox.setOnMouseExited(event -> vbox.setCursor(Cursor.DEFAULT));
                String finalHotelName = hotelName;
                vbox.setOnMouseClicked(event -> {
                    try{
                        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 1) {
                            chosenBooking = bookingVBoxMap.get(vbox);
                            bookingHolder.setBooking(chosenBooking);
                            hotel_name.setText(finalHotelName);
                            room_number.setText(String.valueOf(booking.getRoomNumber()));
                            notes_area.setWrapText(true);
                            notes_area.setText(booking.getAdditionalInfo());
                            checkIN_picker.setValue(booking.getCheckIN_date());
                            checkOUT_picker.setValue(booking.getCheckOUT_date());
                            people_count_field.setText(String.valueOf(booking.getPeopleCount()));
                            total_price.setText(String.valueOf(booking.getTotalPrice()));
                            addInfoFirst = booking.getAdditionalInfo();
                            peopleCountFirst = String.valueOf(booking.getPeopleCount());
                            checkINFirst = booking.getCheckIN_date();
                            checkOUTFirst = booking.getCheckOUT_date();
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                    edit_booking_btn.setDisable(true);
                    delete_booking_btn.setDisable(false);
                });
                bookings_hbox.getChildren().add(vbox);
                bookingVBoxMap.put(vbox, booking);
            }
        }
        else {
            notes_area.setDisable(true);
            people_count_field.setDisable(true);
            checkIN_picker.setDisable(true);
            checkOUT_picker.setDisable(true);
        }
        edit_booking_btn.setDisable(true);
        delete_booking_btn.setDisable(true);
    }
}
