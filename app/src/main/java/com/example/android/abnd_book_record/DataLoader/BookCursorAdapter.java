package com.example.android.abnd_book_record.DataLoader;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.abnd_book_record.R;
import com.example.android.abnd_book_record.data.BookContract;

import org.w3c.dom.Text;

/**
 * Created by Akanksha_Rajwar on 23-11-2018.
 */

public class BookCursorAdapter extends CursorAdapter {
    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView tvName=view.findViewById(R.id.name);
        TextView tvQuantity=view.findViewById(R.id.summary);
        TextView tvPrice=view.findViewById(R.id.price);

        Integer nameIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
        Integer quantityIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        Integer priceIndex=cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);

        tvName.setText(cursor.getString(nameIndex));
        tvQuantity.setText("Stock : "+cursor.getString(quantityIndex));
        tvPrice.setText("Rs. "+cursor.getString(priceIndex));

    }
}
