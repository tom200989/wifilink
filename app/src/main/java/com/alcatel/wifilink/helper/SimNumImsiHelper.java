package com.alcatel.wifilink.helper;

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
        xGetSystemInfoHelper.setOnAppErrorListener(this::SimNumImsiFailedNext);
        xGetSystemInfoHelper.setOnFwErrorListener(this::SimNumImsiFailedNext);
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

    private OnSimNumImsiFailedListener onSimNumImsiFailedListener;

    // Inteerface--> 接口OnSimNumImsiFailedListener
    public interface OnSimNumImsiFailedListener {
        void SimNumImsiFailed();
    }

    // 对外方式setOnSimNumImsiFailedListener
    public void setOnSimNumImsiFailedListener(OnSimNumImsiFailedListener onSimNumImsiFailedListener) {
        this.onSimNumImsiFailedListener = onSimNumImsiFailedListener;
    }

    // 封装方法SimNumImsiFailedNext
    private void SimNumImsiFailedNext() {
        if (onSimNumImsiFailedListener != null) {
            onSimNumImsiFailedListener.SimNumImsiFailed();
        }
    }
}
