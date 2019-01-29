package com.companysf.filmbilet.services;

import com.google.gson.Gson;

class WebSocketMessageService {
//    private boolean choosedPlaces [];
//    private String choosedPlacesString;

    WebSocketMessageService() {
    }

    //    public WebSocketMessageService(boolean[] choosedPlaces) {
//        this.choosedPlaces = choosedPlaces;
//
//        // convert to Json string
//        Gson gson = new Gson();
//        this.choosedPlacesString = gson.toJson(choosedPlaces);
//    }

//    public WebSocketMessageService(String choosedPlaceString) {
//        this.choosedPlacesString = choosedPlaceString;
//
//        //covert Json string to array
//        Gson gson = new Gson();
//        this.choosedPlaces = gson.fromJson(choosedPlaceString, boolean[].class);
//    }

    String convertToJsonString(boolean choosedPlaces[]) {
        Gson gson = new Gson();
        return gson.toJson(choosedPlaces);
    }

    boolean[] convertJsonStringToArray(String choosedPlaceString) {
        Gson gson = new Gson();
        return gson.fromJson(choosedPlaceString, boolean[].class);
    }

//    public boolean[] getChoosedPlaces() {
//        return choosedPlaces;
//    }
//
//    public String getChoosedPlacesString() {
//        return choosedPlacesString;
//    }
}

