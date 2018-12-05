package com.companysf.filmbilet.addition;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {

    public Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean connected(){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (
                activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()
        );
    }
}
