package com.companysf.filmbilet.WebSocket;

import com.google.gson.Gson;

public class Message {
    private boolean choosedPlaces [];
    private String choosedPlacesString;

    public Message(boolean[] choosedPlaces) {
        this.choosedPlaces = choosedPlaces;

        // convert to Json string
        Gson gson = new Gson();
        this.choosedPlacesString = gson.toJson(choosedPlaces);
    }

    public Message(String choosedPlaceString) {
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

