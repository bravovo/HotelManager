package org.example.hotelmanager.model;

public class RoomType {
    private int typeID;
    private String typeName;
    private  String typeDescription;
    private  int typeCapacity;

    public RoomType(int typeID, String typeName, String typeDescription, int typeCapacity) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.typeDescription = typeDescription;
        this.typeCapacity = typeCapacity;
    }

    public int getTypeID() {
        return typeID;
    }
    public String getTypeName() {
        return typeName;
    }
    public String getTypeDescription() {
        return typeDescription;
    }
    public int getTypeCapacity() {
        return typeCapacity;
    }
}
