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
        super(context, "api", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLoginTable = "CREATE TABLE customer(name TEXT, email TEXT UNIQUE, uid TEXT)";
        db.execSQL(createLoginTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer");

        // Create tables again
        onCreate(db);
    }

    public void addUser (String name, String email, String uid){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("email", email);
        contentValues.put("uid", uid);

        db.insert("customer", null, contentValues);
        db.close();
    }

    public HashMap<String, String> getUserDetails() {
        SQLiteDatabase db = this.getReadableDatabase();

        HashMap<String, String> customer = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("SELECT * FROM customer", null);

        //move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            customer.put("name", cursor.getString(1));
            customer.put("email", cursor.getString(2));
            customer.put("uid", cursor.getString(3));
        }
        cursor.close();
        db.close();
        return customer;
    }

    public void deleteUsers(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete("customer", null, null);
        db.close();
    }
}
