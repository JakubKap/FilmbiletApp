package com.companysf.filmbilet.Connection;

public interface ServerConnectionListener extends Listener {
    void callBackOnEndOfFetchingData(boolean manualSwipeRefresh);
}
