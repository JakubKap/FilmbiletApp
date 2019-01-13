package com.companysf.filmbilet.appLogic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Date {
    private GregorianCalendar date;

    public Date(String dateTime) {
        this.date = new GregorianCalendar();
        convertMySQLToJavaDateTime(dateTime);
    }

    private void convertMySQLToJavaDateTime(String mySQLDateTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", new Locale("pl", "PL"));
        try {
            java.util.Date date = format.parse(mySQLDateTime);
            this.date.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getStringDate() {
        String date = "";
        int[] dateArray = new int[5];
        dateArray[0] = this.date.get(Calendar.DATE);
        dateArray[1] = this.date.get(Calendar.MONTH) + 1;
        dateArray[2] = this.date.get(Calendar.YEAR);
        dateArray[3] = (this.date.get(Calendar.AM_PM) == 1) ?
                this.date.get(Calendar.HOUR) + 12 : this.date.get(Calendar.HOUR);
        dateArray[4] = this.date.get(Calendar.MINUTE);

        StringBuilder stringBuilder = new StringBuilder(date);
        for (int i = 0; i < 2; i++) {
            stringBuilder.append(Integer.toString(dateArray[i]));   //Day and month
            stringBuilder.append(".");
        }
        stringBuilder.append(Integer.toString(dateArray[2]));       //Year
        stringBuilder.append(" ");

        stringBuilder.append(Integer.toString(dateArray[3]));   //Hour
        if (dateArray[3] == 0)
            stringBuilder.append("0");
        stringBuilder.append(":");

        stringBuilder.append(Integer.toString(dateArray[4]));   //minute
        if (dateArray[4] == 0)
            stringBuilder.append("0");


        return stringBuilder.toString();
    }

    public GregorianCalendar getDate() {
        return date;
    }
}
