package org.example.hotelmanager.model;

public final class RoomHolder {
    private Room room;
    private final static RoomHolder INSTANCE = new RoomHolder();

    private RoomHolder(){}
    public static RoomHolder getInstance() {
        return INSTANCE;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public Room getRoom() {
        return this.room;
    }
}