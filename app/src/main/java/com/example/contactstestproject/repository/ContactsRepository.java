package com.example.contactstestproject.repository;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import androidx.lifecycle.LiveData;


import com.example.contactstestproject.database.ContactsDAO;
import com.example.contactstestproject.database.ContactsRoomDatabase;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.utils.ContactSyncUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactsRepository implements IContactsRepository {

    private static ContactsRepository sRepository;
    private final ContactsDAO mContactsDAO;
    private ContactSyncUtils mContactSyncUtils;

    public static ContactsRepository getInstance(Context context) {
        if (sRepository == null)
            sRepository = new ContactsRepository(context);

        return sRepository;
    }

    private ContactsRepository(Context context) {
        ContactsRoomDatabase contactsRoomDatabase = ContactsRoomDatabase.getDatabase(context);
        mContactsDAO = contactsRoomDatabase.getContactsDAO();
    }

    @Override
    public LiveData<List<Contact>> getContactsLiveData() {
        return mContactsDAO.getList();
    }

    @Override
    public LiveData<Contact> getContactLiveData(String id) {
        return mContactsDAO.getContactLiveData(id);
    }

    @Override
    public Contact getContact(String id) {
        return mContactsDAO.getContact(id);
    }

    @Override
    public void clear() {
        mContactsDAO.clear();
    }

    public void insertContacts(Context context) {
        mContactSyncUtils = new ContactSyncUtils(context);
        ContactsRoomDatabase.dataBaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                clear();
                mContactsDAO.insertContacts(mContactSyncUtils.getContacts().toArray(new Contact[]{}));
            }
        });
    }
}