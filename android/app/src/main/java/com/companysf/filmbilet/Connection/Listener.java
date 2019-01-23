package com.companysf.filmbilet.Connection;

public interface Listener extends ErrorListener{
    void callBackOnSuccess();
    void callBackOnNoNetwork();
}
