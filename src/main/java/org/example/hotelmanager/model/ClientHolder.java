package org.example.hotelmanager.model;

import javafx.collections.ObservableList;

public final class ClientHolder {
    private Client client;
    private boolean done = false;
    private final static ClientHolder INSTANCE = new ClientHolder();
    private ClientHolder(){}
    public static ClientHolder getInstance() {
        return INSTANCE;
    }
    public void setUser(Client client) {
        this.client = client;
    }
    public Client getUser() {
        return this.client;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}