package com.startupcompany.wireframe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BaseActivity;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;
import com.startupcompany.wireframe.model.User;
import com.startupcompany.wireframe.util.ApiRouter;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignUpActivity extends BaseActivity {
    private Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUpGender();

        btnSignup = (Button) findViewById(R.id.sign_up);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText firstNameField = (EditText) findViewById(R.id.first_name_field);
                String firstName = firstNameField.getText().toString();

                EditText lastNameField = (EditText) findViewById(R.id.last_name_field);
                String lastName = lastNameField.getText().toString();

                EditText dayField = (EditText) findViewById(R.id.day_field);
                int day = Integer.parseInt(dayField.getText().toString());

                EditText monthField = (EditText) findViewById(R.id.month_field);
                int month = Integer.parseInt(monthField.getText().toString());

                EditText yearField = (EditText) findViewById(R.id.year_field);
                int year = Integer.parseInt(yearField.getText().toString());

                Spinner genderField = (Spinner) findViewById(R.id.gender_field);
                String gender = genderField.getItemAtPosition(genderField.getSelectedItemPosition()).toString();

                EditText usernameField = (EditText) findViewById(R.id.username_field_signup);
                String username = usernameField.getText().toString();

                EditText passwordField = (EditText) findViewById(R.id.password_field_signup);
                String password = passwordField.getText().toString();

                ApiRouter.withoutToken().signUp(firstName, lastName, day, month, year, gender, username, password, new Callback<User>() {
                    @Override
                    public void success(User user, Response response) {
                        setCurrentUser(user);

                        Intent i = new Intent(SignUpActivity.this, HomeActivity.class);
                        startActivity(i);

                        finish();
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        displayError(e);
                    }
                });
            }
        });
    }

    public void setUpGender() {
        Spinner gender = (Spinner) findViewById(R.id.gender_field);

        final ArrayList<String> options = new ArrayList<String>();

        options.add("Male");
        options.add("Female");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        gender.setAdapter(adapter);
    }
}
