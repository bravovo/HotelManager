package org.example.hotelmanager.model;

import javafx.collections.ObservableList;

import java.awt.print.Book;

public final class BookingHolder {
    private Booking booking;
    private ObservableList<Booking> bookings;
    private final static BookingHolder INSTANCE = new BookingHolder();

    private BookingHolder(){}
    public static BookingHolder getInstance() {
        return INSTANCE;
    }
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    public void setBookingsList(ObservableList<Booking> rooms) { this.bookings = rooms; }
    public ObservableList<Booking> getBookingList() { return bookings; }
    public Booking getBooking() {
        return this.booking;
    }
}