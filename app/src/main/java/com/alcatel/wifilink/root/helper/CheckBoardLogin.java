package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.ue.activity.LoginRxActivity;
import com.alcatel.wifilink.root.ue.activity.RefreshWifiRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;

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
                    GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                    xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                        if (getLoginStateBean.getState() == Cons.LOGOUT) {
                            to(LoginRxActivity.class);
                            return;
                        }
                        // 3.连接成功后
                        afterCheckSuccess(pgd);
                    });
                    xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> to(RefreshWifiRxActivity.class));
                    xGetLoginStateHelper.getLoginState();

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
                    GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
                    xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
                        if (getLoginStateBean.getState() == Cons.LOGOUT) {
                            to(LoginRxActivity.class);
                            return;
                        }
                        // 3.连接成功后
                        afterCheckSuccess(pgd);
                        OtherUtils.hideProgressPop(pgd);
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


    public void toast(int resId) {
        Log.v("ma_counld", getClass().getSimpleName());
        ToastUtil_m.show(activity, resId);
    }

    private void to(Class clazz) {
        CA.toActivity(activity, clazz, false, true, false, 0);
    }

}
