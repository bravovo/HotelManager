package org.example.hotelmanager.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;

import java.net.URL;
import java.util.ResourceBundle;


public class AfterRegisterFormController implements Initializable {

    @FXML private TextField email_field;
    @FXML private Button later_btn;
    @FXML private Button ok_btn;
    @FXML private TextField rooms_count_field;
    @FXML private VBox stars_vbox;
    @FXML private ChoiceBox<Integer> stars_count_field;

    FormBuilder formBuilder = new FormBuilder();

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();

    public void initialize(URL url, ResourceBundle resourceBundle){
        rooms_count_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                rooms_count_field.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        ObservableList<Integer> items = FXCollections.observableArrayList(
                1, 2, 3, 4, 5
        );
        ((ChoiceBox)stars_count_field).setItems(items);
        stars_count_field.setValue(0);
    }

    public void laterButtonCLick(ActionEvent event){
        Stage stage = (Stage) later_btn.getScene().getWindow();
        stage.close();
    }

    public void okButtonClick(ActionEvent event){
        if(stars_count_field.getValue() == 0
                || email_field.getText().length() == 0
                ||rooms_count_field.getText().length() == 0){
            formBuilder.errorValidation("Усі поля повинні бути заповнені");
            return;
        }
        String roomsCountString = rooms_count_field.getText();
        int roomsCount = Integer.parseInt(roomsCountString);
        if (roomsCount > 25){
            formBuilder.errorValidation("Кількість кімнат не може перевищувати 25");
            return;
        }
        int starsCount = stars_count_field.getValue();
        String email = email_field.getText();
        mongoDatabaseConnection.updateHotel(email, roomsCount, starsCount);
        Stage stage = (Stage) ok_btn.getScene().getWindow();
        stage.close();
    }
}
