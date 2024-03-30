package org.example.hotelmanager.controllers.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.Room;
import org.example.hotelmanager.model.RoomHolder;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class ClientCreateBookingFormController implements Initializable {
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    RoomHolder roomHolder = RoomHolder.getInstance();
    FormBuilder formBuilder = new FormBuilder();
    ObservableList<Room> availableRooms = FXCollections.observableArrayList();
    public ScrollPane scroll_pane;
    public VBox create_booking_vbox;
    public VBox checkIN_date_vbox;
    public DatePicker checkIN_picker;
    public VBox checkOUT_date_vbox;
    public DatePicker checkOUT_picker;
    public VBox people_count_vbox;
    public TextField people_count_field;
    public VBox notes_vbox;
    public TextArea notes_area;
    public Button create_booking_btn;
    public Separator separator;
    public HBox available_rooms_hbox;

    public void initialize(URL url, ResourceBundle resourceBundle){
        notes_area.setWrapText(true);
        people_count_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\d*)?")) {
                people_count_field.setText(oldValue);
            }
        });
    }

    public void createBookingButtonClick(javafx.event.ActionEvent e) throws IOException {
        available_rooms_hbox.getChildren().clear();
        if (checkIN_picker.getValue() == null
                || checkOUT_picker.getValue() == null
                || people_count_field.getText().length() == 0){
            formBuilder.errorValidation("Всі обов'язкові поля повинні бути заповненні");
            return;
        }
        if (checkOUT_picker.getValue().isBefore(checkIN_picker.getValue())
                || checkOUT_picker.getValue().isEqual(checkIN_picker.getValue())){
            formBuilder.errorValidation("Дата заїзду повинна бути раніше дати виїзду");
            return;
        }
        if (checkOUT_picker.getValue().isBefore(LocalDate.now())
                || checkIN_picker.getValue().isBefore(LocalDate.now())){
            formBuilder.errorValidation("Не можна вибирати дати, які раніше ніж сьогоднішня");
            return;
        }
        int peopleCount = Integer.parseInt(people_count_field.getText());
        availableRooms = mongoDatabaseConnection.clientFindAvailableRoomsForBooking(
                checkIN_picker.getValue(),
                checkOUT_picker.getValue(),
                peopleCount
        );
        Map<VBox, Room> roomVBoxMap = new HashMap<>();
        scroll_pane.setStyle("-fx-background-color: transparent; -fx-border-width: 0px;");
        for(Room room : availableRooms){
            Label hotelName = new Label("Готель: ");
            Label roomName = new Label("Кімната: " + room.getRoom_name());
            Label peopleCountLabel = new Label("Місткість: " + room.getCapacity());
            Label checkInLabel = new Label("Дата заїзду: " + checkIN_picker.getValue().toString());
            Label checkOutLabel = new Label("Дата виїзду: " + checkOUT_picker.getValue().toString());
            Button createBookingButton = new Button("Створити бронювання");

            VBox vbox = new VBox();
            double prefWidth = 200;
            double prefHeight = 200;
            vbox.setPrefWidth(prefWidth);
            vbox.setMinWidth(prefWidth);
            vbox.setPrefHeight(prefHeight);
            vbox.setMaxHeight(prefHeight);
            vbox.setSpacing(20);
            vbox.getChildren().addAll(hotelName, roomName, peopleCountLabel, checkInLabel, checkOutLabel, createBookingButton);
            vbox.setStyle("" +
                    "-fx-background-color: white; " +
                    "-fx-padding: 10px; " +
                    "-fx-border-color: black; " +
                    "-fx-border-width: 2px; " +
                    "-fx-border-radius: 5px; " +
                    "-fx-background-radius: 5px;"
            );

            vbox.setOnMouseEntered(event -> vbox.setCursor(Cursor.HAND));
            if(roomVBoxMap.size() >= 4){
                scroll_pane.setFitToHeight(false);
            }
            vbox.setOnMouseExited(event -> vbox.setCursor(Cursor.DEFAULT));
            vbox.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 1) {
                    Room newRoom = roomVBoxMap.get(vbox);
                    System.out.println(newRoom.getPrice());
                }
            });
            available_rooms_hbox.getChildren().add(vbox);
            roomVBoxMap.put(vbox, room);
        }
    }
}
