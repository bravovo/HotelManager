package org.example.hotelmanager.controllers.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Client;
import org.example.hotelmanager.model.ClientHolder;
import org.example.hotelmanager.model.Hotel;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientReviewsFormController implements Initializable {
    public VBox review_vbox;
    public VBox hotel_name_vbox;
    public ChoiceBox hotel_name_choice;
    public VBox review_text_vbox;
    public TextArea review_area;
    public Button save_review_btn;
    public Separator separator;
    public ScrollPane scroll_pane;
    public HBox client_reviews_hbox;

    Client client = new Client();
    ClientHolder clientHolder =  ClientHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    ObservableList<Hotel> hotelObservableList = FXCollections.observableArrayList();
    ObservableList<String> hotelNames = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle rb) {
        setReviewForm();
    }

    public void saveReviewButtonClick(ActionEvent actionEvent) {
        if(hotel_name_choice.getValue().equals("Оберіть готель для відгуку")
                || review_area.getText().length() == 0){
            FormBuilder formBuilder = new FormBuilder();
            formBuilder.errorValidation("Всі поля для відгуку повинні бути заповненні");
            return;
        }
        mongoDatabaseConnection.createClientReview(
                hotelObservableList.get(hotelNames.indexOf(hotel_name_choice.getValue())),
                review_area.getText()
        );
        setReviewForm();
    }

    public void setReviewForm(){
        client = clientHolder.getUser();
        for(Hotel hotel : client.getHotels()){
            hotelNames.add(hotel.getHotel_name());
            hotelObservableList.add(hotel);
        }
        hotel_name_choice.setItems(hotelNames);
        hotel_name_choice.setValue("Оберіть готель для відгуку");
        review_area.setText("");
        review_area.setWrapText(true);
    }
}
