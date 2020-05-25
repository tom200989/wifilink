package com.alcatel.wifilink.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author wdpm
 * use handler and  message to implement count down
 */
@SuppressLint("AppCompatCustomView")
public class CountDownWidget extends TextView {
    private Paint mPaint;
    private int mFontHeight = 0;
    private Shader mShader;

    private static final int DEFAULT_COUNT = 60;
    private static final Typeface DEFAULT_TYPE_FACE = Typeface.SERIF;
    private static final int DEFAULT_TOP_COLOR = Color.BLACK; // 默认颜色上部分黑色
    private static final int DEFAULT_BOTTOM_COLOR = Color.BLACK; // 默认颜色下部分黑色

    private int mCount = DEFAULT_COUNT;
    private boolean isRunning = false;

    private int mUserCount = DEFAULT_COUNT;
    private Typeface mTypeface = DEFAULT_TYPE_FACE;
    private int mTopColor = DEFAULT_TOP_COLOR;
    private int mBottomColor = DEFAULT_BOTTOM_COLOR;

    private String mText = "60";
    private float mTextSize;

    public int getBottomColor() {
        return mBottomColor;
    }

    public void setBottomColor(int mBottomColor) {
        this.mBottomColor = mBottomColor;
    }

    public int getTopColor() {
        return mTopColor;
    }

    public void setTopColor(int mTopColor) {
        this.mTopColor = mTopColor;
    }

    public CountDownWidget(Context context) {
        super(context);
        init(null);
    }

    public CountDownWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CountDownWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            readAttrs(attrs);
        }
        mPaint = new Paint();
        setText(mText);
    }

    private void readAttrs(AttributeSet attrs) {
        int[] attrsArray = new int[]{android.R.attr.text, android.R.attr.textSize};
        TypedArray ta = getContext().obtainStyledAttributes(attrs, attrsArray);
        mText = (String) ta.getText(0);
        if (TextUtils.isEmpty(mText)) {
            mText = "Begin";
        }
        mTextSize = ta.getDimension(1, mTextSize);

        ta.recycle();
        setTypeface(mTypeface);
        setCount(CanParseToInt(mText) ? Integer.parseInt(mText) : DEFAULT_COUNT);
    }

    private boolean CanParseToInt(String mText) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher(mText);
        boolean result = matcher.matches();
        return result;

    }

    public int getCount() {
        return mCount;
    }

    public void setCount(int mCount) {
        this.mCount = mCount;
        this.mUserCount = mCount;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mFontHeight == 0) {
            mPaint = getPaint();
            mFontHeight = getMeasuredHeight() * 7 / 9;
            if (mFontHeight > 0) {
                mShader = new LinearGradient(0, 0, 0, mFontHeight, mTopColor, mBottomColor, Shader.TileMode.CLAMP);
                mPaint.setShader(mShader);
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    mCount--;
                    if (mCount < 0) {
                        return;
                    }
                    setText(String.valueOf(mCount));
                    invalidate();
                    Message message = mHandler.obtainMessage(1);
                    mHandler.sendMessageDelayed(message, 1000);
                case 2:
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void run() {
        if (!isRunning) {//true,represent count is running. no need to run repeatly
            isRunning = true;// set run state true
            Message message = mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
        }
    }

    public void pause() {
        if (isRunning) {
            mHandler.removeMessages(1);
            isRunning = false;
        }
    }

    public void reset() {
        if (isRunning) {
            mHandler.removeMessages(1);
        }

        isRunning = false;
        mCount = mUserCount;
        this.setText(String.valueOf(mUserCount));
        // invalidate();
        run();
    }

}
