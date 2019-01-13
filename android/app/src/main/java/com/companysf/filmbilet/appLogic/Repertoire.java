package com.companysf.filmbilet.appLogic;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Repertoire {
    private int id;
    private String datePom;
    private Calendar date;
    private int year;
    private int month;
    private int dayOfMonth;
    private String dayOfWeek;
    private int hourOfDay;
    private int minute;

    public int getId() {
        return id;
    }

    public String getDatePom() {
        return datePom;
    }

    public Calendar getDate() {
        return date;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMinute() {
        return minute;
    }


    public Repertoire(int id, String datePom) {
        this.id = id;
        this.datePom = datePom;

        this.date = new GregorianCalendar();
        castDate();
    }

    public void castDate(){
        DateFormat format;
        try{
            Date theDate = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss").parse(datePom);
            date.setTime(theDate);
        }catch(ParseException e){
            e.printStackTrace();
        }

        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH) + 1;
        dayOfMonth = date.get(Calendar.DAY_OF_MONTH);

        switch(date.get(Calendar.DAY_OF_WEEK)){
            case 1:
                dayOfWeek = "NIEDZ.";
                break;
            case 2:
                dayOfWeek="PON.";
                break;
            case 3:
                dayOfWeek="WT.";
                break;
            case 4:
                dayOfWeek="ŚR.";
                break;
            case 5:
                dayOfWeek="CZW.";
                break;
            case 6:
                dayOfWeek="PT.";
                break;
            case 7:
                dayOfWeek="SOB.";
                break;
        }

        hourOfDay = date.get(Calendar.HOUR_OF_DAY);
        minute = date.get(Calendar.MINUTE);

    }


    public String toString(){
        return "id = " + id  + ", year = " +  getYear() + ", month = " + getMonth()
                + ", dayOfMonth = " + getDayOfMonth() + ", dayOfWeek = " + dayOfWeek
                + ", hour = " + getHourOfDay() + ", minute = " + getMinute();
        /*return "id = " + id  + ", date = " + date.toString()
                + ", year = " + date.get(Calendar.YEAR)
                + ", month = " + date.get(Calendar.MONTH)
                + ", dayOfMonth = " + date.get(Calendar.DAY_OF_MONTH)
                + ", dayOfWeek = " + date.get(Calendar.DAY_OF_WEEK)
                + ", hour = " + date.get(Calendar.HOUR_OF_DAY)
                + ", minutes = " + date.get(Calendar.MINUTE)
                + ", seconds = " + date.get(Calendar.SECOND)
                + ", AM = " + date.get(Calendar.AM_PM);*/
    }
}
