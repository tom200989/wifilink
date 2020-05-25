package com.alcatel.wifilink.helper;

import com.alcatel.wifilink.bean.Extender_GetHotspotListBean;
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
            Extender_GetHotspotListBean extenderGetHotspotListResult = new Extender_GetHotspotListBean();
            List<GetHotSpotListBean.HotspotBean> hotspotBeanList = bean.getHotspotList();
            if(hotspotBeanList != null && hotspotBeanList.size() >0 ){
                List<Extender_GetHotspotListBean.HotspotListBean> tempHotspotList = new ArrayList<>();
                for(GetHotSpotListBean.HotspotBean hotspotBean : hotspotBeanList){
                    Extender_GetHotspotListBean.HotspotListBean tempHotspotListBean = new Extender_GetHotspotListBean.HotspotListBean();
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
        xGetHotspotListHelper.setOnGetHotSpotListFailListener(this::getHotpotFailedNext);
        xGetHotspotListHelper.getHotSpotList();
    }

    private OnGetHotpotFailedListener onGetHotpotFailedListener;

    // Inteerface--> 接口OnGetHotpotFailedListener
    public interface OnGetHotpotFailedListener {
        void getHotpotFailed();
    }

    // 对外方式setOnGetHotpotFailedListener
    public void setOnGetHotpotFailedListener(OnGetHotpotFailedListener onGetHotpotFailedListener) {
        this.onGetHotpotFailedListener = onGetHotpotFailedListener;
    }

    // 封装方法getHotpotFailedNext
    private void getHotpotFailedNext() {
        if (onGetHotpotFailedListener != null) {
            onGetHotpotFailedListener.getHotpotFailed();
        }
    }

    private OnSuccessListener onSuccessListener;

    // 接口OnSuccessListener
    public interface OnSuccessListener {
        void success(Extender_GetHotspotListBean result);
    }

    // 对外方式setOnSuccessListener
    public void setOnSuccessListener(OnSuccessListener onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }

    // 封装方法successNext
    private void successNext(Extender_GetHotspotListBean result) {
        if (onSuccessListener != null) {
            onSuccessListener.success(result);
        }
    }
}
