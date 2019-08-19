package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2018/10/15 0015.
 */
public class NormalWidget extends RelativeLayout {

    private ImageView ivDialogokWidgetBg;
    private TextView tvDialogokWidgetTitle;
    private TextView tvDialogokWidgetDes;
    private TextView tvDialogokWidgetCancel;
    private TextView tvDialogokWidgetOk;

    public NormalWidget(Context context) {
        this(context, null, 0);
    }

    public NormalWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NormalWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.widget_dialogok, this);
        ivDialogokWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivDialogokWidgetBg.setOnClickListener(v -> bgClickNext());
        tvDialogokWidgetTitle = findViewById(R.id.tv_dialogok_widget_title);
        tvDialogokWidgetDes = findViewById(R.id.tv_dialogok_widget_des);
        tvDialogokWidgetCancel = findViewById(R.id.tv_dialogok_widget_cancel);
        tvDialogokWidgetCancel.setOnClickListener(v -> {
            setVisibility(GONE);
            cancelClickNext();
        });
        tvDialogokWidgetOk = findViewById(R.id.tv_dialogok_widget_ok);
        tvDialogokWidgetOk.setOnClickListener(v -> {
            setVisibility(GONE);
            okClickNext();
        });
    }

    /**
     * 设置标题
     *
     * @param title 标题字符串
     */
    public void setTitle(String title) {
        tvDialogokWidgetTitle.setText(title);
    }

    /**
     * 设置标题
     *
     * @param titleRef 标题字符串资源应用
     */
    public void setTitle(@StringRes int titleRef) {
        tvDialogokWidgetTitle.setText(titleRef);
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

    /**
     * 设置确定按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setOkVisible(boolean isVisible) {
        tvDialogokWidgetOk.setVisibility(isVisible ? VISIBLE : GONE);
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

    // Inteerface--> 接口OnOkClickListener
    public interface OnOkClickListener {
        void okClick();
    }

    // 对外方式setOnOkClickListener
    public void setOnOkClickListener(OnOkClickListener onOkClickListener) {
        this.onOkClickListener = onOkClickListener;
    }

    // 封装方法okClickNext
    private void okClickNext() {
        if (onOkClickListener != null) {
            onOkClickListener.okClick();
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
