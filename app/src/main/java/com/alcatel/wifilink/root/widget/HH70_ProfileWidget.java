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

    private ImageView ivWidgetBg;
    private TextView tvWidgetDes;
    private TextView tvWidgetCancel;

    public HH70_ProfileWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_ProfileWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_ProfileWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_profile, this);
        ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> bgClickNext());

        tvWidgetDes = findViewById(R.id.tv_dialogok_widget_des);
        tvWidgetCancel = findViewById(R.id.tv_dialogok_widget_cancel);
        tvWidgetCancel.setOnClickListener(v -> {
            setVisibility(GONE);
            okClickNext();
        });
    }

    /**
     * 设置内容
     *
     * @param content 内容字符串
     */
    public void setDes(String content) {
        tvWidgetDes.setText(content);
    }

    /**
     * 设置内容
     *
     * @param desRef 内容字符串资源应用
     */
    public void setDes(@StringRes int desRef) {
        tvWidgetDes.setText(desRef);
    }

    /**
     * 设置取消按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setCancelVisible(boolean isVisible) {
        tvWidgetCancel.setVisibility(isVisible ? VISIBLE : GONE);
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


    private OnOkClickListener onOkClickListener;

    // Inteerface--> 接口OnCancelClickListener
    public interface OnOkClickListener {
        void okClick();
    }

    // 对外方式setOnCancelClickListener
    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    // 封装方法cancelClickNext
    private void okClickNext() {
        if (onOkClickListener != null) {
            onOkClickListener.okClick();
        }
    }
}
