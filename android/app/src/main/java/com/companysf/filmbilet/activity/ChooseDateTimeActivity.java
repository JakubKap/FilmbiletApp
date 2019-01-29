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
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.connection.Listener.DateTimeListener;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.entities.Repertoire;
import com.companysf.filmbilet.services.ChooseDateTime;
import com.companysf.filmbilet.entities.Movie;
import com.companysf.filmbilet.services.Login;

import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

import static com.companysf.filmbilet.utils.ToastUtils.showLongToast;

public class ChooseDateTimeActivity extends AppCompatActivity implements Serializable, ErrorListener, DateTimeListener {

    private static final String logTag = ChooseDateTimeActivity.class.getSimpleName();
    private Login login;
    private int movieId;
    private HoursAdapter hoursAdapter;
    private ToggleButton[] datesButtons = new ToggleButton[5];
    private ImageLoader imageLoader;
    ChooseDateTime chooseDateTime;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date_time);

        login = new Login(this);
        if (!login.userIsLoggedIn()) {
            switchToLoginActivity();
        }

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(getString(R.string.movie));
        movieId = movie.getId();

        settingMovieInfo(movie);

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

        TextView titleInDateHours = findViewById(R.id.titleInDateHours);
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

        builder = new AlertDialog.Builder(this);

        chooseDateTime = new ChooseDateTime(this, movieId, this, this);

        for (int i = 0; i < datesButtons.length; i++) {
            final int finalI = i;
            datesButtons[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Log.d(logTag, "Stan buttona przed= " + Boolean.toString(datesButtons[finalI].isChecked()));
                    if (!chooseDateTime.getSelectedDate()[finalI]) {
                        chooseDateTime.chooseDate(finalI);

                        for (int j = 0; j < chooseDateTime.getSelectedDate().length; j++)
                            markSeat(datesButtons[j], j);

                        chooseDateTime.prepareDateButtons();

                        Log.d(logTag, "Stan buttona po= " + Boolean.toString(datesButtons[finalI].isChecked()));
                        chooseDateTime.prepareHoursForDate(finalI);
                        chooseDateTime.clearListOfRepertoires();
                        hoursAdapter.notifyDataSetChanged();

                    } else {
                        updateDateButtons();
                        chooseDateTime.getSelectedDate()[finalI] = true;
                    }
                    Log.d(logTag, "\nStan wszystkich przycisków (po kliknięciu dowolnego): ");
                    for (int k = 0; k < chooseDateTime.getSelectedDate().length; k++)
                        Log.d(logTag, "Stan po selectedDate[" + k + "]= " + chooseDateTime.getSelectedDate()[k]);

                }

            });
        }

        btnAcceptTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDateTime.checkNumOfChoices();
            }
        });
    }

    private void switchToLoginActivity() {
        Intent intent = new Intent(ChooseDateTimeActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void settingMovieInfo(Movie sentMovie) {
        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        TextView title = findViewById(R.id.titleInDateHours);
        TextView movieLength = findViewById(R.id.movieLengthInDateHours);
        TextView minAge = findViewById(R.id.minAgeInDateHours);
        NetworkImageView picture = findViewById(R.id.pictureInDate);
        TextView genres = findViewById(R.id.genresInDateHours);

        Log.d(logTag, "Pozytywnie odnaleziono elementy opisujące film");
        title.setText(sentMovie.getTitle());
        movieLength.setText(String.valueOf(sentMovie.getRunningTimeMin()));
        minAge.setText(String.valueOf(sentMovie.getAge()));
        Log.d(logTag, "Przed ustawieniem obrazka");
        picture.setImageUrl(sentMovie.getPictureURL(), imageLoader);
        Log.d(logTag, "Po ustawieniu obrazka");
        genres.setText(sentMovie.getGenres());
        Log.d(logTag, "Po ustawieniu gatunku");
    }

    public void markSeat(ToggleButton button, int index) {
        final ToggleButton finalButton = button;
        final int finalIndex = index;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!chooseDateTime.getSelectedDate()[finalIndex]) {
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

    public void updateDateButtons() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Repertoire> uniqueDates = chooseDateTime.getCurrentWeek();
                int i = 0;
                for (Repertoire repertoire : uniqueDates) {
                    String text = Integer.toString(repertoire.getDateFormat().getDate().get(Calendar.DAY_OF_MONTH));
                    text = text + "\n" + repertoire.getDateFormat().dayOfWeek();
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
        hoursAdapter = new HoursAdapter(getApplicationContext(), chooseDateTime);

        GridView hoursGridView = findViewById(R.id.hoursGridView);
        hoursGridView.setNumColumns(2);

        chooseDateTime.prepareDateButtons();
        updateDateButtons();

        chooseDateTime.prepareHoursForDate(0);
        hoursGridView.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
        hoursGridView.setAdapter(hoursAdapter);

    }

    @Override
    public void callBackOnBadChoice(boolean noChoice) {
        if (noChoice)
            showDialog(getString(R.string.noChoosedHourTitle),
                    getString(R.string.wrongNumOfHours)
            );
        else
            showDialog(getString(R.string.wrongChoosedHourTitle),
                    getString(R.string.wrongNumOfHours)
            );
    }

    @Override
    public void callBackSuccess() {
        int repertoireId = chooseDateTime.getSelectedRepertoires().get(0);
        Log.d(logTag, "Końcowy repertoireId " + repertoireId);
        Intent intent = new Intent(ChooseDateTimeActivity.this, SectorActivity.class);
        intent.putExtra(getString(R.string.repertoireId), repertoireId);
        startActivity(intent);
    }

    public void showDialog(String title, String message) {
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
