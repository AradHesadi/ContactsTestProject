package com.example.contactstestproject.utils.contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.example.contactstestproject.model.Contact;

import java.util.ArrayList;
import java.util.List;

/*
this class contains logic for getting contacts from content resolver
and creates an array list to insert in database
 */

public class ContactSyncUtils {

    private final Context context;
    private final List<Contact> contacts;

    public ContactSyncUtils(Context context) {
        this.context = context;
        contacts = new ArrayList<>();
    }

    public void sync() {
        contacts.clear();
        if (ContactPermissionUtils.isGranted()) {
            ContentResolver contentResolver = context.getContentResolver();
            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, ContactsContract.Contacts.DISPLAY_NAME);

            if ((cursor != null ? cursor.getCount() : 0) > 0) {
                while (cursor != null && cursor.moveToNext()) {
                    int id = cursor.getInt(
                            cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cursor.getString(cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME));
                    String phoneNo = "no number";
                    if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                        Cursor pCur = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{String.valueOf(id)}, null);
                        while (pCur.moveToNext()) {
                            phoneNo = pCur.getString(pCur.getColumnIndex(
                                    ContactsContract.CommonDataKinds.Phone.NUMBER));

                        }
                        pCur.close();
                    }
                    contacts.add(new Contact(id, name, phoneNo));
                }
            }
            if (cursor != null) {
                cursor.close();
            }
        } else {
            System.exit(0);
        }
    }

    public List<Contact> getContacts() {
        return contacts;
    }
}
