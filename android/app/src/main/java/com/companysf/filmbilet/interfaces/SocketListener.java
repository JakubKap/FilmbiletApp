package com.companysf.filmbilet.interfaces;

public interface SocketListener {
    public void onOpenCallback(String result);
    public void onMessageCallback(boolean [] reservedSeats);
}
