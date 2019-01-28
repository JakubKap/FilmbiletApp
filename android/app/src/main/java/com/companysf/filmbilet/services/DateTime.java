package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.connection.Listener.DateTimeListener;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.Listener.RepertoireConnListener;
import com.companysf.filmbilet.connection.RepertoireConnection;
import com.companysf.filmbilet.entities.Repertoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DateTime implements RepertoireConnListener {
    private static final String logTag = DateTime.class.getSimpleName();
    private Context context;
    private ErrorListener errorListener;
    private DateTimeListener dateTimeListener;
    private boolean[] selectedDate;

    private List<Repertoire> repertoireList;
    private List<Integer> selectedRepertoires;
    private List<Repertoire> hoursForDate;

    private List<Repertoire> currentWeek;
    int movieId;


    public DateTime(Context context, int movieId, ErrorListener errorListener, DateTimeListener dateTimeListener){
        this.context = context;
        this.movieId = movieId;
        this.dateTimeListener = dateTimeListener;
        RepertoireConnection repertoireConnection = new RepertoireConnection(context, errorListener, this);
        repertoireConnection.getRepertoireForMovie(movieId);

        this.selectedRepertoires = new ArrayList<>();

        this.selectedDate = new boolean[5];
        this.selectedDate[0] = true;
        for(int i=1; i<selectedDate.length;i++)
            selectedDate[i]=false;
    }

    @Override
    public void onDbResponseCallback(List<Repertoire> repertoireList) {
        Log.d(logTag, "onDbResponseCallback");
        this.repertoireList = new ArrayList<>(repertoireList);
        this.hoursForDate = new ArrayList<>(repertoireList);

        for(Repertoire repertoire : this.repertoireList)
            Log.d(logTag,"repertoireList before prepare= " + repertoire.toString());

        for(Repertoire repertoire : hoursForDate)
            Log.d(logTag,"hoursForDate = " + repertoire.toString());

        for(Repertoire repertoire : this.repertoireList)
            Log.d(logTag,"repertoireList after prepare= " + repertoire.toString());

        dateTimeListener.callbackOnSetUi();

    }

    public void prepareHoursForDate(int index) {
        if (hoursForDate.size() > 0) hoursForDate.clear();
        Log.d(logTag, "prepareHoursForDate przed forEach, repertoireList.size() = " + repertoireList.size());

        for (Repertoire repertoire : repertoireList) {
            Log.d(logTag, "repertoire in prepare = " + repertoire.toString());
            for(Repertoire currRep : currentWeek)
                Log.d(logTag, "currentWeek in prepare = " + currRep.toString());

            if(repertoire.getDateFormat().isHourInDay(currentWeek.get(index).getDateFormat())){
                hoursForDate.add(repertoire);
                Log.d(logTag, "Dodana wartość do hoursForDate = " + repertoire.toString());
            }
        }

    }

    public void prepareDateButtons(){
        Collections.sort(repertoireList, new Comparator<Repertoire>() {
            @Override
            public int compare(Repertoire r1, Repertoire r2) {
                return r1.getDateFormat().getDate().compareTo(r2.getDateFormat().getDate());
            }
        });

        currentWeek = new ArrayList<>(repertoireList);
        Set<Integer> dateIndexesToRemove = new HashSet<>();

        for (Repertoire currRep : currentWeek)
            Log.d(logTag, "Zawartość currentWeek przed filtrowaniem= " + currRep.toString());

        for (Repertoire repertoire : repertoireList) {
            int innerInc = 0;
            int index = 0;
            for (Repertoire currRep : currentWeek) {

                if(repertoire.getDateFormat().isHourInDay(currRep.getDateFormat()))
                    innerInc++;

                if (innerInc >= 2 && repertoire.getDateFormat().isHourInDay(currRep.getDateFormat())) {

                    if (dateIndexesToRemove.add(index))
                        Log.d(logTag, "Dodany index = " + index);
                }
                index++;
            }
        }

        if (dateIndexesToRemove.size() > 0)
            for (Integer i : dateIndexesToRemove)
                currentWeek.remove(repertoireList.get(i));

        for (Repertoire repertoire : currentWeek)
            Log.d(logTag, "Zawartość currentWeek po filtrowaniu= " + repertoire.toString());

        for (Repertoire repertoire : repertoireList)
            Log.d(logTag, "Zawartość repertoireList po filtrowaniu= " + repertoire.toString());
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

    public void clearListOfRepertoires(){
        if(selectedRepertoires.size() > 0)
            selectedRepertoires.clear();
    }

    public void markAnHour(int position, boolean isSelected){
        int objToRemove=-1;
        if(isSelected){
            Log.d(logTag, "Dodana wartość repertuaru = " + repertoireList.get(position).getId());
            selectedRepertoires.add(hoursForDate.get(position).getId());
        }
        else{
            Log.d(logTag,"Usunięta wartość repertuaru = " + repertoireList.get(position).getId());
            objToRemove = hoursForDate.get(position).getId();
        }
        if(objToRemove>0){
            selectedRepertoires.remove(Integer.valueOf(objToRemove));
        }
        Log.d(logTag, "Zawartość selected repertoires: ");
        for(Integer i : selectedRepertoires)
            Log.d(logTag, "Wartość selectedRepertoires = " + i);
    }

    public void checkNumOfChoices(){
        if(selectedRepertoires.size() == 0)
            dateTimeListener.callBackOnBadChoice(true);
        else if(selectedRepertoires.size() > 1)
            dateTimeListener.callBackOnBadChoice(false);
        else
            dateTimeListener.callBackSuccess();
    }

    public boolean[] getSelectedDate() {
        return selectedDate;
    }

    public List<Repertoire> getHoursForDate() {
        return hoursForDate;
    }

    public List<Repertoire> getCurrentWeek() {
        return currentWeek;
    }
    public List<Integer> getSelectedRepertoires() {
        return selectedRepertoires;
    }
}
