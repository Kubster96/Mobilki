package com.example.mobilki;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestSender {

    private String URL_STRING = "http://34.66.197.154:5000";

    public void uploadFile(final String filePath, final String directoryPath, final int iteration, Resources resources) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_STRING)
                .build();

        UploadService service = retrofit.create(UploadService.class);
        File file = new File(filePath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        final long startTime = System.nanoTime();
        final Resources finalResources = resources;


        Call<ResponseBody> callSync = service.calc(filePart);
        try
        {
            Response<ResponseBody> response = callSync.execute();
            if(response.isSuccessful()) {
                File fileToSave = new File(filePath);
                String fileName = FilenameUtils.removeExtension(fileToSave.getName());
                File convertedImage = new File(directoryPath + "/" + fileName + "_converted.jpg");
                FileOutputStream fileOutputStream = new FileOutputStream(convertedImage);
                IOUtils.write(response.body().bytes(), fileOutputStream);

                long endTime = System.nanoTime();

                long timeElapsedMillis = (endTime - startTime) / 1_000_000;
                InferenceResult result = new InferenceResult(
                        TensorUtils.prepareInput(filePath, finalResources),
                        (float) timeElapsedMillis,
                        ImageModel.Execution.CLOUD.ordinal()
                );
                updateModel(result);
            }
            else {
                System.out.println(response.errorBody());
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void updateModel(InferenceResult result) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_STRING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MLService service = retrofit.create(MLService.class);

        Call<ResponseBody> callSync = service.updateModel(result);
        try {
            Response<ResponseBody> response = callSync.execute();
            if (response.isSuccessful()) {
                System.out.println("yaaay");
            } else {
                System.out.println("ooooops");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
