package com.companysf.filmbilet.interfaces;

public interface OnMessageListener {
    public void showDialogCallback(String takenSeatsNumbers, int seatCount);
    public void msgToServerCallback(boolean [] choosedPlaces);
}
