package com.companysf.filmbilet.AsyncTasks;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
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

public class FreeSeatsTask extends AsyncTask<Integer, Integer, Void> {

    private static final String logTag = FreeSeatsTask.class.getSimpleName();
    private WeakReference<Context> contextref;
    private ArrayList<Boolean> takenSeats = new ArrayList<>();
    private ArrayList<Button> buttons = new ArrayList<>();
    private boolean isLeft;
    private int seatTypeId;
    private LinearLayout linearLayoutRows;
    private ArrayList<Reservation> reservationList = new ArrayList<>();
    private TextView textView1Seats, textView2Seats, textView3Seats;
    private GridLayout gridLayoutSeats;

    private Button buttonIR_1, buttonIR_2, buttonIR_3, buttonIR_4, buttonIR_5,buttonIR_6, buttonIR_7,
            buttonIIR_1, buttonIIR_2, buttonIIR_3, buttonIIR_4, buttonIIR_5,buttonIIR_6, buttonIIR_7,
            buttonIIIR_1, buttonIIIR_2, buttonIIIR_3, buttonIIIR_4, buttonIIIR_5,buttonIIIR_6, buttonIIIR_7,
            buttonIVR_1, buttonIVR_2, buttonIVR_3, buttonIVR_4, buttonIVR_5,buttonIVR_6, buttonIVR_7,
            buttonVR_1, buttonVR_2, buttonVR_3, buttonVR_4, buttonVR_5,buttonVR_6, buttonVR_7,
            btnReserve;

    ProgressBar progressBarSeats;

    View linearLayout; //dodane
    private boolean start;

    public FreeSeatsTask(Context context, View linearLayout, boolean start, boolean isLeft, int seatTypeId) {

        contextref = new WeakReference<>(context);

        this.start=start;

        this.isLeft = isLeft;

        this.linearLayout = linearLayout;

        this.linearLayoutRows = (LinearLayout) linearLayout.findViewById(R.id.linearLayoutRows);

        this.progressBarSeats =(ProgressBar) linearLayout.findViewById(R.id.progressBarSeats);

        this.textView1Seats =(TextView)linearLayout.findViewById(R.id.textView1Seats);
        this.textView2Seats =(TextView)linearLayout.findViewById(R.id.textView2Seats);
        this.textView3Seats =(TextView)linearLayout.findViewById(R.id.textView3Seats);

        this.gridLayoutSeats =(GridLayout)linearLayout.findViewById(R.id.gridLayoutSeats);

        this.seatTypeId=seatTypeId;

        this.btnReserve =linearLayout.findViewById(R.id.btnReserve);

            this.buttonIR_1 = (Button) linearLayout.findViewById(R.id.buttonIR_1);
            buttons.add(buttonIR_1);
            this.buttonIR_2 = (Button) linearLayout.findViewById(R.id.buttonIR_2);
            buttons.add(buttonIR_2);
            this.buttonIR_3 = (Button) linearLayout.findViewById(R.id.buttonIR_3);
            buttons.add(buttonIR_3);
            this.buttonIR_4 = (Button) linearLayout.findViewById(R.id.buttonIR_4);
            buttons.add(buttonIR_4);
            this.buttonIR_5 = (Button) linearLayout.findViewById(R.id.buttonIR_5);
            buttons.add(buttonIR_5);
            this.buttonIR_6 = (Button) linearLayout.findViewById(R.id.buttonIR_6);
            buttons.add(buttonIR_6);
            this.buttonIR_7 = (Button) linearLayout.findViewById(R.id.buttonIR_7);
            buttons.add(buttonIR_7);

            this.buttonIIR_1 = (Button) linearLayout.findViewById(R.id.buttonIIR_1);
            buttons.add(buttonIIR_1);
            this.buttonIIR_2 = (Button) linearLayout.findViewById(R.id.buttonIIR_2);
            buttons.add(buttonIIR_2);
            this.buttonIIR_3 = (Button) linearLayout.findViewById(R.id.buttonIIR_3);
            buttons.add(buttonIIR_3);
            this.buttonIIR_4 = (Button) linearLayout.findViewById(R.id.buttonIIR_4);
            buttons.add(buttonIIR_4);
            this.buttonIIR_5 = (Button) linearLayout.findViewById(R.id.buttonIIR_5);
            buttons.add(buttonIIR_5);
            this.buttonIIR_6 = (Button) linearLayout.findViewById(R.id.buttonIIR_6);
            buttons.add(buttonIIR_6);
            this.buttonIIR_7 = (Button) linearLayout.findViewById(R.id.buttonIIR_7);
            buttons.add(buttonIIR_7);

            this.buttonIIIR_1 = (Button) linearLayout.findViewById(R.id.buttonIIIR_1);
            buttons.add(buttonIIIR_1);
            this.buttonIIIR_2 = (Button) linearLayout.findViewById(R.id.buttonIIIR_2);
            buttons.add(buttonIIIR_2);
            this.buttonIIIR_3 = (Button) linearLayout.findViewById(R.id.buttonIIIR_3);
            buttons.add(buttonIIIR_3);
            this.buttonIIIR_4 = (Button) linearLayout.findViewById(R.id.buttonIIIR_4);
            buttons.add(buttonIIIR_4);
            this.buttonIIIR_5 = (Button) linearLayout.findViewById(R.id.buttonIIIR_5);
            buttons.add(buttonIIIR_5);
            this.buttonIIIR_6 = (Button) linearLayout.findViewById(R.id.buttonIIIR_6);
            buttons.add(buttonIIIR_6);
            this.buttonIIIR_7 = (Button) linearLayout.findViewById(R.id.buttonIIIR_7);
            buttons.add(buttonIIIR_7);

            this.buttonIVR_1 = (Button) linearLayout.findViewById(R.id.buttonIVR_1);
            buttons.add(buttonIVR_1);
            this.buttonIVR_2 = (Button) linearLayout.findViewById(R.id.buttonIVR_2);
            buttons.add(buttonIVR_2);
            this.buttonIVR_3 = (Button) linearLayout.findViewById(R.id.buttonIVR_3);
            buttons.add(buttonIVR_3);
            this.buttonIVR_4 = (Button) linearLayout.findViewById(R.id.buttonIVR_4);
            buttons.add(buttonIVR_4);
            this.buttonIVR_5 = (Button) linearLayout.findViewById(R.id.buttonIVR_5);
            buttons.add(buttonIVR_5);
            this.buttonIVR_6 = (Button) linearLayout.findViewById(R.id.buttonIVR_6);
            buttons.add(buttonIVR_6);
            this.buttonIVR_7 = (Button) linearLayout.findViewById(R.id.buttonIVR_7);
            buttons.add(buttonIVR_7);

            this.buttonVR_1 = (Button) linearLayout.findViewById(R.id.buttonVR_1);
            buttons.add(buttonVR_1);
            this.buttonVR_2 = (Button) linearLayout.findViewById(R.id.buttonVR_2);
            buttons.add(buttonVR_2);
            this.buttonVR_3 = (Button) linearLayout.findViewById(R.id.buttonVR_3);
            buttons.add(buttonVR_3);
            this.buttonVR_4 = (Button) linearLayout.findViewById(R.id.buttonVR_4);
            buttons.add(buttonVR_4);
            this.buttonVR_5 = (Button) linearLayout.findViewById(R.id.buttonVR_5);
            buttons.add(buttonVR_5);
            this.buttonVR_6 = (Button) linearLayout.findViewById(R.id.buttonVR_6);
            buttons.add(buttonVR_6);
            this.buttonVR_7 = (Button) linearLayout.findViewById(R.id.buttonVR_7);
            buttons.add(buttonVR_7);

            Log.d(logTag, "Czy buttons empty: " + buttons.isEmpty());


            //dostosowanie jednego layout do obydwu stron sali
            if(!this.isLeft)
            {
                    int begin = 8;
                    for(Button b : buttons)
                    {
                        Log.d(logTag, "Wartość begin " + begin);
                        b.setText("" + begin);
                        begin++;
                        if(begin == 15) begin=8;
                    }
            }


        }


    public boolean isTaken(int row, int seatNumber) {
        if (this.isLeft) {
            for (Reservation r : reservationList) {
                if (r.getSeatTypeId() == this.seatTypeId && r.getRow() == row && r.getSeatNumber() == seatNumber && r.getSeatNumber() < 8)
                    return true; //znaleziono takie miejsce
            }
        } else {
            for (Reservation r : reservationList) {
                if (r.getSeatTypeId() == this.seatTypeId && r.getRow() == row && r.getSeatNumber() == seatNumber && r.getSeatNumber() >= 8)
                    return true; //znaleziono takie miejsce
            }
        }
        return false; //nie znalezniono takiego miejsca

    }
    public void changeColorOfButton(Button button, int index){
        String taken="#ff9478";
        String free = "#bdc3c7";

       boolean isTaken = takenSeats.get(index);

        if(isTaken)
        {
            button.setEnabled(false);
            button.setBackgroundColor(Color.RED);
            button.setTextColor(Color.WHITE);
//            button.getBackground().setColorFilter(Color.parseColor(taken),PorterDuff.Mode.SRC);
        }
        else if (!button.isEnabled() && isTaken){
            button.setEnabled(true);
            button.getBackground().setColorFilter(Color.parseColor(free),PorterDuff.Mode.SRC);
        }

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.d(logTag, "Rozpoczęcie onPreExecute");

        Log.d(logTag, "Czy buttons empty: " + buttons.isEmpty());
        Log.d(logTag, "buttons size: " + buttons.size());

        if(this.start){

            //this.linearLayoutSeats.setVisibility(View.INVISIBLE);
            this.linearLayoutRows.setVisibility(View.INVISIBLE);

            this.textView1Seats.setVisibility(View.INVISIBLE);
            this.textView2Seats.setVisibility(View.INVISIBLE);
            this.textView3Seats.setVisibility(View.INVISIBLE);

            this.gridLayoutSeats.setVisibility(View.INVISIBLE);

            Log.d(logTag, "OnPreExecute przed zmianą buttonów");
            for(Button b : buttons)
                b.setVisibility(View.INVISIBLE);

            Log.d(logTag, "OnPreExecute po zminie buttonów");
            this.btnReserve.setVisibility(View.INVISIBLE);
        }

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
                                            reservationJSON.getInt("id"),
                                            reservationJSON.getInt("customerId"),
                                            reservationJSON.getInt("seatNumber"),
                                            reservationJSON.getInt("row"),
                                            reservationJSON.getString("date"),
                                            reservationJSON.getInt("seatTypeId")
                                    );

                                    String text = "Sprawdź rezerwację " + reservation.getId() + " " + reservation.getCustomerId()
                                            + " " + reservation.getSeatNumber() + " " + reservation.getRow() + " " + reservation.getDatePom() + " "
                                            + reservation.getSeatTypeId();

                                    reservationList.add(reservation);
                                    String text2 = "moj text2" + reservationList.get(i).getId();
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


                        //isTaken == 1 (miejsce zajęte)

                        if(isLeft) { //lewy sektor
                            boolean taken = false;
                            for (int i = 1; i < 6; i++) {
                                for (int j = 1; j < 8; j++) {
                                    taken = isTaken(i, j);
                                    Log.d(logTag, "isTaken dla (" + i + "," + j + ") = " + taken);
                                    takenSeats.add(taken);
                                }
                            }
                        }
                        else //prawy sektor
                        {
                            boolean taken = false;
                            for (int i = 1; i < 6; i++) {
                                for (int j = 8; j < 15; j++) {
                                    taken = isTaken(i, j);
                                    Log.d(logTag, "isTaken dla (" + i + "," + j + ") = " + taken);
                                    takenSeats.add(taken);
                                }
                            }
                        }

                        for (Boolean t: takenSeats) {
                            Log.d("Zawartość isTaken", "" + t);
                        }


                        int index = 0;
                        for(Button b  : buttons) {
                            changeColorOfButton(b, index);
                            index++;
                        }
                        /*
                        changeColorOfButton(buttonIR_1, 0);

                        changeColorOfButton(buttonIR_2, 1);

                        changeColorOfButton(buttonIR_3, 2);

                        changeColorOfButton(buttonIR_4, 3);

                        changeColorOfButton(buttonIR_5, 4);

                        changeColorOfButton(buttonIR_6, 5);

                        changeColorOfButton(buttonIR_7, 6);


                        changeColorOfButton(buttonIIR_1, 7);

                        changeColorOfButton(buttonIIR_2, 8);

                        changeColorOfButton(buttonIIR_3, 9);

                        changeColorOfButton(buttonIIR_4, 10);

                        changeColorOfButton(buttonIIR_5, 11);

                        changeColorOfButton(buttonIIR_6, 12);

                        changeColorOfButton(buttonIIR_7, 13);


                        changeColorOfButton(buttonIIIR_1, 14);

                        changeColorOfButton(buttonIIIR_2, 15);

                        changeColorOfButton(buttonIIIR_3, 16);

                        changeColorOfButton(buttonIIIR_4, 17);

                        changeColorOfButton(buttonIIIR_5, 18);

                        changeColorOfButton(buttonIIIR_6, 19);

                        changeColorOfButton(buttonIIIR_7, 20);


                        changeColorOfButton(buttonIVR_1, 21);

                        changeColorOfButton(buttonIVR_2, 22);

                        changeColorOfButton(buttonIVR_3, 23);

                        changeColorOfButton(buttonIVR_4, 24);

                        changeColorOfButton(buttonIVR_5, 25);

                        changeColorOfButton(buttonIVR_6, 26);

                        changeColorOfButton(buttonIVR_7, 27);


                        changeColorOfButton(buttonVR_1, 28);

                        changeColorOfButton(buttonVR_2, 29);

                        changeColorOfButton(buttonVR_3, 30);

                        changeColorOfButton(buttonVR_4, 31);

                        changeColorOfButton(buttonVR_5, 32);

                        changeColorOfButton(buttonVR_6, 33);

                        changeColorOfButton(buttonVR_7, 34);
                        */

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
        return null;
    }

    protected void onProgressUpdate(Integer... values) {
        progressBarSeats.setVisibility(View.VISIBLE);
        super.onProgressUpdate(values);
        progressBarSeats.setProgress(values[0]);

    }

    protected void onPostExecute(Void result) {
        super.onPostExecute(result);

        for(Button b : buttons)
            b.setVisibility(View.VISIBLE);

        this.btnReserve.setVisibility(View.VISIBLE);

        //this.linearLayoutSeats.setVisibility(View.VISIBLE);
        this.linearLayoutRows.setVisibility(View.VISIBLE);

        this.textView1Seats.setVisibility(View.VISIBLE);
        this.textView2Seats.setVisibility(View.VISIBLE);
        this.textView3Seats.setVisibility(View.VISIBLE);

        this.gridLayoutSeats.setVisibility(View.VISIBLE);

        this.progressBarSeats.setVisibility(View.INVISIBLE);



        textView1Seats.append(""+seatTypeId);
        textView3Seats.append(""+0);

        int cena=10;

        switch(seatTypeId){
            case 1:
                cena=10;
                break;

            case 2:
                cena=15;
                break;

            case 3:
                cena=20;
                break;

            case 4:
                cena=30;
                break;
        }

        textView2Seats.append(cena + " zł");




    }


}


