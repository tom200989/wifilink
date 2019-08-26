package com.alcatel.wifilink.root.ue.frag;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.utils.WifiUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.hiber.cons.TimerState;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectionSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetProfileListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetNetworkSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.ChangePinCodeHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.ChangePinStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.ConnectHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.DisConnectHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectionStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkSettingsBeanHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetProfileListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetConnectionSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetNetworkSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageSettingsHelper;

import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/21 0021.
 */
@SuppressLint("InflateParams")
public class SettingNetworkFrag extends BaseFrag {

    @BindView(R.id.rl_monthly_data_plan)
    RelativeLayout rlMonthlyDataPlan;
    @BindView(R.id.tv_setnetwork_done)
    TextView tvDone;
    @BindView(R.id.tv_setnetwork_title)
    TextView tvTitle;
    @BindView(R.id.rl_set_time_limit)
    RelativeLayout rlSetTimeLimit;
    @BindView(R.id.relativelayout_network_profile)
    RelativeLayout relativelayoutNetworkProfile;
    @BindView(R.id.ll_mobile_network)
    LinearLayout mMobileNetwork;
    @BindView(R.id.sett_data_plan)
    LinearLayout mSetDataPlan;
    @BindView(R.id.linear_network_change_pin)
    LinearLayout mChangePin;
    @BindView(R.id.relativelayout_network_roaming)
    RelativeLayout mRoamingRl;
    @BindView(R.id.network_mobile_data_switch)
    SwitchCompat mMobileDataSwitchCompat;
    @BindView(R.id.spinner_connection_mode)
    AppCompatSpinner mConnectionModeSpinner;
    @BindView(R.id.settings_network_mode)
    AppCompatSpinner mNetworkModeSpinner;
    @BindView(R.id.network_roaming_switch)
    ImageView mRoamingSwitchCompat;
    @BindView(R.id.network_sim_pin_switch)
    SwitchCompat mSimPinCompat;
    @BindView(R.id.textview_sim_number)
    TextView mSimNumberTextView;
    @BindView(R.id.textview_network_imsi)
    TextView mImsiTextView;
    @BindView(R.id.textview_network_profile)
    TextView mTvProfile;
    @BindView(R.id.network_set_data_plan)
    RelativeLayout networkSetDataPlan;
    @BindView(R.id.network_change_pin)
    RelativeLayout networkChangePin;
    @BindView(R.id.iv_settingnetwork_back)
    ImageView ivSettingnetworkBack;
    @BindView(R.id.textview_monthly_data_plan)
    TextView mMonthlyDataPlanText;
    @BindView(R.id.setdataplan_billing_day)
    AppCompatSpinner mBillingDaySpinner;
    @BindView(R.id.setdataplan_usagealert)
    AppCompatSpinner mUsageAlertSpinner;
    @BindView(R.id.setdataplan_auto_disconnect)
    SwitchCompat mDisconnectCompat;
    @BindView(R.id.ll_time_limit)
    LinearLayout mTimeLimitLl;
    @BindView(R.id.setdataplan_timelimit)
    SwitchCompat mTimeLimitCompat;
    @BindView(R.id.textview_set_time_limit)
    TextView mSetTimeLimitText;
    @BindView(R.id.setdataplan_limit_auto_disaconect)
    SwitchCompat mLimitAutoDisaconectCompat;
    @BindView(R.id.current_sim_pin)
    EditText mCurrentSimPin;
    @BindView(R.id.new_sim_pin)
    EditText mNewSimPin;
    @BindView(R.id.confirm_new_sim_pin)
    EditText mConfirmNewSimPin;
    @BindView(R.id.rl_pop_setttingPin)
    RelativeLayout rl_settingPin;
    @BindView(R.id.iv_pop_setting_bg)
    ImageView iv_settingPin;
    @BindView(R.id.et_pop_setttingpin)
    EditText et_settingPin;
    @BindView(R.id.tv_pop_settingpin_ok)
    TextView ok_settingPin;
    @BindView(R.id.tv_pop_settingpin_cancel)
    TextView cancel_settingPin;

    @BindView(R.id.wd_setting_network_load)
    HH70_LoadWidget wdLoad;

    private boolean mOldMobileDataEnable;
    private boolean mFirstSetConnectionMode;
    private boolean mFirstSetNetworkMode;
    private boolean mFirstSetBillingDay;
    private boolean isRoaming;
    private Drawable switchOn;
    private Drawable switchOff;
    private GetNetworkSettingsBean mNetworkSettings;
    private GetConnectionSettingsBean mConnectionSettings;
    private GetUsageSettingsBean mUsageSetting;
    private GetSimStatusBean mSimStatus;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_setting_network;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
        initRes();
        initClick();
        initSwitch();
        initData();
    }

    private void initData() {
        int usageLimit_default = ShareUtils.get(Cons.USAGE_LIMIT, 90);
        setDefaultLimit(mUsageAlertSpinner, usageLimit_default);
    }

    private void initSwitch() {
        // 流量开关
        mMobileDataSwitchCompat.setOnCheckedChangeListener((compoundButton, enable) -> {
            if (!mMobileDataSwitchCompat.isPressed()) {
                return;
            }

            if (mOldMobileDataEnable != enable) {
                if (enable) {
                    connect();
                } else {
                    disConnect();
                }
            }
        });

        // connection mode
        mConnectionModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mFirstSetConnectionMode) {
                    mFirstSetConnectionMode = false;
                    return;
                }
                setConnectionSettings(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // network mode
        mNetworkModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mFirstSetNetworkMode) {
                    mFirstSetNetworkMode = false;
                    return;
                }
                if (position == 0) {

                    setNetworkSettings(SetNetworkSettingsParam.CONS_AUTO_MODE);
                } else if (position == 1) {
                    setNetworkSettings(SetNetworkSettingsParam.CONS_ONLY_LTE);
                } else if (position == 2) {
                    setNetworkSettings(SetNetworkSettingsParam.CONS_ONLY_3G);
                } else if (position == 3) {
                    setNetworkSettings(SetNetworkSettingsParam.CONS_ONLY_2G);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // bill day
        mBillingDaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (mFirstSetBillingDay) {
                    mFirstSetBillingDay = false;
                    return;
                }
                mUsageSetting.setBillingDay(position);
                setUsageSetting(mUsageSetting);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // usage
        mUsageAlertSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String[] usageAlertStringArr = getResources().getStringArray(R.array.settings_data_plan_usage_alert);
                String usageAlertPercent = usageAlertStringArr[position];
                int usageLimit = Integer.valueOf(usageAlertPercent.replace("%", ""));
                ShareUtils.set(Cons.USAGE_LIMIT, usageLimit);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // disconnect
        mDisconnectCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!buttonView.isPressed())
                return;
            if (isChecked) {
                mUsageSetting.setAutoDisconnFlag(1);
            } else {
                mUsageSetting.setAutoDisconnFlag(0);
            }
            setUsageSetting(mUsageSetting);
        });

        // time limit
        mTimeLimitCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mTimeLimitLl.setVisibility(View.VISIBLE);
            } else {
                mTimeLimitLl.setVisibility(View.GONE);
                if (mUsageSetting.getTimeLimitFlag() == 1) {
                    mUsageSetting.setTimeLimitFlag(0);
                    setUsageSetting(mUsageSetting);
                }
            }
        });

        // limit auto disconnect
        mLimitAutoDisaconectCompat.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                mUsageSetting.setTimeLimitFlag(1);
            } else {
                mUsageSetting.setTimeLimitFlag(0);
            }
            setUsageSetting(mUsageSetting);
        });
    }

    private void initClick() {
        rlMonthlyDataPlan.setOnClickListener(v -> showSetmonthlyDataPlanDialog());
        rlSetTimeLimit.setOnClickListener(v -> showSetTimeLimitDialog());
        relativelayoutNetworkProfile.setOnClickListener(v -> showProfileLinkDialog());
        mRoamingSwitchCompat.setOnClickListener(v -> modifyRoam());
        mSimPinCompat.setOnClickListener(v -> changePinStatus());
        networkSetDataPlan.setOnClickListener(v -> {
            mSetDataPlan.setVisibility(View.VISIBLE);
            mMobileNetwork.setVisibility(View.GONE);
            tvTitle.setText(getString(R.string.setting_set_data_plan));
            getUsageSetting();
        });
        networkChangePin.setOnClickListener(v -> {
            if (mSimPinCompat.isChecked()) {
                mChangePin.setVisibility(View.VISIBLE);
                tvDone.setVisibility(mChangePin.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
                mMobileNetwork.setVisibility(View.GONE);
                tvTitle.setText(getString(R.string.change_pin));
            } else {
                toast(R.string.please_enable_sim_pin, 3000);
            }
        });
        ivSettingnetworkBack.setOnClickListener(v -> onBackPresss());
        tvDone.setOnClickListener(v -> doneChangePinCode());
    }

    @Override
    public void setTimerTask() {
        getConnectionState();
        getConnectionSettings();
        getNetworkModeSettings();
        getProfileList();
        getSimStatus();
        getSystemInfo();
    }

    private void getConnectionState() {
        GetConnectionStateHelper xGetConnectionStateHelper = new GetConnectionStateHelper();
        xGetConnectionStateHelper.setOnDisconnectedListener(() -> {
            mMobileDataSwitchCompat.setChecked(false);
            mOldMobileDataEnable = false;
        });
        xGetConnectionStateHelper.setOnConnectedListener(() -> {
            mMobileDataSwitchCompat.setChecked(true);
            mOldMobileDataEnable = true;
        });
        xGetConnectionStateHelper.getConnectionState();
    }

    private void getConnectionSettings() {
        GetConnectionSettingsHelper xGetConnectionSettingsHelper = new GetConnectionSettingsHelper();
        xGetConnectionSettingsHelper.setOnGetConnectionSettingsSuccessListener(result -> {
            //  0: manual connect 1: auto connect
            mConnectionSettings = result;
            if (result.getConnectMode() == GetConnectionSettingsBean.CONS_AUTO_CONNECT) {
                mConnectionModeSpinner.setSelection(0);
                mRoamingRl.setVisibility(View.VISIBLE);
            } else if (result.getConnectMode() == GetConnectionSettingsBean.CONS_MANUAL_CONNECT) {
                mConnectionModeSpinner.setSelection(1);
                mRoamingRl.setVisibility(View.GONE);
            }
            if (result.getRoamingConnect() == GetConnectionSettingsBean.CONS_WHEN_ROAMING_CAN_NOT_CONNECT) {
                mRoamingSwitchCompat.setImageResource(R.drawable.pwd_switcher_off);
                isRoaming = false;
            } else if (result.getRoamingConnect() == GetConnectionSettingsBean.CONS_WHEN_ROAMING_CAN_CONNECT) {
                isRoaming = true;
                mRoamingSwitchCompat.setImageResource(R.drawable.pwd_switcher_on);
            }
        });
        xGetConnectionSettingsHelper.getConnectionSettings();
    }

    private void getNetworkModeSettings() {
        GetNetworkSettingsBeanHelper xGetNetworkSettingsBeanHelper = new GetNetworkSettingsBeanHelper();
        xGetNetworkSettingsBeanHelper.setOnGetNetworkSettingsSuccessListener(result -> {
            //  0: auto mode 1: 2G only 2: 3G only 3: LTE only
            mNetworkSettings = result;
            if (result.getNetworkMode() == GetNetworkSettingsBean.CONS_AUTO_MODE) {
                mNetworkModeSpinner.setSelection(0);
            } else if (result.getNetworkMode() == GetNetworkSettingsBean.CONS_ONLY_LTE) {
                mNetworkModeSpinner.setSelection(1);
            } else if (result.getNetworkMode() == GetNetworkSettingsBean.CONS_ONLY_3G) {
                mNetworkModeSpinner.setSelection(2);
            } else if (result.getNetworkMode() == GetNetworkSettingsBean.CONS_ONLY_2G) {
                mNetworkModeSpinner.setSelection(3);
            }
        });
        xGetNetworkSettingsBeanHelper.getNetworkSettings();
    }

    private void getProfileList() {
        GetProfileListHelper xGetProfileListHelper = new GetProfileListHelper();
        xGetProfileListHelper.setOnGetProfileListListener(bean -> {
            List<GetProfileListBean.ProfileBean> list = bean.getProfileList();
            if (list != null) {
                if (list.size() > 0) {
                    mTvProfile.setText(list.get(0).getProfileName());
                }
            }
        });
        xGetProfileListHelper.getProfileList();
    }

    private void getSimStatus() {

        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(attr -> {
            // PinState: 0: unknown
            // 1: enable but not verified
            // 2: PIN enable verified
            // 3: PIN disable
            // 4: PUK required
            // 5: PUK times used out;
            mSimStatus = attr;
            if (attr.getPinState() == 2) {
                mSimPinCompat.setChecked(true);
            } else if (attr.getPinState() == 3) {
                mSimPinCompat.setChecked(false);
            }
        });
        xGetSimStatusHelper.getSimStatus();
    }

    private void getSystemInfo() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            mSimNumberTextView.setText(result.getMSISDN());
            mImsiTextView.setText(result.getIMSI());
        });
        xGetSystemInfoHelper.getSystemInfo();
    }

    private void setConnectionSettings(int connectMode) {
        int connMode = 0;
        if (connectMode == 0) {
            connMode = 1;
        }

        int roamingConnect = mConnectionSettings.getRoamingConnect();
        int pdpType = mConnectionSettings.getPdpType();
        int connOffTime = mConnectionSettings.getConnOffTime();

        SetConnectionSettingsHelper xSetConnectionSettingsHelper = new SetConnectionSettingsHelper();
        xSetConnectionSettingsHelper.setOnsetConnectionSettingsSuccessListener(() -> {
            toast(R.string.success, 3000);
            if (connectMode == 0) {
                mRoamingRl.setVisibility(View.VISIBLE);
            } else if (connectMode == 1) {
                mRoamingRl.setVisibility(View.GONE);
            }
        });
        xSetConnectionSettingsHelper.setConnectionSettings(connMode, roamingConnect, pdpType, connOffTime);
    }

    private void setNetworkSettings(int networkMode) {
        SetNetworkSettingsParam param = new SetNetworkSettingsParam();
        param.setDomesticRoam(mNetworkSettings.getDomesticRoam());
        param.setDomesticRoamGuard(mNetworkSettings.getDomesticRoamGuard());
        param.setNetselectionMode(mNetworkSettings.getNetselectionMode());
        param.setNetworkBand(mNetworkSettings.getNetworkBand());
        param.setNetworkMode(networkMode);
        SetNetworkSettingsHelper xSetNetworkSettingsHelper = new SetNetworkSettingsHelper();
        xSetNetworkSettingsHelper.setOnSetNetworkSettingsSuccessListener(() -> toast(R.string.success, 3000));
        xSetNetworkSettingsHelper.setNetworkSettings(param);
    }

    private void changePinState(String pinCode, int enable) {
        ChangePinStateHelper xChangePinStateHelper = new ChangePinStateHelper();
        xChangePinStateHelper.setOnChangePinStateSuccessListener(() -> {
            rl_settingPin.setVisibility(View.GONE);
            mSimPinCompat.setChecked(enable == 1);
        });
        xChangePinStateHelper.setOnChangePinStateFailedListener(() -> {
            et_settingPin.setText("");
            RootUtils.hideKeyBoard(activity);
            int remainTimes = mSimStatus.getPinRemainingTimes() - 1;
            String content = getString(R.string.pin_error_waring_title) + "\n" + getString(R.string.can_also_enter_times, remainTimes + "");
            toast(content, 3000);
            getSimStatus();
            mSimPinCompat.setChecked(enable != 1);
        });
        xChangePinStateHelper.changePinState(pinCode, enable);
    }

    private void setUsageSetting(GetUsageSettingsBean usageSetting) {
        SetUsageSettingsParam param = new SetUsageSettingsParam();
        param.copy(usageSetting);
        SetUsageSettingsHelper xSetUsageSettingsHelper = new SetUsageSettingsHelper();
        xSetUsageSettingsHelper.setOnSetUsageSettingsSuccessListener(this::getUsageSetting);
        xSetUsageSettingsHelper.setUsageSettings(param);
    }

    private void changePinCode(String newPin, String currentPin) {
        ChangePinCodeHelper xChangePinCodeHelper = new ChangePinCodeHelper();
        xChangePinCodeHelper.setOnChangePinCodeSuccessListener(() -> {
            toast(R.string.success, 3000);
            mCurrentSimPin.setText(null);
            mNewSimPin.setText(null);
            mConfirmNewSimPin.setText(null);
        });
        xChangePinCodeHelper.setOnChangePinCodeFailedListener(() -> {
            toast(R.string.can_also_enter_times, 3000);
            getSimStatus();
        });
        xChangePinCodeHelper.changePinCode(newPin, currentPin);
    }

    private void getUsageSetting() {
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(activity);
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(result -> {
            mUsageSetting = result;
            String unit = "";
            if (result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB) {
                unit = "MB";
            } else if (result.getUnit() == GetUsageSettingsBean.CONS_UNIT_GB) {
                unit = "GB";
            } else if (result.getUnit() == GetUsageSettingsBean.CONS_UNIT_KB) {
                unit = "KB";
            }

            long dataPlanByte = getDataPlanByte(result.getUnit());
            double monthPlan = result.getMonthlyPlan() * 1d / dataPlanByte;
            mMonthlyDataPlanText.setText(String.valueOf(monthPlan + " " + unit));
            mBillingDaySpinner.setSelection(result.getBillingDay());
            // mUsageAlertSpinner
            if (result.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_DISABLE) {
                mDisconnectCompat.setChecked(false);
            } else if (result.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE) {
                mDisconnectCompat.setChecked(true);
            }
            if (result.getTimeLimitFlag() == GetUsageSettingsBean.CONS_TIME_LIMIT_DISABLE) {
                mLimitAutoDisaconectCompat.setChecked(false);
            } else if (result.getTimeLimitFlag() == GetUsageSettingsBean.CONS_TIME_LIMIT_ABLE) {
                mLimitAutoDisaconectCompat.setChecked(true);
            }
            mSetTimeLimitText.setText(String.valueOf(result.getTimeLimitTimes() + getString(R.string.min_s)));
        });
        getUsageSettingsHelper.getUsageSetting();
    }

    /**
     * 修改PIN状态
     */
    private void changePinStatus() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                    int simState = getSimStatusBean.getSIMState();
                    switch (simState) {
                        case GetSimStatusBean.CONS_NOWN:
                            toast(R.string.home_no_sim, 3000);
                            break;
                        case GetSimStatusBean.CONS_PIN_REQUIRED:
                            showSimPinEnableDialog();
                            break;
                        case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                            toast(R.string.Home_PukTimes_UsedOut, 3000);
                            break;
                    }
                });
                xGetSimStatusHelper.getSimStatus();
            } else {
                toast(R.string.connect_failed, 3000);
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    private void modifyRoam() {
        wdLoad.setVisibles();
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(attr -> {
            if (attr.getSIMState() == GetSimStatusBean.CONS_SIM_CARD_READY) {
                // 切换标记位
                isRoaming = !isRoaming;
                int RoamingConnect = isRoaming ? 1 : 0;// 原来是选中状态--> 显示为未选中
                // 先获取一次漫游状态
                GetConnectionSettingsHelper xGetConnectionSettingsHelper = new GetConnectionSettingsHelper();
                xGetConnectionSettingsHelper.setOnGetConnectionSettingsSuccessListener(result -> {
                    // 提交
                    int connectMode = result.getConnectMode();
                    int pdpType = result.getPdpType();
                    int connOffTime = result.getConnOffTime();
                    SetConnectionSettingsHelper xSetConnectionSettingsHelper = new SetConnectionSettingsHelper();
                    xSetConnectionSettingsHelper.setOnsetConnectionSettingsSuccessListener(() -> {
                        // 提交后再次获取一次
                        GetConnectionSettingsHelper xGetConnectionSettingsHelper1 = new GetConnectionSettingsHelper();
                        xGetConnectionSettingsHelper1.setOnGetConnectionSettingsSuccessListener(attr1 -> {
                            wdLoad.setGone();
                            int roamingConnect = result.getRoamingConnect();
                            mRoamingSwitchCompat.setImageResource(roamingConnect == 1 ? R.drawable.pwd_switcher_on : R.drawable.pwd_switcher_off);
                        });
                        xGetConnectionSettingsHelper1.setOnGetConnectionSettingsFailedListener(this::roamError);
                        xGetConnectionSettingsHelper1.getConnectionSettings();

                    });
                    xSetConnectionSettingsHelper.setOnsetConnectionSettingsFailedListener(this::roamError);
                    xSetConnectionSettingsHelper.setConnectionSettings(connectMode, RoamingConnect, pdpType, connOffTime);

                });
                xGetConnectionSettingsHelper.setOnGetConnectionSettingsFailedListener(() -> toast(R.string.connect_failed, 3000));
                xGetConnectionSettingsHelper.getConnectionSettings();
            } else {
                wdLoad.setGone();
                toast(R.string.insert_sim_or_wan, 3000);
            }
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.connect_failed, 3000));
        xGetSimStatusHelper.getSimStatus();
    }

    private void roamError() {
        isRoaming = !isRoaming;
        wdLoad.setGone();
        toast(R.string.connect_failed, 3000);
    }

    private void showSetmonthlyDataPlanDialog() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_monthly_data_plan, null);
        final EditText monthlyNumber = v.findViewById(R.id.monthly_number);
        long dataPlanByte = getDataPlanByte(mUsageSetting.getUnit());
        long monthPlan = mUsageSetting.getMonthlyPlan() / dataPlanByte;
        monthlyNumber.setText(String.valueOf(monthPlan + ""));
        RadioGroup radioGroup = v.findViewById(R.id.radiogroup_monthly_plan);
        RadioButton radioButtonGb = v.findViewById(R.id.radio_monthly_plan_gb);
        RadioButton radioButtonMb = v.findViewById(R.id.radio_monthly_plan_mb);
        if (mUsageSetting.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB) {
            radioButtonMb.setChecked(true);
        } else if (mUsageSetting.getUnit() == GetUsageSettingsBean.CONS_UNIT_GB) {
            radioButtonGb.setChecked(true);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(v);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            String mothlyplan = monthlyNumber.getText().toString();
            mothlyplan = TextUtils.isEmpty(mothlyplan) ? "0" : mothlyplan;
            if (Integer.parseInt(mothlyplan) >= 0 && Integer.parseInt(mothlyplan) <= 1024) {
                if (radioButtonMb.getId() == radioGroup.getCheckedRadioButtonId()) {
                    mUsageSetting.setUnit(GetUsageSettingsBean.CONS_UNIT_MB);
                } else if (radioButtonGb.getId() == radioGroup.getCheckedRadioButtonId()) {
                    mUsageSetting.setUnit(GetUsageSettingsBean.CONS_UNIT_GB);
                }
                long dataPlanByte1 = getDataPlanByte(mUsageSetting.getUnit());
                mUsageSetting.setMonthlyPlan((Long.parseLong(mothlyplan) * dataPlanByte1));
                setUsageSetting(mUsageSetting);
            } else {
                toast(R.string.input_a_data_value_between_0_1024, 3000);
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create();
        builder.show();
    }

    private long getDataPlanByte(int unit) {
        long dataPlanByte = 1;
        if (unit == GetUsageSettingsBean.CONS_UNIT_MB) {
            dataPlanByte = dataPlanByte * 1024L * 1024L;
        } else if (unit == GetUsageSettingsBean.CONS_UNIT_GB) {
            dataPlanByte = dataPlanByte * 1024L * 1024L * 1024L;
        } else if (unit == GetUsageSettingsBean.CONS_UNIT_KB) {
            dataPlanByte = dataPlanByte * 1024L;
        }
        return dataPlanByte;
    }

    private void showSetTimeLimitDialog() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_set_time_limit, null);
        final EditText hrEt = v.findViewById(R.id.dialog_time_limit_hr);
        final EditText minEt = v.findViewById(R.id.dialog_time_limit_min);
        hrEt.setText(String.valueOf(mUsageSetting.getTimeLimitTimes() / 60 + ""));
        minEt.setText(String.valueOf(mUsageSetting.getTimeLimitTimes() % 60 + ""));
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(v);
        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            mUsageSetting.setTimeLimitTimes(Integer.parseInt(hrEt.getText().toString()) * 60 + Integer.parseInt(minEt.getText().toString()));
            setUsageSetting(mUsageSetting);
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.create();
        builder.show();
    }

    private void showSimPinEnableDialog() {
        rl_settingPin.setVisibility(View.VISIBLE);
        et_settingPin.setText("");
        iv_settingPin.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            rl_settingPin.setVisibility(View.GONE);
        });
        ok_settingPin.setOnClickListener(v -> {
            String edContent = RootUtils.getEDText(et_settingPin);
            if (TextUtils.isEmpty(edContent)) {
                toast(R.string.not_empty, 3000);
            } else if (edContent.length() < 4 || edContent.length() > 8) {
                toast(R.string.the_pin_code_should_be_4_8_characters, 3000);
            } else {
                RootUtils.hideKeyBoard(activity);
                changePinState(edContent, mSimPinCompat.isChecked() ? 0 : 1);
            }
        });
        cancel_settingPin.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            rl_settingPin.setVisibility(View.GONE);
        });
    }

    private void showProfileLinkDialog() {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View v = inflater.inflate(R.layout.dialog_profile_link, null);
        v.findViewById(R.id.tv_web_edit_profile).setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            String wifiGateWay = WifiUtils.getWifiGateWay(SmartLinkV3App.getInstance());
            Uri content_url = Uri.parse("http://" + wifiGateWay);
            intent.setData(content_url);
            startActivity(intent);
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(v);
        builder.setNegativeButton(R.string.cancel, null);
        builder.create();
        builder.show();
    }

    private void doneChangePinCode() {
        if (mSimStatus.getPinRemainingTimes() == 0) {
            return;
        }
        String currentPin = RootUtils.getEDText(mCurrentSimPin);
        String newPin = RootUtils.getEDText(mNewSimPin);
        String confirmPin = RootUtils.getEDText(mConfirmNewSimPin);
        if (currentPin.length() == 0) {
            toast(R.string.please_input_your_current_pin_code, 3000);
            return;
        }
        if (newPin.length() == 0) {
            toast(R.string.please_input_your_new_pin_code, 3000);
            return;
        }
        if (!newPin.equals(confirmPin)) {
            toast(R.string.inconsistent_new_pin_code, 3000);
            return;
        }
        if (confirmPin.length() < 4 || confirmPin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters, 3000);
            return;
        }

        changePinCode(newPin, currentPin);
    }

    private void connect() {
        ConnectHelper xConnectHelper = new ConnectHelper();
        xConnectHelper.setOnConnectSuccessListener(() -> {
            toast(R.string.success, 3000);
            mOldMobileDataEnable = true;
        });
        xConnectHelper.setOnConnectFailedListener(() -> {
            toast(R.string.restart_device_tip, 3000);
            mOldMobileDataEnable = false;
            mMobileDataSwitchCompat.setChecked(false);
        });
        xConnectHelper.connect();

    }

    private void disConnect() {
        DisConnectHelper xDisConnectHelper = new DisConnectHelper();
        xDisConnectHelper.setOnDisconnectSuccessListener(() -> {
            toast(R.string.success, 3000);
            mOldMobileDataEnable = false;
        });
        xDisConnectHelper.setOnDisconnectFailedListener(() -> {
            toast(R.string.setting_failed, 3000);
            mOldMobileDataEnable = true;
            mMobileDataSwitchCompat.setChecked(true);
        });
        xDisConnectHelper.disconnect();
    }

    private void initRes() {
        switchOn = getRootDrawable(R.drawable.pwd_switcher_on);
        switchOff = getRootDrawable(R.drawable.pwd_switcher_off);

        mFirstSetConnectionMode = true;
        mFirstSetNetworkMode = true;
        mFirstSetBillingDay = true;
        mNetworkSettings = new GetNetworkSettingsBean();
        mConnectionSettings = new GetConnectionSettingsBean();
        mUsageSetting = new GetUsageSettingsBean();
        mSimStatus = new GetSimStatusBean();
    }

    private void setDefaultLimit(AppCompatSpinner mUsageAlertSpinner, int usageLimit_default) {
        switch (usageLimit_default) {
            case 90:
                mUsageAlertSpinner.setSelection(0, true);
                break;
            case 80:
                mUsageAlertSpinner.setSelection(1, true);
                break;
            case 70:
                mUsageAlertSpinner.setSelection(2, true);
                break;
            case 60:
                mUsageAlertSpinner.setSelection(3, true);
                break;
        }
    }

    @Override
    public boolean onBackPresss() {

        if (rl_settingPin.getVisibility() == View.VISIBLE) {
            RootUtils.hideKeyBoard(activity);
            rl_settingPin.setVisibility(View.GONE);
            return true;

        } else if (mSetDataPlan.getVisibility() == View.VISIBLE) {
            mSetDataPlan.setVisibility(View.GONE);
            mMobileNetwork.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.setting_mobile_network);
            return true;

        } else if (mChangePin.getVisibility() == View.VISIBLE) {
            mChangePin.setVisibility(View.GONE);
            tvDone.setVisibility(mChangePin.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
            mMobileNetwork.setVisibility(View.VISIBLE);
            tvTitle.setText(R.string.setting_mobile_network);
            return true;
        }

        toFrag(getClass(), UsageRxFrag.class, null, true);
        return true;
    }
}
