package com.companysf.filmbilet.Utils;

import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;

import com.companysf.filmbilet.R;

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
                activeNetwork != null
        );
    }

    //TODO metoda do usuniecia
    public AlertDialog.Builder buildDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.dialogPositiveBtnText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return builder;
    }


}
