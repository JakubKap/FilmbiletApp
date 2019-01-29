package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.connection.Listener.DateTimeListener;
import com.companysf.filmbilet.connection.Listener.ErrorListener;
import com.companysf.filmbilet.connection.Listener.RepertoireConnListener;
import com.companysf.filmbilet.connection.RepertoireConnection;
import com.companysf.filmbilet.entities.DateTimeChoice;
import com.companysf.filmbilet.entities.Repertoire;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ChooseDateTime implements RepertoireConnListener {
    private static final String logTag = ChooseDateTime.class.getSimpleName();
    private DateTimeListener dateTimeListener;
    private List<Repertoire> repertoireList;

    private DateTimeChoice dateTimeChoice;

    public ChooseDateTime(Context context, int movieId, DateTimeChoice dateTimeChoice,ErrorListener errorListener,
                          DateTimeListener dateTimeListener) {
        this.dateTimeListener = dateTimeListener;
        this.dateTimeChoice = dateTimeChoice;
        RepertoireConnection repertoireConnection =
                new RepertoireConnection(context, errorListener, this);
        repertoireConnection.updateRepertoireForMovieFromServer(movieId);

    }

    @Override
    public void onDbResponseCallback(List<Repertoire> repertoireList) {
        Log.d(logTag, "onDbResponseCallback");
        this.repertoireList = new ArrayList<>(repertoireList);

        for(Repertoire repertoire : this.repertoireList)
            Log.d(logTag,"repertoireList before prepare= " + repertoire.toString());

        for(Repertoire repertoire : dateTimeChoice.getHoursForDate())
            Log.d(logTag,"hoursForDate = " + repertoire.toString());

        for(Repertoire repertoire : this.repertoireList)
            Log.d(logTag,"repertoireList after prepare= " + repertoire.toString());

        dateTimeListener.callbackOnSetUi();

    }

    public void prepareHoursForDate(int index) {
        if (dateTimeChoice.getHoursForDate().size() > 0) dateTimeChoice.getHoursForDate().clear();
        Log.d(logTag, "prepareHoursForDate przed forEach, repertoireList.size() = " + repertoireList.size());

        for (Repertoire repertoire : repertoireList) {
            Log.d(logTag, "repertoire in prepare = " + repertoire.toString());
            for(Repertoire currRep : dateTimeChoice.getCurrentWeek())
                Log.d(logTag, "currentWeek in prepare = " + currRep.toString());

            if(repertoire.getDateFormat().isHourInDay(dateTimeChoice.getCurrentWeek().get(index).getDateFormat())){
                dateTimeChoice.getHoursForDate().add(repertoire);
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

        Set<Integer> dateIndexesToRemove = new HashSet<>();

        for (Repertoire currRep : dateTimeChoice.getCurrentWeek())
            Log.d(logTag, "Zawartość currentWeek przed filtrowaniem= " + currRep.toString());

        for (Repertoire repertoire : repertoireList) {
            int innerInc = 0;
            int index = 0;
            for (Repertoire currRep : dateTimeChoice.getCurrentWeek()) {

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
                dateTimeChoice.getCurrentWeek().remove(repertoireList.get(i));

        for (Repertoire repertoire : dateTimeChoice.getCurrentWeek())
            Log.d(logTag, "Zawartość currentWeek po filtrowaniu= " + repertoire.toString());

        for (Repertoire repertoire : repertoireList)
            Log.d(logTag, "Zawartość repertoireList po filtrowaniu= " + repertoire.toString());
    }

    public void chooseDate(int index){
        for(int i =0; i<dateTimeChoice.getSelectedDate().length; i++){
            if(i!=index){
                dateTimeChoice.setSelectedDate(i, false);
                Log.d(logTag, "selectedDate[" + i + "] = " + dateTimeChoice.getSelectedDate()[i]);
            }
        }
        dateTimeChoice.setSelectedDate(index, true);
        Log.d(logTag, "selectedDate[" + index + "] = " + dateTimeChoice.getSelectedDate()[index]);
    }

    public void clearListOfRepertoires(){
        if(dateTimeChoice.getSelectedRepertoires().size() > 0)
            dateTimeChoice.getSelectedRepertoires().clear();
    }

    public void markAnHour(int position, boolean isSelected){
        int objToRemove=-1;
        if(isSelected){
            Log.d(logTag, "Dodana wartość repertuaru = " + repertoireList.get(position).getId());
            dateTimeChoice.getSelectedRepertoires().add(dateTimeChoice.getHoursForDate().get(position).getId());
        }
        else{
            Log.d(logTag,"Usunięta wartość repertuaru = " + repertoireList.get(position).getId());
            objToRemove = dateTimeChoice.getHoursForDate().get(position).getId();
        }
        if(objToRemove>0){
            dateTimeChoice.getSelectedRepertoires().remove(Integer.valueOf(objToRemove));
        }
        Log.d(logTag, "Zawartość selected repertoires: ");
        for(Integer i : dateTimeChoice.getSelectedRepertoires())
            Log.d(logTag, "Wartość selectedRepertoires = " + i);
    }

    public void checkNumOfChoices(){
        if(dateTimeChoice.getSelectedRepertoires().size() == 0)
            dateTimeListener.callBackOnBadChoice(true);
        else if(dateTimeChoice.getSelectedRepertoires().size() > 1)
            dateTimeListener.callBackOnBadChoice(false);
        else
            dateTimeListener.callBackSuccess();
    }

}
