package com.companysf.filmbilet.entities;

public class WebsocketMessage {
    private boolean choosedPlaces [];
    private String choosedPlacesString;

    public WebsocketMessage(boolean[] choosedPlaces) {
        this.choosedPlaces = choosedPlaces;
    }

    public WebsocketMessage(String choosedPlacesString) {
        this.choosedPlacesString = choosedPlacesString;
    }

    public boolean[] getChoosedPlaces() {
        return choosedPlaces;
    }

    public String getChoosedPlacesString() {
        return choosedPlacesString;
    }
}
