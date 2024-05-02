package org.example.hotelmanager.controllers.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

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
    public HBox review_labels_hbox;

    Client client = new Client();
    ClientHolder clientHolder =  ClientHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    ObservableList<Hotel> hotelObservableList = FXCollections.observableArrayList();
    ObservableList<String> hotelNames = FXCollections.observableArrayList();

    public int MAX_REVIEW_CHARACTERS = 170;

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
        hotelNames.clear();
        hotelObservableList.clear();

        client = clientHolder.getUser();
        for(Hotel hotel : client.getHotels()){
            hotelNames.add(hotel.getHotel_name());
            hotelObservableList.add(hotel);
        }
        hotel_name_choice.setItems(hotelNames);
        hotel_name_choice.setValue("Оберіть готель для відгуку");
        review_area.setText("");
        review_area.setWrapText(true);

        if(review_labels_hbox.getChildren().size() == 2){
            review_labels_hbox.getChildren().remove(1);
        }
        Label characterCount = new Label("0/" + MAX_REVIEW_CHARACTERS);
        characterCount.setMinWidth(70);
        characterCount.setPrefWidth(70);
        characterCount.setMaxWidth(70);
        review_labels_hbox.getChildren().add(characterCount);
        review_area.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.length() >= MAX_REVIEW_CHARACTERS) {
                review_area.setText(oldValue);
            }
            characterCount.setText(newValue.length() + "/" + MAX_REVIEW_CHARACTERS);
        });
        client_reviews_hbox.getChildren().clear();

        for(Review review : client.getReviews()){
            Label hotelNameLabel = new Label("Готель: " + review.getHotelName());
            Label reviewText = new Label(review.getReviewText());
            Button deleteReviewButton = new Button("Видалити відгук");
            deleteReviewButton.setOnAction(click -> {
                mongoDatabaseConnection.deleteClientReview(review);
                setReviewForm();
            });
            reviewText.setWrapText(true);

            VBox mainVbox = new VBox();
            double prefWidth = 250;
            double prefHeight = 250;
            mainVbox.setPrefWidth(prefWidth);
            mainVbox.setMinWidth(prefWidth);
            mainVbox.setPrefHeight(prefHeight);
            mainVbox.setMaxHeight(prefHeight);
            mainVbox.setSpacing(10);
            VBox labelsVbox = new VBox();
            labelsVbox.setSpacing(12.5);
            labelsVbox.getChildren().addAll(
                    hotelNameLabel,
                    reviewText
            );
            Region spacerTop = new Region();
            Region spacerBottom = new Region();
            mainVbox.getChildren().addAll(labelsVbox, deleteReviewButton);


            VBox.setVgrow(labelsVbox, Priority.ALWAYS);
            VBox.setVgrow(deleteReviewButton, Priority.NEVER);

            VBox.setVgrow(spacerTop, Priority.ALWAYS);
            VBox.setVgrow(spacerBottom, Priority.ALWAYS);

            // Стилі маленьких форм відгуків
            mainVbox.setOnMouseEntered(event -> mainVbox.setCursor(Cursor.HAND));
            mainVbox.setOnMouseExited(event -> mainVbox.setCursor(Cursor.DEFAULT));
            mainVbox.setOnMouseClicked(event -> {
                try{
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 1) {
                        System.out.println(review.getID());
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            client_reviews_hbox.getChildren().add(mainVbox);
        }
    }
}
