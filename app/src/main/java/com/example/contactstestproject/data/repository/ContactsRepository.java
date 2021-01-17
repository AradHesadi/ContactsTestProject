package com.example.contactstestproject.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.example.contactstestproject.data.ContactsDAO;
import com.example.contactstestproject.data.room.ContactsRoomDatabase;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.utils.ContactSyncUtils;

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
    public LiveData<Contact> getContactLiveData(final String id) {
        final MutableLiveData<Contact> liveData = new MutableLiveData<>();
        ContactsRoomDatabase.dataBaseWriteExecutor.execute(
                new Runnable() {
                    @Override
                    public void run() {
                        Contact contact = mContactsDAO.getContact(id);
                        liveData.postValue(contact);
                    }
                }
        );
        return liveData;
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