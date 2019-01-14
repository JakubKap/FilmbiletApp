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
    private boolean [] selectedHour;
    private List<ToggleButton> toggleButtons;
    private boolean firstEl;

    public HoursAdapter(Context c, List<Repertoire> repertoireArrayList){
        mContext = c;
        repertoireList = repertoireArrayList;
        selectedHour = new boolean[repertoireList.size()];
        firstEl =true;
        toggleButtons = new ArrayList<>();

        if(selectedHour.length > 0){
            selectedHour[0] = true;

            for(int i = 1; i< selectedHour.length; i++)
                selectedHour[i] = false;
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


           /* if(position==0 && firstEl) {
                toggleButton.setBackgroundResource(R.drawable.selected_hour_button);
                //toggleButton.setTextColor();
                toggleButton.setTextColor(Color.rgb(237, 125, 116));
                firstEl = false;
            }
            else*/
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

        //toggleButton.setOnClickListener(new MyOnClickListener(position));

        final ToggleButton finalToggleButton = toggleButton;
/*
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //notifyDataSetChanged();
                //Log.d(logTag, "Value of b = " + b);
                //if(!b)
                    finalToggleButton.setBackgroundResource(R.drawable.selected_hour_button);

               /* else
                    finalToggleButton.setBackgroundResource(R.drawable.normal_hour_button);*/

            //}
        //});

        /*
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finalToggleButton.setBackgroundResource(R.drawable.selected_hour_button);
            }
        });*/

        //dodanie ToggleButtona do tablicy
        toggleButtons.add(toggleButton);


        //Pierwszy z ToggleButtonów jest wstawinay podwójnie do kolekcji, więc należy usunąć jeden z nich
        /*if(!firstEl)
            toggleButtons.remove(0);*/

       for(ToggleButton t : toggleButtons)
            Log.d(logTag, "Zawartość toggleButtons = " + t.getText());

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
            for(int i = 0; i< selectedHour.length; i++)
                Log.d(logTag, "selectedHour[" + i + "] = "+ selectedHour[i]);





            //jeśli wciśnięto nie zaznaczony wcześniej przycisk
            if(!selectedHour[position]){
                selectedHour[position] = true;

                //toggleButtons.get(position).setBackgroundResource(R.drawable.selected_hour_button);
                //textColor

                //v.setBackgroundResource(R.drawable.selected_hour_button);

                for(int i = 0; i< selectedHour.length; i++) {
                    if (i != position) {
                        selectedHour[i] = false;
                        //toggleButtons.get(i).setBackgroundResource(R.drawable.normal_hour_button);
                    }
                }
            }

            for(int i = 0; i< selectedHour.length; i++)
                Log.d(logTag, "selectedHour[" + i + "] = "+ selectedHour[i]);

        }

    }
}
