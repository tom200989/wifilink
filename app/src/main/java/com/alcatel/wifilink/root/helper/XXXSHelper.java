package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSStorageStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSmsInitStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

/**
 * Created by qianli.ma on 2017/11/22 0022.
 */

public class XXXSHelper {

    private Activity activity;
    private BoardSimHelper boardSimHelper;
    private CheckBoardLogin checkBoardLogin;
    private OnUnreadListener onUnreadListener;
    private OnNownListener onNownListener;

    public XXXSHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取未读
     */
    public void getUnread() {
        // 1.检测硬件是否连接
        if (checkBoardLogin == null) {
            checkBoardLogin = new CheckBoardLogin(activity, 0) {
                @Override
                public void afterCheckSuccess(ProgressDialog pop) {
                    // 2.获取sim卡状态
                    if (boardSimHelper == null) {
                        boardSimHelper = new BoardSimHelper(activity);
                    }
                    // sim卡准备好了之后进行初始化sms接口
                    boardSimHelper.setOnSimReadyListener(result -> getSmsStatus());
                    boardSimHelper.setOnNownListener(simStatus -> nownNext(simStatus));
                    boardSimHelper.boardTimer();
                }
            };
        }
        checkBoardLogin.initCheckTimer();
    }

    /**
     * 获取sim初始化状态
     */
    private void getSmsStatus() {

        // 3.获取sim初始化状态
        GetSmsInitStateHelper xGetSmsInitStateHelper = new GetSmsInitStateHelper();
        xGetSmsInitStateHelper.setOnGetSmsInitStateSuccessListener(bean -> {
            if (bean.getState() == GetSmsInitStateHelper.SMS_COMPLETE) {
                // getInstant sms contents
                getSmsUnread();// 4. 获取未读短信数
            }
        });
        xGetSmsInitStateHelper.setOnGetSmsInitStateFailListener(() -> {
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    /**
     * 获取未读短信数
     */
    private void getSmsUnread() {

        GetSMSStorageStateHelper xGetSMSStorageStateHelper = new GetSMSStorageStateHelper();
        xGetSMSStorageStateHelper.setOnGetSMSStoreStateSuccessListener(stateBean -> {
            int unreadSMSCount = stateBean.getUnreadSMSCount();
            unreadNext(unreadSMSCount);
        });
        xGetSMSStorageStateHelper.setOnGetSMSStoreStateFailedListener(() -> {
        });
        xGetSMSStorageStateHelper.getSMSStorageState();
    }

    /* -------------------------------------------- interface -------------------------------------------- */

    public interface OnNownListener {
        void nown(GetSimStatusBean simStatus);
    }

    public interface OnUnreadListener {
        void unread(int unreadCount);
    }

    public interface OnResultErrorListener {
        void resultError(FwError error);
    }

    public interface OnErrorListener {
        void error(Throwable e);
    }

    /* -------------------------------------------- method -------------------------------------------- */

    public void setOnNownListener(OnNownListener onNownListener) {
        this.onNownListener = onNownListener;
    }

    public void setOnUnreadListener(OnUnreadListener onUnreadListener) {
        this.onUnreadListener = onUnreadListener;
    }

    /* -------------------------------------------- method -------------------------------------------- */

    private void nownNext(GetSimStatusBean simStatus) {
        onNownListener.nown(simStatus);
    }

    private void unreadNext(int unreadCount) {
        onUnreadListener.unread(unreadCount);
    }

}
