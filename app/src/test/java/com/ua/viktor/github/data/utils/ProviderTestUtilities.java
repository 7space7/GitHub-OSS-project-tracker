package com.ua.viktor.github.data.utils;

/**
 * Created by viktor on 09.03.16.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;


import com.ua.viktor.github.data.GitContract;
import com.ua.viktor.github.data.GitDBHelper;


import java.util.Map;
import java.util.Set;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProviderTestUtilities {
    public static final long TEST_DATE = 1419033600L;  // December 20th, 2014

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertThat("Empty cursor returned. " + error, valueCursor.moveToFirst(), is(true));
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
    public static void validateWithoutCloseCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertThat("Empty cursor returned. " + error, valueCursor.moveToFirst(), is(true));
        validateCurrentRecord(error, valueCursor, expectedValues);
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertThat("Column '" + columnName + "' not found. " + error,
                    idx, not(-1));
            //assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertThat("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error,expectedValue,is(valueCursor.getString(idx)));
            //assertEquals("Value '" + entry.getValue().toString() +
            //       "' did not match the expected value '" +
            //        expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }



    public static ContentValues createEventValue(boolean favourite) {
        // Create a new map of values, where column names are the keys
        ContentValues testValues = new ContentValues();
        testValues.put(GitContract.EventEntry._ID, 1L);

        testValues.put(GitContract.EventEntry.COLUMN_DATE, "TESTING");

        return testValues;
    }
    public static ContentValues createVariableValues(int seed){
        ContentValues testValues = new ContentValues();

        testValues.put(GitContract.EventEntry._ID,1L + (long)seed);
        testValues.put(GitContract.EventEntry.COLUMN_DATE,"data");
        testValues.put(GitContract.EventEntry.COLUMN_EVENT_TYPE,"event");
        testValues.put(GitContract.EventEntry.COLUMN_ICON,"icon");
        testValues.put(GitContract.EventEntry.COLUMN_REPO_NAME,"name");
        testValues.put(GitContract.EventEntry.COLUMN_LOGIN,"login");
        return testValues;
    }


    public static long insertDefaultMovieWithReviewAndVideo(Context context){
        // insert our test records into the database
        GitDBHelper dbHelper = new GitDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testEventValues = ProviderTestUtilities.createEventValue(false);


        long locationRowId;
        long originalRowID;
        locationRowId = db.insert(GitContract.EventEntry.TABLE_EVENT, null, testEventValues);
        originalRowID = locationRowId;
        // Verify we got a row back.
        assertThat("Error: Failure to insert North Pole Location Values", locationRowId, not(-1L));

        return originalRowID;
    }
    public static long insertDefaultEventValues(Context context) {
        // insert our test records into the database
        GitDBHelper dbHelper = new GitDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = ProviderTestUtilities.createEventValue(false);


        long locationRowId;
        locationRowId = db.insert(GitContract.EventEntry.TABLE_EVENT, null, testValues);

        // Verify we got a row back.
        assertThat("Error: Failure to insert North Pole Location Values", locationRowId, not(-1L));
        //assertTrue("Error: Failure to insert North Pole Location Values", locationRowId != -1);

        return locationRowId;
    }

    /*
        Note that this only tests that the onChange function is called; it does not test that the
        correct Uri is returned.
     */
    public static class TestContentObserver extends ContentObserver {
        final HandlerThread mHT;
        boolean mContentChanged;

        static TestContentObserver getTestContentObserver() {
            HandlerThread ht = new HandlerThread("ContentObserverThread");
            ht.start();
            return new TestContentObserver(ht);
        }

        private TestContentObserver(HandlerThread ht) {
            super(new Handler(ht.getLooper()));
            mHT = ht;
        }

        // On earlier versions of Android, this onChange method is called
        @Override
        public void onChange(boolean selfChange) {
            onChange(selfChange, null);
        }

        @Override
        public void onChange(boolean selfChange, Uri uri) {
            mContentChanged = true;
        }

        public void waitForNotificationOrFail() {
            // Note: The PollingCheck class is taken from the Android CTS (Compatibility Test Suite).
            // It's useful to look at the Android CTS source for ideas on how to test your Android
            // applications.  The reason that PollingCheck works is that, by default, the JUnit
            // testing framework is not running on the main Android application thread.
            new PollingCheck(5000) {
                @Override
                protected boolean check() {
                    return mContentChanged;
                }
            }.run();
            mHT.quit();
        }
    }

    public static TestContentObserver getTestContentObserver() {
        return TestContentObserver.getTestContentObserver();
    }
}


