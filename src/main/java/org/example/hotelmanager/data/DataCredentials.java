package org.example.hotelmanager.data;

public class DataCredentials {
    private final String url =
            "mongodb+srv://dataUser:manageHotel1902@firstcluster.arqkkwq.mongodb.net/?retryWrites=true&w=majority";

    public DataCredentials() {
    }

    public String getUrl() {
        return url;
    }
}
