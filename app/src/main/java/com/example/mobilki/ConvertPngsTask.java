package com.example.mobilki;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.util.Random;

public class ConvertPngsTask extends AsyncTask<Void, Void, Void> {

    private final File[] files;
    private final int iterations;
    private final MainActivity mainActivity;
    private String directoryPath;
    private Context context;

    public ConvertPngsTask(MainActivity mainActivity, File[] files, int iterations, String directoryPath) {
        this.mainActivity = mainActivity;
        this.context = mainActivity.getApplicationContext();
        this.files = files;
        this.iterations = iterations;
        this.directoryPath = directoryPath;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        for(int i = 0; i<iterations; i++) {
            for(File file: files) {
                if (file.getName().toLowerCase().endsWith(".png")) {
                    ResourcesReader resourcesReader = new ResourcesReader(this.context);
                    Resources resources = resourcesReader.readResources();
                    String filePath = file.getAbsolutePath();

//                  ImageModel model = mainActivity.getModel();
                    boolean converted = false;
                    long startTime = System.nanoTime();
//                  ImageModel.Execution mode = model.classify(filePath, resources);
                    Random random = new Random();
                    ImageModel.Execution mode = ImageModel.Execution.values()[random.nextInt(2)];
                    switch (mode) {
                        case LOCAL:
                            converted = new Converter().convertImage(filePath, directoryPath);
                            break;
                        case CLOUD:
                            new RequestSender().uploadFile(filePath, directoryPath);
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
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void vooid) {
    }
}