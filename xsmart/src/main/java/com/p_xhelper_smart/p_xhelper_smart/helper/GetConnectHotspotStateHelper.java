package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectHotSpotStateParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectHotspotStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class GetConnectHotspotStateHelper extends BaseHelper {

    /**
     * 获取当前热点的连接状态
     */
    public void getConnectHotspotState(GetConnectHotSpotStateParam param) {
        prepareHelperNext();
        XSmart<GetConnectHotspotStateBean> xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_GET_CONNECT_HOTSPOT_STATE).xParam(param).xPost(new XNormalCallback<GetConnectHotspotStateBean>() {
            @Override
            public void success(GetConnectHotspotStateBean result) {
                getConnectHotSpotStateSuNext(result);
            }

            @Override
            public void appError(Throwable ex) {
                getConnectHotSpotStateFail();
            }

            @Override
            public void fwError(FwError fwError) {
                getConnectHotSpotStateFail();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    /*----------------------------------获取当前热点连接状态成功的回调------------------------------*/
    public interface OnGetConnectHotSpotStateSuccessListener {
        void getConnectHotSpotStateSuccess(GetConnectHotspotStateBean bean);
    }

    private OnGetConnectHotSpotStateSuccessListener onGetConnectHotSpotStateSuccessListener;

    //对外方式setOnGetConnectHotSpotStateSuccessListener
    public void setOnGetConnectHotSpotStateSuccessListener(OnGetConnectHotSpotStateSuccessListener onGetConnectHotSpotStateSuccessListener) {
        this.onGetConnectHotSpotStateSuccessListener = onGetConnectHotSpotStateSuccessListener;
    }

    //封装方法getConnectHotSpotStateSuNext
    private void getConnectHotSpotStateSuNext(GetConnectHotspotStateBean bean) {
        if (onGetConnectHotSpotStateSuccessListener != null) {
            onGetConnectHotSpotStateSuccessListener.getConnectHotSpotStateSuccess(bean);
        }
    }
    /*----------------------------------获取当前热点连接状态失败的回调------------------------------*/
    public interface OnGetConnectHotSpotFailListener {
        void getConnectHotSpotStateFail();
    }

    private OnGetConnectHotSpotFailListener onGetConnectHotSpotFailListener;

    //对外方式setOnGetConnectHotSpotFailListener
    public void setOnGetConnectHotSpotFailListener(OnGetConnectHotSpotFailListener onGetConnectHotSpotFailListener) {
        this.onGetConnectHotSpotFailListener = onGetConnectHotSpotFailListener;
    }

    //封装方法getConnectHotSpotStateFail
    private void getConnectHotSpotStateFail() {
        if (onGetConnectHotSpotFailListener != null) {
            onGetConnectHotSpotFailListener.getConnectHotSpotStateFail();
        }
    }


}

