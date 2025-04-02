package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;

public class SaveActivity extends AppCompatActivity {
    private GridView gridView;
    private ImageAdapter imageAdapter;
    private ArrayList<File> imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        gridView = findViewById(R.id.gridView);

        // 讀取應用內部儲存的圖片
        loadImages();
    }

    private void loadImages() {
        File dir = getFilesDir();  // 取得內部儲存空間
        File[] files = dir.listFiles();

        if (files != null) {
            imageFiles = new ArrayList<>();  // 初始化空的 ArrayList
            for (File file : files) {
                if (file.getName().endsWith(".png")) {  // 檢查檔案是否是 PNG 圖片
                    imageFiles.add(file);  // 將 File 添加到 ArrayList 中
                }
            }

            if (imageFiles.size() > 0) {
                // 設置適配器
                imageAdapter = new ImageAdapter(this, imageFiles);
                gridView.setAdapter(imageAdapter);  // 設置 GridView 的 Adapter
            } else {
                Toast.makeText(this, "沒有圖片", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
