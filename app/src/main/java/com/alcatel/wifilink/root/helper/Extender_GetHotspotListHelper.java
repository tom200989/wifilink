package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.Extender_GetHotspotListResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetHotspotListHelper {
    public void get() {
        RX.getInstant().getHotspotList(new ResponseObject<Extender_GetHotspotListResult>() {
            @Override
            protected void onSuccess(Extender_GetHotspotListResult result) {
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
        void success(Extender_GetHotspotListResult result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetHotspotListResult result) {
        if (onSuccessListener != null) {
            onSuccessListener.success(result);
        }
    }
}
