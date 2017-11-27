package com.nd.countrylist;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class CommonUtils {
    Activity mActivity;
    Context mContext;
    ConnectivityManager mConnectivityManager;
    NetworkInfo mNetworkInfo;

    public static String APIURL = "https://restcountries.eu/rest/v2/";

    public CommonUtils() {
        // Todo
    }

    public CommonUtils(Activity activity) {
        this.mActivity = activity;
    }

    public CommonUtils(Activity activity, Context context) {
        this.mActivity = activity;
        this.mContext = context;
    }

    public boolean check_Internet() {
        boolean status = false;

        mConnectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();

        if (mNetworkInfo != null && mNetworkInfo.isConnectedOrConnecting()) {
            status = true;
        }

        return status;
    }
}
