package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;
import com.p_xhelper_smart.p_xhelper_smart.utils.Logg;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class HeartBeatHelper extends BaseHelper {

    /**
     * 心跳
     */
    public void heartbeat() {
        XSmart xHeartbeat = new XSmart();
        xHeartbeat.xMethod(XCons.METHOD_HEART_BEAT).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                Logg.t(XCons.TAG).ww("--> heart beat success! -->");
                heartbeatSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                Logg.t(XCons.TAG).ww("--> heart beat app error! -->");
                heartbeatFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                Logg.t(XCons.TAG).ww("--> heart beat fw error! -->");
                heartbeatFailedNext();
            }

            @Override
            public void finish() {

            }
        });
    }

    private OnHeartBeatSuccessListener onHeartBeatSuccessListener;

    // Inteerface--> 接口OnHeartBeatSuccessListener
    public interface OnHeartBeatSuccessListener {
        void heartbeatSuccess();
    }

    // 对外方式setOnHeartBeatSuccessListener
    public void setOnHeartBeatSuccessListener(OnHeartBeatSuccessListener onHeartBeatSuccessListener) {
        this.onHeartBeatSuccessListener = onHeartBeatSuccessListener;
    }

    // 封装方法heartbeatSuccessNext
    private void heartbeatSuccessNext() {
        if (onHeartBeatSuccessListener != null) {
            onHeartBeatSuccessListener.heartbeatSuccess();
        }
    }

    private OnHeartbeanFailedListener onHeartbeanFailedListener;

    // Inteerface--> 接口OnHeartbeanFailedListener
    public interface OnHeartbeanFailedListener {
        void heartbeatFailed();
    }

    // 对外方式setOnHeartbeanFailedListener
    public void setOnHeartbeanFailedListener(OnHeartbeanFailedListener onHeartbeanFailedListener) {
        this.onHeartbeanFailedListener = onHeartbeanFailedListener;
    }

    // 封装方法heartbeatFailedNext
    private void heartbeatFailedNext() {
        if (onHeartbeanFailedListener != null) {
            onHeartbeanFailedListener.heartbeatFailed();
        }
    }
}
