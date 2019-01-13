package com.companysf.filmbilet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.app.CustomVolleyRequest;
import com.companysf.filmbilet.appLogic.Movie;
import com.companysf.filmbilet.appLogic.Repertoire;
import com.companysf.filmbilet.appLogic.Reservation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseDateTime extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();
    private List<Repertoire> repertoireList = new ArrayList<>();

    public void updateMovieInfo(Movie sentMovie){

        final Movie movie = sentMovie;
        final ImageLoader imageLoader;
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Views
                TextView title = findViewById(R.id.title_in_date);
                TextView movieLength = findViewById(R.id.movie_length_in_date);
                TextView minAge = findViewById(R.id.min_age_in_date);
                NetworkImageView picture = findViewById(R.id.picture_in_date);
                TextView genres = findViewById(R.id.genres_in_date);

                Log.d(logTag, "Pozytywnie odnaleziono elementy opisujące film");
                //set views text
                title.setText(movie.getTitle());
                movieLength.setText(String.valueOf(movie.getRunningTimeMin()));
                minAge.setText(String.valueOf(movie.getAge()));
                Log.d(logTag, "Przed ustawieniem obrazka");
                picture.setImageUrl(movie.getPictureURL(), imageLoader);
                Log.d(logTag, "Po ustawieniu obrazka");
                genres.setText(movie.getGenres());
                Log.d(logTag, "Po ustawieniu gatunku");
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);
        updateMovieInfo(new Movie("Planeta Singli 2", 159, 1, "http://filmbilet.cba.pl/images/planeta-singli-2.jpeg", "Komedia"));

        /*Repertoire r1 = new Repertoire( 12, "2019-01-12 23:50:00");
        Log.d(logTag, r1.toString());*/

        //pobranie informacji o repertuarze dla danego filmu z repertuaru
        //TODO pobranie movieId z poprzedniego Activity
        final int movieId = 1;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_MOVIE_REPERTOIRE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, "Reservation request: " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean("error");
                            if (error) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        json.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray repertoiresJson = json.getJSONArray("repertoire");
                                for (int i = 0; i < repertoiresJson.length(); i++) {
                                    Log.d(logTag, "repertoiresJsonLOG " + repertoiresJson.length());
                                    JSONObject repertoireJSON = repertoiresJson.getJSONObject(i);
                                    Repertoire repertoire = new Repertoire(
                                            repertoireJSON.getInt("id"),
                                            repertoireJSON.getString("date")
                                    );

                                    repertoireList.add(repertoire);
                                    Log.d(logTag, "Pobrany repertoire z BD= " + repertoire.toString());
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    "Json error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                        for(Repertoire r : repertoireList)
                            Log.d(logTag, "Zawartość repertoireList po pobraniu danych z BD = " + r.toString());

                        //zaktulizowanie wyglądu ToggleButton'ów związanych z datą i dniem
                        //zaktulizowanie wyglądu ToggleButton'ów związanych z godziną

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("movieId", Integer.toString(movieId));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "req_register");

    }
}
