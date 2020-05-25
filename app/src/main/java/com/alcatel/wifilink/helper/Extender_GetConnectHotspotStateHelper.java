package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.bean.Extender_GetConnectHotspotStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectHotspotStateHelper;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_GetConnectHotspotStateHelper {

    // 0: none
    // 1: connecting
    // 2: success
    // 3: password error
    // 4: need password
    // 5: fail

    public void get() {
        GetConnectHotspotStateHelper xGetConnectHotspotStateHelper = new GetConnectHotspotStateHelper();
        xGetConnectHotspotStateHelper.setOnGetConnectHotSpotStateSuccessListener(bean -> {
            Extender_GetConnectHotspotStateBean extenderGetConnectHotspotStateResult = new Extender_GetConnectHotspotStateBean();
            extenderGetConnectHotspotStateResult.setState(bean.getState());
            successNext(extenderGetConnectHotspotStateResult);
        });
        xGetConnectHotspotStateHelper.setOnGetConnectHotSpotFailListener(this::getHotspotStateFailedNext);
        xGetConnectHotspotStateHelper.getConnectHotspotState();
    }

    private OnGetHotspotStateFailedListener onGetHotspotStateFailedListener;

    // Inteerface--> 接口OnGetHotspotStateFailedListener
    public interface OnGetHotspotStateFailedListener {
        void getHotspotStateFailed();
    }

    // 对外方式setOnGetHotspotStateFailedListener
    public void setOnGetHotspotStateFailedListener(OnGetHotspotStateFailedListener onGetHotspotStateFailedListener) {
        this.onGetHotspotStateFailedListener = onGetHotspotStateFailedListener;
    }

    // 封装方法getHotspotStateFailedNext
    private void getHotspotStateFailedNext() {
        if (onGetHotspotStateFailedListener != null) {
            onGetHotspotStateFailedListener.getHotspotStateFailed();
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(Extender_GetConnectHotspotStateBean result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetConnectHotspotStateBean attr) {
        if (onSuccessListener != null) {
            onSuccessListener.success(attr);
        }
    }
}
