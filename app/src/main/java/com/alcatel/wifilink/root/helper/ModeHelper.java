package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetNetworkSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetNetworkSettingsHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class ModeHelper {
    private Activity activity;

    public ModeHelper(Activity activity) {
        this.activity = activity;
    }

    public void transfer(int mode) {
        // 1.现获取当前的Mode对象
        NetworkSettingHelper nshelper = new NetworkSettingHelper();
        nshelper.setOnNormalNetworkListener(attr -> {
            // 3.提交请求
            SetNetworkSettingsParam param = new SetNetworkSettingsParam();
            param.setDomesticRoam(attr.getDomesticRoam());
            param.setDomesticRoamGuard(attr.getDomesticRoamGuard());
            param.setNetselectionMode(attr.getNetselectionMode());
            param.setNetworkBand(attr.getNetworkBand());
            param.setNetworkMode(mode);
            SetNetworkSettingsHelper xSetNetworkSettingsHelper = new SetNetworkSettingsHelper();
            xSetNetworkSettingsHelper.setOnSetNetworkSettingsSuccessListener(() -> {
                // 4.再次获取
                NetworkSettingHelper networkSettingHelper = new NetworkSettingHelper();
                networkSettingHelper.setOnNormalNetworkListener(this::modeSuccessNext);
                networkSettingHelper.getNetworkSetting();
            });
            xSetNetworkSettingsHelper.setOnSetNetworkSettingsFailedListener(() -> toast(R.string.connect_failed));
            xSetNetworkSettingsHelper.setNetworkSettings(param);
        });
        nshelper.getNetworkSetting();
    }

    private OnModeSuccessListener onModeSuccessListener;

    // 接口OnModeSuccessListener
    public interface OnModeSuccessListener {
        void modeSuccess(GetNetworkSettingsBean attr);
    }

    // 对外方式setOnModeSuccessListener
    public void setOnModeSuccessListener(OnModeSuccessListener onModeSuccessListener) {
        this.onModeSuccessListener = onModeSuccessListener;
    }

    // 封装方法modeSuccessNext
    private void modeSuccessNext(GetNetworkSettingsBean attr) {
        if (onModeSuccessListener != null) {
            onModeSuccessListener.modeSuccess(attr);
        }
    }

    public void toast(int resId) {
        ToastUtil_m.show(activity, resId);
    }


}
