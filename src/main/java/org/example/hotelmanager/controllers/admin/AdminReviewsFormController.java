package org.example.hotelmanager.controllers.admin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.hotelmanager.model.Hotel;
import org.example.hotelmanager.model.HotelHolder;
import org.example.hotelmanager.model.Review;
import java.net.URL;
import java.util.ResourceBundle;
public class AdminReviewsFormController implements Initializable {
    public Label reviews_count_label;
    public ScrollPane reviews_scroll_pane;
    public TextField search_text_field;
    Hotel hotel = new Hotel();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    ObservableList<Review> hotelReviews = FXCollections.observableArrayList();
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hotel = hotelHolder.getUser();
        hotelReviews.addAll(hotel.getReviews());
        reviews_count_label.setText(String.valueOf(hotelReviews.size()));
        reviews_scroll_pane.setFitToWidth(true);
        setReviewsGrid(hotelReviews);
        search_text_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                setReviewsGrid(hotelReviews);
            } else {
                ObservableList<Review> filteredData = FXCollections.observableArrayList();
                for (Review review : hotelReviews) {
                    if (review.getClientName().toLowerCase().contains(newValue.toLowerCase())) {
                        filteredData.add(review);
                    }
                }
                setReviewsGrid(filteredData);
            }
        });
    }
    public void setReviewsGrid(ObservableList<Review> hotelReviews){
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20.0));
        gridPane.setAlignment(Pos.CENTER);
        for(int i = 0; i < hotelReviews.size(); i++){
            Review review = hotelReviews.get(i);
            VBox reviewVbox = new VBox();
            reviewVbox.setMaxWidth(300);
            reviewVbox.setMaxHeight(200);
            reviewVbox.setMinSize(300, 200);
            reviewVbox.setSpacing(10);
            reviewVbox.setPadding(new Insets(10.0));
            Label reviewClientName = new Label(review.getClientName());
            reviewClientName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            Text reviewText = new Text(review.getReviewText());
            reviewText.setStyle("-fx-font-size: 14px");
            reviewText.setWrappingWidth(280);
            reviewVbox.getChildren().addAll(reviewClientName, new Separator(), reviewText);
            reviewVbox.setStyle("-fx-border-color: grey; -fx-border-width: 1px; -fx-border-radius: 10px");
            gridPane.add(reviewVbox, i % 3, i / 3);
            gridPane.setAlignment(Pos.CENTER);
        }
        reviews_scroll_pane.setContent(gridPane);
    }
}
