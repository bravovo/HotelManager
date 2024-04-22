package org.example.hotelmanager.controllers.client;

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
    public TextField date_of_birth_field;
    public Button save_btn;
    public Button cancel_btn;
    public ButtonBar btns_bar;

    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    ChangeListener<String> changeListener;
    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();

    public void initialize(URL url, ResourceBundle resourceBundle){
        client = clientHolder.getUser();
        cancel_btn.setCancelButton(true);
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
        date_of_birth_field.setText(client.getDateOfBirth().toString());

        first_name_field.textProperty().addListener(changeListener);
        second_name_field.textProperty().addListener(changeListener);
        email_field.textProperty().addListener(changeListener);
        phone_number_field.textProperty().addListener(changeListener);

        //Валідація електронної пошти
        email_field.textProperty().addListener((observable, oldValue, newValue) -> {
            checkEmail(email_field.getText(), email_field, save_btn);
        });

        phone_number_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                phone_number_field.setText(newValue.replaceAll("\\D", ""));
            }
        });
    }

    public void checkEmail(String text, TextField email, Button button){
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (pattern.matcher(text).matches()) {
            email.setStyle("    -fx-background-color: #FFFFFF;\n" +
                    "    -fx-font-size: 14px;\n" +
                    "    -fx-border-color: transparent;\n" +
                    "    -fx-background-radius: 15px;\n" +
                    "    -fx-padding: 12px;\n" +
                    "    -fx-effect: dropshadow(gaussian, rgba(0, 255, 0, 1), 10, 0, 0, 0);"
            );
            button.setDisable(false);
        } else {
            email.setStyle("-fx-background-color: #FFFFFF;\n" +
                    "    -fx-font-size: 14px;\n" +
                    "    -fx-border-color: transparent;\n" +
                    "    -fx-background-radius: 15px;\n" +
                    "    -fx-padding: 12px;\n" +
                    "    -fx-effect: dropshadow(gaussian, rgba(255, 0, 0, 1), 10, 0, 0, 0);"
            );
            button.setDisable(true);
        }
    }

    public void saveButtonClick(ActionEvent event) {
        Client editedClient = client;
        editedClient.setFirstName(first_name_field.getText());
        editedClient.setLastName(second_name_field.getText());
        editedClient.setEmail(email_field.getText());
        editedClient.setPhoneNumber(phone_number_field.getText());
        //clientHolder.setUser(editedClient);
        if(!mongoDatabaseConnection.editClientProfile(editedClient)){
            FormBuilder formBuilder = new FormBuilder();
            formBuilder.errorValidation("Вибрана електронна пошта вже зайнята");
            return;
        }
        Stage stage = (Stage) save_btn.getScene().getWindow();
        stage.close();
    }

    public void cancelButtonClick(ActionEvent event) {
        Stage stage = (Stage) cancel_btn.getScene().getWindow();
        stage.close();
    }
}
