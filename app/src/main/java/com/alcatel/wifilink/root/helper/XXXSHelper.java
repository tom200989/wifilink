package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.app.ProgressDialog;

import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.bean.SimStatus;
import com.alcatel.wifilink.root.bean.SmsInitState;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSStorageStateHelper;

/**
 * Created by qianli.ma on 2017/11/22 0022.
 */

public class XXXSHelper {

    private Activity activity;
    private OnResultErrorListener onResultErrorListener;
    private OnErrorListener onErrorListener;
    private OnInitingListener onInitingListener;
    private OnCompletListener onCompletListener;
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
                    boardSimHelper.setOnRollRequestOnResultError(error -> resultErrorNext(error));
                    boardSimHelper.setOnRollRequestOnError(e -> errorNext(e));
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
        RX.getInstant().getSmsInitState(new ResponseObject<SmsInitState>() {
            @Override
            protected void onSuccess(SmsInitState result) {
                int state = result.getState();
                if (state == Cons.SMS_COMPLETE) {
                    getSmsUnread();// 4. 获取未读短信数
                }
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
            resultErrorNext(null);
            errorNext(null);
        });
        xGetSMSStorageStateHelper.getSMSStorageState();
    }

    /* -------------------------------------------- interface -------------------------------------------- */

    public interface OnNownListener {
        void nown(SimStatus simStatus);
    }

    public interface OnUnreadListener {
        void unread(int unreadCount);
    }

    public interface OnInitingListener {
        void initing(SmsInitState result);
    }

    public interface OnCompletListener {
        void complete(SmsInitState result);
    }

    public interface OnResultErrorListener {
        void resultError(ResponseBody.Error error);
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

    public void setOnInitingListener(OnInitingListener onInitingListener) {
        this.onInitingListener = onInitingListener;
    }

    public void setOnCompletListener(OnCompletListener onCompletListener) {
        this.onCompletListener = onCompletListener;
    }

    public void setOnResultErrorListener(OnResultErrorListener onResultErrorListener) {
        this.onResultErrorListener = onResultErrorListener;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }

    /* -------------------------------------------- method -------------------------------------------- */

    private void nownNext(SimStatus simStatus) {
        onNownListener.nown(simStatus);
    }

    private void unreadNext(int unreadCount) {
        onUnreadListener.unread(unreadCount);
    }

    private void initingNext(SmsInitState result) {
        onInitingListener.initing(result);
    }

    private void completeNext(SmsInitState result) {
        onCompletListener.complete(result);
    }

    private void resultErrorNext(ResponseBody.Error error) {
        onResultErrorListener.resultError(error);
    }

    private void errorNext(Throwable e) {
        onErrorListener.error(e);
    }
}
