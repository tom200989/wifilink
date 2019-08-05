package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.Lgg;

/*
 * Created by qianli.ma on 2018/10/15 0015.
 */
public class WaitWidget extends RelativeLayout {

    private String TAG = "WaitWidget";
    private ImageView ivWaitBg;
    private ImageView ivWaitLogo;
    private TextView tvWaitDes;

    public WaitWidget(Context context) {
        this(context, null, 0);
    }

    public WaitWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaitWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.widget_wait, this);
        ivWaitBg = findViewById(R.id.iv_wait_bg);
        ivWaitBg.setOnClickListener(v -> Lgg.t(TAG).ii("click not area"));
        ivWaitLogo = findViewById(R.id.iv_wait_logo);
        tvWaitDes = findViewById(R.id.tv_wait_des);
        setAnim();
    }

    private void setAnim() {
        RotateAnimation ra = new RotateAnimation(0, 360, 1, 0.5f, 1, 0.5f);
        ra.setRepeatCount(Animation.INFINITE);
        ra.setRepeatMode(Animation.INFINITE);
        ivWaitLogo.setAnimation(ra);
        ra.startNow();
        ivWaitLogo.startAnimation(ra);
    }

    public void setDes(String content) {
        tvWaitDes.setText(content);
    }

    public void setDes(@StringRes int refId) {
        tvWaitDes.setText(refId);
    }
}
