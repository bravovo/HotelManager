package org.example.hotelmanager.objects;

public final class HotelHolder {
    private Hotel hotel;
    private final static HotelHolder INSTANCE = new HotelHolder();

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
}