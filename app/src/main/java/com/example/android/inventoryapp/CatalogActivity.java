package com.example.android.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.example.android.inventoryapp.data.InventoryDbHelper;
import com.example.android.inventoryapp.data.InventoryContract.InventoryEntry;

/**
 * Displays list of products that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new InventoryDbHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        queryData();
    }

    /**
     * Temporary method to query and display information in the onscreen TextView about the state of
     * the product database.
     */
    private void queryData() {

        // Create and/or open a database to read from it
        // like .open databaseName
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform SQL query with projection
        // Projection
        String[] project = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_PRODUCT_NAME,
                InventoryEntry.COLUMN_PRODUCT_PRICE,
                InventoryEntry.COLUMN_PRODUCT_QUANTITY,
                InventoryEntry.COLUMN_SUPPLIER_NAME,
                InventoryEntry.COLUMN_SUPPLIER_PHONE
        };

        //Call to query method and perform query on the inventory table
        Cursor cursor = db.query(
                InventoryEntry.TABLE_NAME,    // The table to query
                project,                    // The columns to return based on above projection
                null,               // The columns for the WHERE clause
                null,           // The values fro the WHERE clause
                null,              // Don't group the rows
                null,               // Don't filter by row groups
                null               // The sort order
        );

        // Assign displayView to the TextView
        TextView displayView = (TextView) findViewById(R.id.text_view_inventory);

        try {

            // The inventory table contains <number of rows in Cursor> inventory.
            // _id - name - price - quantity - supplierName - supplierPhone
            // In the while loop below, iterate through the rows of the Curser and display
            // the information from each column in this order.
            displayView.setText("The inventory table contains " + cursor.getCount() + " products.\n\n");
            displayView.append(InventoryEntry._ID + " - " +
                    InventoryEntry.COLUMN_PRODUCT_NAME + " - " +
                    InventoryEntry.COLUMN_PRODUCT_PRICE + " - " +
                    InventoryEntry.COLUMN_PRODUCT_QUANTITY + " - " +
                    InventoryEntry.COLUMN_SUPPLIER_NAME + " - " +
                    InventoryEntry.COLUMN_SUPPLIER_PHONE + "\n");

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(InventoryEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_PRICE);
            int quantityColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_PRODUCT_QUANTITY);
            int supplierNameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_NAME);
            int supplierPhoneColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_SUPPLIER_PHONE);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                int currentPrice = cursor.getInt(priceColumnIndex);
                int currentQuantity = cursor.getInt(quantityColumnIndex);
                int currentSupplierName = cursor.getInt(supplierNameColumnIndex);
                String currentSupplierPhone = cursor.getString(supplierPhoneColumnIndex);

                // Display the values from each column of the current row in the cursor in the TextView
                displayView.append(("\n" + currentID + " - " +
                        currentName + " - " +
                        currentPrice + " - " +
                        currentQuantity + " - " +
                        currentSupplierName + " - " +
                        currentSupplierPhone));
            }

        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /**
     * Insert Data into the database
     */
    private void insertData() {
        //Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and product details are the values
        ContentValues values = new ContentValues();
        //Create key value pairs
        values.put(InventoryEntry.COLUMN_PRODUCT_NAME, "3D Puzzle Art");
        values.put(InventoryEntry.COLUMN_PRODUCT_PRICE, 400);
        values.put(InventoryEntry.COLUMN_PRODUCT_QUANTITY, 3);
        values.put(InventoryEntry.COLUMN_SUPPLIER_NAME, InventoryEntry.SUPPLIER_PEARSON);
        values.put(InventoryEntry.COLUMN_SUPPLIER_PHONE, "360-220-8090");

        // Insert a new row for inventory in the database, returning the ID of that new row.
        // The first argument for db.insert() (per rubric) is the inventory table name.
        // The second argument provides the name of the column in which the framework
        // Can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for products.
        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
        Log.v("CatalogActivity", "New row ID " + newRowId);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // Do something
                insertData();
                queryData();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
