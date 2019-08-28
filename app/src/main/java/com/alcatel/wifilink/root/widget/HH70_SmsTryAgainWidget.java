package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by wzhiqiang on 2019/8/27
 */
public class HH70_SmsTryAgainWidget extends RelativeLayout {
    public HH70_SmsTryAgainWidget(Context context) {
        this(context,null,0);
    }

    public HH70_SmsTryAgainWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HH70_SmsTryAgainWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_smsdetail_tryagain, this);
        ImageView ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> {});

        TextView tv_cancel = findViewById(R.id.tv_smsdetail_tryagain_cancel);
        TextView tv_confirm = findViewById(R.id.tv_smsdetail_tryagain_confirm);
        tv_cancel.setOnClickListener(v -> setVisibility(GONE));
        tv_confirm.setOnClickListener(v -> {
            setVisibility(GONE);
            confirmClickNext();
        });
    }

    public interface OnConfirmClickListener {
        void confirm();
    }

    private OnConfirmClickListener onConfirmClickListener;

    //对外方式setOnConfirmClickListener
    public void setOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.onConfirmClickListener = onConfirmClickListener;
    }

    //封装方法
    private void confirmClickNext() {
        if (onConfirmClickListener != null) {
            onConfirmClickListener.confirm();
        }
    }
}
