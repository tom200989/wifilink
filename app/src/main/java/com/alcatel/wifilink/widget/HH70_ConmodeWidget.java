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
public class HH70_ConmodeWidget extends RelativeLayout {

    private ImageView ivWidgetBg;

    public HH70_ConmodeWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_ConmodeWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_ConmodeWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.hh70_widget_connmode, this);
        ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> bgClickNext());
        TextView tvAuto = findViewById(R.id.tv_pop_connMode_auto);
        TextView tvManual = findViewById(R.id.tv_pop_connMode_manual);
        tvAuto.setOnClickListener(v -> {
            setVisibility(GONE);
            autoClickNext();
        });
        tvManual.setOnClickListener(v -> {
            setVisibility(GONE);
            manualClickNext();
        });
    }


    /* -------------------------------------------- impl -------------------------------------------- */
    private HH70_NormalWidget.OnBgClickListener onBgClickListener;

    // Inteerface--> 接口OnBgClickListener
    public interface OnBgClickListener {
        void bgClick();
    }

    // 对外方式setOnBgClickListener
    public void setOnBgClickListener(HH70_NormalWidget.OnBgClickListener onBgClickListener) {
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

    private OnAutoClickListener onAutoClickListener;

    //对外方式setOnAutoClickListener
    public void setOnAutoClickListener(OnAutoClickListener onAutoClickListener) {
        this.onAutoClickListener = onAutoClickListener;
    }

    //封装方法autoClickNext
    private void autoClickNext() {
        if (onAutoClickListener != null) {
            onAutoClickListener.autoClick();
        }
    }

    public interface OnManualClickListener {
        void manualClick();
    }

    private OnManualClickListener onManualClickListener;

    //对外方式setOnManualClickListener
    public void setOnManualClickListener(OnManualClickListener onManualClickListener) {
        this.onManualClickListener = onManualClickListener;
    }

    //封装方法manualClickNext
    private void manualClickNext() {
        if (onManualClickListener != null) {
            onManualClickListener.manualClick();
        }
    }
}
