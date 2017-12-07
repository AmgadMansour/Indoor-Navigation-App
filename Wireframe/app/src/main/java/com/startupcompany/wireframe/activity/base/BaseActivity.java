package com.startupcompany.wireframe.activity.base;

/**
 * Created by mohamedabdel-azeem on 3/16/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.model.User;

public abstract class BaseActivity extends AppCompatActivity {
    private static final String PREF_USER_ID = "PREF_USER_ID";
    private static final String PREF_USER_F_NAME = "PREF_USER_F_NAME";
    private static final String PREF_USER_L_NAME = "PREF_USER_L_NAME";
    private static final String PREF_USER_GENDER = "PREF_USER_GENDER";
    private static final String PREF_USER_DAY = "PREF_USER_DAY";
    private static final String PREF_USER_MONTH = "PREF_USER_MONTH";
    private static final String PREF_USER_YEAR = "PREF_USER_YEAR";
    private static final String PREF_USER_TOKEN = "PREF_USER_TOKEN";
    private static final String PREF_USER_EN_ROUTE = "PREF_USER_EN_ROUTE";
    private static final String PREF_USER_X_COORDINATE = "PREF_USER_X_COORDINATE";
    private static final String PREF_USER_Y_COORDINATE = "PREF_USER_Y_COORDINATE";
    private static final String PREF_USER_USERNAME = "PREF_USER_USERNAME";

    private User currentUser;
    private int inProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        /*if (isLoggedIn()) {
            inflater.inflate(R.menu.menu_eshop, menu);
        }
        else {
            inflater.inflate(R.menu.menu_eshop_guest, menu);
        }*/

        return isRefreshable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshViews();
                return true;
            case R.id.action_logout:
                setCurrentUser(null);

//			Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
//			startActivity(intent);

                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }*/
        return super.onOptionsItemSelected(item);
    }

    protected boolean isRefreshable() {
        return true;
    }

    protected void refreshViews() {
    }

    protected void startProgress() {
        setProgressBarIndeterminateVisibility(true);
        inProgress++;
    }

    protected void stopProgress() {
        if (--inProgress == 0) {
            setProgressBarIndeterminateVisibility(false);
        }
    }

    protected void displayError(Exception e) {
        setProgressBarIndeterminateVisibility(false);
        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT)
                .show();
    }

    protected boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    protected User getCurrentUser() {
        if (currentUser == null) {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

            if (sharedPreferences.contains(PREF_USER_TOKEN)) {
                currentUser = new User();
                currentUser.setId(sharedPreferences.getLong(PREF_USER_ID, 0));
                currentUser.setFirstName(sharedPreferences.getString(PREF_USER_F_NAME, null));
                currentUser.setLastName(sharedPreferences.getString(PREF_USER_L_NAME, null));
                currentUser.setGender(sharedPreferences.getString(PREF_USER_GENDER, null));
                currentUser.setDay(sharedPreferences.getInt(PREF_USER_DAY, 0));
                currentUser.setMonth(sharedPreferences.getInt(PREF_USER_MONTH, 0));
                currentUser.setYear(sharedPreferences.getInt(PREF_USER_YEAR, 0));
                currentUser.setToken(sharedPreferences.getString(PREF_USER_TOKEN, null));
                currentUser.setEnRoute(sharedPreferences.getBoolean(PREF_USER_EN_ROUTE, false));
                currentUser.setxCoordinate(sharedPreferences.getFloat(PREF_USER_X_COORDINATE, 0.0f));
                currentUser.setyCoordinate(sharedPreferences.getFloat(PREF_USER_Y_COORDINATE, 0.0f));
                currentUser.setUsername(sharedPreferences.getString(PREF_USER_USERNAME, null));
            }
        }

        return currentUser;
    }

    protected void setCurrentUser(User user) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Editor p = sharedPreferences.edit();

        if ((currentUser = user) != null) {
            p.putLong(PREF_USER_ID, currentUser.getId());
            p.putString(PREF_USER_F_NAME, currentUser.getFirstName());
            p.putString(PREF_USER_L_NAME, currentUser.getLastName());
            p.putString(PREF_USER_GENDER, currentUser.getGender());
            p.putInt(PREF_USER_DAY, currentUser.getDay());
            p.putInt(PREF_USER_MONTH, currentUser.getMonth());
            p.putInt(PREF_USER_YEAR, currentUser.getYear());
            p.putString(PREF_USER_TOKEN, currentUser.getToken());
            p.putBoolean(PREF_USER_EN_ROUTE, currentUser.isEnRoute());
            p.putFloat(PREF_USER_X_COORDINATE, currentUser.getxCoordinate());
            p.putFloat(PREF_USER_Y_COORDINATE, currentUser.getyCoordinate());
            p.putString(PREF_USER_USERNAME, currentUser.getUsername());
        }
        else {
            p.clear();
        }

        p.commit();
    }
}
