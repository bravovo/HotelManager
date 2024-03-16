package org.example.hotelmanager.model;

import javafx.collections.ObservableList;

public final class RoomHolder {
    private Room room;
    private ObservableList<Room> rooms;
    private final static RoomHolder INSTANCE = new RoomHolder();

    private RoomHolder(){}
    public static RoomHolder getInstance() {
        return INSTANCE;
    }
    public void setRoom(Room room) {
        this.room = room;
    }
    public void setRoomList(ObservableList<Room> rooms) { this.rooms = rooms; }
    public ObservableList<Room> getRoomsList() { return rooms; }
    public Room getRoom() {
        return this.room;
    }
}