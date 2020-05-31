package com.example.mobilki;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int FILE_SELECT_CODE = 0;
    private static final int REQUEST_WRITE_STORAGE = 112;

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
                askForPermissionAndShowChooser();
            }
        });
    }

    private void askForPermissionAndShowChooser() {
        boolean hasPermission = (this.getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        showChooser();
    }

    private void showChooser() {
        Intent intent = new Intent();
        intent.setType("image/png");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Picture"), FILE_SELECT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case FILE_SELECT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String filePath = getRealPathFromURI(selectedImage);
                    new ConvertPngTask(this, filePath).execute();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getRealPathFromURI(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);
        String id = wholeID.split(":")[1];
        String[] column = { MediaStore.Images.Media.DATA };
        String sel = MediaStore.Images.Media._ID + "=?";
        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{ id }, null);
        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);
        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }
        cursor.close();
        return filePath;
    }
}
