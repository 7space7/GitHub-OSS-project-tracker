package com.ua.viktor.social;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ua.viktor.social.authentication.LoginActivity;
import com.ua.viktor.social.utils.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String access_token = sp.getString(Constants.GITHUB_API_KEY, null);
        Log.v("lol", ""+access_token);

      //  GitHubClient client = ServiceGenerator.createService(GitHubClient.class);

        // Fetch and print a list of the contributors to this library.
       /* Call<List<Contributors>> call = client.contributors("Uaman", "UkmaSupport");
        call.enqueue(new Callback<List<Contributors>>() {
            @Override
            public void onResponse(Response<List<Contributors>> response, Retrofit retrofit) {
                List<Contributors> contributorses = response.body();

               // System.out.println(contributorses.get(1).login);

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });*/

    }
    }
