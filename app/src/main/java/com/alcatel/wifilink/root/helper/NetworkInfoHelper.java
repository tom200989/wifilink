package com.alcatel.wifilink.root.helper;

import android.app.Activity;
import android.content.Context;

import com.alcatel.wifilink.R;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkRegisterStateHelper;

/**
 * Created by qianli.ma on 2017/11/24 0024.
 */

public abstract class NetworkInfoHelper {
    private Activity activity;

    public NetworkInfoHelper(Activity activity) {
        this.activity = activity;
    }

    /**
     * 获取2G\3G\4G
     *
     * @param context
     * @param type
     * @return
     */
    public String getSignalType(Context context, int type) {
        if (type == GetNetworkInfoBean.CONS_NO_SERVER) {
            return "- -";
        } else if (type >= GetNetworkInfoBean.CONS_GPRS && type <= GetNetworkInfoBean.CONS_EDGE) {
            return context.getString(R.string.hh70_2g);
        } else if (type >= GetNetworkInfoBean.CONS_HSPA && type <= GetNetworkInfoBean.CONS_HSPA_PLUS) {
            return context.getString(R.string.hh70_3g);
        } else if (type >= GetNetworkInfoBean.CONS_LTE && type <= GetNetworkInfoBean.CONS_LTE_PLUS) {
            return context.getString(R.string.hh70_4g);
        } else if (type >= GetNetworkInfoBean.CONS_CDMA && type <= GetNetworkInfoBean.CONS_GSM) {
            return context.getString(R.string.hh70_2g);
        } else if (type == GetNetworkInfoBean.CONS_EVDO) {
            return context.getString(R.string.hh70_3g);
        } else if (type >= GetNetworkInfoBean.CONS_LTE_TDD && type <= GetNetworkInfoBean.CONS_LTE_FDD) {
            return context.getString(R.string.hh70_4g);
        } else if (type == GetNetworkInfoBean.CONS_CDMA_EHRPD) {
            return context.getString(R.string.hh70_3g_plus);
        } else {
            return "- -";
        }
    }

    /**
     * 获取network对象
     */
    public void get() {
        GetNetworkRegisterStateHelper xGetNetworkRegisterStateHelper = new GetNetworkRegisterStateHelper();
        xGetNetworkRegisterStateHelper.setOnRegisterSuccessListener(this::getNetworkInfo);
        xGetNetworkRegisterStateHelper.setOnNotRegisterListener(this::noRegister);
        xGetNetworkRegisterStateHelper.setOnGetNetworkRegisterStateFailedListener(this::noRegister);
        xGetNetworkRegisterStateHelper.setOnRegisttingListener(this::noRegister);
        xGetNetworkRegisterStateHelper.getNetworkRegisterState();
    }

    /**
     * 获取network信息
     */
    private void getNetworkInfo() {
        GetNetworkInfoHelper xGetNetworkInfoHelper = new GetNetworkInfoHelper();
        xGetNetworkInfoHelper.setOnGetNetworkInfoSuccessListener(this::register);
        xGetNetworkInfoHelper.setOnGetNetworkInfoFailedListener(this::noRegister);
        xGetNetworkInfoHelper.getNetworkInfo();
    }

    public abstract void noRegister();

    public abstract void register(GetNetworkInfoBean result);
}
