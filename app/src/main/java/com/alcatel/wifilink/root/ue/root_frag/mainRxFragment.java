package com.alcatel.wifilink.root.ue.root_frag;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.Extender_GetWIFIExtenderCurrentStatusResult;
import com.alcatel.wifilink.root.helper.BoardSimHelper;
import com.alcatel.wifilink.root.helper.BoardWanHelper;
import com.alcatel.wifilink.root.helper.ConnectSettingHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.NetworkInfoHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.UsageHelper;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.ue.root_activity.ActivityDeviceManager;
import com.alcatel.wifilink.root.ue.root_activity.HomeRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.InternetStatusActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.C_Constants;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.alcatel.wifilink.root.widget.MainMW70BatteryView;
import com.alcatel.wifilink.root.widget.MainMW70BottomView;
import com.alcatel.wifilink.root.widget.NormalWidget;
import com.de.wave.core.WaveView;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qianli.ma on 2017/11/21 0021.
 */

// TOGO 2019/8/19 0019 mainFrag
@Deprecated
public class mainRxFragment extends Fragment implements FragmentBackHandler {

    Unbinder unbinder1;

    @BindView(R.id.rl_mainrx_mw120_network_panel)
    RelativeLayout rlMainrxMw120NetworkPanel;// MW120下的network面板显示
    @BindView(R.id.tv_mainrx_mw120_networkType)
    TextView tvMainrxMw120NetworkType;// MW120下的network显示

    @BindView(R.id.bt_mainrx_wanConnect)
    Button btWanConnect;// WAN口连接
    @BindView(R.id.bt_mainrx_simUnConnected)
    Button btSimUnConnected;// SIM卡未连接
    @BindView(R.id.rl_mainrx_simConnected)
    RelativeLayout rlSimConnected;// SIM卡已连接(面板)
    @BindView(R.id.bt_mainrx_simConnected)
    WaveView btSimConnected;// SIM卡已连接(波浪视图)
    @BindView(R.id.bt_mainrx_simLocked)
    Button btSimLocked;// SIM卡被锁定状态
    @BindView(R.id.bt_mainrx_simNown)
    Button btSimNown;// SIM卡未知状态
    @BindView(R.id.bt_mainrx_extender)
    Button btMainrxExtender;// extender状态

    @BindView(R.id.tv_mainrx_usedUnit)
    TextView tvUsedUnit;// 已使用流量单位
    @BindView(R.id.tv_mainrx_usedData)
    TextView tvUsedData;// 已使用流量
    @BindView(R.id.tv_mainrx_usedTotal)
    TextView tvUsedTotal;// 总流量

    @BindView(R.id.tv_mainrx_networkType)
    TextView tvNetworkType;// 运营商

    @BindView(R.id.dw_mainrx)
    WaveView dynamicWave;// 中间动态波浪(无实际意义)

    @BindView(R.id.rl_mainrx_signal_panel)
    PercentRelativeLayout rlSignalPanel;// 信号面板
    @BindView(R.id.iv_mainrx_signal)
    ImageView ivSignal;// 信号图标
    @BindView(R.id.tv_mainrx_signal)
    TextView tvSignal;// 信号类型文本

    @BindView(R.id.rl_mainrx_connectedPeople_panel)
    PercentRelativeLayout rlConnectedPeoplePanel;// 连接面板
    @BindView(R.id.iv_mainrx_connectedPeople)
    ImageView ivConnectedPeople;// 连接人数图标
    @BindView(R.id.tv_mainrx_connectedPeople)
    TextView tvConnectedPeople;// 连接人数文本

    Unbinder unbinder;
    @BindView(R.id.rl_mainrx_mw70_panel)
    MainMW70BottomView rlMainrxMw70BottomPanel;// MW70新型设备需要显示的面板

    @BindView(R.id.rl_mainrx_mw70_battery_extender)
    MainMW70BatteryView rlMainrxMw70BatteryExtender;// MW70新型设备需要显示的电池面板

    @BindView(R.id.dg_mainrx_widget_ok)
    NormalWidget wdOK;// 确定取消面板

    private View inflate;
    private String BLANK_TEXT = " ";
    private String NONE_TEXT = "- -";
    private String noUsagePlan;
    private String useOf;
    private int WAN_CONNECT_MODE = 0;// wan口连接模式
    private int SIM_DISCONNECT_MODE = 1;// SIM卡未连接模式(已解锁)
    private int SIM_CONNECT_MODE = 2;// SIM卡连接模式
    private int SIM_LOCKED = 3;// SIM被锁定状态
    private int SIM_NOWN = 4;// SIM被拔出
    private String circlrDotColor = "#3798f4";// 圆环颜色
    private String behindColor_nor = "#AA00cd3a";// 圆环波浪底色(正常)
    private String frontColor_nor = "#AA00cd3a";// 圆环波浪前景色(正常)
    private String behindColor_over = "#AAf5ac1f";// 圆环波浪底色(超标)
    private String frontColor_over = "#f5ac1f";// 圆环波浪前景色(超标)
    private String behindColor_dynamic = "#AAFFFFFF";// 中间波浪底色
    private String frontColor_dynamic = "#FFFFFF";// 中间波浪前景色
    private String signal_no_text = "--";// 没有信号文本
    private String mb_unit;// 单位MB
    private String gb_unit;// 单位GB
    private Drawable[] signals;// 信号集合
    private Drawable signal0;// 信号0
    private Drawable signal1;// 信号1
    private Drawable signal2;// 信号2
    private Drawable signal3;// 信号3
    private Drawable signal4;// 信号4
    private Drawable signal5;// 信号5
    private Drawable signalR;// 信号漫游
    private String signal_2G;// 2G信号
    private String signal_3G;// 3G信号
    private String signal_3G_plus;// 超3G信号
    private String signal_4G;// 4G信号
    private String signal_text;// SIGNAL文本
    private Drawable connected_none;// 没有连接数
    private Drawable connected_more;// 有连接数
    private String connected_text;// 连接文本
    private View[] buttonsView;
    private TimerHelper timer;
    private int blue_color;
    private int gray_color;

    private BoardWanHelper wanHelper;
    private BoardWanHelper wanHelperClick;
    private BoardSimHelper simHelper;
    private BoardSimHelper simHelper_simNotConnect;
    private BoardSimHelper simHelper_simConnect;
    private BoardSimHelper simHelper_simLocked;
    private NetworkInfoHelper networkHelper;
    private GetConnectionStateHelper xGetConnectionStateHelper;
    private GetSystemInfoHelper xGetSystemInfoHelper;
    private Extender_GetWIFIExtenderSettingsHelper extenderHelper;
    private GetSystemStatusHelper xGetSystemStatusHelper;
    private GetSystemStatusHelper xsystemStatuHelper_for_russia;

    private HomeRxActivity activity;
    private Class[] fragmentClazz;// fragment集合(在HomeRxActivity)
    private String unlockSim_title;
    private String unlockSim_content;
    private String cancel_text;
    private String confirm_text;
    private int usageLimit;// 默认的流量限度(用于显示波浪颜色)
    private ProgressDialog pgdWait;
    private int deviceSize;// 当前连接设备数
    private boolean isMW120;// 是否为mw120
    private boolean isNoService = true;// 是否当前不在服务区(默认不在)
    private int currentConnection = Cons.WAN_CONNECTION;// 当前网络状态(默认WAN口) 

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = one();
        unbinder1 = ButterKnife.bind(this, inflate);
        return inflate;
    }

    private View one() {
        inflate = View.inflate(getActivity(), R.layout.fragment_mainrx, null);
        unbinder = ButterKnife.bind(this, inflate);
        activity = (HomeRxActivity) getActivity();
        fragmentClazz = activity.clazz;
        SP.getInstance(getActivity()).putInt(Cons.TAB_FRA, Cons.TAB_MAIN);
        showPgd();
        initRes();
        resetUi();
        initView();
        initHelper();
        startTimer();
        return inflate;

    }

    // TOGO 2019/8/19 0019 
    /**
     * 全局等待
     */
    private void showPgd() {
        if (pgdWait == null) {
            pgdWait = OtherUtils.showProgressPop(getActivity());
        } else {
            pgdWait.show();
        }
    }

    // TOGO 2019/8/19 0019 
    private void initHelper() {
        wanHelper = new BoardWanHelper(getActivity());
        wanHelperClick = new BoardWanHelper(getActivity());
        simHelper = new BoardSimHelper(getActivity());
        simHelper_simNotConnect = new BoardSimHelper(getActivity());
        simHelper_simConnect = new BoardSimHelper(getActivity());
        simHelper_simLocked = new BoardSimHelper(getActivity());
        xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetSystemInfoHelper = new GetSystemInfoHelper();
        extenderHelper = new Extender_GetWIFIExtenderSettingsHelper();

        // togo 定时检测是否为MW120新型设备
        xGetSystemInfoHelper.setOnFwErrorListener(() -> getSystemInfoError());/* 1.1.发生错误先走wan口 */
        xGetSystemInfoHelper.setOnAppErrorListener(() -> getSystemInfoError());/* 1.1.发生错误先走wan口 */
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(this::judgeDevices);/* 1.1.否则判断设备类型 */

        // togo 定时检测wifi extender是否有开启
        extenderHelper.setOnGetExtenderFailedListener(() -> setMainMW120NetworkAndBatteryUi(false));
        extenderHelper.setOnstateEnableOffListener(stateEnable -> setMainMW120NetworkAndBatteryUi(stateEnable == 1));
        extenderHelper.setOnCurrentHotpotListener(this::showExtenderStrength);

        // togo 定时获取电池状态以及信号强度等状态
        xGetSystemStatusHelper = new GetSystemStatusHelper();
        xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(this::showBatteryAndSignalUi);
        xGetSystemStatusHelper.setOnGetSystemStatusFailedListener(() -> showBatteryAndSignalUi(null));

        // togo 应俄罗斯要求, 先判断当前currentconnection状态, 取消wan口优先原则
        xsystemStatuHelper_for_russia = new GetSystemStatusHelper();
        xsystemStatuHelper_for_russia.setOnGetSystemStatusFailedListener(() -> currentConnection = 0);// 出错--> 无网络
        xsystemStatuHelper_for_russia.setOnGetSystemStatusSuccessListener(getSystemStatusBean -> currentConnection = getSystemStatusBean.getCurrentConnection());

        // togo 定时获取WAN口状态
        wanHelper.setOnResultError(error -> getSim());// 出错
        wanHelper.setOnError(e -> getSim());// 出错
        // wanHelper.setOnConnetedNextListener(wanResult -> getDevicesType());// 显示wan
        wanHelper.setOnConnetedNextListener(wanResult -> wanFirst());// 显示wan
        wanHelper.setOnConnetingNextListener(wanResult -> getSim());// 获取SIM
        wanHelper.setOnDisConnetedNextListener(wanResult -> getSim());// 获取sim
        wanHelper.setOnDisconnetingNextListener(wanResult -> getSim());// 获取sim

        // togo 定时获取sim状态 
        simHelper.setOnNownListener(simStatus -> simNotReady());
        simHelper.setOnSimReadyListener(result -> simReady());
        simHelper.setOnSimLockListener(simStatus -> pinPukSimLock());
        simHelper.setOnPinRequireListener(result -> pinPukSimLock());
        simHelper.setOnpukRequireListener(result -> pinPukSimLock());
        simHelper.setOnpukTimeoutListener(result -> simNotReady());
        simHelper.setOnInitingListener(simStatus -> simNotReady());
        simHelper.setOnDetectedListener(simStatus -> simNotReady());
        simHelper.setOnNownListener(simStatus -> simNown());
        simHelper.setOnRollRequestOnError(e -> simNotReady());
        simHelper.setOnRollRequestOnResultError(error -> simNotReady());

        // togo 定时获取连接状态
        xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnDisConnectingListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnDisconnectedListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnConnectingListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnConnectedListener(() -> buttonUi(SIM_CONNECT_MODE));
        xGetConnectionStateHelper.getConnectionState();
    }

    // TOGO 2019/8/19 0019 
    /**
     * 设置wifi extender开启与关闭情况下的network显示以及电池、信号面板的显示
     *
     * @param isExtenderOn
     */
    private void setMainMW120NetworkAndBatteryUi(boolean isExtenderOn) {
        // 1.MW120下的左上角network面板
        // --> 是否开启了wifi extender?
        //      --> 开启: 隐藏
        //      --> 关闭: 是否在服务区有效范围内？
        //              --> 是: 显示; 否: 隐藏
        rlMainrxMw120NetworkPanel.setVisibility(isExtenderOn ? View.GONE : isNoService ? View.GONE : View.VISIBLE);
        // 2.wifi extender的启闭状态(针对自定义控件使用)
        rlMainrxMw70BatteryExtender.setWifiExtenderState(isExtenderOn);
        // 3.wifi extender下的中心按钮UI
        btMainrxExtender.setVisibility(isExtenderOn ? View.VISIBLE : View.GONE);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 显示extender强度以及SSID
     *
     * @param result
     */
    private void showExtenderStrength(Extender_GetWIFIExtenderCurrentStatusResult result) {
        if (result != null) {
            // 1.切换network、wifi extender的启闭状态、wifi extender下的中心按钮UI
            setMainMW120NetworkAndBatteryUi(result.getHotspotConnectStatus() == 2);
            // 2.设置wifi强度
            rlMainrxMw70BatteryExtender.setWifiStrength(result.getSignal());
            // 3.设置SSID
            String ssid = TextUtils.isEmpty(result.getHotspotSSID()) ? getString(R.string.unknown) : result.getHotspotSSID();
            ssid = OtherUtils.turnUrlCode(ssid);
            rlMainrxMw70BatteryExtender.setWifiName(ssid);// SSID
        }
    }

    // TOGO 2019/8/19 0019 

    /**
     * 判断新型设备
     *
     * @param systemSystemInfo
     */
    private void judgeDevices(GetSystemInfoBean systemSystemInfo) {
        // 1.是否为新型MW120设备
        isMW120 = systemSystemInfo.getDeviceName().toLowerCase().startsWith(Cons.MW_SERIAL);
        // 2.显示MW120 ui与否
        ShowMW120Ui(isMW120);
        // 3.根据情况走接口
        if (isMW120) {// 3.1.如果是MW120设备--> 则获取sim卡\wifi-Extender\SystemStatus
            getSim();
            getExtender();// 拿wifi extender信息
            getSystemStatus();// 拿电池状态

        } else {// 3.1.如果非mw120设备
            getWan();
        }

    }

    // TOGO 2019/8/19 0019 
    /**
     * 获取system status来查看电池信息
     */
    private void getSystemStatus() {
        xGetSystemStatusHelper.getSystemStatus();
    }

    // TOGO 2019/8/19 0019 
    /**
     * 显示电池电量以及进度
     *
     * @param systemSystemStates
     */
    private void showBatteryAndSignalUi(GetSystemStatusBean systemSystemStates) {
        if (systemSystemStates != null) {
            boolean isCharing = systemSystemStates.getChg_state() == Cons.BATTERY_CHARING;// 电池状态
            int cap = systemSystemStates.getBat_cap();// 电池电量
            rlMainrxMw70BatteryExtender.setBattery(isCharing, cap);
            rlMainrxMw70BatteryExtender.setSignal(systemSystemStates.getSignalStrength());
            rlMainrxMw70BatteryExtender.setNetworkText(systemSystemStates.getNetworkType());
        } else {
            rlMainrxMw70BatteryExtender.setBattery(false, 0);
            rlMainrxMw70BatteryExtender.setSignal(0);
            rlMainrxMw70BatteryExtender.setNetworkText(0);
        }
    }

    // TOGO 2019/8/19 0019 

    /**
     * 获取system info失败时候的操作
     */
    private void getSystemInfoError() {
        getWan();
        ShowMW120Ui(false);
    }

    // TOGO 2019/8/19 0019 

    /**
     * 显示MW120的相关面板UI
     */
    private void ShowMW120Ui(boolean isMW120) {
        tvNetworkType.setVisibility(isMW120 ? View.GONE : View.VISIBLE);// 原network隐藏
        rlMainrxMw70BottomPanel.setVisibility(isMW120 ? View.VISIBLE : View.GONE);// 底部显示
        rlMainrxMw70BatteryExtender.setVisibility(isMW120 ? View.VISIBLE : View.GONE);// extender显示
        if (isMW120) {
            rlMainrxMw70BottomPanel.setDevicesNum(deviceSize);
            rlMainrxMw70BottomPanel.setOnClickConnectedListener(o -> to(ActivityDeviceManager.class, false));
            rlMainrxMw70BottomPanel.setOnClickFreeSharingListener(o -> to(SharingFileActivity.class, false));
        }
    }

    // TOGO 2019/8/19 0019 
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            stopTimer();
            dialogDismiss();
        } else {
            SP.getInstance(getActivity()).putInt(Cons.TAB_FRA, Cons.MAIN);
            startTimer();
            resetUi();
        }
    }

    // TOGO 2019/8/19 0019 
    private void resetUi() {
        if (activity == null) {
            activity = (HomeRxActivity) getActivity();
        }
        activity.tabFlag = Cons.TAB_MAIN;
        activity.llNavigation.setVisibility(View.VISIBLE);
        activity.rlBanner.setVisibility(View.GONE);
    }

    // TOGO 2019/8/19 0019 
    private void initRes() {
        mb_unit = getString(R.string.mb_text);
        gb_unit = getString(R.string.gb_text);
        noUsagePlan = getString(R.string.ergo_20181010_no_set_plan);
        useOf = getString(R.string.used_of);
        signal_2G = getString(R.string.home_network_type_2g);
        signal_3G = getString(R.string.home_network_type_3g);
        signal_3G_plus = getString(R.string.home_network_type_3g_plus);
        signal_4G = getString(R.string.home_network_type_4g);
        signal_text = getString(R.string.signal);
        connected_text = getString(R.string.access_lable);
        unlockSim_title = getString(R.string.sim_unlocked);
        unlockSim_content = getString(R.string.home_pin_locked_notice);
        cancel_text = getString(R.string.cancel);
        confirm_text = getString(R.string.confirm_unit);
        blue_color = getResources().getColor(R.color.mg_blue);
        gray_color = getResources().getColor(R.color.gray);
        signal0 = getResources().getDrawable(R.drawable.home_4g_none);
        signal1 = getResources().getDrawable(R.drawable.home_4g1);
        signal2 = getResources().getDrawable(R.drawable.home_4g2);
        signal3 = getResources().getDrawable(R.drawable.home_4g3);
        signal4 = getResources().getDrawable(R.drawable.home_4g4);
        signal5 = getResources().getDrawable(R.drawable.home_4g5);
        signalR = getResources().getDrawable(R.drawable.home_4g_r);
        connected_none = getResources().getDrawable(R.drawable.device_none);
        connected_more = getResources().getDrawable(R.drawable.device_more);
        signals = new Drawable[]{signal0, signal1, signal2, signal3, signal4, signal5, signalR};
        buttonsView = new View[]{btWanConnect, btSimUnConnected, rlSimConnected, btSimLocked, btSimNown};
    }

    // TOGO 2019/8/19 0019 
    private void initView() {
        // 按钮波浪
        // waveButton = new WaveHelper(btSimConnected);
        // waveButton.setBorderAndShape(0, 0, circlrDotColor);
        // waveButton.setWaveColor(behindColor_nor, frontColor_nor);
        // waveButton.setShiftHorizotolAnim(60, null);
        // waveButton.setAmplitude(50);
        // waveButton.setLevel(30);// 默认比率为30
        // waveButton.startAnim();
        btSimConnected.setFrontColor(Color.parseColor(frontColor_nor));
        btSimConnected.setBehindColor(Color.parseColor(behindColor_nor));
        btSimConnected.setHeightRatio(0.3f);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 启动定时器
     */
    private void startTimer() {
        timer = new TimerHelper(getActivity()) {
            @Override
            public void doSomething() {
                // getWan();  /* 1.先走wan口 */
                getSystemInfoFirstAndRussia();/* 1.先走systemInfo */
            }
        };
        timer.start(2500);
        OtherUtils.timerList.add(timer);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 停止定时器
     */
    private void stopTimer() {
        if (timer != null) {
            timer.stop();
            timer = null;
        }
    }

    /**
     * 获取system info
     */
    private void getSystemInfoFirstAndRussia() {
        /* 1.判断是否为MW120新型设备 */
        xGetSystemInfoHelper.getSystemInfo();
        xsystemStatuHelper_for_russia.getSystemStatus();
    }

    /**
     * 获取extender状态
     */
    private void getExtender() {
        extenderHelper.get();
    }

    // TOGO 2019/8/19 0019 getwanstatus

    /**
     * 先获取WAN口状态
     */
    private void getWan() {
        /* 2.wan口无效后再走sim卡 */
        wanHelper.boardTimer();
    }

    // TOGO 2019/8/19 0019 
    /**
     * 显示wan口模式视图
     */
    private void wanFirst() {
        // 0.隐藏等待
        OtherUtils.hideProgressPop(pgdWait);
        // 1.显示UI
        buttonUi(WAN_CONNECT_MODE);// 显示wan口按钮
        tvNetworkType.setVisibility(isMW120 ? View.GONE : View.VISIBLE);
        tvNetworkType.setText(getString(R.string.Ethernet));// 显示网络类型文本
        rlSignalPanel.setVisibility(View.GONE);// 信号面板消隐
        // 2.获取连接设备数
        getDevice();
    }

    // TOGO 2019/8/19 0019 
    /**
     * 获取sim卡状态
     */
    private void getSim() {
        simHelper.boardTimer();
    }

    // TOGO 2019/8/19 0019 simnotready
    /**
     * SIM卡没有插入时显示
     */
    private void simNown() {
        // 0.隐藏等待
        OtherUtils.hideProgressPop(pgdWait);
        // 1.显示UI
        buttonUi(SIM_NOWN);// SIM卡未插入
        // 2.获取注册状态(在SIM卡状态未达到CONNECTED之前切勿使用GetNetworkInfo这个接口)
        getNetworkRegister();
        // 3.获取设备数
        getDevice();
    }

    // TOGO 2019/8/19 0019 
    /**
     * sim卡被锁定
     */
    private void pinPukSimLock() {
        // 0.隐藏等待
        OtherUtils.hideProgressPop(pgdWait);
        // 1.显示UI
        buttonUi(SIM_LOCKED);// SIM卡锁定状态
        // 2.获取注册状态(在SIM卡状态未达到CONNECTED之前切勿使用GetNetworkInfo这个接口)
        getNetworkRegister();
        // 3.获取设备数
        getDevice();
    }

    // TOGO 2019/8/19 0019 

    /**
     * SIM卡已准备
     */
    private void simReady() {
        // 0.隐藏等待
        OtherUtils.hideProgressPop(pgdWait);
        xGetConnectionStateHelper.getConnectionState();
        // 1.获取注册状态
        getNetworkRegister();
        // 2.获取设备状态
        getDevice();
        // 3. 获取流量使用情况
        getUsage();
    }

    // TOGO 2019/8/19 0019 

    /**
     * SIM卡未连接(但已经解锁)
     */
    private void simNotReady() {
        // 0.隐藏等待
        OtherUtils.hideProgressPop(pgdWait);
        // 1.显示UI
        buttonUi(SIM_DISCONNECT_MODE);// SIM卡未连接状态
        // 2.获取注册状态(在SIM卡状态未达到CONNECTED之前切勿使用GetNetworkInfo这个接口)
        getNetworkRegister();
        // 3.获取设备数
        getDevice();
    }

    // TOGO 2019/8/19 0019 
    int temp = 0;

    // TOGO 2019/8/19 0019 

    /**
     * 获取流量
     */
    private void getUsage() {
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(activity);
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(result -> {
            if (temp == 0) {
                temp++;
            }

            // 设置已使用流量
            UsageHelper.Usage usedUsage = UsageHelper.getUsageByte(getActivity(), result.getUsedData());
            boolean isRussian = OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN);
            tvUsedUnit.setVisibility(isRussian ? View.GONE : View.VISIBLE);
            if (isRussian) {
                tvUsedData.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            }
            tvUsedData.setVisibility(View.VISIBLE);
            tvUsedUnit.setText(usedUsage.unit);
            String usage = usedUsage.usage;
            if (isRussian) {
                // test russia show problem
                usage = usage.replace(".", ",") + " " + usedUsage.unit;
            }
            tvUsedData.setText(usage);
            // 设置月计划流量
            UsageHelper.Usage planUsage = UsageHelper.getUsageByte(getActivity(), result.getMonthlyPlan());
            String planUnit = result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB ? mb_unit : gb_unit;
            long plan = result.getMonthlyPlan();
            // used of 0.88GB
            String montyUsage = planUsage.usage;
            if (isRussian) {
                montyUsage = montyUsage.replace(".", ",") + " ";
            }
            String montyUnit = planUnit;
            tvUsedTotal.setText(plan <= 0 ? noUsagePlan : useOf + BLANK_TEXT + montyUsage + montyUnit);
            // 计算已使用流量比率
            usageLimit = SP.getInstance(getActivity()).getInt(Cons.USAGE_LIMIT, 90);
            if (usageLimit == -1) {
                btSimConnected.setFrontColor(Color.parseColor(frontColor_nor));
                btSimConnected.setBehindColor(Color.parseColor(behindColor_nor));
                btSimConnected.setHeightRatio(0.3f);
            } else {
                int rate = UsageHelper.getRateUsed(result.getUsedData(), result.getMonthlyPlan());
                if (rate > usageLimit) {
                    btSimConnected.setFrontColor(Color.parseColor(frontColor_over));
                    btSimConnected.setBehindColor(Color.parseColor(behindColor_over));
                } else {
                    btSimConnected.setFrontColor(Color.parseColor(frontColor_nor));
                    btSimConnected.setBehindColor(Color.parseColor(behindColor_nor));
                }
                float v = rate * 1f / 100f;
                btSimConnected.setHeightRatio(rate < 0.1f ? 0.1f : v);
            }
        });
        getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(this::usageError);
        getUsageSettingsHelper.getUsageSetting();
    }

    // TOGO 2019/8/19 0019 
    private void usageError() {
        if (tvUsedUnit != null) {
            tvUsedUnit.setText(mb_unit);
        }
        if (tvUsedData != null) {
            tvUsedData.setText("-");
        }
        if (tvUsedTotal != null) {
            tvUsedTotal.setText(getString(R.string.used_of) + " -");
        }
    }

    // TOGO 2019/8/19 0019 

    /**
     * 获取注册状态
     */
    private void getNetworkRegister() {
        if (networkHelper == null) {
            networkHelper = new NetworkInfoHelper(getActivity()) {
                @Override
                public void noRegister() {
                    if (rlSignalPanel != null) {
                        rlSignalPanel.setVisibility(View.VISIBLE);
                    }
                    if (ivSignal != null) {
                        ivSignal.setImageDrawable(signal0);
                    }
                    if (tvNetworkType != null) {
                        tvNetworkType.setVisibility(View.GONE);
                    }
                    if (tvSignal != null) {
                        tvSignal.setText(NONE_TEXT);
                        tvSignal.setTextColor(gray_color);
                    }
                }

                @Override
                public void register(GetNetworkInfoBean result) {
                    rlSignalPanel.setVisibility(View.VISIBLE);
                    // 设置漫游+信号强度
                    boolean isRoam = result.getRoaming() == Cons.ROAMING;
                    int signalStrength = result.getSignalStrength();
                    isNoService = signalStrength == Cons.NOSERVICE;
                    ivSignal.setImageDrawable(isRoam ? signalR : isNoService ? signal0 : signals[signalStrength]);
                    // 设置网络类型
                    tvNetworkType.setVisibility(isMW120 ? View.GONE : View.VISIBLE);
                    tvNetworkType.setText(result.getNetworkName());// CMCC\UNICOM\..
                    tvMainrxMw120NetworkType.setText(isRoam ? getString(R.string.usage_setting_roaming) : result.getNetworkName());
                    // CMCC\UNICOM\..
                    String signalType = getSignalType(getActivity(), result.getNetworkType());
                    tvSignal.setText(signalType);// 2G\3G..
                    tvSignal.setTextColor(blue_color);
                }
            };
        }
        networkHelper.get();
    }

    // TOGO 2019/8/19 0019 

    /**
     * 获取连接设备数
     */
    private void getDevice() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(bean -> {
            deviceSize = bean.getConnectedList().size();
            ivConnectedPeople.setImageDrawable(deviceSize > 0 ? connected_more : connected_none);
            String hadConnect = deviceSize + BLANK_TEXT + connected_text;
            if (OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN)) {
                hadConnect = connected_text + ":" + BLANK_TEXT + deviceSize;
            }
            tvConnectedPeople.setText(deviceSize > 0 ? hadConnect : connected_text);
            tvConnectedPeople.setTextColor(deviceSize > 0 ? blue_color : gray_color);
        });
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(this::noneConnected);
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    // TOGO 2019/8/19 0019 

    /**
     * 状态错误 | 状态不良 使用此UI
     */
    private void noneConnected() {
        if (ivConnectedPeople != null) {
            ivConnectedPeople.setImageDrawable(connected_none);
        }
        if (tvConnectedPeople != null) {
            tvConnectedPeople.setText(NONE_TEXT);
            tvConnectedPeople.setTextColor(gray_color);
        }
    }

    // TOGO 2019/8/19 0019 
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        stopTimer();
        dialogDismiss();
    }

    // TOGO 2019/8/19 0019 
    @OnClick({R.id.bt_mainrx_simLocked,// sim卡锁定
            R.id.bt_mainrx_wanConnect,// wan口连接
            R.id.bt_mainrx_simUnConnected,// sim未连接
            R.id.bt_mainrx_simConnected,// sim卡连接
            R.id.iv_mainrx_signal,// 信号图标
            R.id.tv_mainrx_signal,// 信号文本
            R.id.iv_mainrx_connectedPeople,// 连接数图标
            R.id.bt_mainrx_extender,// extender
            R.id.tv_mainrx_connectedPeople})// 连接数文本
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_mainrx_wanConnect:// 点击wan口
                clickWhenWanConnect();
                break;
            case R.id.bt_mainrx_simUnConnected:// sim卡(未连接, 但已经解锁)点击
                clickWhenSimNotConnect();
                break;
            case R.id.bt_mainrx_simNown:// sim未插入
                clickWhenSimNotConnect();
                break;
            case R.id.bt_mainrx_simConnected:// sim 卡(连接)点击
                clickWhenSimConnect();
                break;
            case R.id.bt_mainrx_simLocked:// sim卡锁定
                clickWhenSimLocked();
                break;
            case R.id.iv_mainrx_connectedPeople:// 连接数
            case R.id.tv_mainrx_connectedPeople:
                to(ActivityDeviceManager.class, false);
                break;
            case R.id.bt_mainrx_extender:
                EventBus.getDefault().postSticky(mainRxFragment.class);
                clickWifiExtender();
                break;

        }
    }

    // TOGO 2019/8/19 0019 
    /**
     * extender状态下的点击--> 跳转到extender
     */
    private void clickWifiExtender() {
        activity.fraHelpers.transfer(fragmentClazz[Cons.TAB_WIFI_EXTENDER]);
    }

    // TOGO 2019/8/19 0019 
    /**
     * SIM卡被锁定时点击了该按钮
     */
    private void clickWhenSimLocked() {
        // 检测SIM卡状态
        simHelper_simLocked = new BoardSimHelper(getActivity());
        simHelper_simLocked.setOnPinRequireListener(result -> showPinDialog());// PIN
        simHelper_simLocked.setOnpukRequireListener(result -> showPukDialog());// PUK
        simHelper_simLocked.setOnpukTimeoutListener(result -> showPukTimeoutTip());// PUK
        simHelper_simLocked.setOnSimReadyListener(result -> ConnectSettingHelper.toConnect(null));// to connect
        simHelper_simLocked.boardNormal();
    }

    // TOGO 2019/8/19 0019 
    /**
     * WAN口连接后点击了该按钮
     */
    private void clickWhenWanConnect() {
        /* 2.wan口无效后再走sim卡 */
        wanHelperClick.setOnResultError(error -> toast(R.string.restart_device_tip));// 出错
        wanHelperClick.setOnError(e -> toast(R.string.restart_device_tip));// 出错
        wanHelperClick.setOnConnetedNextListener(wanResult -> to(InternetStatusActivity.class, false));// 跳转到IP
        wanHelperClick.setOnDisConnetedNextListener(wanResult -> toast(R.string.check_your_wan_cabling));// 提示连接
        wanHelperClick.setOnDisconnetingNextListener(wanResult -> toast(R.string.check_your_wan_cabling));// 提示连接
        wanHelperClick.boardNormal();
    }

    // TOGO 2019/8/19 0019 
    /**
     * SIM卡连接后点击了该按钮
     */
    private void clickWhenSimConnect() {
        // 检测SIM卡状态
        simHelper_simConnect = new BoardSimHelper(getActivity());
        simHelper_simConnect.setOnPinRequireListener(result -> showPinDialog());// PIN
        simHelper_simConnect.setOnpukRequireListener(result -> showPukDialog());// PUK
        simHelper_simConnect.setOnpukTimeoutListener(result -> showPukTimeoutTip());// PUK
        simHelper_simConnect.setOnSimReadyListener(result -> {
            Lgg.t("test_usage").ii("to usage");
            activity.fraHelpers.transfer(fragmentClazz[6]);
        });// to usage
        simHelper_simConnect.boardNormal();
    }

    // TOGO 2019/8/19 0019 
    /**
     * SIM卡未连接时,点击了该按钮的操作
     */
    private void clickWhenSimNotConnect() {
        // 检测SIM卡状态
        simHelper_simNotConnect = new BoardSimHelper(getActivity());
        simHelper_simNotConnect.setOnPinRequireListener(result -> showPinDialog());// PIN
        simHelper_simNotConnect.setOnpukRequireListener(result -> showPukDialog());// PUK
        simHelper_simNotConnect.setOnpukTimeoutListener(result -> showPukTimeoutTip());// PUK
        simHelper_simNotConnect.setOnSimReadyListener(result -> {
            // 检测是否超过流量
            UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(activity);
            getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(attr -> {
                long monthlyPlan = attr.getMonthlyPlan();
                long usedData = attr.getUsedData();
                if (usedData >= monthlyPlan && monthlyPlan != 0) {// 超出--> 提示
                    toast(R.string.home_usage_over_redial_message);
                } else {// 未超出--> 连接
                    ConnectSettingHelper.toConnect(null);
                }
            });
            getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(() -> toast(R.string.connect_failed));
            getUsageSettingsHelper.getUsageSetting();

        });// to connect
        simHelper_simNotConnect.boardNormal();
    }

    // TOGO 2019/8/19 0019 
    /**
     * 提示PUK码次数超出限制
     */
    private void showPukTimeoutTip() {
        toastLong(R.string.puk_alarm_des1);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 显示PUK码对话框
     */
    private void showPukDialog() {
        wdOK.setVisibility(View.VISIBLE);
        wdOK.setTitle(unlockSim_title);
        wdOK.setDes(unlockSim_content);
        wdOK.setOnBgClickListener(() -> Lgg.t("mainrx").ii("click not area"));
        wdOK.setOnCancelClickListener(() -> wdOK.setVisibility(View.GONE));
        wdOK.setOnOkClickListener(this::toPukFragment);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 前往PUK界面
     */
    private void toPukFragment() {
        wdOK.setVisibility(View.GONE);
        // 提交当前fragment标记以作为"上一次"跳转标记
        SP.getInstance(getActivity()).putInt(Cons.TAB_FRA, Cons.TAB_MAIN);
        changeFragment(fragmentClazz[5], Cons.TAB_PUK);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 显示PIN码对话框
     */
    private void showPinDialog() {
        wdOK.setVisibility(View.VISIBLE);
        wdOK.setTitle(unlockSim_title);
        wdOK.setDes(unlockSim_content);
        wdOK.setOnBgClickListener(() -> Lgg.t("mainrx").ii("click not area"));
        wdOK.setOnCancelClickListener(() -> wdOK.setVisibility(View.GONE));
        wdOK.setOnOkClickListener(this::toPinFragment);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 前往PIN界面
     */
    private void toPinFragment() {
        wdOK.setVisibility(View.GONE);
        SP.getInstance(getActivity()).putInt(Cons.TAB_FRA, Cons.TAB_MAIN);
        changeFragment(fragmentClazz[Cons.TAB_PIN], Cons.TAB_PIN);
    }

    // TOGO 2019/8/19 0019 

    /**
     * 切换按钮视图
     *
     * @param modeFlag
     */
    public void buttonUi(int modeFlag) {
        for (int i = 0; i < buttonsView.length; i++) {
            buttonsView[i].setVisibility(i == modeFlag ? View.VISIBLE : View.GONE);
        }
    }

    // TOGO 2019/8/19 0019 
    /**
     * 切换fragment
     *
     * @param clazz
     */
    public void changeFragment(Class clazz, int flag) {
        activity.transferUi(flag);
    }

    // TOGO 2019/8/19 0019 
    /**
     * 对话框隐藏
     */
    private void dialogDismiss() {
        if (wdOK != null) {
            wdOK.setVisibility(View.GONE);
        }
    }

    // TOGO 2019/8/19 0019 
    public void toast(int resId) {
        ToastUtil_m.show(getActivity(), resId);
    }

    // TOGO 2019/8/19 0019 
    public void toastLong(int resId) {
        ToastUtil_m.showLong(getActivity(), resId);
    }

    // TOGO 2019/8/19 0019 
    public void toast(String content) {
        ToastUtil_m.show(getActivity(), content);
    }

    // TOGO 2019/8/19 0019 
    public void to(Class ac, boolean isFinish) {
        CA.toActivity(getActivity(), ac, false, isFinish, false, 0);
    }

    // TOGO 2019/8/19 0019 
    @Override
    public boolean onBackPressed() {
        if (wdOK.getVisibility() == View.VISIBLE) {
            wdOK.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}

