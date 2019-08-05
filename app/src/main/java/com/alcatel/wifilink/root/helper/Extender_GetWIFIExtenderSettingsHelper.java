package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.Extender_GetWIFIExtenderCurrentStatusResult;
import com.alcatel.wifilink.root.bean.Extender_GetWIFIExtenderSettingsResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetWIFIExtenderSettingsHelper {
    public void get() {
        RX.getInstant().getWIFIExtenderSettings(new ResponseObject<Extender_GetWIFIExtenderSettingsResult>() {
            @Override
            protected void onSuccess(Extender_GetWIFIExtenderSettingsResult result) {
                successNext(result);
                /* 1.接口: 开关状态 */
                switch (result.getStationEnable()) {
                    case 0:// disable
                        stateEnableOffNext(0);
                        break;
                    case 1:// enable
                        stateEnableOnNext(1);
                        /* 2.接口: 初始化状态 */
                        switch (result.getExtenderInitingStatus()) {
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


            }

            @Override
            protected void onFailure() {
                failedNext(null);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                resultErrorNext(error);
            }
        });
    }

    /**
     * 获取当前热点信息
     */
    private void getCurrentHotpot() {
        RX.getInstant().getWIFIExtenderCurrentStatus(new ResponseObject<Extender_GetWIFIExtenderCurrentStatusResult>() {
            @Override
            protected void onSuccess(Extender_GetWIFIExtenderCurrentStatusResult result) {
                currentHotpotNext(result);
            }

            @Override
            protected void onFailure() {
                currentHotpotNext(null);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                currentHotpotNext(null);
            }
        });
    }

    private OnCurrentHotpotListener onCurrentHotpotListener;

    // 接口OnCurrentHotpotListener
    public interface OnCurrentHotpotListener {
        void currentHotpot(Extender_GetWIFIExtenderCurrentStatusResult result);
    }

    // 对外方式setOnCurrentHotpotListener
    public void setOnCurrentHotpotListener(OnCurrentHotpotListener onCurrentHotpotListener) {
        this.onCurrentHotpotListener = onCurrentHotpotListener;
    }

    // 封装方法currentHotpotNext
    private void currentHotpotNext(Extender_GetWIFIExtenderCurrentStatusResult attr) {
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

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error resultError);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error resultError) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(resultError);
        }
    }

    private OnFailedListener onFailedListener;

    // 接口OnFailedListener
    public interface OnFailedListener {
        void failed(Object attr);
    }

    // 对外方式setOnFailedListener
    public void setOnFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    // 封装方法failedNext
    private void failedNext(Object attr) {
        if (onFailedListener != null) {
            onFailedListener.failed(attr);
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(Extender_GetWIFIExtenderSettingsResult result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetWIFIExtenderSettingsResult result) {
        if (onSuccessListener != null) {
            onSuccessListener.success(result);
        }
    }
}
