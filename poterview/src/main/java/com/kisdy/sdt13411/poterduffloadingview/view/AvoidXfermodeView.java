
package com.kisdy.sdt13411.poterduffloadingview.view;

import android.content.Context;
import android.graphics.AvoidXfermode;
import android.graphics.AvoidXfermode.Mode;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.kisdy.sdt13411.poterduffloadingview.R;

public class AvoidXfermodeView extends View {

    private Paint mBitmapPaint, mAvoidPaint;

    private int mTotalWidth, mTotalHeight;

    private Bitmap mBitmap;
    private int mBitWidth, mBitHeight;
    private Rect mOriginSrcRect, mOriginDestRect;
    private Rect mAvoidSrcRect, mAvoidDestRect;

    private AvoidXfermode mAvoidXfermode;

    public AvoidXfermodeView(Context context,AttributeSet attrs) {
        super(context,attrs);
        initPaint();
        initBitmap();
        // 对蓝色相近的颜色进行替换
        mAvoidXfermode = new AvoidXfermode(Color.BLUE, 150, Mode.TARGET);
    }

    private void initBitmap() {
        mBitmap = ((BitmapDrawable) getResources().getDrawable(R.mipmap.bluelogo)).getBitmap();
        mBitWidth = mBitmap.getWidth();
        mBitHeight = mBitmap.getHeight();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();
        // 去锯齿
        mBitmapPaint.setAntiAlias(true);
        // 防抖动
        mBitmapPaint.setDither(true);
        // 图像过滤
        mBitmapPaint.setFilterBitmap(true);

        // 使用上面属性创建一个新paint
        mAvoidPaint = new Paint(mBitmapPaint);
        // 颜色设置为红色
        mAvoidPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制原图
        canvas.drawBitmap(mBitmap, mOriginSrcRect, mOriginDestRect, mBitmapPaint);

        // 绘制用于变色图
        canvas.drawBitmap(mBitmap, mAvoidSrcRect, mAvoidDestRect, mAvoidPaint);
        // 设置图层混合模式
        mAvoidPaint.setXfermode(mAvoidXfermode);
        // 绘制色块进行混合，得到最终效果
        canvas.drawRect(mAvoidDestRect, mAvoidPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;

        mOriginSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
        // 为了让图水平居中
        int left = (mTotalWidth - mBitWidth) / 2;
        mOriginDestRect = new Rect(left, 0, left + mBitWidth, mBitHeight);

        mAvoidSrcRect = new Rect(mOriginSrcRect);
        // 两张图得间距
        int distance = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10,
                getResources().getDisplayMetrics());
        mAvoidDestRect = new Rect(left, mBitHeight + distance, left + mBitWidth, mBitHeight * 2
                + distance);
    }
}
