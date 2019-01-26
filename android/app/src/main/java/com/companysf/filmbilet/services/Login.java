package com.companysf.filmbilet.services;

import android.content.Context;

import com.companysf.filmbilet.connection.Listener.EmptyFieldsListener;
import com.companysf.filmbilet.connection.Listener.Listener;
import com.companysf.filmbilet.connection.LoginConnection;
import com.companysf.filmbilet.entities.Customer;
import com.companysf.filmbilet.utils.ConnectionDetector;
import com.companysf.filmbilet.utils.SQLiteHandler;
import com.companysf.filmbilet.utils.SessionManager;

public class Login {
    private SessionManager sManager;
    private Context context;
    private SQLiteHandler db;
    private ConnectionDetector cd;
    private Listener listener;
    private EmptyFieldsListener emptyFieldsListener;

    public Login(Context context) {
        this.context = context;
        this.db = new SQLiteHandler(context);
        sManager = new SessionManager(context);
    }

    public Login(Context context, Listener listener, EmptyFieldsListener emptyFieldsListener) {
        this.context = context;
        this.cd = new ConnectionDetector(context);
        sManager = new SessionManager(context);
        this.listener = listener;
        this.emptyFieldsListener = emptyFieldsListener;
    }

    public boolean userIsLoggedIn() {
        return sManager.isLoggedIn();
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
        LoginConnection loginConnection = new LoginConnection(context, listener);
        if (cd.connected()) {
            if (!email.isEmpty() && !password.isEmpty()) {
                loginConnection.checkLogin(email, password);
            } else {
                emptyFieldsListener.callBackOnEmptyField();
            }
        } else {
            listener.callBackOnError();
        }
    }

}
