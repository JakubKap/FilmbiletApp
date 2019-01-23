package com.companysf.filmbilet.Connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.Activity.MainActivity;
import com.companysf.filmbilet.Adapter.MoviesListAdapter;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Entities.Movie;
import com.companysf.filmbilet.Entities.MovieList;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.Utilies.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static com.android.volley.Request.Method.GET;

public class MovieConnection {
    private Context context;
    private ConnectionDetector cd;
    private ServerConnectionListener serverConnectionListener;
    private MovieList movieList;
    private MoviesListAdapter adapter;
    private static final String logTag = MainActivity.class.getSimpleName();

    public MovieConnection(Context context, ServerConnectionListener serverConnectionListener, MovieList movieList, MoviesListAdapter adapter) {
        this.context = context;
        this.serverConnectionListener = serverConnectionListener;
        this.movieList = movieList;
        this.adapter = adapter;
        this.cd = new ConnectionDetector(context);
    }

    public void updateDataFromServer(boolean manualSwipeRefresh) {
        if (cd.connected()) {
            Log.d(logTag, "Jest polaczenie inter");
            StringRequest stringRequest = new StringRequest(
                    GET,
                    AppConfig.GET_MOVIES_FROM_REPERTOIRE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, "get repertoire movies request");
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean(context.getString(R.string.error));
                                if (error) {
                                    Log.d(logTag, "Error on getting data from API Service");
                                    serverConnectionListener.callBackOnError();
                                } else {
                                    serverConnectionListener.callBackOnSuccess();

                                    JSONArray moviesJson = json.getJSONArray(context.getString(R.string.moviesFromRepertoireJsonName));
                                    movieList.getList().clear();

                                    for (int i = 0; i < moviesJson.length(); i++) {
                                        Log.d(logTag, "moviesJsonLOG " + moviesJson.length());
                                        JSONObject movieJSON = moviesJson.getJSONObject(i);
                                        Movie movie = new Movie(
                                                movieJSON.getInt(context.getString(R.string.jsonMovieFromRepertoireId)),
                                                movieJSON.getString(context.getString(R.string.jsonMovieFromRepertoireTitle)),
                                                movieJSON.getInt(context.getString(R.string.jsonMovieFromRepertoireRunningTimeMin)),
                                                movieJSON.getInt(context.getString(R.string.jsonMovieFromRepertoireAge)),
                                                movieJSON.getString(context.getString(R.string.jsonMovieFromRepertoirePictureUrl)),
                                                movieJSON.getString(context.getString(R.string.jsonMovieFromRepertoireGenres))
                                        );
                                        movieList.getList().add(movie);
                                    }
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                serverConnectionListener.callBackOnError();
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d(logTag, "Error on response");
                            serverConnectionListener.callBackOnError();
                        }
                    }
            );
            AppController.getInstance().addToRequestQueue(stringRequest);
        } else {
            serverConnectionListener.callBackOnNoNetwork();
            Log.d(logTag, "Brak polaczenia inter");
        }
        serverConnectionListener.callBackOnEndOfFetchingData(manualSwipeRefresh);
    }

    public void updateDataFromServer() {
        updateDataFromServer(false);
    }
}
