package com.companysf.filmbilet.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.adapter.HoursAdapter;
import com.companysf.filmbilet.connection.Listener.DateTimeListener;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.services.DateTime;
import com.companysf.filmbilet.app.CustomVolleyRequest;
import com.companysf.filmbilet.entities.Movie;
import com.companysf.filmbilet.services.Schedule;

import java.io.Serializable;
import java.util.List;

import static com.companysf.filmbilet.utils.ToastUtils.showLongToast;

public class ChooseDateTimeActivity extends AppCompatActivity implements Serializable, ErrorListener, DateTimeListener {

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

        dateTime = new DateTime(this, movieId, this, this);
/*
        hoursAdapter = new HoursAdapter(this, dateTime);
        hoursGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        hoursGridView.setAdapter(hoursAdapter);*/

        for (int i = 0; i < datesButtons.length; i++) {
            final int finalI = i;
            datesButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(logTag, "Stan buttona przed= " + Boolean.toString(datesButtons[finalI].isChecked()));
                    if(!dateTime.getSelectedDate()[finalI]){
                        dateTime.chooseDate(finalI);

                        for(int j=0; j<dateTime.getSelectedDate().length;j++)
                            markSeat(datesButtons[j], j);

                        dateTime.prepareDateButtons();
                        //updateDateButtons();

                        Log.d(logTag, "Stan buttona po= " + Boolean.toString(datesButtons[finalI].isChecked()));
                        dateTime.prepareHoursForDate(finalI);
                        dateTime.clearListOfSchedules();
                        hoursAdapter.notifyDataSetChanged();

                    }
                    else{
                        updateDateButtons();
                        dateTime.getSelectedDate()[finalI] = true;
                    }
                    Log.d(logTag, "\nStan wszystkich przycisków (po kliknięciu dowolnego): ");
                    for (int k = 0; k < dateTime.getSelectedDate().length; k++)
                        Log.d(logTag, "Stan po selectedDate[" + k + "]= " + dateTime.getSelectedDate()[k]);

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
    public void markSeat(ToggleButton button, int index){
        final ToggleButton finalButton = button;
        final int finalIndex = index;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!dateTime.getSelectedDate()[finalIndex]) {
                    Log.d(logTag, "normal button index = " + finalIndex);
                    finalButton.setBackgroundResource(R.drawable.normal_date_button);
                    finalButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));

                } else {
                    Log.d(logTag, "choosed button index = " + finalIndex);
                    finalButton.setBackgroundResource(R.drawable.gradient_date_button);
                    finalButton.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                }
            }
        });
    }

    public void updateDateButtons(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
            List<Schedule> uniqueDates = dateTime.getUniqueDates();
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
    @Override
    public void callbackOnSetUi() {
        Log.d(logTag, "callbackOnSetUi");
        hoursAdapter = new HoursAdapter(getApplicationContext(), dateTime);

        GridView hoursGridView = findViewById(R.id.hoursGridView);
        hoursGridView.setNumColumns(2);

        dateTime.prepareDateButtons();
        updateDateButtons();

        dateTime.prepareHoursForDate(0);
        hoursGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        hoursGridView.setAdapter(hoursAdapter);

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
