package com.ua.viktor.github.data;

import android.content.UriMatcher;
import android.net.Uri;

import com.ua.viktor.github.ApplicationTestCase;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
/**
 * Created by viktor on 10.03.16.
 */
public class TestUriMatcher extends ApplicationTestCase {

    private static final long TEST_EVENT_ID = 12L;

    private static final Uri TEST_EVENT_DIR = GitContract.EventEntry.buildEventUri(TEST_EVENT_ID);
    private static final Uri TEST_EVENT = GitContract.EventEntry.CONTENT_URI;



    @Test
    public void testUriMatcher(){

        UriMatcher testMacher = GitProvider.buildUriMatcher();
        assertThat("Error: The MovieD URI was matched incorrectly",
                testMacher.match(TEST_EVENT_DIR), is(GitProvider.EVENT_WITH_ID));
        assertThat("Error: The Movies URI was matched incorrectly",
                testMacher.match(TEST_EVENT), is(GitProvider.EVENT));


        //assertEquals("Error: The MovieD URI was matched incorrectly",
        //        testMacher.match(TEST_MOVIE_DIR), MoviesProvider.MOVIE_ID);
    }
}
