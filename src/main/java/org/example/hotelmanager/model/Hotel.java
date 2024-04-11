package org.example.hotelmanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Hotel {
    private int hotel_id;
    private String hotel_name;
    private String address;
    private String login;
    private String password;
    private String email;
    private int rooms_count;
    private String phone_number;
    private ObservableList<Room> rooms = FXCollections.observableArrayList();
    private ObservableList<Booking> bookings = FXCollections.observableArrayList();
    private ObservableList<Guest> guests = FXCollections.observableArrayList();

    public Hotel() {
        this.hotel_name = "";
    }

    public Hotel(int hotel_id, String hotel_name, String address, String email, String phone_number) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.address = address;
        this.email = email;
        this.phone_number = phone_number;
    }

    public Hotel(int hotel_id, String hotel_name, String address, String login, String password, String email, int rooms_count, String phoneNumber) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.address = address;
        this.login = login;
        this.password = password;
        this.email = email;
        this.rooms_count = rooms_count;
        this.phone_number = phoneNumber;
    }
    public int getHotel_id() {
        return hotel_id;
    }
    public String getHotel_name() {
        return hotel_name;
    }
    public String getAddress() {
        return address;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone_number() {
        return phone_number;
    }
    public int getRooms_count() {
        return rooms_count;
    }
    public ObservableList<Room> getRoomsForList() {
        return rooms;
    }
    public ObservableList<Booking> getBookingsForList() {
        return bookings;
    }
    public ObservableList<Guest> getGuests() {
        return guests;
    }
    public void setGuests(ObservableList<Guest> guests) {
        this.guests = guests;
    }
    public void setRooms(ObservableList<Room> rooms) {
        this.rooms = rooms;
    }
    public void setBookings(ObservableList<Booking> bookings) {
        this.bookings = bookings;
    }
    public String getLogin() {
        return this.login;
    }
    public int countAvailableRooms(){
        int counter = 0;
        for(Room room : rooms){
            if(room.getStatus().equals("Доступна")){
                counter++;
            }
        }
        return counter;
    }
    public void setHotel_name(String hotel_name) {
        this.hotel_name = hotel_name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setRooms_count(int rooms_count) {
        this.rooms_count = rooms_count;
    }
    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }



}
