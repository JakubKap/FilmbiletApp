package com.companysf.filmbilet.AsyncTasks;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.support.annotation.StringRes;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.activity.MainActivity;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Reservation;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FreeSectorsTask extends AsyncTask<Integer, Integer, Void> {

    private static final String logTag = FreeSectorsTask.class.getSimpleName();
    private  ArrayList<Reservation> reservationList = new ArrayList<>();
    private ArrayList<Integer> freeSeats = new ArrayList<>();
    private WeakReference<Context> contextref;
    private Toast toast;
    ConstraintLayout constraintLayout;
    Button button1, button2, button3, button4, button5, button6, button7, button8;
    ProgressBar progressBar;

    public FreeSectorsTask(Context context,  ConstraintLayout constraintLayout, Button button1, Button button2, Button button3,
                           Button button4, Button button5, Button button6, Button button7,
                           Button button8, ProgressBar progressBar) {

        contextref = new WeakReference<>(context);
        this.constraintLayout = constraintLayout;
        this.button1=button1;
        this.button2=button2;
        this.button3=button3;
        this.button4=button4;
        this.button5=button5;
        this.button6=button6;
        this.button7=button7;
        this.button8=button8;
        this.progressBar=progressBar;
    }

    public int freeSectorSlots(int slot_number, boolean isLeft) {
        int takenLeft = 0;
        int takenRight = 0;
        String text;

        for (Reservation r : reservationList) {
            if ((r.getSeatTypeId() == slot_number) && isLeft && r.getSeatNumber() <= 7) {
                takenLeft++;
            } else if ((r.getSeatTypeId() == slot_number) && !isLeft && r.getSeatNumber() > 7) {
                takenRight++;
            }
            text="" + r.getCustomerId();
            Log.d("Zawartość listy: ", text);
        }

        if (isLeft) return 35 - takenLeft;
        else return 35 - takenRight;
    }

    public void changeColorOfButton(Button button, int index){
        String taken="#ff9478";

        int freeSlots = freeSeats.get(index);
        if(freeSlots == 0)
        {
            button.setEnabled(false);
            button.getBackground().setColorFilter(Color.parseColor(taken),PorterDuff.Mode.SRC);
        }

    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        toast =  Toast.makeText(contextref.get(),
                "Ładowanie danych o rezerwacjach ", Toast.LENGTH_LONG);

        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();

    }

    @Override
    protected Void doInBackground(Integer... integers) {


        int repertoireId = integers[0];
        final String repertoireIdString = "" + repertoireId;


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_RESERVATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, "Reservation request: " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean("error");
                            if (error) {
                                Toast.makeText(
                                        contextref.get(),
                                        json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray reservationsJson = json.getJSONArray("reservation");
                                for (int i = 0; i < reservationsJson.length(); i++) {
                                    Log.d(logTag, "reservationJsonLOG " + reservationsJson.length());
                                    JSONObject reservationJSON = reservationsJson.getJSONObject(i);
                                    Reservation reservation = new Reservation(
                                            reservationJSON.getInt("customerId"),
                                            reservationJSON.getInt("hall"),
                                            reservationJSON.getInt("seatNumber"),
                                            reservationJSON.getInt("row"),
                                            reservationJSON.getString("date"),
                                            reservationJSON.getInt("seatTypeId")
                                    );

                                    String text = "Sprawdź rezerwację " + reservation.getCustomerId() + " " + reservation.getHall()
                                            + " " + reservation.getSeatNumber() + " " + reservation.getRow() + " " + reservation.getDatePom() + " "
                                            + reservation.getSeatTypeId();

                                    reservationList.add(reservation);
                                    String text2 = "moj text2" + reservationList.get(i).getCustomerId();
                                    Log.d(logTag, text2);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    contextref.get(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                Toast.makeText(contextref.get(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("repertoireId", repertoireIdString);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "req_register");
        //String text2 = "moj text2" + reservationList.get(0).getCustomerId();
       // Log.d(logTag, text2);
        try {
            Thread.sleep(500);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progressBar.setVisibility(View.VISIBLE);
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
        Log.d("Progress value: ", ""+values[0]);
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        toast.cancel();

        String text ="" + freeSectorSlots(1,true);
        button1.setText(text);

        for(int i=0; i<4; i++){
            freeSeats.add(freeSectorSlots(i+1, true));
            freeSeats.add(freeSectorSlots(i+1, false));
        }
/*
        for (Integer f: freeSeats) {
                Log.d("Zawartość listy freeSeats", "" + f);
        }
*/

    String buttonText = "S1 DOST.: " + freeSeats.get(0) + "\n10 zł";
    button1.setText(buttonText);
    changeColorOfButton(button1, 0);

    buttonText = "S1 DOST.: " + freeSeats.get(1) + "\n10 zł";
    button2.setText(buttonText);
    changeColorOfButton(button2, 1);

    buttonText = "S2 DOST.: " + freeSeats.get(2) + "\n15 zł";
    button3.setText(buttonText);
    changeColorOfButton(button3, 2);

    buttonText = "S2 DOST.: " + freeSeats.get(3) + "\n15 zł";
    button4.setText(buttonText);
    changeColorOfButton(button4, 3);

    buttonText = "S3 DOST.: " + freeSeats.get(4) + "\n20 zł";
    button5.setText(buttonText);
    changeColorOfButton(button5, 4);

    buttonText = "S3 DOST.: " + freeSeats.get(5) + "\n20 zł";
    button6.setText(buttonText);
    changeColorOfButton(button6, 5);

    buttonText = "S4 DOST.: " + freeSeats.get(6) + "\n30 zł";
    button7.setText(buttonText);
    changeColorOfButton(button7, 6);

    buttonText = "S4 DOST.: " + freeSeats.get(7) + "\n30 zł";
    button8.setText(buttonText);
    changeColorOfButton(button8, 7);



        constraintLayout.setVisibility(View.VISIBLE);

    }
}