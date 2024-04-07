package org.example.hotelmanager.model;

public final class HotelHolder {
    private Hotel hotel;
    private final static HotelHolder INSTANCE = new HotelHolder();
    private boolean done = false;

    private HotelHolder(){}
    public static HotelHolder getInstance() {
        return INSTANCE;
    }
    public void setUser(Hotel hotel) {
        this.hotel = hotel;
    }
    public Hotel getUser() {
        return this.hotel;
    }
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
}