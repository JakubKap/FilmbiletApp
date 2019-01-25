package com.companysf.filmbilet.connection.Listener;

public interface ServerConnectionListener extends Listener {
    void callBackOnEndOfFetchingData(boolean manualSwipeRefresh);
}
