package org.example.hotelmanager.objects;

public class Room {
    private int room_id;
    private int hotel_id;
    private int type_id;
    private String type_name;
    private String room_name;
    private String room_description;
    private int room_number;
    private String status;

    public Room(int room_id, int hotel_id,
                int type_id, String type_name,
                String room_name, String room_description, int room_number) {
        this.room_id = room_id;
        this.hotel_id = hotel_id;
        this.type_id = type_id;
        this.type_name = type_name;
        this.room_name = room_name;
        this.room_description = room_description;
        this.room_number = room_number;
        this.status = "Availible";
    }

    public String getType_name() {
        return type_name;
    }

    public String getRoom_name() {
        return room_name;
    }

    public int getRoom_number() {
        return room_number;
    }

    public String getStatus() {
        return status;
    }
}
