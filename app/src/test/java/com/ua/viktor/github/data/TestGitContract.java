package com.ua.viktor.github.data;

import android.net.Uri;

import com.ua.viktor.github.ApplicationTestCase;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
/**
 * Created by viktor on 09.03.16.
 */
public class TestGitContract extends ApplicationTestCase {

    private static final long TEST_Event_ID = 12L;

    @Test
    public void testBuildMovies(){
        Uri eventUri = GitContract.EventEntry.buildEventUri(TEST_Event_ID);
        assertThat(eventUri,is(notNullValue()));
        assertThat(Long.toString(TEST_Event_ID),is(eventUri.getLastPathSegment()));
        assertThat(eventUri.toString(),is(GitContract.BASE_CONTENT_URI + "/event/" + TEST_Event_ID));
    }

}