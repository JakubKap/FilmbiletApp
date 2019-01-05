package com.companysf.filmbilet.activity;

import android.content.Intent;
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
import com.companysf.filmbilet.adapter.CustomListAdapter;
import com.companysf.filmbilet.addition.ConnectionDetector;
import com.companysf.filmbilet.addition.SQLiteHandler;
import com.companysf.filmbilet.addition.SessionManager;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Movie;

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

    private List<Movie> moviesList = new ArrayList<>();
    private CustomListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout emptyListRefreshLayout;
    private Animation animation;
    private ImageButton btn_refresh;
    private boolean animationStarted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sManager = new SessionManager(getApplicationContext());
        cd = new ConnectionDetector(this);

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }

        animationStarted = false;

        //Views
        Button btn_logout = findViewById(R.id.btn_logout);
        TextView customerInfo = findViewById(R.id.customer_info);
        ListView moviesListView = findViewById(R.id.movies_list_view);
        btn_refresh = findViewById(R.id.btn_refresh_assets);
        swipeRefreshLayout = findViewById(R.id.swiper);
        emptyListRefreshLayout = findViewById(R.id.empty_list_refresh_layout);

        //animation
        animation = new RotateAnimation(0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setRepeatCount(-1);
        animation.setDuration(2000);

        //setting adapter
        adapter = new CustomListAdapter(this, moviesList);
        moviesListView.setAdapter(adapter);

        //show customer email
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> customer = db.getCustomer();
        String email = customer.get("email");
        customerInfo.setText(email);

        //logout customer button
        btn_logout.setOnClickListener(new View.OnClickListener() {
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
        if (cd.connected()){
            StringRequest stringRequest = new StringRequest(
                    GET,
                    AppConfig.GET_MOVIES_FROM_REPERTOIRE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, "get repertoire movies request");
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean("error");
                                if (error){
                                    Log.d(logTag, "Error on getting data from API Service");

                                    if (moviesList.isEmpty()){
                                        emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                    } else {
                                        //TODO zalduj ekran powyzej listy filmow informujacy o nieaktualnych danych
                                    }

                                    cd.buildDialog(
                                            MainActivity.this,
                                            "Błąd połączenia z serwerem",
                                            "Spróbuj ponownie później"
                                    ).show();
                                } else{
                                    emptyListRefreshLayout.setVisibility(View.GONE);

                                    JSONArray moviesJson = json.getJSONArray("moviesFromRepertoire");
                                    moviesList.clear();

                                    for (int i = 0; i < moviesJson.length(); i++) {
                                        Log.d(logTag, "moviesJsonLOG " + moviesJson.length());
                                        JSONObject movieJSON = moviesJson.getJSONObject(i);
                                        Movie movie = new Movie(
                                                movieJSON.getString("title"),
                                                movieJSON.getInt("runningTimeMin"),
                                                movieJSON.getInt("age"),
                                                movieJSON.getString("pictureUrl"),
                                                movieJSON.getString("genres")
                                        );
                                        moviesList.add(movie);
                                    }

                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                //blad w trakcie pobierania danych
                                e.printStackTrace();

                                if (moviesList.isEmpty()){
                                    emptyListRefreshLayout.setVisibility(View.VISIBLE);
                                } else {
                                    //TODO zalduj ekran powyzej listy filmow informujacy o nieaktualnych danych
                                }

                                //1.zerwanie polaczenia internetowego?
                                if (!cd.connected()){
                                    cd.buildDialog(MainActivity.this,
                                            "Błąd połączenia internetowego",
                                            "Sprawdź połączenie internetowe i spróbuj ponownie"
                                    ).show();
                                }
                                //2.inny blad niz zerwanie polaczenia internetowego
                                else {
                                    cd.buildDialog(
                                            MainActivity.this,
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

                            if (moviesList.isEmpty()){
                                emptyListRefreshLayout.setVisibility(View.VISIBLE);
                            } else {
                                //TODO zalduj ekran powyzej listy filmow informujacy o nieaktualnych danych
                            }

                            cd.buildDialog(
                                    MainActivity.this,
                                    "Błąd połączenia z serwerem",
                                    "Spróbuj ponownie później"
                            ).show();
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            if (moviesList.isEmpty()){
                emptyListRefreshLayout.setVisibility(View.VISIBLE);
            } else {
                //TODO zalduj ekran powyzej listy filmow informujacy o nieaktualnych danych
            }

            cd.buildDialog(MainActivity.this,
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

    private void updateDataFromServer(){
        updateDataFromServer(false);
    }

    private void logOutCustomer(){
        sManager.setLogin(false);

        db.deleteCustomers();

       // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //startActivity(intent);
        //finish();
    }
}
