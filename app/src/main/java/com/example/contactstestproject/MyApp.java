package com.example.contactstestproject;

import android.app.Application;
import android.content.Context;
import android.database.ContentObserver;
import android.provider.ContactsContract;

import com.example.contactstestproject.data.repository.ContactsRepository;

import ir.tapsell.sdk.Tapsell;

public class MyApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        Tapsell.initialize(this, "gdofhmonadppeefnfqlgtdolhbrnnefmafcptggsjtnfkceekognidjatqsoqraitjeajq");
        context = getApplicationContext();
        MyApp.getContext().getContentResolver().registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI,
                true,
                new ContentObserver(null) {
                    @Override
                    public void onChange(boolean selfChange) {
                        super.onChange(selfChange);
                        ContactsRepository.getInstance().insertContacts();
                    }
                });
    }

    public static Context getContext() {
        return context;
    }
}
