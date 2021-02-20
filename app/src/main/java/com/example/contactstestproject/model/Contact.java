package com.example.contactstestproject.model;

import java.io.Serializable;


public class Contact implements Serializable {

    private final int id;

    private String name;

    private String phoneNumber;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String mName) {
        this.name = mName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.phoneNumber = mPhoneNumber;
    }

    public Contact(int id, String name, String mPhoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = mPhoneNumber;
    }
}
