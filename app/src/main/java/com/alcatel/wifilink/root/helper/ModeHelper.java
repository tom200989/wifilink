package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetNetworkSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkSettingsBeanHelper;
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
        GetNetworkSettingsBeanHelper xGetNetworkSettingHelper = new GetNetworkSettingsBeanHelper();
        xGetNetworkSettingHelper.setOnGetNetworkSettingsFailedListener(() ->toast(R.string.hh70_cant_connect) );
        xGetNetworkSettingHelper.setOnGetNetworkSettingsSuccessListener(getNetworkSettingsBean -> {
            // 3.提交请求
            SetNetworkSettingsParam param = new SetNetworkSettingsParam();
            param.setDomesticRoam(getNetworkSettingsBean.getDomesticRoam());
            param.setDomesticRoamGuard(getNetworkSettingsBean.getDomesticRoamGuard());
            param.setNetselectionMode(getNetworkSettingsBean.getNetselectionMode());
            param.setNetworkBand(getNetworkSettingsBean.getNetworkBand());
            param.setNetworkMode(mode);
            SetNetworkSettingsHelper xSetNetworkSettingsHelper = new SetNetworkSettingsHelper();
            xSetNetworkSettingsHelper.setOnSetNetworkSettingsSuccessListener(() -> {
                // 4.再次获取
                GetNetworkSettingsBeanHelper xGetNetSettingHelper = new GetNetworkSettingsBeanHelper();
                xGetNetSettingHelper.setOnGetNetworkSettingsSuccessListener(attr -> modeSuccessNext(attr));
                xGetNetSettingHelper.getNetworkSettings();
            });
            xSetNetworkSettingsHelper.setOnSetNetworkSettingsFailedListener(() -> toast(R.string.hh70_cant_connect));
            xSetNetworkSettingsHelper.setNetworkSettings(param);
        });
        xGetNetworkSettingHelper.getNetworkSettings();
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
        ToastTool.show(activity, resId);
    }


}
