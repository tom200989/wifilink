package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class DisConnectHelper extends BaseHelper {

    /**
     * 断连
     */
    public void disconnect() {
        prepareHelperNext();
        XSmart xDisconnect = new XSmart();
        xDisconnect.xMethod(XCons.METHOD_DISCONNECT);
        xDisconnect.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                disconnectSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                disconnectFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                disconnectFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnDisconnectSuccessListener onDisconnectSuccessListener;

    // Inteerface--> 接口OnDisconnectSuccessListener
    public interface OnDisconnectSuccessListener {
        void disconnectSuccess();
    }

    // 对外方式setOnDisconnectSuccessListener
    public void setOnDisconnectSuccessListener(OnDisconnectSuccessListener onDisconnectSuccessListener) {
        this.onDisconnectSuccessListener = onDisconnectSuccessListener;
    }

    // 封装方法disconnectSuccessNext
    private void disconnectSuccessNext() {
        if (onDisconnectSuccessListener != null) {
            onDisconnectSuccessListener.disconnectSuccess();
        }
    }

    private OnDisconnectFailedListener onDisconnectFailedListener;

    // Inteerface--> 接口OnDisconnectFailedListener
    public interface OnDisconnectFailedListener {
        void disconnectFailed();
    }

    // 对外方式setOnDisconnectFailedListener
    public void setOnDisconnectFailedListener(OnDisconnectFailedListener onDisconnectFailedListener) {
        this.onDisconnectFailedListener = onDisconnectFailedListener;
    }

    // 封装方法disconnectFailedNext
    private void disconnectFailedNext() {
        if (onDisconnectFailedListener != null) {
            onDisconnectFailedListener.disconnectFailed();
        }
    }
}
