package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.R;
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

public class ChooseDateTime implements RepertoireConnListener {
    private static final String logTag = ChooseDateTime.class.getSimpleName();
    private Context context;
    private RepertoireConnection repertoireConnection;
    private ErrorListener errorListener;
    private DateTimeListener dateTimeListener;
    private boolean[] selectedDate;

    private List<Schedule> scheduleList;
    private List<Integer> selectedSchedules;
    private List<Schedule> hoursForDate;

    private List<Schedule> uniqueDates;
    int movieId;


    public ChooseDateTime(Context context, int movieId, ErrorListener errorListener, DateTimeListener dateTimeListener){
        this.context = context;
        this.movieId = movieId;
        this.dateTimeListener = dateTimeListener;
        repertoireConnection = new RepertoireConnection(context, errorListener, this);
        repertoireConnection.getRepertoireForMovie(movieId);

        this.selectedSchedules = new ArrayList<>();

        this.selectedDate = new boolean[5];
        this.selectedDate[0] = true;
        for(int i=1; i<selectedDate.length;i++)
            selectedDate[i]=false;
    }


    @Override
    public void onDbResponseCallback(List<Schedule> scheduleList) {
        Log.d(logTag, "onDbResponseCallback");
        this.scheduleList = new ArrayList<>(scheduleList);
        this.hoursForDate = new ArrayList<>(scheduleList);

        for(Schedule schedule : scheduleList)
            Log.d(logTag,"scheduleList before prepare= " + schedule.toString());

        for(Schedule schedule : hoursForDate)
            Log.d(logTag,"hoursForDate = " + schedule.toString());

        for(Schedule schedule : scheduleList)
            Log.d(logTag,"scheduleList after prepare= " + schedule.toString());

        dateTimeListener.callbackOnSetUi();

    }

    public void prepareHoursForDate(int index) {
        if (hoursForDate.size() > 0) hoursForDate.clear();
        Log.d(logTag, "prepareHoursForDate przed forEach, scheduleList.size() = " + scheduleList.size());

        for (Schedule schedule : scheduleList) {
            Log.d(logTag, "schedule in prepare = " + schedule.toString());
            for(Schedule uniqueDate : uniqueDates)
                Log.d(logTag, "uniqueDates in prepare = " + uniqueDate.toString());

            if (schedule.getYear() == uniqueDates.get(index).getYear() && schedule.getMonth() == uniqueDates.get(index).getMonth()
                    && schedule.getDayOfMonth() == uniqueDates.get(index).getDayOfMonth()) {
                hoursForDate.add(schedule);
                Log.d(logTag, "Dodana wartość do hoursForDate = " + schedule.toString());
            }
        }

    }

    public void prepareDateButtons(){
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

        for (Schedule schedule : scheduleList) {
            int innerInc = 0;
            int index = 0;
            for (Schedule ud : uniqueDates) {

                if (schedule.getYear() == ud.getYear() && schedule.getMonth() == ud.getMonth()
                        && schedule.getDayOfMonth() == ud.getDayOfMonth())
                    innerInc++;

                if (innerInc >= 2 && schedule.getYear() == ud.getYear() && schedule.getMonth() == ud.getMonth()
                        && schedule.getDayOfMonth() == ud.getDayOfMonth()) {

                    if (dateIndexesToRemove.add(index))
                        Log.d(logTag, "Dodany index = " + index);
                }
                index++;
            }
        }

        if (dateIndexesToRemove.size() > 0)
            for (Integer i : dateIndexesToRemove)
                uniqueDates.remove(scheduleList.get(i));

        for (Schedule schedule : uniqueDates)
            Log.d(logTag, "Zawartość uniqueDates po filtrowaniu= " + schedule.toString());

        for (Schedule schedule : scheduleList)
            Log.d(logTag, "Zawartość scheduleList po filtrowaniu= " + schedule.toString());

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

    public void clearListOfSchedules(){
        if(selectedSchedules.size() > 0)
            selectedSchedules.clear();
    }
    public String hourAndMin(int position){
        String text = Integer.toString(hoursForDate.get(position).getHourOfDay());

        StringBuilder sB = new StringBuilder(text);
        sB.append(context.getString(R.string.colon));

        if(hoursForDate.get(position).getMinute() < 10)
            sB.append(context.getString(R.string.zero));

        sB.append(Integer.toString(hoursForDate.get(position).getMinute()));

        if(hoursForDate.get(position).getHourOfDay() < 10)
            sB.insert(0, context.getString(R.string.zero));

        return sB.toString();
    }

    public void markAnHour(int position, boolean isSelected){
        int objToRemove=-1;
        if(isSelected){
            Log.d(logTag, "Dodana wartość repertuaru = " + scheduleList.get(position).getId());
            selectedSchedules.add(hoursForDate.get(position).getId());
        }
        else{
            Log.d(logTag,"Usunięta wartość repertuaru = " + scheduleList.get(position).getId());
            objToRemove = hoursForDate.get(position).getId();
        }
        if(objToRemove>0){
            selectedSchedules.remove(Integer.valueOf(objToRemove));
        }
        Log.d(logTag, "Zawartość selected schedules: ");
        for(Integer i : selectedSchedules)
            Log.d(logTag, "Wartość selectedSchedules = " + i);
    }

    public void checkNumOfChoices(){
        if(selectedSchedules.size() == 0)
            dateTimeListener.callBackOnBadChoice(true);
        else if(selectedSchedules.size() > 1)
            dateTimeListener.callBackOnBadChoice(false);
        else
            dateTimeListener.callBackSuccess();

    }
    public boolean[] getSelectedDate() {
        return selectedDate;
    }

    public List<Schedule> getScheduleList() {
        return scheduleList;
    }

    public List<Schedule> getHoursForDate() {
        return hoursForDate;
    }

    public List<Schedule> getUniqueDates() {
        return uniqueDates;
    }
    public List<Integer> getSelectedSchedules() {
        return selectedSchedules;
    }

}
