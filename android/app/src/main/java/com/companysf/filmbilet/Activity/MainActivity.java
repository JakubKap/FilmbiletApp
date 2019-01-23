package com.companysf.filmbilet.Activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Adapter.MoviesListAdapter;
import com.companysf.filmbilet.Utilies.ConnectionDetector;
import com.companysf.filmbilet.Utilies.SQLiteHandler;
import com.companysf.filmbilet.Utilies.SessionManager;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Entities.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.Request.Method.GET;

public class MainActivity extends AppCompatActivity {
    private static final String logTag = MainActivity.class.getSimpleName();
    private SessionManager sManager;
    private SQLiteHandler db;
    private ConnectionDetector cd;
    private Animation animation;
    private boolean animationStarted;

    private List<Movie> moviesList = new ArrayList<>();
    private MoviesListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyListRefreshLayout;
    private ImageButton btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sManager = new SessionManager(getApplicationContext());
        cd = new ConnectionDetector(this);

        if (!sManager.isLoggedIn()) {
            switchToLoginActivity();
        }

        animationStarted = false;

        Button btnLogout = findViewById(R.id.btn_logout);
        Button btnCustomerReservations = findViewById(R.id.btn_customer_reservations);
        TextView customerInfo = findViewById(R.id.customer_info);
        ListView moviesListView = findViewById(R.id.movies_list_view);
        btn_refresh = findViewById(R.id.btn_refresh_assets);
        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.empty_list_refresh_layout);
        TextView welcomeCustomer = findViewById(R.id.welcomeCustomer);

        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));

        welcomeCustomer.setTypeface(opensansRegular);
        customerInfo.setTypeface(opensansRegular);
        btnCustomerReservations.setTypeface(opensansBold);
        btnLogout.setTypeface(opensansBold);

        animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(2000);

        adapter = new MoviesListAdapter(this, MainActivity.this, moviesList, opensansRegular, opensansBold, opensansItalic);
        moviesListView.setAdapter(adapter);

        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> customer = db.getCustomer();
        String email = customer.get(getString(R.string.sqLiteCustomerEmail));
        customerInfo.setText(email);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOutCustomer();
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_refresh.setAnimation(animation);
                animationStarted = true;
                btn_refresh.setClickable(false);
                updateDataFromServer();
            }
        });

        btnCustomerReservations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToCustomerReservations();
            }
        });


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
                    GET,
                    AppConfig.GET_MOVIES_FROM_REPERTOIRE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, "get repertoire movies request");
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean(getString(R.string.error));
                                if (error) {
                                    Log.d(logTag, "Error on getting data from API Service");

                                    if (moviesList.isEmpty()) {
                                        emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                    }

                                    cd.buildDialog(
                                            MainActivity.this,
                                            getString(R.string.serverErrorTitle),
                                            getString(R.string.serverErrorCheckLater)
                                    ).show();
                                } else {
                                    emptyListRefreshLayout.setVisibility(View.GONE);

                                    JSONArray moviesJson = json.getJSONArray(getString(R.string.moviesFromRepertoireJsonName));
                                    moviesList.clear();

                                    for (int i = 0; i < moviesJson.length(); i++) {
                                        Log.d(logTag, "moviesJsonLOG " + moviesJson.length());
                                        JSONObject movieJSON = moviesJson.getJSONObject(i);
                                        Movie movie = new Movie(
                                                movieJSON.getInt(getString(R.string.jsonMovieFromRepertoireId)),
                                                movieJSON.getString(getString(R.string.jsonMovieFromRepertoireTitle)),
                                                movieJSON.getInt(getString(R.string.jsonMovieFromRepertoireRunningTimeMin)),
                                                movieJSON.getInt(getString(R.string.jsonMovieFromRepertoireAge)),
                                                movieJSON.getString(getString(R.string.jsonMovieFromRepertoirePictureUrl)),
                                                movieJSON.getString(getString(R.string.jsonMovieFromRepertoireGenres))
                                        );
                                        moviesList.add(movie);
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();

                                if (moviesList.isEmpty()) {
                                    emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                }

                                if (!cd.connected()) {
                                    cd.buildDialog(MainActivity.this,
                                            getString(R.string.networkConnectionErrorTitle),
                                            getString(R.string.checkConnectionErrorStatement)
                                    ).show();
                                }
                                else {
                                    cd.buildDialog(
                                            MainActivity.this,
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

                            if (moviesList.isEmpty()) {
                                emptyListRefreshLayout.setVisibility(View.VISIBLE);
                            }

                            cd.buildDialog(
                                    MainActivity.this,
                                    getString(R.string.serverErrorTitle),
                                    getString(R.string.serverErrorCheckLater)
                            ).show();
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            if (moviesList.isEmpty()) {
                emptyListRefreshLayout.setVisibility(View.VISIBLE);
            }

            cd.buildDialog(MainActivity.this,
                    getString(R.string.networkConnectionErrorTitle),
                    getString(R.string.checkConnectionErrorStatement)
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

    private void logOutCustomer() {
        sManager.setLogin(false);

        db.deleteCustomers();

        switchToLoginActivity();
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void  switchToCustomerReservations() {
        Intent intent = new Intent(MainActivity.this, CustomerReservationsActivity.class);
        startActivity(intent);
    }
}
