package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.content.Intent;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.ToastTool;
import com.hiber.bean.SkipBean;
import com.hiber.hiber.RootMAActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;

/**
 * Created by qianli.ma on 2017/12/11 0011.
 */

public class MobileDataHelper {

    private Activity context;

    public MobileDataHelper(Activity context) {
        this.context = context;
    }

    /**
     * 连接或者断连
     */
    public void toConnOrNot() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(RootCons.ACTIVITYS.SPLASH_AC,RootCons.FRAG.LOGIN_FR);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_PIN_REQUIRED:
                    case GetSimStatusBean.CONS_PUK_REQUIRED:
                    case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                        toast(R.string.hh70_verify_your_pin);
                        break;
                    case  GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
                        toast(R.string.hh70_initializing);
                        break;
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        toast(R.string.hh70_puk_timeout);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                        toast(R.string.hh70_sim_card_deleted);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_READY:
                        getConnectState();
                        break;
                    case GetSimStatusBean.CONS_NOWN:
                        toast(R.string.hh70_no_sim_insert);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_ILLEGAL:
                        toast(R.string.hh70_invalid_sim);
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                toast(R.string.hh70_sim_not_accessible);
                to(RootCons.ACTIVITYS.SPLASH_AC,RootCons.FRAG.REFRESH_FR);
            });
            xGetSimStatusHelper.getSimStatus();
        });

        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
            toast(R.string.hh70_connect_failed);
            to(RootCons.ACTIVITYS.SPLASH_AC,RootCons.FRAG.REFRESH_FR);
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 获取连接状态
     */
    private void getConnectState() {
        // 2.获取连接状态(断连--> 连接,连接--> 断连)
        GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> toast(R.string.hh70_connect_failed));
        xGetConnectionStateHelper.setOnDisConnectingListener(() -> toast(R.string.hh70_disconn_try_again));
        xGetConnectionStateHelper.setOnDisconnectedListener(this::toConn);
        xGetConnectionStateHelper.setOnConnectingListener(() -> toast(R.string.hh70_connecting));
        xGetConnectionStateHelper.setOnConnectedListener(this::toDisConn);
        xGetConnectionStateHelper.getConnectionState();
    }

    /**
     * 跳转activity
     *
     * @param targetAc
     * @param targetFr
     */
    private void to(String targetAc, String targetFr) {
        Intent intent = new Intent();
        intent.setAction(targetAc);
        SkipBean skipBean = RootMAActivity.getPendingIntentValue(targetAc, targetFr, null);
        skipBean.setCurrentACFinish(true);
        intent.putExtra(RootMAActivity.getPendingIntentKey(), skipBean);
        context.startActivity(intent);
    }

    /**
     * 断连
     */
    private void toDisConn() {
        ConnectSettingHelper csDisconnhelper = new ConnectSettingHelper();
        // 注册产生的错误
        csDisconnhelper.setOnRegisterErrorListener(attr -> toast(R.string.hh70_connect_failed));
        csDisconnhelper.setOnRegisterResultErrorListener(attr -> toast(R.string.hh70_connect_failed));
        csDisconnhelper.setOnRegisteFailedListener(attr -> toast(R.string.hh70_connect_failed));
        csDisconnhelper.setOnNotRegisterListener(attr -> toast(R.string.hh70_connect_failed));
        csDisconnhelper.setOnRegistingListener(attr -> toast(R.string.hh70_connecting));
        // 断连成功
        csDisconnhelper.setOnDisConnSuccessListener(this::disConnSuccessNext);
        // 连接产生的错误
        csDisconnhelper.setOnConnectSettingFailListener(() -> checkMonthly());
        csDisconnhelper.toDisConnect();
    }

    /**
     * 连接
     */
    private void toConn() {
        ConnectSettingHelper csConnhelper = new ConnectSettingHelper();
        // 注册产生的错误
        csConnhelper.setOnRegisterErrorListener(attr -> toast(R.string.hh70_connect_failed));
        csConnhelper.setOnRegisterResultErrorListener(attr -> toast(R.string.hh70_connect_failed));
        csConnhelper.setOnRegisteFailedListener(attr -> toast(R.string.hh70_connect_failed));
        csConnhelper.setOnNotRegisterListener(attr -> toast(R.string.hh70_connect_failed));
        csConnhelper.setOnRegistingListener(attr -> toast(R.string.hh70_connecting));
        // 连接成功
        csConnhelper.setOnConnSuccessListener(this::connSuccessNext);
        // 连接产生的错误--> 检测是否超出了流量
        csConnhelper.setOnConnectSettingFailListener(() -> checkMonthly());
        csConnhelper.toConnect();
    }

    /**
     * 检查是否超出流量
     */
    private void checkMonthly() {
        UsageHelper usageHelper = new UsageHelper(context);
        usageHelper.setOnGetUsageErrorListener(() -> toast(R.string.hh70_connect_failed));
        usageHelper.setOnOverMonthlyListener(() -> toast(R.string.hh70_month_data_limit));// 超出流量
        usageHelper.getOverUsage();
    }

    private OnDisConnSuccessListener onDisConnSuccessListener;

    // 接口OnDisConnSuccessListener
    public interface OnDisConnSuccessListener {
        void disConnSuccess(Object attr);
    }

    // 对外方式setOnDisConnSuccessListener
    public void setOnDisConnSuccessListener(OnDisConnSuccessListener onDisConnSuccessListener) {
        this.onDisConnSuccessListener = onDisConnSuccessListener;
    }

    // 封装方法disConnSuccessNext
    private void disConnSuccessNext(Object attr) {
        if (onDisConnSuccessListener != null) {
            onDisConnSuccessListener.disConnSuccess(attr);
        }
    }

    private OnConnSuccessListener onConnSuccessListener;

    // 接口OnConnSuccessListener
    public interface OnConnSuccessListener {
        void connSuccess(Object attr);
    }

    // 对外方式setOnConnSuccessListener
    public void setOnConnSuccessListener(OnConnSuccessListener onConnSuccessListener) {
        this.onConnSuccessListener = onConnSuccessListener;
    }

    // 封装方法connSuccessNext
    private void connSuccessNext(Object attr) {
        if (onConnSuccessListener != null) {
            onConnSuccessListener.connSuccess(attr);
        }
    }

    public void toast(int resId) {
        ToastTool.show(context, resId);
    }

    public void toast(String toastString) {
        ToastTool.show(context, toastString);
    }
}
