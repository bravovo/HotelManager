package org.example.hotelmanager.data;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.example.hotelmanager.objects.Hotel;
import org.example.hotelmanager.objects.HotelHolder;
import org.example.hotelmanager.objects.Room;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MongoDatabaseConnection {
    DataCredentials dataCredentials = new DataCredentials();
    Hotel hotel;
    Document hotelDoc = new Document();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    public boolean createHotelObject(String hotelName, String address, String login, String adminPass){
        dataCredentials = new DataCredentials();
        Document newHotel = new Document();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            int count = (int)collection.countDocuments();
            if(collection.find(new Document("address", address)).first() != null ||
                    collection.find(new Document("login", login)).first() != null){
                return false;
            }
            newHotel.append("hotel_id", count);
            newHotel.append("hotel_name", hotelName);
            newHotel.append("login", login);
            newHotel.append("password", adminPass);
            newHotel.append("address", address);
            collection.insertOne(newHotel);
            hotelDoc = newHotel;
            this.hotel = new Hotel(count, hotelName, address, login, adminPass);
            hotelHolder.setUser(hotel);
            updateRoomList();
            return true;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return true;
    }
    public Hotel loginAcc(Document document){
        dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase database = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = database.getCollection("hotels");
            Document foundDoc = collection.find(document).first();
            hotelDoc = foundDoc;
            if(foundDoc != null){
                if(foundDoc.containsKey("email") && foundDoc.containsKey("stars") && foundDoc.containsKey("rooms_count")){
                    this.hotel = new Hotel(foundDoc.getInteger("hotel_id"), foundDoc.getString("hotel_name"),
                            foundDoc.getString("address"), foundDoc.getString("login"),
                            foundDoc.getString("password"), foundDoc.getString("email"),
                            foundDoc.getInteger("stars"), foundDoc.getInteger("rooms_count"));
                    hotelHolder.setUser(hotel);
                    updateRoomList();
                    return this.hotel;
                }
                this.hotel = new Hotel(foundDoc.getInteger("hotel_id"), foundDoc.getString("hotel_name"),
                        foundDoc.getString("address"), foundDoc.getString("login"),
                        foundDoc.getString("password"));
                hotelHolder.setUser(hotel);
                updateRoomList();
                return this.hotel;
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return new Hotel();
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
        hotel = hotelHolder.getUser();
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
            room.append("room_number", roomNumber + 1);
            room.append("status", "Available");
            room.append("from", new Date()); // База створює дату і час сама, але часовий пояс збитий
            LocalDate today = LocalDate.now(); // Дає правильну дату без часу
            room.append("to", today);

            collection.insertOne(room);
            hotelHolder.setUser(hotel);
            updateRoomList();
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }
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
    public void updateRoomList(){
        hotel = hotelHolder.getUser();
        ObservableList<Room> list = FXCollections.observableArrayList();
        int id = hotel.getHotel_id();
        dataCredentials = new DataCredentials();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("rooms");
            FindIterable<Document> documents = collection.find(new Document("hotel_id", id));
            for(Document doc : documents){
                if(doc.getInteger("hotel_id") == id){
                    Room room = new Room(
                            doc.getInteger("room_id"),
                            doc.getInteger("hotel_id"),
                            doc.getInteger("type_id"),
                            doc.getString("type_name"),
                            doc.getString("room_name"),
                            doc.getString("description"),
                            doc.getInteger("room_number"),
                            doc.getString("status"),
                            doc.getDate("from"),
                            doc.getDate("to")
                    );
                    list.add(room);
                    hotel.setRooms(list);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        hotelHolder.setUser(hotel);
    }
}