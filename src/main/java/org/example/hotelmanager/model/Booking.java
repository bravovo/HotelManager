package org.example.hotelmanager.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Booking {
    private final int bookingID;
    private final int hotelID;
    private final int guestID;
    private final String guestFirstName;
    private final String guestSecondName;
    private final int roomNumber;
    private String bookingStatus;
    private LocalDate checkIN_date;
    private LocalDate checkOUT_date;
    private Double totalPrice;

    public Booking(int bookingID, int hotelID, int guestID, String guestFirstName, String guestSecondName, int roomNumber, String bookingStatus, Date checkIN_date, Date checkOUT_date, Double totalPrice) {
        this.bookingID = bookingID;
        this.hotelID = hotelID;
        this.guestID = guestID;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.roomNumber = roomNumber;
        this.bookingStatus = bookingStatus;

        Instant instant = checkIN_date.toInstant();
        LocalDate checkIN = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.checkIN_date = checkIN;

        instant = checkOUT_date.toInstant();
        LocalDate checkOUT = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.checkOUT_date = checkOUT;
        this.totalPrice = totalPrice;
    }

    public String getGuestFirstName() {
        return guestFirstName;
    }
    public String getGuestSecondName() {
        return guestSecondName;
    }
    public String getBookingStatus() {
        return bookingStatus;
    }
    public int getBookingID() {
        return bookingID;
    }
    public int getHotelID() {
        return hotelID;
    }
    public int getGuestID() {
        return guestID;
    }
    public int getRoomNumber() {
        return roomNumber;
    }
    public LocalDate getCheckIN_date() {
        return checkIN_date;
    }
    public LocalDate getCheckOUT_date() {
        return checkOUT_date;
    }
    public double getTotalPrice() {
        return totalPrice;
    }
}
