package com.example.mobilki;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;
import java.lang.ref.WeakReference;

public class GetResourcesTask extends AsyncTask<Void, Void, Resources> {

    private final WeakReference<MainActivity> sActivity;
    private Context context;

    public GetResourcesTask(MainActivity activity) {
        this.sActivity = new WeakReference<>(activity);
        this.context = activity.getApplicationContext();
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

        Converter converter = new Converter(context, activity);
        converter.convert();
    }
}
