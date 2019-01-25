package com.companysf.filmbilet.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.companysf.filmbilet.R;
// TODO odkomentowac
//import com.companysf.filmbilet.Activity.ChooseDateTime;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.entities.Movie;

import java.util.List;

public class MoviesListAdapter extends BaseAdapter {
    private final Activity activity;
    private Context context;
    private final List<Movie> moviesList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private Typeface opensansRegular;
    private Typeface opensansBold;
    private Typeface opensansItalic;

    public MoviesListAdapter(Activity activity, Context context, List<Movie>moviesList,
                             Typeface opensansRegular, Typeface opensansBold, Typeface opensansItalic) {
        this.activity = activity;
        this.context = context;
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

        final Movie movie = moviesList.get(position);

        TextView title = convertView.findViewById(R.id.title);
        TextView movieLength = convertView.findViewById(R.id.movieLength);
        TextView minAge = convertView.findViewById(R.id.minAge);
        NetworkImageView picture = convertView.findViewById(R.id.picture);
        TextView genres = convertView.findViewById(R.id.genres);
        TextView genresText = convertView.findViewById(R.id.genresText);
        TextView movieLengthText = convertView.findViewById(R.id.movieLengthText);
        TextView separator = convertView.findViewById(R.id.separator);
        TextView minAgeText = convertView.findViewById(R.id.minAgeText);
        RelativeLayout movieRow = convertView.findViewById(R.id.movieRow);

        title.setText(movie.getTitle());
        movieLength.setText(String.valueOf(movie.getRunningTimeMin()));
        minAge.setText(String.valueOf(movie.getAge()));
        picture.setImageUrl(movie.getPictureURL(), imageLoader);
        genres.setText(movie.getGenres());

        movieRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO odkomentowac
//                Intent intent = new Intent(context,ChooseDateTime.class);
//                intent.putExtra(context.getString(R.string.movieIntentExtraContentName), movie);
//                context.startActivity(intent);
            }
        });

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
