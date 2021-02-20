package com.example.contactstestproject.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactsDbHelper extends SQLiteOpenHelper {

    public ContactsDbHelper(@Nullable Context context) {
        super(context, ContactsDbSchema.NAME, null, ContactsDbSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ContactsDbSchema.ContactsTable.NAME + "("
                + ContactsDbSchema.ContactsTable.COLS.ID + " int primary key,"
                + ContactsDbSchema.ContactsTable.COLS.NAME + " text,"
                + ContactsDbSchema.ContactsTable.COLS.PHONE_NUMBER + " text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
