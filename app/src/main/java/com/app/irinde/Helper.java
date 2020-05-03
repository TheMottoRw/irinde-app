package com.app.irinde;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.TextView;

public class Helper {
    public Context ctx;
    public Helper(Context context){
        ctx = context;
    }
    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
    public void toggleNetworkConnectivityTextView(TextView tv){
        if(isNetworkConnected()) tv.setVisibility(View.GONE);
        else tv.setVisibility(View.VISIBLE);
        tv.refreshDrawableState();
    }
    public String getHost(String place){
        String local = "http://192.168.56.1/covid19",
                remote = "http://mbwira.rw/covid19";
        return place.equals("local")?local:remote;
    }

}
