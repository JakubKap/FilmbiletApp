package com.companysf.filmbilet.interfaces;

public interface ModelListener {
    public void updateUiCallback();
    public void showDialogCallback(String takenSeatsNumbers, int seatCount);
    public void socketCloseError();
}
