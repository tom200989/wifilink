package com.alcatel.wifilink.root.ue.frag;

import android.app.Activity;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.ClickDoubleHelper;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.FirmUpgradeHelper;
import com.alcatel.wifilink.root.helper.UpgradeHelper;
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_BackupWidget;
import com.alcatel.wifilink.root.widget.HH70_CheckVersionWidget;
import com.alcatel.wifilink.root.widget.HH70_CountDownWidget;
import com.alcatel.wifilink.root.widget.HH70_DownWidget;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.alcatel.wifilink.root.widget.NormalWidget;
import com.hiber.cons.TimerState;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceNewVersionBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceBackupHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceRebootHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceResetHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceRestoreHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceUpdateStopHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class SettingFrag extends BaseFrag {
    private final static String CONFIG_FILE_PATH = "configFilePath";
    public static boolean isSharingSupported = true;

    private static final int RESTART_RESET = 1;
    private static final int Backup_Restore = 2;

    private final static String mdefaultSaveUrl = "/com/tcl/linkhub/backup";
    private final static String mdefaultSaveUrl2 = "/com/tcl/linkzone/backup";

    boolean isDownloading = false;
    private TimerHelper downTimer;
    private GetSystemInfoHelper xGetSystemInfoHelper;
    private Extender_GetWIFIExtenderSettingsHelper extenderHelper;
    private String off;
    private String on;
    private String noSimcard;
    private String detected;
    private String initing;
    private String perText;
    private int upgrade_Temp = 0;// 临时记录升级失效的次数
    int count = 0;
    private boolean isMW120;
    private boolean isHH71;


    @BindView(R.id.tv_settingrx_logout)
    TextView tvLogout;
    @BindView(R.id.setting_login_password)
    RelativeLayout mLoginPassword;
    @BindView(R.id.setting_mobile_network)
    RelativeLayout mMobileNetwork;
    @BindView(R.id.tv_setting_sim_socket)
    TextView mMobileNetworkSimSocket;// SIM开关显示
    @BindView(R.id.tv_setting_wan_socket)
    TextView mMobileNetworkWanSocket;// WAN开关显示
    @BindView(R.id.setting_ethernet_wan)
    RelativeLayout mEthernetWan;
    @BindView(R.id.setting_sharing_service)
    RelativeLayout mSharingService;
    @BindView(R.id.setting_language)
    RelativeLayout mLanguage;
    @BindView(R.id.setting_firmware_upgrade)
    RelativeLayout mFirmwareUpgrade;
    @BindView(R.id.iv_setting_firmware_upgrade_tipdot)
    ImageView mFirmwareUpgrade_dot;// 升级小点
    @BindView(R.id.setting_restart)
    RelativeLayout mRestart;
    @BindView(R.id.setting_backup)
    RelativeLayout mBackup;
    @BindView(R.id.setting_about)
    RelativeLayout mAbout;
    @BindView(R.id.setting_firmware_upgrade_version)
    TextView mDeviceVersion;
    @BindView(R.id.rl_setting_wifi_extender)
    RelativeLayout rl_wifiExtender;// wifi extender 面板
    @BindView(R.id.setting_feedback)
    View rl_feedback;
    @BindView(R.id.tv_wifi_extender_socket)
    TextView tvExtenderOnOff;

    @BindView(R.id.dg_settingRx_widget_ok)
    NormalWidget dgSettingRxWidgetOk;
    @BindView(R.id.dg_settingRx_widget_confirm)
    NormalWidget dgSettingRxWidgetConfirm;
    @BindView(R.id.wd_upgrade_checkversion)
    HH70_CheckVersionWidget wd_checkVersion;
    @BindView(R.id.wd_setting_down)
    HH70_DownWidget wd_down;
    @BindView(R.id.wd_countdown)
    HH70_CountDownWidget wd_countdown;
    @BindView(R.id.wd_backup)
    HH70_BackupWidget wd_backup;
    @BindView(R.id.lw_loading)
    HH70_LoadWidget loadWidget;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_home_setting;
    }

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initRes();
        initSome();
        init();
        initClick();
        timer_period = 2500;
        timerState = TimerState.ON;
    }

    private void initRes() {
        on = getString(R.string.setting_on_state);
        off = getString(R.string.setting_off_state);
        noSimcard = getString(R.string.home_no_sim);
        detected = getString(R.string.Home_SimCard_Detected);
        initing = getString(R.string.home_initializing);
        perText = getString(R.string.usage_setting_usagealertunit);
    }

    private void initSome() {
        extenderHelper = new Extender_GetWIFIExtenderSettingsHelper();

        extenderHelper.setOnStateEnableOnListener(stateEnable -> tvExtenderOnOff.setText(on));
        extenderHelper.setOnstateEnableOffListener(stateEnable -> tvExtenderOnOff.setText(off));
        extenderHelper.setOnGetExtenderFailedListener(() -> tvExtenderOnOff.setText(off));

        // 加入MW120设备类型判断
        xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(systemInfo -> {
            String deviceName = systemInfo.getDeviceName().toLowerCase();
            isMW120 = RootUtils.isMWDEV(deviceName);
            isHH71 = RootUtils.isHH71(deviceName);
            mEthernetWan.setVisibility(isMW120 ? View.GONE : View.VISIBLE);
            rl_wifiExtender.setVisibility(isMW120 | isHH71 ? View.VISIBLE : View.GONE);
            rl_feedback.setVisibility(isMW120 ? View.VISIBLE : View.GONE);
            if (!isMW120) {// 不是MW120机型--> 请求wan口
                requestWAN();
            } else {// 如果是MW120机型--> 请求extender接口
                extenderHelper.get();
            }
        });
    }

    /**
     * 请求wan口
     */
    private void requestWAN() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
                xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
                    switch (wanSettingsBean.getStatus()) {
                        case GetWanSettingsBean.CONS_CONNECTED:
                            mMobileNetworkWanSocket.setText(on);
                            break;
                        case GetWanSettingsBean.CONS_DISCONNECTED:
                        case GetWanSettingsBean.CONS_DISCONNECTING:
                        case GetWanSettingsBean.CONS_CONNECTING:
                            mMobileNetworkWanSocket.setText(off);
                            break;
                    }
                });
                xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> mMobileNetworkWanSocket.setText(off));
                xGetWanSettingsHelper.getWanSettings();
            } else {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> mMobileNetworkWanSocket.setText(off));
        xGetLoginStateHelper.getLoginState();

    }

    @Override
    public void setTimerTask() {
        super.setTimerTask();
        // 检测WAN口 | SIM是否连接 | 是否为新设备
        checkSimStatus();
        xGetSystemInfoHelper.getSystemInfo();
    }


    private void checkSimStatus() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(SplashActivity.class, LoginFrag.class);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_PIN_REQUIRED:
                    case GetSimStatusBean.CONS_PUK_REQUIRED:
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        mMobileNetworkSimSocket.setText(off);
                        break;
                    case GetSimStatusBean.CONS_NOWN:
                        mMobileNetworkSimSocket.setText(noSimcard);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                        mMobileNetworkSimSocket.setText(detected);
                    case GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
                        mMobileNetworkSimSocket.setText(initing);
                    case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                        mMobileNetworkSimSocket.setText(off);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_READY:
                        mMobileNetworkSimSocket.setText(on);
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> mMobileNetworkSimSocket.setText(noSimcard));
            xGetSimStatusHelper.getSimStatus();
        });

        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> mMobileNetworkSimSocket.setText(noSimcard));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 跳转activity
     */
    private void to(Class targetAc, Class targetFr) {
        toFragActivity(getClass(), targetAc, targetFr, null, false, true, 0);
    }

    private void initClick() {
        tvLogout.setOnClickListener(v -> logout());// 登出
        mLoginPassword.setOnClickListener(v -> goToAccountSettingPage());// 修改密码
        mMobileNetwork.setOnClickListener(v -> goToMobileNetworkSettingPage());// 进入Mobile network
        mEthernetWan.setOnClickListener(v -> goEthernetWanConnectionPage());// 进入wan info
        mSharingService.setOnClickListener(v -> {// 进入USB界面
            if (isSharingSupported) {
                goToShareSettingPage();
            } else {
                toast(R.string.setting_not_support_sharing_service);
            }
        });
        mLanguage.setOnClickListener(v -> goSettingLanguagePage());// 进入语言设置
        mFirmwareUpgrade.setOnClickListener(v -> clickUpgrade());// 点击升级
        mRestart.setOnClickListener(v -> popDialogFromBottom(RESTART_RESET));// 重启设备
        mBackup.setOnClickListener(v -> {// 备份配置
            popDialogFromBottom(Backup_Restore);
        });
        mAbout.setOnClickListener(v -> goToAboutSettingPage());// 进入about
        rl_wifiExtender.setOnClickListener(v -> goToWifiExtender());// 进入wifi extender
        rl_feedback.setOnClickListener(v -> goToFeedback());// 上线时把这句打开
    }

    private void init() {
        getDeviceFWCurrentVersion();
        showSharingService();
    }

    /**
     * 获取fw当前版本
     */
    private void getDeviceFWCurrentVersion() {
        // 1.获取当前版本
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            mDeviceVersion.setText(result.getSwVersionMain());
        });
        xGetSystemInfoHelper.getSystemInfo();

        // 2.检测新版本
        UpgradeHelper uh = new UpgradeHelper(activity, false);
        uh.setOnNewVersionListener(attr -> mFirmwareUpgrade_dot.setVisibility(View.VISIBLE));
        uh.checkVersion(wd_countdown);
    }

    private void showSharingService() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            String version = result.getSwVersion().toLowerCase();
            boolean hh71 = RootUtils.isHH71(version);
            boolean hh4 = RootUtils.isHH4X(version);
            boolean mw = RootUtils.isMWDEV(version);
            mSharingService.setVisibility(hh4 | mw | hh71 ? View.GONE : View.VISIBLE);
        });
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * U1.点击了升级
     */
    private void clickUpgrade() {
        FirmUpgradeHelper fh = new FirmUpgradeHelper(activity, true);
        fh.setOnNoNewVersionListener(this::popversion);
        fh.setOnLoginOutListener(() -> toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true));
        fh.setOnNewVersionListener(attr -> popversion(attr, null));// 有新版本
        fh.checkNewVersion(wd_countdown);
    }

    /**
     * U2.弹窗(有新版本 | 没有新版本)
     *
     * @param updateDeviceNewVersion
     */
    private void popversion(GetDeviceNewVersionBean updateDeviceNewVersion, GetSystemInfoBean systemSystemInfo) {
        // 1.获取状态 ( NO_NEW_VERSION || CHECK_ERROR 均为没有版本可以升级 )
        int state = updateDeviceNewVersion.getState();
        boolean noNewVersion = state == GetDeviceNewVersionBean.CONS_NO_NEW_VERSION || state == GetDeviceNewVersionBean.CONS_CHECK_ERROR;
        // 2.显示弹窗
        wd_checkVersion.setVisibility(View.VISIBLE);
        wd_checkVersion.setVersionName(noNewVersion ? systemSystemInfo.getSwVersionMain() : updateDeviceNewVersion.getVersion() + " " + getString(R.string.available));
        wd_checkVersion.setTipVisible(noNewVersion);
        wd_checkVersion.setOkText(noNewVersion ? getString(R.string.ok) : getString(R.string.setting_upgrade));
        wd_checkVersion.setOnClickOKListener(() -> {
            if (!noNewVersion) {
                dgSettingRxWidgetOk.setVisibility(View.VISIBLE);
                dgSettingRxWidgetOk.setTitle(R.string.setting_upgrade);
                dgSettingRxWidgetOk.setDes(R.string.setting_upgrade_firmware_warning);
                dgSettingRxWidgetOk.setOnBgClickListener(() -> {
                });
                dgSettingRxWidgetOk.setOnCancelClickListener(() -> dgSettingRxWidgetOk.setVisibility(View.GONE));
                dgSettingRxWidgetOk.setOnOkClickListener(this::beginDownLoadFOTA);
            }
        });
    }

    /**
     * U3.触发FOTA下载
     */
    private void beginDownLoadFOTA() {
        count = 0;
        isDownloading = true;
        FirmUpgradeHelper fuh = new FirmUpgradeHelper(activity, false);
        fuh.setOnLoginOutListener(() -> toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true));
        fuh.setOnErrorListener(() -> {
            count = 0;
            toast(R.string.connect_failed);
            isDownloading = false;
        });
        // 触发成功
        fuh.setOnSetFOTADownSuccessListener(() -> {
            /* 1.显示进度弹窗 */
            wd_down.setOnClickCancelListener(() -> {
                // 1.2.停止定时器
                stopDownTimerAndPop();
                // 1.3.请求停止
                SetDeviceUpdateStopHelper xSetDeviceUpdateStopHelper = new SetDeviceUpdateStopHelper();
                xSetDeviceUpdateStopHelper.setOnSetDeviceUpdateStopSuccessListener(() -> {
                    stopDownTimerAndPop();
                    toast(R.string.setting_upgrade_stop_error);
                    isDownloading = false;
                });
                xSetDeviceUpdateStopHelper.setOnSetDeviceUpdateStopFailedListener(() -> downError(-1));
                xSetDeviceUpdateStopHelper.setDeviceUpdateStop();
            });
            wd_down.setVisibility(View.VISIBLE);

            /* 2.启动定时器 */
            UpgradeHelper uh = new UpgradeHelper(activity, false);
            uh.setOnErrorListener(() -> downError(R.string.setting_upgrade_get_update_state_failed));
            uh.setOnResultErrorListener(() -> downError(R.string.setting_upgrade_get_update_state_failed));
            uh.setOnNoStartUpdateListener(attr1 -> {
                wd_down.getPercent().setText(String.valueOf(attr1.getProcess() + perText));
                upgrade_Temp++;
                if (upgrade_Temp > 20) {// 如果断连次数已经达到了1分钟(20次)
                    upgrade_Temp = 0;
                    stopDownTimerAndPop();// 停止定时器
                    downError(R.string.setting_upgrade_get_update_state_failed);// 提示
                }
            });
            uh.setOnUpgradeStateNormalListener(attr1 -> {
            });
            uh.setOnUpdatingListener(attr1 -> {
                wd_down.getPercent().setText(String.valueOf(attr1.getProcess() + perText));// 显示百分比
                wd_down.getProgressbar().setProgress(attr1.getProcess());// 进度条显示进度
            });
            uh.setOnCompleteListener(attr1 -> {
                if (count == 0) {
                    // 2.1.显示进度
                    wd_down.getPercent().setText(String.valueOf("100%"));
                    wd_down.getProgressbar().setProgress(100);// 进度条显示进度
                    // 2.2.停止定时器 + 进度条消隐
                    stopDownTimerAndPop();
                    // 2.3.修改标记位
                    isDownloading = false;
                    // 2.3.延迟2秒弹窗提示用户是否触发底层升级
                    new Handler().postDelayed(this::triggerFOTAUpgrade, 2000);
                    // 2.4.修改标记位
                    count++;
                }
            });

            /* -------------------------------------------- 定时器获取WAN以及SIM的连接状态 -------------------------------------------- */

            // 创建定时器
            downTimer = new TimerHelper(activity) {
                @Override
                public void doSomething() {
                    // 轮训WAN口--> 成功:获取进度 + 失败: 获取SIM卡--> 成功:获取进度 + 失败:提示
                    getWAN(uh);
                }
            };
            downTimer.start(3000);
        });
        fuh.triggerFOTA();// 触发FOTA下载
    }

    /**
     * 获取WAN状态
     */
    private void getWAN(UpgradeHelper uh) {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
                xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
                    switch (wanSettingsBean.getStatus()) {
                        case GetWanSettingsBean.CONS_CONNECTED:
                            uh.getDownState();
                            break;
                        case GetWanSettingsBean.CONS_DISCONNECTED:
                        case GetWanSettingsBean.CONS_DISCONNECTING:
                        case GetWanSettingsBean.CONS_CONNECTING:
                            getSIM(uh);
                            break;
                    }
                });
                xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> getSIM(uh));
                xGetWanSettingsHelper.getWanSettings();
            } else {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> getSIM(uh));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 获取SIM状态
     */
    private void getSIM(UpgradeHelper uh) {
        // SIM状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, false, true, 0);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                if (simState != GetSimStatusBean.CONS_SIM_CARD_READY) {
                    // 获取下载进度
                    uh.getDownState();
                } else {
                    // 弹出提示并隐藏弹窗
                    downError(R.string.qs_pin_unlock_can_not_connect_des);
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> downError(-1));
            xGetSimStatusHelper.getSimStatus();
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> downError(-1));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * U4.触发硬件底层进行升级操作
     */
    private void triggerFOTAUpgrade() {
        dgSettingRxWidgetConfirm.setVisibility(View.VISIBLE);
        dgSettingRxWidgetConfirm.setTitle(R.string.setting_upgrade);
        dgSettingRxWidgetConfirm.setDes(R.string.setting_upgrade_firmware_warning);
        dgSettingRxWidgetConfirm.setOnBgClickListener(() -> {
        });
        dgSettingRxWidgetConfirm.setOnCancelClickListener(() -> dgSettingRxWidgetConfirm.setVisibility(View.GONE));
        dgSettingRxWidgetConfirm.setOnOkClickListener(() -> {
            stopDownTimerAndPop();
            startDeviceUpgrade();
        });
    }

    /**
     * U5.正式开始升级
     */
    private void startDeviceUpgrade() {
        isDownloading = true;
        FirmUpgradeHelper fuh = new FirmUpgradeHelper(activity, false);
        fuh.setOnLoginOutListener(() -> toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true));
        fuh.setOnErrorListener(() -> downError(R.string.setting_upgrade_start_update_failed));
        fuh.setOnStartUpgradeListener(() -> {
            toast(R.string.device_will_restart_later);
            loadWidget.setLoadTv(R.string.updating);
            loadWidget.setVisibles();
        });
        fuh.startUpgrade();
    }

    /**
     * U3.Error.下载中发生错误
     */
    private void downError(int resId) {
        // 1.停止定时器 + 2.弹窗消隐
        stopDownTimerAndPop();
        // 3.提示
        if (resId == -1) {
            toast(R.string.connect_failed);
        } else {
            toast(resId);
        }
        // 4.恢复标记位
        isDownloading = false;
    }

    private void popDialogFromBottom(int itemType) {
        wd_backup.setVisibility(View.VISIBLE);
        wd_backup.setFirstText(itemType == RESTART_RESET ? R.string.restart : R.string.backup);
        wd_backup.setSecondText(itemType == RESTART_RESET ? R.string.reset_to_factory_settings : R.string.restore);
        wd_backup.setOnClickFirstTextListener(() -> {
            if (itemType == RESTART_RESET) {
                restartDevice();
            } else {
                backupDevice();
            }
        });
        wd_backup.setOnClickSecTextListener(() -> {
            if (itemType == RESTART_RESET) {
                showDialogResetFactorySetting();
            } else {
                restore();
            }
        });
    }

    private void backupDevice() {
        showBackupSuccessDialog();
    }

    private void showBackupSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.back_up_settings);
        EditText editText = new EditText(activity);
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams.setMargins(20, 0, 20, 0);
        editText.setLayoutParams(LayoutParams);
        editText.setText(isMW120 ? mdefaultSaveUrl2 : mdefaultSaveUrl);// mw120就用mw120的地址
        builder.setView(editText);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.backup, (dialog, which) -> {
            String savePath = RootUtils.getEDText(editText, true);
            ShareUtils.set(CONFIG_FILE_PATH, savePath);
            downLoadConfigureFile(savePath);
            dialog.dismiss();
        });
        builder.show();
    }

    private void downLoadConfigureFile(String saveUrl) {
        SetDeviceBackupHelper xSetDeviceBackup = new SetDeviceBackupHelper();
        xSetDeviceBackup.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceBackup.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceBackup.setOnDownSuccessListener(attr -> toast(R.string.succeed));
        xSetDeviceBackup.setOnSetDeviceBackupFailedListener(() -> toast(R.string.fail));
        xSetDeviceBackup.setOnPathNotMatchRuleListener(path -> {
            toast("path is empty or not match rule , it contains [\\ : * ? \" < > | .]", 2000);
        });
        xSetDeviceBackup.setDeviceBackup(saveUrl);
    }

    private void showDialogResetFactorySetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.reset_router);
        builder.setMessage(R.string.This_will_reset_all_settings_on_your_router_to_factory_defaults_This_action_can_not_be_undone);
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton(R.string.reset, (dialogInterface, i) -> resetDevice());
        builder.show();
    }

    private void restartDevice() {
        SetDeviceRebootHelper xSetDeviceRebootHelper = new SetDeviceRebootHelper();
        xSetDeviceRebootHelper.setOnSetDeviceRebootSuccessListener(() -> toast(R.string.succeed));
        xSetDeviceRebootHelper.setOnSetDeviceRebootFailedListener(() -> toast(R.string.fail));
        xSetDeviceRebootHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceRebootHelper.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceRebootHelper.SetDeviceReboot();
    }

    private void resetDevice() {
        SetDeviceResetHelper xSetDeviceResetHelper = new SetDeviceResetHelper();
        xSetDeviceResetHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceResetHelper.setOnSetDeviceResetSuccessListener(() -> {
            dismissLoadingDialog();
            toast(R.string.complete, 5000);
        });
        xSetDeviceResetHelper.setOnSetDeviceResetFailedListener(() -> {
            dismissLoadingDialog();
            toast(R.string.couldn_t_reset_try_again, 5000);
        });
        xSetDeviceResetHelper.SetDeviceReset();
    }

    private void restore() {
        SetDeviceRestoreHelper xSetDeviceRestoreHelper = new SetDeviceRestoreHelper();
        xSetDeviceRestoreHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceRestoreHelper.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceRestoreHelper.setOnRestoreSuccessListener(file -> toast(R.string.succeed));
        xSetDeviceRestoreHelper.setOnRestoreFailedListener(() -> toast(R.string.couldn_t_restore_try_again));
        xSetDeviceRestoreHelper.setDeviceRestore();
    }

    private void showLoadingDialog() {
        loadWidget.setLoadTv(R.string.back_up_progress);
        loadWidget.setVisibles();
    }

    private void dismissLoadingDialog() {
        loadWidget.setGone();
    }

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    /* -------------------------------------------- HELPER -------------------------------------------- */
    private void goToShareSettingPage() {
        toFrag(getClass(), SettingShareFrag.class, null, true);
    }

    private void goSettingLanguagePage() {
        toFrag(getClass(), LanguageFrag.class, null, true);
    }

    private void goToAccountSettingPage() {
        toFrag(getClass(), SettingAccountFrag.class, null, true);
    }

    /**
     * 进入Mobile network
     */
    private void goToMobileNetworkSettingPage() {
        toFrag(getClass(), MobileNetworkFrag.class, null, true);
    }

    /**
     * 进入ethernet wan conncet
     */
    private void goEthernetWanConnectionPage() {
        toFrag(getClass(), EtherWANFrag.class, null, true);
    }

    /**
     * 进入wifi extender
     */
    private void goToWifiExtender() {
        toFrag(getClass(), WifiExtenderRxFrag.class, null, true);
    }

    /**
     * 进入feedback
     */
    private void goToFeedback() {
        toFrag(getClass(), FeedbackFrag.class, null, true);
    }

    private void goToAboutSettingPage() {
        toFrag(getClass(), AboutFrag.class, null, true);
    }

    private void toast(int resId) {
        toast(resId, 2000);
    }

    /**
     * 停止下载定时器
     */
    public void stopDownTimerAndPop() {
        count = 0;
        if (wd_down.getVisibility() == View.VISIBLE) {
            wd_down.setVisibility(View.GONE);
        }
        if (downTimer != null) {
            downTimer.stop();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (wd_checkVersion.getVisibility() == View.VISIBLE) {
            wd_checkVersion.setVisibility(View.GONE);
            return true;
        } else if (wd_down.getVisibility() == View.VISIBLE) {
            Log.i("ma", "onBackPressed: ");
            return true;
        } else if (dgSettingRxWidgetOk.getVisibility() == View.VISIBLE) {
            dgSettingRxWidgetOk.setVisibility(View.GONE);
            return true;
        } else if (dgSettingRxWidgetConfirm.getVisibility() == View.VISIBLE) {
            dgSettingRxWidgetConfirm.setVisibility(View.GONE);
            return true;
        } else if (isDownloading) {// 如果在下载中, 则自己处理返回按钮的逻辑
            return true;
        } else {
            // 登出
            ClickDoubleHelper clickDouble = new ClickDoubleHelper();
            clickDouble.setOnClickOneListener(() -> toast(R.string.home_exit_app, 3000));
            clickDouble.setOnClickDoubleListener(this::logout);
            clickDouble.click();
            return true;
        }
    }


    /**
     * 退出
     */
    private void logout() {
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
