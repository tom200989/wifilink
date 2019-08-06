package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.User_LoginState;
import com.alcatel.wifilink.root.bean.WanSettingsParams;
import com.alcatel.wifilink.root.bean.WanSettingsResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.ue.activity.LoginRxActivity;
import com.alcatel.wifilink.root.ue.activity.RefreshWifiRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;

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
                    // 2.登陆状态
                    RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
                        @Override
                        protected void onSuccess(User_LoginState result) {
                            if (result.getState() == Cons.LOGOUT) {
                                Logs.t("check login").ii(getClass().getSimpleName() + ": line--> " + "70");
                                to(LoginRxActivity.class);
                                return;
                            }
                            // 3.WAN状态
                            obtainWanStatus();
                        }

                        @Override
                        protected void onResultError(ResponseBody.Error error) {
                            Log.v("ma_couldn_connect", "boardWanHelper getLoginState error: " + error.getMessage());
                            OtherUtils.hideProgressPop(pgd);
                            toast(R.string.connect_failed);
                            to(RefreshWifiRxActivity.class);
                            Logs.t("ma_unknown").vv("boardWanhelper--> boardNormal--> onResultError");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.v("ma_couldn_connect", "boardWanHelper getLoginState error: " + e.getMessage());
                            OtherUtils.hideProgressPop(pgd);
                            toast(R.string.connect_failed);
                            to(RefreshWifiRxActivity.class);
                            Logs.t("ma_unknown").vv("boardWanhelper--> boardNormal--> onError");
                        }
                    });
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
                    RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
                        @Override
                        protected void onSuccess(User_LoginState result) {
                            if (result.getState() == Cons.LOGOUT) {
                                Logs.t("check login").ii(getClass().getSimpleName() + ": line--> " + "116");
                                to(LoginRxActivity.class);
                                return;
                            }
                            // 3.WAN状态
                            obtainWanStatusRoll();
                        }

                        @Override
                        protected void onResultError(ResponseBody.Error error) {
                            resultErrorNext(error);
                        }

                        @Override
                        public void onError(Throwable e) {
                            errorNext(e);
                        }
                    });
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
    public void sendWanRequest(WanSettingsParams wsp) {
        RX.getInstant().setWanSettings(wsp, new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
                reGetWanStatus();// 重复获取WAN口状态
            }

            /**
             * 重复获取WAN口状态
             */
            private void reGetWanStatus() {

                // TODO: 2019/8/5 0005  新框架示例
                // GetWanSettingHelper wan = new GetWanSettingHelper();
                // wan.setOnGetWanSettingsSuccessListener(wanSettings -> {
                //     int status = wanSettings.getStatus();
                //     if (status == Cons.CONNECTED) {
                //         sendSuccessNext();
                //     } else if (status == Cons.CONNECTING) {
                //         reGetWanStatus();
                //     } else {
                //         sendFailedNext();
                //     }
                // });
                // wan.setOnGetwansettingsErrorListener(e -> sendFailedNext());
                // wan.setOnGetWanSettingsResultErrorListener(error -> sendFailedNext());
                // wan.setOnGetWanSettingsFailedListener(() -> sendFailedNext());
                // wan.get();

                GetWanSettingsHelper xGetWanSettingHelper = new GetWanSettingsHelper();
                xGetWanSettingHelper.setOnGetWanSettingsSuccessListener(getWanSettingsBean -> {
                    int status = getWanSettingsBean.getStatus();
                    if (status == Cons.CONNECTED) {
                        sendSuccessNext();
                    } else if (status == Cons.CONNECTING) {
                        reGetWanStatus();
                    } else {
                        sendFailedNext();
                    }
                });
                xGetWanSettingHelper.setOnGetWanSettingsFailedListener(() -> sendFailedNext());
                xGetWanSettingHelper.getWanSettings();
            }


            @Override
            protected void onResultError(ResponseBody.Error error) {
                sendFailedNext();
            }

            @Override
            public void onError(Throwable e) {
                sendFailedNext();
            }
        });
    }

    /**
     * 获取wan状态
     */
    private void obtainWanStatusRoll() {

        GetWanSettingHelper wan = new GetWanSettingHelper();
        wan.setOnGetwansettingsErrorListener(this::errorNext);
        wan.setOnGetWanSettingsFailedListener(() -> errorNext(null));
        wan.setOnGetWanSettingsResultErrorListener(this::resultErrorNext);
        wan.setOnGetWanSettingsSuccessListener(result -> {
            normalNext(result);
            int status = result.getStatus();
            switch (status) {
                case Cons.CONNECTED:
                    connectedNext(result);
                    break;
                case Cons.CONNECTING:
                    connectingNext(result);
                    break;
                case Cons.DISCONNECTED:
                    disconnectedNext(result);
                    break;
                case Cons.DISCONNECTING:
                    disconnectingNext(result);
                    break;
            }
        });
        wan.get();
    }

    /**
     * 获取wan状态
     */
    private void obtainWanStatus() {
        
        GetWanSettingHelper wan = new GetWanSettingHelper();
        wan.setOnGetwansettingsErrorListener(e -> {
            Log.v("ma_couldn_connect", "boardWanHelper obtainWanStatus error: " + e.getMessage());
            OtherUtils.hideProgressPop(pgd);
            toast(R.string.connect_failed);
            to(RefreshWifiRxActivity.class);
            Logs.t("ma_unknown").vv("boardWanhelper--> obtainWanStatus--> onError");
        });
        wan.setOnGetWanSettingsFailedListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            toast(R.string.connect_failed);
            to(RefreshWifiRxActivity.class);
            Logs.t("ma_unknown").vv("boardWanhelper--> obtainWanStatus--> onError");
        });
        wan.setOnGetWanSettingsResultErrorListener(error -> {
            OtherUtils.hideProgressPop(pgd);
            toast(R.string.check_your_wan_cabling);
            to(RefreshWifiRxActivity.class);
            Logs.t("ma_unknown").vv("boardWanhelper--> obtainWanStatus--> onResultError");
        });
        wan.setOnGetWanSettingsSuccessListener(result -> {
            normalNext(result);
            int status = result.getStatus();
            switch (status) {
                case Cons.CONNECTED:// 2
                    Log.v("ma_boardwan", "wan connected");
                    connectedNext(result);
                    break;
                case Cons.CONNECTING:// 1
                    Log.v("ma_boardwan", "wan connecting");
                    connectingNext(result);
                    delayRepeatGetWanstatu();
                    break;
                case Cons.DISCONNECTED:// 0
                    Log.v("ma_boardwan", "wan disconnected");
                    disconnectedNext(result);
                    break;
                case Cons.DISCONNECTING:// 3
                    Log.v("ma_boardwan", "wan wan disconneting");
                    disconnectingNext(result);
                    break;
            }
            if (status != Cons.CONNECTING) {
                OtherUtils.hideProgressPop(pgd);
            }
        });
        wan.get();
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
        void resultError(ResponseBody.Error error);
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
        void normalNext(WanSettingsResult wanResult);
    }

    public interface OnConnetedNextListener {
        void connectedNext(WanSettingsResult wanResult);
    }

    public interface OnDisConnetedNextListener {
        void disConnectedNext(WanSettingsResult wanResult);
    }

    public interface OnConnetingNextListener {
        void connectingNext(WanSettingsResult wanResult);
    }

    public interface OnDisconnetingNextListener {
        void disConnectingNext(WanSettingsResult wanResult);
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

    private void resultErrorNext(ResponseBody.Error error) {
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

    private void normalNext(WanSettingsResult result) {
        if (onNormalNextListener != null) {
            onNormalNextListener.normalNext(result);
        }
    }

    private void connectedNext(WanSettingsResult result) {
        if (onConnetedNextListener != null) {
            onConnetedNextListener.connectedNext(result);
        }
    }

    private void disconnectedNext(WanSettingsResult result) {
        if (onDisConnetedNextListener != null) {
            onDisConnetedNextListener.disConnectedNext(result);
        }
    }

    private void connectingNext(WanSettingsResult result) {
        if (onConnetingNextListener != null) {
            onConnetingNextListener.connectingNext(result);
        }
    }

    private void disconnectingNext(WanSettingsResult result) {
        if (onDisconnetingNextListener != null) {
            onDisconnetingNextListener.disConnectingNext(result);
        }
    }
}
