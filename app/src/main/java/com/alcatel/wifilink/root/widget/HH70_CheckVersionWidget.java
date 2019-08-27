package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_CheckVersionWidget extends RelativeLayout {

    private View inflate;
    private TextView versionName;
    private TextView ok;
    private TextView tip;

    public HH70_CheckVersionWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_CheckVersionWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_CheckVersionWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_checkversion, this);
        inflate.findViewById(R.id.iv_upgrade_checkVersion).setOnClickListener(null);
        versionName = inflate.findViewById(R.id.tv_pop_setting_rx_upgrade_noNewVersion_version);
        ok = inflate.findViewById(R.id.tv_pop_setting_rx_upgrade_ok);
        tip = inflate.findViewById(R.id.tv_pop_setting_rx_upgrade_noNewVersion_tip);
        ok.setOnClickListener(v -> {
            setVisibility(GONE);
            clickOKNext();
        });
    }

    public void setVersionName(String text) {
        versionName.setText(text);
    }

    public void setTipVisible(boolean isVisible) {
        tip.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    public void setOkText(String text) {
        ok.setText(text);
    }

    private OnClickOKListener onClickOKListener;

    // Inteerface--> 接口OnClickOKListener
    public interface OnClickOKListener {
        void clickOK();
    }

    // 对外方式setOnClickOKListener
    public void setOnClickOKListener(OnClickOKListener onClickOKListener) {
        this.onClickOKListener = onClickOKListener;
    }

    // 封装方法clickOKNext
    private void clickOKNext() {
        if (onClickOKListener != null) {
            onClickOKListener.clickOK();
        }
    }


}
