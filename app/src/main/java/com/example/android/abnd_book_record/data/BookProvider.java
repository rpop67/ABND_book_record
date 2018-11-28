package com.example.android.abnd_book_record.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Akanksha_Rajwar on 11-11-2018.
 */

public class BookProvider extends ContentProvider {

    public static BookDbHelper bookDbHelper;

    Cursor cursor;


    private SQLiteDatabase db;
    private static final int BOOKS=100;
    private static final int BOOK_ID=101;
    private static final UriMatcher uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);


    /** Tag for the log messages */
    public static final String LOG_TAG = BookProvider.class.getSimpleName();
    static{

        uriMatcher.addURI(BookContract.BookEntry.CONTENT_AUTHORITY, BookContract.BookEntry.PATH,BOOKS);
        uriMatcher.addURI(BookContract.BookEntry.CONTENT_AUTHORITY, BookContract.BookEntry.PATH+"/#",BOOK_ID);
    }


    @Override
    public boolean onCreate() {
         bookDbHelper=new BookDbHelper(getContext());

        db=bookDbHelper.getReadableDatabase();

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //done: input contentURI and then match URI whether type is a table(i.e. PETS) or a single row(ie. PET_ID)
        //and then proceed accordingly

        SQLiteDatabase database= bookDbHelper.getReadableDatabase();
        //MatchUri method returns int
        int match=uriMatcher.match(uri);
        switch(match){
            case BOOKS:
                cursor=database.query(BookContract.BookEntry.TABLE_NAME,projection,null,null,null,null,sortOrder);
                break;
            case BOOK_ID:
                //id needs to be extracted from uri
                selection= BookContract.BookEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=database.query(BookContract.BookEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {

        final int match=uriMatcher.match(uri);
        switch(match){
            case BOOKS:
                return insertBook(uri,contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    public Uri insertBook(Uri uri, ContentValues contentValues)
    {
        String name=contentValues.getAsString(BookContract.BookEntry.COLUMN_PROD_NAME);
        Integer quantity=contentValues.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
        Integer price=contentValues.getAsInteger(BookContract.BookEntry.COLUMN_PRICE);
        String seller=contentValues.getAsString(BookContract.BookEntry.COLUMN_SELLER_NAME);
        String sellerContact=contentValues.getAsString(BookContract.BookEntry.COLUMN_SELLER_CONTACT);
        if(name==null||name.isEmpty())
        {
            Toast.makeText(getContext(),"Noame field empty",Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Book requires a name");
        }

        if (seller.isEmpty())
        {
            Toast.makeText(getContext(),"Seller name should be valid",Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Book requires a seller");
        }
        if(sellerContact.toString().equals(""))
        {
            Toast.makeText(getContext(),"Seller contact should be valid",Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Valid Seller Contact is required");
        }
        if((quantity != null && quantity < 0)||quantity.toString().isEmpty())
        {
            Toast.makeText(getContext(),"Valid weight required",Toast.LENGTH_SHORT).show();
            throw new IllegalArgumentException("Book requires a quantity");
        }


        long id=db.insert(BookContract.BookEntry.TABLE_NAME,null,contentValues);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);


        // DONE: Insert a new pet into the pets database table with the given ContentValues



        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it

    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match =uriMatcher.match(uri);
        int rownum;
        switch(match){
            case BOOKS:
                //sanitycheck
                return updateBook(uri, contentValues, selection, selectionArgs);
            case BOOK_ID:
                selection= BookContract.BookEntry._ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updateBook(uri, contentValues, selection, selectionArgs);
            default:throw new IllegalArgumentException("Updation is not supported for " + uri);
        }
    }

    private int updateBook(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(BookContract.BookEntry.COLUMN_PROD_NAME)) {
            String name = values.getAsString(BookContract.BookEntry.COLUMN_PROD_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Book requires a name");
            }
        }

        // If the {@link BookEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(BookContract.BookEntry.COLUMN_SELLER_NAME)) {
            String seller = values.getAsString(BookContract.BookEntry.COLUMN_SELLER_NAME);
            if (seller == null ) {
                throw new IllegalArgumentException("Book requires valid gender");
            }
        }

        // If the {@link BookEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(BookContract.BookEntry.COLUMN_QUANTITY)) {
            // Check that the quantity is greater than or equal to 0 kg
            Integer quantity = values.getAsInteger(BookContract.BookEntry.COLUMN_QUANTITY);
            if (quantity != null && quantity < 0) {
                throw new IllegalArgumentException("Book requires valid weight");
            }
        }

        // No need to check the breed, any value is valid (including null).

        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = bookDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        int rowsUpdated = database.update(BookContract.BookEntry.TABLE_NAME, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final int match =uriMatcher.match(uri);
        SQLiteDatabase db=bookDbHelper.getWritableDatabase();
        int rowsDeleted;
        switch(match){
            case BOOKS:
                rowsDeleted = db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case BOOK_ID:
                selection = BookContract.BookEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = db.delete(BookContract.BookEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case BOOKS:
                return BookContract.BookEntry.CONTENT_LIST_TYPE;
            case BOOK_ID:
                return BookContract.BookEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}
