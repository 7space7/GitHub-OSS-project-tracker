package com.ua.viktor.github.utils;

import com.ua.viktor.github.BuildConfig;

/**
 * Created by viktor on 14.01.16.
 */
public final class Constants {
    public static final String CLIENT_ID = BuildConfig.UNIQUE_CLIENT_ID;
    public static final String CLIENT_SECRET = BuildConfig.UNIQUE_CLIENT_SECRET;


    public static final String REDIRECT_URI = "your://redirecturi";
    public static final String API_BASE_URL = "https://github.com/login/oauth/";

    public static final String KEY_YOUR="yours";
    public static final String KEY_STARRED="starred";
    public static final String KEY_WATCHED="watched";

    public static final String KEY_FOLLOWING="following";
    public static final String KEY_FOLLOWERS="followers";
    public static final String KEY_ORGANIZATIONS="organizations";

    public static final String GITHUB_API_KEY ="gitHubApiToken";
    public static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";
    public static final String KEY_USER_NAME = "key_user_name";
    public static final String KEY_USER_LOGO ="user_logo" ;
    public static final String KEY_LOGIN="login";
    public static final String KEY_URL ="user_icon_url";
}
