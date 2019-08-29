package com.alcatel.wifilink.root.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSmsInitStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSMSStorageStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSmsInitStateHelper;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class GetSMSUnreadHelper {

    public void getSMSUnread() {
        // 1.先获取登陆状态
        getLoginState();
    }

    /**
     * 1.先获取登陆状态
     */
    private void getLoginState() {
        GetLoginStateHelper XGetLoginStateHelper = new GetLoginStateHelper();
        XGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                // 2.获取SIM卡状态
                getSMSState();
            } else {
                GetSMSUnreadFailedNext();
            }
        });
        XGetLoginStateHelper.getLoginState();
    }

    /**
     * 2.获取SIM卡状态
     */
    private void getSMSState() {
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
            if (getSimStatusBean.getSIMState() == GetSimStatusBean.CONS_SIM_CARD_READY) {
                // 3.获取短信初始化状态
                getSMSInitState();
            } else {
                GetSMSUnreadFailedNext();
            }
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /**
     * 3.获取短信初始化状态
     */
    private void getSMSInitState() {
        GetSmsInitStateHelper xGetSmsInitStateHelper = new GetSmsInitStateHelper();
        xGetSmsInitStateHelper.setOnGetSmsInitStateSuccessListener(getSmsInitStateBean -> {
            if (getSmsInitStateBean.getState() == GetSmsInitStateBean.CONS_SMS_INIT_STATUS_COMPLETE) {
                // 4.获取短息未读数
                getSMSUnreadCount();
            } else {
                GetSMSUnreadFailedNext();
            }
        });
        xGetSmsInitStateHelper.getSmsInitState();
    }

    /**
     * 4.获取短息未读数
     */
    private void getSMSUnreadCount() {
        GetSMSStorageStateHelper xGetSMSStorageStateHelper = new GetSMSStorageStateHelper();
        xGetSMSStorageStateHelper.setOnGetSMSStoreStateSuccessListener(stateBean -> GetSMSUnreadSuccessNext(stateBean.getUnreadSMSCount()));
        xGetSMSStorageStateHelper.setOnGetSMSStoreStateFailedListener(this::GetSMSUnreadFailedNext);
        xGetSMSStorageStateHelper.getSMSStorageState();
    }

    private OnGetSMSUnreadSuccessListener onGetSMSUnreadSuccessListener;

    // Inteerface--> 接口OnGetSMSUnreadSuccessListener
    public interface OnGetSMSUnreadSuccessListener {
        void GetSMSUnreadSuccess(int count);
    }

    // 对外方式setOnGetSMSUnreadSuccessListener
    public void setOnGetSMSUnreadSuccessListener(OnGetSMSUnreadSuccessListener onGetSMSUnreadSuccessListener) {
        this.onGetSMSUnreadSuccessListener = onGetSMSUnreadSuccessListener;
    }

    // 封装方法GetSMSUnreadSuccessNext
    private void GetSMSUnreadSuccessNext(int count) {
        if (onGetSMSUnreadSuccessListener != null) {
            onGetSMSUnreadSuccessListener.GetSMSUnreadSuccess(count);
        }
    }

    private OnGetSMSUnreadFailedListener onGetSMSUnreadFailedListener;

    // Inteerface--> 接口OnGetSMSUnreadFailedListener
    public interface OnGetSMSUnreadFailedListener {
        void GetSMSUnreadFailed();
    }

    // 对外方式setOnGetSMSUnreadFailedListener
    public void setOnGetSMSUnreadFailedListener(OnGetSMSUnreadFailedListener onGetSMSUnreadFailedListener) {
        this.onGetSMSUnreadFailedListener = onGetSMSUnreadFailedListener;
    }

    // 封装方法GetSMSUnreadFailedNext
    private void GetSMSUnreadFailedNext() {
        if (onGetSMSUnreadFailedListener != null) {
            onGetSMSUnreadFailedListener.GetSMSUnreadFailed();
        }
    }

}
