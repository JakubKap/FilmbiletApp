package com.companysf.filmbilet.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateFormat {
    private GregorianCalendar date;

    public DateFormat(String dateTime) {
        this.date = new GregorianCalendar();
        convertMySQLToJavaDateTime(dateTime);
    }

    private void convertMySQLToJavaDateTime(String mySQLDateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pl", "PL"));
        try {
            Date date = format.parse(mySQLDateTime);
            this.date.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getStringDateTime() {
        return getStringDate() + " " + getStringTime(":");
    }

    public String getStringDate() {
        String date = "";
        int[] dateArray = new int[3];
        dateArray[0] = this.date.get(Calendar.DATE);
        dateArray[1] = this.date.get(Calendar.MONTH) + 1;
        dateArray[2] = this.date.get(Calendar.YEAR);

        StringBuilder stringBuilder = new StringBuilder(date);
        for (int i = 0; i < 2; i++) {
            if (dateArray[i] < 10){
                stringBuilder.append("0");
            }
            stringBuilder.append(Integer.toString(dateArray[i]));   //Day and month
            stringBuilder.append(".");
        }
        stringBuilder.append(Integer.toString(dateArray[2]));       //Year

        return stringBuilder.toString();
    }

    public String getStringTime(String timeSeparator) {
        String time = "";
        int[] timeArray = new int[2];

        timeArray[0] = (this.date.get(Calendar.AM_PM) == 1) ?
                this.date.get(Calendar.HOUR) + 12 : this.date.get(Calendar.HOUR);
        timeArray[1] = this.date.get(Calendar.MINUTE);

        StringBuilder stringBuilder = new StringBuilder(time);

        stringBuilder.append(Integer.toString(timeArray[0]));   //Hour
        if (timeArray[0] == 0)
            stringBuilder.append("0");
        stringBuilder.append(timeSeparator);

        stringBuilder.append(Integer.toString(timeArray[1]));   //minute
        if (timeArray[1] == 0)
            stringBuilder.append("0");

        return stringBuilder.toString();
    }

    public GregorianCalendar getDate() {
        return date;
    }
}
