package com.example.mobilki;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    EditText processorCoresEditText;
    EditText processorFrequencyEditText;
    EditText memoryEditText;
    EditText batteryEditText;
    EditText networkEditText;
    EditText hasWifiEditText;
    EditText downloadSpeedEditText;
    EditText uploadSpeedEditText;
    private Button getResourcesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processorCoresEditText = findViewById(R.id.processorCoresEditText);
        processorFrequencyEditText = findViewById(R.id.processorFrequencyEditText);
        memoryEditText = findViewById(R.id.memoryEditText);
        batteryEditText = findViewById(R.id.batteryEditText);
        networkEditText = findViewById(R.id.networkEditText);
        hasWifiEditText = findViewById(R.id.hasWifiEditText);
        downloadSpeedEditText = findViewById(R.id.downloadSpeedEditText);
        uploadSpeedEditText = findViewById(R.id.uploadSpeedEditText);
        getResourcesButton = findViewById(R.id.getResourcesButton);

        getResourcesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeGetResourcesTask();
            }
        });
    }

    private void executeGetResourcesTask() {
        new GetResourcesTask(this).execute();
    }
}
