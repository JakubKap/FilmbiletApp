package com.companysf.filmbilet.connection;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.companysf.filmbilet.activity.LoginActivity;
import com.companysf.filmbilet.app.AppConfig;
import com.companysf.filmbilet.app.AppController;
import com.companysf.filmbilet.connection.Listener.Listener;
import com.companysf.filmbilet.entities.Customer;
import com.companysf.filmbilet.services.Login;
import com.companysf.filmbilet.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class LoginConnection {
    private Context context;
    private Listener listener;
    private Login login;
    private static final String logTag = LoginActivity.class.getSimpleName();

    public LoginConnection(Context context, Listener listener) {
        this.context = context;
        this.listener = listener;
        login = new Login(context);
    }

    public void checkLogin(final String email, final String password) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                AppConfig.LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(logTag, "Login response: " + response);
                try {
                    JSONObject json = new JSONObject(response);
                    boolean error = json.getBoolean(context.getString(R.string.error));
                    if (error) {
                        listener.callBackOnError();
                    } else {
                        JSONObject customerJson =
                                json.getJSONObject(context.getString(R.string.customerJsonName));
                        Log.d(logTag, "customerName: " + customerJson.getString("name") + "i jego id: " + Integer.toString(customerJson.getInt("id")));

                        Customer customer = new Customer(
                                customerJson.getString(
                                        context.getString(R.string.jsonCustomerName)
                                ),
                                customerJson.getString(
                                        context.getString(R.string.jsonCustomerSurname)
                                ),
                                customerJson.getString(
                                        context.getString(R.string.jsonCustomerEmail)
                                ),
                                customerJson.getInt(context.getString(R.string.jsonCustomerId))
                        );
                        listener.callBackOnSuccess();
                        login.saveUserOnDevice(customer);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.callBackOnError();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(logTag, "Login Error: " + error.getMessage());
                listener.callBackOnError();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put(context.getString(R.string.loginPutReqEmailParam), email);
                params.put(context.getString(R.string.loginPutReqPasswordParam), password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(
                stringRequest, context.getString(R.string.getReservationsRequestAdd)
        );
    }
}
