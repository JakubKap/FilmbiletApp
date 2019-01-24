package com.companysf.filmbilet.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.companysf.filmbilet.R;

public class ErrorDetector {
    public Context context;

    public ErrorDetector(Context context) {
        this.context = context;
    }

    public AlertDialog.Builder buildDialog(Context c, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(c);
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
