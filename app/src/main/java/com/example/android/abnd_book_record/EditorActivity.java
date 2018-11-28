package com.example.android.abnd_book_record;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.abnd_book_record.data.BookContract;
import com.example.android.abnd_book_record.data.BookDbHelper;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int EXISTING_BOOK_LOADER = 0;

    EditText EditProduct;
    EditText EditQuantity;
    EditText EditPrice;
    EditText EditSeller;
    EditText EditContact;
    BookDbHelper dbHelper;
    TextView TextTitle;
    Button Upload;
    Button DelButton;
    ImageButton incrementButton;
    ImageButton decrementButton;
    Uri currentBookURI;
    private boolean mPetHasChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        EditProduct = findViewById(R.id.etPname);
        EditQuantity = findViewById(R.id.etQuantity);
        EditPrice = findViewById(R.id.etPrice);
        EditSeller = findViewById(R.id.etSellerName);
        EditContact = findViewById(R.id.etContact);
        Upload = findViewById(R.id.uploadButton);
        TextTitle = findViewById(R.id.tvTitle);
        DelButton = findViewById(R.id.deleteButton);
        incrementButton = findViewById(R.id.incButton);
        decrementButton = findViewById(R.id.decButton);


        EditContact.setOnTouchListener(mTouchListener);
        EditSeller.setOnTouchListener(mTouchListener);
        EditPrice.setOnTouchListener(mTouchListener);
        EditQuantity.setOnTouchListener(mTouchListener);
        EditProduct.setOnTouchListener(mTouchListener);
        EditContact.setOnTouchListener(mTouchListener);

        getLoaderManager().initLoader(EXISTING_BOOK_LOADER, null, this);


        currentBookURI = getIntent().getData();
        if (currentBookURI == null) {
            TextTitle.setText(R.string.addBook);
            DelButton.setVisibility(View.INVISIBLE);
        } else {
            TextTitle.setText(R.string.editBook);
            DelButton.setVisibility(View.VISIBLE);
        }

        incrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                int quantity = Integer.parseInt(EditQuantity.getText().toString());
                quantity++;
                EditQuantity.setText(quantity + "");
            }
        });

        decrementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {

                int quantity = Integer.parseInt(EditQuantity.getText().toString());
                if (quantity > 0) {
                    quantity--;
                    EditQuantity.setText(quantity + "");

                }
            }
        });


    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.alertMessage));
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keepEditing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteBook();
                finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Perform the deletion of the pet in the database.
     */
    private void deleteBook() {
        if (currentBookURI != null) {
            int rowsDeleted = getContentResolver().delete(currentBookURI, null, null);
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.deletionFailed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.deleteSuccess),
                        Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    public void updateData() {
        String product = EditProduct.getText().toString().trim();
        int quantity = Integer.parseInt(String.valueOf(EditQuantity.getText()));
        int price = Integer.parseInt(EditPrice.getText().toString().trim());
        String seller = EditSeller.getText().toString().trim();
        String contact = EditContact.getText().toString().trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookContract.BookEntry.COLUMN_PROD_NAME, product);
        contentValues.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(BookContract.BookEntry.COLUMN_PRICE, price);
        contentValues.put(BookContract.BookEntry.COLUMN_SELLER_NAME, seller);
        contentValues.put(BookContract.BookEntry.COLUMN_SELLER_CONTACT, contact);
        int rowsAffected = getContentResolver().update(currentBookURI, contentValues, null, null);

        if (rowsAffected == 0) {
            // If no rows were affected, then there was an error with the update.
            Toast.makeText(this, getString(R.string.failedUpdate),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the update was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.updateSuccessful),
                    Toast.LENGTH_SHORT).show();
        }


    }

    public void insertData() {

        Toast.makeText(this, "Taking values", Toast.LENGTH_LONG);
        String product = EditProduct.getText().toString().trim();
        int quantity = Integer.parseInt(String.valueOf(EditQuantity.getText()));
        int price = Integer.parseInt(EditPrice.getText().toString().trim());
        String seller = EditSeller.getText().toString().trim();
        String contact = EditContact.getText().toString().trim();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BookContract.BookEntry.COLUMN_PROD_NAME, product);
        contentValues.put(BookContract.BookEntry.COLUMN_QUANTITY, quantity);
        contentValues.put(BookContract.BookEntry.COLUMN_PRICE, price);
        contentValues.put(BookContract.BookEntry.COLUMN_SELLER_NAME, seller);
        contentValues.put(BookContract.BookEntry.COLUMN_SELLER_CONTACT, contact);
        dbHelper = new BookDbHelper(this);


        Uri newUri = getContentResolver().insert(BookContract.BookEntry.CONTENT_URI, contentValues);
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, R.string.Failed_insertion_msg, Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, R.string.success_insertion_msg, Toast.LENGTH_SHORT).show();
        }


    }

    public void reset() {
        EditProduct.setText("");
        EditQuantity.setText("");
        EditPrice.setText("");
        EditSeller.setText("");
        EditContact.setText("");
    }


    public void uploadFunc(View view) {
        if (currentBookURI == null) {

            if (EditProduct.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.enter_full_details, Toast.LENGTH_SHORT).show();
            } else if ((EditQuantity.getText().toString().isEmpty()) || (!EditQuantity.getText().toString().isEmpty() && Integer.parseInt(EditQuantity.getText().toString()) <= 0)) {
                Toast.makeText(this, R.string.valid_Quantity, Toast.LENGTH_SHORT).show();
            } else if ((EditContact.getText().toString().isEmpty()) || (!EditContact.getText().toString().isEmpty() && EditContact.getText().toString().length() != 10)) {
                Toast.makeText(this, R.string.valid_Contact, Toast.LENGTH_SHORT).show();
            } else if ((EditPrice.getText().toString().isEmpty()) || (!EditPrice.getText().toString().isEmpty() && Integer.parseInt(EditPrice.getText().toString()) <= 0)) {
                Toast.makeText(this, R.string.validAmount, Toast.LENGTH_SHORT).show();
            } else if (EditSeller.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.valid_seller, Toast.LENGTH_SHORT).show();
            } else {
                insertData();
                finish();
                reset();
            }

        }//end Inside if

        else {

            if (EditProduct.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.enter_full_details, Toast.LENGTH_SHORT).show();
            } else if ((EditQuantity.getText().toString().isEmpty()) || (!EditQuantity.getText().toString().isEmpty() && Integer.parseInt(EditQuantity.getText().toString()) <= 0)) {
                Toast.makeText(this, R.string.valid_Quantity, Toast.LENGTH_SHORT).show();
            } else if ((EditContact.getText().toString().isEmpty()) || (!EditContact.getText().toString().isEmpty() && EditContact.getText().toString().length() != 10)) {
                Toast.makeText(this, R.string.valid_Contact, Toast.LENGTH_SHORT).show();
            } else if ((EditPrice.getText().toString().isEmpty()) || (!EditPrice.getText().toString().isEmpty() && Integer.parseInt(EditPrice.getText().toString()) <= 0)) {
                Toast.makeText(this, R.string.validAmount, Toast.LENGTH_SHORT).show();
            } else if (EditSeller.getText().toString().isEmpty()) {
                Toast.makeText(this, R.string.valid_seller, Toast.LENGTH_SHORT).show();
            } else {
                updateData();
                finish();
                reset();
            }


        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /** Content URI for the existing pet (null if it's a new pet) */

        if (currentBookURI != null) {

            String[] Projection = {
                    BookContract.BookEntry._ID,
                    BookContract.BookEntry.COLUMN_PROD_NAME,
                    BookContract.BookEntry.COLUMN_PRICE,
                    BookContract.BookEntry.COLUMN_QUANTITY,
                    BookContract.BookEntry.COLUMN_SELLER_NAME,
                    BookContract.BookEntry.COLUMN_SELLER_CONTACT


            };
            return new CursorLoader(this, currentBookURI, Projection, null, null, null);
        } else
            return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if (currentBookURI != null) {
            incrementButton.setVisibility(View.VISIBLE);
            decrementButton.setVisibility(View.VISIBLE);
            if (cursor.moveToFirst()) {
                // Find the columns of pet attributes that we're interested in
                int nameColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
                int quantityColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
                int priceColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
                int SellerColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_NAME);
                int SellerContactColumnIndex = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_CONTACT);

                // Extract out the value from the Cursor for the given column index
                String name = cursor.getString(nameColumnIndex);
                int quantity = cursor.getInt(quantityColumnIndex);
                int price = cursor.getInt(priceColumnIndex);
                String sellerName = cursor.getString(SellerColumnIndex);
                String sellerContact = cursor.getString(SellerContactColumnIndex);

                EditProduct.setText(name);
                EditQuantity.setText(Integer.toString(quantity));
                EditPrice.setText(Integer.toString(price));
                EditSeller.setText(sellerName);
                EditContact.setText(sellerContact);

            }
        } else {
            incrementButton.setVisibility(View.INVISIBLE);
            decrementButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        EditProduct.setText("");
        EditQuantity.setText("");
        EditPrice.setText("");
        EditSeller.setText("");
        EditContact.setText("");
    }

    public void delFunc(View view) {
        showDeleteConfirmationDialog();
    }
}
