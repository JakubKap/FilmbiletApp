package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.activity.ChooseDateTimeActivity;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.Listener.RepertoireConnListener;
import com.companysf.filmbilet.connection.RepertoireConnection;

import java.util.List;

public class DateTime implements RepertoireConnListener {
    private static final String logTag = DateTime.class.getSimpleName();
    private Context context;
    private RepertoireConnection repertoireConnection;
    private ErrorListener errorListener;
    private List<Schedule> scheduleList;
    int movieId;

    public DateTime(Context context, int movieId, ErrorListener errorListener){
        this.movieId = movieId;
        repertoireConnection = new RepertoireConnection(context, errorListener, this);
        repertoireConnection.getRepertoireForMovie(movieId);
    }


    @Override
    public void onDbResponseCallback(List<Schedule> scheduleList) {
        Log.d(logTag, "onDbResponseCallback");
        this.scheduleList = scheduleList;

        for(Schedule schedule : scheduleList)
            Log.d(logTag,"schedule = " + schedule.toString());

    }
}
