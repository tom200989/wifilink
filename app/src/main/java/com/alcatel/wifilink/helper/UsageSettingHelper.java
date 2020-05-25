package com.alcatel.wifilink.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetUsageSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageSettingsHelper;

/**
 * Created by qianli.ma on 2017/12/13 0013.
 */

public class UsageSettingHelper {

    /**
     * 获取流量设置
     */
    public void getUsageSetting() {
        GetUsageSettingsHelper xGetUsageSettingsHelper = new GetUsageSettingsHelper();
        xGetUsageSettingsHelper.setOnGetUSageSettingsSuccessListener(this::GetUsageSettingsSuccessNext);
        xGetUsageSettingsHelper.setOnGetUsageSettingsFailListener(this::GetUsageSettingsFailedNext);
        xGetUsageSettingsHelper.getUsageSetting();
    }

    /**
     * 提交流量设置
     */
    public void setUsageSetting(GetUsageSettingsBean getUsageSettingsBean) {
        SetUsageSettingsParam param = new SetUsageSettingsParam();
        param.copy(getUsageSettingsBean);
        SetUsageSettingsHelper xSetUsageSettingsHelper = new SetUsageSettingsHelper();
        xSetUsageSettingsHelper.setOnSetUsageSettingsSuccessListener(this::setUsageSettingSuccessNext);
        xSetUsageSettingsHelper.setOnSetUsageSettingsFailListener(this::setUsageSettingFailedNext);
        xSetUsageSettingsHelper.setUsageSettings(param);
    }

    private OnGetUsageSettingsSuccessListener onGetUsageSettingsSuccessListener;

    // Inteerface--> 接口OnGetUsageSettingsSuccessListener
    public interface OnGetUsageSettingsSuccessListener {
        void GetUsageSettingsSuccess(GetUsageSettingsBean getUsageSettingsBean);
    }

    // 对外方式setOnGetUsageSettingsSuccessListener
    public void setOnGetUsageSettingsSuccessListener(OnGetUsageSettingsSuccessListener onGetUsageSettingsSuccessListener) {
        this.onGetUsageSettingsSuccessListener = onGetUsageSettingsSuccessListener;
    }

    // 封装方法GetUsageSettingsSuccessNext
    private void GetUsageSettingsSuccessNext(GetUsageSettingsBean getUsageSettingsBean) {
        if (onGetUsageSettingsSuccessListener != null) {
            onGetUsageSettingsSuccessListener.GetUsageSettingsSuccess(getUsageSettingsBean);
        }
    }

    private OnGetUsageSettingsFailedListener onGetUsageSettingsFailedListener;

    // Inteerface--> 接口OnGetUsageSettingsFailedListener
    public interface OnGetUsageSettingsFailedListener {
        void GetUsageSettingsFailed();
    }

    // 对外方式setOnGetUsageSettingsFailedListener
    public void setOnGetUsageSettingsFailedListener(OnGetUsageSettingsFailedListener onGetUsageSettingsFailedListener) {
        this.onGetUsageSettingsFailedListener = onGetUsageSettingsFailedListener;
    }

    // 封装方法GetUsageSettingsFailedNext
    private void GetUsageSettingsFailedNext() {
        if (onGetUsageSettingsFailedListener != null) {
            onGetUsageSettingsFailedListener.GetUsageSettingsFailed();
        }
    }

    private OnSetUsageSettingSuccessListener onSetUsageSettingSuccessListener;

    // Inteerface--> 接口OnSetUsageSettingSuccessListener
    public interface OnSetUsageSettingSuccessListener {
        void setUsageSettingSuccess();
    }

    // 对外方式setOnSetUsageSettingSuccessListener
    public void setOnSetUsageSettingSuccessListener(OnSetUsageSettingSuccessListener onSetUsageSettingSuccessListener) {
        this.onSetUsageSettingSuccessListener = onSetUsageSettingSuccessListener;
    }

    // 封装方法setUsageSettingSuccessNext
    private void setUsageSettingSuccessNext() {
        if (onSetUsageSettingSuccessListener != null) {
            onSetUsageSettingSuccessListener.setUsageSettingSuccess();
        }
    }

    private OnSetUsageSettingFailedListener onSetUsageSettingFailedListener;

    // Inteerface--> 接口OnSetUsageSettingFailedListener
    public interface OnSetUsageSettingFailedListener {
        void setUsageSettingFailed();
    }

    // 对外方式setOnSetUsageSettingFailedListener
    public void setOnSetUsageSettingFailedListener(OnSetUsageSettingFailedListener onSetUsageSettingFailedListener) {
        this.onSetUsageSettingFailedListener = onSetUsageSettingFailedListener;
    }

    // 封装方法setUsageSettingFailedNext
    private void setUsageSettingFailedNext() {
        if (onSetUsageSettingFailedListener != null) {
            onSetUsageSettingFailedListener.setUsageSettingFailed();
        }
    }

}
