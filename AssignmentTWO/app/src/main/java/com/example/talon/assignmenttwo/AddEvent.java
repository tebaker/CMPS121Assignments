package com.example.talon.assignmenttwo;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

//import android.icu.util.Calendar;
//import java.text.DateFormat;


public class AddEvent extends AppCompatActivity implements LocationListener {

    public JSONObject jo = null;
    public JSONArray ja = null;
    public static final int RequestPermissionCode = 1;
    Context context;
    LocationManager locationManager ;
    Location location;
    boolean GpsStatus = false;
    Criteria criteria ;
    String Holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Start up the Location Service

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        EnableRuntimePermission();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        Holder = locationManager.getBestProvider(criteria, false);
        context = getApplicationContext();
        CheckGpsStatus();
        //final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        //String formattedDate = date.format(calendar.getTime());
        final Date dateNow = Calendar.getInstance().getTime();
        //final DateFormat currentDate = Calendar.getDateInstance(int style "M/d/yy");
        final Calendar calendar = Calendar.getInstance();
        //System.out.println("Current time => "+calendar.getTime());
        //final SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");


        final SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        String timeprint = time.format(calendar.getTime());
        final EditText first = findViewById(R.id.editText);
        final EditText second = findViewById(R.id.editText2);
        String dateprint = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());




        TextView One = (TextView)findViewById(R.id.textView7);
        TextView Two = (TextView)findViewById(R.id.textView8);
        One.setText(dateprint);
        Two.setText(timeprint);
        Button b = findViewById(R.id.button);

        // Read the file


        try{
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);
            // Notice here that we are de-serializing a String object (instead of
            // a JSONObject object) and passing the String to the JSONObject’s
            // constructor. That’s because String is serializable and
            // JSONObject is not. To convert a JSONObject back to a String, simply
            // call the JSONObject’s toString method.
            String j = null;
            try{
                j = (String) o.readObject();
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            try {
                jo = new JSONObject(j);
                ja = jo.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }
        }
        catch(IOException e){
            // Here, initialize a new JSONObject
            jo = new JSONObject();
            ja = new JSONArray();
            try{
                jo.put("data", ja);
            }
            catch(JSONException j){
                j.printStackTrace();
            }
        }
        CheckGpsStatus();
        if(GpsStatus == true) {
            if (Holder != null) {
                if (ActivityCompat.checkSelfPermission(
                        AddEvent.this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(AddEvent.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                location = locationManager.getLastKnownLocation(Holder);
                locationManager.requestLocationUpdates(Holder, 12000, 7, AddEvent.this);
            }
        }else {

            Toast.makeText(AddEvent.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

        }
        b.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                /*CheckGpsStatus();
                if(GpsStatus == true) {
                    if (Holder != null) {
                        if (ActivityCompat.checkSelfPermission(
                                AddEvent.this,
                                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                                &&
                                ActivityCompat.checkSelfPermission(AddEvent.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                                        != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        location = locationManager.getLastKnownLocation(Holder);
                        locationManager.requestLocationUpdates(Holder, 12000, 7, AddEvent.this);
                    }
                }else {

                    Toast.makeText(AddEvent.this, "Please Enable GPS First", Toast.LENGTH_LONG).show();

                }*/
                String firstText = first.getText().toString();
                String secondText = second.getText().toString();
                //String formattedDate = date.format(calendar.getTime());
                //String formattedTime = time.format(calendar.getTime());
                String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
                String currentTime = time.format(calendar.getTime());
                //location = locationManager.getLastKnownLocation(Holder);
                //locationManager.requestLocationUpdates(Holder, 12000, 7, AddEvent.this);
                String currentLoc;
                currentLoc = location.getLongitude() + ", " + location.getLatitude();
                //String tempLoc = (" " + location.getLatitude()).toString();
                //currentLoc = currentLoc + tempLoc;
                JSONObject temp = new JSONObject();
                try {
                    temp.put("first", firstText);
                    temp.put("second", secondText);
                    temp.put("date", currentDate);
                    temp.put("time", currentTime);
                    temp.put("gps", currentLoc);

                }
                catch(JSONException j){
                    j.printStackTrace();
                }

                ja.put(temp);

                // write the file
                try{
                    File f = new File(getFilesDir(), "file.ser");
                    FileOutputStream fo = new FileOutputStream(f);
                    ObjectOutputStream o = new ObjectOutputStream(fo);
                    String j = jo.toString();
                    o.writeObject(j);
                    o.close();
                    fo.close();
                }
                catch(IOException e){

                }

                //pop the activity off the stack
                Intent i = new Intent(AddEvent.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    public void CheckGpsStatus(){

        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }
    public void EnableRuntimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddEvent.this,
                Manifest.permission.ACCESS_FINE_LOCATION))
        {

            Toast.makeText(AddEvent.this,"ACCESS_FINE_LOCATION permission allows us to Access GPS in app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(AddEvent.this,new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, RequestPermissionCode);

        }
    }
    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(AddEvent.this,"Permission Granted, Now your application can access GPS.", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(AddEvent.this,"Permission Canceled, Now your application cannot access GPS.", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }
}


