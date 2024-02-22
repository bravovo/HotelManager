package org.example.hotelmanager.data;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDatabaseConnection {

    public Document createHotelObj(String hotelName, String address, String login, String adminPass){
        DataCredentials dataCredentials = new DataCredentials();
        Document newHotel = new Document();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            int count = (int)collection.countDocuments();
            if(collection.find(new Document("address", address)).first() != null ||
                    collection.find(new Document("login", login)).first() != null){
                return null;
            }
            newHotel.append("id", count);
            newHotel.append("hotel_name", hotelName);
            newHotel.append("login", login);
            newHotel.append("password", adminPass);
            newHotel.append("address", address);
            collection.insertOne(newHotel);
            return newHotel;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return newHotel;
    }

    public boolean loginAcc(Document document){
        DataCredentials dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            if(collection.find(document).first() != null){
                return true;
            }
//            return collection.find(document).first() != null;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return false;
    }
}