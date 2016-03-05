package com.ua.viktor.github.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by viktor on 04.03.16.
 */
public class GitContract {
    public static final String CONTENT_AUTHORITY = "com.ua.viktor.github.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class EventEntry implements BaseColumns {
        // table name
        public static final String TABLE_EVENT = "event";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOGIN = "login";
        public static final String COLUMN_EVENT_TYPE = "event_type";
        public static final String COLUMN_REPO_NAME = "repo_name";


        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_EVENT).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_EVENT;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_EVENT;

        // for building URIs on insertion
        public static Uri buildTagsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
