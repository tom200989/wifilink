package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.p_numberbar.p_numberbar.core.NumberProgressBar;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_DownWidget extends RelativeLayout {

    private View inflate;
    private TextView per;
    private NumberProgressBar progressBar;
    private TextView cancel;

    public HH70_DownWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_DownWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_DownWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_down, this);
        inflate.findViewById(R.id.iv_down_bg).setOnClickListener(null);
        per = inflate.findViewById(R.id.tv_pop_setting_download_per);
        progressBar = inflate.findViewById(R.id.pg_pop_setting_download);
        cancel = inflate.findViewById(R.id.tv_pop_setting_download_cancel);
        cancel.setOnClickListener(v1 -> {
            setVisibility(GONE);
            clickCancelNext();
        });
    }

    public TextView getPercent() {
        return per;
    }

    public NumberProgressBar getProgressbar( ) {
        return progressBar;
    }

    private OnClickCancelListener onClickCancelListener;

    // Inteerface--> 接口OnClickCancelListener
    public interface OnClickCancelListener {
        void clickCancel();
    }

    // 对外方式setOnClickCancelListener
    public void setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
    }

    // 封装方法clickCancelNext
    private void clickCancelNext() {
        if (onClickCancelListener != null) {
            onClickCancelListener.clickCancel();
        }
    }
}
