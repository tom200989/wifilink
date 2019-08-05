package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.User_LoginState;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.ue.activity.LoginRxActivity;
import com.alcatel.wifilink.root.ue.activity.RefreshWifiRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;

/**
 * 检测是否连接上硬件并处于登陆状态
 */
public abstract class CheckBoardLogin {
    public static int DEFAULT = 0;
    public static int TIMER = 1;
    private Activity activity;
    private ProgressDialog pgd;
    private CheckBoard checkBoardTimer;
    private CheckBoard checkBoardClick;

    public abstract void afterCheckSuccess(ProgressDialog pop);

    public CheckBoardLogin(Activity activity) {
        this.activity = activity;
        initCheckOne();// 仅请求一次就使用该方法
    }

    public CheckBoardLogin(Activity activity, int flag) {
        this.activity = activity;
    }

    /**
     * 定时器模式(手动调用)
     */
    public void initCheckTimer() {
        // 1.连接硬件
        if (checkBoardTimer == null) {
            checkBoardTimer = new CheckBoard() {
                @Override
                public void successful() {
                    // 2.登陆状态
                    RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
                        @Override
                        protected void onSuccess(User_LoginState result) {
                            if (result.getState() == Cons.LOGOUT) {
                                Logs.t("check login").ii(getClass().getSimpleName() + ": line--> " + "56");
                                to(LoginRxActivity.class);
                                return;
                            }
                            // 3.连接成功后
                            afterCheckSuccess(pgd);
                        }

                        @Override
                        protected void onResultError(ResponseBody.Error error) {
                            // 3.错误--> wifi界面
                            to(RefreshWifiRxActivity.class);
                            Logs.t("ma_unknown").vv("CheckBoardLogin--> initCheckTimer--> onResultError");
                        }

                        @Override
                        public void onError(Throwable e) {
                            // 3.错误--> wifi界面
                            to(RefreshWifiRxActivity.class);
                            Logs.t("ma_unknown").vv("CheckBoardLogin--> initCheckTimer--> onError");
                        }
                    });
                }
            };
        }
        checkBoardTimer.checkBoard(activity, LoginRxActivity.class, RefreshWifiRxActivity.class);
    }


    /**
     * 单次模式
     */
    private void initCheckOne() {
        if (pgd == null) {
            pgd = OtherUtils.showProgressPop(activity);
        }
        if (!pgd.isShowing()) {
            pgd.show();
        }
        if (checkBoardClick == null) {
            // 1.连接硬件
            checkBoardClick = new CheckBoard() {
                @Override
                public void successful() {
                    // 2.登陆状态
                    RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {
                        @Override
                        protected void onSuccess(User_LoginState result) {
                            if (result.getState() == Cons.LOGOUT) {
                                Logs.t("check login").ii(getClass().getSimpleName() + ": line--> " + "105");
                                to(LoginRxActivity.class);
                                return;
                            }
                            // 3.连接成功后
                            afterCheckSuccess(pgd);
                            OtherUtils.hideProgressPop(pgd);
                        }

                        @Override
                        protected void onResultError(ResponseBody.Error error) {
                            Log.v("ma_couldn_connect", "checkBoardLogin initCheckOne error: " + error.getMessage());
                            OtherUtils.hideProgressPop(pgd);
                            toast(R.string.connect_failed);
                            to(RefreshWifiRxActivity.class);
                            Logs.t("ma_unknown").vv("CheckBoardLogin--> initCheckOne--> onResultError");
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.v("ma_couldn_connect", "checkBoardLogin initCheckOne error: " + e.getMessage());
                            OtherUtils.hideProgressPop(pgd);
                            toast(R.string.connect_failed);
                            to(RefreshWifiRxActivity.class);
                            Logs.t("ma_unknown").vv("CheckBoardLogin--> initCheckOne--> onError");
                        }
                    });
                }
            };
        }
        checkBoardClick.checkBoard(activity, LoginRxActivity.class, RefreshWifiRxActivity.class);
    }


    public void toast(int resId) {
        Log.v("ma_counld", getClass().getSimpleName());
        ToastUtil_m.show(activity, resId);
    }

    private void to(Class clazz) {
        CA.toActivity(activity, clazz, false, true, false, 0);
    }

}
