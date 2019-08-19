package com.alcatel.wifilink.root.ue.root_frag;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.WifiExtenderAdapter;
import com.alcatel.wifilink.root.bean.Extender_GetHotspotListResult;
import com.alcatel.wifilink.root.bean.Extender_GetWIFIExtenderCurrentStatusResult;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.Extender_ConnectHotspotHelper;
import com.alcatel.wifilink.root.helper.Extender_DisConnectHotspotHelper;
import com.alcatel.wifilink.root.helper.Extender_GetConnectHotspotStateHelper;
import com.alcatel.wifilink.root.helper.Extender_GetHotspotListHelper;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderCurrentStatusHelper;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.Extender_SearchHotspotHelper;
import com.alcatel.wifilink.root.helper.Extender_SetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.ue.root_activity.HomeRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.alcatel.wifilink.root.widget.DisConnHotpotView;
import com.alcatel.wifilink.root.widget.ExtenderWait;
import com.alcatel.wifilink.root.widget.HotPotKeyView;
import com.alcatel.wifilink.root.widget.OpenCloseExtenderView;
import com.p_xhelper_smart.p_xhelper_smart.impl.FwError;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qianli.ma on 2018/2/5 0005.
 */

public class WifiExtenderRxFragment extends Fragment implements FragmentBackHandler {

    Unbinder unbinder;
    @BindView(R.id.iv_wifiExtender_back)
    ImageView ivBack;// 返回键
    @BindView(R.id.tv_wifiExtender_scan)
    TextView tvScan;// 扫描键

    @BindView(R.id.tv_wifiExtender_not_connect_tip)
    TextView tvNotConnectTip;// 未连接掉线提示

    @BindView(R.id.iv_wifiExtender_panel_socket)
    ImageView ivPanelSocket;// wifi extender开关
    @BindView(R.id.tv_wifiExtender_not_connect_panel_des)
    TextView tvNotConnectPanelDes;// 连接描述

    @BindView(R.id.rl_wifiExtender_had_connected)
    RelativeLayout rlHadConnected;// 已连接的布局
    @BindView(R.id.tv_wifiExtender_had_connect_hotDot_name)
    TextView tvHadConnectedHotDotName;// 已连接热点名称
    @BindView(R.id.iv_wifiExtender_had_connected_wifi)
    ImageView ivHadConnectedWifiSignal;// 已连接热点的强度
    @BindView(R.id.iv_wifiExtender_had_connect_lock)
    ImageView ivHadConnectedLock;// 已连接的热点是否带密码

    @BindView(R.id.tv_wifiExtender_available_network)
    TextView tvAvailableNetwork;// available_network

    @BindView(R.id.rcv_wifiExtender_available_network)
    RecyclerView rcvWifiExtenderAvailableNetwork;// 待连接的热点列表

    @BindView(R.id.widget_wifi_extender_wait)
    ExtenderWait widgetWifiExtenderWait;// 等待列表

    @BindView(R.id.widget_wifi_extender_password)
    HotPotKeyView widgetWifiExtenderPassword;// 输入热点密码窗口

    @BindView(R.id.widget_wifi_extender_open_close)
    OpenCloseExtenderView widgetWifiExtenderOpenClose;// 开启或者关闭wifiextender时弹出提示窗口

    @BindView(R.id.widget_wifi_extender_disconnhotpot)
    DisConnHotpotView widgetWifiExtenderDisconnhotpot;// 取消当前连接窗口

    private View inflate;
    private String TAG = "WifiExtenderRxFragment";
    private HomeRxActivity activity;
    private Drawable button_on;
    private Drawable button_off;
    private WifiExtenderAdapter wifiExtenderAdapter;
    private List<Extender_GetHotspotListResult.HotspotListBean> hotspotBeans = new ArrayList<>();
    private Class eventBusClass;// 其他fragment跳转过来的标记符
    private int getConnectHotpotCount = 0;// 获取已连接热点的状态计数器
    private Handler handler;
    private TimerHelper timer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (HomeRxActivity) getActivity();
        inflate = View.inflate(activity, R.layout.fra_wifi_extender_rx, null);
        unbinder = ButterKnife.bind(this, inflate);
        EventBus.getDefault().register(this);
        resetUi();
        initRes();
        initAdapter();
        initData();
        initTimer();
        return inflate;
    }

    private void initTimer() {
        timer = new TimerHelper(getActivity()) {
            @Override
            public void doSomething() {
                getWIFISettings();
            }
        };
        timer.start(5000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getOtherPostFlag(Class eventBusClass) {
        // Logs.t("ma_fragment").ii(eventBusClass.getSimpleName());
        // 当前传送的对象有: [mainRxFragment.java]
        this.eventBusClass = eventBusClass;
    }

    private void initAdapter() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcvWifiExtenderAvailableNetwork.setLayoutManager(llm);
        wifiExtenderAdapter = new WifiExtenderAdapter(getActivity(), null);
        wifiExtenderAdapter.setOnClickHotPotListener(this::showConnPasswordWindow);
        rcvWifiExtenderAvailableNetwork.setAdapter(wifiExtenderAdapter);
    }

    /**
     * 连接点击的热点
     *
     * @param hotpot
     */
    private void showConnPasswordWindow(Extender_GetHotspotListResult.HotspotListBean hotpot) {
        if (hotpot.getSecurityMode() == 0) {
            tvScan.setVisibility(View.GONE);
            widgetWifiExtenderWait.setVisibility(View.VISIBLE);
            connhotpot(hotpot, "");
        } else {
            // 弹出密码输入框
            widgetWifiExtenderPassword.setVisibility(View.VISIBLE);
            widgetWifiExtenderPassword.show(hotpot);
            widgetWifiExtenderPassword.setOnClickOkListener((hb, password) -> {
                tvScan.setVisibility(View.GONE);
                widgetWifiExtenderWait.setVisibility(View.VISIBLE);
                connhotpot(hotpot, password);
                widgetWifiExtenderPassword.clear();
            });
        }

    }

    /**
     * 正式连接
     *
     * @param hotpot
     * @param password
     */
    private void connhotpot(Extender_GetHotspotListResult.HotspotListBean hotpot, String password) {

        // 整理要素
        String hotspotId = hotpot.getHotspotId();
        String ssid = hotpot.getSSID();
        int SecurityMode = hotpot.getSecurityMode();
        int Hidden = 0;

        // 开始连接
        Extender_ConnectHotspotHelper extenderConnectHotspotHelper = new Extender_ConnectHotspotHelper();
        extenderConnectHotspotHelper.setOnSuccessListener(attr -> getConnectHotpotState());
        extenderConnectHotspotHelper.setOnFailedListener(attr -> connectFailed("connhotpot", null));
        extenderConnectHotspotHelper.setOnResultErrorListener(attr -> connectFailed("connhotpot", attr));
        extenderConnectHotspotHelper.connect(hotspotId, ssid, password, SecurityMode, Hidden);
    }

    /**
     * 连接成功后获取本次连接状态
     */
    private void getConnectHotpotState() {

        final int NONE = 0;// none
        final int CONNECTING = 1;// connecting
        final int SUCCESS = 2;// success
        final int PASSWORD_ERROR = 3;// password error
        final int NEED_PASSWORD = 4;// need password
        final int FAIL = 5;// fail
        final int OPEN = 6;// open

        Extender_GetConnectHotspotStateHelper helper = new Extender_GetConnectHotspotStateHelper();
        helper.setOnSuccessListener(result -> {
            // 根据state判断
            int state = result.getState();
            Logs.t(TAG).ii("getconnectHotpotState is : " + state);
            switch (state) {
                case NONE:// 0
                    if (getConnectHotpotCount <= 10) {
                        // 延迟1秒再检测
                        handler.postDelayed(() -> {
                            getConnectHotpotState();
                            getConnectHotpotCount++;
                        }, 1000);
                    } else {
                        getConnectHotpotCount = 0;
                        connectFailed("getConnectHotpotState", null, "Extender_GetConnectHotspotStateHelper state NONE");
                    }

                    break;
                case CONNECTING:// 1
                    handler.postDelayed(this::getConnectHotpotState, 1000);
                    break;
                case SUCCESS:// 2
                    getConnectHotpotCount = 0;
                    toast(R.string.success);
                    getHotpotInfo();// 重新获取
                    break;
                case PASSWORD_ERROR:// 3
                    getConnectHotpotCount = 0;
                    toast(R.string.please_enter_the_correct_password);
                    widgetWifiExtenderWait.setVisibility(View.GONE);
                    tvScan.setVisibility(View.VISIBLE);
                    break;
                case NEED_PASSWORD:// 4
                    getConnectHotpotCount = 0;
                    toast(R.string.please_enter_the_correct_password);
                    widgetWifiExtenderWait.setVisibility(View.GONE);
                    tvScan.setVisibility(View.VISIBLE);
                    break;
                case FAIL:// 5
                    if (getConnectHotpotCount <= 10) {
                        // 延迟1秒再检测
                        handler.postDelayed(() -> {
                            getConnectHotpotState();
                            getConnectHotpotCount++;
                        }, 1000);
                    } else {
                        getConnectHotpotCount = 0;
                        connectFailed("getConnectHotpotState", null, "Extender_GetConnectHotspotStateHelper state FAIL");
                    }
                    break;
                case OPEN:// 6
                    getConnectHotpotCount = 0;
                    toast(R.string.success_but_unencrypt);
                    getHotpotInfo();// 重新获取
                    break;
            }
        });
        helper.setOnFailedListener(attr1 -> connectFailed("getConnectHotpotState", null));
        helper.setOnResultErrorListener(error -> connectFailed("getConnectHotpotState", error));
        helper.get();
    }

    /**
     * 连接失败处理
     */
    private void connectFailed(String methodName, FwError error, String... des) {
        String errors = "occur failed;";
        String dess = "";
        if (error != null) {
            errors = error.getMessage();
        }
        for (String de : des) {
            dess = String.valueOf(dess + de);
        }
        Logs.t("connectFailed").ee(methodName + " : " + errors + "des: " + dess);
        widgetWifiExtenderWait.setVisibility(View.GONE);
        tvScan.setVisibility(View.VISIBLE);
        toast(R.string.the_connection_failed_please_try_again_extender);
        getHotpotInfo();// 重新获取
    }


    private void initRes() {
        handler = new Handler();
        button_on = getResources().getDrawable(R.drawable.general_btn_on);
        button_off = getResources().getDrawable(R.drawable.general_btn_off);
    }

    private void initData() {
        getHotpotInfo();
    }

    /**
     * 获取热点的所有信息(当前连接的WIFI + 所有的热点列表)
     */
    private void getHotpotInfo() {

        Logs.t(TAG).ii("1.initData()");
        
        /* 1.获取wifi extender setting */
        widgetWifiExtenderWait.setVisibility(View.VISIBLE);
        tvScan.setVisibility(View.GONE);
        // hotspotBeans.clear();
        getWIFISettings();
    }

    /**
     * 获取WIFI setting配置
     */
    private void getWIFISettings() {

        final int DISABLE = 0;
        final int ENABLE = 1;

        final int INITING = 0;// Initing
        final int COMPLETE = 1;// Complete
        final int INITED_FAILED = 2;// Inited Failed 

        Extender_GetWIFIExtenderSettingsHelper getSettingsHelper = new Extender_GetWIFIExtenderSettingsHelper();
        getSettingsHelper.setOnSuccessListener(extenderSetting -> {

            // 1.获取wifi extender的StateEnable状态
            if (extenderSetting.getStationEnable() == DISABLE) {
                // 1.1.设置描述
                tvNotConnectPanelDes.setText(getString(R.string.connect_to_other_wifi_networks));
                disconnUi();
                Logs.t(TAG).ii("2.GetWIFIExtenderSettings state is off");
            } else {
                // 2.获取wifi extender的初始化状态
                int initingStatus = extenderSetting.getExtenderInitingStatus();
                Logs.t(TAG).ii("2.GetWIFIExtenderSettings initingStatus is :" + initingStatus);
                switch (initingStatus) {
                    case INITING:
                        Logs.t(TAG).ii("2.GetWIFIExtenderSettings initingStatus is :" + "still initing");
                        getWIFISettings();
                        break;
                    case COMPLETE:
                        // 2.1.设置描述
                        tvNotConnectPanelDes.setText(getString(R.string.known_networks_will_be_joined_automatically));
                        Logs.t(TAG).ii("2.GetWIFIExtenderSettings state is on");
                        tvNotConnectTip.setVisibility(View.GONE);
                        ivPanelSocket.setImageDrawable(button_on);
                        getCurrentState();/* 2.获取当前是否有连接的热点 */
                        // getHotpotList();/* 3.获取热点列表 */
                        break;
                    case INITED_FAILED:
                        Logs.t(TAG).ii("2.GetWIFIExtenderSettings initingStatus is :" + "inited failed");
                        disconnUi();
                        break;
                }
            }
        });
        getSettingsHelper.setOnFailedListener(attr -> {
            Logs.t(TAG).ee("method--> Extender_GetWIFIExtenderSettingsHelper failed");
            toast(R.string.connect_failed);
            disconnUi();
        });
        getSettingsHelper.setOnResultErrorListener(resultError -> {
            Logs.t(TAG).ee("method--> Extender_GetWIFIExtenderSettingsHelper: " + resultError.getMessage());
            toast(R.string.get_wifi_extender_settings_failed);
            disconnUi();
        });
        getSettingsHelper.get();
    }

    /**
     * 获取当前是否有正在连接的热点
     */
    private void getCurrentState() {
        Logs.t(TAG).ii("3.getCurrentState()");
        int DISCONNECT = 0;
        int CONNECTTING = 1;
        int CONNECTTED = 2;
        Extender_GetWIFIExtenderCurrentStatusHelper getCurrentStatusHelper = new Extender_GetWIFIExtenderCurrentStatusHelper();
        getCurrentStatusHelper.setOnSuccessListener(result -> {
            if (result.getHotspotConnectStatus() == DISCONNECT) {
                Logs.t(TAG).ii("3.getCurrentState() state: DISCONNECT");
                rlHadConnected.setVisibility(View.GONE);
                getHotpotList(result);/* 3.获取热点列表 */
            } else if (result.getHotspotConnectStatus() == CONNECTTING) {
                Logs.t(TAG).ii("3.getCurrentState() state: CONNECTTING");
                getCurrentState();
            } else {
                Logs.t(TAG).ii("3.getCurrentState() state: CONNECTTED");
                rlHadConnected.setVisibility(View.VISIBLE);// 已连接显示
                Logs.t(TAG).ii("current connect ssid is : " + result.getHotspotSSID());
                tvHadConnectedHotDotName.setText(OtherUtils.turnUrlCode(result.getHotspotSSID()));// SSID(需要进行URL转码)
                ivHadConnectedWifiSignal.setImageDrawable(OtherUtils.transferWifiExtenderSignal(result.getSignal()));// 强度
                getHotpotList(result);/* 3.获取热点列表 */
            }
        });
        getCurrentStatusHelper.setOnFailedListener(attr -> {
            Logs.t(TAG).ee("method--> Extender_GetWIFIExtenderCurrentStatusHelper failed");
            toast(R.string.connect_failed);
            disconnUi();
        });
        getCurrentStatusHelper.setOnResultErrorListener(error -> {
            Logs.t(TAG).ee("method--> Extender_GetWIFIExtenderCurrentStatusHelper: " + error.getMessage());
            toast(R.string.get_wifi_extender_current_status_failed);
            disconnUi();
        });
        getCurrentStatusHelper.get();
    }

    /**
     * 获取热点列表
     */
    private void getHotpotList(Extender_GetWIFIExtenderCurrentStatusResult currentResult) {
        Logs.t(TAG).ii("4.getHotpotList()");
        int NONE = 0;
        int SEARCHING = 1;
        int COMPLETED = 2;
        // 1.触发搜索
        Extender_SearchHotspotHelper extenderSearchHotspotHelper = new Extender_SearchHotspotHelper();
        extenderSearchHotspotHelper.setOnSuccessListener(searchresult -> {
            // 2.获取列表
            Extender_GetHotspotListHelper extenderGetHotspotListHelper = new Extender_GetHotspotListHelper();
            extenderGetHotspotListHelper.setOnSuccessListener(hotpotInfo -> {
                if (rcvWifiExtenderAvailableNetwork != null) {
                    rcvWifiExtenderAvailableNetwork.setVisibility(View.VISIBLE);
                }
                int hotpotStatus = hotpotInfo.getStatus();
                if (hotpotStatus == NONE) {
                    widgetWifiExtenderWait.setVisibility(View.GONE);
                    rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
                    tvScan.setVisibility(View.VISIBLE);
                } else if (hotpotStatus == SEARCHING) {
                    // 延迟3秒再尝试
                    // handler.postDelayed(() -> {
                    //     getHotpotList(currentResult);
                    // }, 3000);
                    getHotpotList(currentResult);

                } else {
                    // 获取成功先清空集合
                    hotspotBeans.clear();
                    // 列表显示
                    rcvWifiExtenderAvailableNetwork.setVisibility(View.VISIBLE);
                    // 获取到热点列表
                    List<Extender_GetHotspotListResult.HotspotListBean> ori_hotpots = hotpotInfo.getHotspotList();
                    // 排除掉当前连接的wifi
                    ori_hotpots = OtherUtils.excludeCurrentHotpot(ori_hotpots, currentResult.getHotspotSSID());
                    Logs.t(TAG).ii("ori_hotpots size: " + ori_hotpots.size());
                    // 测试打印校验获取到的WIFI
                    printSSidTest(ori_hotpots);
                    hotspotBeans = OtherUtils.turnSSISBatch(ori_hotpots);
                    Logs.t(TAG).ii("size: " + hotspotBeans.size());
                    // 刷新适配器
                    wifiExtenderAdapter.notifys(hotspotBeans);
                    // 等待隐藏
                    widgetWifiExtenderWait.setVisibility(View.GONE);
                    // scan按钮显示
                    tvScan.setVisibility(View.VISIBLE);
                    Logs.t(TAG).ii("5.getHotpotList success");
                }

            });
            extenderGetHotspotListHelper.setOnFailedListener(attr -> {
                widgetWifiExtenderWait.setVisibility(View.GONE);
                tvScan.setVisibility(View.VISIBLE);
                rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
                Logs.t(TAG).ee("method--> Extender_GetHotspotListHelper failed");
                toast(R.string.connect_failed);
            });
            extenderGetHotspotListHelper.setOnResultErrorListener(error -> {
                widgetWifiExtenderWait.setVisibility(View.GONE);
                tvScan.setVisibility(View.VISIBLE);
                rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
                Logs.t(TAG).ee("method--> Extender_GetHotspotListHelper: " + error.getMessage());
                toast(R.string.get_current_time_and_timezone_failed);
            });
            extenderGetHotspotListHelper.get();
        });
        extenderSearchHotspotHelper.setOnFailedListener(attr -> {
            widgetWifiExtenderWait.setVisibility(View.GONE);
            tvScan.setVisibility(View.VISIBLE);
            rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
            Logs.t(TAG).ee("method--> getHotpotList failed");
            toast(R.string.connect_failed);
        });
        extenderSearchHotspotHelper.setOnResultErrorListener(error -> {
            widgetWifiExtenderWait.setVisibility(View.GONE);
            tvScan.setVisibility(View.VISIBLE);
            rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
            Logs.t(TAG).ee("method--> getHotpotList: " + error.getMessage());
            toast(R.string.get_current_time_and_timezone_failed);
        });
        extenderSearchHotspotHelper.search();
    }

    /**
     * 测试打印获取到的WIFI
     *
     * @param ori_hotpots
     */
    private void printSSidTest(List<Extender_GetHotspotListResult.HotspotListBean> ori_hotpots) {
        for (Extender_GetHotspotListResult.HotspotListBean hhp : ori_hotpots) {
            Logs.t("ma_ori").ii(hhp.getSSID());
        }
    }

    /**
     * 未连接视图
     */
    private void disconnUi() {
        tvNotConnectTip.setVisibility(View.VISIBLE);// 未连接提示
        ivPanelSocket.setImageDrawable(button_off);// 按钮状态
        tvScan.setVisibility(View.VISIBLE);// 扫描按钮
        rlHadConnected.setVisibility(View.GONE);// 已连接面板
        tvAvailableNetwork.setVisibility(View.GONE);// available标题
        rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);// 热点列表
        widgetWifiExtenderWait.setVisibility(View.GONE);// 等待
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {// 非隐藏状态下
            resetUi();
            getHotpotInfo();
        }
    }

    private void resetUi() {
        if (activity == null) {
            activity = (HomeRxActivity) getActivity();
        }
        activity.tabFlag = Cons.TAB_WIFI_EXTENDER;
        activity.llNavigation.setVisibility(View.GONE);
        activity.rlBanner.setVisibility(View.GONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.iv_wifiExtender_back, // 返回
                     R.id.tv_wifiExtender_scan, // 扫描
                     R.id.iv_wifiExtender_panel_socket, // 开关
                     R.id.widget_wifi_extender_wait, // 等待界面
                     R.id.rl_wifiExtender_had_connected // 已连接
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_wifiExtender_back: // 返回setting界面
                onBackPressed();
                break;
            case R.id.tv_wifiExtender_scan:
                getHotpotInfo();
                break;
            case R.id.iv_wifiExtender_panel_socket:
                openOrCloseWifiExtender();
                break;

            case R.id.widget_wifi_extender_wait:
                toast(R.string.please_wait_a_moment_to_scan);
                break;
            case R.id.rl_wifiExtender_had_connected:
                disconnectCurrentHotpot();
                break;
        }
    }

    /**
     * 切断当前连接的热点
     */
    private void disconnectCurrentHotpot() {
        // 弹出切断确认对话框
        widgetWifiExtenderDisconnhotpot.setVisibility(View.VISIBLE);
        widgetWifiExtenderDisconnhotpot.setOnClickOkListener(attr -> {
            widgetWifiExtenderWait.setVisibility(View.VISIBLE);
            Extender_DisConnectHotspotHelper disp = new Extender_DisConnectHotspotHelper();
            disp.setOnSuccessListener(object -> getHotpotInfo());
            disp.setOnFailedListener(attr1 -> connectFailed("disconnectCurrentHotpot", null));
            disp.setOnResultErrorListener(attr1 -> connectFailed("disconnectCurrentHotpot", attr1));
            disp.disconnect();
        });
    }

    /**
     * 打开者关闭extender功能
     */
    private void openOrCloseWifiExtender() {
        // 弹出提示框
        widgetWifiExtenderOpenClose.setVisibility(View.VISIBLE);
        widgetWifiExtenderOpenClose.setOnClickokListener(attr -> {
            // 点击OK后设置extender
            int DISABLE = 0;
            int ENABLE = 1;
            widgetWifiExtenderWait.setVisibility(View.VISIBLE);
            Extender_SetWIFIExtenderSettingsHelper sseh = new Extender_SetWIFIExtenderSettingsHelper();
            sseh.setOnSuccessListener(o -> getHotpotInfo());
            sseh.setOnFailedListener(nulls -> connectFailed("openOrCloseWifiExtender", null));
            sseh.setOnResultErrorListener(error -> connectFailed("openOrCloseWifiExtender", error));
            int settingState = (ivPanelSocket.getDrawable() == button_off ? ENABLE : DISABLE);
            Logs.t(TAG).ii("settingState: " + settingState);
            sseh.set(settingState);
        });
    }

    @Override
    public boolean onBackPressed() {

        // 等待界面显示时--> 提示用户不可回退
        if (widgetWifiExtenderWait.getVisibility() == View.VISIBLE) {
            toast(R.string.please_wait_a_moment_to_scan);
            return true;
        }

        // 弹出密码输入框时--> 点击回退--> 隐藏
        if (widgetWifiExtenderPassword.getVisibility() == View.VISIBLE) {
            widgetWifiExtenderPassword.setVisibility(View.GONE);
            return true;
        }

        // 打开或者关闭wifi extender窗口
        if (widgetWifiExtenderOpenClose.getVisibility() == View.VISIBLE) {
            widgetWifiExtenderOpenClose.setVisibility(View.GONE);
            return true;
        }

        // 切断当前热点窗口
        if (widgetWifiExtenderDisconnhotpot.getVisibility() == View.VISIBLE) {
            widgetWifiExtenderDisconnhotpot.setVisibility(View.GONE);
            return true;
        }

        /* 切换fragment的逻辑一律在此处处理 */
        if (eventBusClass == mainRxFragment.class) {
            // 由mainRxFragment传送过来
            activity.fraHelpers.transfer(activity.clazz[Cons.TAB_MAIN]);
        } else {
            // 默认: 返回setting界面
            activity.fraHelpers.transfer(activity.clazz[Cons.TAB_SETTING]);
        }

        return true;
    }

    private void toast(int resId) {
        ToastUtil_m.show(activity, resId);
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
}
