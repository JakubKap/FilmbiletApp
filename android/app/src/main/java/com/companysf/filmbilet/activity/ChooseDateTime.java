package com.companysf.filmbilet.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.app.CustomVolleyRequest;
import com.companysf.filmbilet.appLogic.Movie;

public class ChooseDateTime extends AppCompatActivity {

    private static final String logTag = MainActivity.class.getSimpleName();

    public void updateMovieInfo(Movie sentMovie){

        final Movie movie = sentMovie;
        final ImageLoader imageLoader;
        imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                .getImageLoader();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {


               // imageLoader = CustomVolleyRequest.getInstance(this.getApplicationContext())
                       // .getImageLoader();
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
        updateMovieInfo(new Movie("Planeta Singli 2", 45, 12, "http://filmbilet.cba.pl/images/climax.jpg", "Horror"));

    }
}
