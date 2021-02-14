package com.example.contactstestproject.data.repository;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

public class ContactsRepository implements IContactsRepository {

    private static ContactsRepository sRepository;
    private final SQLiteDatabase mDatabase;
    private ContactSyncUtils mContactSyncUtils;

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

    @Override
    public LiveData<List<Contact>> getContactsLiveData() {
        MutableLiveData<List<Contact>> contactsLiveData = new MutableLiveData<>();
        List<Contact> contacts = new ArrayList<>();
        ContactsCursorWrapper cursor = queryContacts(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.add(cursor.getContact());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        contactsLiveData.postValue(contacts);
        return contactsLiveData;
    }

    @Override
    public void clear() {
        mDatabase.delete(ContactsDbSchema.ContactsTable.NAME, null, null);
    }

    @Override
    public void insertContacts() {
        ThreadUtils.dataBaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                clear();
                mContactSyncUtils.sync();
                for (int i = 0; i < mContactSyncUtils.getContacts().size(); i++)
                    mDatabase.insert(ContactsDbSchema.ContactsTable.NAME,
                            null,
                            getContactsContentValue(mContactSyncUtils.getContacts().get(i)));
            }
        });
    }

    private ContactsCursorWrapper queryContacts(String selection, String[] selectionArgs) {
        Cursor cursor = mDatabase.query(ContactsDbSchema.ContactsTable.NAME,
                null,
                selection,
                selectionArgs,
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
