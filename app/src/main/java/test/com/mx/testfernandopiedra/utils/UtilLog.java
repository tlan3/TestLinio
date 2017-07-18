package test.com.mx.testfernandopiedra.utils;

import android.util.Log;

import test.com.mx.testfernandopiedra.BuildConfig;

/**
 * Created by fpiedra on 08/02/16.
 */
public class UtilLog {

    public static final boolean D = BuildConfig.DEBUG;

    public static void I(String TAG, String msg) {
        if (D) {
            Log.i(TAG, msg);
        }
    }

    public static void W(String TAG, String msg) {
        if (D) {
            Log.w(TAG, msg);
        }
    }

    public static void D(String TAG, String msg) {
        if (D) {
            Log.d(TAG, msg);
        }
    }

    public static void E(String TAG, String msg) {
        if (D) {
            Log.e(TAG, msg);
        }
    }
}
