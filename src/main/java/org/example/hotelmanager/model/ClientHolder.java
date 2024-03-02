package org.example.hotelmanager.model;
public final class ClientHolder {
    private Client client;
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
}