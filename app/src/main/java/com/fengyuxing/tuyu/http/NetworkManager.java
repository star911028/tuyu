package com.fengyuxing.tuyu.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.fengyuxing.tuyu.util.L;


public class NetworkManager extends BaseManager<Context> {

    private static final String TAG = NetworkManager.class.getSimpleName();

    private static NetworkManager sInstance;
    private static boolean isInitialized = false;

    private Context mContext;

    public static boolean isInitialized() {
        return isInitialized;
    }

    public static void initialize(Context context) {
        L.d(TAG, "NetworkManager initializing...");
        if (sInstance == null) {
            synchronized (NetworkManager.class) {
                if (sInstance == null) {
                    sInstance = new NetworkManager(context);
                    isInitialized = true;
                }
            }
        }
        L.d(TAG, "NetworkManager initialized.");
    }

    public static NetworkManager getInstance() {
        if (sInstance != null) {
            return sInstance;
        } else {
            throw new IllegalStateException("NetworkManager was not initialized.");
        }
    }

    private NetworkManager(Context context) {
        mContext = context;
    }

    /**
     * Checks whether the device is able mTo connect mTo the network
     *
     * @param
     * @return
     */
    public boolean isNetworkAvailable() {
        final ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            final NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }  // else ignored
                }
            }  // else ignored
        } // else ignored
        return false;
    }
}
