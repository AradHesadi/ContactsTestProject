package com.example.contactstestproject.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactstestproject.MyApp;
import com.example.contactstestproject.data.database.cursorwrapper.ContactsCursorWrapper;
import com.example.contactstestproject.data.database.ContactsDbHelper;
import com.example.contactstestproject.data.database.ContactsDbSchema;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.utils.ThreadUtils;
import com.example.contactstestproject.utils.WriteContactsUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactsRepository{

    private static ContactsRepository repository;
    private final SQLiteDatabase database;
    MutableLiveData<List<Contact>> listMutableLiveData = new MutableLiveData<>();

    public static ContactsRepository getInstance() {
        if (repository == null)
            repository = new ContactsRepository();
        return repository;
    }

    private ContactsRepository() {
        ContactsDbHelper contactsDbHelper = new ContactsDbHelper(MyApp.getContext());
        database = contactsDbHelper.getWritableDatabase();
    }

    public LiveData<List<Contact>> getContactsLiveData() {
        Log.d("testt", "getContactsLiveData: ");
        return listMutableLiveData;
    }

    public void fetchContactsLiveData() {
        List<Contact> contacts = new ArrayList<>();
        try (ContactsCursorWrapper cursor = queryContacts()) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        }
        Log.d("testt", "repository liveData : "+contacts.size());
        listMutableLiveData.postValue(contacts);
    }

    public void clear() {
        database.delete(ContactsDbSchema.ContactsTable.NAME, null, null);
    }

    public void insertContacts() {
        ThreadUtils.dataBaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                clear();
                int size = WriteContactsUtils.writeToDatabase().size();
                for (int i = 0; i < size; i++)
                    database.insert(ContactsDbSchema.ContactsTable.NAME,
                            null,
                            getContactsContentValue(WriteContactsUtils.writeToDatabase().get(i)));
                Log.d("testt", "repository : "+ size);
                fetchContactsLiveData();
            }
        });
    }

    private ContactsCursorWrapper queryContacts() {
        Cursor cursor = database.query(ContactsDbSchema.ContactsTable.NAME,
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
