package com.companysf.filmbilet.connection.Listener;

public interface ReservationConnListener {
    public void onDbResponseCallback(boolean[] takenSeats);
}
