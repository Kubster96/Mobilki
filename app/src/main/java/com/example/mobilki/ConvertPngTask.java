package com.example.mobilki;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Random;

public class ConvertPngTask extends AsyncTask<Void, Void, Resources> {

    private final WeakReference<MainActivity> sActivity;
    private Context context;
    private String filePath;

    public ConvertPngTask(MainActivity activity, String filePath) {
        this.sActivity = new WeakReference<>(activity);
        this.context = activity.getApplicationContext();
        this.filePath = filePath;
    }

    @Override
    protected Resources doInBackground(Void... voids) {
        ResourcesReader resourcesReader = new ResourcesReader(this.context);
        return resourcesReader.readResources();
    }

    @Override
    protected void onPostExecute(Resources resources) {
        MainActivity activity = sActivity.get();
        activity.processorCoresEditText.setText(String.valueOf(resources.getProcessorCores()));
        activity.processorFrequencyEditText.setText(String.valueOf(resources.getProcessorFrequency()));
        activity.batteryEditText.setText(String.valueOf(resources.getBattery()));
        activity.memoryEditText.setText(String.valueOf(resources.getMemory()));
        activity.networkEditText.setText(String.valueOf(resources.getNetwork()));
        activity.hasWifiEditText.setText(String.valueOf(resources.isHasWifi()));
        activity.downloadSpeedEditText.setText(String.valueOf(resources.getDownloadSpeed()));
        activity.uploadSpeedEditText.setText(String.valueOf(resources.getUploadSpeed()));

        Random random = new Random();
        boolean convertLocally = random.nextBoolean();
        //TODO decision based on model - resources

        if(convertLocally) {
            new Converter().convertImage(filePath);
            Toast.makeText(activity, "Converted image locally",  Toast.LENGTH_LONG).show();
        } else {
            new RequestSender().uploadFile(filePath);
            Toast.makeText(activity, "Converted image remotely",  Toast.LENGTH_LONG).show();
        }
    }
}
