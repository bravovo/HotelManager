package org.example.hotelmanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.awt.print.Book;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Room {
    private int room_id;
    private int hotel_id;
    private int type_id;
    private String type_name;
    private String room_name;
    private String room_description;
    private int room_number;
    private String status;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private ObservableList<Booking> bookings = FXCollections.observableArrayList();

    public Room() {
    }

    public Room(int room_id, int hotel_id,
                int type_id, String type_name,
                String room_name, String room_description, int room_number, String status, Date dateFrom, Date dateTo) {
        this.room_id = room_id;
        this.hotel_id = hotel_id;
        this.type_id = type_id;
        this.type_name = type_name;
        this.room_name = room_name;
        this.room_description = room_description;
        this.room_number = room_number;
        this.status = status;

        Instant instant = dateFrom.toInstant();
        LocalDate fromDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.dateFrom = fromDate;

        instant = dateTo.toInstant();
        LocalDate toDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.dateTo = toDate;
    }

    public String getType_name() {
        return type_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public int getRoom_number() {
        return room_number;
    }

    public String getStatus() {
        return status;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setBookings(ObservableList<Booking> bookings) {
        this.bookings = bookings;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }
}
