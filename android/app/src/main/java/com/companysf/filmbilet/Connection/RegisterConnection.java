package com.companysf.filmbilet.Connection;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.Activity.LoginActivity;
import com.companysf.filmbilet.App.AppConfig;
import com.companysf.filmbilet.App.AppController;
import com.companysf.filmbilet.Connection.Listener.Listener;
import com.companysf.filmbilet.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterConnection {
    private Context context;
    private Listener listener;
    private static final String logTag = LoginActivity.class.getSimpleName();

    public RegisterConnection(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void registerUser(final String name, final String surname, final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(logTag, "Register request: " + response);
                        try {
                            JSONObject json = new JSONObject(response);
                            boolean error = json.getBoolean(context.getString(R.string.error));
                            if (error){
                                listener.callBackOnError();
                            } else{
                                listener.callBackOnSuccess();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            listener.callBackOnError();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Registration Error: " + error.getMessage());
                listener.callBackOnError();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(context.getString(R.string.registerPutReqNameParam), name);
                params.put(context.getString(R.string.registerPutReqSurnameParam), surname);
                params.put(context.getString(R.string.registerPutReqEmailParam), email);
                params.put(context.getString(R.string.registerPutReqPasswordParam), password);

                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, context.getString(R.string.registerRequestAdd));
    }
}
