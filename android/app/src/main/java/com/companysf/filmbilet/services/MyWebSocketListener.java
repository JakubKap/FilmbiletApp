package com.companysf.filmbilet.services;

import android.content.Context;
import android.util.Log;

import com.companysf.filmbilet.R;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.connection.Listener.SocketListener;
import com.companysf.filmbilet.entities.WebsocketMessage;

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
        socketListener.onOpenCallback(webSocket);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        Log.d(logTag, "onMessage");
        WebsocketMessage message = new WebsocketMessage(text);
//        WebSocketMessageService messageService = new WebSocketMessageService(text);
        WebSocketMessageService messageService = new WebSocketMessageService();

        socketListener.onMessageCallback(
//                messageService.getChoosedPlaces()
                messageService.convertJsonStringToArray(message.getChoosedPlacesString())
        );

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

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    public void prepareMessage(Context c, WebSocket webSocket, boolean[] myChoosedPlaces){
        WebsocketMessage message = new WebsocketMessage(myChoosedPlaces);
//        WebSocketMessageService message = new WebSocketMessageService(myChoosedPlaces);
        WebSocketMessageService messageService = new WebSocketMessageService();
        webSocket.send(
                messageService.convertToJsonString(message.getChoosedPlaces())
        );
        webSocket.close(1000, c.getString(R.string.socketCloseReason));
    }


}
