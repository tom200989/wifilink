package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.network.ResponseBody;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkRegisterStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkSettingsBeanHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class NetworkSettingHelper {

    public NetworkSettingHelper() {

    }

    /**
     * 获取网络类型
     */
    public void getNetworkSetting() {
        // 先获取注册状态
        GetNetworkRegisterStateHelper xGetNetworkRegisterStateHelper = new GetNetworkRegisterStateHelper();
        xGetNetworkRegisterStateHelper.setOnRegisterSuccessListener(this::getnetworkSetting);
        xGetNetworkRegisterStateHelper.getNetworkRegisterState();
    }

    private void getnetworkSetting() {

        GetNetworkSettingsBeanHelper xGetNetworkSettingsBeanHelper = new GetNetworkSettingsBeanHelper();
        xGetNetworkSettingsBeanHelper.setOnGetNetworkSettingsSuccessListener(result -> {
            normalNetworkNext(result);
            int networkMode = result.getNetworkMode();
            switch (networkMode) {
                // 自动模式
                case Cons.AUTO_MODE:
                    autoNext(result);
                    break;

                // 2G模式
                case Cons.ONLY_2G:
                case Cons.GSM_LTE:
                case Cons.GSM_UMTS:
                    mode2GNext(result);
                    break;

                // 3G模式
                case Cons.ONLY_3G:
                case Cons.UMTS_LTE:
                case Cons.CDMA_EVDO:
                case Cons.EVDO_ONLY:
                case Cons.CDMA_EHRPD:
                case Cons.CDMA_ONLY_1X_SPRINT:
                    mode3GNext(result);
                    break;

                // 4G模式
                case Cons.ONLY_LTE:
                case Cons.LTE_CDMA_EVDO:
                    mode4GNext(result);
                    break;
            }
        });
        xGetNetworkSettingsBeanHelper.setOnGetNetworkSettingsFailedListener(() -> {
            errorNext(null);
            resultErrorNext(null);
        });
        xGetNetworkSettingsBeanHelper.getNetworkSettings();

    }

    private OnNormalNetworkListener onNormalNetworkListener;

    // 接口OnNormalNetworkListener
    public interface OnNormalNetworkListener {
        void normalNetwork(GetNetworkSettingsBean attr);
    }

    // 对外方式setOnNormalNetworkListener
    public void setOnNormalNetworkListener(OnNormalNetworkListener onNormalNetworkListener) {
        this.onNormalNetworkListener = onNormalNetworkListener;
    }

    // 封装方法normalNetworkNext
    private void normalNetworkNext(GetNetworkSettingsBean attr) {
        if (onNormalNetworkListener != null) {
            onNormalNetworkListener.normalNetwork(attr);
        }
    }

    private OnAutoListener onAutoListener;

    // 接口OnAutoListener
    public interface OnAutoListener {
        void auto(GetNetworkSettingsBean attr);
    }

    // 对外方式setOnAutoListener
    public void setOnAutoListener(OnAutoListener onAutoListener) {
        this.onAutoListener = onAutoListener;
    }

    // 封装方法autoNext
    private void autoNext(GetNetworkSettingsBean attr) {
        if (onAutoListener != null) {
            onAutoListener.auto(attr);
        }
    }

    private On4GListener on4GListener;

    // 接口On4GListener
    public interface On4GListener {
        void mode4G(GetNetworkSettingsBean attr);
    }

    // 对外方式setOn4GListener
    public void setOn4GListener(On4GListener on4GListener) {
        this.on4GListener = on4GListener;
    }

    // 封装方法mode4GNext
    private void mode4GNext(GetNetworkSettingsBean attr) {
        if (on4GListener != null) {
            on4GListener.mode4G(attr);
        }
    }

    private On3GListener on3GListener;

    // 接口On3GListener
    public interface On3GListener {
        void mode3G(GetNetworkSettingsBean attr);
    }

    // 对外方式setOn3GListener
    public void setOn3GListener(On3GListener on3GListener) {
        this.on3GListener = on3GListener;
    }

    // 封装方法mode3GNext
    private void mode3GNext(GetNetworkSettingsBean attr) {
        if (on3GListener != null) {
            on3GListener.mode3G(attr);
        }
    }

    private On2GListener on2GListener;

    // 接口On2GListener
    public interface On2GListener {
        void mode2G(GetNetworkSettingsBean attr);
    }

    // 对外方式setOn2GListener
    public void setOn2GListener(On2GListener on2GListener) {
        this.on2GListener = on2GListener;
    }

    // 封装方法mode2GNext
    private void mode2GNext(GetNetworkSettingsBean attr) {
        if (on2GListener != null) {
            on2GListener.mode2G(attr);
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error attr);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error attr) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(attr);
        }
    }

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable attr);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable attr) {
        if (onErrorListener != null) {
            onErrorListener.error(attr);
        }
    }
}
