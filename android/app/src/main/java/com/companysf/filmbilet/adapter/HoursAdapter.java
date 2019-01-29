package com.companysf.filmbilet.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.services.ChooseDateTime;
import com.companysf.filmbilet.entities.Repertoire;

import java.util.List;

public class HoursAdapter extends BaseAdapter {
    private static final String logTag = HoursAdapter.class.getSimpleName();
    private Context mContext;
    private ChooseDateTime chooseDateTime;
    private List<Repertoire> hoursForDate;

    public HoursAdapter(Context c, ChooseDateTime chooseDateTime) {
        mContext = c;
        this.chooseDateTime = chooseDateTime;
        this.hoursForDate = chooseDateTime.getHoursForDate();

        if(chooseDateTime.getSelectedRepertoires().size() > 0){
            chooseDateTime.getSelectedRepertoires().clear();
            Log.d(logTag, "Size of selectedSchedule = " + chooseDateTime.getSelectedRepertoires().size());
        }

    }

    @Override
    public int getCount() {
        return hoursForDate.size();
    }

    @Override
    public Object getItem(int position) {
        return hoursForDate.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ToggleButton toggleButton;

        Log.d(logTag, "Nowoutworzony przycisk dla id = " + (hoursForDate.get(position).getId()));
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
        String text = hoursForDate.get(position).getDateFormat().getStringTime(":");

        toggleButton.setText(text);
        toggleButton.setTextOn(text);
        toggleButton.setTextOff(text);
        toggleButton.setTextColor(ContextCompat.getColor(mContext, R.color.black));

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                chooseDateTime.markAnHour(position, b);
            }
        });


        Log.d(logTag, "hoursForDate.size() == " + hoursForDate.size());
        return toggleButton;
    }
}