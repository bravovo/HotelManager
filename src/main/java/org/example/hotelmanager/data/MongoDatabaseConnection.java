package org.example.hotelmanager.data;

import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import org.example.hotelmanager.model.*;

import java.time.LocalDate;
import java.util.Date;

public class MongoDatabaseConnection {
    DataCredentials dataCredentials = new DataCredentials();
    Hotel hotel;
    Client client;
    Document hotelDoc = new Document();
    Document clientDoc = new Document();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    ClientHolder clientHolder = ClientHolder.getInstance();

    public boolean registerClientAccount(String firstName, String lastName, String clientEmail, String clientPhoneNumber,
                                         LocalDate dateOfBirth, String clientPassword){
        Document newClient = new Document();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("clients");
            int clientCount = (int)collection.countDocuments();
            if(collection.find(new Document("client_email", clientEmail)).first() != null){
                return false;
            }
            newClient.append("client_id", clientCount);
            newClient.append("firstname", firstName);
            newClient.append("lastname", firstName);
            newClient.append("client_email", clientEmail);
            newClient.append("client_phone", clientPhoneNumber);
            newClient.append("dateOfBirth", dateOfBirth);
            newClient.append("password", clientPassword);
            collection.insertOne(newClient);
            clientDoc = newClient;
            this.client = new Client(clientCount, firstName, lastName, clientEmail, clientPhoneNumber, dateOfBirth);
            clientHolder.setUser(client);
        }catch(Exception e){
            e.printStackTrace();
        }
        return true;
    }
    public boolean registerHotel(String hotelName, String address, String login, String adminPass,
                                     String email, String phoneNumber, int roomsCount){
        Document newHotel = new Document();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("hotels");
            int hotelCount = (int)collection.countDocuments();
            if(collection.find(new Document("address", address)).first() != null ||
                    collection.find(new Document("login", login)).first() != null){
                return false;
            }
            newHotel.append("hotel_id", hotelCount);
            newHotel.append("hotel_name", hotelName);
            newHotel.append("login", login);
            newHotel.append("password", adminPass);
            newHotel.append("address", address);
            newHotel.append("email", email);
            newHotel.append("rooms_count", roomsCount);
            newHotel.append("phone_number", phoneNumber);
            collection.insertOne(newHotel);
            hotelDoc = newHotel;
            this.hotel = new Hotel(hotelCount, hotelName, address, login, adminPass, email, roomsCount, phoneNumber);
            hotelHolder.setUser(hotel);
            updateRoomList();
            return true;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return true;
    }
    public Hotel loginAccount(Document document){ //TODO ПЕРЕВІРКА НА ТИП АКАУНТА (АДМІН/КЛІЄНТ)
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("hotels");
            Document foundDoc = collection.find(document).first();
            hotelDoc = foundDoc;
            if(foundDoc != null){
                this.hotel = new Hotel(foundDoc.getInteger("hotel_id"), foundDoc.getString("hotel_name"),
                        foundDoc.getString("address"), foundDoc.getString("login"),
                        foundDoc.getString("password"), foundDoc.getString("email"),
                        foundDoc.getInteger("rooms_count"),
                        foundDoc.getString("phone_number"));
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
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("room_types");
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
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("rooms");
            int roomsCount = (int)collection.countDocuments();
            int roomNumber = (int)collection.countDocuments(new Document("hotel_id",
                    hotel.getHotel_id()));
            Document room = new Document("room_id", roomsCount);
            room.append("hotel_id", hotel.getHotel_id());
            room.append("type_id", getRoomTypeId(typeName));
            room.append("type_name", typeName);
            room.append("room_name", roomName);
            room.append("description", roomDescription);
            room.append("room_number", roomNumber + 1);
            room.append("status", "Available");
            room.append("from", new Date()); // База створює дату і час сама, але часовий пояс збитий
            LocalDate today = LocalDate.now().plusDays(3); // Дає правильну дату без часу
            room.append("to", today);

            collection.insertOne(room);
            hotelHolder.setUser(hotel);
            updateRoomList();
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }
    private int getRoomTypeId(String typeName) {
        Document docToFind = new Document();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("room_types");
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