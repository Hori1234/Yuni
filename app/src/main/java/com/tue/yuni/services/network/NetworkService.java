package com.tue.yuni.services.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.Gravity;
import android.widget.Toast;

public class NetworkService {
    private NetworkService() {
        // Method body intentionally left black
    }

    /**
     * Checks whether the network is available.
     * @param context Context
     * @return True if the network is available, false otherwise
     */
    public static boolean networkAvailabilityHandler(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean result = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        //if network is not available, a message stating this will be shown
        if (result==false){
            handNetworkAvailable(context);
        }
        return result;
    }

    public static void handNetworkAvailable (Context context) {
        //this toast shows the user internet connection is unavailable
        Toast toast = Toast.makeText(context, "No internet connection available, please make sure you're connected and try again", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP, 0, 30);
        toast.show();
    }
}
