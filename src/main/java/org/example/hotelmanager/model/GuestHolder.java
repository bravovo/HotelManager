package org.example.hotelmanager.model;

public class GuestHolder {
    private Guest guest;
    private boolean done = false;
    private final static GuestHolder INSTANCE = new GuestHolder();
    private GuestHolder(){}
    public static GuestHolder getInstance() {
        return INSTANCE;
    }
    public void setUser(Guest guest) {
        this.guest = guest;
    }
    public Guest getUser() {
        return this.guest;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
