package org.example.hotelmanager.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Room {
    private int room_id;
    private int hotel_id;
    private int type_id;
    private String type_name;
    private String room_name;
    private String room_description;
    private int room_number;
    private String status;
    private String dateFrom;
    private String dateTo;

    public Room(int room_id, int hotel_id,
                int type_id, String type_name,
                String room_name, String room_description, int room_number, String status, Date dateFrom, Date dateTo) {
        this.room_id = room_id;
        this.hotel_id = hotel_id;
        this.type_id = type_id;
        this.type_name = type_name;
        this.room_name = room_name;
        this.room_description = room_description;
        this.room_number = room_number;
        this.status = status;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(dateFrom);
        this.dateFrom = formattedDate;
        formattedDate = dateFormat.format(dateTo);
        this.dateTo = formattedDate;
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

    public String getDateFrom() {
        return dateFrom;
    }
    public String getDateTo() {
        return dateTo;
    }
}
