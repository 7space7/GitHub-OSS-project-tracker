package com.ua.viktor.github.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.ua.viktor.github.R;
import com.ua.viktor.github.data.GitContract;
import com.ua.viktor.github.model.Event;
import com.ua.viktor.github.rest.EventService;
import com.ua.viktor.github.rest.ServiceGenerator;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.Utils;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import retrofit2.Call;

/**
 * Created by viktor on 04.03.16.
 */
public class GithubSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = GithubSyncAdapter.class.getSimpleName();
    public static final int SYNC_INTERVAL = 10;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL / 3;
    private List<Event> mEvents;
    private Call<List<Event>> call;

    public GithubSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.v("SyncAdapter", "onPerformSync");
        insertData();
        return;
    }

    /**
     * Helper method to have the sync adapter sync immediately
     *
     * @param context The context used to access the account service
     */
    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
    }


    public static Account getSyncAccount(Context context) {

        String accountType = "com.github";
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        Account[] accounts = accountManager.getAccountsByType(accountType);

        Account account = accounts.length != 0 ? accounts[0] : null;
        String authName = account.name;
        // Create the account type and default account
        Account newAccount = new Account(authName, accountType);


        // If the password doesn't exist, the account doesn't exist
        if (accounts.length == 0) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
            if (!accountManager.addAccountExplicitly(newAccount, null, null)) {
                return null;
            }
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call ContentResolver.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // we can enable inexact timers in our periodic sync
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
        }
    }

    private void insertData() {
        EventService client = ServiceGenerator.createService(EventService.class);

        String authName = Utils.getUserAuthName(getContext());
        call = client.userEvent(authName, Constants.CLIENT_ID, Constants.CLIENT_SECRET);

        try {
            mEvents = call.execute().body();
        } catch (IOException e) {
            // handle errors
        }
        int size = (mEvents == null) ? 0 : mEvents.size();
        if (mEvents != null && size != 0) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(mEvents.size());
            for (Event event : mEvents) {
                ContentValues eventValues = new ContentValues();
                eventValues.put(GitContract.EventEntry.COLUMN_ICON, event.getActor().getAvatar_url());
                eventValues.put(GitContract.EventEntry.COLUMN_DATE, event.getCreated_at());
                eventValues.put(GitContract.EventEntry.COLUMN_EVENT_TYPE, event.getType());
                eventValues.put(GitContract.EventEntry.COLUMN_LOGIN, event.getActor().getLogin());
                eventValues.put(GitContract.EventEntry.COLUMN_REPO_NAME, event.getRepo().getName());
                cVVector.add(eventValues);
            }

            int inserted = 0;
            // add to database
            if (cVVector.size() > 0) {
                ContentValues[] cvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvArray);
                getContext().getContentResolver().bulkInsert(GitContract.EventEntry.CONTENT_URI, cvArray);
            }

            Log.d(LOG_TAG, "Sync Complete. " + cVVector.size() + " Inserted");
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        /*
         * Since we've created an account
         */
        GithubSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        /*
         * Without calling setSyncAutomatically, our periodic sync will not be enabled.
         */
        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        /*
         * Finally, let's do a sync to get things started
         */
        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
