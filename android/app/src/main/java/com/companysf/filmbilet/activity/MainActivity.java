package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.adapter.CustomListAdapter;
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

    private List<Movie> moviesList = new ArrayList<>();
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sManager = new SessionManager(getApplicationContext());

        if (!sManager.isLoggedIn()) {
            logOutCustomer();
        }

        //Views
        Button btn_logout = findViewById(R.id.btn_logout);
        TextView customerInfo = findViewById(R.id.customer_info);
        ListView moviesListView = findViewById(R.id.movies_list_view);

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

        //volley request
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
                                //TODO add error message screen
                            } else{
                                JSONArray moviesJson = json.getJSONArray("moviesFromRepertoire");
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
                                    if (movie.getGenres() == null)
                                        movie.setGenres("");
                                    moviesList.add(movie);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(logTag, "Error on response");
                        //TODO add error message screen
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(stringRequest);
    }

    private void logOutCustomer(){
        sManager.setLogin(false);

        db.deleteCustomers();

       // Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        //startActivity(intent);
        //finish();
    }
}
