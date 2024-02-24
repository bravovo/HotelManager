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

import java.net.URL;
import java.util.ResourceBundle;


public class AfterRegisterFormController implements Initializable {

    @FXML private TextField email_field;
    @FXML private Button later_btn;
    @FXML private Button ok_btn;
    @FXML private TextField rooms_count_field;
    @FXML private VBox stars_vbox;
    @FXML private ChoiceBox<Integer> starts_count_field;

    public void initialize(URL url, ResourceBundle resourceBundle){
        ObservableList<Integer> items = FXCollections.observableArrayList(
                1, 2, 3, 4, 5
        );
        ((ChoiceBox)starts_count_field).setItems(items);
        starts_count_field.setValue(1);
    }

    public void laterButtonCLick(ActionEvent event){
        Stage stage = (Stage) later_btn.getScene().getWindow();
        stage.close();
    }
}
