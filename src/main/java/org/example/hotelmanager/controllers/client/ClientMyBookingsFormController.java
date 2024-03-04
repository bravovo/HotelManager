package org.example.hotelmanager.controllers.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import org.example.hotelmanager.data.MongoDatabaseConnection;

public class ClientMyBookingsFormController {

    @FXML
    private DatePicker check_in_date;

    @FXML
    private DatePicker check_out_date;

    @FXML
    private Button date_button;
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();

    public void createBookingButtonClick(javafx.event.ActionEvent event){
        System.out.println("Almost there");
        mongoDatabaseConnection.createBooking(check_in_date.getValue(), check_out_date.getValue());
    }
}
