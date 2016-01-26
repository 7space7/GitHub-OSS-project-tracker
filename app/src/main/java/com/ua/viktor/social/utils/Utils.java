package com.ua.viktor.social.utils;

import android.content.Context;

/**
 * Created by viktor on 14.01.16.
 */
public class Utils {
    private Context mContext = null;

    public Utils(Context con) {
        mContext = con;
    }

    public static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
