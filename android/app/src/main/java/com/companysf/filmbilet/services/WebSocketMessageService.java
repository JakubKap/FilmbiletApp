package com.companysf.filmbilet.services;

import com.google.gson.Gson;

class WebSocketMessageService {
    WebSocketMessageService() {
    }

    String convertToJsonString(boolean choosedPlaces[]) {
        Gson gson = new Gson();
        return gson.toJson(choosedPlaces);
    }

    boolean[] convertJsonStringToArray(String choosedPlaceString) {
        Gson gson = new Gson();
        return gson.fromJson(choosedPlaceString, boolean[].class);
    }

}

