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
import com.companysf.filmbilet.appLogic.Repertoire;

import java.util.ArrayList;
import java.util.List;

public class HoursAdapter extends BaseAdapter {
    private static final String logTag = HoursAdapter.class.getSimpleName();
    private Context mContext;
    private List<Repertoire> repertoireList;
    private List<ToggleButton> toggleButtons;
    private List<Integer> selectedRepertoires;

    public HoursAdapter(Context c, List<Repertoire> repertoireArrayList){
        mContext = c;
        repertoireList = repertoireArrayList;
        boolean[] selectedHour = new boolean[repertoireList.size()];
        selectedRepertoires = new ArrayList<>();
        toggleButtons = new ArrayList<>();

        if(selectedRepertoires.size() > 0)
            selectedRepertoires.clear();

        if(selectedHour.length > 0){
            selectedHour[0] = true;

            for(int i = 1; i< selectedHour.length; i++)
                selectedHour[i] = false;
        }
    }


    public List<Integer> getSelectedRepertoires() {
        return selectedRepertoires;
    }

    public void clearListOfRepertoires(){
        if(selectedRepertoires.size() > 0)
            selectedRepertoires.clear();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ToggleButton toggleButton;

        Log.d(logTag, "Nowoutworzony przycisk dla id = " + (repertoireList.get(position).getId()));
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

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                int objToRemove = -1;
                if(b) {
                    Log.d(logTag, "Dodana wartość repertuaru = " + repertoireList.get(position).getId());
                    selectedRepertoires.add(repertoireList.get(position).getId());
                }
                else{
                    Log.d(logTag,"Usunięta wartość repertuaru = " + repertoireList.get(position).getId());
                    objToRemove = repertoireList.get(position).getId();
                }

                if(objToRemove>0){
                    Log.d(logTag, "Przed usunięciem:");
                    selectedRepertoires.remove(Integer.valueOf(objToRemove));
                    Log.d(logTag, "Po usunięciu:");
                }

                Log.d(logTag, "Zawartość selected repertoires: ");
                for(Integer i : selectedRepertoires)
                    Log.d(logTag, "Wartość selectedRepertoires = " + i);

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
