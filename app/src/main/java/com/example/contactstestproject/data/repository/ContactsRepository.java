package com.example.contactstestproject.data.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.contactstestproject.MyApp;
import com.example.contactstestproject.data.database.ContactsDbHelper;
import com.example.contactstestproject.data.database.ContactsDbSchema;
import com.example.contactstestproject.data.database.cursorwrapper.ContactsCursorWrapper;
import com.example.contactstestproject.model.Contact;
import com.example.contactstestproject.utils.ThreadUtils;
import com.example.contactstestproject.utils.WriteContactsUtils;

import java.util.ArrayList;
import java.util.List;

public class ContactsRepository {

    private static volatile ContactsRepository repository;
    private final SQLiteDatabase database;
    MutableLiveData<List<Contact>> listMutableLiveData = new MutableLiveData<>();

    public static ContactsRepository getInstance() {
        if (repository == null) {
            synchronized (ContactsRepository.class) {
                if (repository == null) {
                    repository = new ContactsRepository();
                }
            }
        }
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
        Log.d("testt", "repository liveData : " + contacts.size());
        listMutableLiveData.postValue(contacts);
    }

    public void clear() {
        database.execSQL("delete from " + ContactsDbSchema.ContactsTable.NAME);
    }

    public void insertContacts() {
        ThreadUtils.dataBaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                clear();
                List<Contact> contacts = WriteContactsUtils.writeToDatabase();
                int size = contacts.size();
                for (int i = 0; i < size; i++)
                    database.execSQL("insert into "
                            + ContactsDbSchema.ContactsTable.NAME
                            + " values (" + contacts.get(i).getId() + ",'"
                            + contacts.get(i).getName() + "','"
                            + contacts.get(i).getPhoneNumber() + "')");
                Log.d("testt", "repository : " + size);
                fetchContactsLiveData();
            }
        });
    }

    private ContactsCursorWrapper queryContacts() {
        Cursor cursor = database.rawQuery("select * from "
                        + ContactsDbSchema.ContactsTable.NAME
                , null);
        return new ContactsCursorWrapper(cursor);
    }
}
