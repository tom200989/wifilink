package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.ChangePinHelper;
import com.alcatel.wifilink.root.helper.ConnModeHelper;
import com.alcatel.wifilink.root.helper.ConnectSettingHelper;
import com.alcatel.wifilink.root.helper.DataRoamHelper;
import com.alcatel.wifilink.root.helper.MobileDataHelper;
import com.alcatel.wifilink.root.helper.ModeHelper;
import com.alcatel.wifilink.root.helper.NetworkSettingHelper;
import com.alcatel.wifilink.root.helper.PinStatuHelper;
import com.alcatel.wifilink.root.helper.ProfileHelper;
import com.alcatel.wifilink.root.helper.SimNumImsiHelper;
import com.alcatel.wifilink.root.helper.SimPinHelper;
import com.alcatel.wifilink.root.widget.HH70_ChangpinWidget;
import com.alcatel.wifilink.root.widget.HH70_ConmodeWidget;
import com.alcatel.wifilink.root.widget.HH70_ModeWidget;
import com.alcatel.wifilink.root.widget.HH70_ProfileWidget;
import com.alcatel.wifilink.root.widget.HH70_SimpinWidget;
import com.hiber.cons.TimerState;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetProfileListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;

import java.util.List;

import butterknife.BindView;

/**
 * Created by wzhiqiang on 2019/8/19
 */
public class MobileNetworkFrag extends BaseFrag {

    private int PIN = 0;
    private int PUK = 1;
    private String TAG = "MobileNetworkRxFragment";

    @BindView(R.id.iv_mobilenetwork_back)
    ImageView ivBack;// 返回
    @BindView(R.id.iv_mobilenetwork_mobileData)
    ImageView ivMobileData;// mobile data switch
    @BindView(R.id.iv_mobilenetwork_connMode_arrow)
    ImageView ivConnModeArrow;// connection mode arrow
    @BindView(R.id.tv_mobilenetwork_connMode_mode)
    TextView tvConnModeMode;// connection mode text
    @BindView(R.id.iv_mobilenetwork_dataRoaming)
    ImageView ivDataRoaming;// data roaming switch
    @BindView(R.id.rl_mobilenetwork_setDataPlan)
    RelativeLayout rlSetDataPlan;// set data plan
    @BindView(R.id.iv_mobilenetwork_mode_arrow)
    ImageView ivModeArrow;// mode arrow
    @BindView(R.id.tv_mobilenetwork_mode_mode)
    TextView tvModeMode;// mode text
    @BindView(R.id.rl_mobilenetwork_profile)
    RelativeLayout rlProfile;// profile tag
    @BindView(R.id.tv_mobilenetwork_profile_name)
    TextView tvProfileName;// profile name
    @BindView(R.id.iv_mobilenetwork_simPin)
    ImageView ivSimPin;// sim pin switch
    @BindView(R.id.rl_mobilenetwork_changeSimPin)
    RelativeLayout rlChangeSimPin;// change sim pin
    @BindView(R.id.tv_mobilenetwork_simNum)
    TextView tvSimNum;// sim num
    @BindView(R.id.tv_mobilenetwork_imsi)
    TextView tvImsi;// imsi
    @BindView(R.id.wg_profile)
    HH70_ProfileWidget profileWidget;
    @BindView(R.id.wg_simpin)
    HH70_SimpinWidget simpinWidget;
    @BindView(R.id.wg_conmode)
    HH70_ConmodeWidget conmodeWidget;
    @BindView(R.id.wg_changpin)
    HH70_ChangpinWidget changpinWidget;
    @BindView(R.id.wg_mode)
    HH70_ModeWidget modeWidget;

    private String text_auto;
    private String text_manual;
    private String text_4G;
    private String text_3G;
    private String text_2G;
    private String[] modes;
    private Drawable switch_on;
    private Drawable switch_off;
    private ConnectSettingHelper connSettingHelper;
    private NetworkSettingHelper networkSettingHelper;
    private SimNumImsiHelper simNumImsiHelper;
    private ConnectSettingHelper connSettingInitHelper;

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initRes();// 初始化资源
        initClick();
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
        timer_period = 2500;
    }

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_mobilenetwork;
    }

    private void initRes() {
        text_auto = activity.getString(R.string.hh70_auto);
        text_manual = activity.getString(R.string.hh70_maunal);
        text_4G = activity.getString(R.string.hh70_4g);
        text_3G = activity.getString(R.string.hh70_3g);
        text_2G = activity.getString(R.string.hh70_2g);
        modes = new String[]{text_auto, text_4G, text_3G, text_2G};
        switch_on = getRootDrawable(R.drawable.switch_on);
        switch_off = getRootDrawable(R.drawable.switch_off);
    }

    private void initClick() {
        ivBack.setOnClickListener(v -> onBackPressed());// 回退
        ivMobileData.setOnClickListener(v -> clickMobileData());// mobile data
        ivConnModeArrow.setOnClickListener(v -> clickConnectionMode());// connection mode
        tvConnModeMode.setOnClickListener(v -> clickConnectionMode());// connection mode
        ivDataRoaming.setOnClickListener(v -> clickRoaming());// 漫游开关
        rlSetDataPlan.setOnClickListener(v -> clickSetDataPlan());// 设置流量计划
        ivModeArrow.setOnClickListener(v -> clickMode());// 模式切换4G|3G|2G
        tvModeMode.setOnClickListener(v -> clickMode());// 模式切换4G|3G|2G
        rlProfile.setOnClickListener(v -> clickProfile());// profile提示
        ivSimPin.setOnClickListener(v -> clickSimPin());// 开启PIN码 | 关闭PIN码
        rlChangeSimPin.setOnClickListener(v -> clickChangePin());// 修改PIN码
    }

    @Override
    public void setTimerTask() {
        super.setTimerTask();
        getMobileData();// 获取网络连接状态
        getConnMode();// 获取连接模式(auto | manual)
        getDataRoaming();// 获取漫游状态
        getMode();// 获取连接信号类型(4G|3G|2G)
        getSimPin();// 获取SIM是否设置PIN码
        getSimAndImsi();// 获取SIM NUM以及IMSI
        getProfileList();// 获取profile name
    }

    /**
     * 获取profile name
     */
    private void getProfileList() {
        ProfileHelper prh = new ProfileHelper();
        prh.setOnProfileSuccessListener(profileList -> {
            List<GetProfileListBean.ProfileBean> list = profileList.getProfileList();
            if (list != null) {
                if (list.size() > 0) {
                    GetProfileListBean.ProfileBean profile = list.get(0);
                    tvProfileName.setText(profile.getProfileName());
                }
            }
        });
        prh.get();
    }

    /**
     * 获取SIM NUM以及IMSI
     */
    private void getSimAndImsi() {
        if (simNumImsiHelper == null) {
            simNumImsiHelper = new SimNumImsiHelper();
            simNumImsiHelper.setOnSimNumImsiFailedListener(this::simNumImsiEmpty);
            simNumImsiHelper.setOnSimNumberListener(simNum -> tvSimNum.setText(simNum));
            simNumImsiHelper.setOnImsiListener(imsi -> tvImsi.setText(imsi));
        }
        simNumImsiHelper.getSimNumAndImsi();
    }

    /**
     * 获取SIM是否设置PIN码
     */
    private void getSimPin() {
        PinStatuHelper pinStatuHelper = new PinStatuHelper(activity);
        pinStatuHelper.setOnNormalPinStatesListener(result -> {
            boolean isPinEnable = result.getPinState() == GetSimStatusBean.CONS_FOR_PIN_PIN_ENABLE_VERIFIED;
            ivSimPin.setImageDrawable(isPinEnable ? switch_on : switch_off);
        });
        pinStatuHelper.getRoll();
    }

    /**
     * 获取连接信号类型(4G|3G|2G)
     */
    private void getMode() {
        if (networkSettingHelper == null) {
            networkSettingHelper = new NetworkSettingHelper();
            networkSettingHelper.setOnAutoListener(attr -> tvModeMode.setText(text_auto));
            networkSettingHelper.setOn2GListener(attr -> tvModeMode.setText(text_2G));
            networkSettingHelper.setOn3GListener(attr -> tvModeMode.setText(text_3G));
            networkSettingHelper.setOn4GListener(attr -> tvModeMode.setText(text_4G));
        }
        networkSettingHelper.getNetworkSetting();
    }

    /**
     * 获取漫游状态
     */
    private void getDataRoaming() {
        if (connSettingInitHelper == null) {
            connSettingInitHelper = new ConnectSettingHelper();
            connSettingInitHelper.setOnRoamConnListener(attr -> ivDataRoaming.setImageDrawable(switch_on));
            connSettingInitHelper.setOnRoamNotConnListener(attr -> ivDataRoaming.setImageDrawable(switch_off));
        }
        connSettingInitHelper.getConnWhenRoam();
    }

    /**
     * 获取网络连接状态
     */
    private void getMobileData() {
        GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetConnectionStateHelper.setOnDisConnectingListener(() -> ivMobileData.setImageDrawable(switch_off));
        xGetConnectionStateHelper.setOnDisconnectedListener(() -> ivMobileData.setImageDrawable(switch_off));
        xGetConnectionStateHelper.setOnConnectedListener(() -> ivMobileData.setImageDrawable(switch_on));
        xGetConnectionStateHelper.getConnectionState();
    }

    /**
     * 获取连接模式(auto | manual)
     */
    private void getConnMode() {
        if (connSettingHelper == null) {
            connSettingHelper = new ConnectSettingHelper();
            connSettingHelper.setOnConnAutoListener(attr -> tvConnModeMode.setText(text_auto));
            connSettingHelper.setOnConnManualListener(attr -> tvConnModeMode.setText(text_manual));
        }
        connSettingHelper.getConnSettingStatus();
    }

    /**
     * 点击了set data plan 操作
     */
    private void clickSetDataPlan() {
        toFrag(getClass(), SetDataPlanFrag.class, null, true);
    }

    /**
     * 点击了mobile data操作
     */
    private void clickMobileData() {
        MobileDataHelper mobileDataHelper = new MobileDataHelper(activity);
        mobileDataHelper.setOnConnSuccessListener(attr -> ivMobileData.setImageDrawable(switch_on));
        mobileDataHelper.setOnDisConnSuccessListener(attr -> ivMobileData.setImageDrawable(switch_off));
        mobileDataHelper.toConnOrNot();
    }

    /**
     * 点击connectionMode
     */
    private void clickConnectionMode() {
        conmodeWidget.setOnAutoClickListener(() -> setConnMode(GetConnectionSettingsBean.CONS_AUTO_CONNECT));
        conmodeWidget.setOnManualClickListener(() -> setConnMode(GetConnectionSettingsBean.CONS_MANUAL_CONNECT));
        conmodeWidget.setVisibility(View.VISIBLE);
    }

    private void setConnMode(int connMode) {
        ConnModeHelper connModeHelper = new ConnModeHelper(activity);
        connModeHelper.setOnConnModeSuccessListener(result -> tvConnModeMode.setText(result.getConnectMode() == GetNetworkSettingsBean.CONS_AUTO_MODE ? text_auto : text_manual));
        connModeHelper.transferConnMode(connMode);
    }

    /**
     * 点击了data roaming
     */
    private void clickRoaming() {
        DataRoamHelper dataRoamHelper = new DataRoamHelper(activity);
        dataRoamHelper.setOnRoamConnSuccessListener(result -> ivDataRoaming.setImageDrawable(result.getRoamingConnect() == GetConnectionSettingsBean.CONS_WHEN_ROAMING_CAN_CONNECT ? switch_on : switch_off));
        dataRoamHelper.transfer();
    }

    /**
     * 点击了mode
     */
    private void clickMode() {
        modeWidget.setOnAutoClickListener(() -> changeMode(GetNetworkSettingsBean.CONS_AUTO_MODE));
        modeWidget.setOn4gModeClickListener(() -> changeMode(GetNetworkSettingsBean.CONS_ONLY_LTE));
        modeWidget.setOn3gModeClickListener(() -> changeMode(GetNetworkSettingsBean.CONS_ONLY_3G));
        modeWidget.setOn2gModeClickListener(() -> changeMode(GetNetworkSettingsBean.CONS_ONLY_2G));
        modeWidget.setVisibility(View.VISIBLE);
    }

    /**
     * 修改模式
     */
    private void changeMode(int mode) {
        ModeHelper modeHelper = new ModeHelper(activity);
        modeHelper.setOnModeSuccessListener(attr -> tvModeMode.setText(modes[attr.getNetworkMode()]));
        modeHelper.transfer(mode);
    }

    /**
     * 点击了profile
     */
    private void clickProfile() {
        profileWidget.setVisibility(View.VISIBLE);
    }

    /**
     * 点击了sim pin
     */
    private void clickSimPin() {
        simpinWidget.setOnOkClickListener(() -> {
            // 提交修改
            SimPinHelper simPinHelper = new SimPinHelper(activity);
            simPinHelper.setOnPukLockedListener(attr -> toPukRx());
            simPinHelper.setOnPinEnableListener(() -> {// 开启成功
                toast(R.string.hh70_success);
                ivSimPin.setImageDrawable(switch_on);
            });
            simPinHelper.setOnPinDisableListener(() -> {// 关闭成功
                toast(R.string.hh70_success);
                ivSimPin.setImageDrawable(switch_off);
            });
            simPinHelper.setOnPinTimeoutListener(attr -> toPukRx());// PIN次数超过限制
            simPinHelper.transfer(simpinWidget.getEtString());
        });
        simpinWidget.setVisibility(View.VISIBLE);
    }

    /**
     * 点击change pin
     */
    private void clickChangePin() {
        PinStatuHelper pinStatuHelper = new PinStatuHelper(activity);
        pinStatuHelper.setOnNormalPinStatesListener(attr1 -> {
            if (attr1.getPinState() != GetSimStatusBean.CONS_FOR_PIN_PIN_ENABLE_VERIFIED) {
                toast(R.string.hh70_enable_sim_pin);
            } else {
                changpinWidget.setOnOkClickListener(() -> {
                    String currentPin = changpinWidget.getCurrentPin();
                    String newPin = changpinWidget.getNewPin();
                    String confirmPin = changpinWidget.getConfirmPin();

                    ChangePinHelper changePinHelper = new ChangePinHelper(activity);
                    changePinHelper.setOnPinTimeoutListener(attr -> toPukRx());
                    changePinHelper.setOnChangePinSuccessListener(attr -> {
                    });
                    changePinHelper.change(currentPin, newPin, confirmPin);
                });
                changpinWidget.setVisibility(View.VISIBLE);
            }
        });
        pinStatuHelper.getRoll();
    }

    /**
     * 前往PUK界面
     */
    private void toPukRx() {
        toFrag(getClass(), PukRxFrag.class, null, true);
    }

    /**
     * 设置sim num & imsi为空
     */
    private void simNumImsiEmpty() {
        if (tvSimNum != null) {
            tvSimNum.setText("");
        }
        if (tvImsi != null) {
            tvImsi.setText("");
        }
    }

    public void toast(int resId) {
        toast(resId, 2000);
    }

    public void toast(String content) {
        toast(content, 2000);
    }

    public void toastLong(int resId) {
        toast(resId, 3000);
    }

    @Override
    public boolean onBackPressed() {
        if (profileWidget.getVisibility() == View.VISIBLE) {
            profileWidget.setVisibility(View.GONE);
        } else if (simpinWidget.getVisibility() == View.VISIBLE) {
            simpinWidget.setVisibility(View.GONE);
        } else if (conmodeWidget.getVisibility() == View.VISIBLE) {
            conmodeWidget.setVisibility(View.GONE);
        } else if (changpinWidget.getVisibility() == View.VISIBLE) {
            changpinWidget.setVisibility(View.GONE);
        } else if (modeWidget.getVisibility() == View.VISIBLE) {
            modeWidget.setVisibility(View.GONE);
        }
        if (lastFrag == UsageRxFrag.class) {
            toFrag(getClass(), UsageRxFrag.class, null, true);
        } else {
            toFrag(getClass(), SettingFrag.class, null, true);
        }
        return true;
    }
}
