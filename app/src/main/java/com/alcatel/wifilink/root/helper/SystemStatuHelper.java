package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.System_SystemStates;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/**
 * Created by qianli.ma on 2018/6/4 0004.
 */

public class SystemStatuHelper {
    public void get() {
        RX.getInstant().getSystemStates(new ResponseObject<System_SystemStates>() {
            @Override
            protected void onSuccess(System_SystemStates result) {
                successNext(result);
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

    private OnFailedListener onFailedListener;

    // 接口OnFailedListener
    public interface OnFailedListener {
        void failed(Object o);
    }

    // 对外方式setOnFailedListener
    public void setOnFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    // 封装方法failedNext
    private void failedNext(Object o) {
        if (onFailedListener != null) {
            onFailedListener.failed(o);
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error error);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error error) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(error);
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(System_SystemStates systemSystemStates);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(System_SystemStates systemSystemStates) {
        if (onSuccessListener != null) {
            onSuccessListener.success(systemSystemStates);
        }
    }
}
