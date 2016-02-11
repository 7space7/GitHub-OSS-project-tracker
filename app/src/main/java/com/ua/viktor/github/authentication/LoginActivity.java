package com.ua.viktor.github.authentication;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.ua.viktor.github.MainActivity;
import com.ua.viktor.github.R;
import com.ua.viktor.github.model.Users;
import com.ua.viktor.github.retrofit.ServiceGenerator;
import com.ua.viktor.github.retrofit.UserService;
import com.ua.viktor.github.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class LoginActivity extends AccountAuthenticatorActivity {

    private static final String TAG = LoginActivity.class.getName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button loginButton = (Button) findViewById(R.id.login);
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
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        String accountType = "com.github";
        String authType = "password";
        Account[] accounts = accountManager.getAccountsByType(accountType);
        Account account = accounts.length != 0 ?  accounts[0] : null;
        if (account!=null) {
            String authToken = accountManager.peekAuthToken(account, authType);

            if (authToken != null) {
                Intent intentM = new Intent(LoginActivity.this, MainActivity.class);
                intentM.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intentM);
                finish();
            }
        }


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
                             String accessToken = jsonAcess.getString("access_token");
                            if (accessToken!=null) {
                                getUserLogin(accessToken);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });

            } else if (uri.getQueryParameter("error") != null) {
                Log.e(TAG, uri.getQueryParameter("error"));
            }
        }
    }
    public void  addAccountManager(String access_token,String username){
        String accountType = "com.github";

        Account account = new Account(username, "com.github");
        AccountManager accountManager = AccountManager.get(getApplicationContext());
        accountManager.addAccountExplicitly(account, null, null);
        String authType = "password";
        String authToken = access_token;
        accountManager.setAuthToken(account, authType, authToken);

        Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, accountType);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
        ((AccountAuthenticatorActivity) LoginActivity.this).setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);

        Intent intentM = new Intent(LoginActivity.this, MainActivity.class);
        intentM.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intentM);
    }

    public void getUserLogin(final String accessToken){
        UserService client = ServiceGenerator.createService(UserService.class);

        // Fetch and print a list of the contributors to this library.
        retrofit2.Call<Users> call = client.getUserA(accessToken);

        call.enqueue(new retrofit2.Callback<Users>() {
            @Override
            public void onResponse(final retrofit2.Response<Users> response) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addAccountManager(accessToken, response.body().getLogin());
                    }
                });
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
    }

}

