package com.alcatel.wifilink.helper;

import android.app.Activity;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.ToastTool;
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
        PrepareNext();
        // 1.现获取当前的Mode对象
        GetNetworkSettingsBeanHelper xGetNetworkSettingHelper = new GetNetworkSettingsBeanHelper();
        xGetNetworkSettingHelper.setOnGetNetworkSettingsFailedListener(() -> {
            toast(R.string.hh70_cant_connect);
            DoneNext();
        });
        xGetNetworkSettingHelper.setOnGetNetworkSettingsSuccessListener(getNetworkSettingsBean -> {
            // 3.封装数据
            SetNetworkSettingsParam param = new SetNetworkSettingsParam();
            param.setDomesticRoam(getNetworkSettingsBean.getDomesticRoam());
            param.setDomesticRoamGuard(getNetworkSettingsBean.getDomesticRoamGuard());
            param.setNetselectionMode(getNetworkSettingsBean.getNetselectionMode());
            param.setNetworkBand(getNetworkSettingsBean.getNetworkBand());
            param.setNetworkMode(mode);
            // 3.1.提交请求
            SetNetworkSettingsHelper xSetNetworkSettingsHelper = new SetNetworkSettingsHelper();
            xSetNetworkSettingsHelper.setOnSetNetworkSettingsSuccessListener(() -> {
                // 4.再次获取
                GetNetworkSettingsBeanHelper xGetNetSettingHelper = new GetNetworkSettingsBeanHelper();
                xGetNetSettingHelper.setOnGetNetworkSettingsSuccessListener(this::modeSuccessNext);
                xGetNetSettingHelper.setOnDoneHelperListener(this::DoneNext);
                xGetNetSettingHelper.getNetworkSettings();
            });
            xSetNetworkSettingsHelper.setOnSetNetworkSettingsFailedListener(() -> {
                toast(R.string.hh70_cant_connect);
                DoneNext();
            });
            xSetNetworkSettingsHelper.setNetworkSettings(param);
        });
        xGetNetworkSettingHelper.getNetworkSettings();
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    // ---------------- 监听器 [Prepare] ----------------
    private OnPrepareListener onPrepareListener;

    // Interface--> 接口: OnPrepareListener
    public interface OnPrepareListener {
        void Prepare();
    }

    // 对外方式: setOnPrepareListener
    public void setOnPrepareListener(OnPrepareListener onPrepareListener) {
        this.onPrepareListener = onPrepareListener;
    }

    // 封装方法: PrepareNext
    private void PrepareNext() {
        if (onPrepareListener != null) {
            onPrepareListener.Prepare();
        }
    }

    // ---------------- 监听器 [Done] ----------------
    private OnDoneListener onDoneListener;

    // Interface--> 接口: OnDoneListener
    public interface OnDoneListener {
        void Done();
    }

    // 对外方式: setOnDoneListener
    public void setOnDoneListener(OnDoneListener onDoneListener) {
        this.onDoneListener = onDoneListener;
    }

    // 封装方法: DoneNext
    private void DoneNext() {
        if (onDoneListener != null) {
            onDoneListener.Done();
        }
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
