package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.root.bean.User_LoginState;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;

/**
 * Created by qianli.ma on 2018/1/4 0004.
 */

public class LoginStateHelper {
    private Activity activity;

    public LoginStateHelper(Activity activity) {
        this.activity = activity;
    }

    public void get() {
        // 检测wifi是否有连接
        boolean wifiConnect = OtherUtils.isWifiConnect(activity);
        if (wifiConnect) {
            yesWifiNext(wifiConnect);
            // 请求接口前
            GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
            xGetLoginStateHelper.setOnGetLoginStateSuccessListener(this::loginStateNext);
            xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
                resultErrorNext(null);
                errorNext(null);
            });
            xGetLoginStateHelper.getLoginState();

        } else {
            Logs.v("ma_unknown", "LoginStateHelper: no wifi");
            noWifiNext(wifiConnect);
        }
    }

    private CheckBoard.OnLoginstateListener onLoginstateListener;

    // 接口OnLoginstateListener
    public interface OnLoginstateListener {
        void loginState(User_LoginState attr);
    }

    // 对外方式setOnLoginstateListener
    public void setOnLoginstateListener(CheckBoard.OnLoginstateListener onLoginstateListener) {
        this.onLoginstateListener = onLoginstateListener;
    }

    // 封装方法loginStateNext
    private void loginStateNext(GetLoginStateBean attr) {
        if (onLoginstateListener != null) {
            onLoginstateListener.loginState(attr);
        }
    }

    private CheckBoard.OnYesWifiListener onYesWifiListener;

    // 接口OnYesWifiListener
    public interface OnYesWifiListener {
        void yesWifi(boolean attr);
    }

    // 对外方式setOnYesWifiListener
    public void setOnYesWifiListener(CheckBoard.OnYesWifiListener onYesWifiListener) {
        this.onYesWifiListener = onYesWifiListener;
    }

    // 封装方法yesWifiNext
    private void yesWifiNext(boolean attr) {
        if (onYesWifiListener != null) {
            onYesWifiListener.yesWifi(attr);
        }
    }

    private CheckBoard.OnNoWifiListener onNoWifiListener;

    // 接口OnNoWifiListener
    public interface OnNoWifiListener {
        void noWifi(boolean attr);
    }

    // 对外方式setOnNoWifiListener
    public void setOnNoWifiListener(CheckBoard.OnNoWifiListener onNoWifiListener) {
        this.onNoWifiListener = onNoWifiListener;
    }

    // 封装方法noWifiNext
    private void noWifiNext(boolean attr) {
        if (onNoWifiListener != null) {
            onNoWifiListener.noWifi(attr);
        }
    }

    private CheckBoard.OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error attr);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(CheckBoard.OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(ResponseBody.Error attr) {
        if (onResultErrorListener != null) {
            onResultErrorListener.resultError(attr);
        }
    }

    private CheckBoard.OnErrorListener onErrorListener;

    // 接口OnErrorListener
    public interface OnErrorListener {
        void error(Throwable attr);
    }

    // 对外方式setOnErrorListener
    public void setOnErrorListener(CheckBoard.OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    // 封装方法errorNext
    private void errorNext(Throwable attr) {
        if (onErrorListener != null) {
            onErrorListener.error(attr);
        }
    }
}
