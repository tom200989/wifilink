package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class ExtenderWait extends RelativeLayout {

    private RelativeLayout wifiExtenderWait;

    public ExtenderWait(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.widget_extender_wait_layout, this);
        wifiExtenderWait = (RelativeLayout) findViewById(R.id.rl_widget_wifi_extender_wait);
    }

    public ExtenderWait(Context context) {
        this(context, null,0);
    }

    public ExtenderWait(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }
}
