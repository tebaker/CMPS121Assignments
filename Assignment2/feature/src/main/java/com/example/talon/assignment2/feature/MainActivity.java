package com.example.talon.assignment2.feature;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public JSONObject jos = null;
    public JSONArray ja = null;
    private static final String TAG = "JSON_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        configAddEventButton();
    }

    protected void onResume() {
        super.onResume();
        ListView list = findViewById(R.id.data_list_view);
        TextView text = findViewById(R.id.text);
        text.setVisibility(View.INVISIBLE);

        jos = null;
        try{
            File f = new File(getFilesDir(), "file.ser");
            FileInputStream fi = new FileInputStream(f);
            ObjectInputStream o = new ObjectInputStream(fi);
            String j = null;
            try{
                j = (String) o.readObject();
            }
            catch(ClassNotFoundException c){
                c.printStackTrace();
            }
            try{
                jos = new JSONObject(j);
                ja = jos.getJSONArray("data");
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }

            final ArrayList<ListData> aList = new ArrayList<>();
            for(int i = 0; i < ja.length(); i++)
            {
                ListData ld = new ListData();
                try{
                    ld.firstText = ja.getJSONObject(i).getString("first");
                    ld.secondText = ja.getJSONObject(i).getString("second");
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                aList.add(ld);
            }

            String[] listItems = new String[aList.size()];
            for(int i = 0; i < aList.size(); i++){
                ListData listD = aList.get(i);
                listItems[i] = listD.firstText;
            }

            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listItems);
            list.setAdapter(adapter);

            final Context context = this;
//            list.setOnItemClickListener((parent, view, position, id){
//                ListData selected = aList.get(position);
//                Intent detailIntent = new Intent(context, DetailActivity.class);
//                detailIntent.putExtra("first", selected.firstText);
//                detailIntent.putExtra("second", selected.secondText);
//                startActivity(detailIntent);
//            });
        }
        catch(IOException e){
            list.setEnabled(false);
            list.setVisibility(View.INVISIBLE);
            text.setVisibility(View.VISIBLE);
        }
    }

    private void configAddEventButton() {
        Button addEventButton = (Button) findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddEventActivity.class));
            }
        });
    }
}
