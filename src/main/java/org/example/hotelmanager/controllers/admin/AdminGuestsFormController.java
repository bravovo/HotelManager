package org.example.hotelmanager.controllers.admin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminGuestsFormController implements Initializable {
    public ToggleButton find_guest_btn;
    public Button delete_guest_btn;
    public Label guest_count;
    public AnchorPane guests_anchor_pane;
    public VBox find_guest_vbox;
    public ChoiceBox filter_choice;
    public TextField find_input;
    public Button find_button;
    public Button reset_button;
    public TableView guest_table;
    public TableColumn guest_id;
    public TableColumn guest_first_name;
    public TableColumn guest_second_name;
    public TableColumn room_number;
    public TableColumn phone_number;
    public TableColumn email;
    public TableColumn total_price;
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    Guest guest = new Guest();
    GuestHolder guestHolder = GuestHolder.getInstance();
    FormBuilder formBuilder = new FormBuilder();

    public void initialize(URL url, ResourceBundle resourceBundle){
        setGuestTable();
        ObservableList<String> items = FXCollections.observableArrayList(
                "ID", "Ім'я", "Прізвище", "Номер кімнати", "Номер телефону", "Email", "Загальна вартість"
        );
        filter_choice.setItems(items);
        filter_choice.setValue("Фільтр для пошуку");
        guest_table.setOnMouseClicked(event -> {
            Guest selectedGuest = (Guest) guest_table.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 1) { // Перевірка на одинарне клацання
                if (selectedGuest != null) {
                    delete_guest_btn.setDisable(false);
                    guestHolder.setUser(selectedGuest);
                }
                else {
                    delete_guest_btn.setDisable(true);
                }
            }
        });
    }

    public void findGuestButtonClick(ActionEvent event) {
        if(find_guest_btn.isSelected()){
            AnchorPane.setTopAnchor(guest_table, 80.0);
            find_guest_vbox.setVisible(true);
            guest_table.setPrefHeight(541);
        }else {
            setGuestTable();
            AnchorPane.setTopAnchor(guest_table, 0.0);
            find_guest_vbox.setVisible(false);
            guest_table.setPrefHeight(621);
        }
    }

    public void deleteGuestButtonClick(ActionEvent event) throws IOException {
        formBuilder.openDialog("confirming-guest-deleting-form.fxml", "Підтвердити видалення", 300, 200);
        setGuestTable();
        delete_guest_btn.setDisable(true);
    }
    public void setGuestTable(){
        find_input.setText("");
        hotel = hotelHolder.getUser();
        guest_id.setCellValueFactory(new PropertyValueFactory<>("guestID"));
        guest_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        guest_second_name.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        phone_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        room_number.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        ObservableList<Guest> guests = hotel.getGuests();
        guest_table.setItems(guests);

        guest_count.setText(String.valueOf(guests.size()));
    }
    public void setGuestTable(ObservableList<Guest> roomList){
        room_number.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        guest_id.setCellValueFactory(new PropertyValueFactory<>("guestID"));
        guest_first_name.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        guest_second_name.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        phone_number.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        total_price.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        guest_table.setItems(roomList);
    }

    public void findButtonClick(ActionEvent event){
        ObservableList<Guest> guestList = FXCollections.observableArrayList();
        String filter;
        String value;
        if (filter_choice.getValue().equals("Фільтр для пошуку")){
            formBuilder.errorValidation("Фільтр для пошуку повинен бути вибраний!");
            return;
        }
        switch (filter_choice.getValue().toString()){
            case "ID" -> {
                int id = 0;
                try{
                    id = Integer.parseInt(find_input.getText());
                    filter = "ID";
                    value = String.valueOf(id);
                }catch (Exception e){
                    formBuilder.errorValidation("ID повинен бути числом!");
                    find_input.setText("");
                    return;
                }
                guestList = findGuestsByFilterAndValue(filter, value);
            }
            case "Ім'я" -> {
                String firstName = find_input.getText();
                filter = "Ім'я";
                value = firstName;
                guestList = findGuestsByFilterAndValue(filter, value);
            }
            case "Прізвище" -> {
                String secondName = find_input.getText();
                filter = "Прізвище";
                value = secondName;
                guestList = findGuestsByFilterAndValue(filter, value);
            }
            case "Номер кімнати" -> {
                int room_number = 0;
                try{
                    room_number = Integer.parseInt(find_input.getText());
                    filter = "Номер кімнати";
                    value = String.valueOf(room_number);
                }catch (Exception e){
                    formBuilder.errorValidation("Номер кімнати повинен бути числом!");
                    find_input.setText("");
                    return;
                }
                guestList = findGuestsByFilterAndValue(filter, value);
            }
            case "Номер телефону" -> {
                int phoneNumber = 0;
                try{
                    phoneNumber = Integer.parseInt(find_input.getText());
                    filter = "Номер телефону";
                    value = String.valueOf(phoneNumber);
                }catch (Exception e){
                    formBuilder.errorValidation("Номер телефону повинен бути числом!");
                    find_input.setText("");
                    return;
                }
                guestList = findGuestsByFilterAndValue(filter, value);
            }
            case "Email" -> {
                String email = find_input.getText();
                filter = "Email";
                value = email;
                guestList = findGuestsByFilterAndValue(filter, value);
            }
            case "Загальна вартість" -> {
                double totalPrice = 0;
                try{
                    totalPrice = Double.parseDouble(find_input.getText());
                    filter = "Загальна вартість";
                    value = String.valueOf(totalPrice);
                }catch (Exception e){
                    formBuilder.errorValidation("Загальна вартість повинна бути числом!");
                    find_input.setText("");
                    return;
                }
                guestList = findGuestsByFilterAndValue(filter, value);
            }
        }
        setGuestTable(guestList);
    }
    public ObservableList<Guest> findGuestsByFilterAndValue(String filter, String value){
        ObservableList<Guest> guests = FXCollections.observableArrayList();
        for(Guest guestToFind : hotel.getGuests()){
            switch (filter){
                case "ID" -> {
                    int id = Integer.parseInt(value);
                    if(guestToFind.getGuestID() == id){
                        guests.add(guestToFind);
                    }
                }
                case "Ім'я" -> {
                    String firstName = value;
                    if(guestToFind.getFirstName().equals(firstName)){
                        guests.add(guestToFind);
                    }
                }
                case "Прізвище" -> {
                    String secondName = value;
                    if(guestToFind.getSecondName().equals(secondName)){
                        guests.add(guestToFind);
                    }
                }
                case "Номер кімнати" -> {
                    int roomNumberValue = Integer.parseInt(value);
                    if (guestToFind.getRoomNumber() == roomNumberValue){
                        guests.add(guestToFind);
                    }
                }
                case "Номер телефону" -> {
                    int phoneNumber = Integer.parseInt(value);
                    if (guestToFind.getRoomNumber() == phoneNumber){
                        guests.add(guestToFind);
                    }
                }
                case "Email" -> {
                    String email = value;
                    if(guestToFind.getEmail().equals(email)){
                        guests.add(guestToFind);
                    }
                }
                case "Загальна вартість" -> {
                    double roomPrice = Double.parseDouble(value);
                    if(guestToFind.getTotalPrice() == roomPrice){
                        guests.add(guestToFind);
                    }
                }
            }
        }
        return guests;
    }

    public void resetTableButtonClick(ActionEvent event) {
        setGuestTable();
    }
}
