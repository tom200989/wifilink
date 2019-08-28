package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.WifiExtenderAdapter;
import com.alcatel.wifilink.root.bean.Extender_GetHotspotListResult;
import com.alcatel.wifilink.root.bean.Extender_GetWIFIExtenderCurrentStatusResult;
import com.alcatel.wifilink.root.helper.Extender_ConnectHotspotHelper;
import com.alcatel.wifilink.root.helper.Extender_DisConnectHotspotHelper;
import com.alcatel.wifilink.root.helper.Extender_GetConnectHotspotStateHelper;
import com.alcatel.wifilink.root.helper.Extender_GetHotspotListHelper;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderCurrentStatusHelper;
import com.alcatel.wifilink.root.helper.Extender_GetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.helper.Extender_SearchHotspotHelper;
import com.alcatel.wifilink.root.helper.Extender_SetWIFIExtenderSettingsHelper;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.DisConnHotpotView;
import com.alcatel.wifilink.root.widget.ExtenderWait;
import com.alcatel.wifilink.root.widget.HotPotKeyView;
import com.alcatel.wifilink.root.widget.OpenCloseExtenderView;
import com.hiber.cons.TimerState;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by qianli.ma on 2018/2/5 0005.
 */

public class WifiExtenderRxFrag extends BaseFrag {

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

    private String TAG = "WifiExtenderRxFragment";
    private Drawable button_on;
    private Drawable button_off;
    private WifiExtenderAdapter wifiExtenderAdapter;
    private List<Extender_GetHotspotListResult.HotspotListBean> hotspotBeans = new ArrayList<>();
    private int getConnectHotpotCount = 0;// 获取已连接热点的状态计数器
    private Handler handler;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_wifi_extender_rx;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        initRes();
        initAdapter();
        initData();
        initTimer();
        initOnClick();
    }

    @Override
    public void setTimerTask() {
        getWIFISettings();
    }

    private void initTimer() {
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
        timer_period = 5000;
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
        extenderConnectHotspotHelper.setOnFailedListener(attr -> connectFailed());
        extenderConnectHotspotHelper.setOnResultErrorListener(attr -> connectFailed());
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
                        connectFailed();
                    }

                    break;
                case CONNECTING:// 1
                    handler.postDelayed(this::getConnectHotpotState, 1000);
                    break;
                case SUCCESS:// 2
                    getConnectHotpotCount = 0;
                    toast(R.string.success, 2000);
                    getHotpotInfo();// 重新获取
                    break;
                case PASSWORD_ERROR:// 3
                    getConnectHotpotCount = 0;
                    toast(R.string.please_enter_the_correct_password, 2000);
                    widgetWifiExtenderWait.setVisibility(View.GONE);
                    tvScan.setVisibility(View.VISIBLE);
                    break;
                case NEED_PASSWORD:// 4
                    getConnectHotpotCount = 0;
                    toast(R.string.please_enter_the_correct_password, 2000);
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
                        connectFailed();
                    }
                    break;
                case OPEN:// 6
                    getConnectHotpotCount = 0;
                    toast(R.string.success_but_unencrypt, 2000);
                    getHotpotInfo();// 重新获取
                    break;
            }
        });
        helper.setOnFailedListener(attr1 -> connectFailed());
        helper.setOnResultErrorListener(error -> connectFailed());
        helper.get();
    }

    /**
     * 连接失败处理
     */
    private void connectFailed() {
        widgetWifiExtenderWait.setVisibility(View.GONE);
        tvScan.setVisibility(View.VISIBLE);
        toast(R.string.the_connection_failed_please_try_again_extender, 2000);
        getHotpotInfo();// 重新获取
    }


    private void initRes() {
        handler = new Handler();
        button_on = getRootDrawable(R.drawable.general_btn_on);
        button_off = getRootDrawable(R.drawable.general_btn_off);
    }

    private void initData() {
        getHotpotInfo();
    }

    /**
     * 获取热点的所有信息(当前连接的WIFI + 所有的热点列表)
     */
    private void getHotpotInfo() {
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
            } else {
                // 2.获取wifi extender的初始化状态
                int initingStatus = extenderSetting.getExtenderInitingStatus();
                switch (initingStatus) {
                    case INITING:
                        getWIFISettings();
                        break;
                    case COMPLETE:
                        // 2.1.设置描述
                        tvNotConnectPanelDes.setText(getString(R.string.known_networks_will_be_joined_automatically));
                        tvNotConnectTip.setVisibility(View.GONE);
                        ivPanelSocket.setImageDrawable(button_on);
                        getCurrentState();/* 2.获取当前是否有连接的热点 */
                        // getHotpotList();/* 3.获取热点列表 */
                        break;
                    case INITED_FAILED:
                        disconnUi();
                        break;
                }
            }
        });
        getSettingsHelper.setOnGetExtenderFailedListener(() -> {
            toast(R.string.connect_failed, 2000);
            disconnUi();
        });
        getSettingsHelper.get();
    }

    /**
     * 获取当前是否有正在连接的热点
     */
    private void getCurrentState() {
        int DISCONNECT = 0;
        int CONNECTTING = 1;
        int CONNECTTED = 2;
        Extender_GetWIFIExtenderCurrentStatusHelper getCurrentStatusHelper = new Extender_GetWIFIExtenderCurrentStatusHelper();
        getCurrentStatusHelper.setOnSuccessListener(result -> {
            if (result.getHotspotConnectStatus() == DISCONNECT) {
                rlHadConnected.setVisibility(View.GONE);
                getHotpotList(result);/* 3.获取热点列表 */
            } else if (result.getHotspotConnectStatus() == CONNECTTING) {
                getCurrentState();
            } else {
                rlHadConnected.setVisibility(View.VISIBLE);// 已连接显示
                tvHadConnectedHotDotName.setText(RootUtils.turnUrlCode(result.getHotspotSSID()));// SSID(需要进行URL转码)
                ivHadConnectedWifiSignal.setImageDrawable(RootUtils.transferWifiExtenderSignal(result.getSignal()));// 强度
                getHotpotList(result);/* 3.获取热点列表 */
            }
        });
        getCurrentStatusHelper.setOnFailedListener(attr -> {
            toast(R.string.connect_failed, 2000);
            disconnUi();
        });
        getCurrentStatusHelper.setOnResultErrorListener(error -> {
            toast(R.string.get_wifi_extender_current_status_failed, 2000);
            disconnUi();
        });
        getCurrentStatusHelper.get();
    }

    /**
     * 获取热点列表
     */
    private void getHotpotList(Extender_GetWIFIExtenderCurrentStatusResult currentResult) {
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
                    ori_hotpots = RootUtils.excludeCurrentHotpot(ori_hotpots, currentResult.getHotspotSSID());
                    hotspotBeans = RootUtils.turnSSISBatch(ori_hotpots);
                    // 刷新适配器
                    wifiExtenderAdapter.notifys(hotspotBeans);
                    // 等待隐藏
                    widgetWifiExtenderWait.setVisibility(View.GONE);
                    // scan按钮显示
                    tvScan.setVisibility(View.VISIBLE);
                }

            });
            extenderGetHotspotListHelper.setOnGetHotpotFailedListener(() -> {
                widgetWifiExtenderWait.setVisibility(View.GONE);
                tvScan.setVisibility(View.VISIBLE);
                rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
                toast(R.string.get_current_time_and_timezone_failed, 2000);
            });
            extenderGetHotspotListHelper.get();
        });
        extenderSearchHotspotHelper.setOnFailedListener(attr -> {
            widgetWifiExtenderWait.setVisibility(View.GONE);
            tvScan.setVisibility(View.VISIBLE);
            rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
            toast(R.string.connect_failed, 2000);
        });
        extenderSearchHotspotHelper.setOnResultErrorListener(error -> {
            widgetWifiExtenderWait.setVisibility(View.GONE);
            tvScan.setVisibility(View.VISIBLE);
            rcvWifiExtenderAvailableNetwork.setVisibility(View.GONE);
            toast(R.string.get_current_time_and_timezone_failed, 2000);
        });
        extenderSearchHotspotHelper.search();
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
            getHotpotInfo();
        }
    }

    /**
     * 点击事件
     */
    private void initOnClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        tvScan.setOnClickListener(v -> getHotpotInfo());
        ivPanelSocket.setOnClickListener(v -> openOrCloseWifiExtender());
        widgetWifiExtenderWait.setOnClickListener(v -> toast(R.string.please_wait_a_moment_to_scan, 2000));
        rlHadConnected.setOnClickListener(v -> disconnectCurrentHotpot());
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
            disp.setOnDisconnectFailedListener(this::connectFailed);
            disp.setOnDisconnectFailedListener(this::connectFailed);
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
            sseh.setOnFailedListener(nulls -> connectFailed());
            sseh.setOnResultErrorListener(error -> connectFailed());
            int settingState = (ivPanelSocket.getDrawable() == button_off ? ENABLE : DISABLE);
            sseh.set(settingState);
        });
    }

    @Override
    public boolean onBackPresss() {

        // 等待界面显示时--> 提示用户不可回退
        if (widgetWifiExtenderWait.getVisibility() == View.VISIBLE) {
            toast(R.string.please_wait_a_moment_to_scan, 2000);
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
        if (lastFrag == mainFrag.class) {
            // 由mainRxFragment传送过来
            toFrag(getClass(), mainFrag.class, null, false);
        } else {
            // 默认: 返回setting界面
            toFrag(getClass(), SettingFrag.class, null, false);
        }

        return true;
    }
}
