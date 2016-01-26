package com.ua.viktor.social.authentication;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by viktor on 26.01.16.
 */
public class GitHubAuthenticatorService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return new GitHubAuthenticator(this).getIBinder();
    }
}
