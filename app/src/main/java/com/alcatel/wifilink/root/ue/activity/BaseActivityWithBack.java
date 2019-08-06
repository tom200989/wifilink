package com.alcatel.wifilink.root.ue.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.utils.AppInfo;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.C_Constants;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.PreferenceUtil;
import com.alcatel.wifilink.root.utils.SPUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class BaseActivityWithBack extends AppCompatActivity {

    public static boolean isFreeSharingLock = true;
    private static final String TAG = "BaseActivityWithBack";
    private TimerTask mLogOutTask;
    private Timer mLogOutTimer;
    private BroadcastReceiver screenLockReceiver;
    private TimerHelper timerHelper;
    private ActionBar baseActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 如果是[SettingNetworkActivity]则设置无标题
        if (getClass().getSimpleName().contains("SettingNetworkActivity")) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        super.onCreate(savedInstanceState);
        SmartLinkV3App.getContextInstance().add(this);
        if (!getClass().getSimpleName().contains("SettingNetworkActivity")) {
            baseActionBar = getSupportActionBar();
            if (baseActionBar != null) {
                baseActionBar.setHomeButtonEnabled(true);
                baseActionBar.setDisplayHomeAsUpEnabled(true);
                baseActionBar.setElevation(0);
            }
        }
        screenLockReceiver = new ScreenStateChangeReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(screenLockReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 初始化PreferenceUtil
        PreferenceUtil.init(this);
        // 根据上次的语言设置，重新设置语言
        if (!"".equals(PreferenceUtil.getString(C_Constants.Language.LANGUAGE, ""))) {
            switchLanguage(PreferenceUtil.getString(C_Constants.Language.LANGUAGE, ""));
        }
        // 提交当前的activity
        SPUtils.getInstance(this).put(Cons.CURRENT_ACTIVITY, getClass().getSimpleName());
    }

    /**
     * <切换语言>
     *
     * @param language
     * @see [类、类#方法、类#成员]
     */
    protected void switchLanguage(String language) {
        // 设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals(C_Constants.Language.ENGLISH)) {
            config.locale = Locale.ENGLISH;
        } else if (language.equals(C_Constants.Language.ARABIC)) {
            // 阿拉伯语
            config.locale = new Locale(C_Constants.Language.ARABIC);
        } else if (language.equals(C_Constants.Language.GERMENIC)) {
            // 德语
            config.locale = Locale.GERMANY;
        } else if (language.equals(C_Constants.Language.ESPANYOL)) {
            // 西班牙语
            // config.locale = new Locale(C_Constants.Language.ESPANYOL);
            config.locale = new Locale(C_Constants.Language.ESPANYOL, "MX");
        } else if (language.equals(C_Constants.Language.ITALIAN)) {
            // 意大利语
            config.locale = Locale.ITALIAN;
        } else if (language.equals(C_Constants.Language.FRENCH)) {
            // 法语
            config.locale = Locale.FRENCH;
        } else if (language.equals(C_Constants.Language.SERBIAN)) {
            // 塞尔维亚
            config.locale = new Locale(C_Constants.Language.SERBIAN);
        } else if (language.equals(C_Constants.Language.CROATIAN)) {
            // 克罗地亚
            config.locale = new Locale(C_Constants.Language.CROATIAN);
        } else if (language.equals(C_Constants.Language.SLOVENIAN)) {
            // 斯洛文尼亚
            config.locale = new Locale(C_Constants.Language.SLOVENIAN);
        } else if (language.equals(C_Constants.Language.POLAND)) {
            // 波兰语
            config.locale = new Locale(C_Constants.Language.POLAND);
        }else if (language.equals(C_Constants.Language.RUSSIAN)) {
            // 俄语
            config.locale = new Locale(C_Constants.Language.RUSSIAN);
        } else if (language.equals(C_Constants.Language.CHINA)) {
            // 台湾
            config.locale = new Locale(C_Constants.Language.CHINA, "TW");
        }
        resources.updateConfiguration(config, dm);

        // 保存设置语言的类型
        PreferenceUtil.commitString(C_Constants.Language.LANGUAGE, language);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getAction();
        if (MotionEvent.ACTION_DOWN == action || MotionEvent.ACTION_UP == action) {
            // 每次点击时候判断是否为以下三个界面--> 是则不启动定时器--> 只有进入Home界面才启用定时器
            OtherUtils.stopHomeTimer();
            if (isSpecialAc()) {
            } else {
                HomeRxActivity.autoLogoutTask = new TimerTask() {
                    @Override
                    public void run() {
                        Logs.t("check login").ii(getClass().getSimpleName() + ": line--> " + "140");
                        logout(false);
                    }
                };
                HomeRxActivity.autoLogoutTimer = new Timer();
                HomeRxActivity.autoLogoutTimer.schedule(HomeRxActivity.autoLogoutTask, Cons.AUTO_LOGOUT_PERIOD);
                OtherUtils.homeTimerList.add(HomeRxActivity.autoLogoutTimer);
                OtherUtils.homeTimerList.add(HomeRxActivity.autoLogoutTask);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /* 是否为指定的三个界面--> true: 是 */
    public boolean isSpecialAc() {
        String currentActivity = AppInfo.getCurrentActivityName(this);
        boolean la = currentActivity.contains("LoadingRxActivity");
        boolean ga = currentActivity.contains("GuideRxActivity");
        boolean loa = currentActivity.contains("LoginRxActivity");
        if (la | ga | loa) {
            return true;
        } else {
            return false;
        }
    }

    class ScreenStateChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                String action = intent.getAction();
                switch (action) {
                    case Intent.ACTION_SCREEN_ON:
                        break;
                    case Intent.ACTION_SCREEN_OFF:
                        OtherUtils.stopHomeTimer();
                        if (!isSpecialAc()) {
                            logout(true);/* 锁屏后登出 */
                        } else {
                            OtherUtils.stopHomeTimer();
                        }
                        break;
                    case Intent.ACTION_USER_PRESENT:
                        break;
                }
            }
        }
    }

    /* 登出 */

    /**
     * @param isLockScreen 是否为锁屏调用
     */
    private void logout(boolean isLockScreen) {
        // 如果为锁屏则现判断是否为登陆--> 如果为登入,此时允许调用登出接口
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            int state = getLoginStateBean.getState();
            if (state == GetLoginStateBean.CONS_LOGIN) {
                requestLogout();
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    private void requestLogout() {
        LogoutHelper xLogoutHelper = new LogoutHelper();
        xLogoutHelper.setOnLogoutSuccessListener(() -> CA.toActivity(BaseActivityWithBack.this, LoginRxActivity.class, false, true, false, 0));
        xLogoutHelper.setOnLogOutFailedListener(() -> ToastUtil_m.show(BaseActivityWithBack.this, getString(R.string.login_logout_failed)));
        xLogoutHelper.logout();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (screenLockReceiver != null) {
            unregisterReceiver(screenLockReceiver);
            screenLockReceiver = null;
        }
    }
}
