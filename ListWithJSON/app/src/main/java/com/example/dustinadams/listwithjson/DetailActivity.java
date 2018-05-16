package com.example.dustinadams.listwithjson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent i = getIntent();
        String title = i.getStringExtra("first");
        String description = i.getStringExtra("second");

        TextView t = (TextView)findViewById(R.id.textView3);
        TextView d = (TextView)findViewById(R.id.textView4);

        // getting current time
        Date currentTime = Calendar.getInstance().getTime();
        String currentTimeString;
        currentTimeString = currentTime.toString();

        // getting current date
        Calendar cc = Calendar.getInstance();
        int year =cc.get(Calendar.YEAR);
        int month=cc.get(Calendar.MONTH);
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        String currentDateString = Integer.toString(month) + "/" +
                Integer.toString(mDay) +  "/" +
                Integer.toString(year);

        t.setText(title);
        d.setText(description + "\n" + currentTimeString + "\n" + currentDateString);
    }
}
