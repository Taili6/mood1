package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdultQuestionFragmentStateAdapter extends FragmentStateAdapter {

    private String[] questions;

    public AdultQuestionFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity, String[] questions) {
        super(fragmentActivity);
        this.questions = questions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return AdultQuestionFragment.newInstance(position, questions[position]);
    }

    @Override
    public int getItemCount() {
        return questions.length;
    }
}
