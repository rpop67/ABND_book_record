package com.example.android.abnd_book_record;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.abnd_book_record.data.BookContract;
import com.example.android.abnd_book_record.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {

    TextView TextRecord;
    EditText EditProduct;
    EditText EditQuantity;
    EditText EditPrice;
    EditText EditSeller;
    EditText EditContact;
    BookDbHelper dbHelper;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditProduct = findViewById(R.id.etPname);
        EditQuantity = findViewById(R.id.etQuantity);
        EditPrice = findViewById(R.id.etPrice);
        EditSeller = findViewById(R.id.etSellerName);
        EditContact = findViewById(R.id.etContact);
        TextRecord=findViewById(R.id.tvRecord);
        dbHelper = new BookDbHelper(this);
        db = dbHelper.getReadableDatabase();


    }

    public void insertData() {
        Toast.makeText(this,"Taking values",Toast.LENGTH_LONG);
        String product = EditProduct.getText().toString().trim();
        int quantity = Integer.parseInt(EditQuantity.getText().toString().trim());
        int price = Integer.parseInt(EditPrice.getText().toString().trim());
        String seller = EditSeller.getText().toString().trim();
        String contact = EditContact.getText().toString().trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookContract.BookEntry.COLUMN_PROD_NAME, product);
        contentValues.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(BookContract.BookEntry.COLUMN_PRICE, price);
        contentValues.put(BookContract.BookEntry.COLUMN_SELLER_NAME, seller);
        contentValues.put(BookContract.BookEntry.COLUMN_SELLER_CONTACT, contact);



        long id = db.insert(BookContract.BookEntry.TABLE_NAME, null, contentValues);
        TextRecord.setText("Current records count : "+id);

    }

    public void readData() {
        //need to create projection[], selection and selectioargs[] for corresponding sql query
        //projection contains columns that needs to be displayed
        String[] projection = {
                BookContract.BookEntry.COLUMN_PROD_NAME,
                BookContract.BookEntry.COLUMN_QUANTITY,
                BookContract.BookEntry.COLUMN_PRICE,
                BookContract.BookEntry.COLUMN_SELLER_NAME,
                BookContract.BookEntry.COLUMN_SELLER_CONTACT

        };

        Cursor cursor = db.query(BookContract.BookEntry.TABLE_NAME, projection, null, null, null, null, null);

        try {

            TextView display = findViewById(R.id.tvDisplay);
            display.setText("\n" + BookContract.BookEntry.COLUMN_PROD_NAME + "   "
                    + BookContract.BookEntry.COLUMN_QUANTITY + "   "
                    + BookContract.BookEntry.COLUMN_PRICE + "   "
                    + BookContract.BookEntry.COLUMN_SELLER_NAME + "   "
                    + BookContract.BookEntry.COLUMN_SELLER_CONTACT + "\n\n");
            int indexProd = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
            int indexQuantity = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
            int indexPrice = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
            int indexSeller = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_NAME);
            int indexContact = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_CONTACT);



            while (cursor.moveToNext()) {
                String getProd = cursor.getString(indexProd);
                int getQuantity = cursor.getInt(indexQuantity);
                int getPrice = cursor.getInt(indexPrice);
                String getSeller = cursor.getString(indexSeller);
                String getContact = cursor.getString(indexContact);
                display.append("\n" + getProd + " - " + getQuantity + " - " + getPrice + " - " + getSeller + " - " + getContact);


            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    public void reset()
    {
         EditProduct.setText("");
         EditQuantity.setText("");
         EditPrice.setText("");
         EditSeller.setText("");
         EditContact.setText("");
    }

    public void uploadFunc(View view) {
        if(EditProduct.getText()!=null ||EditQuantity.getText()!=null || EditContact.getText()!=null ||EditSeller.getText()!=null
                ||EditPrice.getText()!=null)
        {
            insertData();
            readData();
            reset();
        }
        else
        {
            Toast.makeText(this, "Fill full details", Toast.LENGTH_SHORT).show();
        }

    }
}
