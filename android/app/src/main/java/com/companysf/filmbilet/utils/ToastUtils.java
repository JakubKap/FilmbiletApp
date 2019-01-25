package com.companysf.filmbilet.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static void showShortToast(Context context, String textToShow) {
        Toast.makeText(context, textToShow, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(Context context, String textToShow) {
        Toast.makeText(context, textToShow, Toast.LENGTH_LONG).show();
    }
}
