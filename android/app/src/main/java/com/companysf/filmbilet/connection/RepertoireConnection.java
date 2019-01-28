package com.companysf.filmbilet.connection;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.R;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.Listener.RepertoireConnListener;
import com.companysf.filmbilet.connection.Listener.ReservationConnListener;
import com.companysf.filmbilet.services.Schedule;
import com.companysf.filmbilet.utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RepertoireConnection {

    private static final String logTag = ReservationConnection.class.getSimpleName();
    private Context mContext;
    private ErrorListener errorListener;
    private RepertoireConnListener repertoireConnListener;
    private ConnectionDetector cd;

    public RepertoireConnection(Context c, ErrorListener errorListener, RepertoireConnListener repertoireConnListener) {
        this.mContext = c;
        this.errorListener = errorListener;
        this.repertoireConnListener = repertoireConnListener;
        this.cd = new ConnectionDetector(c);
    }

    public void getRepertoireForMovie(int movieId) {
        if (cd.connected()) {
            Log.d(logTag, "Jest polaczenie inter");
            final int finalMovieId = movieId;

            final List<Schedule> scheduleList = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    AppConfig.GET_MOVIE_REPERTOIRE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(logTag, mContext.getString(R.string.getMovieRep)+ response);
                            try {
                                JSONObject json = new JSONObject(response);
                                boolean error = json.getBoolean(mContext.getString(R.string.error));
                                if (error) {
                                    errorListener.callBackOnError();
                                } else {
                                    JSONArray schedulesJson = json.getJSONArray(mContext.getString(R.string.repertoire));
                                    for (int i = 0; i < schedulesJson.length(); i++) {
                                        Log.d(logTag, mContext.getString(R.string.schedJsonLog) + schedulesJson.length());
                                        JSONObject scheduleJSON = schedulesJson.getJSONObject(i);
                                        Schedule schedule = new Schedule(
                                                scheduleJSON.getInt(mContext.getString(R.string.id)),
                                                scheduleJSON.getString(mContext.getString(R.string.date))
                                        );

                                        scheduleList.add(schedule);
                                        Log.d(logTag, "Pobrany Schedule z BD= " + schedule.toString());

                                    }
                                }
                            } catch (JSONException e) {
                                errorListener.callBackOnError();
                            }


                            for (Schedule r : scheduleList)
                                Log.d(logTag, "Zawartość scheduleList po pobraniu danych z BD = " + r.toString());


                            repertoireConnListener.onDbResponseCallback(scheduleList);
                            /*
                            //zaktulizowanie wyglądu ToggleButton'ów związanych z datą i dniem
                            updateDateButtons();
                            //zaktulizowanie wyglądu ToggleButton'ów związanych z godziną
                            hoursAdapter.notifyDataSetChanged();
                            prepareHoursForDate(0);
                            Log.d(logTag, "Po zaktualizowaniu");*/

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(logTag, mContext.getString(R.string.getMovieRepError) + error.getMessage());
                    errorListener.callBackOnError();
                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put(mContext.getString(R.string.movieId), Integer.toString(finalMovieId));
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(stringRequest, mContext.getString(R.string.registerRequestAdd));


        } else {
            errorListener.callBackOnNoNetwork();
            Log.d(logTag, "Brak polaczenia inter");
        }
    }
}