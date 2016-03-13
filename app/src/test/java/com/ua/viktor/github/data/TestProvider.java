package com.ua.viktor.github.data;

import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.ua.viktor.github.ApplicationTestCase;
import com.ua.viktor.github.data.utils.ProviderTestUtilities;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by viktor on 09.03.16.
 */
public class TestProvider extends ApplicationTestCase {

    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    public void deleteAllRecordFromProvider(){
        getContext().getContentResolver().delete(
                GitContract.EventEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor = getContext().getContentResolver().query(
                GitContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertThat(0, is(cursor.getCount()));
        cursor.close();

    }

    public void deleteAllRecords(){
        deleteAllRecordFromProvider();
    }

    @Before
    public void setUp() throws Exception{
        deleteAllRecords();
    }

  /*  @Test

    public void testProviderRegistry(){
        PackageManager pm = getContext().getPackageManager();


        ComponentName componentName = new ComponentName(getContext().getPackageName(),
                GitProvider.class.getCanonicalName());

        try{

            ShadowApplication shadowApplication =  Robolectric.shadowOf(Robolectric.application);
            PackageManager shadowPackageManger = shadowApplication.getPackageManager();

            ProviderInfo providerInfo = shadowPackageManger.getProviderInfo(componentName,0);
            assertThat("Error: MovieProvider with authority: " + providerInfo.authority +
                            "instead of authority: " + GitContract.CONTENT_AUTHORITY,
                    providerInfo.authority, sameInstance(GitContract.CONTENT_AUTHORITY));

        }catch (PackageManager.NameNotFoundException e){
            assertThat("Error MovieProvider not registered at " + getContext().getPackageName(), true,
                    is(false));
            //assertTrue("Error EventProvider not registered at " + getContext().getPackageName(),false);
        }
    }
*/
    @Test
    /**
     * This test doesn´t touch the database. It verifies that the ContentProvider returns the
     * correct type for earch type of URI that it can handle.
     */
    public void testGetType(){
        String type = getContext().getContentResolver().getType(GitContract.EventEntry.CONTENT_URI);

        assertThat("Error: the MoviesEntry CONTENT_URI should return MovieEntry.CONTENT_TYPE",
                GitContract.EventEntry.CONTENT_ITEM_TYPE, is(type));
    }

    @Test
    /**
     * This test uses the database directly to insert a movie and then uses the content provider
     *  to read out the data.
     */
    public void testBasicMovieQuery(){
        //Insert our test records into the database
        GitDBHelper dbHelper = new GitDBHelper(getContext());

        ContentValues movieValue = ProviderTestUtilities.createEventValue(false);

        ContentValues allInner = new ContentValues();
        allInner.putAll(movieValue);

        long eventRowId = ProviderTestUtilities.insertDefaultMovieWithReviewAndVideo(getContext());

        Cursor movieCursor = getContext().getContentResolver().query(
                GitContract.EventEntry.buildEventUri(eventRowId),
                null,
                null,
                null,
                null
        );

        ProviderTestUtilities.validateCursor("testBasicMovieQuery", movieCursor, allInner);

    }


    @Test
    public void testUpdateEvent(){
        //Create a new map of values, where column names are the keys
        ContentValues values = ProviderTestUtilities.createEventValue(true);

        Uri movieUri = getContext().getContentResolver().insert(
                GitContract.EventEntry.CONTENT_URI, values);
        long movieRowId = ContentUris.parseId(movieUri);

        //Verfity we got a row back
        assertThat(movieRowId,not(-1L));
        //assertTrue(movieRowId != -1);

        ContentValues updateValues = new ContentValues(values);
        //Update data
        updateValues.put(GitContract.EventEntry._ID,movieRowId);
        updateValues.put(GitContract.EventEntry.COLUMN_DATE, 0);

        //Create a cursor with observer to make sure that the content provider is notifying
        //the observer as expected
        Cursor movieCursor = getContext().getContentResolver().query(
                GitContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null);




        ProviderTestUtilities.TestContentObserver testContentObserver =
                ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(GitContract.EventEntry.buildEventUri(movieRowId),
                true,
                testContentObserver);
        int count = getContext().getContentResolver().update(
                GitContract.EventEntry.buildEventUri(movieRowId), updateValues, GitContract.EventEntry._ID + "= ?",
                new String[]{Long.toString(movieRowId)});
        testContentObserver.waitForNotificationOrFail();
        assertThat(count, is(1));


        //Test to make sure our observer is called. If not, we throw an assertion.

        //assertEquals(count,1);
        getContext().getContentResolver().unregisterContentObserver(testContentObserver);

        movieCursor.close();

        //A cursor is your primary interface to query result.

        Cursor cursor = getContext().getContentResolver().query(
                GitContract.EventEntry.CONTENT_URI,
                null,
                GitContract.EventEntry._ID + " = " + movieRowId,
                null,
                null
        );

        ProviderTestUtilities.validateCursor("testUpdateMovie. Error validating location entry update.",
                cursor, updateValues);

        cursor.close();

    }

    @Test
    /**
     * Make sure we call still delete after adding/updating stuff
     */
    public void testInsertReadProvider(){
        ContentValues testValues = ProviderTestUtilities.createEventValue(false);

        //Register a content observer for our insert. This time. directly with the content resolver
        ProviderTestUtilities.TestContentObserver testContentObserver = ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(GitContract.EventEntry.CONTENT_URI,
                true,
                testContentObserver);
        Uri eventUri = getContext().getContentResolver().insert(GitContract.EventEntry.CONTENT_URI, testValues);

        //Verify if the insert call the content resolver
        testContentObserver.waitForNotificationOrFail();
        getContext().getContentResolver().unregisterContentObserver(testContentObserver);

        long movieRowId = ContentUris.parseId(eventUri);

        //Verify we got a row back
        assertThat(movieRowId, not(-1L));
        // assertTrue(movieRowId != -1);

        //Data´s inserted. In Theory. Now pull some out to stare at it and verify it made the
        //round trip.

        //A cursor is your primary interface to the query result
        Cursor cursor = getContext().getContentResolver().query(
                GitContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        ProviderTestUtilities.validateCursor("testInsertReadProvider. Error validating MovieEntry",
                cursor, testValues);
    }

    @Test
    /**
     * Make sure we can till delete after adding/updating stuff
     */
    public void testDeleteRecords(){
        testInsertReadProvider();

        //Register a content observer for our location delete.
        ProviderTestUtilities.TestContentObserver eventObserver = ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(GitContract.EventEntry.CONTENT_URI,
                true, eventObserver);

        deleteAllRecordFromProvider();

        eventObserver.waitForNotificationOrFail();

        getContext().getContentResolver().unregisterContentObserver(eventObserver);
    }

    static private final int BULK_INSERT_RECORDS_TO_INSERT = 10;
    static ContentValues[] createBulkInsertMoviesValues(){
        long currentTestDate = ProviderTestUtilities.TEST_DATE;
        long millisecondsInADay = 100*60*60*24;
        ContentValues[] returnContentValues = new ContentValues[BULK_INSERT_RECORDS_TO_INSERT];

        for (int i = 0; i<BULK_INSERT_RECORDS_TO_INSERT; i++, currentTestDate +=millisecondsInADay){
            returnContentValues[i] = ProviderTestUtilities.createVariableValues(i);
        }
        return returnContentValues;
    }



    @Test
    public void deleteEvent(){


        getContext().getContentResolver().delete(
                GitContract.EventEntry.buildEventUri(1),
                null,
                null
        );
    }


    @Test
    public void testBulkInsert(){

        //Now we can bulkInsert some event.
        ContentValues[] bulkInsertContentValues = createBulkInsertMoviesValues();

        ProviderTestUtilities.TestContentObserver movieObserver = ProviderTestUtilities.getTestContentObserver();
        getContext().getContentResolver().registerContentObserver(GitContract.EventEntry.CONTENT_URI, true,
                movieObserver);

        int insertCount = getContext().getContentResolver().bulkInsert(GitContract.EventEntry.CONTENT_URI,
                bulkInsertContentValues);

        movieObserver.waitForNotificationOrFail();
        getContext().getContentResolver().unregisterContentObserver(movieObserver);

        assertThat(insertCount, is(BULK_INSERT_RECORDS_TO_INSERT));
        //assertEquals(insertCount, BULK_INSERT_RECORDS_TO_INSERT);

        Cursor cursor = getContext().getContentResolver().query(
                GitContract.EventEntry.CONTENT_URI,
                null,
                null,
                null,
                GitContract.EventEntry._ID
        );

        //we should have as many record in the database as we have inserted
        assertThat(cursor.getCount(), is(BULK_INSERT_RECORDS_TO_INSERT));
        //assertEquals(cursor.getCount(), BULK_INSERT_RECORDS_TO_INSERT);

        cursor.moveToFirst();
        for (int i = 0; i < BULK_INSERT_RECORDS_TO_INSERT; i++, cursor.moveToNext()){
            ProviderTestUtilities.validateCurrentRecord("testBulkInsert. Error validating MovieEntry " + i,
                    cursor, bulkInsertContentValues[i]);
        }
        cursor.close();
    }
}