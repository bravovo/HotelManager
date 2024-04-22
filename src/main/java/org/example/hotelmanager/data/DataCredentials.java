package org.example.hotelmanager.data;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class DataCredentials {
    Properties properties = new Properties();
    private String url = "";

    public DataCredentials() {
        try {
            properties.load(new FileReader("src/main/java/org/example/hotelmanager/data/dbconfig.properties"));
            url = properties.getProperty("dburl").toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getUrl() {
        return url;
    }
}
