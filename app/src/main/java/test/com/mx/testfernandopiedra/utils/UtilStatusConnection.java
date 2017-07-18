package test.com.mx.testfernandopiedra.utils;

/**
 * Created by fernandostone on 14/07/17.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import test.com.mx.testfernandopiedra.R;

/**
 * Created by fpiedra on 13/10/15.
 */
public class UtilStatusConnection {

    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static String getConnectivityStatusString(Context context) {
        int conn = UtilStatusConnection.getConnectivityStatus(context);
        String status = null;
        if (conn == UtilStatusConnection.TYPE_WIFI) {
            status = "Wifi habilitada";
        } else if (conn == UtilStatusConnection.TYPE_MOBILE) {
            status = "Datos móviles habilitados";
        } else
        if (conn == UtilStatusConnection.TYPE_NOT_CONNECTED) {
            status = context.getString(R.string.sin_conexion_internet);
        }
        return status;
    }


    public static boolean isConected(Context mContext){
        return UtilStatusConnection.TYPE_NOT_CONNECTED != UtilStatusConnection.getConnectivityStatus(mContext);
    }

}

