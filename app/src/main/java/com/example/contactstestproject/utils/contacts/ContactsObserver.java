package com.example.contactstestproject.utils.contacts;

import android.database.ContentObserver;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.contactstestproject.data.repository.ContactsRepository;
import com.example.contactstestproject.utils.ApplicationUtils;

public class ContactsObserver {

   ContactsRepository mRepository = ContactsRepository.getInstance();


    public ContentObserver mContentObserver = new ContentObserver(null) {
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            mRepository.insertContacts();
            Log.d("testt", "contentObserver");
        }
    };

    public void setData() {
        ApplicationUtils.getContext().getContentResolver().registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI,
                true,
                mContentObserver);
    }
}
