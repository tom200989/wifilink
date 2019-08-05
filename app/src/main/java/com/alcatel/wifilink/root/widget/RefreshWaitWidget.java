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
 * Created by qianli.ma on 2018/9/6 0006.
 */
public class RefreshWaitWidget extends RelativeLayout {

    private ImageView ivRefreshWidgetConnectingLogo;
    private ImageView ivRefreshWidgetConnectingBg;
    private RotateAnimation ra;

    public RefreshWaitWidget(Context context) {
        this(context, null, 0);
    }

    public RefreshWaitWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshWaitWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = View.inflate(context, R.layout.widget_refreshwait, this);
        ivRefreshWidgetConnectingBg = inflate.findViewById(R.id.iv_refresh_widget_connecting_bg);
        ivRefreshWidgetConnectingBg.setOnClickListener(null);
        ivRefreshWidgetConnectingLogo = inflate.findViewById(R.id.iv_refresh_widget_connecting_logo);
    }

    public void visibleNow() {
        setVisibility(VISIBLE);
        startAnim();
    }

    public void startAnim() {
        ra = new RotateAnimation(0, 360, 1, 0.5f, 1, 0.5f);
        ra.setDuration(2000);
        ra.setInterpolator(new LinearInterpolator());
        ra.setFillAfter(true);
        ra.setRepeatMode(Animation.INFINITE);
        ra.setRepeatCount(Animation.INFINITE);
        ivRefreshWidgetConnectingLogo.setAnimation(ra);
        ivRefreshWidgetConnectingLogo.startAnimation(ra);
        ra.startNow();
    }

    public void inVisibleNow() {
        setVisibility(GONE);
        if (ra != null) {
            ra.cancel();
            ra = null;
        }
    }

}
