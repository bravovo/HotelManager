package org.example.hotelmanager.model;

import org.bson.types.ObjectId;

public class Guest {
    private ObjectId guestID;
    private String hotelID;
    private int roomNumber;
    private String firstName;
    private String secondName;
    private String email;
    private String phoneNumber;
    private double totalPrice;

    public Guest(ObjectId guestID,
                 String hotelID,
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

    public ObjectId getGuestID() {
        return guestID;
    }
    public String getHotelID() {
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

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
