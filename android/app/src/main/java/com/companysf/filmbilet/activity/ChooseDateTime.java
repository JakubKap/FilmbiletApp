package com.companysf.filmbilet.activity;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChooseDateTime extends AppCompatActivity {

    private static final String logTag = ChooseDateTime.class.getSimpleName();
    private List<Repertoire> repertoireList = new ArrayList<>();
    private GridView hoursGridView;
    private ToggleButton[] datesButtons = new ToggleButton[5];

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

    public void updateDateButtons(){

        //sortowanie elementów w kolekcji (zgodnie z kolejnością dat)
        Collections.sort(repertoireList, new Comparator<Repertoire>() {
            @Override
            public int compare(Repertoire r1, Repertoire r2) {
                return r1.getDate().compareTo(r2.getDate());
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                for(Repertoire r : repertoireList){
                    String text = Integer.toString(r.getDayOfMonth()) + "\n" + r.getDayOfWeek();
                    datesButtons[i].setText(text);
                    datesButtons[i].setTextOn(text);
                    datesButtons[i].setTextOff(text);
                    i++;
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);
        updateMovieInfo(new Movie("Planeta Singli 2", 159, 1, "http://filmbilet.cba.pl/images/planeta-singli-2.jpeg", "Komedia"));

        datesButtons[0] = findViewById(R.id.toggleButton1);
        //domyślnie pierwszty element zawsze na początku zaznaczony
        datesButtons[0].setChecked(true);
        datesButtons[1] = findViewById(R.id.toggleButton2);
        datesButtons[2] = findViewById(R.id.toggleButton3);
        datesButtons[3] = findViewById(R.id.toggleButton4);
        datesButtons[4] = findViewById(R.id.toggleButton5);

        for(int i=0; i<datesButtons.length; i++)
            Log.d(logTag, "isChecked = " + datesButtons[i].isChecked());

        hoursGridView = findViewById(R.id.hoursGridView);

        hoursGridView.setNumColumns(2);


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


                        for (Repertoire r : repertoireList)
                            Log.d(logTag, "Zawartość repertoireList po pobraniu danych z BD = " + r.toString());

                        //zaktulizowanie wyglądu ToggleButton'ów związanych z datą i dniem
                        updateDateButtons();
                        //zaktulizowanie wyglądu ToggleButton'ów związanych z godziną
                        Log.d(logTag, "Po zaktualizowaniu");

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


        //dodanie do każdego z przycisków wyżej zadeklarowanej metody setOnCheckedChangeListener

        for (int i = 0; i < datesButtons.length; i++){
            final int finalI = i;
            datesButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(logTag, "Stan buttona = " + Boolean.toString(datesButtons[finalI].isChecked()));
                    if (isChecked) {
                        for(int j =0; j<datesButtons.length; j++) {
                            if (finalI != j) {
                                datesButtons[j].setChecked(false);
                                datesButtons[j].setBackgroundResource(R.drawable.normal_date_button);
                                datesButtons[j].setTextColor(Color.BLACK);
                            }
                        }
                        datesButtons[finalI].setBackgroundResource(R.drawable.gradient_date_button);
                        datesButtons[finalI].setTextColor(Color.WHITE);
                        updateDateButtons();
                        //TODO notofyDataChanged do Adaptera obsługującego godziny
                    }
                    else {
                        updateDateButtons();
                    }

                }

            });

    }

    }
}
