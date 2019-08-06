package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
public class GetConnectionStateHelper extends BaseHelper {

    /**
     * 获取连接状态
     */
    public void getConnectionState() {
        prepareHelperNext();
        XSmart<GetConnectionStateBean> xGetConnState = new XSmart<>();
        xGetConnState.xMethod(XCons.METHOD_GET_CONNECTION_STATE);
        xGetConnState.xPost(new XNormalCallback<GetConnectionStateBean>() {
            @Override
            public void success(GetConnectionStateBean result) {
                int status = result.getConnectionStatus();
                if (status==GetConnectionStateBean.CONS_DISCONNECTED) {// 未连接
                    disconnectedNext();
                } else if (status == GetConnectionStateBean.CONS_CONNECTING) {// 连接中
                    connectingNext();
                } else if (status == GetConnectionStateBean.CONS_CONNECTED) {// 已连接
                    connectedNext();
                } else if (status == GetConnectionStateBean.CONS_DISCONNECTING) {// 断连中
                    disconnectingNext();
                }
            }

            @Override
            public void appError(Throwable ex) {
                GetConnectionStateFailedNext();
            }

            @Override
            public void fwError(FwError fwError) {
                GetConnectionStateFailedNext();
            }

            @Override
            public void finish() {
                doneHelperNext();
            }
        });
    }

    private OnDisconnectedListener onDisconnectedListener;

    // Inteerface--> 接口OnDisconnectedListener
    public interface OnDisconnectedListener {
        void disconnected();
    }

    // 对外方式setOnDisconnectedListener
    public void setOnDisconnectedListener(OnDisconnectedListener onDisconnectedListener) {
        this.onDisconnectedListener = onDisconnectedListener;
    }

    // 封装方法disconnectedNext
    private void disconnectedNext() {
        if (onDisconnectedListener != null) {
            onDisconnectedListener.disconnected();
        }
    }

    private OnConnectingListener onConnectingListener;

    // Inteerface--> 接口OnConnectingListener
    public interface OnConnectingListener {
        void connecting();
    }

    // 对外方式setOnConnectingListener
    public void setOnConnectingListener(OnConnectingListener onConnectingListener) {
        this.onConnectingListener = onConnectingListener;
    }

    // 封装方法connectingNext
    private void connectingNext() {
        if (onConnectingListener != null) {
            onConnectingListener.connecting();
        }
    }

    private OnConnectedListener onConnectedListener;

    // Inteerface--> 接口OnConnectedListener
    public interface OnConnectedListener {
        void connected();
    }

    // 对外方式setOnConnectedListener
    public void setOnConnectedListener(OnConnectedListener onConnectedListener) {
        this.onConnectedListener = onConnectedListener;
    }

    // 封装方法connectedNext
    private void connectedNext() {
        if (onConnectedListener != null) {
            onConnectedListener.connected();
        }
    }

    private OnDisConnectingListener onDisConnectingListener;

    // Inteerface--> 接口OnDisConnectingListener
    public interface OnDisConnectingListener {
        void disconnecting();
    }

    // 对外方式setOnDisConnectingListener
    public void setOnDisConnectingListener(OnDisConnectingListener onDisConnectingListener) {
        this.onDisConnectingListener = onDisConnectingListener;
    }

    // 封装方法disconnectingNext
    private void disconnectingNext() {
        if (onDisConnectingListener != null) {
            onDisConnectingListener.disconnecting();
        }
    }

    private OnGetConnectionStateFailedListener onGetConnectionStateFailedListener;

    // Inteerface--> 接口OnGetConnectionStateFailedListener
    public interface OnGetConnectionStateFailedListener {
        void GetConnectionStateFailed();
    }

    // 对外方式setOnGetConnectionStateFailedListener
    public void setOnGetConnectionStateFailedListener(OnGetConnectionStateFailedListener onGetConnectionStateFailedListener) {
        this.onGetConnectionStateFailedListener = onGetConnectionStateFailedListener;
    }

    // 封装方法GetConnectionStateFailedNext
    private void GetConnectionStateFailedNext() {
        if (onGetConnectionStateFailedListener != null) {
            onGetConnectionStateFailedListener.GetConnectionStateFailed();
        }
    }
}
