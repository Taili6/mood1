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

public class GalleryActivity extends AppCompatActivity {

    private GridView gridView;
    private ImageAdapter imageAdapter;
    private ArrayList<File> imageFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gridView = findViewById(R.id.gridView);

        loadImages();
    }

    private void loadImages() {
        File dir = getFilesDir();  // 取得內部儲存空間
        File[] files = dir.listFiles();

        if (files != null) {
            imageFiles = new ArrayList<>();
            for (File file : files) {
                if (file.getName().endsWith(".png")) {
                    imageFiles.add(file);
                }
            }

            if (imageFiles.size() > 0) {
                imageAdapter = new ImageAdapter(this, imageFiles);
                gridView.setAdapter(imageAdapter);
            } else {
                Toast.makeText(this, "沒有圖片", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
