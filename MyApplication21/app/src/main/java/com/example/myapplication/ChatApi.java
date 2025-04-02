package com.example.myapplication;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatApi {
    @POST("/chat")
    Call<ChatResponse> sendMessage(@Body ChatRequest message);
}
