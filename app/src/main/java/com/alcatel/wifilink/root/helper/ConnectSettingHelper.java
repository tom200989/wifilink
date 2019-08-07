package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.ConnectHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.DisConnectHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkRegisterStateHelper;

/**
 * Created by qianli.ma on 2017/11/25 0025.
 */

public class ConnectSettingHelper {

    public ConnectSettingHelper() {

    }

    /**
     * 获取漫游时是否允许连接的状态
     */
    public void getConnWhenRoam() {

        GetConnectionSettingsHelper xGetConnectionSettingsHelper = new GetConnectionSettingsHelper();
        xGetConnectionSettingsHelper.setOnGetConnectionSettingsSuccessListener(result -> {
            int roamingConnect = result.getRoamingConnect();
            switch (roamingConnect) {
                case GetConnectionSettingsBean.CONS_WHEN_ROAMING_CAN_NOT_CONNECT:
                    roamNotConnNext(result);
                    break;
                case GetConnectionSettingsBean.CONS_WHEN_ROAMING_CAN_CONNECT:
                    roamConnNext(result);
                    break;
            }
        });
        xGetConnectionSettingsHelper.setOnGetConnectionSettingsFailedListener(() -> {
            resultErrorNext(null);
            errorNext(null);
        });
        xGetConnectionSettingsHelper.getConnectionSettings();
    }

    private OnRoamNotConnListener onRoamNotConnListener;

    // 接口OnRoamNotConnListener
    public interface OnRoamNotConnListener {
        void roamNotConn(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnRoamNotConnListener
    public void setOnRoamNotConnListener(OnRoamNotConnListener onRoamNotConnListener) {
        this.onRoamNotConnListener = onRoamNotConnListener;
    }

    // 封装方法roamNotConnNext
    private void roamNotConnNext(GetConnectionSettingsBean attr) {
        if (onRoamNotConnListener != null) {
            onRoamNotConnListener.roamNotConn(attr);
        }
    }

    private OnRoamConnListener onRoamConnListener;

    // 接口OnRoamConnListener
    public interface OnRoamConnListener {
        void roamConn(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnRoamConnListener
    public void setOnRoamConnListener(OnRoamConnListener onRoamConnListener) {
        this.onRoamConnListener = onRoamConnListener;
    }

    // 封装方法roamConnNext
    private void roamConnNext(GetConnectionSettingsBean attr) {
        if (onRoamConnListener != null) {
            onRoamConnListener.roamConn(attr);
        }
    }

    /**
     * 切断连接
     */
    public void toDisConnect() {
        GetNetworkRegisterStateHelper xGetNetworkRegisterStateHelper = new GetNetworkRegisterStateHelper();
        xGetNetworkRegisterStateHelper.setOnRegisterSuccessListener(() -> connectOrDisconnect(false));// 注册成功
        xGetNetworkRegisterStateHelper.setOnRegisttingListener(() -> registingNext(1));// 注册中
        xGetNetworkRegisterStateHelper.setOnGetNetworkRegisterStateFailedListener(() -> registerFailedNext(3));// 注册失败
        xGetNetworkRegisterStateHelper.setOnNotRegisterListener(() -> notRegisterNext(0));// 没有注册
        xGetNetworkRegisterStateHelper.setOnGetNetworkRegisterStateFailedListener(() -> {
            registerResultErrorNext(null);
            registerErrorNext(null);
        });
        xGetNetworkRegisterStateHelper.getNetworkRegisterState();
    }

    /**
     * 连接
     */
    public void toConnect() {
        // 1.检测sim卡的注册状态
        GetNetworkRegisterStateHelper xGetNetworkRegisterStateHelper = new GetNetworkRegisterStateHelper();
        xGetNetworkRegisterStateHelper.setOnRegisterSuccessListener(() -> connectOrDisconnect(false));// 注册成功
        xGetNetworkRegisterStateHelper.setOnRegisttingListener(() -> registingNext(1));// 注册中
        xGetNetworkRegisterStateHelper.setOnGetNetworkRegisterStateFailedListener(() -> registerFailedNext(3));// 注册失败
        xGetNetworkRegisterStateHelper.setOnNotRegisterListener(() -> notRegisterNext(0));// 没有注册
        xGetNetworkRegisterStateHelper.setOnGetNetworkRegisterStateFailedListener(() -> {
            registerResultErrorNext(null);
            registerErrorNext(null);
        });
        xGetNetworkRegisterStateHelper.getNetworkRegisterState();
    }

    private OnNotRegisterListener onNotRegisterListener;

    // 接口OnNotRegisterListener
    public interface OnNotRegisterListener {
        void notRegister(int attr);
    }

    // 对外方式setOnNotRegisterListener
    public void setOnNotRegisterListener(OnNotRegisterListener onNotRegisterListener) {
        this.onNotRegisterListener = onNotRegisterListener;
    }

    // 封装方法notRegisterNext
    private void notRegisterNext(int attr) {
        if (onNotRegisterListener != null) {
            onNotRegisterListener.notRegister(attr);
        }
    }

    private OnRegisteFailedListener onRegisteFailedListener;

    // 接口OnRegisteFailedListener
    public interface OnRegisteFailedListener {
        void registerFailed(int attr);
    }

    // 对外方式setOnRegisteFailedListener
    public void setOnRegisteFailedListener(OnRegisteFailedListener onRegisteFailedListener) {
        this.onRegisteFailedListener = onRegisteFailedListener;
    }

    // 封装方法registerFailedNext
    private void registerFailedNext(int attr) {
        if (onRegisteFailedListener != null) {
            onRegisteFailedListener.registerFailed(attr);
        }
    }

    private OnRegistingListener onRegistingListener;

    // 接口OnRegistingListener
    public interface OnRegistingListener {
        void registing(int attr);
    }

    // 对外方式setOnRegistingListener
    public void setOnRegistingListener(OnRegistingListener onRegistingListener) {
        this.onRegistingListener = onRegistingListener;
    }

    // 封装方法registingNext
    private void registingNext(int attr) {
        if (onRegistingListener != null) {
            onRegistingListener.registing(attr);
        }
    }

    private OnRegisterResultErrorListener onRegisterResultErrorListener;

    // 接口OnRegisterResultErrorListener
    public interface OnRegisterResultErrorListener {
        void registerResultError(ResponseBody.Error attr);
    }

    // 对外方式setOnRegisterResultErrorListener
    public void setOnRegisterResultErrorListener(OnRegisterResultErrorListener onRegisterResultErrorListener) {
        this.onRegisterResultErrorListener = onRegisterResultErrorListener;
    }

    // 封装方法registerResultErrorNext
    private void registerResultErrorNext(ResponseBody.Error attr) {
        if (onRegisterResultErrorListener != null) {
            onRegisterResultErrorListener.registerResultError(attr);
        }
    }

    private OnRegisterErrorListener onRegisterErrorListener;

    // 接口OnRegisterErrorListener
    public interface OnRegisterErrorListener {
        void registerError(Throwable attr);
    }

    // 对外方式setOnRegisterErrorListener
    public void setOnRegisterErrorListener(OnRegisterErrorListener onRegisterErrorListener) {
        this.onRegisterErrorListener = onRegisterErrorListener;
    }

    // 封装方法registerErrorNext
    private void registerErrorNext(Throwable attr) {
        if (onRegisterErrorListener != null) {
            onRegisterErrorListener.registerError(attr);
        }
    }

    /**
     * 开启连接 | 断开连接
     *
     * @param needConn 是否需要连接 true:执行连接操作
     */
    private void connectOrDisconnect(boolean needConn) {
        if (needConn) {

            ConnectHelper xConnectHelper = new ConnectHelper();
            xConnectHelper.setOnConnectSuccessListener(() -> {
                connSuccessNext(null);
            });
            xConnectHelper.setOnConnectFailedListener(() -> {
                resultErrorNext(null);
                errorNext(null);
            });
            xConnectHelper.connect();

        } else {

            DisConnectHelper xDisConnectHelper = new DisConnectHelper();
            xDisConnectHelper.setOnDisconnectSuccessListener(() -> disconnSuccessNext(null));
            xDisConnectHelper.setOnDisconnectFailedListener(() -> {
                resultErrorNext(null);
                errorNext(null);

            });
            xDisConnectHelper.disconnect();

        }
    }

    private OnDisConnSuccessListener onDisConnSuccessListener;

    // 接口OnDisConnSuccessListener
    public interface OnDisConnSuccessListener {
        void disconnSuccess(Object attr);
    }

    // 对外方式setOnDisConnSuccessListener
    public void setOnDisConnSuccessListener(OnDisConnSuccessListener onDisConnSuccessListener) {
        this.onDisConnSuccessListener = onDisConnSuccessListener;
    }

    // 封装方法disconnSuccessNext
    private void disconnSuccessNext(Object attr) {
        if (onDisConnSuccessListener != null) {
            onDisConnSuccessListener.disconnSuccess(attr);
        }
    }

    private OnConnSuccessListener onConnSuccessListener;

    // 接口OnConnSuccessListener
    public interface OnConnSuccessListener {
        void connSuccess(Object attr);
    }

    // 对外方式setOnConnSuccessListener
    public void setOnConnSuccessListener(OnConnSuccessListener onConnSuccessListener) {
        this.onConnSuccessListener = onConnSuccessListener;
    }

    // 封装方法connSuccessNext
    private void connSuccessNext(Object attr) {
        if (onConnSuccessListener != null) {
            onConnSuccessListener.connSuccess(attr);
        }
    }

    /**
     * 获取连接状态
     */
    public void getConnSettingStatus() {

        GetConnectionSettingsHelper xGetConnectionSettingsHelper = new GetConnectionSettingsHelper();
        xGetConnectionSettingsHelper.setOnGetConnectionSettingsSuccessListener(result -> {
            connResultNext(result);
            switch (result.getConnectMode()) {
                case GetConnectionSettingsBean.CONS_AUTO_CONNECT:
                    connAutoNext(result);
                    break;
                case GetConnectionSettingsBean.CONS_MANUAL_CONNECT:
                    connManualNext(result);
                    break;
            }
        });
        xGetConnectionSettingsHelper.setOnGetConnectionSettingsFailedListener(() -> {
            errorNext(null);
            resultErrorNext(null);
        });
        xGetConnectionSettingsHelper.getConnectionSettings();

    }

    private OnNormalConnResultListener onNormalConnResultListener;

    // 接口OnNormalConnResultListener
    public interface OnNormalConnResultListener {
        void connResult(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnNormalConnResultListener
    public void setOnNormalConnResultListener(OnNormalConnResultListener onNormalConnResultListener) {
        this.onNormalConnResultListener = onNormalConnResultListener;
    }

    // 封装方法connResultNext
    private void connResultNext(GetConnectionSettingsBean attr) {
        if (onNormalConnResultListener != null) {
            onNormalConnResultListener.connResult(attr);
        }
    }

    private OnConnManualListener onConnManualListener;

    // 接口OnConnManualListener
    public interface OnConnManualListener {
        void connManual(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnConnManualListener
    public void setOnConnManualListener(OnConnManualListener onConnManualListener) {
        this.onConnManualListener = onConnManualListener;
    }

    // 封装方法connManualNext
    private void connManualNext(GetConnectionSettingsBean attr) {
        if (onConnManualListener != null) {
            onConnManualListener.connManual(attr);
        }
    }

    private OnConnAutoListener onConnAutoListener;

    // 接口OnConnAutoListener
    public interface OnConnAutoListener {
        void connAuto(GetConnectionSettingsBean attr);
    }

    // 对外方式setOnConnAutoListener
    public void setOnConnAutoListener(OnConnAutoListener onConnAutoListener) {
        this.onConnAutoListener = onConnAutoListener;
    }

    // 封装方法connAutoNext
    private void connAutoNext(GetConnectionSettingsBean attr) {
        if (onConnAutoListener != null) {
            onConnAutoListener.connAuto(attr);
        }
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

    private OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable attr);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable attr) {
        if (onErrorListener != null) {
            onErrorListener.error(attr);
        }
    }

    /**
     * 连接
     */
    public static void toConnect(Activity activity) {
        Log.v("ma_clickConn", "begin");

        GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetConnectionStateHelper.setOnDisconnectedListener(() -> {

            ConnectHelper xConnectHelper = new ConnectHelper();
            xConnectHelper.setOnConnectFailedListener(() -> ToastUtil_m.showLong(activity, activity.getString(R.string.usage_limit_over_notification_content)));
            xConnectHelper.connect();

        });
        xGetConnectionStateHelper.setOnDisConnectingListener(() -> {

            ConnectHelper xConnectHelper = new ConnectHelper();
            xConnectHelper.setOnConnectFailedListener(() -> ToastUtil_m.showLong(activity, activity.getString(R.string.usage_limit_over_notification_content)));
            xConnectHelper.connect();

        });
        xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> ToastUtil_m.showLong(activity, activity.getString(R.string.usage_limit_over_notification_content)));
        xGetConnectionStateHelper.getConnectionState();
    }

    private static void toast(Activity activity, int resId) {
        ToastUtil_m.show(activity, resId);
    }

    private static void toastLong(Activity activity, int resId) {
        ToastUtil_m.showLong(activity, resId);
    }

    private static void toast(Activity activity, String content) {
        ToastUtil_m.show(activity, content);
    }

    private static void to(Activity activity, Class ac, boolean isFinish) {
        CA.toActivity(activity, ac, false, isFinish, false, 0);
    }
}
