package com.example.android.abnd_book_record;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.abnd_book_record.DataLoader.BookCursorAdapter;
import com.example.android.abnd_book_record.data.BookContract;
import com.example.android.abnd_book_record.data.BookDbHelper;

/**
 * Created by Akanksha_Rajwar on 23-11-2018.
 */

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> /*,View.OnClickListener */{


    private static final int BOOK_LOADER=0;
    private ContentResolver mContentResolver;

    BookCursorAdapter mCursorAdapter;
    ContentValues contentValues;
    Button buyButton;
    Button editButton;
    Uri currentUri;
    int stock;

    private BookDbHelper mDbHelper;
    private ListView bookListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        editButton = findViewById(R.id.editButton);
        buyButton = findViewById(R.id.buyButton);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mContentResolver = this.getContentResolver();
        contentValues = new ContentValues();


        //intent to editor activity
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                intent.putExtra("title", "Add a Book");
                startActivity(intent);
            }
        });

        bookListView = (ListView) findViewById(R.id.list);
        //Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        bookListView.setEmptyView(emptyView);

        buyButton = bookListView.findViewById(R.id.buyButton);
        editButton = bookListView.findViewById(R.id.editButton);


        mDbHelper = new BookDbHelper(this);
        mCursorAdapter = new BookCursorAdapter(this, null);
        bookListView.setAdapter(mCursorAdapter);
        //buyButton.setOnClickListener(this);
        //editButton.setOnClickListener(this);




        getLoaderManager().initLoader(BOOK_LOADER, null, this);

/*
        bookListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {


                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                        intent.putExtra("title", "Edit a book");
                        currentUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, id);
                        intent.setData(currentUri);
                        startActivity(intent);
                    }
                });


            }
        });*/
    } //onCreate





    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] Projection={
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_PROD_NAME,
                BookContract.BookEntry.COLUMN_PRICE,
                BookContract.BookEntry.COLUMN_QUANTITY,
                BookContract.BookEntry.COLUMN_SELLER_NAME,
                BookContract.BookEntry.COLUMN_SELLER_CONTACT


        };
        return new CursorLoader(this, BookContract.BookEntry.CONTENT_URI,Projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

/*
Toast.makeText(this,"Buy Button Pressed",Toast.LENGTH_LONG).show();
 */



  /*  @Override
    public void onClick(View v) {
        if(v.equals(editButton))
        {
            Toast.makeText(this,"*********EDIT Button Pressed*************",Toast.LENGTH_LONG).show();

        }
        else if(v.equals(buyButton)) {
            Toast.makeText(this,"########Buy Button Pressed",Toast.LENGTH_LONG).show();
            TextView priceTV= findViewById(R.id.summary);
            stock=Integer.parseInt(priceTV.getText().toString());
            if (stock > 0) {
                contentValues.put(BookContract.BookEntry.COLUMN_QUANTITY, --stock);
                mContentResolver.update(
                        currentUri,
                        contentValues,
                        null,
                        null);
            }
            priceTV.setText(stock);
        }
        }

        }*/
    }

