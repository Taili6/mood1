package com.example.myapplication;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class ChatGPTClient {
    private static final String API_URL = "https://api.openai.com/v1/chat/completions";
    private static final String API_KEY = " "; // 🚨 請替換成你的 OpenAI API Key

    public interface ChatGPTCallback {
        void onResponse(String response);
        void onFailure(String error);
    }

    public static void sendMessage(String message, ChatGPTCallback callback) {
        OkHttpClient client = new OkHttpClient();

        // 建立 JSON 內容
        JSONObject json = new JSONObject();
        try {
            json.put("model", "gpt-3.5-turbo"); // 可以改成 "gpt-4"

            // 正確的 messages 格式
            JSONArray messages = new JSONArray();
            messages.put(new JSONObject().put("role", "system").put("content", "你是一個虛擬機器人"));
            messages.put(new JSONObject().put("role", "user").put("content", message));

            json.put("messages", messages);
            json.put("temperature", 0.7);
        } catch (Exception e) {
            e.printStackTrace();
            callback.onFailure("JSON 組合錯誤");
            return;
        }

        // 建立請求
        RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json; charset=utf-8"));
        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("Authorization", "Bearer " + API_KEY) // 確保 "Bearer " + API_KEY
                .post(body)
                .build();

        // 執行請求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure("錯誤：" + response.code() + " - " + response.body().string());
                    return;
                }

                // 解析 API 回應
                String responseBody = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseBody);
                    String reply = jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
                    callback.onResponse(reply);
                } catch (Exception e) {
                    callback.onFailure("回應解析錯誤");
                }
            }
        });
    }
}
