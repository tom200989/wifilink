package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.SetTimeLimitHelper;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_TimelimitWidget extends RelativeLayout {

    private View inflate;

    public HH70_TimelimitWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_TimelimitWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_TimelimitWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_setplan_timelimit, this);
        inflate.findViewById(R.id.iv_timelimit_bg).setOnClickListener(null);
        EditText etHour = inflate.findViewById(R.id.et_pop_setPlan_rx_settimelimit_hour);
        EditText etMin = inflate.findViewById(R.id.et_pop_setPlan_rx_settimelimit_min);
        SetTimeLimitHelper.addEdwatch(etHour, etMin);// 增加监听器
        View cancel = inflate.findViewById(R.id.tv_pop_setPlan_rx_settimelimit_cancel);
        View ok = inflate.findViewById(R.id.tv_pop_setPlan_rx_settimelimit_ok);
        cancel.setOnClickListener(v -> setVisibility(GONE));
        ok.setOnClickListener(v -> {
            clickOkNext(etHour, etMin);
            setVisibility(GONE);
        });
    }

    private OnClickOkListener onClickOkListener;

    // Inteerface--> 接口OnClickOkListener
    public interface OnClickOkListener {
        void clickOk(EditText etHour, EditText etMin);
    }

    // 对外方式setOnClickOkListener
    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    // 封装方法clickOkNext
    private void clickOkNext(EditText etHour, EditText etMin) {
        if (onClickOkListener != null) {
            onClickOkListener.clickOk(etHour, etMin);
        }
    }
}
