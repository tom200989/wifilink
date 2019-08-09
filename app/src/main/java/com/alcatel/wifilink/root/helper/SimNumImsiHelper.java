package com.alcatel.wifilink.root.helper;

import android.text.TextUtils;

import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class SimNumImsiHelper {
    public SimNumImsiHelper() {
    }

    /**
     * 获取IMSI号以及SIM号
     */
    public void getSimNumAndImsi() {

        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            String msisdn = result.getMSISDN();
            String imsi = result.getIMSI();
            if (!TextUtils.isEmpty(msisdn)) {
                simNumberNext(msisdn);
            }
            if (!TextUtils.isEmpty(imsi)) {
                imsiNext(imsi);
            }
        });
        xGetSystemInfoHelper.setOnAppErrorListener(() -> errorNext());
        xGetSystemInfoHelper.setOnFwErrorListener(() -> resultErrorNext());
        xGetSystemInfoHelper.getSystemInfo();
    }

    private OnImsiListener onImsiListener;

    // 接口OnImsiListener
    public interface OnImsiListener {
        void imsi(String imsi);
    }

    // 对外方式setOnImsiListener
    public void setOnImsiListener(OnImsiListener onImsiListener) {
        this.onImsiListener = onImsiListener;
    }

    // 封装方法imsiNext
    private void imsiNext(String imsi) {
        if (onImsiListener != null) {
            onImsiListener.imsi(imsi);
        }
    }

    private OnSimNumberListener onSimNumberListener;

    // 接口OnSimNumberListener
    public interface OnSimNumberListener {
        void simNumber(String simNum);
    }

    // 封装方法simNumberNext
    private void simNumberNext(String simNum) {
        if (onSimNumberListener != null) {
            onSimNumberListener.simNumber(simNum);
        }
    }

    // 对外方式setOnSimNumberListener
    public void setOnSimNumberListener(OnSimNumberListener onSimNumberListener) {
        this.onSimNumberListener = onSimNumberListener;
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError();
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext() {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError();
        }
    }

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error();
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext() {
        if (onErrorListener != null) {
            onErrorListener.error();
        }
    }
}
