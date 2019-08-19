package com.alcatel.wifilink.root.helper;

import android.app.Activity;

import com.alcatel.wifilink.root.ue.root_activity.LoginRxActivity;
import com.alcatel.wifilink.root.utils.AppInfo;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

/**
 * Created by qianli.ma on 2017/11/15 0015.
 */

public abstract class CheckBoard {

    private OnBoardListener onBoardListener;
    private OnAllErrorListener onAllErrorListener;

    public CheckBoard() {
    }

    public CheckBoard(OnBoardListener onBoardListener) {
        this.onBoardListener = onBoardListener;
    }

    public CheckBoard(OnAllErrorListener onAllErrorListener) {
        this.onAllErrorListener = onAllErrorListener;
    }

    public abstract void successful();

    /**
     * 检测是否连接上硬件
     *
     * @param ori    调用的ac
     * @param target 出错时的目标ac
     */
    public void checkBoard(Activity ori, Class... target) {
        // 检测wifi是否有连接
        boolean wifiConnect = OtherUtils.isWifiConnect(ori);
        if (wifiConnect) {
            yesWifiNext(wifiConnect);
            // 请求接口前
            onPrepare();

            GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
            xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                onSuccessful();// 请求接口成功后
                successful();
                loginStateNext(getLoginStateBean);
            });
            xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
                resultErrorNext(null);
                allError();
                onResultErrors(null);// 请求接口中途错误
                CA.toActivity(ori, target[0], false, true, false, 0);
            });
            xGetLoginStateHelper.getLoginState();
            
        } else {
            Logs.t("ma_unknown").vv("Checkboard--> wifi not Connect");
            noWifiNext(wifiConnect);
            // wifi掉线
            if (ori != null) {
                // 获取当前顶层的activity
                String currentActivitySimpleName = AppInfo.getCurrentActivitySimpleName(ori);
                String simpleName = LoginRxActivity.class.getSimpleName();
                // 如果当前是处于登陆页面则不跳转
                if (!currentActivitySimpleName.equalsIgnoreCase(simpleName) & // 相等
                            !currentActivitySimpleName.contains(simpleName) & // 包含
                            !simpleName.contains(currentActivitySimpleName) // 包含
                        ) {
                    CA.toActivity(ori, target[1] != null ? target[1] : target[0], false, true, false, 0);
                } else {
                    Logs.t("ma_unknown").vv("Checkboard--> checkBoard");  
                }
                
            }
        }
    }

    private OnLoginstateListener onLoginstateListener;

    // 接口OnLoginstateListener
    public interface OnLoginstateListener {
        void loginState(GetLoginStateBean attr);
    }

    // 对外方式setOnLoginstateListener
    public void setOnLoginstateListener(OnLoginstateListener onLoginstateListener) {
        this.onLoginstateListener = onLoginstateListener;
    }

    // 封装方法loginStateNext
    private void loginStateNext(GetLoginStateBean attr) {
        if (onLoginstateListener != null) {
            onLoginstateListener.loginState(attr);
        }
    }

    private OnYesWifiListener onYesWifiListener;

    // 接口OnYesWifiListener
    public interface OnYesWifiListener {
        void yesWifi(boolean attr);
    }

    // 对外方式setOnYesWifiListener
    public void setOnYesWifiListener(OnYesWifiListener onYesWifiListener) {
        this.onYesWifiListener = onYesWifiListener;
    }

    // 封装方法yesWifiNext
    private void yesWifiNext(boolean attr) {
        if (onYesWifiListener != null) {
            onYesWifiListener.yesWifi(attr);
        }
    }

    private OnNoWifiListener onNoWifiListener;

    // 接口OnNoWifiListener
    public interface OnNoWifiListener {
        void noWifi(boolean attr);
    }

    // 对外方式setOnNoWifiListener
    public void setOnNoWifiListener(OnNoWifiListener onNoWifiListener) {
        this.onNoWifiListener = onNoWifiListener;
    }

    // 封装方法noWifiNext
    private void noWifiNext(boolean attr) {
        if (onNoWifiListener != null) {
            onNoWifiListener.noWifi(attr);
        }
    }

    private OnResultErrorListener onResultErrorListener;

    // 接口OnResultErrorListener
    public interface OnResultErrorListener {
        void resultError(FwError attr);
    }

    // 对外方式setOnResultErrorListener
    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    // 封装方法resultErrorNext
    private void resultErrorNext(FwError attr) {
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
     * 错误的普通做法(适合自定义)
     */
    private void allError() {
        if (onAllErrorListener != null) {
            onAllErrorListener.allError();
        }
    }

    /**
     * 请求接口前
     */
    private void onPrepare() {
        if (onBoardListener != null) {
            onBoardListener.onPrepare();
        }
    }

    /**
     * 请求接口成功后
     */
    private void onSuccessful() {
        if (onBoardListener != null) {
            onBoardListener.onSuccessful();
        }
    }

    /**
     * 请求接口中途错误
     */
    private void onResultErrors(FwError error) {
        if (onBoardListener != null) {
            onBoardListener.onResultErrors(error);
        }
    }

    /**
     * 请求接口错误溢出
     */
    private void onErrors(Throwable e) {
        if (onBoardListener != null) {
            onBoardListener.onErrors(e);
        }
    }

    public interface OnAllErrorListener {
        void allError();
    }


    public interface OnBoardListener {
        /**
         * 请求接口前
         */
        void onPrepare();

        /**
         * 请求成功
         */
        void onSuccessful();

        /**
         * 请求接口错误
         */
        void onResultErrors(FwError error);

        /**
         * 请求溢出
         */
        void onErrors(Throwable e);
    }
}
