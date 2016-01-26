package com.ua.viktor.social.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.social.MainActivity;
import com.ua.viktor.social.R;
import com.ua.viktor.social.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends AccountAuthenticatorActivity {

    private static final String TAG = LoginActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = getIntent().getData();
                if (uri == null) {
                    Intent intent = new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(Constants.API_BASE_URL + "/authorize?client_id="
                                    + Constants.CLIENT_ID + "&redirect_uri=" + Constants.REDIRECT_URI + "&scope=repo"));
                    startActivity(intent);
                }
            }
        });

        // to get account manager
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = accountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ?  accounts[0] : null;
        String authToken = accountManager.peekAuthToken(account, authType);
        if(authToken!=null){
            finish();
            Intent intentM = new Intent(LoginActivity.this, MainActivity.class);
            intentM.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intentM);
        }
       // System.out.println(""+authToken);
    }


    @Override
    protected void onResume() {
        super.onResume();
        authorization();


    }

    public void authorization() {
        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(Constants.REDIRECT_URI)) {
            String code = uri.getQueryParameter("code");
            if (code != null) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder() //
                        .url("https://github.com/login/oauth/access_token?client_id="
                                + Constants.CLIENT_ID + "&client_secret=" + Constants.CLIENT_SECRET + "&code=" + code)
                        .header("Accept", "application/json")
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        final String json = response.body().string();
                        JSONObject jsonAcess = null;
                        try {
                            jsonAcess = new JSONObject(json);
                            final String accessToken = jsonAcess.getString("access_token");
                            if (accessToken!=null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        SharedPreferences.Editor spe = sp.edit();
                                        spe.putString(Constants.GITHUB_API_KEY, accessToken).apply();

                                        String accountType = "com.github";

                                        Account account = new Account("7space7", "com.github");
                                        AccountManager accountManager = AccountManager.get(getApplicationContext());
                                        accountManager.addAccountExplicitly(account, null, null);
                                        String authType = "password";
                                        String authToken = accessToken;
                                        accountManager.setAuthToken(account, authType, authToken);

                                        Intent intent = new Intent();
                                        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, "7space7");
                                        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                                        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
                                        ((AccountAuthenticatorActivity) LoginActivity.this).setAccountAuthenticatorResult(intent.getExtras());
                                        setResult(RESULT_OK, intent);

                                        Intent intentM = new Intent(LoginActivity.this, MainActivity.class);
                                        intentM.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intentM);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            } else if (uri.getQueryParameter("error") != null) {
                Log.e("ERROR: ", uri.getQueryParameter("error"));
            }
        }
    }

}

