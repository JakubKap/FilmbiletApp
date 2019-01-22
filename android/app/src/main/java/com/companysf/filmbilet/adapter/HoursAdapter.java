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
import com.companysf.filmbilet.appLogic.Schedule;

import java.util.ArrayList;
import java.util.List;

public class HoursAdapter extends BaseAdapter {
    private static final String logTag = HoursAdapter.class.getSimpleName();
    private Context mContext;
    private List<Schedule> scheduleList;
    private List<ToggleButton> toggleButtons;
    private List<Integer> selectedSchedules;

    public HoursAdapter(Context c, List<Schedule> scheduleArrayList){
        mContext = c;
        scheduleList = scheduleArrayList;
        boolean[] selectedHour = new boolean[scheduleList.size()];
        selectedSchedules = new ArrayList<>();
        toggleButtons = new ArrayList<>();

        if(selectedSchedules.size() > 0)
            selectedSchedules.clear();

        if(selectedHour.length > 0){
            selectedHour[0] = true;

            for(int i = 1; i< selectedHour.length; i++)
                selectedHour[i] = false;
        }
    }


    public List<Integer> getSelectedSchedules() {
        return selectedSchedules;
    }

    public void clearListOfSchedules(){
        if(selectedSchedules.size() > 0)
            selectedSchedules.clear();
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

                int objToRemove = -1;
                if(b) {
                    Log.d(logTag, "Dodana wartość repertuaru = " + scheduleList.get(position).getId());
                    selectedSchedules.add(scheduleList.get(position).getId());
                }
                else{
                    Log.d(logTag,"Usunięta wartość repertuaru = " + scheduleList.get(position).getId());
                    objToRemove = scheduleList.get(position).getId();
                }

                if(objToRemove>0){
                    Log.d(logTag, "Przed usunięciem:");
                    selectedSchedules.remove(Integer.valueOf(objToRemove));
                    Log.d(logTag, "Po usunięciu:");
                }

                Log.d(logTag, "Zawartość selected schedules: ");
                for(Integer i : selectedSchedules)
                    Log.d(logTag, "Wartość selectedSchedules = " + i);

               /* else
                    finalToggleButton.setBackgroundResource(R.drawable.normal_hour_button);*/

            }
        });

        //dodanie ToggleButtona do tablicy
        toggleButtons.add(toggleButton);

       for(ToggleButton t : toggleButtons)
            Log.d(logTag, "Zawartość toggleButtons = " + t.getText());

        return toggleButton;
    }
}
