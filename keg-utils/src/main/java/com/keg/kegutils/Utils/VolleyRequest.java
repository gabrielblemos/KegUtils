package com.keg.kegutils.Utils;

/**
 * Created by gamer on 12/07/2017.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Código fonte: https://developer.android.com/training/volley/requestqueue.html#network
 */
public class VolleyRequest {

    private static final String VOLLEY_TAG = "VolleyTag";

    private static VolleyRequest instance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    public static synchronized VolleyRequest getInstance() {
        if (instance == null) {
            throw new RuntimeException("Err: VolleyRequest not yet initialized.");
        }
        return instance;
    }

    public static void initialize(Context context) {
        if (instance == null) {
            if (context == null) {
                throw new RuntimeException("Invalid parameters: Context is null");
            } else {
                instance = new VolleyRequest(context);
            }
        } else {
            Log.d(VOLLEY_TAG, "VolleyRequest already initialized.");
        }
    }

    private VolleyRequest(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

}