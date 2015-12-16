
package com.kisdy.sdt13411.poterduffloadingview.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.kisdy.sdt13411.poterduffloadingview.R;


public class PorterDuffXfermodeView extends View {

    private Paint mPaint;
    private Bitmap mBottomBitmap, mTopBitmap;
    private Rect mBottomSrcRect, mBottomDestRect;
    private Rect mTopSrcRect, mTopDestRect;

    private Xfermode mPorterDuffXfermode;
    // 图层混合模式
    private PorterDuff.Mode mPorterDuffMode;
    // 总宽高
    private int mTotalWidth, mTotalHeight;
    private Resources mResources;

    public PorterDuffXfermodeView(Context context,AttributeSet attrs) {
        super(context,attrs);
        mResources = getResources();
        initBitmap();
        initPaint();
        initXfermode();
    }

    // 初始化bitmap
    private void initBitmap() {
        mBottomBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.blue)).getBitmap();
        mTopBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.red)).getBitmap();
    }

    // 初始化混合模式
    private void initXfermode() {
        mPorterDuffMode = PorterDuff.Mode.MULTIPLY;
        mPorterDuffXfermode = new PorterDuffXfermode(mPorterDuffMode);
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 背景铺白
        canvas.drawColor(Color.WHITE);
        // 保存为单独的层
        int saveCount = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, mPaint,
                Canvas.ALL_SAVE_FLAG);
        // 绘制目标图
        canvas.drawBitmap(mBottomBitmap, mBottomSrcRect, mBottomDestRect, mPaint);
        // 设置混合模式
        mPaint.setXfermode(mPorterDuffXfermode);
        // 绘制源图
        canvas.drawBitmap(mTopBitmap, mTopSrcRect, mTopDestRect, mPaint);
        mPaint.setXfermode(null);
        canvas.restoreToCount(saveCount);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        int halfHeight = h / 2;

        mBottomSrcRect = new Rect(0, 0, mBottomBitmap.getWidth(), mBottomBitmap.getHeight());
        // 矩形只画屏幕一半
        mBottomDestRect = new Rect(0, 0, mTotalWidth, halfHeight);

        mTopSrcRect = new Rect(0, 0, mTopBitmap.getWidth(), mTopBitmap.getHeight());
        mTopDestRect = new Rect(0, 0, mTotalWidth, mTotalHeight);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
