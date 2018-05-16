package com.example.talon.assignment2.feature;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        configBackButton();
        configEnterButton();
    }

    private void configBackButton() {
        Button goBackButton = (Button) findViewById(R.id.backButton);
        goBackButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
        });
    }// end - configBackButton

    private void configEnterButton() {
        Button enterButton = (Button) findViewById(R.id.enterButton);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                String fileName = "eventOutput.txt";

                // accessing titleDescriptionTextBox
                EditText titleText = (EditText) findViewById(R.id.titleDescriptionTextBox);
                String titleTextString = titleText.getText().toString();

                // accessing eventDesciptionTextBox
                EditText eventText = (EditText) findViewById(R.id.eventDesciptionTextBox);
                String eventTextString = eventText.getText().toString();

                // getting current time
                Date currentTime = Calendar.getInstance().getTime();
                String currentTimeString = currentTime.toString();

                // getting current date
                Calendar cc = Calendar.getInstance();
                int year =cc.get(Calendar.YEAR);
                int month=cc.get(Calendar.MONTH);
                int mDay = cc.get(Calendar.DAY_OF_MONTH);
                String currentDateString = Integer.toString(month) + "/" +
                                           Integer.toString(mDay) +  "/" +
                                           Integer.toString(year);

                System.out.println("Date: " )

                // getting instance of location
//                LocationManager locManager;
//                LocationListener locListener;
//                LocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//                LocationListener = new LocationListener() {
//                    @Override
//                    public void onLocationChanged(Location location) {
//
//                    }
//
//                    @Override
//                    public void onStatusChanged(String s, int i, Bundle bundle) {
//
//                    }
//
//                    @Override
//                    public void onProviderEnabled(String s) {
//
//                    }
//
//                    @Override
//                    public void onProviderDisabled(String s) {
//
//                    }
//                };

            }// end - onClick
        });
    }// end - configEnterButton
}
