package org.example.hotelmanager.controllers.admin;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;

import java.io.IOException;

public class AdminSettingsFormController {
    public Button delete_acc_btn;

    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();

    public void deleteAccountButtonClick(ActionEvent event) throws IOException {
        hotel = hotelHolder.getUser();
        formBuilder.openDialog("admin-forms/confirm-deleting-acc-form.fxml",
                "Видалити акаунт",
                300, 200);
        if(hotelHolder.isDone()){
            Stage stage = (Stage) delete_acc_btn.getScene().getWindow();
            stage.close();
        }
    }
}
