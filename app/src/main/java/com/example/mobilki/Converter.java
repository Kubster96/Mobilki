package com.example.mobilki;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FilenameUtils;


public class Converter {

    public boolean convertImage(String filePath, String directoryPath){
        boolean success;
        try {
            File file = new File(filePath);
            String fileName = FilenameUtils.removeExtension(file.getName());
            Bitmap bmp = BitmapFactory.decodeFile(filePath);
            File convertedImage = new File(directoryPath + "/" + fileName + "_converted.jpg");
            convertedImage.createNewFile();
            FileOutputStream outStream = new FileOutputStream(convertedImage);
            success = bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);

            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }
}