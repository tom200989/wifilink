package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class ConnectHelper extends BaseHelper {

    /**
     * 连接
     */
    public void connect() {
        prepareHelperNext();
        XSmart xConnect = new XSmart();
        xConnect.xMethod(XCons.METHOD_CONNECT);
        xConnect.xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                connectSuccessNext();
            }

            @Override
            public void appError(Throwable ex) {
                connectFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                connectFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnConnectSuccessListener onConnectSuccessListener;

    // Inteerface--> 接口OnConnectSuccessListener
    public interface OnConnectSuccessListener {
        void connectSuccess();
    }

    // 对外方式setOnConnectSuccessListener
    public void setOnConnectSuccessListener(OnConnectSuccessListener onConnectSuccessListener) {
        this.onConnectSuccessListener = onConnectSuccessListener;
    }

    // 封装方法connectSuccessNext
    private void connectSuccessNext() {
        if (onConnectSuccessListener != null) {
            onConnectSuccessListener.connectSuccess();
        }
    }

    private OnConnectFailedListener onConnectFailedListener;

    // Inteerface--> 接口OnConnectFailedListener
    public interface OnConnectFailedListener {
        void connectFailed();
    }

    // 对外方式setOnConnectFailedListener
    public void setOnConnectFailedListener(OnConnectFailedListener onConnectFailedListener) {
        this.onConnectFailedListener = onConnectFailedListener;
    }

    // 封装方法connectFailedNext
    private void connectFailedNext() {
        if (onConnectFailedListener != null) {
            onConnectFailedListener.connectFailed();
        }
    }
}
