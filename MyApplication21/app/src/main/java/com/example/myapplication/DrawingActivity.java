package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import yuku.ambilwarna.AmbilWarnaDialog;

public class DrawingActivity extends AppCompatActivity {
    private DrawingView drawingView;
    private ImageButton clearButton , btnChangeColor , eraserDrawButton , saveButton , galleryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        drawingView = findViewById(R.id.drawingView);
        clearButton = findViewById(R.id.clearButton);
        btnChangeColor = findViewById(R.id.btnChangeColor);
        eraserDrawButton = findViewById(R.id.eraserDrawButton);
        saveButton = findViewById(R.id.saveButton);
        galleryButton = findViewById(R.id.galleryButton);

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DrawingActivity.this, GalleryActivity.class);
                startActivity(intent);
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawingView.clearCanvas();
            }
        });
        eraserDrawButton.setOnClickListener(new View.OnClickListener() {
            private boolean isEraser = false;

            @Override
            public void onClick(View v) {
                isEraser = !isEraser; // 切換模式
                drawingView.enableEraser(isEraser);

                if (isEraser) {
                    eraserDrawButton.setBackgroundResource(R.drawable.eraser); // 切換按鈕背景
                } else {
                    eraserDrawButton.setBackgroundResource(R.drawable.draw); // 恢復畫筆模式
                }
            }
        });
        btnChangeColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(DrawingActivity.this, drawingView.getBrushColor(), new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        drawingView.setBrushColor(color); // 設定畫筆顏色
                    }

                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        // 如果用戶按取消，不做任何事
                    }
                });
                colorPicker.show(); // 確保對話框被顯示
            }
        });
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 獲取 DrawingView 中的 Bitmap
                Bitmap bitmap = drawingView.getBitmap();

                // 調用 saveCanvasToFile 方法來保存圖片
                String filePath = drawingView.saveCanvasToFile(DrawingActivity.this, bitmap);

                if (filePath != null) {
                    // 儲存成功，傳遞圖片路徑到 SaveActivity
                    Intent intent = new Intent(DrawingActivity.this, SaveActivity.class);
                    intent.putExtra("image_path", filePath);
                    startActivity(intent);
                } else {
                    // 儲存失敗，顯示提示訊息
                    Toast.makeText(DrawingActivity.this, "保存失敗", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
