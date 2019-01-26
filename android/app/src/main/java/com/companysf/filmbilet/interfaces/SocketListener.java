package com.companysf.filmbilet.interfaces;

import okhttp3.WebSocket;

public interface SocketListener {
    public void onOpenCallback(WebSocket webSocket);
    public void onMessageCallback(boolean [] reservedSeats);
}
