package org.example.hotelmanager.model;

public class SuperAdminHolder {
    private SuperAdmin superAdmin;
    private final static SuperAdminHolder INSTANCE = new SuperAdminHolder();

    private SuperAdminHolder(){}
    public static SuperAdminHolder getInstance() {
        return INSTANCE;
    }
    public void setSuper(SuperAdmin superAdmin) {
        this.superAdmin = superAdmin;
    }
    public SuperAdmin getSuper() {
        return this.superAdmin;
    }
}
