package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.System_SystemInfo;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/**
 * Created by qianli.ma on 2017/12/21 0021.
 */

public class SystemInfoHelper {

    public void get() {
        RX.getInstant().getSystemInfo(new ResponseObject<System_SystemInfo>() {
            @Override
            protected void onSuccess(System_SystemInfo result) {
                getSuccessNext(result);
            }

            @Override
            public void onError(Throwable e) {
                errorNext(e);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                resultErrorNext(error);
            }

            @Override
            protected void onFailure() {
                getSystemInfoFailedNext();
            }
        });
    }

    private OnGetSystemInfoFailedListener onGetSystemInfoFailedListener;

    // Inteerface--> 接口OnGetSystemInfoFailedListener
    public interface OnGetSystemInfoFailedListener {
        void getSystemInfoFailed();
    }

    // 对外方式setOnGetSystemInfoFailedListener
    public void setOnGetSystemInfoFailedListener(OnGetSystemInfoFailedListener onGetSystemInfoFailedListener) {
        this.onGetSystemInfoFailedListener = onGetSystemInfoFailedListener;
    }

    // 封装方法getSystemInfoFailedNext
    private void getSystemInfoFailedNext() {
        if (onGetSystemInfoFailedListener != null) {
            onGetSystemInfoFailedListener.getSystemInfoFailed();
        }
    }

    private OnGetSystemInfoSuccessListener onGetSystemInfoSuccessListener;

    // 接口OnGetSystemInfoSuccessListener
    public interface OnGetSystemInfoSuccessListener {
        void getSuccess(System_SystemInfo attr);
    }

    // 对外方式setOnGetSystemInfoSuccessListener
    public void setOnGetSystemInfoSuccessListener(OnGetSystemInfoSuccessListener onGetSystemInfoSuccessListener) {
        this.onGetSystemInfoSuccessListener = onGetSystemInfoSuccessListener;
    }

    // 封装方法getSuccessNext
    private void getSuccessNext(System_SystemInfo attr) {
        if (onGetSystemInfoSuccessListener != null) {
            onGetSystemInfoSuccessListener.getSuccess(attr);
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
