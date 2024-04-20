package org.example.hotelmanager.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SuperAdmin {
    private ObservableList<Hotel> hotels = FXCollections.observableArrayList();
    private ObservableList<Client> clients = FXCollections.observableArrayList();

    public SuperAdmin(ObservableList<Hotel> hotels, ObservableList<Client> clients) {
        this.hotels = hotels;
        this.clients = clients;
    }

    public SuperAdmin() {

    }

    public ObservableList<Hotel> getHotels() {
        return hotels;
    }

    public void setHotels(ObservableList<Hotel> hotels) {
        this.hotels = hotels;
    }

    public ObservableList<Client> getClients() {
        return clients;
    }

    public void setClients(ObservableList<Client> clients) {
        this.clients = clients;
    }
}
