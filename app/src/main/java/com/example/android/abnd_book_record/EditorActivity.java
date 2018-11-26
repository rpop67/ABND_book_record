package com.example.android.abnd_book_record;

import android.app.AlertDialog;
import android.app.LoaderManager;
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
        DelButton=findViewById(R.id.deleteButton);



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
        if(currentBookURI!=null)
        {
            int rowsDeleted=getContentResolver().delete(currentBookURI,null,null);
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



    public void updateData()
    {
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

    /* public void readData() {
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

             //display column names
             display.setText("\n" + BookContract.BookEntry.COLUMN_PROD_NAME + "   "
                     + BookContract.BookEntry.COLUMN_QUANTITY + "   "
                     + BookContract.BookEntry.COLUMN_PRICE + "   "
                     + BookContract.BookEntry.COLUMN_SELLER_NAME + "   "
                     + BookContract.BookEntry.COLUMN_SELLER_CONTACT + "\n\n");
             //extract column index
             int indexProd = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PROD_NAME);
             int indexQuantity = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_QUANTITY);
             int indexPrice = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_PRICE);
             int indexSeller = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_NAME);
             int indexContact = cursor.getColumnIndex(BookContract.BookEntry.COLUMN_SELLER_CONTACT);



             //while data is present
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
 */
    public void reset() {
        EditProduct.setText("");
        EditQuantity.setText("");
        EditPrice.setText("");
        EditSeller.setText("");
        EditContact.setText("");
    }


    public void uploadFunc(View view) {
        if (currentBookURI == null) {
            if (!EditProduct.getText().toString().equals("") && !EditQuantity.getText().toString().equals("") && !EditContact.getText().toString().equals("") && !EditSeller.getText().toString().equals("")
                    && !EditSeller.getText().toString().equals("")) {

                insertData();
                finish();
                reset();

            } else {
                Toast.makeText(this, "Fill full details", Toast.LENGTH_SHORT).show();
            }//end Inside if

        } else {
            updateData();
            finish();
            reset();


        }
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        /** Content URI for the existing pet (null if it's a new pet) */

        if(currentBookURI!=null)
        {

        String[] Projection = {
                BookContract.BookEntry._ID,
                BookContract.BookEntry.COLUMN_PROD_NAME,
                BookContract.BookEntry.COLUMN_PRICE,
                BookContract.BookEntry.COLUMN_QUANTITY,
                BookContract.BookEntry.COLUMN_SELLER_NAME,
                BookContract.BookEntry.COLUMN_SELLER_CONTACT


        };
        return new CursorLoader(this, currentBookURI, Projection, null, null, null);
      }
      else
          return null;
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor){

        if (cursor == null || cursor.getCount() < 1) {
            return;
        }
        if(currentBookURI!=null) {
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

    public void delFunc(View view)
    {
        showDeleteConfirmationDialog();


    }


}
