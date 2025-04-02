package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Page2Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 設定 Fragment 的 UI 佈局
        View view = inflater.inflate(R.layout.fragment_page2, container, false);

        // 綁定按鈕
        Button btnOpenDrawing = view.findViewById(R.id.btn_open_drawing);
        btnOpenDrawing.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), DrawingActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
