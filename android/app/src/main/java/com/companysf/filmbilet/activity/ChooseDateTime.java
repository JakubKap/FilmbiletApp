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
import com.companysf.filmbilet.adapter.HoursAdapter;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChooseDateTime extends AppCompatActivity {

    private static final String logTag = ChooseDateTime.class.getSimpleName();
    private List<Repertoire> repertoireList = new ArrayList<>();
    private List<Repertoire> uniqueDates;
    private List<Repertoire> hoursForDate = new ArrayList<>();
    private GridView hoursGridView;
    private HoursAdapter hoursAdapter;
    private ToggleButton[] datesButtons = new ToggleButton[5];
    private boolean[] selectedDate = new boolean[5];

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

        uniqueDates = new ArrayList<>(repertoireList);
        Set<Integer> dateIndexesToRemove = new HashSet<>();

        for(Repertoire r : uniqueDates)
            Log.d(logTag, "Zawartość uniqueDates przed filtrowaniem= " + r.toString());
        //wstawienie do kolekcji uniqueDates unikalnych dat z repertoire List


        for(Repertoire r : repertoireList){
            int innerInc=0;
            int index =0;
            for(Repertoire ud : uniqueDates){

                if(r.getYear() == ud.getYear() && r.getMonth() == ud.getMonth()
                        && r.getDayOfMonth() == ud.getDayOfMonth())
                    innerInc++;

                if(innerInc >= 2 && r.getYear() == ud.getYear() && r.getMonth() == ud.getMonth()
                        && r.getDayOfMonth() == ud.getDayOfMonth()) {

                    if(dateIndexesToRemove.add(index))
                        Log.d(logTag, "Dodany index = " + index);
                }
                index++;
            }
        }

        if(dateIndexesToRemove.size() > 0)
        for(Integer i : dateIndexesToRemove)
            uniqueDates.remove(repertoireList.get(i));

        for(Repertoire r : uniqueDates)
            Log.d(logTag, "Zawartość uniqueDates po filtrowaniu= " + r.toString());



        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                for(Repertoire r : uniqueDates){
                        String text = Integer.toString(r.getDayOfMonth());
                        StringBuilder sB = new StringBuilder(text);
                        sB.append("\n");
                        sB.append(r.getDayOfWeek());
                        text = sB.toString();
                        datesButtons[i].setText(text);
                        datesButtons[i].setTextOn(text);
                        datesButtons[i].setTextOff(text);
                        i++;
                }
            }
        });

    }

    public void prepareHoursForDate(int indexOfList){

        int index = indexOfList;
        if(hoursForDate.size() > 0) hoursForDate.clear();

        for(Repertoire r : repertoireList) {
            if(r.getYear() == uniqueDates.get(index).getYear() && r.getMonth() == uniqueDates.get(index).getMonth()
                    && r.getDayOfMonth() == uniqueDates.get(index).getDayOfMonth()){
                hoursForDate.add(r);
                Log.d(logTag, "Dodana wartość do hoursForDate = " + r.toString());
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);
        updateMovieInfo(new Movie("Planeta Singli 2", 159, 1, "http://filmbilet.cba.pl/images/planeta-singli-2.jpeg", "Komedia"));

        datesButtons[0] = findViewById(R.id.toggleButton1);
        //domyślnie pierwszty element zawsze na początku zaznaczony
        selectedDate[0] = true;
        datesButtons[1] = findViewById(R.id.toggleButton2);
        datesButtons[2] = findViewById(R.id.toggleButton3);
        datesButtons[3] = findViewById(R.id.toggleButton4);
        datesButtons[4] = findViewById(R.id.toggleButton5);

        for(int i=1; i<selectedDate.length; i++)
            selectedDate[i] = false;


        for(int j=0; j<selectedDate.length; j++)
            Log.d(logTag, "datesButton[ = " + j + "] = " +  selectedDate[j]);

        hoursGridView = findViewById(R.id.hoursGridView);
        hoursGridView.setNumColumns(2);

        hoursAdapter = new HoursAdapter(this, hoursForDate);
        hoursGridView.setAdapter(hoursAdapter);


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
                        hoursAdapter.notifyDataSetChanged();
                        prepareHoursForDate(0);
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


        //dodanie do każdego z przycisków  metody setOnCheckedChangeListener

        Log.d(logTag, "\nStan wszystkich przycisków (przed kliknięciu dowolnego): ");
        for(int k =0; k<selectedDate.length; k++)
            Log.d(logTag, "Stan przed selectedDate[" + k + "]= " + selectedDate[k]);

        for (int i = 0; i < datesButtons.length; i++){
            final int finalI = i;
            datesButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Log.d(logTag, "Stan buttona = " + Boolean.toString(datesButtons[finalI].isChecked()));
                    Log.d(logTag, "Stan buttona = " + Boolean.toString(selectedDate[finalI]));

                    if (!selectedDate[finalI]) {
                        for(int j =0; j<selectedDate.length; j++) {
                            if (j != finalI) {
                                selectedDate[j] = false;
                                Log.d(logTag, "selectedDate[" + j + "] = " + selectedDate[j]);
                                datesButtons[j].setBackgroundResource(R.drawable.normal_date_button);
                                datesButtons[j].setTextColor(Color.BLACK);
                            }
                        }
                        selectedDate[finalI] = true;
                        datesButtons[finalI].setBackgroundResource(R.drawable.gradient_date_button);
                        datesButtons[finalI].setTextColor(Color.WHITE);
                        updateDateButtons();

                        prepareHoursForDate(finalI);
                        //TODO notofyDataChanged do Adaptera obsługującego godziny
                        hoursAdapter.notifyDataSetChanged();
                    }
                    else {
                        updateDateButtons();
                        selectedDate[finalI] = true;
                    }

                    Log.d(logTag, "\nStan wszystkich przycisków (po kliknięciu dowolnego): ");
                    for(int k =0; k<selectedDate.length; k++)
                        Log.d(logTag, "Stan po selectedDate[" + k + "]= " + selectedDate[k]);


                }

            });

    }

    }
}
