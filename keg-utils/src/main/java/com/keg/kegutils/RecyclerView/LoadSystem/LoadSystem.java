package com.keg.kegutils.RecyclerView.LoadSystem;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.keg.kegutils.Utils.VolleyRequest;

import org.json.JSONObject;

/**
 * Created by gamer on 17/07/2017.
 */

public class LoadSystem {

    protected static final int DEFAULT_MAX_ATTEMPTS = 5;

    public interface OnLoad {
        void onSuccess(JSONObject response);
        void onFailure(VolleyError error);
    }

    protected int maxAttempts;
    protected OnLoad onLoadFinished;
    protected int remainingAttempts;
    protected String lastRequestedUrl;

    protected VolleyError volleyError;
    protected VolleyRequest volleyRequest;
    protected Response.Listener listener;
    protected Response.ErrorListener errListener;

    public LoadSystem(Context context, OnLoad onLoadFinished) {
        this(context, onLoadFinished, DEFAULT_MAX_ATTEMPTS);
    }

    public LoadSystem(Context context, OnLoad onLoadFinished, int maxAttempts) {
        VolleyRequest.initialize(context);
        this.volleyRequest = VolleyRequest.getInstance();
        this.onLoadFinished = onLoadFinished;
        this.maxAttempts = maxAttempts;
        configureErrorListener();
        configureListener();
    }

    protected void configureErrorListener() {
        errListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                volleyError = error;
                remainingAttempts--;
                startLoadAttempt();
            }
        };
    }

    protected void configureListener() {
        listener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (onLoadFinished == null) {
                    return;
                }
                onLoadFinished.onSuccess(response);
            }
        };
    }

    public void startLoad(String url) {
        lastRequestedUrl = url;
        remainingAttempts = maxAttempts;
        startLoadAttempt();
    }

    protected void startLoadAttempt() {
        if (remainingAttempts <= 0) {
            Log.d("keg-utils", "Last loading attempt failed.");
            if (onLoadFinished != null) {
                onLoadFinished.onFailure(volleyError);
            }
            return;
        }

        Log.d("keg-utils", "Attempt (" + remainingAttempts + ") to load urlLoading url: " + lastRequestedUrl + " .");
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, lastRequestedUrl, null, listener, errListener);
        volleyRequest.addToRequestQueue(jsObjRequest);
    }

}
