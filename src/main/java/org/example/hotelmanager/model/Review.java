package org.example.hotelmanager.model;

import org.bson.types.ObjectId;

public class Review {
    private ObjectId ID;
    private ObjectId hotelID;
    private String hotelName;
    private int clientID;
    private String clientName;
    private String clientEmail;
    private String reviewText;

    public Review(){}

    public Review(ObjectId ID, ObjectId hotelID, String hotelName, int clientID, String clientName, String clientEmail, String reviewText) {
        this.ID = ID;
        this.hotelID = hotelID;
        this.hotelName = hotelName;
        this.clientID = clientID;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.reviewText = reviewText;
    }

    public ObjectId getID() {
        return ID;
    }

    public void setID(ObjectId ID) {
        this.ID = ID;
    }

    public ObjectId getHotelID() {
        return hotelID;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public void setHotelID(ObjectId hotelID) {
        this.hotelID = hotelID;
    }


    public int getClientID() {
        return clientID;
    }

    public void setClientID(int clientID) {
        this.clientID = clientID;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
