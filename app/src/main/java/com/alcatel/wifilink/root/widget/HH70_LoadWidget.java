package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class HH70_LoadWidget extends RelativeLayout {

    private View inflate;
    private ImageView ivLoad;

    public HH70_LoadWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_LoadWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_LoadWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_load, this);
        ivLoad = inflate.findViewById(R.id.iv_wd_load);
    }

    public void setVisibles() {
        startAnim();
    }

    public void setGone() {
        stopAnim();
    }

    /**
     * 显示并启动动画
     */
    private void startAnim() {
        setVisibility(VISIBLE);
        RotateAnimation ro = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ro.setDuration(2000);
        ro.setInterpolator(new LinearInterpolator());
        ro.setFillAfter(true);
        ro.setRepeatMode(Animation.INFINITE);
        ro.setRepeatCount(Animation.INFINITE);
        ivLoad.setAnimation(ro);
        ro.startNow();
        ro.start();
    }

    /**
     * 停止动画并消隐
     */
    private void stopAnim() {
        Animation animation = ivLoad.getAnimation();
        if (animation != null) {
            animation.cancel();
            ivLoad.clearAnimation();
            setVisibility(GONE);
        }
        
    }
}
