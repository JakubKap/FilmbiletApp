package com.companysf.filmbilet.Utilies;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;

public class ConnectionDetector {

    private static final String logTag = ConnectionDetector.class.getSimpleName();
    public Context context;

    public ConnectionDetector(Context context) {
        this.context = context;
    }

    public boolean connected(){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (
                activeNetwork != null
        );
    }

    public AlertDialog.Builder buildDialog(Context c, String title, String message) {

        Log.d(logTag, "przed wywolaniem buildera");

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }
}
