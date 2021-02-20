package com.example.contactstestproject.model;

import androidx.annotation.NonNull;

import java.io.Serializable;


public class Contact implements Serializable {

    private final String mId;

    private String mName;

    private String mPhoneNumber;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public Contact(@NonNull String id, String mName, String mPhoneNumber) {
        this.mId = id;
        this.mName = mName;
        this.mPhoneNumber = mPhoneNumber;
    }
}
