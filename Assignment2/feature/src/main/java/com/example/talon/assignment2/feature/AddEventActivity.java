package com.example.talon.assignment2.feature;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

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
            @Override
            public void onClick(View view) {

                String fileName = "eventOutput.txt";

                // accessing titleDescriptionTextBox
                EditText titleText = (EditText) findViewById(R.id.titleDescriptionTextBox);
                String titleTextString = titleText.getText().toString();

                // accessing eventDesciptionTextBox
                EditText eventText = (EditText) findViewById(R.id.eventDesciptionTextBox);
                String eventTextString = eventText.getText().toString();

                // opening and writing data from eventOutput.txt
                try {
                    PrintWriter outStream = new PrintWriter(fileName);
                    outStream.println("Data: " + titleTextString + ", " + eventTextString);
                    outStream.close();

                    System.out.println("file closed OKAY!");

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }// end - onClick
        });
    }// end - configEnterButton
}
