package org.example.hotelmanager.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Booking {
    private final int bookingID;
    private final int hotelID;
    private final int guestID;
    private final int roomNumber;
    private LocalDate checkIN_date;
    private LocalDate checkOUT_date;
    private double totalPrice;

    public Booking(int bookingID, int hotelID, int guestID, int roomNumber, Date checkIN_date, Date checkOUT_date, double totalPrice) {
        this.bookingID = bookingID;
        this.hotelID = hotelID;
        this.guestID = guestID;
        this.roomNumber = roomNumber;

        Instant instant = checkIN_date.toInstant();
        LocalDate checkIN = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.checkIN_date = checkIN;

        instant = checkOUT_date.toInstant();
        LocalDate checkOUT = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        this.checkOUT_date = checkOUT;
        this.totalPrice = totalPrice;
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
