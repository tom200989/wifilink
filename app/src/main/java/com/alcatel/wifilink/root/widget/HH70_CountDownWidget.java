package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_CountDownWidget extends RelativeLayout {

    private View inflate;
    private CountDownWidget ctv;

    public HH70_CountDownWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_CountDownWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_CountDownWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_countdown, this);
        findViewById(R.id.iv_count_bg).setOnClickListener(null);
        ctv = inflate.findViewById(R.id.ctv_pop_upgrade);
        ctv.setCount(180);
        ctv.setTopColor(Color.parseColor("#009AFF"));
        ctv.setBottomColor(Color.parseColor("#009AFF"));
        ctv.run();
    }

    public CountDownWidget getCountDownText() {
        return ctv;
    }
}
