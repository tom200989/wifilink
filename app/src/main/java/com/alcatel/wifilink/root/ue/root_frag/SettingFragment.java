package com.alcatel.wifilink.root.ue.root_frag;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.BoardSimHelper;
import com.alcatel.wifilink.root.helper.BoardWanHelper;
import com.alcatel.wifilink.root.helper.CheckBoardLogin;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.FirmUpgradeHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.UpgradeHelper;
import com.alcatel.wifilink.root.ue.root_activity.AboutActivity;
import com.alcatel.wifilink.root.ue.root_activity.EthernetWanConnectionActivity;
import com.alcatel.wifilink.root.ue.root_activity.HomeRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.LoginRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.SettingAccountActivity;
import com.alcatel.wifilink.root.ue.root_activity.SettingLanguageActivity;
import com.alcatel.wifilink.root.ue.root_activity.SettingShareActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SPUtils;
import com.alcatel.wifilink.root.utils.ScreenSize;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.alcatel.wifilink.root.widget.NormalWidget;
import com.alcatel.wifilink.root.widget.PopupWindows;
import com.p_numberbar.p_numberbar.core.NumberProgressBar;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDeviceNewVersionBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceBackupHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceRebootHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceResetHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceRestoreHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceUpdateStopHelper;

import java.util.Objects;

/**
 * Created by qianli.ma on 2017/6/16.
 */
@SuppressLint("ValidFragment")
public class SettingFragment extends Fragment implements View.OnClickListener, FragmentBackHandler {

    private final static String TAG = "SettingFragment";
    private final static String CONFIG_SPNAME = "config";
    private final static String CONFIG_FILE_PATH = "configFilePath";
    public final static int SET_LANGUAGE_REQUEST = 0x001;
    public static boolean isFtpSupported = false;
    public static boolean isDlnaSupported = false;
    public static boolean isSharingSupported = true;
    public static boolean m_blFirst = true;
    private int upgradeStatus;

    private RelativeLayout mLoginPassword;
    private RelativeLayout mMobileNetwork;
    private RelativeLayout mEthernetWan;
    private RelativeLayout mSharingService;
    private RelativeLayout mLanguage;
    private RelativeLayout mFirmwareUpgrade;
    private RelativeLayout mRestart;
    private RelativeLayout mBackup;
    private RelativeLayout mAbout;
    private NormalWidget dgSettingRxWidgetOk;
    private NormalWidget dgSettingRxWidgetConfirm;
    private View m_view;

    private static final int RESTART_RESET = 1;
    private static final int Backup_Restore = 2;
    private ProgressDialog mProgressDialog;

    private TextView mDeviceVersion;
    private final static String mdefaultSaveUrl = "/com/tcl/linkhub/backup";
    private final static String mdefaultSaveUrl2 = "/com/tcl/linkzone/backup";
    private static final int REQUEST_EXTERNAL_STORAGE = 123;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private ProgressDialog mCheckingDlg;
    private TimerHelper timerHelper;
    private AlertDialog mUpgradingDlg;
    private AlertDialog mUpgradedDlg;
    private final int PROGRESS_STYLE_WAITING = -1;
    ProgressBar mUpgradingProgressBar;
    TextView mUpgradingProgressValue;
    private TextView mMobileNetworkSimSocket;
    private TextView mMobileNetworkWanSocket;
    private TimerHelper checkTimer;
    private HomeRxActivity activity;
    private ProgressDialog pgd;
    private CheckBoardLogin checkBoardLogin;
    private RelativeLayout rl_wifiExtender;
    private View rl_feedback;
    private TextView tvExtenderOnOff;

    private PopupWindows pop_noNewVersion;
    private PopupWindows pop_downloading;
    boolean isDownloading = false;
    private TimerHelper downTimer;
    private BoardSimHelper simTimerHelper;
    private BoardWanHelper wanTimerHelper;
    private GetSystemInfoHelper xGetSystemInfoHelper;
    private Extender_GetWIFIExtenderSettingsHelper extenderHelper;
    private String off;
    private String on;
    private String noSimcard;
    private String detected;
    private String initing;
    private String perText;
    private ImageView mFirmwareUpgrade_dot;
    private int upgrade_Temp = 0;// 临时记录升级失效的次数
    private TextView tvLogout;
    int count = 0;
    private Handler handler;
    private boolean isMW120;
    private boolean isHH71;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (HomeRxActivity) getActivity();
        m_view = View.inflate(getActivity(), R.layout.fragment_home_setting, null);
        initRes();
        initSome();
        resetUi();
        init();
        initEvent();
        startTimer();
        return m_view;
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
        handler = new Handler();
        simTimerHelper = new BoardSimHelper(getActivity());
        wanTimerHelper = new BoardWanHelper(getActivity());
        extenderHelper = new Extender_GetWIFIExtenderSettingsHelper();

        extenderHelper.setOnStateEnableOnListener(stateEnable -> tvExtenderOnOff.setText(on));
        extenderHelper.setOnstateEnableOffListener(stateEnable -> tvExtenderOnOff.setText(off));
        extenderHelper.setOnGetExtenderFailedListener(() -> tvExtenderOnOff.setText(off));

        wanTimerHelper.setOnError(e -> mMobileNetworkWanSocket.setText(off));
        wanTimerHelper.setOnResultError(e -> mMobileNetworkWanSocket.setText(off));
        wanTimerHelper.setOnDisconnetingNextListener(wanResult -> mMobileNetworkWanSocket.setText(off));
        wanTimerHelper.setOnDisConnetedNextListener(wanResult -> mMobileNetworkWanSocket.setText(off));
        wanTimerHelper.setOnConnetingNextListener(wanResult -> mMobileNetworkWanSocket.setText(off));
        wanTimerHelper.setOnConnetedNextListener(wanResult -> mMobileNetworkWanSocket.setText(on));

        simTimerHelper.setOnRollRequestOnResultError(error -> mMobileNetworkSimSocket.setText(noSimcard));
        simTimerHelper.setOnRollRequestOnError(error -> mMobileNetworkSimSocket.setText(noSimcard));
        simTimerHelper.setOnNownListener(simStatus -> mMobileNetworkSimSocket.setText(noSimcard));
        simTimerHelper.setOnDetectedListener(simStatus -> mMobileNetworkSimSocket.setText(detected));
        simTimerHelper.setOnInitingListener(simStatus -> mMobileNetworkSimSocket.setText(initing));
        simTimerHelper.setOnPinRequireListener(result -> mMobileNetworkSimSocket.setText(off));
        simTimerHelper.setOnpukRequireListener(result -> mMobileNetworkSimSocket.setText(off));
        simTimerHelper.setOnpukTimeoutListener(result -> mMobileNetworkSimSocket.setText(off));
        simTimerHelper.setOnSimLockListener(result -> mMobileNetworkSimSocket.setText(off));
        simTimerHelper.setOnSimReadyListener(result -> mMobileNetworkSimSocket.setText(on));

        // 加入MW120设备类型判断
        xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(systemInfo -> {
            String deviceName = systemInfo.getDeviceName().toLowerCase();
            isMW120 = deviceName.startsWith(Cons.MW_SERIAL);
            isHH71 = deviceName.startsWith(Cons.HH71);
            mEthernetWan.setVisibility(isMW120 ? View.GONE : View.VISIBLE);
            rl_wifiExtender.setVisibility(isMW120 | isHH71 ? View.VISIBLE : View.GONE);
            rl_feedback.setVisibility(isMW120 ? View.VISIBLE : View.GONE);
            if (!isMW120) {// 不是MW120机型--> 请求wan口
                wanTimerHelper.boardTimer();
            } else {// 如果是MW120机型--> 请求extender接口
                extenderHelper.get();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        stopTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        Logs.t("ma_permission").vv("settingfragment pause");
        stopTimer();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {// 隐藏界面
            stopTimer();
        } else {
            startTimer();
            resetUi();
        }
    }

    private void resetUi() {
        if (activity == null) {
            activity = (HomeRxActivity) getActivity();
        }
        activity.tabFlag = Cons.TAB_SETTING;
        activity.llNavigation.setVisibility(View.VISIBLE);
        activity.rlBanner.setVisibility(View.GONE);// 已经自定义banner--> 此处设置公类的banner为gone
    }

    private void stopTimer() {
        if (checkTimer != null) {
            checkTimer.stop();
            checkTimer = null;
        }
    }

    private void startTimer() {
        if (checkTimer == null) {
            checkTimer = new TimerHelper(getActivity()) {
                @Override
                public void doSomething() {
                    // 检测WAN口 | SIM是否连接 | 是否为新设备
                    simTimerHelper.boardTimer();
                    xGetSystemInfoHelper.getSystemInfo();
                }
            };
        }
        checkTimer.start(2500);
        OtherUtils.timerList.add(checkTimer);
    }


    private void init() {
        tvLogout = (TextView) m_view.findViewById(R.id.tv_settingrx_logout);
        tvLogout.setOnClickListener(this);
        mLoginPassword = (RelativeLayout) m_view.findViewById(R.id.setting_login_password);
        mMobileNetwork = (RelativeLayout) m_view.findViewById(R.id.setting_mobile_network);
        mMobileNetworkSimSocket = (TextView) m_view.findViewById(R.id.tv_setting_sim_socket);// SIM开关显示
        mMobileNetworkWanSocket = (TextView) m_view.findViewById(R.id.tv_setting_wan_socket);// WAN开关显示
        mEthernetWan = (RelativeLayout) m_view.findViewById(R.id.setting_ethernet_wan);
        mSharingService = (RelativeLayout) m_view.findViewById(R.id.setting_sharing_service);
        mLanguage = (RelativeLayout) m_view.findViewById(R.id.setting_language);
        mFirmwareUpgrade = (RelativeLayout) m_view.findViewById(R.id.setting_firmware_upgrade);
        mFirmwareUpgrade_dot = (ImageView) m_view.findViewById(R.id.iv_setting_firmware_upgrade_tipdot);// 升级小点
        mRestart = (RelativeLayout) m_view.findViewById(R.id.setting_restart);
        mBackup = (RelativeLayout) m_view.findViewById(R.id.setting_backup);
        mAbout = (RelativeLayout) m_view.findViewById(R.id.setting_about);
        mDeviceVersion = (TextView) m_view.findViewById(R.id.setting_firmware_upgrade_version);

        rl_wifiExtender = (RelativeLayout) m_view.findViewById(R.id.rl_setting_wifi_extender);// wifi extender 面板
        rl_wifiExtender.setOnClickListener(this);
        tvExtenderOnOff = (TextView) m_view.findViewById(R.id.tv_wifi_extender_socket);

        rl_feedback = m_view.findViewById(R.id.setting_feedback);
        rl_feedback.setOnClickListener(this);

        dgSettingRxWidgetOk = (NormalWidget) m_view.findViewById(R.id.dg_settingRx_widget_ok);
        dgSettingRxWidgetConfirm = (NormalWidget) m_view.findViewById(R.id.dg_settingRx_widget_confirm);

        getDeviceFWCurrentVersion();
        showSharingService();
    }

    private void showSharingService() {
        OtherUtils otherUtils = new OtherUtils();
        otherUtils.setOnHwVersionListener(deviceVersion -> {
            Lgg.t(TAG).ii("device: " + deviceVersion);
            boolean hh4 = deviceVersion.toLowerCase().contains("hh4");
            boolean mw = deviceVersion.toLowerCase().contains("mw");
            boolean hh71 = deviceVersion.toLowerCase().contains("hh71");
            mSharingService.setVisibility(hh4 | mw | hh71 ? View.GONE : View.VISIBLE);
        });
        otherUtils.getDeviceHWVersion();
    }

    private void initEvent() {
        mLoginPassword.setOnClickListener(this);
        mMobileNetwork.setOnClickListener(this);
        mEthernetWan.setOnClickListener(this);
        mSharingService.setOnClickListener(this);
        mLanguage.setOnClickListener(this);
        mFirmwareUpgrade.setOnClickListener(this);
        mRestart.setOnClickListener(this);
        mBackup.setOnClickListener(this);
        mAbout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_settingrx_logout:// 登出
                logout();
                break;
            case R.id.setting_login_password:// 修改密码
                goToAccountSettingPage();
                break;
            case R.id.setting_mobile_network:// 进入Mobile network
                goToMobileNetworkSettingPage();
                break;
            case R.id.setting_ethernet_wan:// 进入wan info
                goEthernetWanConnectionPage();
                break;
            case R.id.setting_sharing_service:// 进入USB界面
                if (isSharingSupported) {
                    goToShareSettingPage();
                } else {
                    ToastUtil_m.show(getActivity(), getString(R.string.setting_not_support_sharing_service));
                }
                break;
            case R.id.setting_language:// 进入语言设置
                goSettingLanguagePage();
                break;
            case R.id.setting_firmware_upgrade:// 点击升级
                clickUpgrade();
                break;
            case R.id.setting_restart:// 重启设备
                popDialogFromBottom(RESTART_RESET);
                break;
            case R.id.setting_backup:// 备份配置
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    verifyStoragePermissions(getActivity());
                }
                break;
            case R.id.setting_about:// 进入about
                goToAboutSettingPage();
                break;

            case R.id.rl_setting_wifi_extender:// 进入wifi extender
                goToWifiExtender();
                break;
            case R.id.setting_feedback:// 进入feedback
                goToFeedback();  // 上线时把这句打开
                break;
            default:
                break;
        }
    }

    /**
     * U1.点击了升级
     */
    private void clickUpgrade() {
        FirmUpgradeHelper fh = new FirmUpgradeHelper(getActivity(), true);
        fh.setOnNoNewVersionListener(this::popversion);
        fh.setOnNewVersionListener(attr -> popversion(attr, null));// 有新版本
        fh.checkNewVersion();
    }

    /**
     * U2.弹窗(有新版本 | 没有新版本)
     *
     * @param updateDeviceNewVersion
     */
    private void popversion(GetDeviceNewVersionBean updateDeviceNewVersion, GetSystemInfoBean systemSystemInfo) {
        // 1.获取状态 ( NO_NEW_VERSION || CHECK_ERROR 均为没有版本可以升级 )
        boolean noNewVersion = updateDeviceNewVersion.getState() == GetDeviceNewVersionBean.CONS_NO_NEW_VERSION || updateDeviceNewVersion.getState() == GetDeviceNewVersionBean.CONS_CHECK_ERROR;
        // 2.显示弹窗
        Drawable pop_bg = getResources().getDrawable(R.drawable.bg_pop_conner);
        View inflate = View.inflate(getActivity(), R.layout.pop_setting_upgrade_checkversion, null);
        ScreenSize.SizeBean size = ScreenSize.getSize(getActivity());
        int width = (int) (size.width * 0.85f);
        int height = (int) (size.height * 0.21f);
        // 3.修改弹窗属性信息
        TextView versionName = (TextView) inflate.findViewById(R.id.tv_pop_setting_rx_upgrade_noNewVersion_version);
        TextView ok = (TextView) inflate.findViewById(R.id.tv_pop_setting_rx_upgrade_ok);
        TextView tip = (TextView) inflate.findViewById(R.id.tv_pop_setting_rx_upgrade_noNewVersion_tip);
        // 3.1.同上
        versionName.setText(noNewVersion ? systemSystemInfo.getSwVersionMain() : updateDeviceNewVersion.getVersion() + " " + getString(R.string.available));
        tip.setVisibility(noNewVersion ? View.VISIBLE : View.GONE);
        ok.setText(noNewVersion ? getString(R.string.ok) : getString(R.string.setting_upgrade));
        ok.setOnClickListener(v -> {
            // 版本弹窗消隐
            pop_noNewVersion.dismiss();
            // 需要升级的话则弹出新的确认升级的弹窗
            if (!noNewVersion) {
                dgSettingRxWidgetOk.setVisibility(View.VISIBLE);
                dgSettingRxWidgetOk.setTitle(R.string.setting_upgrade);
                dgSettingRxWidgetOk.setDes(R.string.setting_upgrade_firmware_warning);
                dgSettingRxWidgetOk.setOnBgClickListener(() -> Lgg.t(TAG).ii("click not area"));
                dgSettingRxWidgetOk.setOnCancelClickListener(() -> dgSettingRxWidgetOk.setVisibility(View.GONE));
                dgSettingRxWidgetOk.setOnOkClickListener(this::beginDownLoadFOTA);
            }
        });
        pop_noNewVersion = null;
        pop_noNewVersion = new PopupWindows(getActivity(), inflate, width, height, true, pop_bg);
    }

    /**
     * U3.触发FOTA下载
     */
    private void beginDownLoadFOTA() {
        count = 0;
        isDownloading = true;
        FirmUpgradeHelper fuh = new FirmUpgradeHelper(getActivity(), false);
        fuh.setOnErrorListener(() -> {
            count = 0;
            toast(R.string.connect_failed);
            isDownloading = false;
        });
        // 触发成功
        fuh.setOnSetFOTADownSuccessListener(() -> {
            /* 1.显示进度弹窗 */
            Drawable pop_bg = getResources().getDrawable(R.drawable.bg_pop_conner);
            View v = View.inflate(getActivity(), R.layout.pop_setting_dowing, null);
            ScreenSize.SizeBean size = ScreenSize.getSize(getActivity());
            int width = (int) (size.width * 0.85f);
            int height = (int) (size.height * 0.24f);
            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.rl_pop_setting_download_all);
            rl.setOnClickListener(null);
            TextView per = (TextView) v.findViewById(R.id.tv_pop_setting_download_per);
            NumberProgressBar progressBar = (NumberProgressBar) v.findViewById(R.id.pg_pop_setting_download);
            TextView cancel = (TextView) v.findViewById(R.id.tv_pop_setting_download_cancel);
            cancel.setOnClickListener(v1 -> {
                // 1.1.消隐
                pop_downloading.dismiss();
                // 1.2.停止定时器
                stopDownTimerAndPop();
                // 1.3.请求停止
                SetDeviceUpdateStopHelper xSetDeviceUpdateStopHelper = new SetDeviceUpdateStopHelper();
                xSetDeviceUpdateStopHelper.setOnSetDeviceUpdateStopSuccessListener(() -> {
                    stopDownTimerAndPop();
                    toast(R.string.setting_upgrade_stop_error);
                    isDownloading = false;
                });
                xSetDeviceUpdateStopHelper.setOnSetDeviceUpdateStopFailedListener(() -> {
                    downError(-1);
                });
                xSetDeviceUpdateStopHelper.setDeviceUpdateStop();
            });

            /* 2.启动定时器 */
            UpgradeHelper uh = new UpgradeHelper(getActivity(), false);
            uh.setOnErrorListener(() -> downError(R.string.setting_upgrade_get_update_state_failed));
            uh.setOnResultErrorListener(() -> downError(R.string.setting_upgrade_get_update_state_failed));
            uh.setOnNoStartUpdateListener(attr1 -> {
                per.setText(String.valueOf(attr1.getProcess() + perText));
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
                per.setText(String.valueOf(attr1.getProcess() + perText));// 显示百分比
                progressBar.setProgress(attr1.getProcess());// 进度条显示进度
            });
            uh.setOnCompleteListener(attr1 -> {
                if (count == 0) {
                    // 2.1.显示进度
                    per.setText(String.valueOf("100%"));
                    progressBar.setProgress(100);// 进度条显示进度
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
            // SIM状态
            BoardSimHelper bsh = new BoardSimHelper(getActivity());
            bsh.setOnRollRequestOnError(e -> downError(-1));
            bsh.setOnRollRequestOnResultError(error -> downError(-1));
            bsh.setOnNormalSimstatusListener(simStatus -> {
                int simState = simStatus.getSIMState();
                if (simState == Cons.READY) {
                    // 获取下载进度
                    uh.getDownState();
                } else {
                    // 弹出提示并隐藏弹窗
                    downError(R.string.qs_pin_unlock_can_not_connect_des);
                }
            });

            // WAN状态
            BoardWanHelper bwh = new BoardWanHelper(getActivity());
            bwh.setOnError(e -> bsh.boardTimer());
            bwh.setOnResultError(error -> bsh.boardTimer());
            bwh.setOnDisConnetedNextListener(wanResult -> bsh.boardTimer());
            bwh.setOnDisconnetingNextListener(wanResult -> bsh.boardTimer());
            bwh.setOnConnetingNextListener(wanResult -> bsh.boardTimer());
            bwh.setOnConnetedNextListener(wanResult -> uh.getDownState());// 请求下载进度

            /* -------------------------------------------- 定时器获取WAN以及SIM的连接状态 -------------------------------------------- */

            // 创建定时器
            downTimer = new TimerHelper(getActivity()) {
                @Override
                public void doSomething() {
                    // 轮训WAN口--> 成功:获取进度 + 失败: 获取SIM卡--> 成功:获取进度 + 失败:提示
                    bwh.boardTimer();
                }
            };
            downTimer.start(3000);
            pop_downloading = null;
            pop_downloading = new PopupWindows(getActivity(), v, width, height, false, pop_bg);
        });
        fuh.triggerFOTA();// 触发FOTA下载
    }

    /**
     * U4.触发硬件底层进行升级操作
     */
    private void triggerFOTAUpgrade() {
        dgSettingRxWidgetConfirm.setVisibility(View.VISIBLE);
        dgSettingRxWidgetConfirm.setTitle(R.string.setting_upgrade);
        dgSettingRxWidgetConfirm.setDes(R.string.setting_upgrade_firmware_warning);
        dgSettingRxWidgetConfirm.setOnBgClickListener(() -> Lgg.t("mainrx").ii("click not area"));
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
        FirmUpgradeHelper fuh = new FirmUpgradeHelper(getActivity(), false);
        fuh.setOnErrorListener(() -> downError(R.string.setting_upgrade_start_update_failed));
        fuh.setOnStartUpgradeListener(() -> {
            toast(R.string.device_will_restart_later);
            OtherUtils.showProgressPop(getActivity(), getString(R.string.updating));
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
        PopupWindow popupWindow = new PopupWindow(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_from_bottom, null);

        TextView mFirstTxt = (TextView) view.findViewById(R.id.first_txt);
        TextView mSecondTxt = (TextView) view.findViewById(R.id.second_txt);
        TextView mCancelTxt = (TextView) view.findViewById(R.id.cancel_txt);

        popupWindow.setContentView(view);
        if (itemType == RESTART_RESET) {
            mFirstTxt.setText(R.string.restart);
            mSecondTxt.setText(R.string.reset_to_factory_settings);

        } else {
            mFirstTxt.setText(R.string.backup);
            mSecondTxt.setText(R.string.restore);
        }

        mFirstTxt.setOnClickListener(v -> {
            if (itemType == RESTART_RESET) {
                restartDevice();
            } else {
                backupDevice();
            }
            popupWindow.dismiss();
        });
        mSecondTxt.setOnClickListener(v -> {
            if (itemType == RESTART_RESET) {
                showDialogResetFactorySetting();
            } else {
                restore();
            }
            popupWindow.dismiss();
        });
        mCancelTxt.setOnClickListener(v -> {
            popupWindow.dismiss();
            backgroundAlpha(getActivity(), 1f);
        });
        popupWindow.setOnDismissListener(() -> backgroundAlpha(getActivity(), 1f));
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.white));
        backgroundAlpha(getActivity(), 0.5f);
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    public int requestTimes = 0;

    private void backupDevice() {
        showBackupSuccessDialog();
    }

    private void showBackupSuccessDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.back_up_settings);
        EditText editText = new EditText(getActivity());
        LinearLayout.LayoutParams LayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LayoutParams.setMargins(20, 0, 20, 0);
        editText.setLayoutParams(LayoutParams);
        editText.setText(isMW120 ? mdefaultSaveUrl2 : mdefaultSaveUrl);// mw120就用mw120的地址
        builder.setView(editText);
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss());
        builder.setPositiveButton(R.string.backup, (dialog, which) -> {
            String savePath = editText.getText().toString();
            SPUtils.getInstance(CONFIG_SPNAME, getActivity()).put(CONFIG_FILE_PATH, savePath);
            downLoadConfigureFile(savePath);
            dialog.dismiss();
        });
        builder.show();
    }

    private void verifyStoragePermissions(Activity activity) {

        int permission = ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PackageManager.PERMISSION_DENIED) {// 权限本身不允许--> 请求
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            Logs.t("ma_permission").vv("requestPermissions");
        } else {// 权限本身允许--> 执行业务逻辑
            popDialogFromBottom(Backup_Restore);
        }
    }

    private void downLoadConfigureFile(String saveUrl) {
        SetDeviceBackupHelper xSetDeviceBackup = new SetDeviceBackupHelper();
        xSetDeviceBackup.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceBackup.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceBackup.setOnDownSuccessListener(attr -> ToastUtil_m.show(getActivity(), R.string.succeed));
        xSetDeviceBackup.setOnSetDeviceBackupFailedListener(() -> ToastUtil_m.show(getActivity(), R.string.fail));
        xSetDeviceBackup.setOnPathNotMatchRuleListener(path -> {
            Lgg.t("ma_path").ee("path is empty or not match rule , it contains [\\ : * ? \" < > | .]");
            ToastUtil_m.show(getActivity(), "path is empty or not match rule , it contains [\\ : * ? \" < > | .]");
        });
        xSetDeviceBackup.setDeviceBackup(saveUrl);
    }

    private void showDialogResetFactorySetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.reset_router);
        builder.setMessage(R.string.This_will_reset_all_settings_on_your_router_to_factory_defaults_This_action_can_not_be_undone);
        builder.setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss());
        builder.setPositiveButton(R.string.reset, (dialogInterface, i) -> resetDevice());
        builder.show();
    }

    private void restartDevice() {
        SetDeviceRebootHelper xSetDeviceRebootHelper = new SetDeviceRebootHelper();
        xSetDeviceRebootHelper.setOnSetDeviceRebootSuccessListener(() -> ToastUtil_m.show(getActivity(), R.string.succeed));
        xSetDeviceRebootHelper.setOnSetDeviceRebootFailedListener(() -> ToastUtil_m.show(getActivity(), R.string.fail));
        xSetDeviceRebootHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceRebootHelper.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceRebootHelper.SetDeviceReboot();
    }


    private void resetDevice() {
        SetDeviceResetHelper xSetDeviceResetHelper = new SetDeviceResetHelper();
        xSetDeviceResetHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceResetHelper.setOnSetDeviceResetSuccessListener(() -> {
            dismissLoadingDialog();
            showSuccessDialog();
        });
        xSetDeviceResetHelper.setOnSetDeviceResetFailedListener(() -> {
            dismissLoadingDialog();
            showFailedDialog(R.string.couldn_t_reset_try_again);
        });
        xSetDeviceResetHelper.SetDeviceReset();
    }

    public int restoreTimes = 0;

    private void restore() {
        SetDeviceRestoreHelper xSetDeviceRestoreHelper = new SetDeviceRestoreHelper();
        xSetDeviceRestoreHelper.setOnPrepareHelperListener(this::showLoadingDialog);
        xSetDeviceRestoreHelper.setOnDoneHelperListener(this::dismissLoadingDialog);
        xSetDeviceRestoreHelper.setOnRestoreSuccessListener(file -> ToastUtil_m.show(getActivity(), R.string.succeed));
        xSetDeviceRestoreHelper.setOnRestoreFailedListener(() -> ToastUtil_m.show(getActivity(), R.string.couldn_t_restore_try_again));
        xSetDeviceRestoreHelper.setDeviceRestore();
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setMessage(activity.getString(R.string.complete));
        builder.setCancelable(true);
        builder.show();
    }

    private void showFailedDialog(int stringId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()));
        builder.setMessage(getActivity().getString(stringId));
        builder.setCancelable(true);
        builder.show();
    }

    private void showLoadingDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());

        }
        mProgressDialog.setMessage(activity.getString(R.string.back_up_progress));
        mProgressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }


    /* -------------------------------------------- HELPER -------------------------------------------- */
    /* -------------------------------------------- HELPER -------------------------------------------- */
    private void goToShareSettingPage() {
        Intent intent = new Intent(activity, SettingShareActivity.class);
        activity.startActivity(intent);
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
        UpgradeHelper uh = new UpgradeHelper(getActivity(), false);
        uh.setOnNewVersionListener(attr -> mFirmwareUpgrade_dot.setVisibility(View.VISIBLE));
        uh.checkVersion();
    }

    private void showCheckingDlg() {
        if (getActivity().isFinishing()) {
            return;
        }
        if (mCheckingDlg == null) {
            mCheckingDlg = new ProgressDialog(getActivity());
            mCheckingDlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mCheckingDlg.setMessage(getString(R.string.checking_for_update));
            mCheckingDlg.setTitle("");
            mCheckingDlg.setCancelable(false);
            mCheckingDlg.setCanceledOnTouchOutside(false);
            mCheckingDlg.show();
        } else if (!mCheckingDlg.isShowing()) {
            mCheckingDlg.show();
        }
    }

    private void goSettingLanguagePage() {
        Intent intent = new Intent(getActivity(), SettingLanguageActivity.class);
        startActivityForResult(intent, SET_LANGUAGE_REQUEST);
    }

    private void goToAccountSettingPage() {
        Intent intent = new Intent(getActivity(), SettingAccountActivity.class);
        activity.startActivity(intent);
    }

    /**
     * 进入Mobile network
     */
    private void goToMobileNetworkSettingPage() {
        activity.fraHelpers.transfer(activity.clazz[Cons.TAB_MOBILE_NETWORK]);
    }

    /**
     * 进入ethernet wan conncet
     */
    private void goEthernetWanConnectionPage() {
        Intent intent = new Intent(getActivity(), EthernetWanConnectionActivity.class);
        getActivity().startActivity(intent);
    }

    /**
     * 进入device setting
     */
    // private void goToDeviceSettingPage() {
    //     CA.toActivity(activity, SettingDeviceActivity.class, false, true, false, 0);
    // }

    /**
     * 进入wifi extender
     */
    private void goToWifiExtender() {
        activity.fraHelpers.transfer(activity.clazz[Cons.TAB_WIFI_EXTENDER]);
    }

    /**
     * 进入feedback
     */
    private void goToFeedback() {
        activity.fraHelpers.transfer(activity.clazz[Cons.TAB_FEEDBACK]);
    }

    private void goToAboutSettingPage() {
        Intent intent = new Intent(getActivity(), AboutActivity.class);
        intent.putExtra("upgradeStatus", upgradeStatus);
        getActivity().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SettingFragment.SET_LANGUAGE_REQUEST) {
            if (data != null && data.getBooleanExtra(SettingLanguageActivity.IS_SWITCH_LANGUAGE, false)) {
                // 切换语言(重新加载fragment) + 跳转到setting-fragment
                // activity.tvTabHome.setText(R.string.main_home);
                // activity.tvTabWifi.setText(R.string.setting_wifi);
                // activity.tvTabSms.setText(R.string.main_sms);
                // activity.tvTabSetting.setText(R.string.main_setting);
                try {
                    activity.reloadFragment(activity.clazz[Cons.TAB_SETTING]);
                    activity.fraHelpers.transfer(activity.clazz[Cons.TAB_SETTING]);
                } catch (Exception e) {
                    activity.fraHelpers.transfer(activity.clazz[Cons.TAB_SETTING]);
                }
                // handler.postDelayed(() -> {
                //     activity.fraHelpers.transfer(activity.clazz[Cons.TAB_SETTING]);
                // }, 2000);

            }
        }
    }

    private void toast(int resId) {
        ToastUtil_m.show(getActivity(), resId);
    }

    private void toastLong(int resId) {
        ToastUtil_m.showLong(getActivity(), resId);
    }

    private void toast(String content) {
        ToastUtil_m.show(getActivity(), content);
    }

    private void to(Class ac, boolean isFinish) {
        CA.toActivity(getActivity(), ac, false, isFinish, false, 0);
    }

    /**
     * 停止下载定时器
     */
    public void stopDownTimerAndPop() {
        count = 0;
        if (pop_downloading != null) {
            pop_downloading.dismiss();
        }
        if (downTimer != null) {
            downTimer.stop();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (dgSettingRxWidgetOk.getVisibility() == View.VISIBLE) {
            dgSettingRxWidgetOk.setVisibility(View.GONE);
            return true;
        } else if (dgSettingRxWidgetConfirm.getVisibility() == View.VISIBLE) {
            dgSettingRxWidgetConfirm.setVisibility(View.GONE);
            return true;
        }
        // 如果在下载中, 则自己处理返回按钮的逻辑
        return isDownloading;
    }

    /**
     * 退出
     */
    private void logout() {
        OtherUtils.clearAllTimer();
        LogoutHelper xLogouthelper = new LogoutHelper();
        xLogouthelper.setOnLogoutSuccessListener(() -> to(LoginRxActivity.class, true));
        xLogouthelper.setOnLogOutFailedListener(() -> ToastUtil_m.show(getActivity(), getString(R.string.login_logout_failed)));
        xLogouthelper.logout();
    }
}
