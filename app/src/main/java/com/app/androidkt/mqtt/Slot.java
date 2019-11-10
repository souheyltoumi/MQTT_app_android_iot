package com.app.androidkt.mqtt;

public class Slot {
    String id ,status,parking,user;

    public Slot(String id, String status, String parking, String user) {
        this.id = id;
        this.status = status;
        this.parking = parking;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParking() {
        return parking;
    }

    public void setParking(String parking) {
        this.parking = parking;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
