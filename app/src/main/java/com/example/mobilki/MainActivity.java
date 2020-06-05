package com.example.mobilki;

import android.Manifest;
import android.app.Activity;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.pytorch.Module;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import lib.folderpicker.FolderPicker;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private static final int FOLDER_PICKER_CODE = 123;

    EditText iterationsEditText;
    private Button convertImagesButton;
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
        convertImagesButton = findViewById(R.id.convertImagesButton);
        iterationsEditText = findViewById(R.id.iterationEditText);

        convertImagesButton.setOnClickListener(new View.OnClickListener() {
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
        try {
            Integer.parseInt(iterationsEditText.getText().toString());
            showChooser();
        } catch(NumberFormatException nfe) {
            Toast.makeText(this, "Number not valid", Toast.LENGTH_SHORT).show();
        }
    }

    private void showChooser() {
        Intent intent = new Intent(this, FolderPicker.class);
        startActivityForResult(intent, FOLDER_PICKER_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FOLDER_PICKER_CODE && resultCode == Activity.RESULT_OK) {

            String folderLocation = data.getExtras().getString("data");
            Log.i("folderLocation", folderLocation);
            File directory = new File(folderLocation);
            File[] files = directory.listFiles();
            int iterations = Integer.parseInt(iterationsEditText.getText().toString());


            new ConvertPngsTask(this, files, iterations, folderLocation).execute();

        }
        super.onActivityResult(requestCode, resultCode, data);
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
