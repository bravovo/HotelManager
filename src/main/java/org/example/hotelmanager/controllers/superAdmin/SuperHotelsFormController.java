package org.example.hotelmanager.controllers.superAdmin;

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

public class SuperHotelsFormController implements Initializable {
    SuperAdmin superAdmin = new SuperAdmin();
    SuperAdminHolder superAdminHolder = SuperAdminHolder.getInstance();
    FormBuilder formBuilder = new FormBuilder();
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    public ToggleButton find_hotel_btn;
    public Button delete_hotel_btn;
    public Label hotels_cnt;
    public AnchorPane hotels_anchor_pane;
    public VBox find_hotel_vbox;
    public ChoiceBox<String> filter_choice;
    public TextField find_input;
    public Button find_button;
    public Button reset_button;
    public TableView<Hotel> hotel_table;
    public TableColumn hotel_id;
    public TableColumn hotel_name;
    public TableColumn hotel_email;
    public TableColumn hotel_phone_number;
    public TableColumn hotel_login;
    public TableColumn hotel_address;

    public void initialize(URL location, ResourceBundle resources) {
        superAdmin = superAdminHolder.getSuper();
        setHotelsTable();
        ObservableList<String> items = FXCollections.observableArrayList(
                "Назва", "Електронна пошта", "Номер телефону", "Логін", "Адреса"
        );
        filter_choice.setItems(items);
        filter_choice.setValue("Фільтр для пошуку");
        hotel_table.setOnMouseClicked(event -> {
            Hotel selectedHotel = hotel_table.getSelectionModel().getSelectedItem();
            if (event.getClickCount() == 1) { // Одинарне клацання
                if (selectedHotel != null) {
                    delete_hotel_btn.setDisable(false);
                    hotelHolder.setUser(selectedHotel);
                }
                else {
                    delete_hotel_btn.setDisable(true);
                }
            }
        });
    }

    private void setHotelsTable() {
        find_input.setText("");
        hotel_id.setCellValueFactory(new PropertyValueFactory<>("hotel_id"));
        hotel_name.setCellValueFactory(new PropertyValueFactory<>("hotel_name"));
        hotel_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        hotel_phone_number.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        hotel_login.setCellValueFactory(new PropertyValueFactory<>("login"));
        hotel_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        ObservableList<Hotel> hotels = superAdmin.getHotels();
        hotel_table.setItems(hotels);

        hotels_cnt.setText(String.valueOf(hotels.size()));
    }

    public void setHotelsTable(ObservableList<Hotel> hotelList){
        hotel_id.setCellValueFactory(new PropertyValueFactory<>("hotel_id"));
        hotel_name.setCellValueFactory(new PropertyValueFactory<>("hotel_name"));
        hotel_email.setCellValueFactory(new PropertyValueFactory<>("email"));
        hotel_phone_number.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        hotel_login.setCellValueFactory(new PropertyValueFactory<>("login"));
        hotel_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        hotel_table.setItems(hotelList);
    }

    public void findHotelButtonClick(ActionEvent actionEvent) {
        if(find_hotel_btn.isSelected()){
            AnchorPane.setTopAnchor(hotel_table, 80.0);
            find_button.setVisible(true);
            hotel_table.setPrefHeight(541);
        }else {
            setHotelsTable();
            AnchorPane.setTopAnchor(hotel_table, 0.0);
            find_button.setVisible(false);
            hotel_table.setPrefHeight(621);
        }
    }

    public void deleteHotelButtonClick(ActionEvent actionEvent) throws IOException {
        formBuilder.openDialog("super-confirm-hotel-delete-form.fxml", "Підтвердити видалення", 300, 200);
        setHotelsTable();
        delete_hotel_btn.setDisable(true);
    }

    public void findButtonClick(ActionEvent actionEvent) {
        ObservableList<Hotel> hotelList = FXCollections.observableArrayList();
        String filter;
        String value;
        if (filter_choice.getValue().equals("Фільтр для пошуку")){
            formBuilder.errorValidation("Фільтр для пошуку повинен бути вибраний!");
            return;
        }
        switch (filter_choice.getValue()){
            case "Назва" -> {
                String hotelName = find_input.getText();
                filter = "Назва";
                value = hotelName;
                hotelList = findHotelsByFilterAndValue(filter, value);
            }
            case "Електронна пошта" -> {
                String hotelEmail = find_input.getText();
                filter = "Електронна пошта";
                value = String.valueOf(hotelEmail);
                hotelList = findHotelsByFilterAndValue(filter, value);
            }
            case "Номер телефону" -> {
                String phoneNumber = find_input.getText().replaceAll("[^0-9]", "");
                filter = "Номер телефону";
                value = phoneNumber;
                hotelList = findHotelsByFilterAndValue(filter, value);
            }
            case "Логін" -> {
                String hotelLogin = find_input.getText();
                filter = "Логін";
                value = String.valueOf(hotelLogin);
                hotelList = findHotelsByFilterAndValue(filter, value);
            }
            case "Адреса" -> {
                String hotelAddress = find_input.getText();
                filter = "Адреса";
                value = String.valueOf(hotelAddress);
                hotelList = findHotelsByFilterAndValue(filter, value);
            }
        }
        setHotelsTable(hotelList);
    }

    private ObservableList<Hotel> findHotelsByFilterAndValue(String filter, String value) {
        ObservableList<Hotel> hotels = FXCollections.observableArrayList();
        for(Hotel hotelFromList : superAdmin.getHotels()){
            switch (filter){
                case "Назва" -> {
                    if(hotelFromList.getHotel_name().equals(value)){
                        hotels.add(hotelFromList);
                    }
                }
                case "Електронна пошта" -> {
                    if(hotelFromList.getEmail().equals(value)){
                        hotels.add(hotelFromList);
                    }
                }
                case "Номер телефону" -> {
                    if(hotelFromList.getPhone_number().equals(value)){
                        hotels.add(hotelFromList);
                    }
                }
                case "Логін" -> {
                    if(hotelFromList.getLogin().equals(value)){
                        hotels.add(hotelFromList);
                    }
                }
                case "Адреса" -> {
                    if(hotelFromList.getAddress().equals(value)){
                        hotels.add(hotelFromList);
                    }
                }
            }
        }
        return hotels;
    }

    public void resetTableButtonClick(ActionEvent actionEvent) {
        setHotelsTable();
    }
}
