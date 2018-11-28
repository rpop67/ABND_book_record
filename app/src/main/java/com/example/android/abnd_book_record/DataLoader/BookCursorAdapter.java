package com.example.android.abnd_book_record.DataLoader;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.abnd_book_record.R;
import com.example.android.abnd_book_record.data.BookContract;


/**
 * Created by Akanksha_Rajwar on 23-11-2018.
 */

public class BookCursorAdapter extends CursorAdapter {

    Integer quantityIndex;


    public BookCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final Cursor mcursor = cursor;
        TextView tvName = view.findViewById(R.id.name);
        final TextView tvQuantity = view.findViewById(R.id.summary);
        TextView tvPrice = view.findViewById(R.id.price);
        final Button buyButton = view.findViewById(R.id.buyButton);
        final Integer nameIndex = mcursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
        quantityIndex = mcursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
        Integer priceIndex = mcursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
        ImageButton shopButton = view.findViewById(R.id.shopButton);

        tvName.setText(mcursor.getString(nameIndex));
        tvQuantity.setText(mcursor.getString(quantityIndex));
        tvPrice.setText("Rs. " + mcursor.getString(priceIndex));

        int idIndex = cursor.getColumnIndex(BookContract.BookEntry._ID);
        int contactIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_CONTACT);
        final int col = cursor.getInt(idIndex);
        final String contact = cursor.getString(contactIndex);


        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(tvQuantity.getText().toString());

                if (quantity > 0) {
                    quantity--;
                    ContentValues values = new ContentValues();
                    values.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
                    Uri updateUri = ContentUris.withAppendedId(BookContract.BookEntry.CONTENT_URI, col);
                    int rowsUpdated = context.getContentResolver().update(updateUri, values, null, null);
                    tvQuantity.setText(quantity + "");

                    if (rowsUpdated == 0) {
                        Toast.makeText(context, R.string.Failed_to_update,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context, R.string.updated_item_successfully,
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, context.getString(R.string.out_of_stock), Toast.LENGTH_SHORT).show();


                }
            }
        });

        shopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact));
                if (dialIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(dialIntent);
                }
            }
        });

    }
}
