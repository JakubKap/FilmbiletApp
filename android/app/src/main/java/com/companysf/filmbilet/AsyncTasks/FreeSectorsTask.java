package com.companysf.filmbilet.AsyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.companysf.filmbilet.R;

import static android.graphics.Color.rgb;

public class FreeSectorsTask extends AsyncTask<Integer, Integer, Void> {

    private static final String logTag = FreeSectorsTask.class.getSimpleName();
    private  ArrayList<Reservation> reservationList = new ArrayList<>();
    private ArrayList<Integer> freeSeats = new ArrayList<>();
    private WeakReference<Context> contextref;
    private ConstraintLayout constraintLayout;
    private Button button1, button2, button3, button4, button5, button6, button7, button8, btn_next, btnReserve;
    private ArrayList<Button> buttons = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView textView1, textView2, textView3, textView4;
    LinearLayout linearLayout;

    boolean start;



    public FreeSectorsTask(Context context,  ConstraintLayout constraintLayout, boolean start) {

        contextref = new WeakReference<>(context);
        this.constraintLayout = constraintLayout;

        this.button1=(Button) constraintLayout.findViewById(R.id.button1);
        buttons.add(button1);

        this.button2=(Button) constraintLayout.findViewById(R.id.button2);
        buttons.add(button2);

        this.button3=(Button) constraintLayout.findViewById(R.id.button3);
        buttons.add(button3);

        this.button4=(Button) constraintLayout.findViewById(R.id.button4);
        buttons.add(button4);

        this.button5=(Button) constraintLayout.findViewById(R.id.button5);
        buttons.add(button5);

        this.button6=(Button) constraintLayout.findViewById(R.id.button6);
        buttons.add(button6);

        this.button7=(Button) constraintLayout.findViewById(R.id.button7);
        buttons.add(button7);

        this.button8=(Button) constraintLayout.findViewById(R.id.button8);
        buttons.add(button8);

        this.btn_next=(Button) constraintLayout.findViewById(R.id.btn_next);
        this.btnReserve = (Button) constraintLayout.findViewById(R.id.btnReserve);

        this.progressBar=(ProgressBar) constraintLayout.findViewById(R.id.progressBar);

        this.textView1=(TextView) constraintLayout.findViewById(R.id.textView1);
        this.textView2=(TextView) constraintLayout.findViewById(R.id.textView2);
        this.textView3=(TextView) constraintLayout.findViewById(R.id.textView3);
        this.textView4=(TextView) constraintLayout.findViewById(R.id.textView4);

        this.linearLayout=(LinearLayout)constraintLayout.findViewById(R.id.linearLayout);
        this.start = start;

    }

    public int freeSectorSlots(int slot_number, int firstSeat) {
        int taken=0;

        //zbudowanie siatki miejsc

        ArrayList<Integer> seatNumber = new ArrayList<>();
        int seatNr = firstSeat;

        for(int i=1; i<=35; i++){

            if(i==8 || i==15 || i==22 || i==29) {
                seatNr += 7;
                seatNumber.add(seatNr);
                Log.d(logTag, "Dodana wartość do siatki: " + seatNr + " dla i = " + i);
                seatNr++;
            }
            else{
                seatNumber.add(seatNr);
                Log.d(logTag, "Dodana wartość do siatki: " + seatNr+ " dla i = " + i);
                seatNr++;
            }

        }

        String text;

/*
        for (Reservation r : reservationList) {
            if ((r.getSeatTypeId() == slot_number) && isLeft && r.getSeatNumber() <= 7) {
                takenLeft++;
            } else if ((r.getSeatTypeId() == slot_number) && !isLeft && r.getSeatNumber() > 7) {
                takenRight++;
            }
            text="" + r.getCustomerId();
            Log.d("Zawartość listy: ", text);
        }
*/


        for (Reservation r : reservationList) {
            if ((r.getSeatTypeId() == slot_number) && seatNumber.contains(r.getSeatNumber()))
                taken++;

            text="" + seatNumber.contains(r.getSeatNumber());
            Log.d("Zawartość listy: ", text);
        }
        return 35-taken;
    }

    public void changeColorOfButton(Button button, int index){

        int freeSlots = freeSeats.get(index);

        if(freeSlots == 0)
        {
            button.setEnabled(false);
            //button.setBackgroundColor(rgb(207, 0, 15)); //Monza
            button.setBackgroundResource(R.drawable.button_taken); //Monza
        }
        else if (!button.isEnabled() && freeSlots == 1){
            button.setEnabled(true);
            //button.getBackground().setColorFilter(Color.parseColor(free),PorterDuff.Mode.SRC);

        }


    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(this.start){
            this.button1.setVisibility(View.INVISIBLE);
            this.button2.setVisibility(View.INVISIBLE);
            this.button3.setVisibility(View.INVISIBLE);
            this.button4.setVisibility(View.INVISIBLE);
            this.button5.setVisibility(View.INVISIBLE);
            this.button6.setVisibility(View.INVISIBLE);
            this.button7.setVisibility(View.INVISIBLE);
            this.button8.setVisibility(View.INVISIBLE);
            this.btn_next.setVisibility(View.INVISIBLE);
            this.btnReserve.setVisibility(View.INVISIBLE);

            this.progressBar.setVisibility(View.VISIBLE);

            this.textView1.setVisibility(View.INVISIBLE);
            this.textView2.setVisibility(View.INVISIBLE);
            this.textView3.setVisibility(View.INVISIBLE);
            this.textView4.setVisibility(View.INVISIBLE);

            this.linearLayout.setVisibility(View.INVISIBLE);
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
                                    String text2 = "moj text2: " + reservationList.get(i).getId();
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


                        int firstSeat = 1;
                        int j = 1;

                        for(int i=1; i<9; i++){
                            freeSeats.add(freeSectorSlots(j, firstSeat));

                            if(i%2 != 0) firstSeat+= 7;
                            else {
                                firstSeat+=63;
                                j++;
                            }
                        }

                        for (Integer f: freeSeats) {
                            Log.d("Zawartość freeSeats", "" + f);
                        }


                        String buttonText;
                        int i=0;
                        for(Button b : buttons){
                            buttonText=freeSeats.get(i) + "/35";
                            b.setText(buttonText);
                            changeColorOfButton(b, i);
                            i++;
                        }

                        /*
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

    @Override
    protected void onProgressUpdate(Integer... values) {
        /*progressBar.setVisibility(View.VISIBLE);
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);*/

    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);


        this.button1.setVisibility(View.VISIBLE);
        this.button1.setBackgroundResource(R.drawable.button_normal_first);
        //this.button1.setBackground();


        this.button2.setVisibility(View.VISIBLE);
        this.button2.setBackgroundResource(R.drawable.button_normal_first);

        this.button3.setVisibility(View.VISIBLE);
        this.button3.setBackgroundResource(R.drawable.button_normal_second);

        this.button4.setVisibility(View.VISIBLE);
        this.button4.setBackgroundResource(R.drawable.button_normal_second);

        this.button5.setVisibility(View.VISIBLE);
        this.button5.setBackgroundResource(R.drawable.button_normal_third);

        this.button6.setVisibility(View.VISIBLE);
        this.button6.setBackgroundResource(R.drawable.button_normal_third);

        this.button7.setVisibility(View.VISIBLE);
        this.button7.setBackgroundResource(R.drawable.button_normal_fourth);

        this.button8.setVisibility(View.VISIBLE);
        this.button8.setBackgroundResource(R.drawable.button_normal_fourth);

        this.button1.setBackgroundResource(R.drawable.button_normal_first);

        this.btn_next.setVisibility(View.VISIBLE);

        this.progressBar.setVisibility(View.INVISIBLE);

        this.textView1.setVisibility(View.VISIBLE);
        this.textView2.setVisibility(View.VISIBLE);

        this.linearLayout.setVisibility(View.VISIBLE);

    }
}