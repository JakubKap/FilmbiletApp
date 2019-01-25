package com.companysf.filmbilet.services;

import com.google.gson.Gson;

public class WebSocketMessage {
    private boolean choosedPlaces [];
    private String choosedPlacesString;

    public WebSocketMessage(boolean[] choosedPlaces) {
        this.choosedPlaces = choosedPlaces;

        // convert to Json string
        Gson gson = new Gson();
        this.choosedPlacesString = gson.toJson(choosedPlaces);
    }

    public WebSocketMessage(String choosedPlaceString) {
        this.choosedPlacesString = choosedPlaceString;

        //covert Json string to array
        Gson gson = new Gson();
        this.choosedPlaces = gson.fromJson(choosedPlaceString, boolean[].class);
    }

    public boolean[] getChoosedPlaces() {
        return choosedPlaces;
    }

    public String getChoosedPlacesString() {
        return choosedPlacesString;
    }
}

