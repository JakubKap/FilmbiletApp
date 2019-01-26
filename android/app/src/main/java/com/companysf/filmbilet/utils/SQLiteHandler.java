package com.companysf.filmbilet.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.SimpleCursorAdapter;

import com.companysf.filmbilet.entities.Customer;
import com.companysf.filmbilet.R;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final String DATABASE_NAME = "filmbiletDB.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "customerTbName";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SURNAME = "surname";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_CUSTOMER_ID = "id";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(" + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                        COLUMN_NAME + " TEXT, " +
                        COLUMN_EMAIL + " TEXT, " +
                        COLUMN_SURNAME + " TEXT, " +
                        COLUMN_CUSTOMER_ID + " INTEGER)"
        );
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_NAME, customer.getName());
        contentValues.put(COLUMN_EMAIL, customer.getEmail());
        contentValues.put(COLUMN_SURNAME, customer.getSurname());
        contentValues.put(COLUMN_CUSTOMER_ID, customer.getId());

        db.insert(TABLE_NAME, null, contentValues);
    }

    public Customer getCustomer() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =  db.rawQuery( "SELECT * FROM " + TABLE_NAME, null );
        cursor.moveToFirst();
        String name = cursor.getString(cursor.getColumnIndex(SQLiteHandler.COLUMN_NAME));
        String email = cursor.getString(cursor.getColumnIndex(SQLiteHandler.COLUMN_EMAIL));
        String surname = cursor.getString(cursor.getColumnIndex(SQLiteHandler.COLUMN_SURNAME));
        int id = cursor.getInt(cursor.getColumnIndex(SQLiteHandler.COLUMN_CUSTOMER_ID));
        if (!cursor.isClosed()) {
            cursor.close();
        }

        return new Customer(name, surname, email, id);
    }

    public void deleteCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
        db.close();

        Log.d(TAG, "Table customer was deleted from SQLite");
    }
}
