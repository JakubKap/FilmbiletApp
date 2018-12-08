package com.companysf.filmbilet.AsyncTasks;

import android.os.AsyncTask;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.TextView;

import com.companysf.filmbilet.appLogic.Reservation;

import java.util.ArrayList;

public class FreeSeatsTask extends AsyncTask<Void, Integer, Void> {

    LinearLayout linearLayout1, linearLayout2;
    ArrayList<Reservation> reservations = new ArrayList<>();
    TextView textView1, textView2, textView3;
    GridLayout gridLayout;

    Button btnPayment;

    public FreeSeatsTask(Context context, ArrayList<Reservation> reservations, LinearLayout linearLayout) {
    }


    @Override
    protected Void doInBackground(Void... voids) {
        return null;
    }
}
