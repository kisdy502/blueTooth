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
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.kisdy.sdt13411.poterduffloadingview.R;

/**
 * Created by sdt13411 on 2015/12/14.
 */
public class PoterDuffLoadingView extends View {

    private static final String TAG ="PoterDuffLoadingView" ;
    private Resources mResources;
    private Paint mBitPaint;
    private Bitmap mBitmap;

    private int mTotalWidth, mTotalHeight;  //总宽高
    private int mBitWidth, mBitHeight;      //图宽高

    private Rect mSrcRect, mDestRect;       //源矩形区域,目标矩形区域
    private PorterDuffXfermode mXfermode;   //Xfermode

    private Rect mDynamicRect;
    private int mCurrentTop;
    private int mStart, mEnd;

    boolean isfirstDraw=false;

    public PoterDuffLoadingView(Context context) {
        this(context, null, 0);
    }

    public PoterDuffLoadingView(Context context, AttributeSet attrs){
        this(context, attrs,0);
    }

    public PoterDuffLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mResources = getResources();
        initBitmap();
        initPaint();
        initXfermode();
        int left = (int) TypedValue.complexToDimension(20, mResources.getDisplayMetrics());
        Log.i(TAG, "left_PoterDuffLoadingView:" + left);
    }

    private void initBitmap() {
        // 初始化bitmap
        mBitmap = ((BitmapDrawable) mResources.getDrawable(R.mipmap.ga_studio))
                .getBitmap();
        mBitWidth = mBitmap.getWidth();
        mBitHeight = mBitmap.getHeight();
        Log.i(TAG,"mBitWidth:"+mBitWidth);
        Log.i(TAG,"mBitHeight:"+mBitHeight);
    }

    private void initPaint() {
        // 初始化paint
        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);
        mBitPaint.setColor(Color.RED);
    }

    private void initXfermode() {
        // 叠加处绘制源图
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.ADD);
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR); //清除掉原来图层
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DARKEN);//覆盖原来图层
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST);     //没有任何影响
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_ATOP);  //在原来图层下方绘制
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);  //
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        //mXfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OVER);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if(!isfirstDraw) {
            Log.i(TAG,"onDraw");
            isfirstDraw=true;
        }
        super.onDraw(canvas);
        // 存为新图层
        int saveLayerCount = canvas.saveLayer(0, 0, mTotalWidth, mTotalHeight, mBitPaint,
                Canvas.ALL_SAVE_FLAG);
        // 绘制目标图 绿色底图
        canvas.drawBitmap(mBitmap, mSrcRect, mDestRect, mBitPaint);
        // 设置混合模式
        mBitPaint.setXfermode(mXfermode);
        // 绘制源图形
        canvas.drawRect(mDynamicRect, mBitPaint);
        // 清除混合模式
        mBitPaint.setXfermode(null);
        // 恢复保存的图层；
        canvas.restoreToCount(saveLayerCount);

        // 改变Rect区域，真实情况下时提供接口传入进度，计算高度
        mCurrentTop -= 4;
        if (mCurrentTop <= mEnd) {
            mCurrentTop = mStart;
        }
        mDynamicRect.top = mCurrentTop;
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.i(TAG,"onSizeChanged");
        Log.i(TAG, "w:" + w);
        Log.i(TAG, "h:" + h);
        Log.i(TAG, "oldw:" + oldw);
        Log.i(TAG, "oldh:" + oldh);
        super.onSizeChanged(w, h, oldw, oldh);
        mTotalWidth = w;
        mTotalHeight = h;
        mSrcRect = new Rect(0, 0, mBitWidth, mBitHeight);
        // 让左边和上边有些距离
        int left = (int) TypedValue.complexToDimension(20, mResources.getDisplayMetrics());
        Log.i(TAG, "left:" + left);
        mDestRect = new Rect(left, left, left + mBitWidth, left + mBitHeight);
        mStart = left + mBitHeight;
        mCurrentTop = mStart;
        mEnd = left;
        mDynamicRect = new Rect(left, mStart, left + mBitWidth, left + mBitHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.i(TAG,"onLayout");
        Log.i(TAG, "left:" + left);
        Log.i(TAG, "top:" + top);
        Log.i(TAG, "right:" + right);
        Log.i(TAG, "bottom:" + bottom);
        Log.i(TAG, "changed:" + changed);
        super.onLayout(changed, left, top, right, bottom);
    }
}
