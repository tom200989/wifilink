package com.alcatel.wifilink.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by wzhiqiang on 2019/8/20
 */
public class HH70_ModeWidget extends RelativeLayout {

    private ImageView ivWidgetBg;
    private TextView tvAuto;
    private TextView tv4g;
    private TextView tv3g;

    public HH70_ModeWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_ModeWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_ModeWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.hh70_widget_mode, this);
        ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> setVisibility(GONE));
        tvAuto = findViewById(R.id.tv_pop_connMode_auto);
        tv4g = findViewById(R.id.tv_pop_connMode_4g);
        tv3g = findViewById(R.id.tv_pop_connMode_3g);

        tvAuto.setOnClickListener(v -> autoClickNext());
        tv4g.setOnClickListener(v -> fourModeNext());
        tv3g.setOnClickListener(v -> thirdModeNext());
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    public interface OnAutoClickListener {
        void autoClick();
    }

    private OnAutoClickListener onAutoClickListener;

    //对外方式setOnAutoClickListener
    public void setOnAutoClickListener(OnAutoClickListener onAutoClickListener) {
        this.onAutoClickListener = onAutoClickListener;
    }

    //封装方法autoClickNext
    private void autoClickNext() {
        setVisibility(GONE);
        if (onAutoClickListener != null) {
            onAutoClickListener.autoClick();
        }
    }

    public interface On4gModeClickListener {
        void click();
    }

    private On4gModeClickListener on4gModeClickListener;

    //对外方式setOn4gModeClickListener
    public void setOn4gModeClickListener(On4gModeClickListener on4gModeClickListener) {
        this.on4gModeClickListener = on4gModeClickListener;
    }

    //封装方法fourModeNext
    private void fourModeNext() {
        setVisibility(GONE);
        if (on4gModeClickListener != null) {
            on4gModeClickListener.click();
        }
    }

    public interface On3gModeClickListener {
        void click();
    }

    private On3gModeClickListener on3gModeClickListener;

    //对外方式setOn3gModeClickListener
    public void setOn3gModeClickListener(On3gModeClickListener on3gModeClickListener) {
        this.on3gModeClickListener = on3gModeClickListener;
    }

    //封装方法thirdModeNext
    private void thirdModeNext() {
        setVisibility(GONE);
        if (on3gModeClickListener != null) {
            on3gModeClickListener.click();
        }
    }
}
