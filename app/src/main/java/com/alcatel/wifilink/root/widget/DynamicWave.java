package com.alcatel.wifilink.root.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.view.View;

import com.alcatel.wifilink.root.utils.C_CommonUtil;

public class DynamicWave extends View {

    // 波纹颜色
    private static final int WAVE_PAINT_COLOR = 0xffffffff;
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 40;
    private static final int OFFSET_Y = -20;/* 此处调节波纹的高度和振幅,值越小,振幅越明显 */
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 8;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 6;
    //第三条水波移动速度
    private static final int TRANSLATE_X_SPEED_THREE = 7;
    private float mCycleFactorW;

    private int mTotalWidth, mTotalHeight;
    private float[] mYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private float[] mResetThreePositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOffsetSpeedThree;
    private int mXOneOffset;
    private int mXTwoOffset;
    private int mXThreeOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private float rate = 1f;

    public DynamicWave(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = C_CommonUtil.dipToPx(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = C_CommonUtil.dipToPx(context, TRANSLATE_X_SPEED_TWO);
        mXOffsetSpeedThree = C_CommonUtil.dipToPx(context, TRANSLATE_X_SPEED_THREE);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Style.FILL);
        // 设置画笔颜色
        mWavePaint.setColor(WAVE_PAINT_COLOR);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    /* 设置高度缩放倍率 */
    public void setDelY(float rate) {
        if (rate < 0) {
            rate = 1f;
        }
        if (rate > 5) {
            rate = 5f;
        }
        this.rate = rate;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        for (int i = 0; i < mTotalWidth; i++) {

            // 减400只是为了控制波纹绘制的y的在屏幕的位置，大家可以改成一个变量，然后动态改变这个变量，从而形成波纹上升下降效果
            // 绘制第一条水波纹
            canvas.drawLine(i, mTotalHeight - mResetOneYPositions[i] / rate, i, mTotalHeight, mWavePaint);

            // 绘制第二条水波纹
            canvas.drawLine(i, mTotalHeight - mResetTwoYPositions[i] / rate, i, mTotalHeight, mWavePaint);

            //绘制第三条水波纹
            canvas.drawLine(i, mTotalHeight - mResetThreePositions[i] / rate, i, mTotalHeight, mWavePaint);
        }

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;
        mXThreeOffset += mXOffsetSpeedThree;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }
        if (mXThreeOffset > mTotalWidth) {
            mXThreeOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        postInvalidate();
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mYPositions.length - mXTwoOffset;
        System.arraycopy(mYPositions, mXTwoOffset, mResetTwoYPositions, 0, yTwoInterval);
        System.arraycopy(mYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);

        int yThreeInterval = mYPositions.length - mXThreeOffset;
        System.arraycopy(mYPositions, mXThreeOffset, mResetThreePositions, 0, yThreeInterval);
        System.arraycopy(mYPositions, 0, mResetThreePositions, yThreeInterval, mXThreeOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w;
        mTotalHeight = h;
        // 用于保存原始波纹的y值
        mYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];
        //用于保存波纹三的y值
        mResetThreePositions = new float[mTotalWidth];

        // 将周期定为view总宽度
        mCycleFactorW = (float) (2 * Math.PI / mTotalWidth);

        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mYPositions[i] = (float) (STRETCH_FACTOR_A * Math.sin(mCycleFactorW * i) + OFFSET_Y);
        }
    }

}
