package com.alcatel.wifilink.root.ue.frag;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.ClickDoubleHelper;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.FirmUpgradeHelper;
import com.alcatel.wifilink.root.helper.UpgradeHelper;
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_BackupPathWidget;
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
    @BindView(R.id.wd_backup_path)
    HH70_BackupPathWidget wd_backpath;
    @BindView(R.id.wd_reset_tip)
    NormalWidget wd_reset;
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
        on = getString(R.string.hh70_on);
        off = getString(R.string.hh70_off);
        noSimcard = getString(R.string.hh70_no_sim);
        detected = getString(R.string.hh70_sim_card_deleted);
        initing = getString(R.string.hh70_initializing);
        perText = getString(R.string.hh70_usage_alert_unit);
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
                toast(R.string.hh70_not_support_sharing);
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
        uh.checkVersion(wd_countdown, loadWidget);
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
        FirmUpgradeHelper fh = new FirmUpgradeHelper(activity, loadWidget, true);
        fh.setOnNoNewVersionListener(this::popversion);
        fh.setOnLoginOutListener(() -> toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true));
        fh.setOnNewVersionListener(attr -> popversion(attr, null));// 有新版本
        fh.checkNewVersion(wd_countdown, loadWidget);
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
        wd_checkVersion.setVersionName(noNewVersion ? systemSystemInfo.getSwVersionMain() : updateDeviceNewVersion.getVersion() + " " + getString(R.string.hh70_available));
        wd_checkVersion.setTipVisible(noNewVersion);
        wd_checkVersion.setOkText(noNewVersion ? getString(R.string.hh70_ok) : getString(R.string.hh70_upgrade));
        wd_checkVersion.setOnClickOKListener(() -> {
            if (!noNewVersion) {
                dgSettingRxWidgetOk.setVisibility(View.VISIBLE);
                dgSettingRxWidgetOk.setTitle(R.string.hh70_upgrade);
                dgSettingRxWidgetOk.setDes(R.string.hh70_the_update_process);
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
        FirmUpgradeHelper fuh = new FirmUpgradeHelper(activity, loadWidget, false);
        fuh.setOnLoginOutListener(() -> toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true));
        fuh.setOnErrorListener(() -> {
            count = 0;
            toast(R.string.hh70_cant_connect);
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
                    toast(R.string.hh70_cant_update);
                    isDownloading = false;
                });
                xSetDeviceUpdateStopHelper.setOnSetDeviceUpdateStopFailedListener(() -> downError(-1));
                xSetDeviceUpdateStopHelper.setDeviceUpdateStop();
            });
            wd_down.setVisibility(View.VISIBLE);

            /* 2.启动定时器 */
            UpgradeHelper uh = new UpgradeHelper(activity, false);
            uh.setOnUpgradeFailedListener(() -> downError(R.string.hh70_cant_get_device));
            uh.setOnNoStartUpdateListener(attr1 -> {
                wd_down.getPercent().setText(String.valueOf(attr1.getProcess() + perText));
                upgrade_Temp++;
                if (upgrade_Temp > 20) {// 如果断连次数已经达到了1分钟(20次)
                    upgrade_Temp = 0;
                    stopDownTimerAndPop();// 停止定时器
                    downError(R.string.hh70_cant_get_device);// 提示
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
                    downError(R.string.hh70_check_your_wan);
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
        dgSettingRxWidgetConfirm.setTitle(R.string.hh70_upgrade);
        dgSettingRxWidgetConfirm.setDes(R.string.hh70_the_update_process);
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
        FirmUpgradeHelper fuh = new FirmUpgradeHelper(activity, loadWidget, false);
        fuh.setOnLoginOutListener(() -> toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true));
        fuh.setOnErrorListener(() -> downError(R.string.hh70_device_cant_start));
        fuh.setOnStartUpgradeListener(() -> {
            toast(R.string.hh70_device_will_restart);
            loadWidget.setLoadTv(R.string.hh70_updating);
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
            toast(R.string.hh70_cant_connect);
        } else {
            toast(resId);
        }
        // 4.恢复标记位
        isDownloading = false;
    }

    private void popDialogFromBottom(int itemType) {
        wd_backup.setVisibility(View.VISIBLE);
        wd_backup.setFirstText(itemType == RESTART_RESET ? R.string.hh70_restart : R.string.hh70_backup);
        wd_backup.setSecondText(itemType == RESTART_RESET ? R.string.hh70_reset_factory : R.string.hh70_restore);
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
        wd_backpath.setVisibility(View.VISIBLE);
        wd_backpath.setPath(isMW120 ? mdefaultSaveUrl2 : mdefaultSaveUrl);
        wd_backpath.setOnClickBackupListener(path -> {
            ShareUtils.set(CONFIG_FILE_PATH, path);
            downLoadConfigureFile(path);
        });
    }

    private void downLoadConfigureFile(String saveUrl) {
        SetDeviceBackupHelper xSetDeviceBackup = new SetDeviceBackupHelper();
        xSetDeviceBackup.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceBackup.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceBackup.setOnDownSuccessListener(attr -> toast(R.string.hh70_succeed));
        xSetDeviceBackup.setOnSetDeviceBackupFailedListener(() -> toast(R.string.hh70_fail));
        xSetDeviceBackup.setOnPathNotMatchRuleListener(path -> toast("path is empty or not match rule , it contains [\\ : * ? \" < > | .]", 2000));
        xSetDeviceBackup.setDeviceBackup(saveUrl);
    }

    private void showDialogResetFactorySetting() {
        wd_reset.setVisibility(View.VISIBLE);
        wd_reset.setTitle(R.string.hh70_reset_router);
        wd_reset.setDes(R.string.hh70_will_reset_route);
        wd_reset.setOnBgClickListener(() -> wd_reset.setVisibility(View.GONE));
        wd_reset.setOnCancelClickListener(() -> wd_reset.setVisibility(View.GONE));
        wd_reset.setOnOkClickListener(() -> {
            resetDevice();
            wd_reset.setVisibility(View.GONE);
        });
    }

    private void restartDevice() {
        SetDeviceRebootHelper xSetDeviceRebootHelper = new SetDeviceRebootHelper();
        xSetDeviceRebootHelper.setOnSetDeviceRebootSuccessListener(() -> toast(R.string.hh70_succeed));
        xSetDeviceRebootHelper.setOnSetDeviceRebootFailedListener(() -> toast(R.string.hh70_fail));
        xSetDeviceRebootHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceRebootHelper.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceRebootHelper.SetDeviceReboot();
    }

    private void resetDevice() {
        SetDeviceResetHelper xSetDeviceResetHelper = new SetDeviceResetHelper();
        xSetDeviceResetHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceResetHelper.setOnSetDeviceResetSuccessListener(() -> {
            dismissLoadingDialog();
            toast(R.string.hh70_complete, 5000);
        });
        xSetDeviceResetHelper.setOnSetDeviceResetFailedListener(() -> {
            dismissLoadingDialog();
            toast(R.string.hh70_reset_try_again, 5000);
        });
        xSetDeviceResetHelper.SetDeviceReset();
    }

    private void restore() {
        SetDeviceRestoreHelper xSetDeviceRestoreHelper = new SetDeviceRestoreHelper();
        xSetDeviceRestoreHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceRestoreHelper.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceRestoreHelper.setOnRestoreSuccessListener(file -> toast(R.string.hh70_succeed));
        xSetDeviceRestoreHelper.setOnRestoreFailedListener(() -> toast(R.string.hh70_restore_try_again));
        xSetDeviceRestoreHelper.setDeviceRestore();
    }

    private void showLoadingDialog() {
        loadWidget.setLoadTv(R.string.hh70_wait_5_60);
        loadWidget.setVisibles();
    }

    private void dismissLoadingDialog() {
        loadWidget.setGone();
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

        } else if (wd_countdown.getVisibility() == View.VISIBLE) {
            Log.i("ma", "onBackPressed: ");
            return true;

        } else if (wd_backup.getVisibility() == View.VISIBLE) {
            wd_backup.setVisibility(View.GONE);
            return true;

        } else if (wd_backpath.getVisibility() == View.VISIBLE) {
            wd_backpath.setVisibility(View.GONE);
            return true;

        } else if (dgSettingRxWidgetOk.getVisibility() == View.VISIBLE) {
            dgSettingRxWidgetOk.setVisibility(View.GONE);
            return true;

        } else if (dgSettingRxWidgetConfirm.getVisibility() == View.VISIBLE) {
            dgSettingRxWidgetConfirm.setVisibility(View.GONE);
            return true;

        } else if (loadWidget.getVisibility() == View.VISIBLE) {
            Log.i("ma", "onBackPressed: ");
            return true;

        } else if (isDownloading) {// 如果在下载中, 则自己处理返回按钮的逻辑
            Log.i("ma", "onBackPressed: ");
            return true;

        } else {
            // 登出
            ClickDoubleHelper clickDouble = new ClickDoubleHelper();
            clickDouble.setOnClickOneListener(() -> toast(R.string.hh70_touch_again, 3000));
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
                    toast(R.string.hh70_logout_completed, 3000);
                    toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
                });
                xLogoutHelper.setOnLogOutFailedListener(() -> toast(R.string.hh70_cant_logout, 3000));
                xLogoutHelper.logout();
            } else {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.getLoginState();
    }
}
