package org.example.hotelmanager.controllers.client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.data.MongoDatabaseConnection;
import org.example.hotelmanager.model.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
public class ClientCreateBookingFormController implements Initializable {
    public Label availble_rooms_cnt;
    public Text message_on_scroll;
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
    Client client = new Client();
    ClientHolder clientHolder = ClientHolder.getInstance();
    BookingHolder bookingHolder = BookingHolder.getInstance();
    MongoDatabaseConnection mongoDatabaseConnection = new MongoDatabaseConnection();
    RoomHolder roomHolder = RoomHolder.getInstance();
    FormBuilder formBuilder = new FormBuilder();
    ObservableList<Room> availableRooms = FXCollections.observableArrayList();
    public void initialize(URL url, ResourceBundle resourceBundle){
        availble_rooms_cnt.setText("");
        message_on_scroll.toBack();
        available_rooms_hbox.getChildren().clear();
        scroll_pane.setStyle("-fx-background-color: transparent; -fx-border-width: 0px;");
        notes_area.setWrapText(true);
        people_count_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\d*)?")) {
                people_count_field.setText(oldValue);
            }
        });
    }
    public void createBookingButtonClick(javafx.event.ActionEvent e){
        available_rooms_hbox.getChildren().clear();
        client = clientHolder.getUser();
        bookingHolder.setBookingDone(false);
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
        long nightPeriod = ChronoUnit.DAYS.between(checkIN_picker.getValue(), checkOUT_picker.getValue());
        Booking booking = new Booking(peopleCount,
                nightPeriod,
                notes_area.getText(),
                checkIN_picker.getValue(),
                checkOUT_picker.getValue()
        );
        bookingHolder.setBooking(booking);
        availableRooms = mongoDatabaseConnection.clientFindAvailableRoomsForBooking(
                checkIN_picker.getValue(),
                checkOUT_picker.getValue(),
                peopleCount
        );
        Map<VBox, Room> roomVBoxMap = new HashMap<>();
        ObservableList<Hotel> hotels = mongoDatabaseConnection.getHotels();
        ObservableList<RoomType> roomTypes = client.getRoomTypes();
        for(Room room : availableRooms){
            String hotelName = "";
            String typeDescription = "";
            for(Hotel hotel : hotels){
                if(hotel.getHotel_id().toString().equals(room.getHotel_id().toString())){
                    hotelName = hotel.getHotel_name();
                    room.setHotel(hotel);
                }
            }
            for(RoomType roomType : roomTypes){
                if(room.getType_id() == roomType.getTypeID()){
                    typeDescription = roomType.getTypeDescription();
                }
            }
            Label hotelNameLabel = new Label("Готель: " + hotelName);
            Label roomName = new Label("Кімната: " + room.getRoom_name());
            Label peopleCountLabel = new Label("Місткість: " + room.getCapacity());
            Label price = new Label("Ціна за одну ніч: " + room.getPrice());
            Label roomTypeDescription = new Label("Тип кімнати:\n" + typeDescription);
            roomTypeDescription.setWrapText(true);
            Button createBookingButton = new Button("Створити бронювання");
            createBookingButton.setOnAction(click -> {
                Booking clientBooking = new Booking(
                        room.getHotel_id(),
                        room.getRoom_id(),
                        client.getFirstName(),
                        client.getLastName(),
                        client.getPhoneNumber(),
                        client.getEmail(),
                        room.getRoom_number(),
                        room.getType_name(),
                        Integer.parseInt(people_count_field.getText()),
                        booking.getCheckIN_date(),
                        booking.getCheckOUT_date(),
                        booking.getNightCount() * room.getPrice(),
                        notes_area.getText()
                );
                mongoDatabaseConnection.createClientBooking(clientBooking);
                resetEverything();
            });
            VBox vbox = new VBox();
            double prefWidth = 250;
            double prefHeight = 250;
            vbox.setPrefWidth(prefWidth);
            vbox.setMinWidth(prefWidth);
            vbox.setPrefHeight(prefHeight);
            vbox.setMaxHeight(prefHeight);
            vbox.setSpacing(12.5);
            vbox.getChildren().addAll(
                    hotelNameLabel,
                    roomName,
                    peopleCountLabel,
                    price,
                    roomTypeDescription,
                    createBookingButton
            );
            // Стилі маленьких форм вільних кімнат
            createBookingButton.setCursor(Cursor.HAND);
            vbox.getStyleClass().add("room-form");
            hotelNameLabel.getStyleClass().add("room-form-label");
            roomName.getStyleClass().add("room-form-label");
            peopleCountLabel.getStyleClass().add("room-form-label");
            price.getStyleClass().add("room-form-label");
            roomTypeDescription.getStyleClass().add("room-form-label");
            createBookingButton.getStyleClass().add("room-form-button");
            vbox.setOnMouseEntered(event -> vbox.setCursor(Cursor.HAND));
            if(roomVBoxMap.size() >= 4){
                scroll_pane.setFitToHeight(false);
            }
            vbox.setOnMouseExited(event -> vbox.setCursor(Cursor.DEFAULT));
            vbox.setOnMouseClicked(event -> {
                try{
                    if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() >= 1) {
                        Room availableRoom = roomVBoxMap.get(vbox);
                        roomHolder.setRoom(availableRoom);
                        bookingHolder.getBooking().setAdditionalInfo(notes_area.getText());
                        formBuilder.openDialog("client-forms/watch-and-confirm-booking-form.fxml",
                                "Створити бронювання",
                                800, 650);
                        if (bookingHolder.getBookingDone()){
                            resetEverything();
                        }
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            });
            available_rooms_hbox.getChildren().add(vbox);
            roomVBoxMap.put(vbox, room);
        }
        availble_rooms_cnt.setText(availableRooms.size() + " вільних кімнат");
    }
    private void resetEverything() {
        availble_rooms_cnt.setText("");
        available_rooms_hbox.getChildren().clear();
        checkIN_picker.setValue(null);
        checkOUT_picker.setValue(null);
        people_count_field.setText("");
        notes_area.setText("");
    }
}