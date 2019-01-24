package com.companysf.filmbilet.Model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;
import com.companysf.filmbilet.Connection.Listener.EmptyEmailListener;
import com.companysf.filmbilet.Connection.Listener.Listener;
import com.companysf.filmbilet.Connection.RegisterConnection;
import com.companysf.filmbilet.Utils.ConnectionDetector;

public class Register {
    private ConnectionDetector cd;
    private Listener listener;
    private EmptyEmailListener emptyEmailListener;
    private RegisterConnection registerConnection;

    public Register(Context context, Listener listener, EmptyEmailListener emptyEmailListener) {
        cd = new ConnectionDetector(context);
        this.listener = listener;
        this.emptyEmailListener = emptyEmailListener;
        registerConnection = new RegisterConnection(context, listener);
    }

    public void validateEmail(String name, String surname, String email, String password) {
        if (cd.connected()) {
            if (!isEmail(email)) {
                emptyEmailListener.callBackOnEmptyEmail();
            } else if (name.isEmpty() || surname.isEmpty() || password.isEmpty()) {
                emptyEmailListener.callBackOnEmptyField();
            } else {
                registerConnection.registerUser(name, surname, email, password);
            }
        } else {
            listener.callBackOnNoNetwork();
        }
    }

    private boolean isEmail(String email) {
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
}
