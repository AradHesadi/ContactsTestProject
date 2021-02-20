package com.example.contactstestproject.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactstestproject.data.database.cursorwrapper.ContactsCursorWrapper;
import com.example.contactstestproject.data.database.ContactsDbHelper;
import com.example.contactstestproject.data.database.ContactsDbSchema;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.utils.ApplicationUtils;
import com.example.contactstestproject.utils.ThreadUtils;
import com.example.contactstestproject.utils.contacts.ContactSyncUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactsRepository{

    private static ContactsRepository sRepository;
    private final SQLiteDatabase mDatabase;
    private final ContactSyncUtils mContactSyncUtils;
    MutableLiveData<List<Contact>> mContactsLiveData = new MutableLiveData<>();

    public static ContactsRepository getInstance() {
        if (sRepository == null)
            sRepository = new ContactsRepository();
        return sRepository;
    }

    private ContactsRepository() {
        ContactsDbHelper contactsDbHelper = new ContactsDbHelper(ApplicationUtils.getContext());
        mDatabase = contactsDbHelper.getWritableDatabase();
        mContactSyncUtils = new ContactSyncUtils(ApplicationUtils.getContext());
    }

    public LiveData<List<Contact>> getContactsLiveData() {
        Log.d("testt", "getContactsLiveData: ");
        return mContactsLiveData;
    }

    public void fetchContactsLiveData() {
        List<Contact> contacts = new ArrayList<>();
        ContactsCursorWrapper cursor = queryContacts();
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        Log.d("testt", "repository liveData : "+contacts.size());
        mContactsLiveData.postValue(contacts);
    }

    public void clear() {
        mDatabase.delete(ContactsDbSchema.ContactsTable.NAME, null, null);
    }

    public void insertContacts() {
        ThreadUtils.dataBaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                clear();
                mContactSyncUtils.sync();
                int size = mContactSyncUtils.getContacts().size();
                for (int i = 0; i < size; i++)
                    mDatabase.insert(ContactsDbSchema.ContactsTable.NAME,
                            null,
                            getContactsContentValue(mContactSyncUtils.getContacts().get(i)));
                Log.d("testt", "repository : "+ size);
                fetchContactsLiveData();
            }
        });
    }

    private ContactsCursorWrapper queryContacts() {
        Cursor cursor = mDatabase.query(ContactsDbSchema.ContactsTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        return new ContactsCursorWrapper(cursor);
    }

    private ContentValues getContactsContentValue(Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactsDbSchema.ContactsTable.COLS.ID, contact.getId());
        values.put(ContactsDbSchema.ContactsTable.COLS.NAME, contact.getName());
        values.put(ContactsDbSchema.ContactsTable.COLS.PHONE_NUMBER, contact.getPhoneNumber());
        return values;
    }
}
