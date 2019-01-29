package com.companysf.filmbilet.app;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.companysf.filmbilet.utils.LruBitmapCache;

public class AppController extends Application {
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController instance;

    public static synchronized AppController getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return requestQueue;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        if (imageLoader == null){
            imageLoader = new ImageLoader(requestQueue, new LruBitmapCache());
        }
        return this.imageLoader;
    }
}
