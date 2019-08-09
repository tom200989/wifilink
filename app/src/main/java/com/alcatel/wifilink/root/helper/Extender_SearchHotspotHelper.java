package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.p_xhelper_smart.p_xhelper_smart.helper.SearchHotspotHelper;

/**
 * Created by qianli.ma on 2018/5/24 0024.
 */

public class Extender_SearchHotspotHelper {
    public void search() {
        SearchHotspotHelper xSearchHotspotHelper = new SearchHotspotHelper();
        xSearchHotspotHelper.setOnSearchHotSpotSuccessListener(this::successNext);
        xSearchHotspotHelper.setOnSearchHotSpotFailListener(() -> {
            failedNext(null);
            resultErrorNext(null);
        });
        xSearchHotspotHelper.searchHotSpot();
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
        void success(Object attr);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Object attr) {
        if (onSuccessListener != null) {
            onSuccessListener.success(attr);
        }
    }
}
