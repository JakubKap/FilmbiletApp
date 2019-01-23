package com.companysf.filmbilet.Activity;

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
import android.widget.LinearLayout;
import android.widget.TextView;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Adapter.CustomerReservationsListAdapter;
import com.companysf.filmbilet.Utilies.ConnectionDetector;
import com.companysf.filmbilet.Utilies.SQLiteHandler;
import com.companysf.filmbilet.Utilies.SessionManager;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Entities.CustomerReservation;
import com.companysf.filmbilet.Entities.Movie;
import com.companysf.filmbilet.Entities.Repertoire;

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
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyListRefreshLayout;

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

        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.empty_list_refresh_layout);
        TextView title = findViewById(R.id.title);

        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));

        title.setTypeface(opensansBold);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        reservationsList = new ArrayList<>();
        adapter = new CustomerReservationsListAdapter(this, reservationsList,
                opensansItalic, opensansRegular);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        updateDataFromServer();

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
                                boolean error = json.getBoolean(getString(R.string.error));
                                if (error) {
                                    Log.d(logTag, "Error on getting data from API Service");

                                    if (reservationsList.isEmpty()) {
                                        emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    emptyListRefreshLayout.setVisibility(View.GONE);

                                    JSONArray CustomerReservationJson = json.getJSONArray(getString(R.string.customerReservationsJsonName));
                                    reservationsList.clear();

                                    for (int i = 0; i < CustomerReservationJson.length(); i++) {
                                        Log.d(logTag, "CustomerReservationJson LOG " + CustomerReservationJson.length());
                                        JSONObject CustomerReservationJSON = CustomerReservationJson.getJSONObject(i);
                                        CustomerReservation customerReservation =
                                                new CustomerReservation(
                                                        CustomerReservationJSON.getString(getString(R.string.jsonSeatNumbers)),
                                                        CustomerReservationJSON.getString(getString(R.string.jsonReservDate)),
                                                        new Repertoire(
                                                                new Movie(
                                                                        CustomerReservationJSON.getString(getString(R.string.jsonMovieTitle))
                                                                ),
                                                                CustomerReservationJSON.getString(getString(R.string.jsonRepertDate)),
                                                                Integer.parseInt(CustomerReservationJSON.getString(getString(R.string.jsonRepertId)))
                                                        ),
                                                        Float.parseFloat(CustomerReservationJSON.getString(getString(R.string.jsonPrice)))
                                                );

                                        reservationsList.add(customerReservation);
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                if (reservationsList.isEmpty()) {
                                    emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                }

                                if (!cd.connected()) {
                                    cd.buildDialog(CustomerReservationsActivity.this,
                                            getString(R.string.networkConnectionErrorTitle),
                                            getString(R.string.checkConnectionErrorStatement)
                                    ).show();
                                }
                                else {
                                    cd.buildDialog(
                                            CustomerReservationsActivity.this,
                                            getString(R.string.serverErrorTitle),
                                            getString(R.string.serverErrorCheckLater)
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
                            }

                            cd.buildDialog(
                                    CustomerReservationsActivity.this,
                                    getString(R.string.serverErrorTitle),
                                    getString(R.string.serverErrorCheckLater)
                            ).show();
                        }
                    }
            ){
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(getString(R.string.reservationPutReqParam), getCustomerId());
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.loginRequestAdd));
        } else {
            if (reservationsList.isEmpty()) {
                emptyListRefreshLayout.setVisibility(View.VISIBLE);
            }

            cd.buildDialog(CustomerReservationsActivity.this,
                    getString(R.string.networkConnectionErrorTitle),
                    getString(R.string.checkConnectionErrorStatement)
            ).show();
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
        return customer.get(getString(R.string.id));
    }
}
