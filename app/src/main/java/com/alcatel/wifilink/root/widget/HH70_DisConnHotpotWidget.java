package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/5/29 0029.
 */

public class HH70_DisConnHotpotWidget extends RelativeLayout {

    private ImageView ivBg;
    private TextView tvCancel;
    private TextView tvOk;

    public HH70_DisConnHotpotWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_DisConnHotpotWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_DisConnHotpotWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_extend_disconn_hotpot, this);
        ivBg =  findViewById(R.id.iv_extender_disconn_hotpot_bg);
        tvCancel =  findViewById(R.id.tv_extender_disconn_hotpot_cancel);
        tvOk =  findViewById(R.id.tv_extender_disconn_hotpot_ok);
        ivBg.setOnClickListener(v -> setVisibility(GONE));
        tvCancel.setOnClickListener(v -> setVisibility(GONE));
        tvOk.setOnClickListener(v -> {
            clickNext(null);
            setVisibility(GONE);
        });
    }

    private OnClickOkListener onClickOkListener;

    // 接口OnClickOkListener
    public interface OnClickOkListener {
        void click(Object attr);
    }

    // 对外方式setOnClickOkListener
    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    // 封装方法clickNext
    private void clickNext(Object attr) {
        if (onClickOkListener != null) {
            onClickOkListener.click(attr);
        }
    }
}
