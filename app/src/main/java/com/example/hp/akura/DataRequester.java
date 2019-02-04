package com.example.hp.akura;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DataRequester {
    private Context context;
    private int method;
    private String url;
    private Map<String, String> headers;
    private JSONObject body;
    private int statusCode;

    public DataRequester(Context context, int method, String url, Map<String, String> headers, JSONObject body) {
        this.context = context;
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public void sendRequest(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        JsonObjectRequest request = new JsonObjectRequest(
                method,
                url,
                body,
                responseListener,
                errorListener
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if(headers != null){
                    return headers;
                }
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if(body != null){
                    Map<String, String> params = new HashMap<>();
                    Iterator<String> keys = body.keys();
                    while (keys.hasNext()){
                        String key = keys.next();
                        try {
                            params.put(key, body.getString("key"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.d("MY_APP", params.toString());
                    return params;
                }
                return super.getParams();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                statusCode = response.statusCode;
                return super.parseNetworkResponse(response);
            }

            @Override
            protected VolleyError parseNetworkError(VolleyError volleyError) {
                statusCode = volleyError.networkResponse.statusCode;
                return super.parseNetworkError(volleyError);
            }
        };
        Volley.newRequestQueue(context).add(request);
    }
}
