package com.companysf.filmbilet.Connection.Listener;

public interface ServerConnectionListener extends Listener {
    void callBackOnEndOfFetchingData(boolean manualSwipeRefresh);
}
