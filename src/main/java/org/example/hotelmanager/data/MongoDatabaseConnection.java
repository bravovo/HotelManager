package org.example.hotelmanager.data;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;
import org.example.hotelmanager.objects.Room;

import java.util.Collection;

public class MongoDatabaseConnection {
    DataCredentials dataCredentials = new DataCredentials();
    Hotel hotel = new Hotel();
    Document hotelDoc = new Document();
    public void getHotel(){
        HotelHolder hotelHolder = HotelHolder.getInstance();
        hotel = hotelHolder.getUser();
    }
    public Hotel createHotelObj(String hotelName, String address, String login, String adminPass){
        dataCredentials = new DataCredentials();
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
        dataCredentials = new DataCredentials();
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
        dataCredentials = new DataCredentials();
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

    public void createRoom(String typeName, String roomName, String roomDescription){
        getHotel();
        dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("rooms");
            int count = (int)collection.countDocuments();
            int roomNumber = (int)collection.countDocuments(new Document("hotel_id",
                    hotel.getHotel_id()));
            Document room = new Document("room_id", count);
            room.append("hotel_id", hotel.getHotel_id());
            room.append("type_id", getRoomTypeId(typeName));
            room.append("type_name", typeName);
            room.append("room_name", roomName);
            room.append("description", roomDescription);
            room.append("room_number", roomNumber + 1); //getRoomNumber()
            collection.insertOne(room);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

//    public int getRoomNumber(){
//        int roomNumber = 0;
//        dataCredentials = new DataCredentials();
//        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
//            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
//            MongoCollection<Document> collection = database.getCollection("rooms");
//            roomNumber = (int)collection.countDocuments(new Document("hotel_id",
//                        hotelDoc.getInteger("hotel_id")));
//        } catch(Exception exception){
//            exception.printStackTrace();
//        }
//        return roomNumber;
//    }

    private int getRoomTypeId(String typeName) {
        dataCredentials = new DataCredentials();
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

    public void updateHotel(String email, int roomsCount, int starsCount) {
        getHotel();
        dataCredentials = new DataCredentials();
        Document docToFind;
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            docToFind = collection.find(new Document("hotel_id", hotel.getHotel_id())).first();
            collection.updateOne(Filters.eq("hotel_id", docToFind.getInteger("hotel_id")),
                    Updates.combine(
                            Updates.set("email", email),
                            Updates.set("stars", starsCount),
                            Updates.set("rooms_count", roomsCount)
                    )
            );
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    public ObservableList<Room> getRooms() {
        ObservableList<Room> list = FXCollections.observableArrayList();
        int id = hotel.getHotel_id();
        dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("rooms");
            FindIterable<Document> documents = collection.find(new Document("hotel_id", id));
            for(Document doc : documents){
                Room room = new Room(
                        doc.getInteger("room_id"),
                        doc.getInteger("hotel_id"),
                        doc.getInteger("type_id"),
                        doc.getString("type_name"),
                        doc.getString("room_name"),
                        doc.getString("description"),
                        doc.getInteger("room_number")
                );
                list.add(room);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
}