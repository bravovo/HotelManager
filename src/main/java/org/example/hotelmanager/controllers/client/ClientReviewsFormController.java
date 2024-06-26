package org.example.hotelmanager.controllers.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientReviewsFormController implements Initializable {
    public Tab make_reviews;
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
    public Label reviews_cnt;

    public Tab all_reviews;
    public ChoiceBox hotel_name_see_all;
    public Button find_reviews_btn;
    public Separator separator2;
    public ScrollPane scroll_pane2;
    public Label all_reviews_cnt;

    Client client = new Client();
    ClientHolder clientHolder =  ClientHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    ObservableList<Hotel> hotelObservableList = FXCollections.observableArrayList();
    ObservableList<String> hotelNames = FXCollections.observableArrayList();

    public final int MAX_REVIEW_CHARACTERS = 170;

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
        hotel_name_see_all.setItems(hotelNames);
        hotel_name_see_all.setValue("Оберіть готель для відгуку");
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
                    new Separator(),
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
            mainVbox.getStyleClass().add("review-form");
            hotelNameLabel.getStyleClass().add("review-form-label");
            reviewText.getStyleClass().add("review-text");
            deleteReviewButton.getStyleClass().add("review-delete-button");

            mainVbox.setOnMouseEntered(event -> mainVbox.setCursor(Cursor.HAND));
            mainVbox.setOnMouseExited(event -> mainVbox.setCursor(Cursor.DEFAULT));
            client_reviews_hbox.getChildren().add(mainVbox);
        }
        reviews_cnt.setText(client.getReviews().size() + " відгуків");
        all_reviews_cnt.setText("");
    }

    public void findReviewsButtonClick(ActionEvent actionEvent) {
        if(hotel_name_see_all.getValue().equals("Оберіть готель для відгуку")){
            FormBuilder formBuilder = new FormBuilder();
            formBuilder.errorValidation("Готель повинен бути обраний");
            return;
        }
        Hotel hotelToFind = hotelObservableList.get(hotelNames.indexOf(hotel_name_see_all.getValue()));
        ObservableList<Review> hotelReviews = mongoDatabaseConnection.getAllClientsReviews(hotelToFind);

        GridPane gridPane = new GridPane();
        gridPane.setStyle("-fx-background-color: #ffffff");
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(20.0));
        gridPane.setAlignment(Pos.CENTER);

        for(int i = 0; i < hotelReviews.size(); i++){
            Review review = hotelReviews.get(i);
            VBox reviewVbox = new VBox();
            reviewVbox.setMaxWidth(250);
            reviewVbox.setMaxHeight(250);
            reviewVbox.setMinSize(250, 250);
            reviewVbox.setSpacing(10);
            reviewVbox.setPadding(new Insets(10.0));

            Label reviewClientName = new Label(
                    review.getClientName().equals(client.getFirstName()) ? "Ви" : review.getClientName()
            );
            Label reviewText = new Label(review.getReviewText());
            reviewText.setWrapText(true);

            // Стилі маленьких форм відгуків
            reviewVbox.getStyleClass().add("review-form");
            reviewClientName.getStyleClass().add("review-form-label");
            reviewText.getStyleClass().add("review-text");

            reviewVbox.getChildren().addAll(
                    reviewClientName,
                    new Separator(),
                    reviewText
            );

            gridPane.add(reviewVbox, i % 3, i / 3);
        }
        scroll_pane2.setContent(gridPane);
        all_reviews_cnt.setText(hotelReviews.size() + " відгуків");
    }
}
