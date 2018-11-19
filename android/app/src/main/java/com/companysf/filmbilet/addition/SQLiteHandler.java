package com.companysf.filmbilet.addition;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {
    private static final String TAG = SQLiteHandler.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    public SQLiteHandler(Context context) {
        super(context, "apiDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLoginTable = "CREATE TABLE customer(name TEXT, surname TEXT, email TEXT UNIQUE, uid TEXT)";
        db.execSQL(createLoginTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer");

        // Create tables again
        onCreate(db);
    }

    public void addUser (String name, String surname, String email, String uid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("email", email);
        contentValues.put("uid", uid);

        db.insert("customer", null, contentValues);
        db.close();

        Log.d(TAG, "New customer inserted into DB: ");
    }

    public HashMap<String, String> getUserDetails() {
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, String> customer = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT * FROM customer", null);

        //move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            customer.put("name", cursor.getString(1));
            customer.put("surname", cursor.getString(2));
            customer.put("email", cursor.getString(3));
            customer.put("uid", cursor.getString(4));
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Getting customer from Sqlite: ");
        return customer;
    }

    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("customer", null, null);
        db.close();

        Log.d(TAG, "Table customer was deleted from SQLite");
    }
}
