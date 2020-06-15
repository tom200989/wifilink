package com.alcatel.wifilink.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.RootCons;
import com.alcatel.wifilink.utils.RootUtils;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;

import java.util.Arrays;
import java.util.List;

/**
 * Created by wzhiqiang on 2019/8/20
 */
public class HH70_ModeWidget extends RelativeLayout {

    private ImageView ivWidgetBg;
    private TextView tvAuto;
    private TextView tv4g;
    private TextView tv3g;
    private TextView tv2g;

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
        tvAuto = findViewById(R.id.tv_pop_connMode_auto);
        tv4g = findViewById(R.id.tv_pop_connMode_4g);
        tv3g = findViewById(R.id.tv_pop_connMode_3g);
        tv2g = findViewById(R.id.tv_pop_connMode_2g);

        // 针对HH42(外包)所做的字符串适配
        if (RootUtils.isHH42(ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT))) {
            tvAuto.setText(R.string.hh70_auto_42);
            tv4g.setVisibility(GONE);
            tv2g.setVisibility(GONE);
        }

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

    /**
     * 设置不支持的模式, 则隐藏对应UI - 针对某些设备做的兼容(HH42)
     *
     * @param modes 不支持的模式
     */
    public void notSupportMode(Integer... modes) {
        List<Integer> mols = Arrays.asList(modes);
        if (mols.contains(GetNetworkSettingsBean.CONS_AUTO_MODE)) {
            tvAuto.setVisibility(GONE);
        }

        if (mols.contains(GetNetworkSettingsBean.CONS_ONLY_LTE)) {
            tv4g.setVisibility(GONE);
        }

        if (mols.contains(GetNetworkSettingsBean.CONS_ONLY_3G)) {
            tv3g.setVisibility(GONE);
        }

        if (mols.contains(GetNetworkSettingsBean.CONS_ONLY_2G)) {
            tv2g.setVisibility(GONE);
        }
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
