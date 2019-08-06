package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/**
 * Created by wzhiqiang on 2019/7/30
 */
public class DisConnectHotspotHelper extends BaseHelper {

    public void disConnectHotspot() {
        prepareHelperNext();
        XSmart xSmart = new XSmart<>();
        xSmart.xMethod(XCons.METHOD_DISCONNECT_HOTSPOT).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                disConnectHotSpotSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                disConnectHotSpotFailNext();
            }

            @Override
            public void fwError(FwError fwError) {
                disConnectHotSpotFailNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }
    
    /*----------------------------------请求断开连接热点的成功回调------------------------------*/
    public interface OnDisConnectHotSpotSuccessListener {
        void disConnectHotSpotSuccess( );
    }

    private OnDisConnectHotSpotSuccessListener onDisConnectHotSpotSuccessListener;

    //对外方式setDisConnectHotSpotSuccessListener
    public void setOnDisConnectHotSpotSuccessListener(OnDisConnectHotSpotSuccessListener onDisConnectHotSpotSuccessListener) {
        this.onDisConnectHotSpotSuccessListener = onDisConnectHotSpotSuccessListener;
    }

    //封装方法disConnectHotSpotSuccessNext
    private void disConnectHotSpotSuccessNext( ) {
        if (onDisConnectHotSpotSuccessListener != null) {
            onDisConnectHotSpotSuccessListener.disConnectHotSpotSuccess();
        }
    }

    /*----------------------------------请求断开连接热点的失败回调------------------------------*/
    public interface OnDisConnectHotSpotFailListener {
        void disConnectHotSpotFail( );
    }

    private OnDisConnectHotSpotFailListener onDisConnectHotSpotFailListener;

    //对外方式setOnDisConnectHotSpotFailListener
    public void setOnDisConnectHotSpotFailListener(OnDisConnectHotSpotFailListener onDisConnectHotSpotFailListener) {
        this.onDisConnectHotSpotFailListener = onDisConnectHotSpotFailListener;
    }

    //封装方法disConnectHotSpotFailNext
    private void disConnectHotSpotFailNext( ) {
        if (onDisConnectHotSpotFailListener != null) {
            onDisConnectHotSpotFailListener.disConnectHotSpotFail();
        }
    }
    
}
