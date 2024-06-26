package org.example.hotelmanager.model;

import org.bson.types.ObjectId;

import java.time.LocalDate;

public class Booking {
    private ObjectId bookingID;
    private ObjectId hotelID;
    private ObjectId roomID;
    private ObjectId guestID;
    private String guestFirstName;
    private String guestSecondName;
    private String guestPhoneNumber;
    private String guestEmail;
    private int roomNumber;
    private String roomType;
    private int peopleCount;
    private long nightCount;
    private String bookingStatus;
    private LocalDate checkIN_date;
    private LocalDate checkOUT_date;
    private Double totalPrice;
    private String additionalInfo;

    public Booking(){
    }
    public Booking(int peopleCount,
                   long nightCount,
                   String additionalInfo,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date){
        this.peopleCount = peopleCount;
        this.nightCount = nightCount;
        this.additionalInfo = additionalInfo;
        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
    }

    public Booking(ObjectId bookingID,
                   ObjectId hotelID,
                   ObjectId roomID,
                   ObjectId guestID,
                   String guestFirstName,
                   String guestSecondName,
                   String guestPhoneNumber,
                   String guestEmail,
                   int roomNumber,
                   String roomType,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date,
                   Double totalPrice,
                   String additionalInfo,
                   int peopleCount) {
        this.bookingID = bookingID;
        this.hotelID = hotelID;
        this.roomID = roomID;
        this.guestID = guestID;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.guestPhoneNumber = guestPhoneNumber;
        this.guestEmail = guestEmail;
        this.roomNumber = roomNumber;
        this.roomType = roomType;

        if (LocalDate.now().isBefore(checkIN_date)) {
            this.bookingStatus = "Очікується";
        } else if (LocalDate.now().isAfter(checkOUT_date)){
            this.bookingStatus = "Виконано";
        } else {
            this.bookingStatus = "Виконується";
        }

        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
        this.peopleCount = peopleCount;
    }

    public Booking(String guestFirstName,
                   String guestSecondName,
                   String guestPhoneNumber,
                   String guestEmail,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date,
                   int peopleCount,
                   long nightCount,
                   String additionalInfo) {
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.guestPhoneNumber = guestPhoneNumber;
        this.guestEmail = guestEmail;

        if (LocalDate.now().isBefore(checkIN_date)) {
            this.bookingStatus = "Очікується";
        } else if (LocalDate.now().isAfter(checkOUT_date)){
            this.bookingStatus = "Виконано";
        } else {
            this.bookingStatus = "Виконується";
        }

        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.peopleCount = peopleCount;
        this.nightCount = nightCount;
        this.additionalInfo = additionalInfo;
    }

    public Booking(ObjectId roomID,
                   String guestFirstName,
                   String guestSecondName,
                   String guestPhoneNumber,
                   String guestEmail,
                   int roomNumber,
                   String roomType,
                   int peopleCount,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date,
                   Double totalPrice,
                   String additionalInfo) {
        this.roomID = roomID;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.guestPhoneNumber = guestPhoneNumber;
        this.guestEmail = guestEmail;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.peopleCount = peopleCount;

        if (LocalDate.now().isBefore(checkIN_date)) {
            this.bookingStatus = "Очікується";
        } else if (LocalDate.now().isAfter(checkOUT_date)){
            this.bookingStatus = "Виконано";
        } else {
            this.bookingStatus = "Виконується";
        }

        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
    }

    public Booking(ObjectId roomID,
                   String guestFirstName,
                   String guestSecondName,
                   String phoneNumber,
                   String email,
                   int roomNumber,
                   String typeName,
                   int peopleCount,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date,
                   double totalPrice,
                   String additionalInfo) {
        this.roomID = roomID;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.guestPhoneNumber = phoneNumber;
        this.guestEmail = email;
        this.roomNumber = roomNumber;
        this.roomType = typeName;
        this.peopleCount = peopleCount;
        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
    }

    public Booking(ObjectId hotelID,
                   ObjectId roomID,
                   String guestFirstName,
                   String guestSecondName,
                   String phoneNumber,
                   String email,
                   int roomNumber,
                   String typeName,
                   int peopleCount,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date,
                   double totalPrice,
                   String additionalInfo) {
        this.hotelID = hotelID;
        this.roomID = roomID;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.guestPhoneNumber = phoneNumber;
        this.guestEmail = email;
        this.roomNumber = roomNumber;
        this.roomType = typeName;
        this.peopleCount = peopleCount;
        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.totalPrice = totalPrice;
        this.additionalInfo = additionalInfo;
    }

    public Booking(ObjectId bookingID,
                   ObjectId hotelID,
                   ObjectId roomID,
                   String guestFirstName,
                   String guestSecondName,
                   String phoneNumber,
                   String email,
                   LocalDate checkIN_date,
                   LocalDate checkOUT_date,
                   int peopleCount,
                   long nightPeriod,
                   String additionalInfo
    ) {
        this.bookingID = bookingID;
        this.hotelID = hotelID;
        this.roomID = roomID;
        this.guestFirstName = guestFirstName;
        this.guestSecondName = guestSecondName;
        this.guestPhoneNumber = phoneNumber;
        this.guestEmail = email;
        this.peopleCount = peopleCount;
        this.checkIN_date = checkIN_date;
        this.checkOUT_date = checkOUT_date;
        this.nightCount = nightPeriod;
        this.additionalInfo = additionalInfo;
    }


    public String getGuestPhoneNumber() {
        return guestPhoneNumber;
    }
    public String getGuestEmail() {
        return guestEmail;
    }
    public String getRoomType() {
        return roomType;
    }
    public String getAdditionalInfo() {
        return additionalInfo;
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
    public ObjectId getBookingID() {
        return bookingID;
    }
    public ObjectId getHotelID() {
        return hotelID;
    }
    public ObjectId getRoomID() {
        return roomID;
    }
    public ObjectId getGuestID() {
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

    public void setPeopleCount(int peopleCount) {
        this.peopleCount = peopleCount;
    }

    public void setCheckIN_date(LocalDate checkIN_date) {
        this.checkIN_date = checkIN_date;
    }

    public void setCheckOUT_date(LocalDate checkOUT_date) {
        this.checkOUT_date = checkOUT_date;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public void setNightCount(long nightCount) {
        this.nightCount = nightCount;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
