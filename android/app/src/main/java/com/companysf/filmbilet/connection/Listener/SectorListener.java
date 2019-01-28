package com.companysf.filmbilet.connection.Listener;

public interface SectorListener {
    public void updateUiCallback();
    public void showDialogCallback(String takenSeatsNumbers, int seatCount);
    public void socketCloseError();
}
