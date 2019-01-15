package com.sourceof0.matrixtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SampleMatrixView extends View
{
    private Bitmap bmp;
    private Canvas bmpCanvas;
    private Paint bmpPaint;

    private Paint paint;

    private Matrix starMatrix;
    private Path star;

    private Matrix gradientMatrix;
    private Shader gradient;

    private float posX;
    private float posY;

    public SampleMatrixView(Context context, AttributeSet attr) {
        this(context, attr, 0, 0);
    }
    public SampleMatrixView(Context context, AttributeSet attr, int width, int height) {
        super(context, attr);

        bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bmpCanvas = new Canvas(bmp);

        bmpPaint = new Paint();
        bmpPaint.setDither(true);
        bmpPaint.setAntiAlias(true);

        /* 描画モード */
        //bmpPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)); // デフォ
        bmpPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SCREEN));

        float size = width / 2;

        starMatrix = new Matrix();
        starMatrix.postScale(size, size);

        gradientMatrix = new Matrix();

        /* 円形グラデーション */
        //gradient = new RadialGradient(0f, 0f, size, 0xFFFFFF00, 0xFF000000, android.graphics.Shader.TileMode.CLAMP);

        /* 線形グラデーション */
        gradient = new LinearGradient(-size, -size, size, size, 0xFFFFFF00, 0xFF000000, android.graphics.Shader.TileMode.CLAMP);

        paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);

        /* 塗り */
        paint.setStyle(Paint.Style.FILL); // 塗りつぶし（線なし）
        //paint.setStyle(Paint.Style.STROKE); // 線（塗りつぶしなし）
        //paint.setStyle(Paint.Style.FILL_AND_STROKE); // 塗りつぶしと線

        /* 色 */
        //paint.setColor(Color.argb(200, 255, 255, 0));

        /* 星型パス生成 */
        star = new Path();
        star.moveTo(0.0f, -1.0f);
        for ( int i = 1; i <= 10; i++ ) {
            double rad = 2 * Math.PI * i / 10;
            double len = (i % 2 == 0)? 1.0f : 0.5f;
            float x = (float)(Math.sin(rad) * len);
            float y = (float)(-Math.cos(rad) * len);
            star.lineTo(x, y);
        }
        starMatrix.postTranslate(posX, posY);
        star.transform(starMatrix);

        /* 背景色 */
        setBackgroundColor(Color.argb(255, 0, 50, 100));
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        /* canvasやbmpにロックは無いらしい。やるならSurfaceViewで。 */
        canvas.drawBitmap(bmp, 0, 0, bmpPaint);
    }

    protected void update()
    {
        if ( getWidth() == 0 ) return;
        starMatrix.reset();
        starMatrix.postTranslate(-posX, -posY);
        /* 1度ずつ回転 */
        starMatrix.postRotate(1);
        posX = getWidth() / 2.0f;
        posY = getHeight() / 2.0f;
        starMatrix.postTranslate(posX, posY);
        star.transform(starMatrix);

        gradientMatrix.postConcat(starMatrix);
        gradient.setLocalMatrix(gradientMatrix);

        paint.setShader(gradient);

        /* 画面クリア */
        //bmpCanvas.drawColor(0, PorterDuff.Mode.CLEAR);

        bmpCanvas.drawPath(star, paint);
    }
}