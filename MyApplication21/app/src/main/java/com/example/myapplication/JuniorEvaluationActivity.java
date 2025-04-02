package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.widget.ViewPager2;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.HashMap;
import java.util.Map;

public class JuniorEvaluationActivity extends AppCompatActivity {

    private static final int TOTAL_QUESTIONS = 20;
    private ViewPager2 viewPager;
    private Button submitButton;
    private Button previousButton; // 新增上一題按鈕
    private Map<Integer, Integer> answers = new HashMap<>(); // 用來存儲每題的答案
    private Map<Integer, Boolean> questionAnswered = new HashMap<>(); // 用來跟踪每題是否已經回答

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_junior_evaluation);

        viewPager = findViewById(R.id.view_pager);
        submitButton = findViewById(R.id.submit_button);
        previousButton = findViewById(R.id.previous_button); // 綁定上一題按鈕

        String[] questions = getResources().getStringArray(R.array.junior_questions); // 確保這個陣列包含了你的20個問題

        FragmentManager fragmentManager = getSupportFragmentManager();
        Lifecycle lifecycle = getLifecycle();
        QuestionPagerAdapter adapter = new QuestionPagerAdapter(fragmentManager, lifecycle, questions);
        viewPager.setAdapter(adapter);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() == TOTAL_QUESTIONS - 1) {
                    collectAnswers();
                } else {
                    if (isCurrentQuestionAnswered()) {
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    } else {
                        // 提示用戶回答當前問題
                        Toast.makeText(JuniorEvaluationActivity.this, "請回答當前問題", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // 設置上一題按鈕的行為
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                } else {
                    Toast.makeText(JuniorEvaluationActivity.this, "已經是第一題了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void collectAnswers() {
        int score = 0;

        // 收集所有 Fragment 的答案
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            Integer answer = answers.get(i);
            if (answer != null) {
                score += answer;
            }
        }

        // 根據分數生成反饋信息
        Intent intent = new Intent(JuniorEvaluationActivity.this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("FEEDBACK", getResultMessage(score));
        startActivity(intent);
    }

    private String getResultMessage(int score) {
        if (score <= 10) {
            return "你的分數是 " + score + "。你可能需要關注心理健康，尋求專業幫助可能會有幫助。";
        } else if (score <= 15) {
            return "你的分數是 " + score + "。你可能有輕微的心理壓力，嘗試放鬆和與他人交流可能會有幫助。";
        } else {
            return "你的分數是 " + score + "。你的心理狀態相對健康，但保持良好的生活習慣仍然很重要。";
        }
    }

    // 在這裡定義 setAnswer 方法，用於接收 Fragment 的答案
    public void setAnswer(int questionIndex, int answer) {
        answers.put(questionIndex, answer);
        questionAnswered.put(questionIndex, true);
    }

    // 用於檢查當前問題是否已經回答
    private boolean isCurrentQuestionAnswered() {
        int currentQuestionIndex = viewPager.getCurrentItem();
        return questionAnswered.getOrDefault(currentQuestionIndex, false);
    }
}
