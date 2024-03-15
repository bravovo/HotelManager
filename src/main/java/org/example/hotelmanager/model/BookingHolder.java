package org.example.hotelmanager.model;

public final class BookingHolder {
    private Booking booking;
    private final static BookingHolder INSTANCE = new BookingHolder();

    private BookingHolder(){}
    public static BookingHolder getInstance() {
        return INSTANCE;
    }
    public void setBooking(Booking booking) {
        this.booking = booking;
    }
    public Booking getBooking() {
        return this.booking;
    }
}