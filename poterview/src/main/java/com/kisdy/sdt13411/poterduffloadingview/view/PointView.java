package com.kisdy.sdt13411.poterduffloadingview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by sdt13411 on 2015/12/14.
 */
public class PointView extends View {

    private static final String TAG ="PointView";

    public PointView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(100,100,30);
        init(200,100,30);
        init(100,300,30);
        init(200,300,30);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        canvas.drawRect(400, 400, 700, 700, mPaint);

        canvas.save();
        canvas.rotate(30);  //旋转方向为顺时针，旋转点为左上角

        mPaint.setColor(Color.RED);
        canvas.drawRect(100, 100, 200, 300, mPaint);
        canvas.restore();

        /*canvas.save();
        canvas.rotate(30);

        mPaint.setColor(Color.YELLOW);
        canvas.drawRect(250, 500, 400, 600, mPaint);
        canvas.restore();*/




    }

    //计算矩形的每个点的转转后的坐标和每个点的初始角度  TEST CODE
    //三角函数需要熟练使用
    //角度与弧度关系也要掌握
    //还有Math类的Api参数和返回值与数学集合起来使用，也不是那么简单的事情
    void init(int x,int y,int rotate) {
        Log.d(TAG, "--------------------------------------------");

        double a=Math.atan((1.0f*y)/x);  //角度弧度
        Log.d(TAG, "a=:" + a);
        double a_angle=a*180/Math.PI;  //角度
        Log.d(TAG, "a_angle=:" + a_angle);


        double sin_a= Math.sin(a_angle * Math.PI / 180);
        double sin_b= Math.sin(rotate * Math.PI / 180);

        double cos_a= Math.cos(a_angle * Math.PI / 180);
        double cos_b= Math.cos(rotate * Math.PI / 180);
        double result=Math.pow(x,2)+Math.pow(y,2);
        Log.d(TAG, "result=:" + result);
        double r=Math.sqrt(result);


        int y1= (int) (r*Math.sin((a_angle+rotate)* Math.PI / 180));
        int x1= (int) (r*Math.cos((a_angle+rotate)* Math.PI / 180));
        Log.d(TAG, "x1=:" + x1);
        Log.d(TAG, "y1=:" + y1);

        Log.d(TAG, "--------------------------------------------");
    }
}
