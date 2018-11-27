package com.example.android.abnd_book_record.DataLoader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.abnd_book_record.CatalogActivity;
import com.example.android.abnd_book_record.R;
import com.example.android.abnd_book_record.data.BookContract;


/**
 * Created by Akanksha_Rajwar on 23-11-2018.
 */

public class BookCursorAdapter extends CursorAdapter {
    int pos=-1;
    Integer quantityIndex;





    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    public int getBookPosition()
    {
        return pos;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

       final Cursor mcursor=cursor;
        TextView tvName=view.findViewById(R.id.name);
        final TextView tvQuantity=view.findViewById(R.id.summary);
        TextView tvPrice=view.findViewById(R.id.price);
        Button buyButton=view.findViewById(R.id.buyButton);

        final Integer nameIndex=mcursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
         quantityIndex=mcursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        Integer priceIndex=mcursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);

        tvName.setText(mcursor.getString(nameIndex));
        tvQuantity.setText(mcursor.getString(quantityIndex));
        tvPrice.setText("Rs. "+mcursor.getString(priceIndex));



        /*View.OnClickListener myButtonClickListener = new View.OnClickListener() {
          @Override
            public void onClick(View v) {

               int quantity=Integer.parseInt(tvQuantity.getText().toString());

              tvQuantity.setText(quantity-1);

            }
        };*/
        buyButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final int quantity =Integer.parseInt(tvQuantity.getText().toString());

                CatalogActivity catalogActivity = (CatalogActivity) context;
                ContentValues values = new ContentValues();
                values.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity-1);

                Uri updateUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, quantityIndex);

                int rowsAffected = catalogActivity.getContentResolver().update(updateUri, values,null, null);


                tvQuantity.setText(quantity-1+"");

            }
        });

    }
}
