package com.example.mobilki;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class Converter {

    private Context context;
    private Activity activity;
    public static final int REQUEST_WRITE_STORAGE = 112;

    public Converter(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public boolean convert() {
        boolean success = false;
        try {

            boolean hasPermission = (context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
            if (!hasPermission) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_WRITE_STORAGE);

                success = convertImage();
            } else {
                success = convertImage();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return success;
    }

    private boolean convertImage() throws IOException {
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.test);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File convertedImage = new File(path + "/converted.jpg");
        convertedImage.createNewFile();
        FileOutputStream outStream = new FileOutputStream(convertedImage);
        boolean success = bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);

        outStream.flush();
        outStream.close();
        return success;
    }
}