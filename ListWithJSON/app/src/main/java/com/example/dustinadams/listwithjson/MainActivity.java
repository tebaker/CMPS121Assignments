package com.example.dustinadams.listwithjson;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private final String TAG = "TESTGPS";
    public static TextView latText;
    public static TextView longText;

    Location l;

    public JSONObject jos = null;
    public JSONArray ja = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // References for the widgets
//        latText = findViewById(R.id.latitude);
//        longText = findViewById(R.id.longitude);

        // This statement requests permission to the user.
        // If permissions are not set in the Manifest file, then access
        // will automatically be denied. Once the user chooses an option,
        // onRequestPermissionsResult is called.
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                99);

        Button b = findViewById(R.id.button);
        b.setOnClickListener(new Button.OnClickListener(){
            @SuppressLint("MissingPermission")
            public void onClick(View view){
                // A reference to the location manager. The LocationManager has already
                // been set up in MyService, we're just getting a reference here.
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = lm.getProviders(true);
                // Go through the location providers starting with GPS, stop as soon
                // as we find one.
                for (int i=providers.size()-1; i>=0; i--) {
                    l = lm.getLastKnownLocation(providers.get(i));
                    longText.setText(Double.toString(l.getLongitude()));
                    latText.setText(Double.toString(l.getLatitude()));
                    if (l != null) break;
                }
            }
        });
    }
    protected void onResume(){
        super.onResume();

        Toast.makeText(MainActivity.this, "Event Added", Toast.LENGTH_LONG).show();

        ListView list = findViewById(R.id.data_list_view);
        TextView text = findViewById(R.id.text);
        text.setVisibility(View.INVISIBLE);

        Log.d(TAG, ""+getFilesDir());

        jos = null;
        try{
            // Reading a file that already exists
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
                jos = new JSONObject(j);
                ja = jos.getJSONArray("data");
            }
            catch(JSONException e){
                e.printStackTrace();
            }

            // Show the list
            final ArrayList<ListData> aList = new ArrayList<ListData>();
            for(int i = 0; i < ja.length(); i++){

                ListData ld = new ListData();
                try {
                    ld.firstText = ja.getJSONObject(i).getString("first");
                    ld.secondText = ja.getJSONObject(i).getString("second");
                    ld.thirdText = ja.getJSONObject(i).getString("third");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

                aList.add(ld);
            }

            // Create an array and assign each element to be the title
            // field of each of the ListData objects (from the array list)
            String[] listItems = new String[aList.size()];

            for(int i = 0; i < aList.size(); i++){
                ListData listD = aList.get(i);
                listItems[i] = listD.firstText;
            }

            // Show the list view with the each list item an element from listItems
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            // Set an OnItemClickListener for each of the list items
            final Context context = this;
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    ListData selected = aList.get(position);

                    // Create an Intent to reference our new activity, then call startActivity
                    // to transition into the new Activity.
                    Intent detailIntent = new Intent(context, DetailActivity.class);

                    // pass some key value pairs to the next Activity (via the Intent)
                    detailIntent.putExtra("first", selected.firstText);
                    detailIntent.putExtra("second", selected.secondText);
                    detailIntent.putExtra("gpsData", String.valueOf(Double.toString(l.getLongitude()) + Double.toString(l.getLatitude())));


                    startActivity(detailIntent);
                }

            });
        }
        catch(IOException e){
            // There's no JSON file that exists, so don't
            // show the list. But also don't worry about creating
            // the file just yet, that takes place in AddText.

            //Here, disable the list view
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);

            //show the text view
            text.setVisibility(View.VISIBLE);
        }
    }

    // This method will just show the menu item (which is our button "ADD")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        // the menu being referenced here is the menu.xml from res/menu/menu.xml
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    /* Here is the event handler for the menu button that I forgot in class.
    The value returned by item.getItemID() is
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG, String.format("" + item.getItemId()));
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_favorite:
                /*the R.id.action_favorite is the ID of our button (defined in strings.xml).
                Change Activity here (if that's what you're intending to do, which is probably is).
                 */
                Intent i = new Intent(this, AddText.class);
                startActivity(i);
            default:
                super.onOptionsItemSelected(item);
        }
        return true;
    }
    // This class implements OnRequestPermissionsResultCallback, so when the
    // user is prompted for location permission, the below method is called
    // as soon as the user chooses an option.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "callback");
        switch (requestCode) {
            case 99:
                // If the permissions aren't set, then return. Otherwise, proceed.
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                                , 10);
                    }
                    Log.d(TAG, "returning program");
                    return;
                }
                else{
                    // Create Intent to reference MyService, start the Service.
                    Log.d(TAG, "starting service");
                    Intent i = new Intent(this, DetailActivity.class);
                    if(i==null)
                        Log.d(TAG, "intent null");
                    else{
                        startService(i);
                    }

                }
                break;
            default:
                break;
        }
    }
    // Used for debugging. Below method is extraneous
    @SuppressLint("MissingPermission")
    public void doSomething(View view){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = lm.getProviders(true);
        Location l;
        for (int i=providers.size()-1; i>=0; i--) {
            l = lm.getLastKnownLocation(providers.get(i));
            longText.setText(Double.toString(l.getLongitude()));
            latText.setText(Double.toString(l.getLatitude()));
            if (l != null) break;
        }
    }
}
