package org.example.hotelmanager.controllers.client;

import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ClientProfileFormController implements Initializable {
    public VBox first_name_vbox;
    public TextField first_name_field;
    public VBox second_name_vbox;
    public TextField second_name_field;
    public VBox email_vbox;
    public TextField email_field;
    public VBox phone_number_vbox;
    public TextField phone_number_field;
    public VBox date_of_birth_vbox;
    public DatePicker date_of_birth_field;
    public Button save_btn;
    public Button cancel_btn;
    public ProgressBar email_valid_bar;
    public ButtonBar btns_bar;

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    ChangeListener<String> changeListener;
    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();

    public void initialize(URL url, ResourceBundle resourceBundle){
        client = clientHolder.getUser();
        cancel_btn.setCancelButton(true);
        email_valid_bar.setVisible(false);
        changeListener = (observable, oldValue, newValue) -> {
            btns_bar.setVisible(!Objects.equals(client.getFirstName(), first_name_field.getText())
                    || !Objects.equals(client.getLastName(), second_name_field.getText())
                    || !Objects.equals(client.getEmail(), email_field.getText())
                    || !Objects.equals(client.getPhoneNumber(), phone_number_field.getText()));
        };

        first_name_field.setText(client.getFirstName());
        second_name_field.setText(client.getLastName());
        email_field.setText(client.getEmail());
        phone_number_field.setText(client.getPhoneNumber());
        date_of_birth_field.setValue(client.getDateOfBirth());

        first_name_field.textProperty().addListener(changeListener);
        second_name_field.textProperty().addListener(changeListener);
        email_field.textProperty().addListener(changeListener);
        phone_number_field.textProperty().addListener(changeListener);

        //Валідація електронної пошти
        email_field.textProperty().addListener((observable, oldValue, newValue) -> {
            checkEmail(email_field.getText(), email_valid_bar, save_btn);
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

    public void saveButtonClick(ActionEvent event) {
        client.setFirstName(first_name_field.getText());
        client.setLastName(second_name_field.getText());
        client.setEmail(email_field.getText());
        client.setPhoneNumber(phone_number_field.getText());
        clientHolder.setUser(client);
        mongoDatabaseConnection.editClientProfile();
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
