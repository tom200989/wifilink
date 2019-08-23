package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by wzhiqiang on 2019/8/20
 */
public class HH70_ProfileWidget extends RelativeLayout {

    private ImageView ivDialogokWidgetBg;
    private TextView tvDialogokWidgetDes;
    private TextView tvDialogokWidgetCancel;

    public HH70_ProfileWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_ProfileWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_ProfileWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_profile, this);
        ivDialogokWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivDialogokWidgetBg.setOnClickListener(v -> bgClickNext());
        tvDialogokWidgetDes = findViewById(R.id.tv_dialogok_widget_des);
        tvDialogokWidgetCancel = findViewById(R.id.tv_dialogok_widget_cancel);
        tvDialogokWidgetCancel.setOnClickListener(v -> {
            setVisibility(GONE);
            cancelClickNext();
        });
    }

    /**
     * 设置内容
     *
     * @param content 内容字符串
     */
    public void setDes(String content) {
        tvDialogokWidgetDes.setText(content);
    }

    /**
     * 设置内容
     *
     * @param desRef 内容字符串资源应用
     */
    public void setDes(@StringRes int desRef) {
        tvDialogokWidgetDes.setText(desRef);
    }

    /**
     * 设置取消按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setCancelVisible(boolean isVisible) {
        tvDialogokWidgetCancel.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /* -------------------------------------------- impl -------------------------------------------- */
    private OnBgClickListener onBgClickListener;

    // Inteerface--> 接口OnBgClickListener
    public interface OnBgClickListener {
        void bgClick();
    }

    // 对外方式setOnBgClickListener
    public void setOnBgClickListener(OnBgClickListener onBgClickListener) {
        this.onBgClickListener = onBgClickListener;
    }

    // 封装方法bgClickNext
    private void bgClickNext() {
        if (onBgClickListener != null) {
            onBgClickListener.bgClick();
        }
    }


    private OnCancelClickListener onCancelClickListener;

    // Inteerface--> 接口OnCancelClickListener
    public interface OnCancelClickListener {
        void cancelClick();
    }

    // 对外方式setOnCancelClickListener
    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }

    // 封装方法cancelClickNext
    private void cancelClickNext() {
        if (onCancelClickListener != null) {
            onCancelClickListener.cancelClick();
        }
    }
}
