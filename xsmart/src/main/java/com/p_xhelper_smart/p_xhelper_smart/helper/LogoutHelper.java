package com.p_xhelper_smart.p_xhelper_smart.helper;

import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;
import com.p_xhelper_smart.p_xhelper_smart.impl.XNormalCallback;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;

/*
 * Created by qianli.ma on 2019/7/30 0030.
 */
@SuppressWarnings("unchecked")
public class LogoutHelper extends BaseHelper {

    private int count = 0;// 辅助计数器

    /**
     * 登出
     */
    public void logout() {
        count = 0;
        prepareHelperNext();
        // 先获取登陆状态
        GetLoginStateHelper loginStateHelper = new GetLoginStateHelper();
        loginStateHelper.setOnGetLoginStateSuccessListener(loginStateBean -> {
            int state = loginStateBean.getState();
            if (state == GetLoginStateBean.CONS_LOGIN) {// 处于登陆状态 -- 去登出
                toLogout();

            } else if (state == GetLoginStateBean.CONS_LOGOUT) {// 已经是登出状态 -- 登出成功
                logoutSuccessNext();
            }
        });

        loginStateHelper.setOnGetLoginStateFailedListener(this::logoutFailed);
        loginStateHelper.getLoginState();
    }

    /**
     * 登出
     */
    private void toLogout() {
        XSmart xLogout = new XSmart();
        xLogout.xMethod(XCons.METHOD_LOGOUT).xPost(new XNormalCallback() {
            @Override
            public void success(Object result) {
                // 再获取登陆状态 -- 确保已经登出
                getLoginStateAgain();
            }

            @Override
            public void appError(Throwable ex) {
                logoutFailed();
            }

            @Override
            public void fwError(FwError fwError) {
                logoutFailed();
            }

            @Override
            public void finish() {

            }
        });
    }

    /**
     * 再获取登陆状态 -- 确保已经登出
     */
    private void getLoginStateAgain() {
        GetLoginStateHelper loginStateHelper = new GetLoginStateHelper();
        loginStateHelper.setOnGetLoginStateSuccessListener(loginStateBean -> {
            int state = loginStateBean.getState();
            if (state == GetLoginStateBean.CONS_LOGOUT) {
                logoutSuccessNext();
                doneHelperNext();

            } else {
                if (count < 5) {
                    getLoginStateAgain();
                    count++;
                } else {
                    logoutFailed();
                }
            }
        });

        loginStateHelper.setOnGetLoginStateFailedListener(this::logoutFaildNext);
        loginStateHelper.getLoginState();

    }

    /**
     * 登出失败后的处理
     */
    private void logoutFailed() {
        count = 0;
        logoutFaildNext();
        doneHelperNext();
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnLogoutSuccessListener onLogoutSuccessListener;

    // Inteerface--> 接口OnLogoutSuccessListener
    public interface OnLogoutSuccessListener {
        void logoutSuccess();
    }

    // 对外方式setOnLogoutSuccessListener
    public void setOnLogoutSuccessListener(OnLogoutSuccessListener onLogoutSuccessListener) {
        this.onLogoutSuccessListener = onLogoutSuccessListener;
    }

    // 封装方法logoutSuccessNext
    private void logoutSuccessNext() {
        if (onLogoutSuccessListener != null) {
            onLogoutSuccessListener.logoutSuccess();
        }
    }

    private OnLogOutFailedListener onLogOutFailedListener;

    // Inteerface--> 接口OnLogOutFailedListener
    public interface OnLogOutFailedListener {
        void logoutFaild();
    }

    // 对外方式setOnLogOutFailedListener
    public void setOnLogOutFailedListener(OnLogOutFailedListener onLogOutFailedListener) {
        this.onLogOutFailedListener = onLogOutFailedListener;
    }

    // 封装方法logoutFaildNext
    private void logoutFaildNext() {
        if (onLogOutFailedListener != null) {
            onLogOutFailedListener.logoutFaild();
        }
    }
}
