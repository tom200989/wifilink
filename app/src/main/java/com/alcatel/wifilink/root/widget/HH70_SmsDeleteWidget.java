package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

/**
 * Created by wzhiqiang on 2019/8/27
 */
public class HH70_SmsDeleteWidget extends RelativeLayout {
    public HH70_SmsDeleteWidget(Context context) {
        this(context,null,0);
    }

    public HH70_SmsDeleteWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HH70_SmsDeleteWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_smsdetail_deleted, this);
        ImageView ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> {});
        Button tv_delete_cancel = findViewById(R.id.tv_smsdetail_detele_cancel);
        Button tv_delete_confirm = findViewById(R.id.tv_smsdetail_detele_confirm);
        tv_delete_cancel.setOnClickListener(v -> cancelNext());
        tv_delete_confirm.setOnClickListener(v -> {
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

    public interface OnCancelClickListener {
        void cancel();
    }

    private OnCancelClickListener onCancelClickListener;

    //对外方式setOnCancelClickListener
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    //封装方法cancelNext
    private void cancelNext() {
        if (onCancelClickListener != null) {
            onCancelClickListener.cancel();
        }
    }
}
