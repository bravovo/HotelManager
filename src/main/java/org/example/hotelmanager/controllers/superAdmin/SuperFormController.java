package org.example.hotelmanager.controllers.superAdmin;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.ManagerApplication;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SuperFormController implements Initializable {
    public BorderPane super_border_pane;
    public Button hotels_btn;
    public Button clients_btn;
    public Button logout_btn;

    public void initialize(URL location, ResourceBundle resources) {
        changeCenter("super-admin-forms/super-hotels-form.fxml");
    }

    public void changeCenter(String fxmlFile){
        try {
            FXMLLoader loader = new FXMLLoader(ManagerApplication.class.getResource(fxmlFile));
            Node centerContent = loader.load();
            super_border_pane.setCenter(centerContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logoutButtonClick(ActionEvent actionEvent) throws IOException {
        FormBuilder formBuilder = new FormBuilder();
        formBuilder.openWindow((Stage)logout_btn.getScene().getWindow(),
                "login-form.fxml", "Авторизація | Hotelis", 700, 500);
    }

    public void hotelsFormButtonClick(ActionEvent actionEvent) {
        changeCenter("super-admin-forms/super-hotels-form.fxml");
    }

    public void clientsFormButtonClick(ActionEvent actionEvent) {
        changeCenter("super-admin-forms/super-clients-form.fxml");
    }
}
