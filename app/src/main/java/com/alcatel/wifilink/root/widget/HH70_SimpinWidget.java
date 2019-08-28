package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootUtils;

/**
 * Created by wzhiqiang on 2019/8/20
 */
public class HH70_SimpinWidget extends RelativeLayout {

    private ImageView ivWidgetBg;
    private TextView tvWidgetCancel;
    private TextView tvWidgetOk;
    private EditText etSimPin;
    
    public HH70_SimpinWidget(Context context) {
        this(context,null,0);
    }

    public HH70_SimpinWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HH70_SimpinWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_simpin, this);
        ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> bgClickNext());
        etSimPin = findViewById(R.id.et_pop_simpin);
        tvWidgetCancel = findViewById(R.id.tv_dialogok_widget_cancel);
        tvWidgetCancel.setOnClickListener(v -> {
            setVisibility(GONE);
            cancelClickNext();
        });
        tvWidgetOk = findViewById(R.id.tv_dialogok_widget_ok);
        tvWidgetOk.setOnClickListener(v -> {
            setVisibility(GONE);
            okClickNext();
        });
    }

    /**
     * 获取editText的内容
     * @return
     */
    public String getEtString() {
        return RootUtils.getEDText(etSimPin);
    }

    /**
     * 设置取消按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setCancelVisible(boolean isVisible) {
        tvWidgetCancel.setVisibility(isVisible ? VISIBLE : GONE);
    }

    /**
     * 设置确定按钮是否可显
     *
     * @param isVisible true:可显
     */
    public void setOkVisible(boolean isVisible) {
        tvWidgetOk.setVisibility(isVisible ? VISIBLE : GONE);
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
