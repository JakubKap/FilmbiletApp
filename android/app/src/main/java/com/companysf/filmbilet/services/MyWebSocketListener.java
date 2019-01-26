package com.companysf.filmbilet.services;

import android.util.Log;

import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.interfaces.SocketListener;
import com.companysf.filmbilet.services.WebSocketMessage;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class MyWebSocketListener extends WebSocketListener {

    private static final String logTag = MyWebSocketListener.class.getSimpleName();
    SocketListener socketListener;
    OkHttpClient httpClient;
    Request request;

    public MyWebSocketListener(SocketListener socketListener){
        this.socketListener = socketListener;
        this.httpClient  = new OkHttpClient();
        this.request = new Request.Builder().url(AppConfig.websocketURL).build();
        httpClient.newWebSocket(request, this);
        httpClient.dispatcher().executorService().shutdown();
    }

    @Override
    public void onOpen(final WebSocket webSocket, okhttp3.Response response) {
        socketListener.onOpenCallback("success");
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(logTag, "onMessage");
        WebSocketMessage message = new WebSocketMessage(text);

        socketListener.onMessageCallback(message.getChoosedPlaces());

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
        Log.d(logTag, "onFailure: " + t.getMessage());
    }

    private void sendMessageToServer(OkHttpClient httpClient, WebSocket webSocket) {
    }
}
