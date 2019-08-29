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
public class HH70_NormalWidget extends RelativeLayout {

    private ImageView ivbg;
    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvCancel;
    private TextView tvOK;

    public HH70_NormalWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_NormalWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_NormalWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.hh70_widget_normal, this);
        ivbg = findViewById(R.id.iv_dialogok_widget_bg);
        ivbg.setOnClickListener(v -> bgClickNext());
        tvTitle = findViewById(R.id.tv_dialogok_widget_title);
        tvContent = findViewById(R.id.tv_dialogok_widget_des);
        tvCancel = findViewById(R.id.tv_dialogok_widget_cancel);
        tvCancel.setOnClickListener(v -> {
            setVisibility(GONE);
            cancelClickNext();
        });
        tvOK = findViewById(R.id.tv_dialogok_widget_ok);
        tvOK.setOnClickListener(v -> {
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
        tvTitle.setText(title);
    }

    /**
     * 设置标题
     *
     * @param titleRef 标题字符串资源应用
     */
    public void setTitle(@StringRes int titleRef) {
        tvTitle.setText(titleRef);
    }

    /**
     * 设置内容
     *
     * @param content 内容字符串
     */
    public void setDes(String content) {
        tvContent.setText(content);
    }

    /**
     * 设置内容
     *
     * @param desRef 内容字符串资源应用
     */
    public void setDes(@StringRes int desRef) {
        tvContent.setText(desRef);
    }

    /**
     * 设置取消按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setCancelVisible(boolean isVisible) {
        tvCancel.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /**
     * 设置确定按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setOkVisible(boolean isVisible) {
        tvOK.setVisibility(isVisible ? VISIBLE : GONE);
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
