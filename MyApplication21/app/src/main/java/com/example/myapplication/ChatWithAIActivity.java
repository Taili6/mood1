package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;  // OkHttp 的 Callback
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ChatWithAIActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView welcomeTextView;
    EditText messageEditText;
    ImageButton sendButton, micButton;
    List<Message> messageList;
    MessageAdapter messageAdapter;

    public static final MediaType JSON = MediaType.get("application/json");
    OkHttpClient client = new OkHttpClient();

    private OpenAIService openAIService;
    private TextToSpeech textToSpeech;
    private SpeechRecognizer speechRecognizer;
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;  // 自定義請求碼

    private boolean isVoiceMode = false;  // 用於標記是否處於語音模式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_ai);

        messageList = new ArrayList<>();

        recyclerView = findViewById(R.id.recycler_view);
        welcomeTextView = findViewById(R.id.welcome_text);
        messageEditText = findViewById(R.id.message_edit_text);
        sendButton = findViewById(R.id.send_btn);
        micButton = findViewById(R.id.voice_toggle_btn);

        messageAdapter = new MessageAdapter(messageList);
        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        openAIService = RetrofitClient.getClient().create(OpenAIService.class);

        String userMessage = "我不想活了";
        sendMessageToOpenAI(userMessage);

        findViewById(R.id.confirm_button).setOnClickListener(v -> {
            String editedText = messageEditText.getText().toString().trim();
            if (!TextUtils.isEmpty(editedText)) {
                addToChat(editedText, Message.SENT_BY_ME);
                callAPI(editedText);
                v.setVisibility(View.GONE); // 隱藏確認按鈕
            }
        });

        sendButton.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            if (question.isEmpty()) {
                addResponse("Message cannot be empty!");
                return;
            }
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question);
            welcomeTextView.setVisibility(View.GONE);
        });

        // 初始化 TextToSpeech
        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(Locale.TRADITIONAL_CHINESE);
                textToSpeech.setSpeechRate(1.0f);
                textToSpeech.setPitch(1.0f);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    addResponse("語音引擎不支援中文！");
                }
            } else {
                addResponse("語音引擎初始化失敗！");
            }
        });

        // 初始化語音識別
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this);

        micButton.setOnClickListener(v -> toggleVoiceMode());
    }

    private void sendMessageToOpenAI(String userMessage) {
        OpenAIRequest request = new OpenAIRequest(
                "ft-abc123",  // 替換為你的 Fine-Tuned 模型 ID
                userMessage,
                150 // 最大 token 數
        );

        // 確保 `Callback` 使用 retrofit2.Callback
        openAIService.generateResponse(request).enqueue(new retrofit2.Callback<OpenAIResponse>() {
            @Override
            public void onResponse(retrofit2.Call<OpenAIResponse> call, retrofit2.Response<OpenAIResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // 修正這裡的取值
                    String aiResponse = response.body().choices.get(0).message.content;
                    Toast.makeText(ChatWithAIActivity.this, aiResponse, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ChatWithAIActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<OpenAIResponse> call, Throwable t) {
                Toast.makeText(ChatWithAIActivity.this, "Failed to connect: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // 切換語音模式的開關
    private void toggleVoiceMode() {
        isVoiceMode = !isVoiceMode; // 切換語音模式狀態
        if (isVoiceMode) {
            startVoiceInput();  // 開始語音識別
        } else {
            micButton.setImageResource(R.drawable.mic); // 更改按鈕顯示為語音輸入圖標
        }
    }

    // 開始語音識別
    private void startVoiceInput() {
        // 檢查裝置是否支持語音識別
        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);  // 使用自定義請求碼
        } else {
            addResponse("此裝置不支援語音識別。");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 檢查返回的結果碼是否為OK
        if (requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK && data != null) {
            // 從返回的 Intent 中獲取語音識別結果
            ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && !results.isEmpty()) {
                String recognizedText = results.get(0);  // 取第一個結果
                messageEditText.setText(recognizedText); // 將語音結果顯示到EditText
                addToChat(recognizedText, Message.SENT_BY_ME); // 把語音結果顯示在聊天界面
                callAPI(recognizedText);
                messageEditText.setText(""); // 呼叫API發送語音結果
            }
        }
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messageList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        addToChat(response, Message.SENT_BY_BOT);
        // 如果處於語音模式，則語音回應；否則只顯示文字
        if (isVoiceMode) {
            speakWithLanguage(response);
        }
    }


    void callAPI(String question) {
        JSONObject jsonBody = new JSONObject();
        try {
            // 使用 Fine-tuned 模型
            jsonBody.put("model", " ");

            // 設定對話上下文，包含系統角色與用戶問題
            JSONArray messages = new JSONArray();

            // 系統角色描述
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一個情緒助手");
            messages.put(systemMessage);

            // 使用者訊息
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", question);
            messages.put(userMessage);

            jsonBody.put("messages", messages);
            jsonBody.put("temperature", 0); // 固定回應樣式
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .post(body)
                .addHeader("Authorization", "Bearer ")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseData);
                        // 取得 Fine-tuned 模型的回應
                        String botReply = jsonResponse.getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        addResponse(botReply);  // 顯示模型回應
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void speakWithLanguage(String text) {
        if (textToSpeech != null) {
            // 語音輸出繁體中文
            textToSpeech.setLanguage(Locale.TAIWAN);
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        super.onDestroy();
    }
}
