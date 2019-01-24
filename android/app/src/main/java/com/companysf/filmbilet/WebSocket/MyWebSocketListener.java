package com.companysf.filmbilet.WebSocket;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.Activity.MainActivity;
import com.companysf.filmbilet.Interfaces.SocketListener;
import com.companysf.filmbilet.R;

import okhttp3.OkHttpClient;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketListener extends WebSocketListener {

    private static final String logTag = MyWebSocketListener.class.getSimpleName();
    Context mContext;
    SocketListener socketListener;

    public MyWebSocketListener(Context c, SocketListener socketListener){
        this.mContext = c;
        this.socketListener = socketListener;
    }

    @Override
    public void onOpen(final WebSocket webSocket, okhttp3.Response response) {
        socketListener.callback("success");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(logTag, "onMessage");

    }

    @Override
    public void onMessage(WebSocket webSocket, ByteString bytes) {
        super.onMessage(webSocket, bytes);

    }

    @Override
    public void onClosing(WebSocket webSocket, int code, String reason) {
        webSocket.close(1000, null);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        super.onClosed(webSocket, code, reason);
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
        Log.d(logTag, mContext.getString(R.string.socketFailure) + t.getMessage());
    }

    private void sendMessageToServer(OkHttpClient httpClient, WebSocket webSocket) {
    }
}
