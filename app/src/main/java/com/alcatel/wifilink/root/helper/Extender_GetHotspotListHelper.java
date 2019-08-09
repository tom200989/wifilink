package com.alcatel.wifilink.root.helper;

import com.alcatel.wifilink.root.bean.Extender_GetHotspotListResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetHotSpotListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetHotspotListHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianli.ma on 2018/5/23 0023.
 */

public class Extender_GetHotspotListHelper {
    public void get() {
        GetHotspotListHelper xGetHotspotListHelper = new GetHotspotListHelper();
        xGetHotspotListHelper.setOnGetHotSpotListSuccessListener(bean -> {
            Extender_GetHotspotListResult extenderGetHotspotListResult = new Extender_GetHotspotListResult();
            List<GetHotSpotListBean.HotspotBean> hotspotBeanList = bean.getHotspotList();
            if(hotspotBeanList != null && hotspotBeanList.size() >0 ){
                List<Extender_GetHotspotListResult.HotspotListBean> tempHotspotList = new ArrayList<>();
                for(GetHotSpotListBean.HotspotBean hotspotBean : hotspotBeanList){
                    Extender_GetHotspotListResult.HotspotListBean tempHotspotListBean = new Extender_GetHotspotListResult.HotspotListBean();
                    tempHotspotListBean.setConnectState(hotspotBean.getConnectState());
                    tempHotspotListBean.setHotspotId(hotspotBean.getHotspotId());
                    tempHotspotListBean.setIsSave(hotspotBean.getIsSave());
                    tempHotspotListBean.setSecurityMode(hotspotBean.getSecurityMode());
                    tempHotspotListBean.setSignal(hotspotBean.getSignal());
                    tempHotspotListBean.setSSID(hotspotBean.getSSID());
                    tempHotspotList.add(tempHotspotListBean);
                }
                extenderGetHotspotListResult.setHotspotList(tempHotspotList);
            }
            extenderGetHotspotListResult.setStatus(bean.getStatus());
            successNext(extenderGetHotspotListResult);
        });
        xGetHotspotListHelper.setOnGetHotSpotListFailListener(() -> {

        });
        xGetHotspotListHelper.getHotSpotList();
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
