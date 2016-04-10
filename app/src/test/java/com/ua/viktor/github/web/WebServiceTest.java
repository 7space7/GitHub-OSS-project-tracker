package com.ua.viktor.github.web;

import com.ua.viktor.github.AplicationStub;
import com.ua.viktor.github.GradleRobolectricTestRunner;
import com.ua.viktor.github.model.Event;
import com.ua.viktor.github.model.Organizations;
import com.ua.viktor.github.model.Repositories;
import com.ua.viktor.github.model.Users;
import com.ua.viktor.github.rest.EventService;
import com.ua.viktor.github.rest.OrganizationService;
import com.ua.viktor.github.rest.RepositoryService;
import com.ua.viktor.github.rest.ServiceGenerator;
import com.ua.viktor.github.rest.UserService;
import com.ua.viktor.github.utils.Constants;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Created by viktor on 10.04.16.
 */

@RunWith(GradleRobolectricTestRunner.class)
@Config(emulateSdk = 18, reportSdk = 18, application = AplicationStub.class)
public class WebServiceTest {
    private String mLogin = "7space7";

    @Test
    public void testEventService() throws Exception {

        Call<List<Event>> call;

        EventService client = ServiceGenerator.createService(EventService.class);
        call = client.userEvent(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);

        List<Event> events = call.execute().body();

        Assert.assertNotNull(events);

    }

    @Test
    public void testOrganizationService() throws Exception {
        String org = "google";
        Call<ArrayList<Organizations>> call;

        OrganizationService client = ServiceGenerator.createService(OrganizationService.class);
        call = client.getOrgs(org, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ArrayList<Organizations> orgs = call.execute().body();

        Assert.assertNotNull(orgs);

        call = client.getFollowers(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ArrayList<Organizations> followers = call.execute().body();

        Assert.assertNotNull(followers);

        call = client.getFollowing(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ArrayList<Organizations> following = call.execute().body();

        Assert.assertNotNull(following);


    }
    @Test
    public void testRepositoryService() throws Exception {
        Call<ArrayList<Repositories>> call;

        RepositoryService client = ServiceGenerator.createService(RepositoryService.class);
        call = client.userRepositories(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ArrayList<Repositories> userRepos = call.execute().body();

        Assert.assertNotNull(userRepos);

        call = client.repoStarred(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ArrayList<Repositories> repoStarred = call.execute().body();

        Assert.assertNotNull(repoStarred);

        call = client.repoWatched(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        ArrayList<Repositories> repoWatched = call.execute().body();

        Assert.assertNotNull(repoWatched);


    }
    @Test
    public void testUserService() throws Exception {
        Call<Users> call;

        UserService client = ServiceGenerator.createService(UserService.class);
        call = client.getSingleUser(mLogin, Constants.CLIENT_ID, Constants.CLIENT_SECRET);
        Users user = call.execute().body();

        Assert.assertNotNull(user);

    }
}
