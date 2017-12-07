package com.startupcompany.wireframe.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;
import com.startupcompany.wireframe.model.Category;
import com.startupcompany.wireframe.util.ApiRouter;

import java.util.ArrayList;
import java.util.List;

import no.uib.ii.algo.st8.Main;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CategoryActivity extends BasePrivateActivity {
    private List<Category> myCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        setUpSpinner();
    }

    private void setUpSpinner() {
//        Spinner lstDestinations = (Spinner) findViewById(R.id.spinner_categories);
//
//        final ArrayList<String> choices = new ArrayList<String>();
//
//        // Add choices
//        choices.add("Mobile Apps");
//        choices.add("Test Suites");
//        choices.add("Universities");
//        choices.add("Cloud Computing");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
//        lstDestinations.setAdapter(adapter);

        Spinner lstCategories = (Spinner) findViewById(R.id.spinner_categories);

        final ArrayList<String> options = new ArrayList<String>();

        options.add("Select a category");

        ApiRouter.withToken(getCurrentUser().getToken()).getCategories(1, new Callback<List<Category>>() {
            @Override
            public void success(List<Category> categories, Response response) {
                for (int i = 0; i < categories.size(); i++) {
                    options.add(categories.get(i).getName());
                }

                myCategories = categories;

                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        lstCategories.setAdapter(adapter);
    }

    public void onCategoryNavigateTap(View v) {
//        Intent i = new Intent(this, Main.class);
//
//        Spinner dest = (Spinner) findViewById(R.id.spinner_categories);
//        String choice = dest.getItemAtPosition(dest.getSelectedItemPosition()).toString();
//
//        Bundle bundle = new Bundle();
//
//        switch (choice) {
//            case "Mobile Apps":
//                bundle.putString("CATEGORY", "MOB");
//                break;
//            case "Test Suites":
//                bundle.putString("CATEGORY", "TST");
//                break;
//            case "Universities":
//                bundle.putString("CATEGORY", "UNI");
//                break;
//            case "Cloud Computing":
//                bundle.putString("CATEGORY", "CLD");
//                break;
//            default:
//                break;
//        }
//
//        bundle.putString("MODE", "CAT");
//
//        i.putExtras(bundle);
//
//        startActivity(i);

        startProgress();

        Intent intent = new Intent(this, Main.class);

        Bundle bundle = new Bundle();
        long catId = 0;

        Spinner spinner = (Spinner) findViewById(R.id.spinner_categories);
        String choice = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

        if (!choice.equals("Select a category")) {
            for (int i = 0; i < myCategories.size(); i++) {
                if (choice.equals(myCategories.get(i).getName())) {
                    catId = myCategories.get(i).getId();
                }
            }

            bundle.putLong("CATEGORY", catId);
            bundle.putString("MODE", "CAT");

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }
}
