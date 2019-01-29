package com.companysf.filmbilet.entities;

import java.util.ArrayList;
import java.util.List;

public class DateTimeChoice {

    private List<Integer> selectedRepertoires;
    private List<Repertoire> hoursForDate;
    private List<Repertoire> currentWeek;
    private boolean[] selectedDate;

    public DateTimeChoice(){
        this.selectedRepertoires = new ArrayList<>();
        this.selectedDate = new boolean[5];
        this.selectedDate[0] = true;
        for(int i=1; i<selectedDate.length;i++)
            selectedDate[i]=false;
    }

    public List<Integer> getSelectedRepertoires() {

        return selectedRepertoires;
    }

    public List<Repertoire> getHoursForDate() {
        return hoursForDate;
    }

    public List<Repertoire> getCurrentWeek() {
        return currentWeek;
    }

    public boolean[] getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedRepertoires(List<Integer> selectedRepertoires) {
        this.selectedRepertoires = selectedRepertoires;
    }

    public void setHoursForDate(List<Repertoire> hoursForDate) {
        this.hoursForDate = hoursForDate;
    }

    public void setCurrentWeek(List<Repertoire> currentWeek) {
        this.currentWeek = currentWeek;
    }

    public void setSelectedDate(int index, boolean value) {
        this.selectedDate[index] = value;
    }
}
