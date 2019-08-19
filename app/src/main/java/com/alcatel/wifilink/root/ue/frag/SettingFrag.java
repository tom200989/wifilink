package com.alcatel.wifilink.root.ue.frag;

import android.view.View;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.ClickDoubleHelper;
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class SettingFrag extends BaseFrag {
    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_setting;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
    }

    @Override
    public boolean onBackPresss() {

        // 登出
        ClickDoubleHelper clickDouble = new ClickDoubleHelper();
        clickDouble.setOnClickOneListener(() -> toast(R.string.home_exit_app, 3000));
        clickDouble.setOnClickDoubleListener(this::logOut);
        clickDouble.click();
        return true;
    }

    /**
     * 登出
     */
    private void logOut() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                LogoutHelper xLogoutHelper = new LogoutHelper();
                xLogoutHelper.setOnLogoutSuccessListener(() -> {
                    toast(R.string.login_logout_successful, 3000);
                    toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
                });
                xLogoutHelper.setOnLogOutFailedListener(() -> toast(R.string.login_logout_failed, 3000));
                xLogoutHelper.logout();
            } else {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.getLoginState();
    }
}
