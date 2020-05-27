package com.alcatel.wifilink.ue.frag;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.bean.Extender_GetWIFICurrentStatuBean;
import com.alcatel.wifilink.helper.ClickDoubleHelper;
import com.alcatel.wifilink.helper.DeviceHelper;
import com.alcatel.wifilink.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.helper.NetworkInfoHelper;
import com.alcatel.wifilink.helper.UsageHelper;
import com.alcatel.wifilink.helper.UsageSettingHelper;
import com.alcatel.wifilink.ue.activity.SplashActivity;
import com.alcatel.wifilink.utils.RootCons;
import com.alcatel.wifilink.utils.RootUtils;
import com.alcatel.wifilink.widget.HH70_LoadWidget;
import com.alcatel.wifilink.widget.HH70_MwBatteryWidget;
import com.alcatel.wifilink.widget.HH70_MwBottomWidget;
import com.alcatel.wifilink.widget.HH70_NormalWidget;
import com.de.wave.core.WaveView;
import com.hiber.cons.TimerState;
import com.hiber.impl.RootEventListener;
import com.hiber.tools.ShareUtils;
import com.p_freesharing.p_freesharing.bean.InteractiveRequestBean;
import com.p_freesharing.p_freesharing.bean.InteractiveResponceBean;
import com.p_freesharing.p_freesharing.ui.SharingFileActivity;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.ConnectHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.utils.Logg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class mainFrag extends BaseFrag {

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

    @BindView(R.id.rl_mainrx_signal_panel)
    RelativeLayout rlSignalPanel;// 信号面板
    @BindView(R.id.iv_mainrx_signal)
    ImageView ivSignal;// 信号图标
    @BindView(R.id.tv_mainrx_signal)
    TextView tvSignal;// 信号类型文本

    @BindView(R.id.rl_mainrx_connectedPeople_panel)
    RelativeLayout rlConnectedPeoplePanel;// 连接面板
    @BindView(R.id.iv_mainrx_connectedPeople)
    ImageView ivConnectedPeople;// 连接人数图标
    @BindView(R.id.tv_mainrx_connectedPeople)
    TextView tvConnectedPeople;// 连接人数文本

    @BindView(R.id.rl_mainrx_mw70_panel)
    HH70_MwBottomWidget rlMainrxMw70BottomPanel;// MW70新型设备需要显示的面板

    @BindView(R.id.rl_mainrx_mw70_battery_extender)
    HH70_MwBatteryWidget rlMainrxMw70BatteryExtender;// MW70新型设备需要显示的电池面板

    @BindView(R.id.dg_mainrx_widget_ok)
    HH70_NormalWidget wdOK;// 确定取消面板

    @BindView(R.id.wd_main_load)
    HH70_LoadWidget wdLoad;// 等待

    private String BLANK_TEXT = " ";
    private String NONE_TEXT = "- -";
    private String useOf;
    private int WAN_CONNECT_MODE = 0;// wan口连接模式
    private int SIM_DISCONNECT_MODE = 1;// SIM卡未连接模式(已解锁)
    private int SIM_CONNECT_MODE = 2;// SIM卡连接模式
    private int SIM_LOCKED = 3;// SIM被锁定状态
    private String behindColor_nor = "#AA00cd3a";// 圆环波浪底色(正常)
    private String frontColor_nor = "#AA00cd3a";// 圆环波浪前景色(正常)
    private String behindColor_over = "#AAf5ac1f";// 圆环波浪底色(超标)
    private String frontColor_over = "#f5ac1f";// 圆环波浪前景色(超标)
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
    private Drawable connected_none;// 没有连接数
    private Drawable connected_more;// 有连接数
    private String connected_text;// 连接文本
    private View[] buttonsView;
    private int blue_color;
    private int gray_color;

    private String unlockSim_title;
    private String unlockSim_content;
    private int usageLimit;// 默认的流量限度(用于显示波浪颜色)
    private int deviceSize;// 当前连接设备数
    private boolean isMWDev;// 是否为MW设备
    private boolean isNoService = true;// 是否当前不在服务区(默认不在)
    private int temp = 0;
    private ClickDoubleHelper clickDouble;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_main;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        timerState = TimerState.ON;
        wdLoad.setVisibles();
        wdOK.setVisibility(View.GONE);
        initRes();
        initView();
        initClick();
        initEvent();
    }

    @Override
    public void setTimerTask() {
        getSystemInfoFirstAndRussia();
    }

    private void initClick() {
        // 点击WAN图标
        btWanConnect.setOnClickListener(v -> clickWhenWanConnect());
        // 点击SIM(未连接, 但已解锁 或者 SIM未插入)
        btSimUnConnected.setOnClickListener(v -> clickWhenSimNotConnect());
        btSimNown.setOnClickListener(v -> clickWhenSimNotConnect());
        // 点击SIM(已连接)
        btSimConnected.setOnClickListener(v -> clickWhenSimConnect());
        // 点击SIM(SIM锁定)
        btSimLocked.setOnClickListener(v -> clickWhenSimLocked());
        // 点击连接数
        ivConnectedPeople.setOnClickListener(v -> toFrag(getClass(), DeviceConnectFrag.class, null, true));
        tvConnectedPeople.setOnClickListener(v -> toFrag(getClass(), DeviceConnectFrag.class, null, true));
        // 点击wifi-extender
        btMainrxExtender.setOnClickListener(v -> {
            lastFrag = mainFrag.class;
            toFrag(getClass(), WifiExtenderRxFrag.class, null, true);
        });
    }

    /**
     * SIM卡被锁定时点击了该按钮
     */
    private void clickWhenSimLocked() {
        // 检测SIM卡状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                int simState = getSimStatusBean.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_PIN_REQUIRED:
                        showPinPanel();
                        break;
                    case GetSimStatusBean.CONS_PUK_REQUIRED:
                        showPukPanel();
                        break;
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        toast(R.string.hh70_you_have_incorrect, 5000);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_READY:
                        toConnect();
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
            xGetSimStatusHelper.getSimStatus();
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 启动连接
     */
    private void toConnect() {
        GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetConnectionStateHelper.setOnDisconnectedListener(() -> {
            ConnectHelper xConnectHelper = new ConnectHelper();
            xConnectHelper.setOnConnectFailedListener(() -> toast(R.string.hh70_month_data_limit, 5000));
            xConnectHelper.setOnConnectSuccessListener(() -> Log.i("ma_mode", "Disconnected: "));
            xConnectHelper.connect();
        });
        xGetConnectionStateHelper.setOnDisConnectingListener(() -> {
            ConnectHelper xConnectHelper = new ConnectHelper();
            xConnectHelper.setOnConnectFailedListener(() -> toast(R.string.hh70_month_data_limit, 5000));
            xConnectHelper.connect();
        });
        xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> toast(R.string.hh70_month_data_limit, 5000));
        xGetConnectionStateHelper.getConnectionState();
    }

    /**
     * WAN口连接后点击了该按钮
     */
    private void clickWhenWanConnect() {
        /* 2.wan口无效后再走sim卡 */
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
            xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
            xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
                int status = wanSettingsBean.getStatus();
                switch (status) {
                    case GetWanSettingsBean.CONS_CONNECTED:
                        toFrag(getClass(), InternetStatusFrag.class, null, true);
                        break;
                    case GetWanSettingsBean.CONS_DISCONNECTED:
                    case GetWanSettingsBean.CONS_DISCONNECTING:
                        toast(R.string.hh70_restart_device_tip, 5000);
                        break;
                }
            });
            xGetWanSettingsHelper.getWanSettings();
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * SIM卡连接后点击了该按钮
     */
    private void clickWhenSimConnect() {
        // 检测SIM卡状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                int simState = getSimStatusBean.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_PIN_REQUIRED:
                        showPinPanel();
                        break;
                    case GetSimStatusBean.CONS_PUK_REQUIRED:
                        showPukPanel();
                        break;
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        toast(R.string.hh70_you_have_incorrect, 5000);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_READY:
                        toFrag(getClass(), UsageRxFrag.class, null, true);
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
            xGetSimStatusHelper.getSimStatus();
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * SIM卡未连接时,点击了该按钮的操作
     */
    private void clickWhenSimNotConnect() {
        // 检测SIM卡状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                int simState = getSimStatusBean.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_PIN_REQUIRED:
                        showPinPanel();
                        break;
                    case GetSimStatusBean.CONS_PUK_REQUIRED:
                        showPukPanel();
                        break;
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        toast(R.string.hh70_you_have_incorrect, 5000);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_READY:
                        checkUsageOver();
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
            xGetSimStatusHelper.getSimStatus();
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.hh70_restart_device_tip, 5000));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 检查流量是否超标
     */
    private void checkUsageOver() {
        // 检测是否超过流量
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper();
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(attr -> {
            long monthlyPlan = attr.getMonthlyPlan();
            long usedData = attr.getUsedData();
            if (usedData >= monthlyPlan && monthlyPlan != 0 && attr.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE) {// 超出--> 提示
                toast(R.string.hh70_monthly_data_plan_used, 5000);
            } else {// 未超出--> 连接
                toConnect();
            }
        });
        getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(() -> toast(R.string.hh70_cant_connect, 5000));
        getUsageSettingsHelper.getUsageSetting();
    }

    /**
     * 显示PIN码对话框
     */
    private void showPinPanel() {
        wdOK.setVisibility(View.VISIBLE);
        wdOK.setTitle(unlockSim_title);
        wdOK.setDes(unlockSim_content);
        wdOK.setOnCancelClickListener(() -> wdOK.setVisibility(View.GONE));
        wdOK.setOnOkClickListener(() -> toFrag(getClass(), PinRxFrag.class, null, true));
    }

    /**
     * 显示PUK码对话框
     */
    private void showPukPanel() {
        wdOK.setVisibility(View.VISIBLE);
        wdOK.setTitle(unlockSim_title);
        wdOK.setDes(unlockSim_content);
        wdOK.setOnCancelClickListener(() -> wdOK.setVisibility(View.GONE));
        wdOK.setOnOkClickListener(() -> toFrag(getClass(), PukRxFrag.class, null, true));
    }

    /**
     * 应俄罗斯要求, 先判断当前currentconnection状态, 取消wan口优先原则
     */
    private void getSystemInfoFirstAndRussia() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(this::choiceDev);
        xGetSystemInfoHelper.setOnAppErrorListener(this::getSystemInfoError);
        xGetSystemInfoHelper.setOnFwErrorListener(this::getSystemInfoError);
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 选择设备
     */
    private void choiceDev(GetSystemInfoBean getSystemInfoBean) {
        // 1.是否为新型MW120设备
        isMWDev = RootUtils.isMWDEV(getSystemInfoBean.getDeviceName());
        // 2.显示MW120 ui与否
        showMWUi(isMWDev);
        // 3.根据情况走接口
        if (isMWDev) {// 3.1.如果是MW120设备--> 则获取sim卡\wifi-Extender\SystemStatus
            getSim();
            getExtender();// 拿wifi extender信息
            getSystemStatus();// 拿电池状态

        } else {// 3.1.如果非mw120设备
            getWANStatus();
        }
    }

    /**
     * 获取system status来查看电池信息
     */
    private void getSystemStatus() {
        GetSystemStatusHelper xGetSystemStatusHelper = new GetSystemStatusHelper();
        xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(this::showBatteryAndSignalUi);
        xGetSystemStatusHelper.setOnGetSystemStatusFailedListener(() -> showBatteryAndSignalUi(null));
        xGetSystemStatusHelper.getSystemStatus();
    }

    /**
     * 显示电池电量以及进度
     */
    private void showBatteryAndSignalUi(GetSystemStatusBean systemSystemStates) {
        if (systemSystemStates != null) {
            boolean isCharing = systemSystemStates.getChg_state() == GetSystemStatusBean.CONS_CHARGE_CHARGING;// 电池状态
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

    /**
     * 获取wifi-extender状态
     */
    private void getExtender() {
        Extender_GetWIFIExtenderSettingsHelper extenderHelper = new Extender_GetWIFIExtenderSettingsHelper();
        extenderHelper.setOnGetExtenderFailedListener(() -> setMWNetworkAndBatUi(false));
        extenderHelper.setOnstateEnableOffListener(stateEnable -> setMWNetworkAndBatUi(stateEnable == 1));
        extenderHelper.setOnCurrentHotpotListener(this::showExtenderStrength);
        extenderHelper.get();
    }

    /**
     * 显示extender强度以及SSID
     */
    private void showExtenderStrength(Extender_GetWIFICurrentStatuBean result) {
        if (result != null) {
            // 1.切换network、wifi extender的启闭状态、wifi extender下的中心按钮UI
            setMWNetworkAndBatUi(result.getHotspotConnectStatus() == 2);
            // 2.设置wifi强度
            rlMainrxMw70BatteryExtender.setWifiStrength(result.getSignal());
            // 3.设置SSID
            String ssid = TextUtils.isEmpty(result.getHotspotSSID()) ? activity.getString(R.string.hh70_unknown) : result.getHotspotSSID();
            ssid = RootUtils.turnUrlCode(ssid);
            rlMainrxMw70BatteryExtender.setWifiName(ssid);// SSID
        }
    }

    /**
     * 显示MW设备的network以及电池状态
     */
    private void setMWNetworkAndBatUi(boolean isExtenderOn) {
        // 1.MW下的左上角network面板
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

    private void getSystemInfoError() {
        getWANStatus();
        showMWUi(false);
    }

    /**
     * 显示MW120的相关面板UI
     */
    private void showMWUi(boolean isMWDev) {
        tvNetworkType.setVisibility(isMWDev ? View.GONE : View.VISIBLE);// 原network隐藏
        rlMainrxMw70BottomPanel.setVisibility(isMWDev ? View.VISIBLE : View.GONE);// 底部显示
        rlMainrxMw70BatteryExtender.setVisibility(isMWDev ? View.VISIBLE : View.GONE);// extender显示
        if (isMWDev) {
            rlMainrxMw70BottomPanel.setDevicesNum(deviceSize);
            rlMainrxMw70BottomPanel.setOnClickConnectedListener(o -> toFrag(getClass(), DeviceConnectFrag.class, null, true));
            rlMainrxMw70BottomPanel.setOnClickFreeSharingListener(o -> {
                Intent intent = new Intent(activity, SharingFileActivity.class);
                startActivity(intent);
            });
        }
    }

    /**
     * 获取WAN口状态
     */
    private void getWANStatus() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
                xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(wanSettingsBean -> {
                    if (wanSettingsBean.getStatus() == GetWanSettingsBean.CONS_CONNECTED) {
                        wanFirst();
                    } else {
                        getSim();
                    }
                });
                xGetWanSettingsHelper.setOnGetWanSettingFailedListener(this::getSim);
                xGetWanSettingsHelper.getWanSettings();
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 获取sim卡状态
     */
    private void getSim() {
        // 先获取登陆状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                // 再获取sim卡状态
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                    int simState = getSimStatusBean.getSIMState();
                    switch (simState) {
                        // 未知 | PUK超限 | SIM正在初始化 | SIM已移除
                        case GetSimStatusBean.CONS_NOWN:
                        case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        case GetSimStatusBean.CONS_SIM_CARD_IS_INITING:
                        case GetSimStatusBean.CONS_SIM_CARD_DETECTED:
                            simNotReady();
                            break;
                        // SIM已就绪
                        case GetSimStatusBean.CONS_SIM_CARD_READY:
                            simReady();
                            break;
                        // SIM被锁 | PIN码 | PUK码
                        case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                        case GetSimStatusBean.CONS_PIN_REQUIRED:
                        case GetSimStatusBean.CONS_PUK_REQUIRED:
                            pinPukSimLock();
                            break;
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(this::simNotReady);
                xGetSimStatusHelper.getSimStatus();
            } else {
                simNotReady();
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * sim卡被锁定
     */
    private void pinPukSimLock() {
        // 0.隐藏等待
        wdLoad.setGone();
        // 1.显示UI
        buttonUi(SIM_LOCKED);// SIM卡锁定状态
        // 2.获取注册状态(在SIM卡状态未达到CONNECTED之前切勿使用GetNetworkInfo这个接口)
        getNetworkRegister();
        // 3.获取设备数
        getDevice();
    }

    /**
     * SIM卡已准备
     */
    private void simReady() {
        // 0.隐藏等待
        wdLoad.setGone();
        getConnectStatus();
        // 1.获取注册状态
        getNetworkRegister();
        // 2.获取设备状态
        getDevice();
        // 3. 获取流量使用情况
        getUsage();
    }

    /**
     * 获取连接状态
     */
    private void getConnectStatus() {
        GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetConnectionStateHelper.setOnGetConnectionStateFailedListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnDisConnectingListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnDisconnectedListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnConnectingListener(() -> buttonUi(SIM_DISCONNECT_MODE));
        xGetConnectionStateHelper.setOnConnectedListener(() -> buttonUi(SIM_CONNECT_MODE));
        xGetConnectionStateHelper.getConnectionState();
    }

    /**
     * 获取流量
     */
    private void getUsage() {
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper();
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(result -> {
            if (temp == 0) {
                temp++;
            }
            // 设置已使用流量
            UsageHelper.Usage usedUsage = UsageHelper.getUsageByte(getActivity(), result.getUsedData());
            String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
            boolean isRussian = currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN);
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
            String planUnit = planUsage.unit;// 单位

            // 不要直接使用FW返回的unit做判断, FW有时候抽筋返回错误的单位
            // String planUnit = result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB ? mb_unit : gb_unit;

            long plan = result.getMonthlyPlan();
            // used of 0.88GB
            String montyUsage = planUsage.usage;
            if (isRussian) {
                montyUsage = montyUsage.replace(".", ",") + " ";
            }
            tvUsedTotal.setText(plan <= 0 ? "" : useOf + BLANK_TEXT + montyUsage + planUnit);
            // 计算已使用流量比率
            usageLimit = ShareUtils.get(RootCons.USAGE_LIMIT_DEFAULT, 90);
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
        getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(this::getUsageSettingError);
        getUsageSettingsHelper.getUsageSetting();
    }

    /**
     * 获取流量错误
     */
    private void getUsageSettingError() {
        if (tvUsedUnit != null) {
            tvUsedUnit.setText(mb_unit);
        }
        if (tvUsedData != null) {
            tvUsedData.setText("-");
        }
        if (tvUsedTotal != null) {
            tvUsedTotal.setText(String.valueOf(activity.getString(R.string.hh70_used_of) + " -"));
        }
    }

    /**
     * SIM卡未连接(但已经解锁)
     */
    private void simNotReady() {
        // 0.隐藏等待
        wdLoad.setGone();
        // 1.显示UI
        buttonUi(SIM_DISCONNECT_MODE);// SIM卡未连接状态
        // 2.获取注册状态(在SIM卡状态未达到CONNECTED之前切勿使用GetNetworkInfo这个接口)
        getNetworkRegister();
        // 3.获取设备数
        getDevice();
    }

    /**
     * 获取注册状态
     */
    private void getNetworkRegister() {
        NetworkInfoHelper networkHelper = new NetworkInfoHelper() {
            @Override
            public void noRegister() {
                Logg.t("HH42_REGISTER").ww("fw had no register");
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
                boolean isRoam = result.getRoaming() == GetNetworkInfoBean.CONS_ROAMING;
                int signalStrength = result.getSignalStrength();
                isNoService = signalStrength == GetNetworkInfoBean.CONS_NO_SERVICE;
                ivSignal.setImageDrawable(isRoam ? signalR : isNoService ? signal0 : signals[signalStrength]);
                // 设置网络类型
                tvNetworkType.setVisibility(isMWDev ? View.GONE : View.VISIBLE);
                tvNetworkType.setText(result.getNetworkName());// CMCC\UNICOM\..
                tvMainrxMw120NetworkType.setText(isRoam ? activity.getString(R.string.hh70_data_roaming) : result.getNetworkName());
                // CMCC\UNICOM\..
                String signalType = getSignalType(activity, result.getNetworkType());
                Logg.t("HH42_REGISTER").ww("signalType: " + signalType);
                tvSignal.setText(signalType);// 2G\3G..
                tvSignal.setTextColor(blue_color);
            }
        };
        networkHelper.get();
    }

    /**
     * 显示wan口模式视图
     */
    private void wanFirst() {
        // 0.隐藏等待
        wdLoad.setGone();
        // 1.显示UI
        buttonUi(WAN_CONNECT_MODE);// 显示wan口按钮
        tvNetworkType.setVisibility(isMWDev ? View.GONE : View.VISIBLE);
        tvNetworkType.setText(activity.getString(R.string.hh70_Ethernet));// 显示网络类型文本
        rlSignalPanel.setVisibility(View.GONE);// 信号面板消隐
        // 2.获取连接设备数
        getDevice();
    }

    /**
     * 切换按钮视图
     */
    public void buttonUi(int btMode) {
        for (int i = 0; i < buttonsView.length; i++) {
            buttonsView[i].setVisibility(i == btMode ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 获取连接设备数
     */
    private void getDevice() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(bean -> {
            deviceSize = bean.getConnectedList().size();
            ivConnectedPeople.setImageDrawable(deviceSize > 0 ? connected_more : connected_none);
            String hadConnect = deviceSize + BLANK_TEXT + connected_text;
            String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
            if (currentLanguage.equalsIgnoreCase(RootCons.LANGUAGES.RUSSIAN)) {
                hadConnect = connected_text + ":" + BLANK_TEXT + deviceSize;
            }
            tvConnectedPeople.setText(deviceSize > 0 ? hadConnect : connected_text);
            tvConnectedPeople.setTextColor(deviceSize > 0 ? blue_color : gray_color);
        });
        xGetConnectedDeviceListHelper.setOnGetDeviceListFailListener(this::noneConnected);
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

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

    private void initRes() {
        mb_unit = activity.getString(R.string.hh70_mb);
        gb_unit = activity.getString(R.string.hh70_gb);
        useOf = activity.getString(R.string.hh70_used_of);
        connected_text = activity.getString(R.string.hh70_connected);
        unlockSim_title = activity.getString(R.string.hh70_sim_unlocked);
        unlockSim_content = activity.getString(R.string.hh70_verify_your_pin);
        blue_color = getRootColor(R.color.mg_blue);
        gray_color = getRootColor(R.color.gray);
        signal0 = getRootDrawable(R.drawable.home_4g_none);
        signal1 = getRootDrawable(R.drawable.home_4g1);
        signal2 = getRootDrawable(R.drawable.home_4g2);
        signal3 = getRootDrawable(R.drawable.home_4g3);
        signal4 = getRootDrawable(R.drawable.home_4g4);
        signal5 = getRootDrawable(R.drawable.home_4g5);
        signalR = getRootDrawable(R.drawable.home_4g_r);
        connected_none = getRootDrawable(R.drawable.device_none);
        connected_more = getRootDrawable(R.drawable.device_more);
        signals = new Drawable[]{signal0, signal1, signal2, signal3, signal4, signal5, signalR};
        buttonsView = new View[]{btWanConnect, btSimUnConnected, rlSimConnected, btSimLocked, btSimNown};
    }

    private void initView() {
        // 按钮波浪
        btSimConnected.setFrontColor(Color.parseColor(frontColor_nor));
        btSimConnected.setBehindColor(Color.parseColor(behindColor_nor));
        btSimConnected.setHeightRatio(0.3f);
    }

    @Override
    public boolean onBackPresss() {
        if (wdOK.getVisibility() == View.VISIBLE) {
            wdOK.setVisibility(View.GONE);
            return true;
        }
        // 登出
        if (clickDouble == null) {
            clickDouble = new ClickDoubleHelper();
            clickDouble.setOnClickOneListener(() -> toast(R.string.hh70_touch_again, 3000));
            clickDouble.setOnClickDoubleListener(this::killAllActivitys);
        }
        clickDouble.click();
        return true;
    }

    private void initEvent() {
        setEventListener(InteractiveRequestBean.class, new RootEventListener<InteractiveRequestBean>() {
            @Override
            public void getData(InteractiveRequestBean requestBean) {
                if (requestBean.getRecevie() == InteractiveRequestBean.REQ) {
                    getDevicesList();
                }
            }
        });
    }

    private void getDevicesList() {
        if (SplashActivity.freeSharingLock) {
            synchronized (mainFrag.class) {
                SplashActivity.freeSharingLock = false;
                DeviceHelper deviceHelper = new DeviceHelper();
                deviceHelper.setOnGetDevicesSuccessListener(connectedList -> {
                    // 转换
                    com.p_freesharing.p_freesharing.bean.ConnectedList aarConnectList = transferConnectList(connectedList);
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(aarConnectList);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_SUCCESS);
                    sendEvent(responceBean, false);
                    SplashActivity.freeSharingLock = true;
                });
                deviceHelper.setOnFwErrorListener(() -> {
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(null);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_FW_ERROR);
                    sendEvent(responceBean, false);
                    SplashActivity.freeSharingLock = true;
                });
                deviceHelper.setOnAppErrorListener(() -> {
                    InteractiveResponceBean responceBean = new InteractiveResponceBean();
                    responceBean.setConnectedList(null);
                    responceBean.setErrorMsg("no error msg");
                    responceBean.setType(InteractiveResponceBean.DEVICEHELPER_APP_ERROR);
                    sendEvent(responceBean, false);
                    SplashActivity.freeSharingLock = true;
                });
                deviceHelper.getDeviecs();
            }
        }
    }

    /**
     * 将本地的connectlist转换成aar接收的bean
     */
    private com.p_freesharing.p_freesharing.bean.ConnectedList transferConnectList(GetConnectDeviceListBean connectedListBean) {
        // 本工程的connectlist
        List<GetConnectDeviceListBean.ConnectedDeviceBean> wifiConnectList = connectedListBean.getConnectedList();
        // 创建aar包里的connectlist
        com.p_freesharing.p_freesharing.bean.ConnectedList aarConnectList = new com.p_freesharing.p_freesharing.bean.ConnectedList();
        List<com.p_freesharing.p_freesharing.bean.ConnectedList.Device> aarDevices = new ArrayList<>();
        int size = wifiConnectList.size();
        for (int i = 0; i < size; i++) {
            com.p_freesharing.p_freesharing.bean.ConnectedList.Device aarDevice = new com.p_freesharing.p_freesharing.bean.ConnectedList().new Device();
            aarDevice.setAssociationTime(wifiConnectList.get(i).getAssociationTime());
            aarDevice.setConnectMode(wifiConnectList.get(i).getConnectMode());
            aarDevice.setDeviceName(wifiConnectList.get(i).getDeviceName());
            aarDevice.setDeviceType(wifiConnectList.get(i).getDeviceType());
            aarDevice.setId(wifiConnectList.get(i).getId());
            aarDevice.setInternetRight(wifiConnectList.get(i).getInternetRight());
            aarDevice.setIPAddress(wifiConnectList.get(i).getIPAddress());
            aarDevice.setMacAddress(wifiConnectList.get(i).getMacAddress());
            aarDevice.setStorageRight(wifiConnectList.get(i).getStorageRight());
            aarDevices.add(aarDevice);
        }
        aarConnectList.setConnectedList(aarDevices);
        return aarConnectList;
    }
}
