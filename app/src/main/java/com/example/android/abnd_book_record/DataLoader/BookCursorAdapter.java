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

import com.example.android.abnd_book_record.R;
import com.example.android.abnd_book_record.data.BookContract;
import com.example.android.abnd_book_record.data.BookProvider;

import org.w3c.dom.Text;


import static java.security.AccessController.getContext;

/**
 * Created by Akanksha_Rajwar on 23-11-2018.
 */

public class BookCursorAdapter extends CursorAdapter {
    int pos=-1;

    TextView tvQuantity;
    BookProvider bookProvider;
    ContentValues contentValues;
    Uri currentUri;

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
    public void bindView(View view, Context context, final Cursor cursor) {

        TextView tvName=view.findViewById(R.id.name);
        tvQuantity=view.findViewById(R.id.summary);
        TextView tvPrice=view.findViewById(R.id.price);
        Button buyButton=view.findViewById(R.id.buyButton);

        Integer nameIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
        final Integer quantityIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        Integer priceIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);

        tvName.setText(cursor.getString(nameIndex));
        tvQuantity.setText(cursor.getString(quantityIndex));
        tvPrice.setText("Rs. "+cursor.getString(priceIndex));

        View.OnClickListener myButtonClickListener = new View.OnClickListener() {
          @Override
            public void onClick(View v) {

               int quantity=Integer.parseInt(tvQuantity.getText().toString());

              tvQuantity.setText(quantity-1);

            }
        };
        buyButton.setOnClickListener(myButtonClickListener);






    }
}
