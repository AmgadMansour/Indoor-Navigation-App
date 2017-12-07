package com.startupcompany.wireframe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BaseActivity;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;
import com.startupcompany.wireframe.model.User;
import com.startupcompany.wireframe.util.ApiRouter;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends BaseActivity {
    private static final String EXTRA_REFERRER = "EXTRA_REFERRER";
    private Class<?> referrer;
    private Button btnLogin;
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_REFERRER)) {
            String referrerString = intent.getStringExtra(EXTRA_REFERRER);
            if (referrerString != null) {
                try {
                    referrer = Class.forName(referrerString);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        btnLogin = (Button) findViewById(R.id.login_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText usernameField = (EditText) findViewById(R.id.username_field);
                String username = usernameField.getText().toString();

                EditText passwordField = (EditText) findViewById(R.id.password_field);
                String password = passwordField.getText().toString();

                Log.d(getPackageName(), "Login with '" + username + "' and '" + password + "'");

                ApiRouter.withoutToken().login(username, password, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        setCurrentUser(user);
                        Log.d(getPackageName(), "" + user.getFirstName());

                        stopProgress();

                        if (referrer != null) {
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(intent);

                            finish();
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);

                        btnLogin.setEnabled(true);
                    }
                });

                if (referrer != null) {
                    Intent intent = new Intent(LoginActivity.this, referrer);
                    startActivity(intent);

                    finish();
                }
            }
        });

        btnSignup = (Button) findViewById(R.id.signup_btn);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(i);
            }
        });
    }
}
