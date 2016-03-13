package com.ua.viktor.github.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.florent37.picassopalette.PicassoPalette;
import com.squareup.picasso.Picasso;
import com.ua.viktor.github.R;
import com.ua.viktor.github.adapter.pager.PagerUserDetail;
import com.ua.viktor.github.model.Users;
import com.ua.viktor.github.rest.ServiceGenerator;
import com.ua.viktor.github.rest.UserService;
import com.ua.viktor.github.utils.CircleTransform;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailActivity extends AppCompatActivity {
    private static final String FOLLOWIN_KEY = "status";
    private String mLogin;
    private String mUrl;
    private ImageView mUserLogo;
    private TextView mUserLogin;
    private Call<Users> mUsersCall;
    private Menu mMenu;
    private Boolean status = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        Intent intent = getIntent();

        mLogin = intent.getStringExtra(Constants.KEY_LOGIN);
        mUrl = intent.getStringExtra(Constants.KEY_URL);

        initializeScreen();


        mUserLogin.setText(mLogin);

        Picasso.with(getApplicationContext()).load(mUrl)
                .transform(new CircleTransform())
                .fit().centerInside()
                .into(mUserLogo,
                        PicassoPalette.with(mUrl, mUserLogo)
                                .use(PicassoPalette.Profile.VIBRANT_LIGHT)
                        // .intoTextColor(mUserLogin)
                );

        if (savedInstanceState == null) {
            checkIsFollowing();
        } else {
            status = savedInstanceState.getBoolean(FOLLOWIN_KEY);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_detail_main, menu);
        this.mMenu = menu;
        if (status != null) {
            updateMenuTitles(status);
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (status != null) {
            outState.putBoolean(FOLLOWIN_KEY, status);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_unfollow:
                String val = item.getTitle().toString();
                if (val.equals("UNFOLLOW")) {
                    unfollowUser();
                } else if (val.equals("FOLLOW")) {
                    followUser();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateMenuTitles(boolean status) {
        if (mMenu != null) {
            MenuItem menuItem = mMenu.findItem(R.id.action_unfollow);
            if (status) {
                menuItem.setTitle("UNFOLLOW");
            } else {
                menuItem.setTitle("FOLLOW");
            }
        }
    }


    private void initializeScreen() {
        mUserLogo = (ImageView) findViewById(R.id.user_logo_detail);
        mUserLogin = (TextView) findViewById(R.id.user_name_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_user);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout_user_detail);
        tabLayout.addTab(tabLayout.newTab().setText("INFO"));
        tabLayout.addTab(tabLayout.newTab().setText("REPOSITORIES"));
        tabLayout.addTab(tabLayout.newTab().setText("Events"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_user_detail);
        final PagerUserDetail adapter = new PagerUserDetail
                (getSupportFragmentManager(), tabLayout.getTabCount(), mLogin);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private String getUserLogin() {
        Intent intent = getIntent();

        return intent.getStringExtra(Constants.KEY_LOGIN);
    }

    private void checkIsFollowing() {
        String login = getUserLogin();

        String authToken = Utils.getAuthToken(getApplication());

        UserService client = ServiceGenerator.createService(UserService.class);
        mUsersCall = client.checkUserFollowing(login, authToken);
        mUsersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Response<Users> response) {
                if (response.code() == 204) {
                    status = true;
                    updateMenuTitles(status);
                } else if (response.code() == 404) {
                    status = false;
                    updateMenuTitles(status);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void unfollowUser() {

        String login = getUserLogin();

        String authToken = Utils.getAuthToken(getApplication());

        UserService client = ServiceGenerator.createService(UserService.class);

        mUsersCall = client.deleteFollowUser(login, authToken);
        mUsersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Response<Users> response) {
                if (response.code() == 204) {
                    status = false;
                    updateMenuTitles(status);
                } else if (response.code() == 404) {
                    status = true;
                    updateMenuTitles(status);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

    private void followUser() {
        String login = getUserLogin();

        String authToken = Utils.getAuthToken(getApplication());

        UserService client = ServiceGenerator.createService(UserService.class);

        mUsersCall = client.followUser(login, authToken);
        mUsersCall.enqueue(new Callback<Users>() {
            @Override
            public void onResponse(Response<Users> response) {
                if (response.code() == 204) {
                    status = true;
                    updateMenuTitles(status);
                } else if (response.code() == 404) {
                    status = false;
                    updateMenuTitles(status);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}
