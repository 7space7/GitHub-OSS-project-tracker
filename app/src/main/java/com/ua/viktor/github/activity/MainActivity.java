package com.ua.viktor.github.activity;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.ua.viktor.github.R;
import com.ua.viktor.github.authentication.LoginActivity;
import com.ua.viktor.github.fragment.EventUserFragment;
import com.ua.viktor.github.fragment.PeopleOrgPager;
import com.ua.viktor.github.fragment.RepositoryPager;
import com.ua.viktor.github.utils.CircleTransform;
import com.ua.viktor.github.utils.Constants;
import com.ua.viktor.github.utils.Utils;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String LOG_TAG =MainActivity.class.getSimpleName() ;
    private ImageView mUserLogo;
    private TextView mUseLogin;
    private TextView mUserName;
    private static final String TAG = MainActivity.class.getName();
    private int mCurrentSelectedPosition = 0;
    private AccountManager mAccountManager;
    private String mUserIcon;
    private String mUser_Name;

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(Constants.STATE_SELECTED_POSITION);
            displayView(mCurrentSelectedPosition);
        } else {
            displayView(R.id.nav_event);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        initializeNavigationView();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void displayView(int viewId) {

        Fragment fragment = null;
        String title = getString(R.string.app_name);

        switch (viewId) {
            case R.id.nav_event:
                fragment = new EventUserFragment();
                title = "Events";
                break;
            case R.id.nav_repository:
                fragment = new RepositoryPager();
                title = "Repositories";
                break;
            case R.id.nav_people:
                fragment = new PeopleOrgPager();
                title = "People & Organizations";
                break;
            case R.id.nav_sign_out:
                logOut();
            case R.id.nav_about:

                break;
        }

        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContent, fragment);
            //ft.addToBackStack("Events");
            ft.commit();
        }

        // set the toolbar title

        setTitle(title);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int position = item.getItemId();
        mCurrentSelectedPosition = position;
        displayView(position);
        return true;
    }

    public void logOut() {
        mAccountManager = AccountManager.get(getApplicationContext());
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = mAccountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ? accounts[0] : null;

        if (accounts.length > 0) {
            mAccountManager.removeAccount(account, new AccountManagerCallback<Boolean>() {
                @Override
                public void run(AccountManagerFuture<Boolean> future) {
                    try {
                        if (future.getResult()) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }, null);

        }
    }

    public void initializeNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.inflateHeaderView(R.layout.nav_header_main);

        mAccountManager = AccountManager.get(getApplicationContext());
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = mAccountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ? accounts[0] : null;


        mUser_Name = mAccountManager.getUserData(account, Constants.KEY_USER_NAME);
        mUserIcon = mAccountManager.getUserData(account, Constants.KEY_USER_LOGO);
        String authToken = mAccountManager.peekAuthToken(account, authType);
        Log.v(TAG, authToken);

        mUserLogo = (ImageView) headerLayout.findViewById(R.id.user_logo);
        mUseLogin = (TextView) headerLayout.findViewById(R.id.login_Name);
        mUserName = (TextView) headerLayout.findViewById(R.id.text_eventName);

        mUseLogin.setText(Utils.getUserAuthName(getApplicationContext()));
        mUserName.setText(mUser_Name);
        Picasso.with(getApplication()).load(mUserIcon)
                .transform(new CircleTransform())
                .into(mUserLogo);

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

}
