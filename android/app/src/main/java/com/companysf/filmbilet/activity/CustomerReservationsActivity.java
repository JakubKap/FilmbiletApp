package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.adapter.CustomerReservationsListAdapter;
import com.companysf.filmbilet.addition.ConnectionDetector;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.CustomerReservation;
import com.companysf.filmbilet.appLogic.Movie;
import com.companysf.filmbilet.appLogic.Repertoire;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CustomerReservationsActivity extends AppCompatActivity {
    private static final String logTag = CustomerReservationsActivity.class.getSimpleName();
    private ConnectionDetector cd;
    private ImageButton btn_refresh;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyListRefreshLayout;
    private LinearLayout noEmptyList;
    private boolean animationStarted;
    private Animation animation;

    private CustomerReservationsListAdapter adapter;
    private List<CustomerReservation> reservationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_reservations);

        SessionManager sManager = new SessionManager(getApplicationContext());
        cd = new ConnectionDetector(this);

        if (!sManager.isLoggedIn()) {
            switchToLoginActivity();
        }

        //Views
        btn_refresh = findViewById(R.id.btn_refresh_assets);
        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.empty_list_refresh_layout);
        noEmptyList = findViewById(R.id.noEmptyList);
        TextView title = findViewById(R.id.title);

        //font
        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), "opensans_regular.ttf");
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), "opensans_bold.ttf");
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), "opensans_italic.ttf");

        title.setTypeface(opensansBold);

        //animation
        animationStarted = false;

        //recyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        reservationsList = new ArrayList<>();
        adapter = new CustomerReservationsListAdapter(this, reservationsList,
                opensansItalic, opensansRegular);

        //recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        //animation
        animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(2000);

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_refresh.setAnimation(animation);
                animationStarted = true;
                btn_refresh.setClickable(false);
                updateDataFromServer();
            }
        });

        //volley request
        updateDataFromServer();

        //refresh movies list by swipe down
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateDataFromServer(true);
            }
        });
    }

    private void updateDataFromServer(final boolean manualSwipeRefresh) {
        if (cd.connected()) {
            StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    AppConfig.GET_CUSTOMER_RESERVATIONS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, "get customer reservations");
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean("error");
                                if (error) {
                                    Log.d(logTag, "Error on getting data from API Service");

                                    if (reservationsList.isEmpty()) {
                                        emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                        noEmptyList.setVisibility(View.GONE);
                                    }

                                    cd.buildDialog(
                                            CustomerReservationsActivity.this,
                                            "Błąd połączenia z serwerem",
                                            "Spróbuj ponownie później"
                                    ).show();
                                } else {
                                    emptyListRefreshLayout.setVisibility(View.GONE);
                                    noEmptyList.setVisibility(View.VISIBLE);

                                    JSONArray CustomerReservationJson = json.getJSONArray("customerReservations");
                                    reservationsList.clear();

                                    for (int i = 0; i < CustomerReservationJson.length(); i++) {
                                        Log.d(logTag, "CustomerReservationJson LOG " + CustomerReservationJson.length());
                                        JSONObject CustomerReservationJSON = CustomerReservationJson.getJSONObject(i);
                                        CustomerReservation customerReservation =
                                                new CustomerReservation(
                                                        CustomerReservationJSON.getString("seatNumbers"),
                                                        CustomerReservationJSON.getString("reservDate"),
                                                        new Repertoire(
                                                                new Movie(
                                                                        CustomerReservationJSON.getString("movieTitle")
                                                                ),
                                                                CustomerReservationJSON.getString("repertDate"),
                                                                Integer.parseInt(CustomerReservationJSON.getString("repertId"))
                                                        ),
                                                        Float.parseFloat(CustomerReservationJSON.getString("price"))
                                                );

                                        reservationsList.add(customerReservation);
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                //blad w trakcie pobierania danych
                                e.printStackTrace();

                                if (reservationsList.isEmpty()) {
                                    emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                    noEmptyList.setVisibility(View.GONE);
                                }

                                //1.zerwanie polaczenia internetowego?
                                if (!cd.connected()) {
                                    cd.buildDialog(CustomerReservationsActivity.this,
                                            "Błąd połączenia internetowego",
                                            "Sprawdź połączenie internetowe i spróbuj ponownie"
                                    ).show();
                                }
                                //2.inny blad niz zerwanie polaczenia internetowego
                                else {
                                    cd.buildDialog(
                                            CustomerReservationsActivity.this,
                                            "Błąd pobierania danych z serwera",
                                            "Spróbuj ponownie później"
                                    ).show();
                                }
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(logTag, "Error on response");

                            if (reservationsList.isEmpty()) {
                                emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                noEmptyList.setVisibility(View.GONE);
                            }

                            cd.buildDialog(
                                    CustomerReservationsActivity.this,
                                    "Błąd połączenia z serwerem",
                                    "Spróbuj ponownie później"
                            ).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("customerId", getCustomerId());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest, "req_login");
        } else {
            if (reservationsList.isEmpty()) {
                emptyListRefreshLayout.setVisibility(View.VISIBLE);
                noEmptyList.setVisibility(View.GONE);
            }

            cd.buildDialog(CustomerReservationsActivity.this,
                    "Błąd połączenia internetowego",
                    "Sprawdź połączenie internetowe i spróbuj ponownie"
            ).show();
        }

        if (animationStarted) {
            btn_refresh.clearAnimation();
            btn_refresh.setClickable(true);
            animationStarted = false;
        }

        if (manualSwipeRefresh)
            swipeRefreshLayout.setRefreshing(false);
    }

    private void updateDataFromServer() {
        updateDataFromServer(false);
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(CustomerReservationsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private String getCustomerId() {
        SQLiteHandler db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> customer = db.getCustomer();
        return customer.get("id");
    }
}
