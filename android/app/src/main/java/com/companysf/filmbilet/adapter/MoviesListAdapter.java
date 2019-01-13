package com.companysf.filmbilet.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.appLogic.Movie;

import org.w3c.dom.Text;

import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private final Activity activity;
    private final List<Movie> moviesList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;

    //font
    private Typeface opensansRegular;
    private Typeface opensansBold;
    private Typeface opensansItalic;

    public CustomListAdapter(Activity activity, List<Movie>moviesList,
                             Typeface opensansRegular, Typeface opensansBold, Typeface opensansItalic) {
        this.activity = activity;
        this.moviesList = moviesList;
        this.opensansRegular = opensansRegular;
        this.opensansBold = opensansBold;
        this.opensansItalic = opensansItalic;
    }

    @Override
    public int getCount() {
        return moviesList.size();
    }

    @Override
    public Object getItem(int position) {
        return moviesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null){
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.movies_list_row, null);
        }
        if (imageLoader == null){
            imageLoader = AppController.getInstance().getImageLoader();
        }

        Movie movie = moviesList.get(position);

        //Views
        TextView title = convertView.findViewById(R.id.title);
        TextView movieLength = convertView.findViewById(R.id.movie_length);
        TextView minAge = convertView.findViewById(R.id.min_age);
        NetworkImageView picture = convertView.findViewById(R.id.picture);
        TextView genres = convertView.findViewById(R.id.genres);
        TextView genresText = convertView.findViewById(R.id.genres_text);
        TextView movieLengthText = convertView.findViewById(R.id.movie_length_text);
        TextView separator = convertView.findViewById(R.id.separator);
        TextView minAgeText = convertView.findViewById(R.id.min_age_text);

        //set views text
        title.setText(movie.getTitle());
        movieLength.setText(String.valueOf(movie.getRunningTimeMin()));
        minAge.setText(String.valueOf(movie.getAge()));
        picture.setImageUrl(movie.getPictureURL(), imageLoader);
        genres.setText(movie.getGenres());

        //font
        title.setTypeface(opensansBold);
        genresText.setTypeface(opensansRegular);
        genres.setTypeface(opensansItalic);
        movieLengthText.setTypeface(opensansRegular);
        movieLength.setTypeface(opensansItalic);
        separator.setTypeface(opensansRegular);
        minAgeText.setTypeface(opensansRegular);
        minAge.setTypeface(opensansItalic);

        return convertView;
    }
}
