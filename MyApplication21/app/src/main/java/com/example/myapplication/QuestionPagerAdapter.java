package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class QuestionPagerAdapter extends FragmentStateAdapter {

    private final String[] questions;

    public QuestionPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String[] questions) {
        super(fragmentManager, lifecycle);
        this.questions = questions;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return QuestionFragment.newInstance(position, questions[position]);
    }

    @Override
    public int getItemCount() {
        return questions.length;
    }
}
