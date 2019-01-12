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
    private int DayOfMonth;
    private String DayOfWeek;
    private int hour;
    private int minute;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDatePom() {
        return datePom;
    }

    public void setDatePom(String datePom) {
        this.datePom = datePom;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
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
    }


    public String toString(){
        /*return "id = " + id  + ", year = " + date.getYear() + ", month = " + date.getMonth()
                + ", dayOfMonth = ";*/
        return "id = " + id  + ", date = " + date.toString()
                + ", year = " + date.get(Calendar.YEAR)
                + ", month = " + date.get(Calendar.MONTH)
                + ", dayOfMonth = " + date.get(Calendar.DAY_OF_MONTH)
                + ", dayOfWeek = " + date.get(Calendar.DAY_OF_WEEK)
                + ", hour = " + date.get(Calendar.HOUR_OF_DAY)
                + ", minutes = " + date.get(Calendar.MINUTE)
                + ", seconds = " + date.get(Calendar.SECOND)
                + ", AM = " + date.get(Calendar.AM_PM);
    }
}
