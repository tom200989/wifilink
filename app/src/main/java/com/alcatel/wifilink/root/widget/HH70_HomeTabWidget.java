package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/17 0017.
 */
public class HH70_HomeTabWidget extends RelativeLayout {

    LinearLayout llHome;// HOME
    ImageView ivTabHome;
    LinearLayout llWifi;// WIFI
    ImageView ivTabWifi;
    RelativeLayout llSms;// SMS
    ImageView ivTabSms;
    TextView tvSmsDot;
    LinearLayout llSetting;// SETTING
    ImageView ivTabSetting;

    private View inflate;
    private ImageView[] ivTabs;
    private Drawable home_logo_pre;
    private Drawable home_logo_nor;
    private Drawable wifi_logo_pre;
    private Drawable wifi_logo_nor;
    private Drawable sms_logo_pre;
    private Drawable sms_logo_nor;
    private Drawable setting_logo_pre;
    private Drawable setting_logo_nor;
    private Drawable[] logo_pres;
    private Drawable[] logo_nors;

    public HH70_HomeTabWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_HomeTabWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_HomeTabWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        home_logo_pre = getResources().getDrawable(R.drawable.tab_home_pre);
        home_logo_nor = getResources().getDrawable(R.drawable.tab_home_nor);
        wifi_logo_pre = getResources().getDrawable(R.drawable.wifi_ex);
        wifi_logo_nor = getResources().getDrawable(R.drawable.wifi_ex_signal0);
        sms_logo_pre = getResources().getDrawable(R.drawable.tab_sms_pre);
        sms_logo_nor = getResources().getDrawable(R.drawable.tab_sms_nor);
        setting_logo_pre = getResources().getDrawable(R.drawable.tab_settings_pre);
        setting_logo_nor = getResources().getDrawable(R.drawable.tab_settings_nor);
        logo_pres = new Drawable[]{home_logo_pre, wifi_logo_pre, sms_logo_pre, setting_logo_pre};// 资源1
        logo_nors = new Drawable[]{home_logo_nor, wifi_logo_nor, sms_logo_nor, setting_logo_nor};// 资源2

        inflate = inflate(context, R.layout.hh70_widget_home_tab, this);
        llHome = inflate.findViewById(R.id.ll_homeRx_home);
        ivTabHome = inflate.findViewById(R.id.iv_homeRx_tab_home);
        llWifi = inflate.findViewById(R.id.ll_homeRx_wifi);
        ivTabWifi = inflate.findViewById(R.id.iv_homeRx_tab_wifi);
        llSms = inflate.findViewById(R.id.ll_homeRx_sms);
        ivTabSms = inflate.findViewById(R.id.iv_homeRx_tab_sms);
        tvSmsDot = inflate.findViewById(R.id.tv_homeRx_smsDot);
        llSetting = inflate.findViewById(R.id.ll_homeRx_setting);
        ivTabSetting = inflate.findViewById(R.id.iv_homeRx_tab_setting);
        ivTabs = new ImageView[]{ivTabHome, ivTabWifi, ivTabSms, ivTabSetting};// 控件

        llHome.setOnClickListener(v -> clicktab(0));
        llWifi.setOnClickListener(v -> clicktab(1));
        llSms.setOnClickListener(v -> clicktab(2));
        llSetting.setOnClickListener(v -> clicktab(3));
    }

    /**
     * 点击某个TAB的操作
     *
     * @param position 位标
     */
    private void clicktab(int position) {
        setCheck(position);
        clickTabNext(position);
    }

    /**
     * 设置某个TAB选中
     *
     * @param position 选中的位置
     */
    public void setCheck(int position) {
        position = position < 0 ? 0 : position > ivTabs.length - 1 ? ivTabs.length - 1 : position;
        for (int i = 0; i < ivTabs.length; i++) {
            ivTabs[i].setImageDrawable(i == position ? logo_pres[i] : logo_nors[i]);
        }
    }

    /**
     * 显示或隐藏未读小点
     *
     * @param isVisible T:显示
     */
    public void setSMSDot(boolean isVisible) {
        tvSmsDot.setVisibility(isVisible ? VISIBLE : GONE);
    }

    private OnClickTabListener onClickTabListener;

    // Inteerface--> 接口OnClickTabListener
    public interface OnClickTabListener {
        void clickTab(int position);
    }

    // 对外方式setOnClickTabListener
    public void setOnClickTabListener(OnClickTabListener onClickTabListener) {
        this.onClickTabListener = onClickTabListener;
    }

    // 封装方法clickTabNext
    private void clickTabNext(int position) {
        if (onClickTabListener != null) {
            onClickTabListener.clickTab(position);
        }
    }
}
