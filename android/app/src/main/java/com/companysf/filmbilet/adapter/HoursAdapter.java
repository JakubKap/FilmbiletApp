package com.companysf.filmbilet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.appLogic.Repertoire;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HoursAdapter extends BaseAdapter {
    private static final String logTag = HoursAdapter.class.getSimpleName();
    private Context mContext;
    private List<Repertoire> repertoireList;
    private boolean [] selectedHours;
    private List<ToggleButton> toggleButtons;
    private boolean firstEl;

    public HoursAdapter(Context c, List<Repertoire> repertoireArrayList){
        mContext = c;
        repertoireList = repertoireArrayList;
        selectedHours = new boolean[repertoireList.size()];
        firstEl =true;
        toggleButtons = new ArrayList<>();

        if(selectedHours.length > 0){
            selectedHours[0] = true;

            for(int i=1; i<selectedHours.length; i++)
                selectedHours[i] = false;
        }
    }
    @Override
    public int getCount() {
        return repertoireList.size();
    }

    @Override
    public Object getItem(int position) {
        return repertoireList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToggleButton toggleButton;

        Log.d(logTag, "Nowoutworzony przycisk dla id = " + (repertoireList.get(position).getId()));
        if(convertView == null){
            toggleButton = new ToggleButton(mContext);
            toggleButton.setLayoutParams(new GridView.LayoutParams(400, 200));

            if(position == 0 && firstEl) {
                toggleButton.setBackgroundResource(R.drawable.selected_hour_button);
                //toggleButton.setTextColor();
                toggleButton.setTextColor(Color.rgb(237, 125, 116));
                firstEl = false;
            }
            else
                toggleButton.setBackgroundResource(R.drawable.normal_hour_button);

            toggleButton.setPadding(8, 8, 8, 8);
        }
        else{
            toggleButton = (ToggleButton) convertView;
        }

        String text = Integer.toString(repertoireList.get(position).getHourOfDay());

        StringBuilder sB = new StringBuilder(text);
        sB.append(":");

        if(repertoireList.get(position).getMinute() < 10)
            sB.append("0");

        sB.append(Integer.toString(repertoireList.get(position).getMinute()));

        if(repertoireList.get(position).getHourOfDay() < 10)
            sB.insert(0, "0");



        text = sB.toString();

        toggleButton.setText(text);
        toggleButton.setTextOn(text);
        toggleButton.setTextOff(text);
        toggleButton.setTextColor(Color.BLACK);

        toggleButton.setOnClickListener(new MyOnClickListener(position));

        if(!toggleButtons.contains(toggleButton))
            toggleButtons.add(toggleButton);

        for(ToggleButton t : toggleButtons)
            Log.d(logTag, "Zawartość toggleButtons: " + t.getText());

        return toggleButton;
    }

    class MyOnClickListener implements View.OnClickListener
    {
        private final int position;

        public MyOnClickListener(int position)
        {
            this.position = position;
        }

        public void onClick(View v)
        {
            // Preform a function based on the position
            Log.d(logTag, "position = " + position);

            //jeśli wciśnięto nie zaznaczony wcześniej przycisk
            if(!selectedHours[position]){
                selectedHours[position] = true;

                //toggleButtons.get(position).setBackgroundResource(R.drawable.selected_hour_button);
                //textColor

                for(int i =0; i<selectedHours.length; i++) {
                    if (i != position) {
                        selectedHours[i] = false;
                        //toggleButtons.get(i).setBackgroundResource(R.drawable.normal_hour_button);
                    }
                }
            }
        }
    }
}
