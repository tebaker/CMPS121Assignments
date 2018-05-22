package com.example.talon.assignmenttwo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ShowInfo extends AppCompatActivity {


    public JSONObject jos = null;
    public JSONArray ja = null;
    private static final String TAG = "JSON_LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_show_info);

        final Intent i = getIntent();
        String title = i.getStringExtra("title");
        String description = i.getStringExtra("description");
        final String date = i.getStringExtra("date");
        final String time = i.getStringExtra("time");
        String location = i.getStringExtra("gps");
        final int posit = i.getIntExtra("test", 0);

        TextView getTime = findViewById(R.id.textView3);
        TextView getDes = findViewById(R.id.textView4);
        TextView day = findViewById(R.id.textDate);
        TextView tim = findViewById(R.id.textTime);
        TextView loc = findViewById(R.id.textLoc);

        getTime.setText(title);
        getDes.setText(description);
        day.setText(date);
        tim.setText(time);
        loc.setText(location);

        Button delete = findViewById(R.id.button2);
        delete.setOnClickListener(new Button.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onClick(View v){
                jos = null;
                try{
                    File f = new File(getFilesDir(), "file.ser");
                    FileInputStream fi = new FileInputStream(f);
                    ObjectInputStream o = new ObjectInputStream(fi);
                    String j = null;
                    try{
                        j = (String) o.readObject();
                    }
                    catch(ClassNotFoundException c) {
                        c.printStackTrace();
                    }
                    try {
                        jos = new JSONObject(j);
                        ja = jos.getJSONArray("data");
                    }
                    catch(JSONException e){
                        e.printStackTrace();
                    }
                    ja.remove(posit);

                    fi.close();
                    o.close();
                }
                catch(IOException e){
                    e.printStackTrace();

                }
                try{
                    File k = new File(getFilesDir(), "file.ser");
                    FileOutputStream fout = new FileOutputStream(k);
                    ObjectOutputStream outPut = new ObjectOutputStream(fout);
                    String J = jos.toString();
                    outPut.writeObject(J);
                    outPut.close();
                    fout.close();
                }
                catch(Exception e){
                    e.printStackTrace();
                }

                Intent in = new Intent(ShowInfo.this, MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
            }
        });
    }
}
