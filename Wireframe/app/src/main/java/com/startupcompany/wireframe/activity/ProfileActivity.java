package com.startupcompany.wireframe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;
import com.startupcompany.wireframe.model.User;

public class ProfileActivity extends BasePrivateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        final User user = getCurrentUser();

        TextView txtFirstName = (TextView) findViewById(R.id.profile_first_name);
        txtFirstName.setText(user.getFirstName());

        TextView txtLastName = (TextView) findViewById(R.id.profile_last_name);
        txtLastName.setText(user.getLastName());

        TextView txtDob = (TextView) findViewById(R.id.profile_dob);
        txtDob.setText(user.getDay() + "/" +
                user.getMonth() + "/" + user.getYear());

        TextView txtGender = (TextView) findViewById(R.id.profile_gender);
        txtGender.setText(user.getGender());
    }
}
