package com.companysf.filmbilet.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.companysf.filmbilet.addition.ErrorDetector;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.app.CustomVolleyRequest;
import com.companysf.filmbilet.appLogic.Movie;
import com.companysf.filmbilet.appLogic.Schedule;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChooseDateTime extends AppCompatActivity implements Serializable {

    private static final String logTag = ChooseDateTime.class.getSimpleName();
    private List<Schedule> scheduleList = new ArrayList<>();
    private List<Schedule> uniqueDates;
    private List<Schedule> hoursForDate = new ArrayList<>();
    private HoursAdapter hoursAdapter;
    private int movieId;
    private ToggleButton[] datesButtons = new ToggleButton[5];
    private boolean[] selectedDate = new boolean[5];
    private ErrorDetector ed;


    public void updateMovieInfo(Movie sentMovie) {

        final Movie movie = sentMovie;
        final ImageLoader imageLoader;
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Views
                TextView title = findViewById(R.id.title_in_date_hours);
                TextView movieLength = findViewById(R.id.movie_length_in_date_hours);
                TextView minAge = findViewById(R.id.min_age_in_date_hours);
                NetworkImageView picture = findViewById(R.id.picture_in_date);
                TextView genres = findViewById(R.id.genres_in_date_hours);

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

    public void updateDateButtons() {

        //sortowanie elementów w kolekcji (zgodnie z kolejnością dat)
        Collections.sort(scheduleList, new Comparator<Schedule>() {
            @Override
            public int compare(Schedule r1, Schedule r2) {
                return r1.getDate().compareTo(r2.getDate());
            }
        });

        uniqueDates = new ArrayList<>(scheduleList);
        Set<Integer> dateIndexesToRemove = new HashSet<>();

        for (Schedule r : uniqueDates)
            Log.d(logTag, "Zawartość uniqueDates przed filtrowaniem= " + r.toString());
        //wstawienie do kolekcji uniqueDates unikalnych dat z Schedule List


        for (Schedule r : scheduleList) {
            int innerInc = 0;
            int index = 0;
            for (Schedule ud : uniqueDates) {

                if (r.getYear() == ud.getYear() && r.getMonth() == ud.getMonth()
                        && r.getDayOfMonth() == ud.getDayOfMonth())
                    innerInc++;

                if (innerInc >= 2 && r.getYear() == ud.getYear() && r.getMonth() == ud.getMonth()
                        && r.getDayOfMonth() == ud.getDayOfMonth()) {

                    if (dateIndexesToRemove.add(index))
                        Log.d(logTag, "Dodany index = " + index);
                }
                index++;
            }
        }

        if (dateIndexesToRemove.size() > 0)
            for (Integer i : dateIndexesToRemove)
                uniqueDates.remove(scheduleList.get(i));

        for (Schedule r : uniqueDates)
            Log.d(logTag, "Zawartość uniqueDates po filtrowaniu= " + r.toString());


        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                for (Schedule r : uniqueDates) {
                    String text = Integer.toString(r.getDayOfMonth());
                    text = text + "\n" + r.getDayOfWeek();
                    datesButtons[i].setText(text);
                    datesButtons[i].setTextOn(text);
                    datesButtons[i].setTextOff(text);
                    i++;
                }
            }
        });

    }

    public void prepareHoursForDate(int index) {

        if (hoursForDate.size() > 0) hoursForDate.clear();

        for (Schedule r : scheduleList) {
            if (r.getYear() == uniqueDates.get(index).getYear() && r.getMonth() == uniqueDates.get(index).getMonth()
                    && r.getDayOfMonth() == uniqueDates.get(index).getDayOfMonth()) {
                hoursForDate.add(r);
                Log.d(logTag, "Dodana wartość do hoursForDate = " + r.toString());
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(getString(R.string.movie));
        movieId = movie.getId();

        updateMovieInfo(movie);

        datesButtons[0] = findViewById(R.id.toggleButton1);
        //domyślnie pierwszty element zawsze na początku zaznaczony
        selectedDate[0] = true;
        datesButtons[1] = findViewById(R.id.toggleButton2);
        datesButtons[2] = findViewById(R.id.toggleButton3);
        datesButtons[3] = findViewById(R.id.toggleButton4);
        datesButtons[4] = findViewById(R.id.toggleButton5);

        Button btnAcceptTime = findViewById(R.id.btn_accept_time);

        for (int i = 1; i < selectedDate.length; i++)
            selectedDate[i] = false;


        for (int j = 0; j < selectedDate.length; j++)
            Log.d(logTag, "datesButton[ = " + j + "] = " + selectedDate[j]);

        GridView hoursGridView = findViewById(R.id.hoursGridView);
        hoursGridView.setNumColumns(2);

        hoursAdapter = new HoursAdapter(this, hoursForDate);
        hoursGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        hoursGridView.setAdapter(hoursAdapter);

        //fonts
        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));

        TextView stepNumber1 = findViewById(R.id.stepNumber1);
        stepNumber1.setTypeface(opensansBold);

        TextView selectSectorText = findViewById(R.id.selectSectorText);
        selectSectorText.setTypeface(opensansBold);

        TextView title_in_date_hours =findViewById(R.id.title_in_date_hours);
        title_in_date_hours.setTypeface(opensansBold);

        TextView genres_text_hours = findViewById(R.id.genres_text_hours);
        genres_text_hours.setTypeface(opensansRegular);

        TextView genres_in_date_hours = findViewById(R.id.genres_in_date_hours);
        genres_in_date_hours.setTypeface(opensansItalic);

        TextView movie_length_text_hours = findViewById(R.id.movie_length_text_hours);
        movie_length_text_hours.setTypeface(opensansRegular);

        TextView movie_length_in_date_hours = findViewById(R.id.movie_length_in_date_hours);
        movie_length_in_date_hours.setTypeface(opensansItalic);

        TextView separator_hours = findViewById(R.id.separator_hours);
        separator_hours.setTypeface(opensansRegular);

        TextView min_age_text_hours = findViewById(R.id.min_age_text_hours);
        min_age_text_hours.setTypeface(opensansRegular);

        TextView min_age_in_date_hours = findViewById(R.id.min_age_in_date_hours);
        min_age_in_date_hours.setTypeface(opensansItalic);

        ed = new ErrorDetector(this);
        //pobranie informacji o repertuarze dla danego filmu z repertuaru
        //final int movieId = 1;

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.GET_MOVIE_REPERTOIRE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, getString(R.string.getMovieRep)+ response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean(getString(R.string.error));
                            if (error) {
                                Toast.makeText(
                                        getApplicationContext(),
                                        json.getString(getString(R.string.message)),
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                JSONArray schedulesJson = json.getJSONArray(getString(R.string.repertoire));
                                for (int i = 0; i < schedulesJson.length(); i++) {
                                    Log.d(logTag, getString(R.string.schedJsonLog) + schedulesJson.length());
                                    JSONObject scheduleJSON = schedulesJson.getJSONObject(i);
                                    Schedule schedule = new Schedule(
                                            scheduleJSON.getInt(getString(R.string.id)),
                                            scheduleJSON.getString(getString(R.string.date))
                                    );

                                    scheduleList.add(schedule);
                                    Log.d(logTag, "Pobrany Schedule z BD= " + schedule.toString());

                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(
                                    getApplicationContext(),
                                    getString(R.string.jsonError) + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }


                        for (Schedule r : scheduleList)
                            Log.d(logTag, "Zawartość scheduleList po pobraniu danych z BD = " + r.toString());

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
                Log.e(logTag, getString(R.string.getMovieRepError) + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(getString(R.string.movieId), Integer.toString(movieId));
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, getString(R.string.registerRequestAdd));


        //dodanie do każdego z przycisków  metody setOnCheckedChangeListener

        Log.d(logTag, "\nStan wszystkich przycisków (przed kliknięciu dowolnego): ");
        for (int k = 0; k < selectedDate.length; k++)
            Log.d(logTag, "Stan przed selectedDate[" + k + "]= " + selectedDate[k]);

        for (int i = 0; i < datesButtons.length; i++) {
            final int finalI = i;
            datesButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Log.d(logTag, "Stan buttona = " + Boolean.toString(datesButtons[finalI].isChecked()));
                    Log.d(logTag, "Stan buttona = " + Boolean.toString(selectedDate[finalI]));

                    if (!selectedDate[finalI]) {
                        for (int j = 0; j < selectedDate.length; j++) {
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
                        hoursAdapter.clearListOfSchedules();
                        hoursAdapter.notifyDataSetChanged();
                    } else {
                        updateDateButtons();
                        selectedDate[finalI] = true;
                    }

                    Log.d(logTag, "\nStan wszystkich przycisków (po kliknięciu dowolnego): ");
                    for (int k = 0; k < selectedDate.length; k++)
                        Log.d(logTag, "Stan po selectedDate[" + k + "]= " + selectedDate[k]);


                }

            });


        }

        btnAcceptTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pobranie zawartość repertuaru
                List<Integer> schedules = hoursAdapter.getSelectedSchedules();
                for (Integer i : schedules)
                    Log.d(logTag, "Pobrana wartość repertuaru = " + i);

                //nie wybrano żadnego repertuaru
                if (schedules.size() == 0)
                    ed.buildDialog(ChooseDateTime.this, getString(R.string.noChoosedHourTitle),
                            getString(R.string.wrongNumOfHours)).show();
                else if (schedules.size() > 1)
                    //wybrano > 2 repertuary
                    ed.buildDialog(ChooseDateTime.this, getString(R.string.wrongChoosedHourTitle),
                            getString(R.string.wrongNumOfHours)).show();

                else {
                    //wybrano 1 repertuar - przejście do kolejnego activity z wyborem sektora

                    int scheduleId = schedules.get(0);
                    Intent intent = new Intent(ChooseDateTime.this, SectorActivity.class);
                    intent.putExtra(getString(R.string.scheduleId), scheduleId);
                    startActivity(intent);

                }


            }
        });
    }
}
