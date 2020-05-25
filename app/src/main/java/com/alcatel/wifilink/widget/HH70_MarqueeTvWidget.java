package com.alcatel.wifilink.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯
 * 
 * Created by wzq on 2019/5/23 0023.
 */
@SuppressLint("AppCompatCustomView")
public class HH70_MarqueeTvWidget extends TextView {
    public HH70_MarqueeTvWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_MarqueeTvWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_MarqueeTvWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setSingleLine();
        setFocusable(true);
        setFocusableInTouchMode(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
        setHorizontallyScrolling(true);
    }

    /**
     * 返回textview是否处在选中的状态
     * 而只有选中的textview才能够实现跑马灯效果
     *
     * @return true:选中
     */
    @Override
    public boolean isFocused() {
        return true;
    }

    /**
     * edittxt控制了焦点后，跑马灯就不跑了，所以重写这个方法
     * 当focused为true时，就复写父类的方法，当focus为false时，就不重写
     *
     * @param focused               是否获取到了焦点
     * @param direction             方向
     * @param previouslyFocusedRect 焦点范围
     */
    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        if (focused) {
            super.onFocusChanged(focused, direction, previouslyFocusedRect);
        }
    }

    /**
     * dialog,popUpwindow控制了焦点后，跑马灯就不跑了，所以重写这个方法
     * 当hasWindowFocus为true时，就复写父类的方法，当hasWindowFocus为false时，就不重写
     *
     * @param hasWindowFocus 是否获取窗口焦点
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (hasWindowFocus) {
            super.onWindowFocusChanged(hasWindowFocus);
        }
    }
}
