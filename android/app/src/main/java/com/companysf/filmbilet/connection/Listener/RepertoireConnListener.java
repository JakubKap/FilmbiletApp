package com.companysf.filmbilet.connection.Listener;

import com.companysf.filmbilet.entities.Repertoire;

import java.util.List;

public interface RepertoireConnListener {
    public void onDbResponseCallback(List<Repertoire> repertoireList);
}
