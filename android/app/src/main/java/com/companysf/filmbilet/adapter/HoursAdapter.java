package com.companysf.filmbilet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.services.DateTime;
import com.companysf.filmbilet.services.Schedule;

import java.util.ArrayList;
import java.util.List;

public class HoursAdapter extends BaseAdapter {
    private static final String logTag = HoursAdapter.class.getSimpleName();
    private Context mContext;
    DateTime dateTime;
    List<Schedule> scheduleList;

    public HoursAdapter(Context c, DateTime dateTime, List<Schedule> scheduleList) {
        mContext = c;
        this.dateTime = dateTime;
        this.scheduleList = scheduleList;
    }


    @Override
    public int getCount() {
        return scheduleList.size();
    }

    @Override
    public Object getItem(int position) {
        return scheduleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ToggleButton toggleButton;

        Log.d(logTag, "Nowoutworzony przycisk dla id = " + (scheduleList.get(position).getId()));
        if(convertView == null){
            toggleButton = new ToggleButton(mContext);
            toggleButton.setLayoutParams(new GridView.LayoutParams(400, 200));

           toggleButton.setBackgroundResource(R.drawable.toggle_button_selector);

            toggleButton.setPadding(8, 8, 8, 8);
        }
        else{
            toggleButton = (ToggleButton) convertView;
        }

        Log.d(logTag, "Position w getView = " + position + ", converView = " + convertView);

        //budowanie textu
        String text = Integer.toString(scheduleList.get(position).getHourOfDay());

        StringBuilder sB = new StringBuilder(text);
        sB.append(mContext.getString(R.string.colon));

        if(scheduleList.get(position).getMinute() < 10)
            sB.append(mContext.getString(R.string.zero));

        sB.append(Integer.toString(scheduleList.get(position).getMinute()));

        if(scheduleList.get(position).getHourOfDay() < 10)
            sB.insert(0, mContext.getString(R.string.zero));


        text = sB.toString();

        toggleButton.setText(text);
        toggleButton.setTextOn(text);
        toggleButton.setTextOff(text);
        toggleButton.setTextColor(Color.BLACK);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


            }
        });

        //dodanie ToggleButtona do tablicy

        return toggleButton;
    }
}
