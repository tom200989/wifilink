package com.alcatel.wifilink.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class HH70_ExtenderWaitWidget extends RelativeLayout {

    private RelativeLayout wifiExtenderWait;

    public HH70_ExtenderWaitWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_extend_wait, this);
        wifiExtenderWait = (RelativeLayout) findViewById(R.id.rl_widget_wifi_extender_wait);
    }

    public HH70_ExtenderWaitWidget(Context context) {
        this(context, null,0);
    }

    public HH70_ExtenderWaitWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
}
