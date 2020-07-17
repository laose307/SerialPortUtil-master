package com.serialportlibrary.util;

import android.util.Log;

public class LogUtil {
    public static boolean isDebug = false;

    public static void e(String msg) {
        if (isDebug) {
            Log.e("SerialPort", msg);
        }
    }

    public static void d(String msg) {
        if (isDebug) {
            Log.d("SerialPort", msg);
        }
    }

}
