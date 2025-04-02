package com.example.myapplication;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer "
    })
    @POST("v1/completions")
    Call<OpenAIResponse> generateResponse(@Body OpenAIRequest request);
}
