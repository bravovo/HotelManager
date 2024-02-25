package org.example.hotelmanager;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;

import java.io.IOException;

public class FormBuilder {
    private FXMLLoader fxmlLoader;
    private Scene scene;
    private Hotel hotel;
    public void openWindow(Stage stage, String source, String title, int v, int v1) throws IOException {
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource(source));
        scene = new Scene(fxmlLoader.load(), v, v1);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }
    public void openWindow(Stage stage, String source, String title, int v, int v1, Hotel hotel) throws IOException {
        stage.close();
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource(source));
        HotelHolder holder = HotelHolder.getInstance();
        holder.setUser(hotel);
        scene = new Scene(fxmlLoader.load(), v, v1);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void openDialog(String source, String title) throws IOException{
        fxmlLoader = new FXMLLoader(ManagerApplication.class.getResource(source));
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title); // ОБОВ'ЯЗКОВО ЗМІНИТИ ЗАГОЛОВОК
        Scene dialogScene = new Scene(fxmlLoader.load(), 400, 400);
        dialogStage.setScene(dialogScene);
        dialogStage.showAndWait();
    }

    public void errorValidation(String errorText){
        VBox vBox = new VBox();
        Label errorLabel = new Label();
        Button okButton = new Button();

        errorLabel.setText(errorText);
        errorLabel.setAlignment(Pos.CENTER);
        okButton.setText("OK");
        okButton.setPrefWidth(70);
        okButton.setPrefHeight(30);
        okButton.setOnAction(e -> {
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        });
        vBox.getChildren().addAll(errorLabel, okButton);
        vBox.setSpacing(50);
        vBox.setPadding(new Insets(30));
        vBox.setAlignment(Pos.CENTER);
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        Scene scene = new Scene(vBox);
        dialogStage.setScene(scene);
        dialogStage.setTitle("Помилка");
        dialogStage.show();
    }
}
