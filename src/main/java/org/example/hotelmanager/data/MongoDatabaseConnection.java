package org.example.hotelmanager.data;

import com.mongodb.client.*;
import com.mongodb.client.model.Sorts;
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
    Room roomToDelete = new Room();
    Booking bookingToDelete = new Booking();
    Hotel hotel;
    Client client;
    Document foundHotel = new Document();
    Document clientDoc = new Document();
    HotelHolder hotelHolder = HotelHolder.getInstance();
    ClientHolder clientHolder = ClientHolder.getInstance();
    RoomHolder roomHolder = RoomHolder.getInstance();
    BookingHolder bookingHolder = BookingHolder.getInstance();

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
                setBookingList();
                Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                formBuilder.openWindow(stage, "admin-forms/admin-form.fxml",
                        "Версія для адміністратора | Система управління готелями", 1350, 750);
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

    private void setBookingList() {
        ObservableList<Booking> bookings = FXCollections.observableArrayList();
        ObservableList<Booking> bookingsDone = FXCollections.observableArrayList();
        //hotel = hotelHolder.getUser();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> bookingsCollection = mongoDatabase.getCollection("bookings");
            FindIterable<Document> bookingOfHotel =
                    bookingsCollection.find(new Document("hotel_id", hotel.getHotel_id()));
            for(Document bookingDoc : bookingOfHotel){
                Booking booking = new Booking(
                        bookingDoc.getInteger("booking_id"),
                        hotel.getHotel_id(),
                        bookingDoc.getInteger("guest_id"),
                        bookingDoc.getString("guest_first_name"),
                        bookingDoc.getString("guest_second_name"),
                        bookingDoc.getString("guest_phone_number"),
                        bookingDoc.getString("guest_email"),
                        bookingDoc.getInteger("room_number"),
                        bookingDoc.getString("room_type"),
                        bookingDoc.getDate("checkIN_date")
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        bookingDoc.getDate("checkOUT_date")
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        bookingDoc.getDouble("total_price"),
                        bookingDoc.getString("add_info"),
                        bookingDoc.getInteger("people_count")
                );
                bookings.add(booking);
            }
            MongoCollection<Document> bookingsDoneCollection = mongoDatabase.getCollection("booking_done");
            FindIterable<Document> bookingDoneOfHotel =
                    bookingsDoneCollection.find(new Document("hotel_id", hotel.getHotel_id()));
            for(Document bookingDoc : bookingDoneOfHotel){
                Booking booking = new Booking(
                        bookingDoc.getInteger("booking_id"),
                        hotel.getHotel_id(),
                        bookingDoc.getInteger("guest_id"),
                        bookingDoc.getString("guest_first_name"),
                        bookingDoc.getString("guest_second_name"),
                        bookingDoc.getString("guest_phone_number"),
                        bookingDoc.getString("guest_email"),
                        bookingDoc.getInteger("room_number"),
                        bookingDoc.getString("room_type"),
                        bookingDoc.getDate("checkIN_date")
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        bookingDoc.getDate("checkOUT_date")
                                .toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                        bookingDoc.getDouble("total_price"),
                        bookingDoc.getString("add_info"),
                        bookingDoc.getInteger("people_count")
                );
                bookingsDone.add(booking);
            }
            hotel.setBookingsDone(bookingsDone);
            hotel.setBookings(bookings);
            hotelHolder.setUser(hotel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ObservableList<String> getRoomTypesNames(){
        ObservableList<String> typeNameList = FXCollections.observableArrayList();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("room_types");
            FindIterable<Document> documents = collection.find();
            for(Document coll : documents){
                typeNameList.add(coll.getString("type_name"));
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return typeNameList;
    }
    public ObservableList<Integer> getRoomTypesIDs() {
        ObservableList<Integer> typeIDsList = FXCollections.observableArrayList();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> collection = mongoDatabase.getCollection("room_types");
            FindIterable<Document> documents = collection.find();
            for(Document coll : documents){
                typeIDsList.add(coll.getInteger("type_id"));
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        return typeIDsList;
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
    public void createRoom(String typeName, String roomName, String roomDescription, String roomPrice){
        hotel = hotelHolder.getUser();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomsCollection = mongoDatabase.getCollection("rooms");
            int roomsCount = (int)roomsCollection.countDocuments();
            Document sortedByIdBooking = roomsCollection.find().sort(Sorts.descending("room_number")).first();
            int roomNumber = 0;
            if (sortedByIdBooking != null){
                roomNumber = sortedByIdBooking.getInteger("room_number");
            }
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
            int capacity = 0;
            if(typeName.equals("F")){
                capacity = 4;
            } else if (typeName.charAt(1) == 'S') {
                capacity = 1;
            } else if (typeName.charAt(1) == 'D' || typeName.charAt(1) == 'T'){
                capacity = 2;
            }
            room.append("capacity", capacity);

            roomsCollection.insertOne(room);
            hotelHolder.setUser(hotel);
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }
    public void editRoom(Room room){
        hotel = hotelHolder.getUser();
        roomHolder.setRoom(room);
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())) {
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomCollection = mongoDatabase.getCollection("rooms");
            MongoCollection<Document> bookingCollection = mongoDatabase.getCollection("bookings");
            int capacity = 0;
            if(room.getType_name().equals("F")){
                capacity = 4;
            } else if (room.getType_name().charAt(1) == 'S') {
                capacity = 1;
            } else if (room.getType_name().charAt(1) == 'D' || room.getType_name().charAt(1) == 'T'){
                capacity = 2;
            }
            Document roomDocument = new Document("$set", new Document("room_id", room.getRoom_id())
                    .append("hotel_id", hotel.getHotel_id())
                    .append("type_id", room.getType_id())
                    .append("type_name", room.getType_name())
                    .append("room_name", room.getRoom_name())
                    .append("description", room.getRoom_description())
                    .append("room_number", room.getRoom_number())
                    .append("status", room.getStatus())
                    .append("from", room.getDateFrom())
                    .append("to", room.getDateTo())
                    .append("price", room.getPrice())
                    .append("capacity", capacity));
            roomCollection.updateOne(new Document("hotel_id", hotel.getHotel_id())
                    .append("room_number", room.getRoom_number()), roomDocument);

            // Після оновлення кімнати,
            // потрібно оновити дані про бронювання,
            // бо вони застарілі і можуть не відповідати даним кімнати

            FindIterable<Document> bookingsToEdit = bookingCollection.find(new Document("hotel_id", hotel.getHotel_id())
                    .append("room_number", room.getRoom_number()));
            for(Document bookingToEdit : bookingsToEdit){
                Document updatedBooking = new Document("$set", new Document("room_number", room.getRoom_number())
                        .append("room_type", room.getType_name())
                );
                bookingCollection.updateOne(new Document("hotel_id", hotel.getHotel_id())
                        .append("room_number", room.getRoom_number())
                        .append("booking_id", bookingToEdit.getInteger("booking_id")), updatedBooking);
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
        setBookingList();
        updateRoomList();
    }
    public void updateRoomList(){
        hotel = hotelHolder.getUser();
        int id = hotel.getHotel_id();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomsCollection = mongoDatabase.getCollection("rooms");
            FindIterable<Document> roomDocuments = roomsCollection.find(new Document("hotel_id", id));
            MongoCollection<Document> bookingsCollection = mongoDatabase.getCollection("bookings");
            MongoCollection<Document> bookingsDoneCollection = mongoDatabase.getCollection("booking_done");
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
                    if(LocalDate.now().isAfter(checkIN_date) && LocalDate.now().isAfter(checkOUT_date)){
                        bookingsDoneCollection.insertOne(bookingDoc);
                        bookingsCollection.deleteOne(bookingDoc);
                        continue;
                    }
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
                    } else if (LocalDate.now().isEqual(checkIN_date) && LocalDateTime.now().isAfter(LocalDateTime.of(checkIN_date, LocalTime.of(14, 30)))) {
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
                                .append("capacity", roomDoc.getInteger("capacity"))
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
                            .append("price", roomDoc.getDouble("price"))
                            .append("capacity", roomDoc.getInteger("capacity"))
                    );
                    roomsCollection.updateOne(new Document("hotel_id", id)
                            .append("room_number", roomDoc.getInteger("room_number")), updatedRoom);
                }
            }
            ObservableList<Booking> bookingsFromHotel = hotel.getBookingsForList();
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
                        roomDoc.getDouble("price"),
                        roomDoc.getInteger("capacity")
                );
                ObservableList<Booking> bookingsForRoom = FXCollections.observableArrayList();
                for(Booking booking : bookingsFromHotel){
                    if(booking.getRoomNumber() == room.getRoom_number()){
                        bookingsForRoom.add(booking);
                    }
                }
                room.setBookings(bookingsForRoom);
                roomList.add(room);
            }
            hotel.setRooms(roomList);
            hotelHolder.setUser(hotel);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<Room> adminFindAvailableRoomsForBooking(Booking booking){
        hotel = hotelHolder.getUser();
        ObservableList<Room> availableRooms = FXCollections.observableArrayList();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> bookingCollection = mongoDatabase.getCollection("bookings");
            MongoCollection<Document> roomsCollection = mongoDatabase.getCollection("rooms");
            FindIterable<Document> hotelRooms = roomsCollection.find(new Document("hotel_id", hotel.getHotel_id())
                    .append("capacity", new Document("$gte", booking.getPeopleCount())));
            for(Document hotelRoom : hotelRooms){
                boolean isAvailable = true;
                FindIterable<Document> hotelBookings =
                        bookingCollection.find(new Document("hotel_id", hotel.getHotel_id())
                                .append("room_number", hotelRoom.getInteger("room_number")));
                for(Document hotelBooking : hotelBookings) {
                    Date checkIn = hotelBooking.getDate("checkIN_date");
                    Date checkOut = hotelBooking.getDate("checkOUT_date");
                    LocalDate checkInDate = checkIn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate checkOutDate = checkOut.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (checkInDate.isBefore(booking.getCheckOUT_date())
                            && checkOutDate.isAfter(booking.getCheckIN_date())) {
                        isAvailable = false;
                        break;
                    }
                }
                if(isAvailable){
                    Room availableRoom = new Room(
                            hotelRoom.getInteger("room_id"),
                            hotelRoom.getInteger("hotel_id"),
                            hotelRoom.getInteger("type_id"),
                            hotelRoom.getString("type_name"),
                            hotelRoom.getString("room_name"),
                            hotelRoom.getString("description"),
                            hotelRoom.getInteger("room_number"),
                            hotelRoom.getString("status"),
                            hotelRoom.getDate("from"),
                            hotelRoom.getDate("to"),
                            hotelRoom.getDouble("price"),
                            hotelRoom.getInteger("capacity")
                    );
                    availableRooms.add(availableRoom);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return availableRooms;
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
                book.append("room_number", 1);
                book.append("checkIN_date", Date.from(checkIN_date
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()));
                book.append("checkOUT_date", Date.from(checkOUT_date
                        .atStartOfDay(ZoneId.systemDefault())
                        .toInstant()));
                book.append("total_price", Double.valueOf(1200));
                System.out.println("DONE");
                collection.insertOne(book);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public ObservableList<RoomType> getRoomTypes(){
        ObservableList<RoomType> roomTypes = FXCollections.observableArrayList();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomTypesCollection = mongoDatabase.getCollection("room_types");
            FindIterable<Document> roomTypesFound = roomTypesCollection.find();
            for(Document roomTypeDocument : roomTypesFound){
                RoomType roomType = new RoomType(
                        roomTypeDocument.getInteger("type_id"),
                        roomTypeDocument.getString("type_name"),
                        roomTypeDocument.getString("description"),
                        roomTypeDocument.getInteger("capacity")
                );
                roomTypes.add(roomType);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return roomTypes;
    }
    public void deleteRoom() { //TODO Додати видалення бронювань пов'язаних з кімнатою
        hotel = hotelHolder.getUser();
        Document roomDocument;
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> roomsCollection = mongoDatabase.getCollection("rooms");
            roomToDelete = roomHolder.getRoom();
            roomDocument = roomsCollection.find(new Document("hotel_id", roomToDelete.getHotel_id())
                    .append("room_id", roomToDelete.getRoom_id())).first();
            if (roomDocument != null){
                roomsCollection.deleteOne(roomDocument);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        updateRoomList();
    }
    public void deleteBooking(Booking booking) {
        Document bookingDocument;
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> bookingsCollection = mongoDatabase.getCollection("bookings");
            bookingToDelete = bookingHolder.getBooking();
            bookingDocument = bookingsCollection.find(new Document("hotel_id", bookingToDelete.getHotelID())
                    .append("booking_id", bookingToDelete.getBookingID())).first();
            if (bookingDocument != null){
                bookingsCollection.deleteOne(bookingDocument);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setBookingList();
        updateRoomList();
    }

    // ----------------------------------------------------------------------------------------------->

    public void createAdminBooking(Booking booking){
        hotel = hotelHolder.getUser();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> bookingCollection = mongoDatabase.getCollection("bookings");
            MongoCollection<Document> guestCollection = mongoDatabase.getCollection("guests");
            Document sortedByIdGuest = guestCollection.find().sort(Sorts.descending("guest_id")).first();
            Document sortedByIdBooking = bookingCollection.find().sort(Sorts.descending("booking_id")).first();
            int bookingID = 0;
            if (sortedByIdBooking != null){
                bookingID = sortedByIdBooking.getInteger("booking_id");
            }
            int guestID = 0;
            if (sortedByIdGuest != null){
                guestID = sortedByIdBooking.getInteger("guest_id");
            }
            Document adminBooking = new Document("hotel_id", hotel.getHotel_id())
                    .append("booking_id", bookingID + 1)
                    .append("guest_id", guestID + 1)
                    .append("guest_first_name", booking.getGuestFirstName())
                    .append("guest_second_name", booking.getGuestSecondName())
                    .append("guest_phone_number", booking.getGuestPhoneNumber())
                    .append("guest_email", booking.getGuestEmail())
                    .append("room_number", booking.getRoomNumber())
                    .append("room_type", booking.getRoomType())
                    .append("checkIN_date", Date.from(booking.getCheckIN_date()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()))
                    .append("checkOUT_date", Date.from(booking.getCheckOUT_date()
                            .atStartOfDay(ZoneId.systemDefault())
                            .toInstant()))
                    .append("people_count", booking.getPeopleCount())
                    .append("total_price", booking.getTotalPrice())
                    .append("add_info", booking.getAdditionalInfo());
            bookingCollection.insertOne(adminBooking);
        }catch (Exception e){
            e.printStackTrace();
        }
        setBookingList();
        updateRoomList();
    }

    public void editAdminBooking(Booking booking, boolean findNewAvailable){
        hotel = hotelHolder.getUser();
        try(MongoClient mongoClient = MongoClients.create(dataCredentials.getUrl())){
            MongoDatabase mongoDatabase = mongoClient.getDatabase("HotelDataBase");
            MongoCollection<Document> bookingCollection = mongoDatabase.getCollection("bookings");
            Document sortedByIdBooking = bookingCollection.find().sort(Sorts.descending("booking_id")).first();
            int bookingID = 4;
            if (sortedByIdBooking != null){
                bookingID = sortedByIdBooking.getInteger("booking_id");
            }
            if(findNewAvailable){
                deleteBooking(booking);
                ObservableList<Room> availableRoomsForBooking = adminFindAvailableRoomsForBooking(booking);
                roomHolder.setRoomList(availableRoomsForBooking);
            }else{
                Document bookingToEdit = new Document("$set", new Document("booking_id", bookingID)
                        .append("guest_first_name", booking.getGuestFirstName())
                        .append("guest_second_name", booking.getGuestSecondName())
                        .append("guest_phone_number", booking.getGuestPhoneNumber())
                        .append("guest_email", booking.getGuestEmail())
                        .append("add_info", booking.getAdditionalInfo())
                );
                bookingCollection.updateOne(new Document("booking_id", bookingID), bookingToEdit);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setBookingList();
        updateRoomList();
    }
}