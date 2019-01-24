package com.companysf.filmbilet.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.companysf.filmbilet.R;

public class ErrorDialog {
    private Context context;

    public ErrorDialog(Context context) {
        this.context = context;
    }

    public void showErrorDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(context.getString(R.string.dialogPositiveBtnText), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
