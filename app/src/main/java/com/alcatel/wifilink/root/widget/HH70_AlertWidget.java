package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_AlertWidget extends RelativeLayout {
    private View inflate;

    public HH70_AlertWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_AlertWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_AlertWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_setplan_alert, this);
        inflate.findViewById(R.id.iv_alert_bg).setOnClickListener(null);
        inflate.findViewById(R.id.tv_pop_setPlan_alert_not_reminder).setOnClickListener(v -> {
            setVisibility(GONE);
            clickAlertItemNext(-1);
        });
        inflate.findViewById(R.id.tv_pop_setPlan_alert_90).setOnClickListener(v -> {
            setVisibility(GONE);
            clickAlertItemNext(90);
        });
        inflate.findViewById(R.id.tv_pop_setPlan_alert_80).setOnClickListener(v -> {
            setVisibility(GONE);
            clickAlertItemNext(80);
        });
        inflate.findViewById(R.id.tv_pop_setPlan_alert_70).setOnClickListener(v -> {
            setVisibility(GONE);
            clickAlertItemNext(70);
        });
        inflate.findViewById(R.id.tv_pop_setPlan_alert_60).setOnClickListener(v -> {
            setVisibility(GONE);
            clickAlertItemNext(60);
        });
        inflate.findViewById(R.id.tv_pop_setPlan_alert_cancel).setOnClickListener(v -> setVisibility(GONE));
    }

    private OnClickAlertItemListener onClickAlertItemListener;

    // Inteerface--> 接口OnClickAlertItemListener
    public interface OnClickAlertItemListener {
        void clickAlertItem(int alert);
    }

    // 对外方式setOnClickAlertItemListener
    public void setOnClickAlertItemListener(OnClickAlertItemListener onClickAlertItemListener) {
        this.onClickAlertItemListener = onClickAlertItemListener;
    }

    // 封装方法clickAlertItemNext
    private void clickAlertItemNext(int alert) {
        if (onClickAlertItemListener != null) {
            onClickAlertItemListener.clickAlertItem(alert);
        }
    }
}
