package com.example.android.abnd_book_record.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Akanksha_Rajwar on 10-11-2018.
 */

public class BookContract {
    private BookContract() {
    };

    public static final class BookEntry implements BaseColumns {

        public final static String TABLE_NAME = "books";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_PROD_NAME = "BOOK_TITLE";
        public static final String COLUMN_PRICE = "PRICE";
        public static final String COLUMN_QUANTITY = "QUANTITY";
        public static final String COLUMN_SELLER_NAME = "SELLER_NAME";
        public static final String COLUMN_SELLER_CONTACT = "SELLER_CONTACT";

        public static final String CONTENT_AUTHORITY="com.example.android.abnd_book_record";
        public static final Uri BASE_CONTENT_URI=Uri.parse("content://"+CONTENT_AUTHORITY);
        public static final String PATH = "books";
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH);

    }
}
