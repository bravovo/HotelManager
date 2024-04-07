package org.example.hotelmanager.controllers.client;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;

import java.io.IOException;

public class ClientSettingsFormController {
    public Button delete_acc_btn;

    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    FormBuilder formBuilder = new FormBuilder();

    public void deleteAccountButtonClick(ActionEvent event) throws IOException {
        client = clientHolder.getUser();
        formBuilder.openDialog("client-forms/confirm-deleting-acc-form.fxml",
                "Видалити акаунт",
                300, 200);
        if (clientHolder.isDone()){
            Stage stage = (Stage) delete_acc_btn.getScene().getWindow();
            stage.close();
        }
    }
}
