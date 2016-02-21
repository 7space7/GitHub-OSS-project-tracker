package com.ua.viktor.github.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

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

    public static Date dateIso(String string){
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            return simpleDateFormat.parse(string);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }
    public static String getUserAuthName(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = accountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ? accounts[0] : null;
        String authName = account.name;
        return authName;
    }

    public static String getAuthToken(Context context) {
        AccountManager accountManager = AccountManager.get(context);
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = accountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ? accounts[0] : null;

        String authToken = accountManager.peekAuthToken(account, authType);
        return authToken;
    }


}
