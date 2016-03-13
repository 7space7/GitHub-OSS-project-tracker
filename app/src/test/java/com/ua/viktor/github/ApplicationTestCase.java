package com.ua.viktor.github;

import android.content.Context;

import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by viktor on 09.03.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = 18, reportSdk = 18, application = AplicationStub.class)
public abstract class ApplicationTestCase {


    public Context getContext(){
        return Robolectric.application.getApplicationContext();
    }
}
