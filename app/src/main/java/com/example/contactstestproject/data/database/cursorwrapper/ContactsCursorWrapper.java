package com.example.contactstestproject.data.database.cursorwrapper;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.contactstestproject.data.database.ContactsDbSchema;
import com.example.contactstestproject.model.Contact;

public class ContactsCursorWrapper extends CursorWrapper {

    public ContactsCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Contact getContact() {
        int id = getInt(getColumnIndex(ContactsDbSchema.ContactsTable.COLS.ID));
        String name = getString(getColumnIndex(ContactsDbSchema.ContactsTable.COLS.NAME));
        String phoneNumber = getString(getColumnIndex(ContactsDbSchema.ContactsTable.COLS.PHONE_NUMBER));
        return new Contact(id, name, phoneNumber);
    }
}

