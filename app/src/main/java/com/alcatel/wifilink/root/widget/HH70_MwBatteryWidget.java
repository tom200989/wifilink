package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/6/4 0004.
 */

public class HH70_MwBatteryWidget extends RelativeLayout {

    private ProgressBar pgBattery;// 电池进度
    private TextView tvBatteryPercent;// 电池百分比
    private ImageView ivSignal;// 信号强度
    private TextView tvNetwork;// 网络类型
    private ImageView ivWifiStrength;// wifi强度
    private TextView tvWifiName;// wifi名称
    private Drawable batFlash;
    private Drawable batUse;
    private Drawable batUseLow20;
    private List<Drawable> signals;// 信号强度集合
    private List<String> networks;// 信号类型
    private List<Drawable> wifiStrength;// wifi强度集合

    public HH70_MwBatteryWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_MwBatteryWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_MwBatteryWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_mw_battery, this);
        pgBattery = findViewById(R.id.pg_main_mw70_battery);
        tvBatteryPercent = findViewById(R.id.tv_main_mw70_battery_percent);
        ivSignal = findViewById(R.id.iv_main_mw70_signal);
        tvNetwork = findViewById(R.id.tv_main_mw70_network);
        ivWifiStrength = findViewById(R.id.iv_main_mw70_extender_wifi);
        tvWifiName = findViewById(R.id.tv_main_mw70_extender_phoneName);
        initRes(context);
    }

    /**
     * 初始化资源以及初始Ui
     */
    private void initRes(Context context) {
        batFlash = getResources().getDrawable(R.drawable.battery_01_flash);
        batUse = getResources().getDrawable(R.drawable.battery_01);
        batUseLow20 = getResources().getDrawable(R.drawable.battery_01_low20);

        signals = new ArrayList<>();
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_0));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_1));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_2));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_3));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_4));
        signals.add(context.getResources().getDrawable(R.drawable.mw_signal_4));

        networks = new ArrayList<>();
        networks.add(context.getString(R.string.hh70_no_service));
        networks.add(context.getString(R.string.hh70_2g));
        networks.add(context.getString(R.string.hh70_3g_plus));
        networks.add(context.getString(R.string.hh70_4g));

        wifiStrength = new ArrayList<>();
        wifiStrength.add(context.getResources().getDrawable(R.drawable.main_wifi_ex_signal0));
        wifiStrength.add(context.getResources().getDrawable(R.drawable.main_wifi_ex_signal1));
        wifiStrength.add(context.getResources().getDrawable(R.drawable.main_wifi_ex_signal2));
        wifiStrength.add(context.getResources().getDrawable(R.drawable.main_wifi_ex_signal3));
        wifiStrength.add(context.getResources().getDrawable(R.drawable.main_wifi_ex_signal4));

        pgBattery.setProgressDrawable(batUse);
        pgBattery.setProgress(12);
        ivWifiStrength.setVisibility(GONE);
        tvWifiName.setVisibility(GONE);
    }

    /**
     * 设置电池状态
     *
     * @param isCharing 是否在充电
     * @param cap       电量(0 ~ 100)
     */
    public void setBattery(boolean isCharing, int cap) {
        pgBattery.setProgress(isCharing ? 0 : cap <= 12 ? 12 : cap);
        pgBattery.setProgressDrawable(isCharing ? batFlash : cap <= 20 ? batUseLow20 : batUse);
        tvBatteryPercent.setText(String.valueOf(cap + "%"));
    }

    /**
     * 设置信号强度
     *
     * @param strength 信号强度(0 ~ 5)
     */
    public void setSignal(int strength) {
        ivSignal.setImageDrawable(signals.get(strength <= 0 ? 0 : strength));
    }

    /**
     * 设置网络类型
     *
     * @param level 类型(0 ~ 12)
     */
    public void setNetworkText(int level) {
        if (level == 0) {
            tvNetwork.setText(networks.get(0));// no service
        } else if (level >= 1 & level <= 2) {
            tvNetwork.setText(networks.get(1));// 2G
        } else if (level >= 3 & level <= 7) {
            tvNetwork.setText(networks.get(2));// 3G
        } else if (level >= 8 & level <= 9) {
            tvNetwork.setText(networks.get(3));// 4G
        } else if (level >= 10 & level <= 12) {
            tvNetwork.setText(networks.get(1));// 2G
        }
    }

    /**
     * wifi extender状态是否开启
     */
    public void setWifiExtenderState(boolean isOpen) {
        ivSignal.setVisibility(isOpen ? GONE : VISIBLE);
        tvNetwork.setVisibility(isOpen ? GONE : VISIBLE);
        ivWifiStrength.setVisibility(isOpen ? VISIBLE : GONE);
        tvWifiName.setVisibility(isOpen ? VISIBLE : GONE);
    }

    /**
     * 设置wifi强度
     *
     * @param strength wifi 强度(0 ~ 4)
     */
    public void setWifiStrength(int strength) {
        ivWifiStrength.setImageDrawable(wifiStrength.get(strength));
    }

    /**
     * 设置wifi名称
     */
    public void setWifiName(String wifiName) {
        tvWifiName.setText(wifiName);
    }

}
