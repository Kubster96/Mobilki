package com.example.mobilki;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.pytorch.Module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private ImageModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (model == null) {
            try {
                model = new ImageModel(
                        Module.load(assetFilePath(this, "model.pt"))
                );
            } catch (IOException e) {
                e.printStackTrace();
                Log.e("ModelLoad", "Error reading assets", e);

                finish();
            }
        }
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

    public ImageModel getModel() {
        return model;
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

    /**
     * Copies specified asset to the file in /files app directory and returns this file absolute path.
     *
     * @return absolute file path
     */
    public static String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }

        try (InputStream is = context.getAssets().open(assetName)) {
            try (OutputStream os = new FileOutputStream(file)) {
                byte[] buffer = new byte[4 * 1024];
                int read;
                while ((read = is.read(buffer)) != -1) {
                    os.write(buffer, 0, read);
                }
                os.flush();
            }
            return file.getAbsolutePath();
        }
    }
}
