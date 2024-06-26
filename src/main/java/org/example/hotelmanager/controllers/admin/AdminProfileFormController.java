package org.example.hotelmanager.controllers.admin;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class AdminProfileFormController implements Initializable {
    public VBox hotel_name_vbox;
    public TextField hotel_name_field;
    public VBox login_vbox;
    public TextField login_field;
    public VBox address_vbox;
    public TextField address_field;
    public VBox room_number_vbox;
    public TextField room_number_field;
    public VBox phone_number_vbox;
    public TextField phone_number_field;
    public VBox email_vbox;
    public TextField email_field;
    public ProgressBar email_valid_bar;
    public ProgressBar phone_valid_bar;
    public ButtonBar btns_bar;
    public Button save_btn;
    public Button cancel_btn;

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    ChangeListener<String> changeListener;
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();

    public void initialize(URL url, ResourceBundle resourceBundle){
        hotel = hotelHolder.getUser();
        changeListener = (observable, oldValue, newValue) -> {
            btns_bar.setVisible(!Objects.equals(hotel.getHotel_name(), hotel_name_field.getText())
                    || !Objects.equals(hotel.getLogin(), login_field.getText())
                    || !Objects.equals(hotel.getPhone_number(), phone_number_field.getText())
                    || !Objects.equals(hotel.getEmail(), email_field.getText()));
        };

        cancel_btn.setCancelButton(true);
        email_valid_bar.setVisible(false);

        setProfile(hotel);

        // Валідація електронної пошти
        email_field.textProperty().addListener((observable, oldValue, newValue) -> {
            checkEmail(email_field.getText(), email_valid_bar, save_btn);
        });

        // Валідація номера телефону
        phone_number_field.textProperty().addListener((observable, oldValue, newValue) -> {
            checkPhoneNumber(phone_number_field.getText(), phone_valid_bar, save_btn);
        });

        phone_number_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone_number_field.setText(newValue.replaceAll("\\D", ""));
            }
        });

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

    public void saveButtonClick(ActionEvent event) {
        if(hotel_name_field.getText().length() == 0
                || login_field.getText().length() == 0
                || email_field.getText().length() == 0
                || phone_number_field.getText().length() == 0
        ){
            FormBuilder formBuilder = new FormBuilder();
            formBuilder.errorValidation("Всі обов'язков поля повинні бути заповненні");
            return;
        }
        Hotel editedHotel = new Hotel(
                hotel.getHotel_id(),
                hotel_name_field.getText(),
                address_field.getText(),
                login_field.getText(),
                email_field.getText(),
                Integer.parseInt(room_number_field.getText()),
                phone_number_field.getText()
        );
        if(!mongoDatabaseConnection.editHotelProfile(editedHotel)){
            FormBuilder formBuilder = new FormBuilder();
            formBuilder.errorValidation("Вибранй логін вже зайнятий");
            return;
        }
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }

    public void setProfile(Hotel hotel){
        hotel_name_field.setText(hotel.getHotel_name());
        login_field.setText(hotel.getLogin());
        address_field.setText(hotel.getAddress());
        room_number_field.setText(String.valueOf(hotel.getRooms_count()));
        phone_number_field.setText(hotel.getPhone_number());
        email_field.setText(hotel.getEmail());

        hotel_name_field.textProperty().addListener(changeListener);
        login_field.textProperty().addListener(changeListener);
        room_number_field.textProperty().addListener(changeListener);
        email_field.textProperty().addListener(changeListener);
        phone_number_field.textProperty().addListener(changeListener);
    }
}
