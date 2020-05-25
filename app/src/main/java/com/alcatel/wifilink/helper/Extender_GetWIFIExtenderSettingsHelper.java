package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.bean.Extender_GetWIFICurrentStatuBean;
import com.alcatel.wifilink.bean.Extender_GetWIFISettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWIFIExtenderCurrentStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWIFIExtenderSettingsHelper;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFIExtenderSettingsHelper {
    public void get() {
        GetWIFIExtenderSettingsHelper xGetWIFIExtenderSettingsHelper = new GetWIFIExtenderSettingsHelper();
        xGetWIFIExtenderSettingsHelper.setGetWifiExtenderSettingsSuccessListener(bean -> {
            Extender_GetWIFISettingsBean extenderGetWIFIExtenderSettingsResult = new Extender_GetWIFISettingsBean();
            extenderGetWIFIExtenderSettingsResult.setExtenderInitingStatus(bean.getExtenderInitingStatus());
            extenderGetWIFIExtenderSettingsResult.setStationEnable(bean.getStationEnable());
            successNext(extenderGetWIFIExtenderSettingsResult);
            /* 1.接口: 开关状态 */
            switch (bean.getStationEnable()) {
                case 0:// disable
                    stateEnableOffNext(0);
                    break;
                case 1:// enable
                    stateEnableOnNext(1);
                    /* 2.接口: 初始化状态 */
                    switch (bean.getExtenderInitingStatus()) {
                        case 0:// initing
                            initNext(0);
                            break;
                        case 1:// Complete
                            completeNext(1);
                            getCurrentHotpot();
                            break;
                        case 2:// Inited Failed
                            initFailedNext(2);
                            break;
                    }
                    break;
            }
        });
        xGetWIFIExtenderSettingsHelper.setGetWifiExtenderSettingsFailListener(this::getExtenderFailedNext);
        xGetWIFIExtenderSettingsHelper.getWIFIExtenderSettings();
    }

    /**
     * 获取当前热点信息
     */
    private void getCurrentHotpot() {
        GetWIFIExtenderCurrentStatusHelper xGetWIFIExtenderCurrentStatusHelper = new GetWIFIExtenderCurrentStatusHelper();
        xGetWIFIExtenderCurrentStatusHelper.setOnGetWifiExCurStatusSuccessListener(bean -> {
            Extender_GetWIFICurrentStatuBean extenderGetWIFIExtenderCurrentStatusResult = new Extender_GetWIFICurrentStatuBean();
            extenderGetWIFIExtenderCurrentStatusResult.setHotspotConnectStatus(bean.getHotspotConnectStatus());
            extenderGetWIFIExtenderCurrentStatusResult.setHotspotSSID(bean.getHotspotSSID());
            extenderGetWIFIExtenderCurrentStatusResult.setIPV4Addr(bean.getIPV4Addr());
            extenderGetWIFIExtenderCurrentStatusResult.setIPV6Addr(bean.getIPV6Addr());
            extenderGetWIFIExtenderCurrentStatusResult.setSignal(bean.getSignal());
            currentHotpotNext(extenderGetWIFIExtenderCurrentStatusResult);
        });
        xGetWIFIExtenderCurrentStatusHelper.setOnGetWifiExCurStatusFailListener(() -> currentHotpotNext(null));
        xGetWIFIExtenderCurrentStatusHelper.getWIFIExtenderCurrentStatus();
    }

    private OnCurrentHotpotListener onCurrentHotpotListener;

    // 接口OnCurrentHotpotListener
    public interface OnCurrentHotpotListener {
        void currentHotpot(Extender_GetWIFICurrentStatuBean result);
    }

    // 对外方式setOnCurrentHotpotListener
    public void setOnCurrentHotpotListener(OnCurrentHotpotListener onCurrentHotpotListener) {
        this.onCurrentHotpotListener = onCurrentHotpotListener;
    }

    // 封装方法currentHotpotNext
    private void currentHotpotNext(Extender_GetWIFICurrentStatuBean attr) {
        if (onCurrentHotpotListener != null) {
            onCurrentHotpotListener.currentHotpot(attr);
        }
    }

    private OnstateEnableOffListener onstateEnableOffListener;

    // 接口OnstateEnableOffListener
    public interface OnstateEnableOffListener {
        void stateEnableOff(int stateEnable);
    }

    // 对外方式setOnstateEnableOffListener
    public void setOnstateEnableOffListener(OnstateEnableOffListener onstateEnableOffListener) {
        this.onstateEnableOffListener = onstateEnableOffListener;
    }

    // 封装方法stateEnableOffNext
    private void stateEnableOffNext(int stateEnable) {
        if (onstateEnableOffListener != null) {
            onstateEnableOffListener.stateEnableOff(stateEnable);
        }
    }

    private OnStateEnableOnListener onStateEnableOnListener;

    // 接口OnStateEnableOnListener
    public interface OnStateEnableOnListener {
        void stateEnableOn(int stateEnable);
    }

    // 对外方式setOnStateEnableOnListener
    public void setOnStateEnableOnListener(OnStateEnableOnListener onStateEnableOnListener) {
        this.onStateEnableOnListener = onStateEnableOnListener;
    }

    // 封装方法stateEnableOnNext
    private void stateEnableOnNext(int stateEnable) {
        if (onStateEnableOnListener != null) {
            onStateEnableOnListener.stateEnableOn(stateEnable);
        }
    }

    private OnInitFailedListener onInitFailedListener;

    // 接口OnInitFailedListener
    public interface OnInitFailedListener {
        void initFailed(int initStatus);
    }

    // 对外方式setOnInitFailedListener
    public void setOnInitFailedListener(OnInitFailedListener onInitFailedListener) {
        this.onInitFailedListener = onInitFailedListener;
    }

    // 封装方法initFailedNext
    private void initFailedNext(int initStatus) {
        if (onInitFailedListener != null) {
            onInitFailedListener.initFailed(initStatus);
        }
    }

    private OnCompleteListener onCompleteListener;

    // 接口OnCompleteListener
    public interface OnCompleteListener {
        void complete(int initStatus);
    }

    // 对外方式setOnCompleteListener
    public void setOnCompleteListener(OnCompleteListener onCompleteListener) {
        this.onCompleteListener = onCompleteListener;
    }

    // 封装方法completeNext
    private void completeNext(int initStatus) {
        if (onCompleteListener != null) {
            onCompleteListener.complete(initStatus);
        }
    }

    private OnInitListener onInitListener;

    // 接口OnInitListener
    public interface OnInitListener {
        void init(int initStatus);
    }

    // 对外方式setOnInitListener
    public void setOnInitListener(OnInitListener onInitListener) {
        this.onInitListener = onInitListener;
    }

    // 封装方法initNext
    private void initNext(int initStatus) {
        if (onInitListener != null) {
            onInitListener.init(initStatus);
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(Extender_GetWIFISettingsBean result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetWIFISettingsBean result) {
        if (onSuccessListener != null) {
            onSuccessListener.success(result);
        }
    }

    private OnGetExtenderFailedListener onGetExtenderFailedListener;

    // Inteerface--> 接口OnGetExtenderFailedListener
    public interface OnGetExtenderFailedListener {
        void getExtenderFailed();
    }

    // 对外方式setOnGetExtenderFailedListener
    public void setOnGetExtenderFailedListener(OnGetExtenderFailedListener onGetExtenderFailedListener) {
        this.onGetExtenderFailedListener = onGetExtenderFailedListener;
    }

    // 封装方法getExtenderFailedNext
    private void getExtenderFailedNext() {
        if (onGetExtenderFailedListener != null) {
            onGetExtenderFailedListener.getExtenderFailed();
        }
    }
}
