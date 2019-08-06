package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.ConnectHotspotParam;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class ConnectHotspotHelper extends BaseHelper {


    public void connectHotSpot(ConnectHotspotParam param) {
        prepareHelperNext();
        XSmart xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_CONNECT_HOTSPOT).xParam(param).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                connectHotSpotSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                connectHotSpotFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                connectHotSpotFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }


    /*----------------------------------请求连接热点成功的回调------------------------------*/
    public interface OnConnectHotSpotSuccessListener {
        void connectHotSpotSuccess();
    }

    private OnConnectHotSpotSuccessListener onConnectHotSpotSuccessListener;

    //对外方式setOnConnectHotSpotSuccessListener
    public void setOnConnectHotSpotSuccessListener(OnConnectHotSpotSuccessListener onConnectHotSpotSuccessListener) {
        this.onConnectHotSpotSuccessListener = onConnectHotSpotSuccessListener;
    }

    //封装方法connectHotSpotSuccessNext
    private void connectHotSpotSuccessNext() {
        if (onConnectHotSpotSuccessListener != null) {
            onConnectHotSpotSuccessListener.connectHotSpotSuccess();
        }
    }

    /*----------------------------------请求连接热点失败的回调------------------------------*/
    public interface OnConnectHotSpotFailListener {
        void connectHotSpotFail();
    }

    private OnConnectHotSpotFailListener onConnectHotSpotFailListener;

    //对外方式setOnConnectHotSpotFailListener
    public void setOnConnectHotSpotFailListener(OnConnectHotSpotFailListener onConnectHotSpotFailListener) {
        this.onConnectHotSpotFailListener = onConnectHotSpotFailListener;
    }

    //封装方法connectHotSpotFailNext
    private void connectHotSpotFailNext() {
        if (onConnectHotSpotFailListener != null) {
            onConnectHotSpotFailListener.connectHotSpotFail();
        }
    }


}
