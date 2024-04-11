package org.example.hotelmanager.model;

import java.time.LocalDate;

public class Guest {
    private int guestID;
    private int hotelID;
    private int roomNumber;
    private String firstName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private double totalPrice;

    public Guest(int guestID,
                 int hotelID,
                 int roomNumber,
                 String firstName,
                 String secondName,
                 String email,
                 String phoneNumber,
                 double totalPrice
    ) {
        this.guestID = guestID;
        this.hotelID = hotelID;
        this.roomNumber = roomNumber;
        this.firstName = firstName;
        this.secondName = secondName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.totalPrice = totalPrice;
    }

    public Guest() {

    }

    public int getGuestID() {
        return guestID;
    }
    public int getHotelID() {
        return hotelID;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getSecondName() {
        return secondName;
    }
    public String getEmail() {
        return email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
