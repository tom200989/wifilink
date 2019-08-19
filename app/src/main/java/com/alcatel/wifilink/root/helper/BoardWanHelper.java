package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.ue.root_activity.LoginRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.RefreshWifiRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetWanSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

/**
 * Created by qianli.ma on 2017/11/16 0016.
 */

public class BoardWanHelper {


    private Activity activity;
    private ProgressDialog pgd;
    private Handler handler;
    private OnNormalNextListener onNormalNextListener;
    private OnConnetedNextListener onConnetedNextListener;
    private OnDisConnetedNextListener onDisConnetedNextListener;
    private OnConnetingNextListener onConnetingNextListener;
    private OnDisconnetingNextListener onDisconnetingNextListener;
    private OnSendRequestSuccess onSendRequestSuccess;
    private OnSendRequestFailed onSendRequestFailed;
    private OnResultError onResultError;
    private OnError onError;
    private CheckBoard checkBoardClick;
    private CheckBoard checkBoardRoll;

    public BoardWanHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 点击事件调用此方法
     */
    public void boardNormal() {
        if (pgd == null) {
            pgd = OtherUtils.showProgressPop(activity);
        }
        if (!pgd.isShowing()) {
            pgd.show();
        }
        // 1.连接硬件
        if (checkBoardClick == null) {
            checkBoardClick = new CheckBoard() {
                @Override
                public void successful() {
                    // 2.获取状态
                    GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                    xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                        if (getLoginStateBean.getState() ==  GetLoginStateBean.CONS_LOGOUT) {
                            to(LoginRxActivity.class);
                            return;
                        }
                        // 3.WAN状态
                        obtainWanStatus();
                    });
                    xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
                        OtherUtils.hideProgressPop(pgd);
                        toast(R.string.connect_failed);
                        to(RefreshWifiRxActivity.class);
                    });
                    xGetLoginStateHelper.getLoginState();
                }
            };
        }
        checkBoardClick.checkBoard(activity, LoginRxActivity.class, RefreshWifiRxActivity.class);
    }

    /**
     * 定时器调用此方法
     */
    public void boardTimer() {
        // 1.连接硬件
        if (checkBoardRoll == null) {
            checkBoardRoll = new CheckBoard() {
                @Override
                public void successful() {
                    // 2.登陆状态
                    GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                    xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                        if (getLoginStateBean.getState() ==  GetLoginStateBean.CONS_LOGOUT) {
                            to(LoginRxActivity.class);
                            return;
                        }
                        // 3.WAN状态
                        obtainWanStatusRoll();
                    });
                    xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
                        resultErrorNext(null);
                        errorNext(null);
                    });
                    xGetLoginStateHelper.getLoginState();
                }
            };
        }
        checkBoardRoll.checkBoard(activity, LoginRxActivity.class, RefreshWifiRxActivity.class);
    }

    /**
     * 发送设置wan口请求
     *
     * @param wsp
     */
    public void sendWanRequest(SetWanSettingsParam wsp) {
        SetWanSettingsHelper xSetWanSettingsHelper = new SetWanSettingsHelper();
        xSetWanSettingsHelper.setOnSetWanSettingsSuccessListener(() -> {
            reGetWanStatus();// 重复获取WAN口状态
        });
        xSetWanSettingsHelper.setOnSetWanSettingsFailedListener(() -> {
            sendFailedNext();
        });
        xSetWanSettingsHelper.setWanSettings(wsp);
    }

    /**
     * 重复获取WAN口状态
     */
    private void reGetWanStatus() {

        GetWanSettingsHelper xGetWanSettingHelper = new GetWanSettingsHelper();
        xGetWanSettingHelper.setOnGetWanSettingsSuccessListener(getWanSettingsBean -> {
            int status = getWanSettingsBean.getStatus();
            if (status == GetWanSettingsBean.CONS_CONNECTED) {
                sendSuccessNext();
            } else if (status == GetWanSettingsBean.CONS_CONNECTING) {
                reGetWanStatus();
            } else {
                sendFailedNext();
            }
        });
        xGetWanSettingHelper.setOnGetWanSettingFailedListener(this::sendFailedNext);
        xGetWanSettingHelper.getWanSettings();
    }

    /**
     * 获取wan状态
     */
    private void obtainWanStatusRoll() {
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> resultErrorNext(null));
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
            normalNext(result);
            int status = result.getStatus();
            switch (status) {
                case GetWanSettingsBean.CONS_CONNECTED:
                    connectedNext(result);
                    break;
                case GetWanSettingsBean.CONS_CONNECTING:
                    connectingNext(result);
                    break;
                case GetWanSettingsBean.CONS_DISCONNECTED:
                    disconnectedNext(result);
                    break;
                case GetWanSettingsBean.CONS_DISCONNECTING:
                    disconnectingNext(result);
                    break;
            }
        });
        xGetWanSettingsHelper.getWanSettings();
    }

    /**
     * 获取wan状态
     */
    private void obtainWanStatus() {

        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            toast(R.string.check_your_wan_cabling);
            to(RefreshWifiRxActivity.class);
        });
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
            normalNext(result);
            int status = result.getStatus();
            switch (status) {
                case GetWanSettingsBean.CONS_CONNECTED:// 2
                    Log.v("ma_boardwan", "wan connected");
                    connectedNext(result);
                    break;
                case GetWanSettingsBean.CONS_CONNECTING:// 1
                    Log.v("ma_boardwan", "wan connecting");
                    connectingNext(result);
                    delayRepeatGetWanstatu();
                    break;
                case GetWanSettingsBean.CONS_DISCONNECTED:// 0
                    Log.v("ma_boardwan", "wan disconnected");
                    disconnectedNext(result);
                    break;
                case GetWanSettingsBean.CONS_DISCONNECTING:// 3
                    Log.v("ma_boardwan", "wan wan disconneting");
                    disconnectingNext(result);
                    break;
            }
            if (status != GetWanSettingsBean.CONS_CONNECTING) {
                OtherUtils.hideProgressPop(pgd);
            }
        });
        xGetWanSettingsHelper.getWanSettings();

    }

    /**
     * 延迟迭代请求
     */
    private void delayRepeatGetWanstatu() {
        handler = new Handler();
        handler.postDelayed(this::obtainWanStatus, 1000);
    }

    private void toast(int resId) {
        Log.v("ma_counld", getClass().getSimpleName());
        ToastUtil_m.show(activity, resId);
    }

    private void to(Class ac) {
        CA.toActivity(activity, ac, false, true, false, 0);
    }

    /* -------------------------------------------- INTERFACE -------------------------------------------- */

    public interface OnResultError {
        void resultError(FwError error);
    }

    public interface OnError {
        void error(Throwable e);
    }

    public interface OnSendRequestSuccess {
        void sendSuccess();
    }

    public interface OnSendRequestFailed {
        void sendFailed();
    }

    public interface OnNormalNextListener {
        void normalNext(GetWanSettingsBean wanResult);
    }

    public interface OnConnetedNextListener {
        void connectedNext(GetWanSettingsBean wanResult);
    }

    public interface OnDisConnetedNextListener {
        void disConnectedNext(GetWanSettingsBean wanResult);
    }

    public interface OnConnetingNextListener {
        void connectingNext(GetWanSettingsBean wanResult);
    }

    public interface OnDisconnetingNextListener {
        void disConnectingNext(GetWanSettingsBean wanResult);
    }

    /* -------------------------------------------- METHOD -------------------------------------------- */

    public void setOnResultError(OnResultError onResultError) {
        this.onResultError = onResultError;
    }

    public void setOnError(OnError onError) {
        this.onError = onError;
    }

    public void setOnSendRequestSuccess(OnSendRequestSuccess onSendRequestSuccess) {
        this.onSendRequestSuccess = onSendRequestSuccess;
    }

    public void setOnSendRequestFailed(OnSendRequestFailed onSendRequestFailed) {
        this.onSendRequestFailed = onSendRequestFailed;
    }

    public void setOnNormalNextListener(OnNormalNextListener onNormalNextListener) {
        this.onNormalNextListener = onNormalNextListener;
    }

    public void setOnConnetedNextListener(OnConnetedNextListener onConnetedNextListener) {
        this.onConnetedNextListener = onConnetedNextListener;
    }

    public void setOnDisConnetedNextListener(OnDisConnetedNextListener onDisConnetedNextListener) {
        this.onDisConnetedNextListener = onDisConnetedNextListener;
    }

    public void setOnConnetingNextListener(OnConnetingNextListener onConnetingNextListener) {
        this.onConnetingNextListener = onConnetingNextListener;
    }

    public void setOnDisconnetingNextListener(OnDisconnetingNextListener onDisconnetingNextListener) {
        this.onDisconnetingNextListener = onDisconnetingNextListener;
    }

    /* -------------------------------------------- USE -------------------------------------------- */

    private void resultErrorNext(FwError error) {
        if (onResultError != null) {
            onResultError.resultError(error);
        }
    }

    private void errorNext(Throwable e) {
        if (onError != null) {
            onError.error(e);
        }
    }

    private void sendSuccessNext() {
        if (onSendRequestSuccess != null) {
            onSendRequestSuccess.sendSuccess();
        }
    }

    private void sendFailedNext() {
        if (onSendRequestFailed != null) {
            onSendRequestFailed.sendFailed();
        }
    }

    private void normalNext(GetWanSettingsBean result) {
        if (onNormalNextListener != null) {
            onNormalNextListener.normalNext(result);
        }
    }

    private void connectedNext(GetWanSettingsBean result) {
        if (onConnetedNextListener != null) {
            onConnetedNextListener.connectedNext(result);
        }
    }

    private void disconnectedNext(GetWanSettingsBean result) {
        if (onDisConnetedNextListener != null) {
            onDisConnetedNextListener.disConnectedNext(result);
        }
    }

    private void connectingNext(GetWanSettingsBean result) {
        if (onConnetingNextListener != null) {
            onConnetingNextListener.connectingNext(result);
        }
    }

    private void disconnectingNext(GetWanSettingsBean result) {
        if (onDisconnetingNextListener != null) {
            onDisconnetingNextListener.disConnectingNext(result);
        }
    }
}
