package org.example.hotelmanager.data;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDatabaseConnection {

    public boolean createHotelObj(String hotelName, String address, String admin, String adminPass, int starsCount){
        DataCredentials dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            int count = (int)collection.countDocuments();
            Document newHotel = new Document();
            if(collection.find(new Document("address", address)).first() != null || collection.find(new Document("administrator", admin)).first() != null){
                return false;
            }
            newHotel.append("id", count);
            newHotel.append("hotel_name", hotelName);
            newHotel.append("administrator", admin);
            newHotel.append("password", adminPass);
            newHotel.append("address", address);
            newHotel.append("stars", starsCount == 0 ? 2 : starsCount);
            collection.insertOne(newHotel);
            return true;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return true;
    }
}