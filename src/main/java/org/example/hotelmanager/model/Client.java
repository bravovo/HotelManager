package org.example.hotelmanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.swing.*;
import java.time.LocalDate;
import java.util.Date;

public class Client {
    private int clientID;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private ObservableList<RoomType> roomTypes = FXCollections.observableArrayList();
    private ObservableList<Hotel> hotels = FXCollections.observableArrayList();

    public Client() {

    }
    public Client(int clientID,
                  String firstName,
                  String lastName,
                  String email,
                  String phoneNumber,
                  LocalDate dateOfBirth) {
        this.clientID = clientID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
    }

    public ObservableList<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(ObservableList<Hotel> hotels) {
        this.hotels = hotels;
    }

    public int getClientID() {
        return clientID;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public ObservableList<RoomType> getRoomTypes() {
        return roomTypes;
    }

    public void setRoomTypes(ObservableList<RoomType> roomTypes) {
        this.roomTypes = roomTypes;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}