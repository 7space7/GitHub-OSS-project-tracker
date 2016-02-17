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
import com.ua.viktor.github.utils.CircleTransform;
import com.ua.viktor.github.utils.Constants;

public class UserDetailActivity extends AppCompatActivity {
    private String mLogin;
    private String mUrl;
    private ImageView mUserLogo;
    private TextView mUserLogin;

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
                        .intoTextColor(mUserLogin)
                );


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_detail_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_unfollow:
                item.setTitle("UNFOLLOW");
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        // tabLayout.addTab(tabLayout.newTab().setText("Events"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager_user_detail);
        final PagerUserDetail adapter = new PagerUserDetail
                (getSupportFragmentManager(), tabLayout.getTabCount(), mLogin);
        viewPager.setAdapter(adapter);
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
}
