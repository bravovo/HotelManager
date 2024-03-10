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
import java.time.*;
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
    public void createRoom(String typeName, String roomName, String roomDescription, String roomPrice){
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
            room.append("status", "Доступна");
            room.append("from", LocalDate.now());
            room.append("to", LocalDate.now().plusDays(1));
            double roomPriceConverted = Double.parseDouble(roomPrice);
            room.append("price", roomPriceConverted);

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
        int id = hotel.getHotel_id();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomsCollection = mongoDatabase.getCollection("rooms");
            FindIterable<Document> roomDocuments = roomsCollection.find(new Document("hotel_id", id));
            MongoCollection<Document> bookingsCollection = mongoDatabase.getCollection("bookings");
            for(Document roomDoc : roomDocuments){
                FindIterable<Document> bookingDocuments =
                        bookingsCollection.find(new Document("hotel_id", id)
                                .append("room_number", roomDoc.getInteger("room_number")));
                Document updatedRoom;
                for(Document bookingDoc : bookingDocuments) {
                    boolean toUpdate = false;
                    String status = "Доступна";
                    Date dateTO = new Date();
                    Date dateFROM = new Date();
                    Date dateFromDB = bookingDoc.getDate("checkIN_date");
                    Instant instant = dateFromDB.toInstant();
                    LocalDate checkIN_date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

                    dateFromDB = bookingDoc.getDate("checkOUT_date");
                    instant = dateFromDB.toInstant();
                    LocalDate checkOUT_date = instant.atZone(ZoneId.systemDefault()).toLocalDate();

                    if (LocalDate.now().isAfter(checkIN_date) && LocalDate.now().isBefore(checkOUT_date)) {
                        status = "Занята";
                        dateFROM = bookingDoc.getDate("checkIN_date");
                        dateTO = bookingDoc.getDate("checkOUT_date");
                        toUpdate = true;
                    } else if (LocalDateTime.now().isBefore(LocalDateTime.of(checkOUT_date, LocalTime.of(11, 30))) &&
                    LocalDateTime.now().isAfter(LocalDateTime.of(checkOUT_date, LocalTime.of(0, 0)))) {
                        LocalDateTime localDateTime = LocalDateTime.now();
                        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                        dateFROM = Date.from(instant);

                        status = "Чек-аут";

                        localDateTime = LocalDateTime.of(checkOUT_date, LocalTime.of(11, 30));
                        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                        dateTO = Date.from(instant);
                        toUpdate = true;
                    } else if (LocalDateTime.now().isAfter(LocalDateTime.of(checkIN_date, LocalTime.of(11, 30))) &&
                            LocalDateTime.now().isBefore(LocalDateTime.of(checkIN_date, LocalTime.of(14, 30)))) {
                        LocalDateTime localDateTime = LocalDateTime.of(checkIN_date, LocalTime.of(11, 30));
                        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                        dateFROM = Date.from(instant);

                        status = "Прибирання";

                        localDateTime = LocalDateTime.of(checkIN_date, LocalTime.of(14, 30));
                        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                        dateTO = Date.from(instant);
                        toUpdate = true;
                    } else if (LocalDateTime.now().isAfter(LocalDateTime.of(checkIN_date, LocalTime.of(14, 30)))) {
                        LocalDateTime localDateTime = LocalDateTime.of(checkIN_date, LocalTime.of(14, 30));
                        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                        dateFROM = Date.from(instant);

                        status = "Чек-ін";

                        localDateTime = LocalDateTime.of(checkIN_date.plusDays(1), LocalTime.of(0, 0));
                        instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                        dateTO = Date.from(instant);
                        toUpdate = true;
                    }
                    if(toUpdate){
                        updatedRoom = new Document("$set", new Document(
                                "room_id", roomDoc.getInteger("room_id"))
                                .append("hotel_id", roomDoc.getInteger("hotel_id"))
                                .append("type_id", roomDoc.getInteger("type_id"))
                                .append("type_name", roomDoc.getString("type_name"))
                                .append("room_name", roomDoc.getString("room_name"))
                                .append("description", roomDoc.getString("description"))
                                .append("room_number", roomDoc.getInteger("room_number"))
                                .append("status", status)
                                .append("from", dateFROM)
                                .append("to", dateTO)
                                .append("price", roomDoc.getDouble("price"))
                        );
                        roomsCollection.updateOne(new Document("hotel_id", id)
                                .append("room_number", roomDoc.getInteger("room_number")), updatedRoom);
                        break;
                    }
                    LocalDateTime localDateTime = LocalDateTime.now();
                    instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                    dateFROM = Date.from(instant);

                    localDateTime = LocalDateTime.now().plusDays(1);
                    instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
                    dateTO = Date.from(instant);
                    updatedRoom = new Document("$set", new Document(
                            "room_id", roomDoc.getInteger("room_id"))
                            .append("hotel_id", roomDoc.getInteger("hotel_id"))
                            .append("type_id", roomDoc.getInteger("type_id"))
                            .append("type_name", roomDoc.getString("type_name"))
                            .append("room_name", roomDoc.getString("room_name"))
                            .append("description", roomDoc.getString("description"))
                            .append("room_number", roomDoc.getInteger("room_number"))
                            .append("status", status)
                            .append("from", dateFROM)
                            .append("to", dateTO)
                    );
                    roomsCollection.updateOne(new Document("hotel_id", id)
                            .append("room_number", roomDoc.getInteger("room_number")), updatedRoom);
                }
            }
            ObservableList<Room> roomList = FXCollections.observableArrayList();
            roomDocuments = roomsCollection.find(new Document("hotel_id", id));
            for(Document roomDoc : roomDocuments){
                Room room = new Room(
                        roomDoc.getInteger("room_id"),
                        roomDoc.getInteger("hotel_id"),
                        roomDoc.getInteger("type_id"),
                        roomDoc.getString("type_name"),
                        roomDoc.getString("room_name"),
                        roomDoc.getString("description"),
                        roomDoc.getInteger("room_number"),
                        roomDoc.getString("status"),
                        roomDoc.getDate("from"),
                        roomDoc.getDate("to"),
                        roomDoc.getDouble("price")
                );
                bookingsCollection = mongoDatabase.getCollection("bookings");
                FindIterable<Document> bookingDocuments =
                        bookingsCollection.find(new Document("room_number", roomDoc.getInteger("room_number")));
                ObservableList<Booking> bookings = FXCollections.observableArrayList();
                for(Document bookingDoc : bookingDocuments){
                    Object totalPrice = bookingDoc.get("total_price");
                    double priceInDouble = 0;
                    if (totalPrice instanceof Integer) {
                        Integer intValue = (Integer) totalPrice;
                        priceInDouble = intValue.doubleValue();
                    } else if (totalPrice instanceof Double) {
                        priceInDouble = (Double) totalPrice;
                    }
                    Booking booking = new Booking(
                            bookingDoc.getInteger("booking_id"),
                            bookingDoc.getInteger("hotel_id"),
                            bookingDoc.getInteger("guest_id"),
                            bookingDoc.getInteger("room_number"),
                            bookingDoc.getDate("checkIN_date"),
                            bookingDoc.getDate("checkOUT_date"),
                            priceInDouble
                    );
                    bookings.add(booking);
                }
                room.setBookings(bookings);
                roomList.add(room);
            }
            hotel.setRooms(roomList);
            hotelHolder.setUser(hotel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void updateRoomBookings(){ // TODO Доробити це, з'єднати з методом updateRoomList()
//        hotel = hotelHolder.getUser();
//        ObservableList<Room> list = FXCollections.observableArrayList();
//        int id = hotel.getHotel_id();
//        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
//            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
//            MongoCollection<Document> collection = mongoDatabase.getCollection("bookings");
//            for(Room room : hotel.getRoomsForList()){
//                FindIterable<Document> bookingDocument = collection.find(new Document("room_number", room.getRoom_number()));
//                for(Document booking : bookingDocument){
//                    LocalDate checkIN_date = booking.getDate("checkIN_date")
//                            .toInstant()
//                            .atZone(ZoneId.systemDefault())
//                            .toLocalDate().minusDays(1);
//                    LocalDate checkOUT_date = booking.getDate("checkOUT_date")
//                            .toInstant()
//                            .atZone(ZoneId.systemDefault())
//                            .toLocalDate().minusDays(1);
//                    if(LocalDate.now().isAfter(checkIN_date) && LocalDate.now().isBefore(checkOUT_date)){
//                        room.setStatus("Занята");
//                        room.setDateTo(checkIN_date);
//                        room.setDateTo(checkOUT_date);
//                        break;
//                    }
//                    else if(LocalDateTime.now().isBefore(LocalDateTime.of(checkIN_date, LocalTime.of(11, 30)))){
//                        room.setStatus("Чек-аут");
//                        break;
//                    }
//                    else if(LocalDateTime.now().isAfter(LocalDateTime.of(checkIN_date, LocalTime.of(11, 30))) &&
//                            LocalDateTime.now().isBefore(LocalDateTime.of(checkIN_date, LocalTime.of(14, 30)))){
//                        room.setStatus("Прибирання");
//                        break;
//                    }
//                    else if(LocalDateTime.now().isAfter(LocalDateTime.of(checkIN_date, LocalTime.of(14, 30)))){
//                        room.setStatus("Чек-ін");
//                        break;
//                    }
//                    else {
//                        room.setStatus("Доступна");
//                        break;
//                    }
//                }
//                list.add(room);
//            }
//            hotel.setRooms(list);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        hotelHolder.setUser(hotel);
//    }

    public void createBooking(LocalDate checkIN_date, LocalDate checkOUT_date){
        client = clientHolder.getUser();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("bookings");
            FindIterable<Document> bookingDocuments = collection.find(new Document("hotel_id", 0).append("room_number", 2));
            boolean isAvailable = true;
            for(Document booking : bookingDocuments){
                Date checkIn = booking.getDate("checkIN_date");
                Date checkOut = booking.getDate("checkOUT_date");
                LocalDate checkInDate = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate checkOutDate = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                if (checkInDate.isBefore(checkOUT_date) && checkOutDate.isAfter(checkIN_date)) {
                    isAvailable = false;
                    System.out.println("NOPE");
                    break;
                }
            }
            if(isAvailable){
                int bookingCount = (int)collection.countDocuments();
                Document book = new Document("booking_id", bookingCount);
                book.append("guest_id", client.getClientID());
                book.append("hotel_id", 0);
                book.append("room_number", 2);
                book.append("checkIN_date", Date.from(checkIN_date
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()));
                book.append("checkOUT_date", Date.from(checkOUT_date
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()));
                book.append("total_price", 1200);
                System.out.println("DONE");
                collection.insertOne(book);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<Room> findRoomByFilter(String filter, String value){
        hotel = hotelHolder.getUser();
        ObservableList<Room> roomList = FXCollections.observableArrayList();
        try (MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomCollection = mongoDatabase.getCollection("rooms");
            FindIterable<Document> roomsFound = null;
            switch (filter){
                case "Статус" -> {
                    roomsFound = roomCollection.find(new Document("hotel_id",
                            hotel.getHotel_id()).append("status", value));
                }
                case "Назва кімнати" -> {
                    roomsFound = roomCollection.find(new Document("hotel_id",
                            hotel.getHotel_id()).append("room_name", value));
                }
                case "Тип кімнати" -> {
                    roomsFound = roomCollection.find(new Document("hotel_id",
                            hotel.getHotel_id()).append("type_name", value));
                }
                case "Номер кімнати" -> {
                    roomsFound = roomCollection.find(new Document("hotel_id",
                            hotel.getHotel_id()).append("room_number", Integer.parseInt(value)));
                }
                case "Ціна" -> {
                    roomsFound = roomCollection.find(new Document("hotel_id",
                            hotel.getHotel_id()).append("price", Double.parseDouble(value)));
                }
            }
            for(Document roomDocument : roomsFound){
                Room room = new Room(
                        roomDocument.getInteger("room_id"),
                        roomDocument.getInteger("hotel_id"),
                        roomDocument.getInteger("type_id"),
                        roomDocument.getString("type_name"),
                        roomDocument.getString("room_name"),
                        roomDocument.getString("description"),
                        roomDocument.getInteger("room_number"),
                        roomDocument.getString("status"),
                        roomDocument.getDate("from"),
                        roomDocument.getDate("to"),
                        roomDocument.getDouble("price")
                );
                roomList.add(room);
            }
        }catch (Exception e){
            formBuilder.errorValidation("Помилка виконання. Спробуйте ще раз");
        }
        return roomList;
    }
}