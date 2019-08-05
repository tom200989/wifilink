package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/*
 * Created by qianli.ma on 2018/10/15 0015.
 */
public class ResetHelper {
    public void reset() {
        RX.getInstant().resetDevice(new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
                resetSuccessNext();
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                resultErrorNext(error);
            }

            @Override
            public void onError(Throwable e) {
                errorNext(e);
            }

            @Override
            protected void onFailure() {
                failedNext();
            }
        });
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnResetSuccessListener onResetSuccessListener;

    // Inteerface--> 接口OnResetSuccessListener
    public interface OnResetSuccessListener {
        void resetSuccess();
    }

    // 对外方式setOnResetSuccessListener
    public void setOnResetSuccessListener(OnResetSuccessListener onResetSuccessListener) {
        this.onResetSuccessListener = onResetSuccessListener;
    }

    // 封装方法resetSuccessNext
    private void resetSuccessNext() {
        if (onResetSuccessListener != null) {
            onResetSuccessListener.resetSuccess();
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // Inteerface--> 接口OnResultErrorListener
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

    private OnErrorListener onErrorListener;

    // Inteerface--> 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable throwable);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable throwable) {
        if (onErrorListener != null) {
            onErrorListener.error(throwable);
        }
    }

    private OnFailedListener onFailedListener;

    // Inteerface--> 接口OnFailedListener
    public interface OnFailedListener {
        void failed();
    }

    // 对外方式setOnFailedListener
    public void setOnFailedListener(OnFailedListener onFailedListener) {
        this.onFailedListener = onFailedListener;
    }

    // 封装方法failedNext
    private void failedNext() {
        if (onFailedListener != null) {
            onFailedListener.failed();
        }
    }
}
