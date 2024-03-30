package org.example.hotelmanager.controllers.client;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import org.example.hotelmanager.ManagerApplication;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientFormController implements Initializable {
    ClientHolder clientHolder = ClientHolder.getInstance();
    Client client;
    public BorderPane client_border_pane;
    public Label hotel_label;
    public ImageView image_view;
    public Button guests_btn;
    public Button rooms_btn;
    public Button settings_btn;
    public Button logout_btn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        client = clientHolder.getUser();
        hotel_label.setText(client.getFirstName() + " " + client.getLastName());
        changeCenter("client-forms/client-create-booking-form.fxml");
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
}
