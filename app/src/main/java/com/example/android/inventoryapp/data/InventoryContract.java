package com.example.android.inventoryapp.data;

import android.provider.BaseColumns;

/**
 * API Contract for the Inventory App.
 *
 * Created by yahir on 10/13/2018.
 */

public final class InventoryContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InventoryContract() {}

    /**
     * Inner class that defines constant values for the inventory database table.
     * Each entry in the table represents a single product.
     */
    public static final class InventoryEntry implements BaseColumns {

        /** Name of database table for inventory */
        public final static String TABLE_NAME = "inventory";

        /**
         * Unique ID number for the product (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_PRODUCT_NAME ="name";

        /**
         * Price of the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_PRICE = "price";

        /**
         * Quantity of the product.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_PRODUCT_QUANTITY = "quantity";

        /**
         * Supplier name of the product.
         *
         * The only possible values are {@link #SUPPLIER_PEARSON}, {@link #SUPPLIER_BROOK_TAYLOR},
         * or {@link #SUPPLIER_AMERICAN_BOOK}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_SUPPLIER_NAME = "supplierName";

        /**
         * Supplier phone number of the product.
         *
         * Type: TEXT
         */
        public final static String COLUMN_SUPPLIER_PHONE = "supplierPhone";

        /**
         * Possible values for the supplier name.
         */
        public static final int SUPPLIER_PEARSON = 0;
        public static final int SUPPLIER_BROOK_TAYLOR = 1;
        public static final int SUPPLIER_AMERICAN_BOOK = 2;
    }

}
