package com.ua.viktor.github.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by viktor on 04.03.16.
 */
public class GitDBHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = GitDBHelper.class.getSimpleName();


    private static final String DATABASE_NAME = "event.db";
    private static final int DATABASE_VERSION = 3;

    public GitDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TAG_TABLE = "CREATE TABLE " +
                GitContract.EventEntry.TABLE_EVENT + "(" + GitContract.EventEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GitContract.EventEntry.COLUMN_LOGIN + " TEXT NOT NULL, " +
                GitContract.EventEntry.COLUMN_EVENT_TYPE + " TEXT, " +
                GitContract.EventEntry.COLUMN_REPO_NAME + " TEXT NOT NULL , " +
                GitContract.EventEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                GitContract.EventEntry.COLUMN_ICON + " TEXT NOT NULL);";

        sqLiteDatabase.execSQL(SQL_CREATE_TAG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");
        // Drop the table
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GitContract.EventEntry.TABLE_EVENT);
        sqLiteDatabase.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                GitContract.EventEntry.TABLE_EVENT + "'");

        // re-create database
        onCreate(sqLiteDatabase);
    }

}


