package com.companysf.filmbilet.connection.Listener;

import com.companysf.filmbilet.services.Schedule;

import java.util.List;

public interface RepertoireConnListener {
    public void onDbResponseCallback(List<Schedule> scheduleList);
}