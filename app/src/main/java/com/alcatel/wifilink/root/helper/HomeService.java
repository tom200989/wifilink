package com.alcatel.wifilink.root.helper;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

/**
 * Created by qianli.ma on 2017/8/10.
 */

public class HomeService extends Service {
    private TimerHelper packageTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        packageTimer = new TimerHelper(this) {
            @Override
            public void doSomething() {
                // TOGO 2018/1/11 0011 获取当前运行的包集合是否包含当前APP的包名(该操作在android6.0已过时)
                // getCurrentPackage();

                /* 推荐使用该方式, 通过获取当前运行的服务是否包含了HomeRxActivity绑定的服务 */
                // 1.检测HomeRxActivity绑定的服务是否正在运行
                boolean checkServiceRunning = isCheckServiceRunning();
                // 2.如果绑定的服务不再运行
                if (!checkServiceRunning) {
                    // 3.停止服务--> 4.停止服务的onDestroy方法里登出
                    if (intent != null) {
                        stopService(intent);
                    }
                }
            }
        };
        packageTimer.start(500);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (packageTimer != null) {
            packageTimer.stop();
        }
        Logs.t("ma_service").vv("homeservice onDestroy");
        logout();// 4.停止服务的onDestroy方法里登出
    }

    private boolean isCheckServiceRunning() {
        return OtherUtils.isServiceWork(SmartLinkV3App.getInstance(), CheckService.class);
    }

    private void logout() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            OtherUtils.clearAllTimer();
            if (getLoginStateBean.getState() ==  GetLoginStateBean.CONS_LOGOUT) {
                LogoutHelper xLogoutHelper = new LogoutHelper();
                xLogoutHelper.logout();
            }
        });
        xGetLoginStateHelper.getLoginState();
    }
}
