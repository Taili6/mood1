package com.example.myapplication;
import android.content.Context;
import android.graphics.*;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DrawingView extends View {
    private Paint paint; // 畫筆
    private Path path; // 使用者繪畫的路徑
    private Bitmap bitmap;
    private Canvas canvas;
    private int brushColor = Color.BLACK; // 預設顏色
    private float brushSize = 10f; // 預設筆刷大小
    private boolean isEraserMode = false;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public int getBrushColor() {
        return brushColor;
    }


    private void init() {
        paint = new Paint();
        paint.setColor(brushColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(brushSize);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                canvas.drawPath(path, paint);
                path.reset();
                break;
        }

        invalidate();
        return true;
    }

    // 清除畫布
    public void clearCanvas() {
        bitmap.eraseColor(Color.TRANSPARENT);
        invalidate();
    }

    // 設定筆刷顏色
    public void setBrushColor(int color) {
        this.brushColor = color;
        paint.setColor(color);
    }

    // 設定筆刷大小
    public void setBrushSize(float size) {
        this.brushSize = size;
        paint.setStrokeWidth(size);
    }

    // 儲存畫布為圖片
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void enableEraser(boolean enable) {
        isEraserMode = enable;
        if (enable) {
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(50); // 橡皮擦大小，可以調整
        } else {
            paint.setXfermode(null);
            paint.setStrokeWidth(brushSize);
            paint.setColor(brushColor);
        }
    }
    public String saveCanvasToFile(Context context, Bitmap bitmap) {
        String fileName = "drawing_" + System.currentTimeMillis() + ".png";
        FileOutputStream fos = null;

        try {
            // 儲存圖片到應用內部儲存空間
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);  // 壓縮並儲存圖片
            fos.flush();
            return context.getFilesDir() + "/" + fileName;  // 回傳儲存的檔案路徑
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
