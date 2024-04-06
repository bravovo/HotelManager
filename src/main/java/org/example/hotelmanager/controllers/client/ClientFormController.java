package org.example.hotelmanager.controllers.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.ManagerApplication;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {
    public VBox profile_vbox;
    ClientHolder clientHolder = ClientHolder.getInstance();
    Client client;
    public Button create_booking_form_btn;
    public Button my_bookings_form_btn;
    public BorderPane client_border_pane;
    public Label client_label;
    public Button profile_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = clientHolder.getUser();
        client_label.setText(client.getFirstName() + " " + client.getLastName());
        changeCenter("client-forms/client-create-booking-form.fxml");
        create_booking_form_btn.setDisable(true);
        profile_vbox.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                FormBuilder formBuilder = new FormBuilder();
                try{
                    formBuilder.openDialog("client-forms/client-profile-form.fxml",
                            "Профіль користувача | Hotelis",
                            450, 500);
                }catch (Exception e){
                    e.printStackTrace();
                }
                client_label.setText(client.getFirstName() + " " + client.getLastName());
            }
        });
    }
    public void changeCenter(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(ManagerApplication.class.getResource(fxmlFile));
            Node centerContent = loader.load();
            client_border_pane.setCenter(centerContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logoutButtonClick(javafx.event.ActionEvent event){
        System.exit(0);
    }

    public void createBookingFormButtonClick(ActionEvent event) {
        changeCenter("client-forms/client-create-booking-form.fxml");
        my_bookings_form_btn.setDisable(false);
        create_booking_form_btn.setDisable(true);
    }
    public void myBookingsFormButtonClick(ActionEvent event) {
        changeCenter("client-forms/client-my-bookings-form.fxml");
        create_booking_form_btn.setDisable(false);
        my_bookings_form_btn.setDisable(true);
    }

    public void profileButtonClick(ActionEvent event) throws IOException {
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openDialog("client-forms/client-profile-form.fxml",
                "Профіль користувача | Hotelis",
                450, 500);
        client_label.setText(client.getFirstName() + " " + client.getLastName());
    }
}
