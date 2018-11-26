package com.example.android.abnd_book_record.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Akanksha_Rajwar on 10-11-2018.
 */

public class BookDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "shelter.db";
    public static final Integer DATABASE_VERSION = 1;

    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE_BOOKS = "CREATE TABLE " + BookContract.BookEntry.TABLE_NAME + " ("
                + BookContract.BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookContract.BookEntry.COLUMN_PROD_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_PRICE + " INTEGER DEFAULT 40, "
                + BookContract.BookEntry.COLUMN_QUANTITY + " INTEGER DEFAULT NOT NULL, "
                + BookContract.BookEntry.COLUMN_SELLER_NAME + " TEXT NOT NULL, "
                + BookContract.BookEntry.COLUMN_SELLER_CONTACT + " TEXT NOT NULL);";

        db.execSQL(SQL_CREATE_TABLE_BOOKS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
