package com.alcatel.wifilink.root.widget;

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
        ivWidgetBg.setOnClickListener(v -> bgClickNext());
        TextView tvAuto = findViewById(R.id.tv_pop_connMode_auto);
        TextView tv4g = findViewById(R.id.tv_pop_connMode_4g);
        TextView tv3g = findViewById(R.id.tv_pop_connMode_3g);
        TextView tv2g = findViewById(R.id.tv_pop_connMode_2g);
        tvAuto.setOnClickListener(v -> {
            setVisibility(GONE);
            autoClickNext();
        });
        tv4g.setOnClickListener(v -> {
            setVisibility(GONE);
            fourModeNext();
        });
        tv3g.setOnClickListener(v -> {
            setVisibility(GONE);
            thirdModeNext();
        });
        tv2g.setOnClickListener(v -> {
            setVisibility(GONE);
            secondModeNext();
        });

    }


    /* -------------------------------------------- impl -------------------------------------------- */
    private NormalWidget.OnBgClickListener onBgClickListener;

    // Inteerface--> 接口OnBgClickListener
    public interface OnBgClickListener {
        void bgClick();
    }

    // 对外方式setOnBgClickListener
    public void setOnBgClickListener(NormalWidget.OnBgClickListener onBgClickListener) {
        this.onBgClickListener = onBgClickListener;
    }

    // 封装方法bgClickNext
    private void bgClickNext() {
        if (onBgClickListener != null) {
            onBgClickListener.bgClick();
        }
    }

    public interface OnAutoClickListener {
        void autoClick();
    }

    private HH70_ConmodeWidget.OnAutoClickListener onAutoClickListener;

    //对外方式setOnAutoClickListener
    public void setOnAutoClickListener(HH70_ConmodeWidget.OnAutoClickListener onAutoClickListener) {
        this.onAutoClickListener = onAutoClickListener;
    }

    //封装方法autoClickNext
    private void autoClickNext() {
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
        if (on3gModeClickListener != null) {
            on3gModeClickListener.click();
        }
    }

    public interface On2gModeClickListener {
        void click();
    }

    private On2gModeClickListener on2gModeClickListener;

    //对外方式setOn2gModeClickListener
    public void setOn2gModeClickListener(On2gModeClickListener on2gModeClickListener) {
        this.on2gModeClickListener = on2gModeClickListener;
    }

    //封装方法secondModeNext
    private void secondModeNext() {
        if (on2gModeClickListener != null) {
            on2gModeClickListener.click();
        }
    }

}
