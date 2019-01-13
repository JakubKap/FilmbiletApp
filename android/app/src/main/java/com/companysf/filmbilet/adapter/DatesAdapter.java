package com.companysf.filmbilet.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ToggleButton;

import com.companysf.filmbilet.activity.MainActivity;
import com.companysf.filmbilet.appLogic.Repertoire;

import java.util.ArrayList;
import java.util.List;

public class DatesAdapter extends BaseAdapter {
    private static final String logTag = DatesAdapter.class.getSimpleName();
    private Context mContext;
    private List<Repertoire> repertoireList;

    public DatesAdapter(Context c, List<Repertoire> repertoireArrayList){
        mContext = c;
        repertoireList = repertoireArrayList;
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
            toggleButton.setLayoutParams(new GridView.LayoutParams(100, 55));
            toggleButton.setPadding(8, 8, 8, 8);
        }
        else{
            toggleButton = (ToggleButton) convertView;
        }

        toggleButton.setText(Integer.toString(repertoireList.get(position).getId()));
        toggleButton.setTextColor(Color.BLACK);

        return toggleButton;
    }
}
