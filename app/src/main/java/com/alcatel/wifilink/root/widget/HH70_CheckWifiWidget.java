package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2018/9/6 0006.
 */
public class HH70_CheckWifiWidget extends RelativeLayout {

    private ImageView ivRefreshGetconnectBg;
    private TextView tvRefreshGetconnectOk;

    public HH70_CheckWifiWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_CheckWifiWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_CheckWifiWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View inflate = View.inflate(context, R.layout.hh70_widget_refresh, this);
        ivRefreshGetconnectBg = inflate.findViewById(R.id.iv_refresh_getconnect_bg);
        ivRefreshGetconnectBg.setOnClickListener(null);
        tvRefreshGetconnectOk = inflate.findViewById(R.id.tv_refresh_getconnect_ok);
        tvRefreshGetconnectOk.setOnClickListener(v -> refreshClickOkNext());
    }

    private OnRefreshClickOkListener onRefreshClickOkListener;

    // Inteerface--> 接口OnRefreshClickOkListener
    public interface OnRefreshClickOkListener {
        void refreshClickOk();
    }

    // 对外方式setOnRefreshClickOkListener
    public void setOnRefreshClickOkListener(OnRefreshClickOkListener onRefreshClickOkListener) {
        this.onRefreshClickOkListener = onRefreshClickOkListener;
    }

    // 封装方法refreshClickOkNext
    private void refreshClickOkNext() {
        if (onRefreshClickOkListener != null) {
            onRefreshClickOkListener.refreshClickOk();
        }
    }
}
