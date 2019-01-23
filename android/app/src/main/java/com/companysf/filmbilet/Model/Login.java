package com.companysf.filmbilet.Model;

import android.content.Context;

import com.companysf.filmbilet.Connection.Listener.EmptyFieldsListener;
import com.companysf.filmbilet.Connection.Listener.ErrorListener;
import com.companysf.filmbilet.Connection.LoginConnection;
import com.companysf.filmbilet.Entities.Customer;
import com.companysf.filmbilet.Utilies.ConnectionDetector;
import com.companysf.filmbilet.Utilies.SQLiteHandler;
import com.companysf.filmbilet.Utilies.SessionManager;

public class Login {
    private SessionManager sManager;
    private Context context;
    private SQLiteHandler db;
    private ConnectionDetector cd;
    private ErrorListener errorListener;
    private EmptyFieldsListener emptyFieldsListener;

    public Login(Context context) {
        this.context = context;
        this.db = new SQLiteHandler(context);
        sManager = new SessionManager(context);
    }

    public Login(Context context, ErrorListener errorListener, EmptyFieldsListener emptyFieldsListener) {
        this.context = context;
        this.cd = new ConnectionDetector(context);
        sManager = new SessionManager(context);
        this.errorListener = errorListener;
        this.emptyFieldsListener = emptyFieldsListener;
    }

    public boolean userIsLoggedIn() {

        if (!sManager.isLoggedIn()) {
            return false;
        }
        return true;
    }

    public void logOutCustomer() {
        sManager.setLogin(false);
        db.deleteCustomers();
    }

    public void saveUserOnDevice(Customer customer) {
        sManager.setLogin(true);
        db.addCustomer(customer);
    }

    public void loginCustomer(String email, String password) {
        LoginConnection loginConnection = new LoginConnection(context, errorListener);
        if (cd.connected()) {
            if (!email.isEmpty() && !password.isEmpty()) {
                loginConnection.checkLogin(email, password);
            } else {
                emptyFieldsListener.callBackOnEmptyField();
            }
        } else {
            errorListener.callBackOnError();
        }
    }

}
