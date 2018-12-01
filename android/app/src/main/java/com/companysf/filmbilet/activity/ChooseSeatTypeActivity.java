package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Movie;
import com.companysf.filmbilet.appLogic.Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.Request.Method.GET;

public class ChooseSeatTypeActivity extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private List<Reservation> reservationList = new ArrayList<>();


    Button button1, button2, button3, button4, button5, button6, button7, button8, btn_back, btn_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_seat_type2);

        // btnNext = (Button) findViewById(R.id.btnNext);

        //adjusting size of  the buttons

        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);
        button7 = (Button) findViewById(R.id.button7);
        button8 = (Button) findViewById(R.id.button8);

        btn_back = (Button) findViewById(R.id.btn_back);
        btn_next=(Button) findViewById(R.id.btn_next);

        //ConstraintLayout.LayoutParams params1 = (RelativeLayout.LayoutParams)button1.getLayoutParams();


        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT)
        {

            ConstraintLayout.LayoutParams params1 = (ConstraintLayout.LayoutParams)button1.getLayoutParams();
            params1.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params1.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button1.setLayoutParams(params1);

            ConstraintLayout.LayoutParams params2 = (ConstraintLayout.LayoutParams)button2.getLayoutParams();
            params2.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params2.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button2.setLayoutParams(params2);

            ConstraintLayout.LayoutParams params3 = (ConstraintLayout.LayoutParams)button3.getLayoutParams();
            params3.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params3.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button3.setLayoutParams(params3);

            ConstraintLayout.LayoutParams params4 = (ConstraintLayout.LayoutParams)button4.getLayoutParams();
            params4.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params4.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button4.setLayoutParams(params4);


            ConstraintLayout.LayoutParams params5 = (ConstraintLayout.LayoutParams)button5.getLayoutParams();
            params5.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params5.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button5.setLayoutParams(params5);

            ConstraintLayout.LayoutParams params6 = (ConstraintLayout.LayoutParams)button6.getLayoutParams();
            params6.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params6.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button6.setLayoutParams(params6);

            ConstraintLayout.LayoutParams params7 = (ConstraintLayout.LayoutParams)button7.getLayoutParams();
            params7.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params7.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button7.setLayoutParams(params7);

            ConstraintLayout.LayoutParams params8 = (ConstraintLayout.LayoutParams)button8.getLayoutParams();
            params8.width=(getResources().getDisplayMetrics().widthPixels)/4;
            params8.height=((getResources().getDisplayMetrics().heightPixels/3))/4;
            button8.setLayoutParams(params8);

            ConstraintLayout.LayoutParams params9 = (ConstraintLayout.LayoutParams)btn_back.getLayoutParams();
            params9.width=(getResources().getDisplayMetrics().widthPixels)/6;
            params9.height=((getResources().getDisplayMetrics().heightPixels/3))/6;
            btn_back.setLayoutParams(params9);


            ConstraintLayout.LayoutParams params10 = (ConstraintLayout.LayoutParams)btn_next.getLayoutParams();
            params10.width=(getResources().getDisplayMetrics().widthPixels)/6;
            params10.height=((getResources().getDisplayMetrics().heightPixels/3))/6;
            btn_next.setLayoutParams(params10);



            checkReservations("1");




        }
        else
        {

            button1.setText("S1 dost.: 10 zł");
            button2.setText("S1 dost.: 10 zł");


            button3.setText("S2 dost.: 15 zł");
            button4.setText("S2 dost.: 15 zł");

            button5.setText("S3 dost.: 20 zł");
            button6.setText("S3 dost.: 20 zł");

            button7.setText("S4 dost.: 30 zł");
            button8.setText("S4 dost.: 30 zł");


        }

/*

        sManager = new SessionManager(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }
*/


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent i= new Intent(getApplicationContext(),ChooseSeatActivity.class);
                startActivity(i);*/

                // inflate the layout of the popup window
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.activity_choose_seat, null);

                // create the popup window
                int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

                // show the popup window
                // which view you pass in doesn't matter, it is only used for the window tolken
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                //popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.clear));
               /* popupWindow.setBackgroundDrawable(new ColorDrawable(
                        android.graphics.Color.TRANSPARENT));*/




                Drawable d = new ColorDrawable(Color.WHITE);

                d.setAlpha(130);

                popupWindow.setBackgroundDrawable(new BitmapDrawable());

               // popupWindow.showAsDropDown(mBtnPopUpWindow, 0, 0,Gravity.LEFT);

                getWindow().setBackgroundDrawable(d);

                // dismiss the popup window when touched
                popupView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        Drawable d = new ColorDrawable(Color.WHITE);
                        popupWindow.setBackgroundDrawable(new BitmapDrawable());
                        getWindow().setBackgroundDrawable(d);
                        return true;
                    }
                });
            }


        });


    }

    private void checkReservations(final String repertoireId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_RESERVATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, "Reservation request: " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean("error");
                            if (error){
                                Toast.makeText(
                                        getApplicationContext(),
                                        json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else{
                                JSONArray reservationsJson = json.getJSONArray("reservation");
                                for (int i = 0; i < reservationsJson.length(); i++) {
                                    Log.d(logTag, "moviesJsonLOG " + reservationsJson.length());
                                    JSONObject reservationJSON = reservationsJson.getJSONObject(i);
                                    Reservation reservation = new Reservation(
                                            reservationJSON.getInt("customerId"),
                                            reservationJSON.getInt("hall"),
                                            reservationJSON.getInt("searNumber"),
                                            reservationJSON.getInt("row"),
                                            reservationJSON.getInt("TypeId"),
                                            reservationJSON.getInt("repertoireId"),
                                            (SimpleDateFormat) reservationJSON.get("date")

                                    );

                                    reservationList.add(reservation);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("repertoireId", repertoireId);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "req_register");
    }
    public void buttonClicked(View view){



    }

}

