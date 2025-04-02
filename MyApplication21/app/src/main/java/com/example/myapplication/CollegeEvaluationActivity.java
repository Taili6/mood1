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

public class CollegeEvaluationActivity extends AppCompatActivity implements CollegeQuestionFragment.OnAnswerSelectedListener {

    private static final int TOTAL_QUESTIONS = 32;
    private ViewPager2 viewPager;
    private Button nextButton;
    private Map<Integer, Integer> answers = new HashMap<>();
    private Button previousButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_college_evaluation);

        nextButton = findViewById(R.id.next_Button);
        viewPager = findViewById(R.id.view_pager);
        previousButton = findViewById(R.id.previous_button);
        String[] questions = getResources().getStringArray(R.array.college_questions);
        CollegeQuestionPagerAdapter adapter = new CollegeQuestionPagerAdapter(this, questions);
        viewPager.setAdapter(adapter);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentItem = viewPager.getCurrentItem();
                if (currentItem == TOTAL_QUESTIONS - 1) {
                    collectAnswers();
                } else {
                    if (isCurrentQuestionAnswered()) {
                        viewPager.setCurrentItem(currentItem + 1);
                    } else {
                        Toast.makeText(CollegeEvaluationActivity.this, "請回答當前問題", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewPager.getCurrentItem() > 0) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                } else {
                    Toast.makeText(CollegeEvaluationActivity.this, "已經是第一題了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onAnswerSelected(int questionIndex, int answer) {
        answers.put(questionIndex, answer);
    }

    @Override
    public void onNextQuestion() {
        // 在这里可以添加处理代码，如果有需要
    }

    private boolean isCurrentQuestionAnswered() {
        int currentQuestionIndex = viewPager.getCurrentItem();
        return answers.containsKey(currentQuestionIndex);
    }

    private void collectAnswers() {
        int score = 0;


        for (int answer : answers.values()) {
            score += answer;
        }

        Intent intent = new Intent(CollegeEvaluationActivity.this, ResultActivity.class);
        intent.putExtra("SCORE", score);
        intent.putExtra("FEEDBACK", getResultMessage(score));
        startActivity(intent);
    }

    private boolean areAllQuestionsAnswered() {
        for (int i = 0; i < TOTAL_QUESTIONS; i++) {
            if (!answers.containsKey(i)) {
                return false;
            }
        }
        return true;
    }

    private String getResultMessage(int score) {
        if (score >= 51) {
            return "你的分數是 " + score + "。你可能需要關注心理健康，尋求專業幫助可能會有幫助。";
        } else if (score >= 16 && score<=50) {
            return "你的分數是 " + score + "。你可能有稍微嚴重的心理壓力，嘗試放鬆和與他人交流可能會有幫助。";
        } else {
            return "你的分數是 " + score + "。你的心理狀態相對健康，但保持良好的生活習慣仍然很重要。";
        }
    }
}
