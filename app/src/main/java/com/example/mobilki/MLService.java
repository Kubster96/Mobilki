package com.example.mobilki;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MLService {
    @POST("updateModel")
    Call<ResponseBody> updateModel(@Body InferenceResult result);

}
