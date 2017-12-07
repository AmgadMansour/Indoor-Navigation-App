package com.startupcompany.wireframe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;

import no.uib.ii.algo.st8.Main;

public class HomeActivity extends BasePrivateActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.menu_home, menu);

        return isRefreshable();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                setCurrentUser(null);
                recreate();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSelectDestinationTap(View v) {
        Intent i = new Intent(this, DestinationActivity.class);
        startActivity(i);
    }

    public void onTourTap(View v) {
        Intent i = new Intent(this, Main.class);

        Bundle bundle = new Bundle();
        bundle.putString("MODE", "TOUR");
        i.putExtras(bundle);

        startActivity(i);
    }

    public void onCategoriesTap(View v) {
        Intent i = new Intent(this, CategoryActivity.class);
        startActivity(i);
    }
}
