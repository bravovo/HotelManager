package org.example.hotelmanager.controllers.superAdmin;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Review;
import org.example.hotelmanager.model.ReviewHolder;
import org.example.hotelmanager.model.SuperAdmin;
import org.example.hotelmanager.model.SuperAdminHolder;

import java.net.URL;
import java.util.ResourceBundle;

public class SuperReviewsFormController implements Initializable {
    public TextField search_text_field;
    public Label reviews_count_label;
    public ScrollPane reviews_scroll_pane;
    public Button delete_review_btn;

    SuperAdmin superAdmin = new SuperAdmin();
    SuperAdminHolder superAdminHolder = SuperAdminHolder.getInstance();
    ObservableList<Review> reviews = FXCollections.observableArrayList();
    Review review = new Review();
    ReviewHolder reviewHolder = ReviewHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();

    public void initialize(URL url, ResourceBundle resourceBundle) {
        superAdmin = superAdminHolder.getSuper();
        reviews = superAdmin.getReviews();

        reviews_scroll_pane.setFitToWidth(true);

        setReviewsGrid(reviews);

        search_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                setReviewsGrid(superAdminHolder.getSuper().getReviews());
            } else {
                ObservableList<Review> filteredData = FXCollections.observableArrayList();
                for (Review review : superAdminHolder.getSuper().getReviews()) {
                    if (review.getClientName().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredData.add(review);
                    }
                    else if(review.getHotelName().toLowerCase().contains(newValue.toLowerCase())){
                        filteredData.add(review);
                    }
                }
                setReviewsGrid(filteredData);
            }
        });
    }

    public void setReviewsGrid(ObservableList<Review> reviews){
        reviews_count_label.setText(String.valueOf(reviews.size()));
        delete_review_btn.setDisable(true);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20.0));
        gridPane.setAlignment(Pos.CENTER);

        for(int i = 0; i < reviews.size(); i++){
            Review review = reviews.get(i);
            VBox reviewVbox = new VBox();
            reviewVbox.setMaxWidth(250);
            reviewVbox.setMaxHeight(250);
            reviewVbox.setMinSize(250, 250);
            reviewVbox.setSpacing(10);
            reviewVbox.setPadding(new Insets(10.0));

            Label reviewTitle = new Label(review.getClientName() + " про готель " + review.getHotelName() + ":");
            reviewTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

            Text reviewText = new Text(review.getReviewText());
            reviewText.setStyle("-fx-font-size: 14px");
            reviewText.setWrappingWidth(230);

            reviewVbox.getChildren().addAll(
                    reviewTitle,
                    new Separator(),
                    reviewText
            );

            reviewVbox.setOnMouseClicked(click -> {
                reviewHolder.setReview(review);
                delete_review_btn.setDisable(false);
            });

            reviewVbox.setStyle("-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 10px");

            gridPane.add(reviewVbox, i % 3, i / 3);
            gridPane.setAlignment(Pos.CENTER);
        }
        reviews_scroll_pane.setContent(gridPane);
    }

    public void deleteReviewButtonClick(ActionEvent actionEvent) {
        review = reviewHolder.getReview();
        mongoDatabaseConnection.deleteReview(review);
        superAdmin = superAdminHolder.getSuper();
        setReviewsGrid(superAdmin.getReviews());
    }
}
