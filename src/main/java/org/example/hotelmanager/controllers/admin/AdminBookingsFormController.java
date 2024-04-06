package org.example.hotelmanager.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.model.*;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminBookingsFormController implements Initializable {
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    FormBuilder formBuilder = new FormBuilder();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    @FXML private TableColumn<Booking, Integer> booking_id;
    @FXML private TableColumn<Booking, String> booking_status;
    @FXML private TableView<Booking> booking_table;
    @FXML private AnchorPane bookings_anchor_pane;
    @FXML private Label bookings_count;
    @FXML private TableColumn<Booking, LocalDate> check_in;
    @FXML private TableColumn<Booking, LocalDate> check_out;
    @FXML private Button create_booking_btn;
    @FXML private Button delete_booking_btn;
    @FXML private ChoiceBox<String> filter_choice;
    @FXML private ToggleButton find_booking_btn;
    @FXML private VBox find_booking_vbox;
    @FXML private Button find_button;
    @FXML private TextField find_input;
    @FXML private TableColumn<Booking, String> guest_first_name;
    @FXML private TableColumn<Booking, String> guest_second_name;
    @FXML private Button reset_button;
    @FXML private TableColumn<Booking, Integer> room_number;
    @FXML private TableColumn<Booking, Double> total_price;

    public void initialize(URL url, ResourceBundle resourceBundle){
        hotel = hotelHolder.getUser();
        setBookingsTable();
        ObservableList<String> items = FXCollections.observableArrayList(
                "ID", "Ім'я гостя", "Прізвище гостя", "Cтатус", "Номер кімнати", "Вартість"
        );
        ((ChoiceBox)filter_choice).setItems(items);
        filter_choice.setValue("Фільтр для пошуку");
        booking_table.setOnMouseClicked(event -> {
            Booking selectedBooking = booking_table.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 2) { // Перевіряємо, чи було зроблено подвійне клацання
                if (selectedBooking != null) {
                    bookingHolder.setBooking(selectedBooking);
                    try{
                        if(selectedBooking.getBookingStatus().equals("Виконується")){
                            formBuilder.openWindow("admin-forms/see-booking-form.fxml", "Переглянути діюче бронювання", 700, 500);
                        }else {
                            formBuilder.openWindow("admin-forms/edit-booking-form.fxml", "Редагувати бронювання", 700, 500);
                            setBookingsTable();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            } else if (event.getClickCount() == 1) { // Одинарне клацання
                if (selectedBooking != null) {
                    delete_booking_btn.setDisable(false);
                    bookingHolder.setBooking(selectedBooking);
                }
            }
        });
    }
    private void setBookingsTable() {
        hotel = hotelHolder.getUser();
        booking_id.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        guest_first_name.setCellValueFactory(new PropertyValueFactory<>("guestFirstName"));
        guest_second_name.setCellValueFactory(new PropertyValueFactory<>("guestSecondName"));
        room_number.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        booking_status.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
        check_in.setCellValueFactory(new PropertyValueFactory<>("checkIN_date"));
        check_out.setCellValueFactory(new PropertyValueFactory<>("checkOUT_date"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        ObservableList<Booking> bookings = hotel.getBookingsForList();
        booking_table.setItems(bookings);

        bookings_count.setText(String.valueOf(bookings.size()));
    }
    private void setBookingsTable(ObservableList<Booking> bookings) {
        booking_id.setCellValueFactory(new PropertyValueFactory<>("bookingID"));
        guest_first_name.setCellValueFactory(new PropertyValueFactory<>("guestFirstName"));
        guest_second_name.setCellValueFactory(new PropertyValueFactory<>("guestSecondName"));
        room_number.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        booking_status.setCellValueFactory(new PropertyValueFactory<>("bookingStatus"));
        check_in.setCellValueFactory(new PropertyValueFactory<>("checkIN_date"));
        check_out.setCellValueFactory(new PropertyValueFactory<>("checkOUT_date"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        booking_table.setItems(bookings);
    }
    public void deleteBookingButtonClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("confirming-booking-dialog-form.fxml", "Підтвердити видалення", 300, 200);
        setBookingsTable();
        delete_booking_btn.setDisable(true);
    }
    public void createBookingClick(ActionEvent event) throws IOException {
        formBuilder.openWindow("admin-forms/find-available-room-form.fxml", "Створення бронювання", 800, 500);
        setBookingsTable();
    }
    public void findBookingButtonClick(ActionEvent event){
        if(find_booking_btn.isSelected()){
            AnchorPane.setTopAnchor(booking_table, 80.0);
            find_booking_vbox.setVisible(true);
            booking_table.setPrefHeight(541);
        }else {
            setBookingsTable();
            AnchorPane.setTopAnchor(booking_table, 0.0);
            find_booking_vbox.setVisible(false);
            find_input.setText("");
            booking_table.setPrefHeight(621);
        }
    }
    public void findBookingClick(ActionEvent event){
        String filter;
        String value;
        if (filter_choice.getValue().equals("Фільтр для пошуку")){
            formBuilder.errorValidation("Фільтр для пошуку повинен бути вибраний!");
            return;
        }
        switch (filter_choice.getValue()){
            case "ID" -> {
                int bookingID;
                try{
                    bookingID = Integer.parseInt(find_input.getText());
                    filter = "ID";
                    value = String.valueOf(bookingID);
                }catch (Exception e){
                    FormBuilder formBuilder = new FormBuilder();
                    formBuilder.errorValidation("ID бронювання повинно бути числом!");
                    find_input.setText("");
                    return;
                }
                setBookingsTable(findBookingsByFilterAndValue(filter, value));
            }
            case "Cтатус" -> {
                String status = find_input.getText();
                filter = "Cтатус";
                value = status;
                setBookingsTable(findBookingsByFilterAndValue(filter, value));
            }
            case "Ім'я гостя" -> {
                String guestFirstName = find_input.getText();
                filter = "Ім'я гостя";
                value = guestFirstName;
                setBookingsTable(findBookingsByFilterAndValue(filter, value));
            }
            case "Прізвище гостя" -> {
                String guestSecondName = find_input.getText();
                filter = "Прізвище гостя";
                value = guestSecondName;
                setBookingsTable(findBookingsByFilterAndValue(filter, value));
            }
            case "Номер кімнати" -> {
                int room_number;
                try{
                    room_number = Integer.parseInt(find_input.getText());
                    filter = "Номер кімнати";
                    value = String.valueOf(room_number);
                }catch (Exception e){
                    FormBuilder formBuilder = new FormBuilder();
                    formBuilder.errorValidation("Номер кімнати повинен бути числом!");
                    find_input.setText("");
                    return;
                }
                setBookingsTable(findBookingsByFilterAndValue(filter, value));
            }
            case "Вартість" -> {
                double totalPrice;
                try{
                    totalPrice = Double.parseDouble(find_input.getText());
                    filter = "Вартість";
                    value = String.valueOf(totalPrice);
                }catch (Exception e){
                    FormBuilder formBuilder = new FormBuilder();
                    formBuilder.errorValidation("Вартість повинна бути числом!");
                    find_input.setText("");
                    return;
                }
                setBookingsTable(findBookingsByFilterAndValue(filter, value));
            }
        }
    }
    public ObservableList<Booking> findBookingsByFilterAndValue(String filter, String value){
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        for(Booking booking : hotel.getBookingsForList()){
            switch (filter){
                case "ID" -> {
                    int IDvalue = Integer.parseInt(value);
                    if (booking.getBookingID() == IDvalue){
                        bookings.add(booking);
                    }
                }
                case "Номер кімнати" -> {
                    int roomNumberValue = Integer.parseInt(value);
                    if (booking.getRoomNumber() == roomNumberValue){
                        bookings.add(booking);
                    }
                }
                case "Вартість" -> {
                    double totalPriceValue = Double.parseDouble(value);
                    if(booking.getTotalPrice() == totalPriceValue){
                        bookings.add(booking);
                    }
                }
                case "Ім'я гостя" -> {
                    if(booking.getGuestFirstName().equals(value)){
                        bookings.add(booking);
                    }
                }
                case "Прізвище гостя" -> {
                    if(booking.getGuestSecondName().equals(value)){
                        bookings.add(booking);
                    }
                }
                case "Cтатус" -> {
                    if(booking.getBookingStatus().equals(value)){
                        bookings.add(booking);
                    }
                }
            }
        }
        return bookings;
    }
    public void resetTableButtonClick(ActionEvent event){
        find_input.setText("");
        setBookingsTable();
    }
}
