package com.startupcompany.wireframe.activity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.startupcompany.wireframe.R;
import com.startupcompany.wireframe.activity.base.BasePrivateActivity;
import com.startupcompany.wireframe.model.Destination;
import com.startupcompany.wireframe.util.ApiRouter;

import no.uib.ii.algo.st8.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import java.util.ArrayList;
import java.util.List;

// public class DestinationActivity extends BasePrivateActivity implements SensorEventListener {
public class DestinationActivity extends BasePrivateActivity {
    private List<Destination> myDestinations;

    // Trying a new one
    // private SensorManager sensorManager;
    // double ax, ay, az;

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    SensorManager sensorManager;
    private Sensor sensorAccelerometer;
    private Sensor sensorMagneticField;

    private float[] valuesAccelerometer;
    private float[] valuesMagneticField;

    private float[] matrixR;
    private float[] matrixI;
    private float[] matrixValues;

    TextView readingAzimuth, readingPitch, readingRoll;
    TextView xAcc, yAcc, zAcc;
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        setUpSpinner();

        // TESTING accelerometer -- trying a new one
        /*sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);*/

//        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//        readingAzimuth = (TextView)findViewById(R.id.azimuth);
//        readingPitch = (TextView)findViewById(R.id.pitch);
//        readingRoll = (TextView)findViewById(R.id.roll);
//
//        xAcc = (TextView)findViewById(R.id.x);
//        yAcc = (TextView)findViewById(R.id.y);
//        zAcc = (TextView)findViewById(R.id.z);
//
//        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
//        sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
//        sensorMagneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
//
//        valuesAccelerometer = new float[3];
//        valuesMagneticField = new float[3];
//
//        matrixR = new float[9];
//        matrixI = new float[9];
//        matrixValues = new float[3];
        // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onNavigateTap(View v) {
//        Intent i = new Intent(this, Main.class);
//
//        Spinner dest = (Spinner) findViewById(R.id.spinner);
//        String choice = dest.getItemAtPosition(dest.getSelectedItemPosition()).toString();
//
//        Bundle bundle = new Bundle();
//
//        switch (choice) {
//            case "Booth_1": bundle.putString("BOOTH", "1"); break;
//            case "Booth_2": bundle.putString("BOOTH", "2"); break;
//            case "Booth_3": bundle.putString("BOOTH", "3"); break;
//            case "Booth_4": bundle.putString("BOOTH", "4"); break;
//            case "Booth_5": bundle.putString("BOOTH", "5"); break;
//            case "Booth_6": bundle.putString("BOOTH", "6"); break;
//            default: break;
//        }
//
//        bundle.putString("MODE", "DEST");
//
//        i.putExtras(bundle);
//
//        startActivity(i);

        startProgress();

        Intent intent = new Intent(this, Main.class);

        Bundle bundle = new Bundle();
        long destinationId = 0;

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        String choice = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();

        //System.out.println("SIZE: " + myDestinations.size());

        if (!choice.equals("Select a destination")) {
            for (int i = 0; i < myDestinations.size(); i++) {
                if (choice.equals(myDestinations.get(i).getName())) {
                    destinationId = myDestinations.get(i).getId();

                    //System.out.println("ID: " + myDestinations.get(i).getId());
                }
            }

            bundle.putLong("BOOTH", destinationId);
            bundle.putString("MODE", "DEST");

            intent.putExtras(bundle);

            startActivity(intent);
        }
    }

    private void setUpSpinner() {
//        Spinner lstDestinations = (Spinner) findViewById(R.id.spinner);
//
//        final ArrayList<String> choices = new ArrayList<String>();
//
//        // Add choices
//        choices.add("Booth_1");
//        choices.add("Booth_2");
//        choices.add("Booth_3");
//        choices.add("Booth_4");
//        choices.add("Booth_5");
//        choices.add("Booth_6");
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choices);
//        lstDestinations.setAdapter(adapter);

        Spinner lstDestinations = (Spinner) findViewById(R.id.spinner);

        final ArrayList<String> options = new ArrayList<String>();

        options.add("Select a destination");

        ApiRouter.withToken(getCurrentUser().getToken()).getDestinations(1, new Callback<List<Destination>>() {
            @Override
            public void success(List<Destination> destinations, Response response) {
                for (int i = 0; i < destinations.size(); i++) {
                    options.add(destinations.get(i).getName());
                }

                myDestinations = destinations;

                stopProgress();
            }

            @Override
            public void failure(RetrofitError e) {
                displayError(e);
            }
        });

//        options.add("D1");
//        options.add("D2");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        lstDestinations.setAdapter(adapter);
    }

    // TESTING accelerometer -- trying a new one
    /*@Override
    public void onAccuracyChanged(Sensor arg0, int arg1) {

    }*/

    // TESTING accelerometer -- trying a new one
    /*@Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            ax = event.values[0];
            ay = event.values[1];
            az = event.values[2];

            System.out.println("X: " + ax + " Y: " + ay + " Z: " + az);
        }
    }*/

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
//    @Override
//    protected void onResume() {
//
//        sensorManager.registerListener(this,
//                sensorAccelerometer,
//                SensorManager.SENSOR_DELAY_NORMAL);
//        sensorManager.registerListener(this,
//                sensorMagneticField,
//                SensorManager.SENSOR_DELAY_NORMAL);
//        super.onResume();
//    }
//
//    @Override
//    protected void onPause() {
//
//        sensorManager.unregisterListener(this,
//                sensorAccelerometer);
//        sensorManager.unregisterListener(this,
//                sensorMagneticField);
//        super.onPause();
//    }
//
//    @Override
//    public void onAccuracyChanged(Sensor arg0, int arg1) {
//        // TODO Auto-generated method stub
//
//    }
//
//    @Override
//    public void onSensorChanged(SensorEvent event) {
//        // TODO Auto-generated method stub
//
//        switch(event.sensor.getType()){
//            case Sensor.TYPE_ACCELEROMETER:
//                for(int i =0; i < 3; i++){
//                    valuesAccelerometer[i] = event.values[i];
//                }
//                break;
//            case Sensor.TYPE_MAGNETIC_FIELD:
//                for(int i =0; i < 3; i++){
//                    valuesMagneticField[i] = event.values[i];
//                }
//                break;
//        }
//
//        boolean success = SensorManager.getRotationMatrix(
//                matrixR,
//                matrixI,
//                valuesAccelerometer,
//                valuesMagneticField);
//
//        if(success){
//            SensorManager.getOrientation(matrixR, matrixValues);
//
//            double azimuth = Math.toDegrees(matrixValues[0]);
//            double pitch = Math.toDegrees(matrixValues[1]);
//            double roll = Math.toDegrees(matrixValues[2]);
//
//            readingAzimuth.setText("Azimuth: " + String.valueOf(azimuth));
//            readingPitch.setText("Pitch: " + String.valueOf(pitch));
//            readingRoll.setText("Roll: " + String.valueOf(roll));
//
//            xAcc.setText("X: " + valuesAccelerometer[0]);
//            yAcc.setText("Y: " + valuesAccelerometer[1]);
//            zAcc.setText("Z: " + valuesAccelerometer[2]);
//        }
//
//    }
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
}
