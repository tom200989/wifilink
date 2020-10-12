package com.alcatel.wifilink.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;

/**
 * Created by wzhiqiang on 2019/8/20
 */
public class HH42_ModeWidget extends RelativeLayout {// TOAT: 适配HH42

    private ImageView ivWidgetBg;
    private TextView tvAuto4G;
    private TextView tvAuto3G;
    private TextView tv4g;
    private TextView tv3g;
    private TextView tv2g;

    public HH42_ModeWidget(Context context) {
        this(context, null, 0);
    }

    public HH42_ModeWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH42_ModeWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        View.inflate(context, R.layout.hh42_widget_mode, this);
        // 背景
        ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg_42);
        ivWidgetBg.setOnClickListener(v -> setVisibility(GONE));
        // 条目
        tvAuto4G = findViewById(R.id.tv_pop_connMode_auto_42_4g);
        tvAuto3G = findViewById(R.id.tv_pop_connMode_auto_42_3g);
        tv4g = findViewById(R.id.tv_pop_connMode_4g_42);
        tv3g = findViewById(R.id.tv_pop_connMode_3g_42);
        tv2g = findViewById(R.id.tv_pop_connMode_2g_42);
        // 点击事件
        tvAuto4G.setOnClickListener(v -> ClickAuto4GFor42Next());
        tvAuto3G.setOnClickListener(v -> ClickAuto3GFor42Next());
        tv4g.setOnClickListener(v -> ClickOnly4GFor42Next());
        tv3g.setOnClickListener(v -> ClickOnly3GFor42Next());
        tv2g.setOnClickListener(v -> ClickOnly2GFor42Next());
    }

    /**
     * 根据networkmodeMask进行选择
     *
     * @param networkmodeMask 网络可选模式
     */
    public void setEnableNetmodeByMask(int[] networkmodeMask) {

        // 先隐藏
        tvAuto4G.setVisibility(GONE);
        tvAuto3G.setVisibility(GONE);
        tv4g.setVisibility(GONE);
        tv3g.setVisibility(GONE);
        tv2g.setVisibility(GONE);

        // 根据对应mask显示
        for (int mask : networkmodeMask) {

            // 显示［自动］
            if (mask == GetNetworkSettingsBean.CONS_AUTO_ENABLE) {
                tvAuto4G.setVisibility(VISIBLE);
                tvAuto3G.setVisibility(VISIBLE);
            }

            // 显示［4G only］
            if (mask == GetNetworkSettingsBean.CONS_4G_ONLY_ENABLE) {
                tv4g.setVisibility(VISIBLE);
            }

            // 显示［3G only］
            if (mask == GetNetworkSettingsBean.CONS_3G_ONLY_ENABLE) {
                tv3g.setVisibility(VISIBLE);
            }

            // 显示［2G only］
            if (mask == GetNetworkSettingsBean.CONS_2G_ONLY_ENABLE) {
                tv2g.setVisibility(VISIBLE);
            }
        }
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    // ---------------- 监听器 [ClickAuto4GFor42] ----------------
    private OnClickAuto4GFor42Listener onClickAuto4GFor42Listener;

    // Interface--> 接口: OnClickAuto4GFor42Listener
    public interface OnClickAuto4GFor42Listener {
        void ClickAuto4GFor42();
    }

    // 对外方式: setOnClickAuto4GFor42Listener
    public void setOnClickAuto4GFor42Listener(OnClickAuto4GFor42Listener onClickAuto4GFor42Listener) {
        this.onClickAuto4GFor42Listener = onClickAuto4GFor42Listener;
    }

    // 封装方法: ClickAuto4GFor42Next
    private void ClickAuto4GFor42Next() {
        setVisibility(GONE);
        if (onClickAuto4GFor42Listener != null) {
            onClickAuto4GFor42Listener.ClickAuto4GFor42();
        }
    }

    // ---------------- 监听器 [ClickAuto3GFor42] ----------------
    private OnClickAuto3GFor42Listener onClickAuto3GFor42Listener;

    // Interface--> 接口: OnClickAuto3GFor42Listener
    public interface OnClickAuto3GFor42Listener {
        void ClickAuto3GFor42();
    }

    // 对外方式: setOnClickAuto3GFor42Listener
    public void setOnClickAuto3GFor42Listener(OnClickAuto3GFor42Listener onClickAuto3GFor42Listener) {
        this.onClickAuto3GFor42Listener = onClickAuto3GFor42Listener;
    }

    // 封装方法: ClickAuto3GFor42Next
    private void ClickAuto3GFor42Next() {
        setVisibility(GONE);
        if (onClickAuto3GFor42Listener != null) {
            onClickAuto3GFor42Listener.ClickAuto3GFor42();
        }
    }

    // ---------------- 监听器 [ClickOnly4GFor42] ----------------
    private OnClickOnly4GFor42Listener onClickOnly4GFor42Listener;

    // Interface--> 接口: OnClickOnly4GFor42Listener
    public interface OnClickOnly4GFor42Listener {
        void ClickOnly4GFor42();
    }

    // 对外方式: setOnClickOnly4GFor42Listener
    public void setOnClickOnly4GFor42Listener(OnClickOnly4GFor42Listener onClickOnly4GFor42Listener) {
        this.onClickOnly4GFor42Listener = onClickOnly4GFor42Listener;
    }

    // 封装方法: ClickOnly4GFor42Next
    private void ClickOnly4GFor42Next() {
        setVisibility(GONE);
        if (onClickOnly4GFor42Listener != null) {
            onClickOnly4GFor42Listener.ClickOnly4GFor42();
        }
    }

    // ---------------- 监听器 [ClickOnly3GFor42] ----------------
    private OnClickOnly3GFor42Listener onClickOnly3GFor42Listener;

    // Interface--> 接口: OnClickOnly3GFor42Listener
    public interface OnClickOnly3GFor42Listener {
        void ClickOnly3GFor42();
    }

    // 对外方式: setOnClickOnly3GFor42Listener
    public void setOnClickOnly3GFor42Listener(OnClickOnly3GFor42Listener onClickOnly3GFor42Listener) {
        this.onClickOnly3GFor42Listener = onClickOnly3GFor42Listener;
    }

    // 封装方法: ClickOnly3GFor42Next
    private void ClickOnly3GFor42Next() {
        setVisibility(GONE);
        if (onClickOnly3GFor42Listener != null) {
            onClickOnly3GFor42Listener.ClickOnly3GFor42();
        }
    }

    // ---------------- 监听器 [ClickOnly2GFor42] ----------------
    private OnClickOnly2GFor42Listener onClickOnly2GFor42Listener;

    // Interface--> 接口: OnClickOnly2GFor42Listener
    public interface OnClickOnly2GFor42Listener {
        void ClickOnly2GFor42();
    }

    // 对外方式: setOnClickOnly2GFor42Listener
    public void setOnClickOnly2GFor42Listener(OnClickOnly2GFor42Listener onClickOnly2GFor42Listener) {
        this.onClickOnly2GFor42Listener = onClickOnly2GFor42Listener;
    }

    // 封装方法: ClickOnly2GFor42Next
    private void ClickOnly2GFor42Next() {
        setVisibility(GONE);
        if (onClickOnly2GFor42Listener != null) {
            onClickOnly2GFor42Listener.ClickOnly2GFor42();
        }
    }

}
