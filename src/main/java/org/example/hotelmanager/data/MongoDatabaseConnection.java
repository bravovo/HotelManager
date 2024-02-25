package org.example.hotelmanager.data;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.example.hotelmanager.objects.Hotel;

public class MongoDatabaseConnection {

    public Hotel createHotelObj(String hotelName, String address, String login, String adminPass){
        DataCredentials dataCredentials = new DataCredentials();
        Document newHotel = new Document();
        Hotel hotel = new Hotel();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            int count = (int)collection.countDocuments();
            if(collection.find(new Document("address", address)).first() != null ||
                    collection.find(new Document("login", login)).first() != null){
                return null;
            }
            newHotel.append("hotel_id", count);
            newHotel.append("hotel_name", hotelName);
            newHotel.append("login", login);
            newHotel.append("password", adminPass);
            newHotel.append("address", address);
            collection.insertOne(newHotel);
            hotel = new Hotel(count, hotelName, login, adminPass, address);
            return hotel;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return hotel;
    }

    public Hotel loginAcc(Document document){
        DataCredentials dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            Document foundDoc = collection.find(document).first();
            if(foundDoc != null){
                Hotel hotel;
                if(foundDoc.containsKey("email") && foundDoc.containsKey("stars") && foundDoc.containsKey("rooms_count")){
                    hotel = new Hotel(foundDoc.getInteger("hotel_id"), foundDoc.getString("hotel_name"),
                            foundDoc.getString("address"), foundDoc.getString("login"),
                            foundDoc.getString("password"), foundDoc.getString("email"),
                            foundDoc.getInteger("stars"), foundDoc.getInteger("rooms_count"));
                    return hotel;
                }
                hotel = new Hotel(foundDoc.getInteger("hotel_id"), foundDoc.getString("hotel_name"),
                        foundDoc.getString("address"), foundDoc.getString("login"),
                        foundDoc.getString("password"));
                return hotel;
            }
//            return collection.find(document).first() != null;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }

    public void createRoom(){

    }
}