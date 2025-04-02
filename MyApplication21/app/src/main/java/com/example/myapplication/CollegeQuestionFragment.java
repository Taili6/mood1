package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CollegeQuestionFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_QUESTION = "question";

    private int position;
    private String question;
    private OnAnswerSelectedListener answerSelectedListener;


    public static CollegeQuestionFragment newInstance(int position, String question) {
        CollegeQuestionFragment fragment = new CollegeQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_college_question, container, false);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            question = getArguments().getString(ARG_QUESTION);
        }

        TextView questionTextView = view.findViewById(R.id.question_text_view);
        RadioGroup answerGroup = view.findViewById(R.id.answer_group);
        ImageButton backButton = view.findViewById(R.id.Back_button);

        questionTextView.setText(question);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity() , AgeSelectionActivity.class);
                startActivity(intent);
            }
        });

        answerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int answer = getAnswerFromId(checkedId);
                if (answer != -1 && answerSelectedListener != null) {
                    answerSelectedListener.onAnswerSelected(position, answer);
                }
            }
        });


        return view;
    }

    private int getAnswerFromId(int checkedId) {
        if (checkedId == R.id.option_rarely) {
            return 0;
        } else if (checkedId == R.id.option_sometimes) {
            return 1;
        } else if (checkedId == R.id.option_often) {
            return 2;
        } else if (checkedId == R.id.option_always) {
            return 3;
        } else {
            return -1; // 如果没有匹配的选项，则返回 -1
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnAnswerSelectedListener) {
            answerSelectedListener = (OnAnswerSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " 必須實作 OnAnswerSelectedListener");
        }
    }

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionIndex, int answer);
        void onNextQuestion();
    }
}
