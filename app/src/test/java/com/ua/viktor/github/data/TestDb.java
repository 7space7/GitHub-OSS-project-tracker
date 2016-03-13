package com.ua.viktor.github.data;

/**
 * Created by viktor on 09.03.16.
 */

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ua.viktor.github.ApplicationTestCase;
import com.ua.viktor.github.data.utils.ProviderTestUtilities;

import org.junit.Before;
import org.junit.Test;

import java.util.HashSet;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by virtu on 22/08/2015.
 */
public class TestDb extends ApplicationTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    // Since we want each test to start with a clean slate
    void deleteTheDatabase() {

        getContext().deleteDatabase(GitDBHelper.DATABASE_NAME);
    }

    /*
    This function gets called before each test is executed to delete the database.  This makes
    sure that we always have a clean test.
    */
    @Before
    public void setUp(){
        deleteTheDatabase();
    }

    @Test
    public void testCreateDb() throws Throwable{
        // build a HashSet of all of the table names we wish to look for
        // Note that there will be another table in the DB that stores the
        // Android metadata (db version information)
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(GitContract.EventEntry.TABLE_EVENT);

        getContext().deleteDatabase(GitDBHelper.DATABASE_NAME);
        SQLiteDatabase db = new GitDBHelper(
                getContext()).getWritableDatabase();
        assertThat(db.isOpen(),is(true));
        //assertEquals(true, db.isOpen());

        // have we created the tables we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertThat("Error: This means that the database has not been created correctly",
                c.moveToFirst(),is(true));
        //assertTrue("Error: This means that the database has not been created correctly",
        //        c.moveToFirst());

        // verify that the tables have been created
        do {
            tableNameHashSet.remove(c.getString(0));
        } while( c.moveToNext() );

        // if this fails, it means that your database doesn't contain both the location entry
        // and weather entry tables
        assertThat("Error: Your database was created without both the location entry and weather entry tables",
                tableNameHashSet.isEmpty(),is(true));
        // assertTrue("Error: Your database was created without both the location entry and weather entry tables",
        //        tableNameHashSet.isEmpty());

        // now, do our tables contain the correct columns?
        c = db.rawQuery("PRAGMA table_info(" + GitContract.EventEntry.TABLE_EVENT + ")",
                null);

        assertThat("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst(),is(true));
        //assertTrue("Error: This means that we were unable to query the database for table information.",
        //        c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> eventColumnHashSet = new HashSet<String>();
        eventColumnHashSet.add(GitContract.EventEntry._ID);
        eventColumnHashSet.add(GitContract.EventEntry.COLUMN_DATE);
        eventColumnHashSet.add(GitContract.EventEntry.COLUMN_EVENT_TYPE);
        eventColumnHashSet.add(GitContract.EventEntry.COLUMN_ICON);
        eventColumnHashSet.add(GitContract.EventEntry.COLUMN_REPO_NAME);
        eventColumnHashSet.add(GitContract.EventEntry.COLUMN_LOGIN);
        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            eventColumnHashSet.remove(columnName);
        } while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertThat("Error: The database doesn't contain all of the required location entry columns",
                eventColumnHashSet.isEmpty(),is(true));
        //assertTrue("Error: The database doesn't contain all of the required location entry columns",
        //        MovieColumnHashSet.isEmpty());
        db.close();
    }

    @Test
    public void testEventTable() {
        insertEvent();
    }


    public long insertEvent() {
        // First step: Get reference to writable database
        // If there's an error in those massive SQL table creation Strings,
        // errors will be thrown here when you try to get a writable database.
        GitDBHelper dbHelper = new GitDBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Second Step: Create ContentValues of what you want to insert
        // (you can use the createNorthPoleLocationValues if you wish)
        ContentValues testValues = ProviderTestUtilities.createEventValue(false);

        // Third Step: Insert ContentValues into database and get a row ID back
        long locationRowId;
        locationRowId = db.insert(GitContract.EventEntry.TABLE_EVENT, null, testValues);

        // Verify we got a row back.
        assertThat(locationRowId, not(-1L));
        // assertTrue(locationRowId != -1);

        // Data's inserted.  IN THEORY.  Now pull some out to stare at it and verify it made
        // the round trip.

        // Fourth Step: Query the database and receive a Cursor back
        // A cursor is your primary interface to the query results.
        Cursor cursor = db.query(
                GitContract.EventEntry.TABLE_EVENT,  // Table to Query
                null, // all columns
                null, // Columns for the "where" clause
                null, // Values for the "where" clause
                null, // columns to group by
                null, // columns to filter by row groups
                null // sort order
        );

        // Move the cursor to a valid database row and check to see if we got any records back
        // from the query
        assertThat("Error: No Records returned from location query", cursor.moveToFirst(), is(true));
        //assertTrue("Error: No Records returned from location query", cursor.moveToFirst());

        // Fifth Step: Validate data in resulting Cursor with the original ContentValues
        // (you can use the validateCurrentRecord function in ProviderTestUtilities to validate the
        // query if you like)
        ProviderTestUtilities.validateCurrentRecord("Error: Location Query Validation Failed",
                cursor, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertThat("Error: No Records returned from location query",cursor.moveToNext(),is(false));
        //assertFalse( "Error: More than one record returned from location query",
        //       cursor.moveToNext() );

        // Sixth Step: Close Cursor and Database
        cursor.close();
        db.close();
        return locationRowId;
    }



}