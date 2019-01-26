package com.companysf.filmbilet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
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
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.services.DateTime;
import com.companysf.filmbilet.utils.ErrorDetector;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.app.CustomVolleyRequest;
import com.companysf.filmbilet.entities.Movie;
import com.companysf.filmbilet.services.Schedule;

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

import static com.companysf.filmbilet.utils.ToastUtils.showLongToast;

public class ChooseDateTimeActivity extends AppCompatActivity implements Serializable, ErrorListener {

    private static final String logTag = ChooseDateTimeActivity.class.getSimpleName();
    private int movieId;
    private HoursAdapter hoursAdapter;
    private ToggleButton[] datesButtons = new ToggleButton[5];
    DateTime dateTime;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(getString(R.string.movie));
        movieId = movie.getId();

        updateMovieInfo(movie);

        datesButtons[0] = findViewById(R.id.toggleButton1);
        datesButtons[1] = findViewById(R.id.toggleButton2);
        datesButtons[2] = findViewById(R.id.toggleButton3);
        datesButtons[3] = findViewById(R.id.toggleButton4);
        datesButtons[4] = findViewById(R.id.toggleButton5);

        Button btnAcceptTime = findViewById(R.id.btnAcceptTime);

        GridView hoursGridView = findViewById(R.id.hoursGridView);
        hoursGridView.setNumColumns(2);

        Typeface opensansRegular = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansRegular));
        Typeface opensansBold = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansBold));
        Typeface opensansItalic = Typeface.createFromAsset(getAssets(), getString(R.string.opensSansItalic));

        TextView stepNumber1 = findViewById(R.id.stepNumber1);
        stepNumber1.setTypeface(opensansBold);

        TextView selectSectorText = findViewById(R.id.selectSectorText);
        selectSectorText.setTypeface(opensansBold);

        TextView titleInDateHours =findViewById(R.id.titleInDateHours);
        titleInDateHours.setTypeface(opensansBold);

        TextView genresTextHours = findViewById(R.id.genresTextHours);
        genresTextHours.setTypeface(opensansRegular);

        TextView genresInDateHours = findViewById(R.id.genresInDateHours);
        genresInDateHours.setTypeface(opensansItalic);

        TextView movieLengthTextHours = findViewById(R.id.movieLengthTextHours);
        movieLengthTextHours.setTypeface(opensansRegular);

        TextView movieLengthInDateHours = findViewById(R.id.movieLengthInDateHours);
        movieLengthInDateHours.setTypeface(opensansItalic);

        TextView separatorHours = findViewById(R.id.separatorHours);
        separatorHours.setTypeface(opensansRegular);

        TextView minAgeTextHours = findViewById(R.id.minAgeTextHours);
        minAgeTextHours.setTypeface(opensansRegular);

        TextView minAgeInDateHours = findViewById(R.id.minAgeInDateHours);
        minAgeInDateHours.setTypeface(opensansItalic);

        builder =  new AlertDialog.Builder(this);

        dateTime = new DateTime(this, movieId, this);
/*
        hoursAdapter = new HoursAdapter(this, dateTime);
        hoursGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        hoursGridView.setAdapter(hoursAdapter);*/


        for (int i = 0; i < datesButtons.length; i++) {
            final int finalI = i;
            datesButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //Log.d(logTag, "Stan buttona = " + Boolean.toString(datesButtons[finalI].isChecked()));


                }

            });
        }

        btnAcceptTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }

    public void updateMovieInfo(Movie sentMovie) {
        final Movie movie = sentMovie;
        final ImageLoader imageLoader;
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                //Views
                TextView title = findViewById(R.id.titleInDateHours);
                TextView movieLength = findViewById(R.id.movieLengthInDateHours);
                TextView minAge = findViewById(R.id.minAgeInDateHours);
                NetworkImageView picture = findViewById(R.id.pictureInDate);
                TextView genres = findViewById(R.id.genresInDateHours);

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
    public void callBackOnError() {
        showLongToast(this, getString(R.string.serverErrorTitle));

    }

    @Override
    public void callBackOnNoNetwork() {
        showDialog(
                getString(R.string.networkConnectionErrorTitle),
                getString(R.string.loginNetworkConnectionErrorMsg)
        );
    }

    public void showDialog(String title, String message){
        final String finalTitle = title;
        final String finalMessage = message;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                builder.setTitle(finalTitle);
                builder.setMessage(finalMessage);
                builder.setPositiveButton(getString(R.string.dialogPositiveBtnText),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builder.show();
            }
        });
    }


}
