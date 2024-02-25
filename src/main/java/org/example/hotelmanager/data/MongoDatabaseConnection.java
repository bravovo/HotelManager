package org.example.hotelmanager.data;

import com.mongodb.client.*;
import com.mongodb.client.model.ReturnDocument;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;

import java.util.Collection;

public class MongoDatabaseConnection {
    Hotel hotel = new Hotel();
    Document hotelDoc = new Document();
    public void getHotel(){
        HotelHolder hotelHolder = HotelHolder.getInstance();
        hotel = hotelHolder.getUser();
    }
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
            hotelDoc = newHotel;
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
            hotelDoc = foundDoc;
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
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return null;
    }
    public ObservableList<String> getRoomTypesNames(){
        ObservableList<String> list = FXCollections.observableArrayList();
        DataCredentials dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("room_types");
            FindIterable<Document> documents = collection.find();
            for(Document coll : documents){
                list.add(coll.getString("type_name"));
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return list;
    }

    public void createRoom(String typeName, String roomName, String roomDescription, int roomNumber){
        getHotel();
        DataCredentials dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("rooms");
            int count = (int)collection.countDocuments();
            Document room = new Document("room_id", count);
            room.append("hotel_id", hotel.getHotel_id());
            room.append("type_id", getRoomTypeId(typeName));
            room.append("type_name", typeName);
            room.append("roomName", roomName);
            room.append("description", roomDescription);
            room.append("room_number", getRoomNumber(roomNumber));
            collection.insertOne(room);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public int getRoomNumber(int roomNumber){
        DataCredentials dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("rooms");
            if (roomNumber == 0){
                roomNumber = (int)collection.countDocuments(new Document("hotel_id",
                        hotelDoc.getInteger("hotel_id")));
            }
            if(collection.find(new Document("room_number", roomNumber)).first() != null){
                roomNumber = roomNumber + 1;
                getRoomNumber(roomNumber);
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return roomNumber;
    }

    private int getRoomTypeId(String typeName) {
        DataCredentials dataCredentials = new DataCredentials();
        Document docToFind = new Document();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("room_types");
            docToFind = collection.find(new Document("type_name", typeName)).first();
            if (docToFind == null){
                docToFind = new Document("type_id", 0);
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return docToFind.getInteger("type_id");
    }
}