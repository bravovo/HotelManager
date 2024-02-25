package org.example.hotelmanager.objects;

public class Hotel {
    private int hotel_id;
    private String hotel_name;
    private String address;
    private String login;
    private String password;
    private String email;
    private int stars;
    private int rooms_count;

    public Hotel() {
    }

    public Hotel(int hotel_id, String hotel_name, String address, String login, String password, String email, int stars, int rooms_count) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.address = address;
        this.login = login;
        this.password = password;
        this.email = email;
        this.stars = stars;
        this.rooms_count = rooms_count;
    }

    public Hotel(int hotel_id, String hotel_name, String address, String login, String password) {
        this.hotel_id = hotel_id;
        this.hotel_name = hotel_name;
        this.address = address;
        this.login = login;
        this.password = password;
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
    public int getStars() {
        return stars;
    }
    public int getRooms_count() {
        return rooms_count;
    }
}
