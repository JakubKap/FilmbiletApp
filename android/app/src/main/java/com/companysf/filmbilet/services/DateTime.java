package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.connection.Listener.DateTimeListener;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.Listener.RepertoireConnListener;
import com.companysf.filmbilet.connection.RepertoireConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateTime implements RepertoireConnListener {
    private static final String logTag = DateTime.class.getSimpleName();
    private Context context;
    private RepertoireConnection repertoireConnection;
    private ErrorListener errorListener;
    private DateTimeListener dateTimeListener;
    private boolean[] selectedDate;

    private List<Schedule> scheduleList;
    private List<Schedule> hoursForDate;
    private List<Schedule> uniqueDates;
    int movieId;

    public DateTime(Context context, int movieId, ErrorListener errorListener, DateTimeListener dateTimeListener){
        this.context = context;
        this.movieId = movieId;
        this.dateTimeListener = dateTimeListener;
        repertoireConnection = new RepertoireConnection(context, errorListener, this);
        repertoireConnection.getRepertoireForMovie(movieId);

        this.selectedDate = new boolean[5];
        this.selectedDate[0] = true;
        for(int i=1; i<selectedDate.length;i++)
            selectedDate[i]=false;
    }


    @Override
    public void onDbResponseCallback(List<Schedule> scheduleList) {
        Log.d(logTag, "onDbResponseCallback");
        this.scheduleList = scheduleList;
        this.hoursForDate = scheduleList;

        for(Schedule schedule : scheduleList)
            Log.d(logTag,"schedule = " + schedule.toString());

        dateTimeListener.callbackOnSetUi();

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


    public void prepareDateButtons(){
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


        /*runOnUiThread(new Runnable() {
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
        });*/
    }
    public void chooseDate(int index){
        for(int i =0; i<selectedDate.length; i++){
            if(i!=index){
                selectedDate[i] = false;
                Log.d(logTag, "selectedDate[" + i + "] = " + selectedDate[i]);
            }
        }
        selectedDate[index]=true;
        Log.d(logTag, "selectedDate[" + index + "] = " + selectedDate[index]);
    }

    public boolean[] getSelectedDate() {
        return selectedDate;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public List<Schedule> getUniqueDates() {
        return uniqueDates;
    }
}
