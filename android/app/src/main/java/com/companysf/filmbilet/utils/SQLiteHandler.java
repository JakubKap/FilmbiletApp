package com.companysf.filmbilet.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.companysf.filmbilet.entities.Customer;
import com.companysf.filmbilet.R;

public class SQLiteHandler extends SQLiteOpenHelper {
    private Context context;
    private static final String TAG = SQLiteHandler.class.getSimpleName();

    private static final String customerName = "name";
    private static final String customerSurname = "surname";
    private static final String customerEmail = "email";
    private static final String customerId = "id";

    public SQLiteHandler(Context context) {
        super(context, context.getString(R.string.sqLiteDatabaseName), null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createLoginTable =
                "CREATE TABLE customer" + "(" +
                        customerName + "TEXT, " +
                        customerSurname + "TEXT, " +
                        customerEmail + "TEXT, " +
                        customerId + "TEXT)";
        db.execSQL(createLoginTable);
        Log.d(TAG, "Database tables created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS customer");
        onCreate(db);
    }

    //TODO metoda do usuniecia
//    public void addCustomer(String name, String surname, String email, String id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        db.execSQL("INSERT INTO customer values(?,?,?,?)",new String[]{name,surname,email,id});
//        db.close();
//
//        Log.d(TAG, "New customer inserted into DB: ");
//    }

    public void addCustomer(Customer customer) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL(
                "INSERT INTO customer values(?,?,?,?)",
                new String[]{customer.getName(),customer.getSurname(),customer.getEmail(),customer.getId()}
        );
        db.close();

        Log.d(TAG, "New customer inserted into DB: ");
    }

    //TODO metoda do usuniecia
//    public HashMap<String, String> getCustomer() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        HashMap<String, String> customer = new HashMap<>();
//        Cursor cursor = db.rawQuery("SELECT * FROM customer", null);
//
//        //move to first row
//        cursor.moveToFirst();
//        if (cursor.getCount() > 0) {
//            customer.put(customerName, cursor.getString(0));
//            customer.put(customerSurname, cursor.getString(1));
//            customer.put(customerEmail, cursor.getString(2));
//            customer.put(customerId, cursor.getString(3));
//
//            Log.d(TAG, "cursor.getCount() > 0");
//        }
//        cursor.close();
//        db.close();
//
//        Log.d(TAG, "Getting customer from Sqlite: ");
//        return customer;
//    }

    public Customer getCustomer() {
        SQLiteDatabase db = this.getReadableDatabase();

        Customer customer = new Customer();
        Cursor cursor = db.rawQuery("SELECT * FROM customer", null);

        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            customer.setName(cursor.getString(0));
            customer.setSurname(cursor.getString(1));
            customer.setEmail(cursor.getString(2));
            customer.setId(cursor.getString(3));

            Log.d(TAG, "cursor.getCount() > 0");
        }
        cursor.close();
        db.close();

        Log.d(TAG, "Getting customer from Sqlite: ");
        return customer;
    }

    public void deleteCustomers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(context.getString(R.string.sqLiteCustomerTable), null, null);
        db.close();

        Log.d(TAG, "Table customer was deleted from SQLite");
    }
}
