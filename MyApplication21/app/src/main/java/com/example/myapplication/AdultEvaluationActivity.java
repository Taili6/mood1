package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import java.util.HashMap;
import java.util.Map;

public class AdultEvaluationActivity extends AppCompatActivity implements AdultQuestionFragment.OnAnswerSelectedListener {

    private static final int TOTAL_QUESTIONS = 18; // 總題數
    private ViewPager2 viewPager;
    private Button nextButton;
    private Button previousButton; // 新增上一題按鈕
    private Map<Integer, Integer> answers = new HashMap<>(); // 用來儲存答案

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adult_evaluation);

        viewPager = findViewById(R.id.view_pager);
        nextButton = findViewById(R.id.next_Button);
        previousButton = findViewById(R.id.previous_button); // 綁定到上一題按鈕

        String[] questions = getResources().getStringArray(R.array.adult_questions); // 問題數組
        AdultQuestionPagerAdapter adapter = new AdultQuestionPagerAdapter(this, questions);
        viewPager.setAdapter(adapter);

        // 下一題按鈕的邏輯
        nextButton.setOnClickListener(v -> {
            int currentItem = viewPager.getCurrentItem();
            if (currentItem == TOTAL_QUESTIONS - 1) {
                collectAnswers();
            } else {
                if (isCurrentQuestionAnswered()) {
                    viewPager.setCurrentItem(currentItem + 1);
                } else {
                    Toast.makeText(AdultEvaluationActivity.this, "請回答當前問題", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // 上一題按鈕的邏輯
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                } else {
                    Toast.makeText(AdultEvaluationActivity.this, "已經是第一題了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAnswerSelected(int questionIndex, int answer) {
        answers.put(questionIndex, answer); // 儲存答案
    }

    @Override
    public void onNextQuestion() {
        // 這裡可以留空，除非你有特定的行為需要在這裡實現
    }

    // 檢查當前問題是否已經回答
    private boolean isCurrentQuestionAnswered() {
        int currentQuestionIndex = viewPager.getCurrentItem();
        return answers.containsKey(currentQuestionIndex);
    }

    // 收集答案並跳轉到結果頁面
    private void collectAnswers() {
        int score = 0;

        if (!areAllQuestionsAnswered()) {
            Toast.makeText(this, "請回答所有問題", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int answer : answers.values()) {
            score += answer;
        }

        Intent intent = new Intent(AdultEvaluationActivity.this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("FEEDBACK", getResultMessage(score));
        startActivity(intent);
        finish(); // 結束當前 Activity 以防止返回
    }

    // 確保所有問題都已經回答
    private boolean areAllQuestionsAnswered() {
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            if (!answers.containsKey(i)) {
                return false;
            }
        }
        return true;
    }

    // 根據分數返回結果信息
    private String getResultMessage(int score) {
        if (score >= 31) {
            return "你的分數是 " + score + "。你可能需要關注心理健康，尋求專業幫助可能會有幫助。";
        } else if (score >= 11 && score <= 30) {
            return "你的分數是 " + score + "。你可能心理壓力稍微有點大，嘗試放鬆和與他人交流可能會有幫助。";
        } else {
            return "你的分數是 " + score + "。你的心理狀態相對健康，但保持良好的生活習慣仍然很重要。";
        }
    }
}
