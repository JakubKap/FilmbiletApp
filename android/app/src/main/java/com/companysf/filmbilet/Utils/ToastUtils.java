package com.companysf.filmbilet.Utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    private Context context;

    public ToastUtils(Context context) {
        this.context = context;
    }

    public void showShortToast(String textToShow) {
        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(String textToShow) {
        Toast.makeText(context, textToShow, Toast.LENGTH_LONG).show();
    }
}
