package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class QuestionFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private static final String ARG_QUESTION = "question";

    private int position;
    private String question;


    public static QuestionFragment newInstance(int position, String question) {
        QuestionFragment fragment = new QuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_QUESTION, question);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_question, container, false);

        if (getArguments() != null) {
            position = getArguments().getInt(ARG_POSITION);
            question = getArguments().getString(ARG_QUESTION);
        }


        TextView questionTextView = view.findViewById(R.id.question_text_view);
        RadioGroup answerGroup = view.findViewById(R.id.answer_group);
        ImageButton backButton = view.findViewById(R.id.Back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AgeSelectionActivity.class);
                startActivity(intent);
            }
        });

        questionTextView.setText(question);

        answerGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int answer = checkedId == R.id.yes_radio_button ? 0: 1;
                ((JuniorEvaluationActivity) getActivity()).setAnswer(position, answer);
            }
        });

        return view;
    }
}
