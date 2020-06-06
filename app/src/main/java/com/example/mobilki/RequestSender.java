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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestSender {

    private String URL_STRING = "http://192.168.43.169:5000";

    public void uploadFile(final String filePath, final String directoryPath, final int iteration) {
    public void uploadFile(final String filePath, final String directoryPath, Resources resources) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_STRING)
                .build();

        UploadService service = retrofit.create(UploadService.class);
        File file = new File(filePath);
        MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        final long startTime = System.nanoTime();
        final Resources finalResources = resources;
        service.calc(filePart).enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    File file = new File(filePath);
                    String fileName = FilenameUtils.removeExtension(file.getName());
                    File convertedImage = new File(directoryPath + "/" + fileName + "_" + iteration + "_converted.jpg");
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
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    public void updateModel(InferenceResult result) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_STRING)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MLService service = retrofit.create(MLService.class);
        service.updateModel(result).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println("yaaay");
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("ooooops");
            }
        });
    }
}
