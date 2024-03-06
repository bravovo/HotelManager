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

    public Hotel() {
        this.hotel_name = "";
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
    public int getRooms_count() {
        return rooms_count;
    }
    public ObservableList<Room> getRoomsForList() {
        return rooms;
    }
    public void setRooms(ObservableList<Room> rooms) {
        this.rooms = rooms;
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
}
