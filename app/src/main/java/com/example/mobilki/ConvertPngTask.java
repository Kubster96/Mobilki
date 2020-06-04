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

//        ImageModel model = activity.getModel();
        boolean converted = false;
        long startTime = System.nanoTime();
//        ImageModel.Execution mode = model.classify(filePath, resources);
        Random random = new Random();
        ImageModel.Execution mode = ImageModel.Execution.values()[random.nextInt(2)];
        switch (mode) {
            case LOCAL:
                converted = new Converter().convertImage(filePath);
                Toast.makeText(activity, "Converted image locally", Toast.LENGTH_LONG).show();
                break;
            case CLOUD:
                new RequestSender().uploadFile(filePath);
                Toast.makeText(activity, "Converted image remotely", Toast.LENGTH_LONG).show();
        }
        long endTime = System.nanoTime();

        if (converted) {
            long timeElapsedMillis = (endTime - startTime) / 1_000_000;
            InferenceResult result = new InferenceResult(
                    TensorUtils.prepareInput(filePath, resources),
                    (float) timeElapsedMillis,
                    mode.ordinal()
            );
            new RequestSender().updateModel(result);
        }
    }
}
