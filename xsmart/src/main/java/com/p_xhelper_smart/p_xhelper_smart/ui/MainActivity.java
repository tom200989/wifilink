package com.p_xhelper_smart.p_xhelper_smart.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.p_xhelper_smart.p_xhelper_smart.R;
import com.p_xhelper_smart.p_xhelper_smart.core.XSmart;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.HeartBeatHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LoginHelper;
import com.p_xhelper_smart.p_xhelper_smart.utils.XCons;
import com.p_xhelper_smart.p_xhelper_smart.utils.Logg;
import com.p_xhelper_smart.p_xhelper_smart.utils.TimerHelper;

public class MainActivity extends AppCompatActivity {

    String TAG = "x_ma_qianli";

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_activity_main);
        // 不带返回参数
        XSmart.init(this);

        // 登陆
        Logg.t("ma_test_ui").ii("login begin");
        LoginHelper loginHelper = new LoginHelper();
        loginHelper.setOnLoginSuccesListener(() -> {
            Logg.t("ma_test_ui").ii("login success start timer");
            TimerHelper timerHelper = new TimerHelper(this) {
                @Override
                public void doSomething() {
                    HeartBeatHelper heartBeatHelper = new HeartBeatHelper();
                    heartBeatHelper.heartbeat();

                    GetSimStatusHelper getSimStatusHelper = new GetSimStatusHelper();
                    getSimStatusHelper.getSimStatus();
                }
            };
            timerHelper.start(3000);

        });
        loginHelper.setOnLoginFailedListener(() -> {
            Logg.t("ma_test_ui").ee("login failed");
        });
        loginHelper.setOnLoginOutTimeListener(() -> {
            Logg.t("ma_test_ui").ee("login out times");
        });
        loginHelper.login(XCons.ACCOUNT, "admin");

    }
}
