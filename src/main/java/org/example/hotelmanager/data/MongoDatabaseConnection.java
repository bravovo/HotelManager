package org.example.hotelmanager.data;

import com.mongodb.client.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.bson.Document;
import org.example.hotelmanager.FormBuilder;
import org.example.hotelmanager.model.*;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class MongoDatabaseConnection {
    FormBuilder formBuilder = new FormBuilder();
    DataCredentials dataCredentials = new DataCredentials();
    Hotel hotel;
    Client client;
    Document foundHotel = new Document();
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
            foundHotel = newHotel;
            this.hotel = new Hotel(hotelCount, hotelName, address, login, adminPass, email, roomsCount, phoneNumber);
            hotelHolder.setUser(hotel);
            updateRoomList();
            return true;
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return true;
    }
    public void loginAccount(Document document, ActionEvent event){
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("hotels");
            Document findHotel = collection.find(document).first();
            foundHotel = findHotel;
            if(findHotel != null){
                this.hotel = new Hotel(findHotel.getInteger("hotel_id"), findHotel.getString("hotel_name"),
                        findHotel.getString("address"), findHotel.getString("login"),
                        findHotel.getString("password"), findHotel.getString("email"),
                        findHotel.getInteger("rooms_count"),
                        findHotel.getString("phone_number"));
                hotelHolder.setUser(hotel);
                updateRoomList();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                formBuilder.openWindow(stage, "admin-forms/admin-form.fxml",
                        "Версія для адміністратора | Система управління готелями", 1100, 750);
                return;
            }

            // Якщо не знайдено акаунт готелю, то шукати акаунт користувача ----------------------

            collection = mongoDatabase.getCollection("clients");
            Document clientDocument = new Document("client_email", document.getString("login"))
                    .append("password", document.getString("password"));
            Document findClient = collection.find(clientDocument).first();
            if(findClient != null){
                this.client = new Client(
                        findClient.getInteger("client_id"),
                        findClient.getString("firstname"),
                        findClient.getString("lastname"),
                        findClient.getString("client_email"),
                        findClient.getString("client_phone"),
                        findClient.getDate("dateOfBirth")
                                .toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDate()
                );
                clientHolder.setUser(client);
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                formBuilder.openWindow(stage, "client-forms/client-form.fxml",
                        "Версія для адміністратора | Система управління готелями", 1100, 750);
                return;
            }

            // Якщо не знайдено користувача також, тоді акаунта не існує, або введені неправильні дані

            formBuilder.errorValidation("Неправильно введені дані для авторизації");

        } catch(Exception exception){
            exception.printStackTrace();
        }
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

    public void createBooking(LocalDate checkIN_date, LocalDate checkOUT_date){
        client = clientHolder.getUser();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookings");
            FindIterable<Document> bookingDocuments = collection.find(new Document("hotel_id", 0).append("room_number", 1));
            boolean isAvailable = true;
            for(Document booking : bookingDocuments){
                Date checkIn = booking.getDate("checkIN_date");
                Date checkOut = booking.getDate("checkIN_date");
                LocalDate checkInDate = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOutDate = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if((checkOutDate.compareTo(checkIN_date) >= 0 || checkInDate.compareTo(checkOUT_date) <= 0)){
                    isAvailable = false;
                    break;
                }
            }
            if(isAvailable){
                int bookingCount = (int)collection.countDocuments();
                Document book = new Document("booking_id", bookingCount);
                book.append("guest_id", client.getClientID());
                book.append("hotel_id", 0);
                book.append("room_number", 1);
                book.append("checkIN_date", checkIN_date);
                book.append("checkOUT_date", checkOUT_date);
                book.append("total_price", 1200);
                collection.insertOne(book);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}