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
    private int roomNumber;
    private String roomType;
    private double roomPrice;
    private int peopleCount;
    private long nightCount;
    private String bookingStatus;
    private LocalDate checkIN_date;
    private LocalDate checkOUT_date;
    private Double totalPrice;
    private String additionalInfo;

    public Booking(int bookingID, int hotelID, int guestID, String guestFirstName, String guestSecondName, int roomNumber, String bookingStatus, Date checkIN_date, Date checkOUT_date, Double totalPrice, String additionalInfo) {
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
        this.additionalInfo = additionalInfo;
    }

    public Booking(String guestFirstName, String guestSecondName, LocalDate checkIN_date, LocalDate checkOUT_date, int peopleCount, long nightCount, String additionalInfo) {
        this.bookingID = -1;
        this.hotelID = -1;
        this.guestID = -1;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.peopleCount = peopleCount;
        this.nightCount = nightCount;
        this.additionalInfo = additionalInfo;
    }

    public String getRoomType() {
        return roomType;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public double getRoomPrice() {
        return roomPrice;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public long getNightCount() {
        return nightCount;
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
