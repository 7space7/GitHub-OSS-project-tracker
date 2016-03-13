package com.ua.viktor.github.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by viktor on 04.03.16.
 */
public class GithubSyncService extends Service {
    private static final Object sSyncAdapterLock = new Object();
    private static GithubSyncAdapter sGithubSyncAdapter = null;

    @Override
    public void onCreate() {
        Log.d("SunshineSyncService", "onCreate - SunshineSyncService");
        synchronized (sSyncAdapterLock) {
            if (sGithubSyncAdapter == null) {
                sGithubSyncAdapter = new GithubSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sGithubSyncAdapter.getSyncAdapterBinder();
    }
}
