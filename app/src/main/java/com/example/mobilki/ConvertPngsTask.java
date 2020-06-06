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
        for(int iteration = 0; iteration<iterations; iteration++) {
            for(File file: files) {
                if (file.getName().toLowerCase().endsWith(".png")) {
                    ResourcesReader resourcesReader = new ResourcesReader(this.context);
                    Resources resources = resourcesReader.readResources();
                    String filePath = file.getAbsolutePath();

//                  ImageModel model = mainActivity.getModel();
//                  ImageModel.Execution mode = model.classify(filePath, resources);
                    Random random = new Random();
                    ImageModel.Execution mode = ImageModel.Execution.values()[random.nextInt(2)];
                    switch (mode) {
                        case LOCAL:
                            long startTime = System.nanoTime();
                            boolean converted = new Converter().convertImage(filePath, directoryPath, iteration);

                            if (converted) {

                                long endTime = System.nanoTime();

                                long timeElapsedMillis = (endTime - startTime) / 1_000_000;
                                InferenceResult result = new InferenceResult(
                                        TensorUtils.prepareInput(filePath, resources),
                                        (float) timeElapsedMillis,
                                        ImageModel.Execution.LOCAL.ordinal()
                                );
                                new RequestSender().updateModel(result);
                            }
                            break;
                        case CLOUD:
                            new RequestSender().uploadFile(filePath, directoryPath, iteration, resources);
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
