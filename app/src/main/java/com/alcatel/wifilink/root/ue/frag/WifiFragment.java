package com.alcatel.wifilink.root.ue.frag;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.bean.AP;
import com.alcatel.wifilink.root.bean.WlanSettings;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.PreHelper;
import com.alcatel.wifilink.root.helper.SystemInfoHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.WepPsdHelper;
import com.alcatel.wifilink.root.helper.WpaPsdHelper;
import com.alcatel.wifilink.root.ue.activity.HomeRxActivity;
import com.alcatel.wifilink.root.ue.activity.RefreshWifiActivity;
import com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.C_ENUM;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSupportModeBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWlanSupportModeHelper;

import static android.app.Activity.RESULT_OK;
import static com.alcatel.wifilink.R.id.text_advanced_settings_2g;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_AP_ISOLATION;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_BANDWIDTH;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_CHANNEL;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_COUNTRY;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_FRE;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_MODE_80211;
import static com.alcatel.wifilink.root.ue.activity.WlanAdvancedSettingsActivity.EXTRA_SSID_BROADCAST;

public class WifiFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener, FragmentBackHandler {

    private static final String TAG = "WifiFragment";

    public static final int REQUEST_CODE_ADVANCED_SETTINGS_2_4G = 0x1000;
    public static final int REQUEST_CODE_ADVANCED_SETTINGS_5G = 0x1001;


    private ViewGroup m2GSettingsGroup;
    private ViewGroup m5GSettingsGroup;

    private SwitchCompat mWifi2GSwitch;// 2.4G wifi开关
    private TextView m2GAdvancedText;
    private SwitchCompat mWifi5GSwitch;// 5G wifi开关
    private TextView m5GAdvancedText;

    private AppCompatSpinner mSecurity2GSpinner;
    private AppCompatSpinner mSecurity5GSpinner;

    private AppCompatSpinner mEncryption2GSpinner;
    private AppCompatSpinner mEncryption5GSpinner;

    private EditText mSsid2GEdit;
    private EditText mSsid5GEdit;
    private EditText mKey2GEdit;
    private EditText mKey5GEdit;


    private ViewGroup mEncryption2GGroup;
    private ViewGroup mEncryption5GGroup;

    private ViewGroup mKey2GGroup;
    private ViewGroup mKey5GGroup;

    private View mDividerView;

    private WlanSettings mOriginSettings;
    private WlanSettings mEditedSettings;

    private Context mContext;
    private int mSupportMode;
    private ProgressDialog mProgressDialog;
    private String[] mWpaEncryptionSettings;
    private String[] mWepEncryptionSettings;
    private TimerHelper wifiTimer;
    private HomeRxActivity activity;
    private RelativeLayout rlWait;

    private LinearLayout mllSsid2G;
    private RelativeLayout mrlSecurity2G;
    private LinearLayout mllSsid5G;
    private RelativeLayout mrlSecurity5G;
    private View[] views_2p4;// 2G 视图
    private View[] views_5G;// 5G 视图
    private String deviceName = "hh70";// 默认HH70
    private int wlanState_2g;// 2G是否开启
    private int wlanState_5g;// 5G是否开启
    private int WLAN_2G_ON = 1;// 2G是否开启
    private int WLAN_5G_OFF = 1;// 5G是否开启
    private DialogOkWidget dgWifiSettingrxOk;

    public WifiFragment() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            resetUi();
        }
    }

    private void resetUi() {
        if (activity == null) {
            activity = (HomeRxActivity) getActivity();
        }
        activity.tabFlag = Cons.TAB_WIFI;
        activity.llNavigation.setVisibility(View.VISIBLE);
        activity.rlBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 获取各类初始化信息(devices name, wifi on or off)
        getKindState();
    }

    /**
     * 获取各类初始化信息(devices name, wifi on or off)
     */
    private void getKindState() {
        SystemInfoHelper sif = new SystemInfoHelper();
        sif.setOnGetSystemInfoSuccessListener(systeminfo -> {
            // 1.得到设备名
            deviceName = systeminfo.getDeviceName().toLowerCase();
            // 2.获取systemstatus--> 得到目前正在启用的WIFI
            PreHelper pr = new PreHelper(getActivity());
            pr.setOnSuccessListener(systemStates -> {
                wlanState_2g = systemStates.getWlanState_2g();
                wlanState_5g = systemStates.getWlanState_5g();
                requestWlanSupportMode();
            });
            pr.setOnErrorListener(systemStates -> requestWlanSupportMode());
            pr.setOnResultErrorListener(systemStates -> requestWlanSupportMode());
            pr.get();
        });
        sif.setOnResultErrorListener(systeminfo -> requestWlanSupportMode());
        sif.setOnErrorListener(systeminfo -> requestWlanSupportMode());
        sif.get();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (HomeRxActivity) getActivity();
        resetUi();
        return inflater.inflate(R.layout.wifi_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        SystemInfoHelper sif = new SystemInfoHelper();
        sif.setOnGetSystemInfoSuccessListener(attr -> {
            deviceName = attr.getDeviceName().toLowerCase();
            initUi(view, OtherUtils.isMw120(deviceName));
        });
        sif.setOnErrorListener(attr -> {
            initUi(view, false);
        });
        sif.setOnResultErrorListener(attr -> {
            initUi(view, false);
        });
        sif.get();
    }

    private void initUi(View view, boolean isMw120) {
        // 2.4G kind of panel
        mWifi2GSwitch = (SwitchCompat) view.findViewById(R.id.switch_wifi_2g);
        mllSsid2G = (LinearLayout) view.findViewById(R.id.ll_ssid_2g);
        mrlSecurity2G = (RelativeLayout) view.findViewById(R.id.rl_security_2g);
        mEncryption2GGroup = (ViewGroup) view.findViewById(R.id.rl_encryption_2g);
        mKey2GGroup = (ViewGroup) view.findViewById(R.id.ll_key_2g);
        m2GAdvancedText = (TextView) view.findViewById(text_advanced_settings_2g);
        views_2p4 = new View[]{mllSsid2G, mrlSecurity2G, mEncryption2GGroup, mKey2GGroup, m2GAdvancedText};

        // 5G kind of panel
        mWifi5GSwitch = (SwitchCompat) view.findViewById(R.id.switch_wifi_5g);
        mllSsid5G = (LinearLayout) view.findViewById(R.id.ll_ssid_5g);
        mrlSecurity5G = (RelativeLayout) view.findViewById(R.id.rl_security_5g);
        mEncryption5GGroup = (ViewGroup) view.findViewById(R.id.rl_encryption_5g);
        mKey5GGroup = (ViewGroup) view.findViewById(R.id.ll_key_5g);
        m5GAdvancedText = (TextView) view.findViewById(R.id.text_advanced_settings_5g);
        views_5G = new View[]{mllSsid5G, mrlSecurity5G, mEncryption5GGroup, mKey5GGroup, m5GAdvancedText};

        mWifi2GSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Logs.t("ma_wifi_switch").ii("2.4G: " + isChecked);

            if (isMw120) {
                hideOrShow(isChecked ? views_5G : views_2p4, isChecked ? views_2p4 : views_5G);
                mWifi5GSwitch.setChecked(!isChecked);
            }

        });

        mWifi5GSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Logs.t("ma_wifi_switch").ii("5G: " + isChecked);

            if (isMw120) {
                hideOrShow(isChecked ? views_2p4 : views_5G, isChecked ? views_5G : views_2p4);
                mWifi2GSwitch.setChecked(!isChecked);
            }

        });

        // other
        rlWait = (RelativeLayout) view.findViewById(R.id.rl_wifiSettingrx_wait);
        rlWait.setOnClickListener(v -> ToastUtil_m.show(getActivity(), getString(R.string.connecting)));

        m2GSettingsGroup = (ViewGroup) view.findViewById(R.id.ll_settings_2g);
        m5GSettingsGroup = (ViewGroup) view.findViewById(R.id.ll_settings_5g);

        m2GAdvancedText.setOnClickListener(this);
        m5GAdvancedText.setOnClickListener(this);

        mSsid2GEdit = (EditText) view.findViewById(R.id.edit_ssid_2g);
        mSsid5GEdit = (EditText) view.findViewById(R.id.edit_ssid_5g);

        mKey2GEdit = (EditText) view.findViewById(R.id.edit_key_2g);
        mKey5GEdit = (EditText) view.findViewById(R.id.edit_key_5g);

        dgWifiSettingrxOk = (DialogOkWidget) view.findViewById(R.id.dg_wifiSettingrx_ok);

        mSecurity2GSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner_security_2g);
        mSecurity5GSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner_security_5g);
        mSecurity2GSpinner.setOnItemSelectedListener(this);
        mSecurity5GSpinner.setOnItemSelectedListener(this);

        mEncryption2GSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner_encryption_2g);
        mEncryption5GSpinner = (AppCompatSpinner) view.findViewById(R.id.spinner_encryption_5g);

        view.findViewById(R.id.btn_apply).setOnClickListener(this);
        view.findViewById(R.id.btn_cancel).setOnClickListener(this);

        mDividerView = view.findViewById(R.id.divider);

        mWpaEncryptionSettings = getActivity().getResources().getStringArray(R.array.wlan_settings_wpa_type);
        mWepEncryptionSettings = getActivity().getResources().getStringArray(R.array.wlan_settings_wep_type);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void requestWlanSettings() {
        RX.getInstant().getWlanSettings(new ResponseObject<WlanSettings>() {
            @Override
            protected void onSuccess(WlanSettings result) {
                mOriginSettings = result;
                mEditedSettings = mOriginSettings.clone();
                updateUIWithWlanSettings();
            }

            @Override
            protected void onFailure() {

            }
        });
    }


    private void requestWlanSupportMode() {

        GetWlanSupportModeHelper xGetWlanSupportModeHelper = new GetWlanSupportModeHelper();
        xGetWlanSupportModeHelper.setOnGetWlanSupportModeSuccessListener(attr -> {
            // 获取到wlan支持模式
            // 0: 2.4G
            // 1: 5G
            // 2: 2.4G+2.4G
            // 3: 5G+5G
            int wlanSupportAPMode = attr.getWlanAPMode();
            updateUIWithSupportMode(wlanSupportAPMode);
            requestWlanSettings();
        });
        xGetWlanSupportModeHelper.getWlanSupportMode();
    }

    private void updateUIWithWlanSettings() {
        if (mOriginSettings == null) {
            return;
        }
        if (mSupportMode == GetWlanSupportModeBean.CONS_WLAN_2_4G) {
            set2GData();
        } else if (mSupportMode == GetWlanSupportModeBean.CONS_WLAN_5G) {
            set5GData();
        } else {
            set2GData();
            set5GData();
        }

        // 判断新设备--> 隐藏相关布局
        isMW120();
    }

    /* 5G */
    private void set5GData() {
        mWifi5GSwitch.setChecked(mOriginSettings.getAP5G().isApEnabled());
        mSsid5GEdit.setText(mOriginSettings.getAP5G().getSsid());
        mSecurity5GSpinner.setSelection(mOriginSettings.getAP5G().getSecurityMode());
        if (mOriginSettings.getAP5G().getSecurityMode() == C_ENUM.SecurityMode.Disable.ordinal()) {
            mEncryption5GGroup.setVisibility(View.GONE);
            mKey5GGroup.setVisibility(View.GONE);
            mEncryption5GSpinner.setSelection(-1);
            mKey5GEdit.setText("");
        } else if (mOriginSettings.getAP5G().getSecurityMode() == C_ENUM.SecurityMode.WEP.ordinal()) {
            mEncryption5GGroup.setVisibility(View.VISIBLE);
            mKey5GGroup.setVisibility(View.VISIBLE);
            mEncryption5GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
            mEncryption5GSpinner.setSelection(mOriginSettings.getAP5G().getWepType());
            mKey5GEdit.setText(mOriginSettings.getAP5G().getWepKey());
        } else {
            mEncryption5GGroup.setVisibility(View.VISIBLE);
            mKey5GGroup.setVisibility(View.VISIBLE);
            mEncryption5GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
            mEncryption5GSpinner.setSelection(mOriginSettings.getAP5G().getWpaType());
            mKey5GEdit.setText(mOriginSettings.getAP5G().getWpaKey());
        }
    }

    /* 2G */
    private void set2GData() {
        // 先设置
        mWifi2GSwitch.setChecked(mOriginSettings.getAP2G().isApEnabled());
        mSsid2GEdit.setText(mOriginSettings.getAP2G().getSsid());
        int securityMode = mOriginSettings.getAP2G().getSecurityMode();
        mSecurity2GSpinner.setSelection(securityMode);
        if (securityMode == C_ENUM.SecurityMode.Disable.ordinal()) {
            mEncryption2GGroup.setVisibility(View.GONE);
            mKey2GGroup.setVisibility(View.GONE);
            mEncryption2GSpinner.setSelection(-1);
            mKey2GEdit.setText("");
        } else if (securityMode == C_ENUM.SecurityMode.WEP.ordinal()) {
            mEncryption2GGroup.setVisibility(View.VISIBLE);
            mKey2GGroup.setVisibility(View.VISIBLE);
            mEncryption2GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
            mEncryption2GSpinner.setSelection(mOriginSettings.getAP2G().getWepType());
            mKey2GEdit.setText(mOriginSettings.getAP2G().getWepKey());
        } else {
            mEncryption2GGroup.setVisibility(View.VISIBLE);
            mKey2GGroup.setVisibility(View.VISIBLE);
            mEncryption2GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
            mEncryption2GSpinner.setSelection(mOriginSettings.getAP2G().getWpaType());
            mKey2GEdit.setText(mOriginSettings.getAP2G().getWpaKey());
        }
    }

    private void updateUIWithSupportMode(int supportMode) {

        mSupportMode = supportMode;
        if (supportMode == C_ENUM.WlanSupportMode.Mode2Point4G.ordinal()) {
            m2GSettingsGroup.setVisibility(View.VISIBLE);
            m5GSettingsGroup.setVisibility(View.GONE);
            mDividerView.setVisibility(View.GONE);
        } else if (supportMode == C_ENUM.WlanSupportMode.Mode5G.ordinal()) {
            m2GSettingsGroup.setVisibility(View.GONE);
            m5GSettingsGroup.setVisibility(View.VISIBLE);
            mDividerView.setVisibility(View.GONE);
        } else if (supportMode == C_ENUM.WlanSupportMode.Mode2Point4GAnd5G.ordinal()) {
            m2GSettingsGroup.setVisibility(View.VISIBLE);
            m5GSettingsGroup.setVisibility(View.VISIBLE);
        } else {
            m2GSettingsGroup.setVisibility(View.GONE);
            m5GSettingsGroup.setVisibility(View.GONE);
            mDividerView.setVisibility(View.GONE);
        }
    }

    /**
     * 是否为mw120新型设备
     */
    private void isMW120() {
        // 判断是否为新设备
        boolean isMW120 = OtherUtils.isMw120(deviceName);
        Logs.t(TAG).ii("ismw120: " + isMW120);
        if (isMW120) {
            // 隐藏相关布局
            if (wlanState_2g == WLAN_2G_ON) {
                hideOrShow(views_5G, views_2p4);
            } else {
                hideOrShow(views_2p4, views_5G);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.text_advanced_settings_2g) {
            Intent intent = new Intent(mContext, WlanAdvancedSettingsActivity.class);
            intent.putExtra(EXTRA_FRE, 2);
            intent.putExtra(EXTRA_SSID_BROADCAST, mEditedSettings.getAP2G().isSsidHiden());
            intent.putExtra(EXTRA_CHANNEL, mEditedSettings.getAP2G().getChannel());
            intent.putExtra(EXTRA_COUNTRY, mEditedSettings.getAP2G().getCountryCode());
            intent.putExtra(EXTRA_BANDWIDTH, mEditedSettings.getAP2G().getBandwidth());
            intent.putExtra(EXTRA_MODE_80211, mEditedSettings.getAP2G().getWMode());
            intent.putExtra(EXTRA_AP_ISOLATION, mEditedSettings.getAP2G().isApIsolated());
            startActivityForResult(intent, REQUEST_CODE_ADVANCED_SETTINGS_2_4G);
        } else if (v.getId() == R.id.text_advanced_settings_5g) {
            Intent intent = new Intent(mContext, WlanAdvancedSettingsActivity.class);
            intent.putExtra(EXTRA_FRE, 5);
            intent.putExtra(EXTRA_SSID_BROADCAST, mEditedSettings.getAP5G().isSsidHiden());
            intent.putExtra(EXTRA_CHANNEL, mEditedSettings.getAP5G().getChannel());
            intent.putExtra(EXTRA_COUNTRY, mEditedSettings.getAP5G().getCountryCode());
            intent.putExtra(EXTRA_BANDWIDTH, mEditedSettings.getAP5G().getBandwidth());
            intent.putExtra(EXTRA_MODE_80211, mEditedSettings.getAP5G().getWMode());
            intent.putExtra(EXTRA_AP_ISOLATION, mEditedSettings.getAP5G().isApIsolated());
            startActivityForResult(intent, REQUEST_CODE_ADVANCED_SETTINGS_5G);
        } else if (v.getId() == R.id.btn_apply) {
            // 检查密码规则
            showApplySettingsDlg();
        } else if (v.getId() == R.id.btn_cancel) {
            restoreSettings();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((REQUEST_CODE_ADVANCED_SETTINGS_2_4G == requestCode || REQUEST_CODE_ADVANCED_SETTINGS_5G == requestCode) && resultCode == RESULT_OK) {
            boolean broadcast = data.getBooleanExtra(EXTRA_SSID_BROADCAST, false);
            int channel = data.getIntExtra(EXTRA_CHANNEL, 0);
            String countryCode = data.getStringExtra(EXTRA_COUNTRY);
            int bandwidth = data.getIntExtra(EXTRA_BANDWIDTH, 0);
            int mode80211 = data.getIntExtra(EXTRA_MODE_80211, 0);
            boolean isolation = data.getBooleanExtra(EXTRA_AP_ISOLATION, false);

            AP ap = requestCode == REQUEST_CODE_ADVANCED_SETTINGS_2_4G ? mEditedSettings.getAP2G() : mEditedSettings.getAP5G();
            ap.setSsidHidden(broadcast);
            ap.setChannel(channel);
            ap.setCountryCode(countryCode);
            ap.setBandwidth(bandwidth);
            ap.setWMode(mode80211);
            ap.setApIsolated(isolation);
        }
    }

    private void restoreSettings() {
        requestWlanSettings();
    }

    private void showApplySettingsDlg() {
        applySettings();
    }

    private void applySettings() {

        // 密码标记
        boolean isPasswordMatch = true;

        // check 2.4g settings
        if (mSupportMode == C_ENUM.WlanSupportMode.Mode2Point4G.ordinal() || mSupportMode == C_ENUM.WlanSupportMode.Mode2Point4GAnd5G.ordinal()) {
            boolean isAP2GStateChanged = mWifi2GSwitch.isChecked() != mOriginSettings.getAP2G().isApEnabled();
            if (isAP2GStateChanged && !mWifi2GSwitch.isChecked()) {
                mEditedSettings.getAP2G().setApEnabled(false);
            } else {
                mEditedSettings.getAP2G().setApEnabled(true);
                String newSsid2G = mSsid2GEdit.getText().toString().trim();
                int newSecurity2GMode = mSecurity2GSpinner.getSelectedItemPosition();
                int newEncryption2G = mEncryption2GSpinner.getSelectedItemPosition();
                String newKey2G = mKey2GEdit.getText().toString().trim();

                if (newSsid2G.isEmpty()) {
                    ToastUtil_m.show(mContext, getString(R.string.qs_wifi_ssid_prompt));
                    return;
                }

                mEditedSettings.getAP2G().setSsid(newSsid2G);
                mEditedSettings.getAP2G().setSecurityMode(newSecurity2GMode);
                //disable
                if (newSecurity2GMode == 0) {
                    mEditedSettings.getAP2G().setWepKey("");
                    mEditedSettings.getAP2G().setWepType(0);
                    mEditedSettings.getAP2G().setWpaType(0);
                    mEditedSettings.getAP2G().setWpaKey("");
                    //wep
                } else if (newSecurity2GMode == 1) {
                    if (!WepPsdHelper.psdMatch(newKey2G)) {
                        isPasswordMatch = false;
                        ToastUtil_m.showLong(mContext, getString(R.string.setting_wep_password_error_prompt));
                        return;
                    }
                    mEditedSettings.getAP2G().setWepType(newEncryption2G);
                    mEditedSettings.getAP2G().setWepKey(newKey2G);
                    mEditedSettings.getAP2G().setWpaType(0);
                    mEditedSettings.getAP2G().setWpaKey("");
                    //wpa
                } else {
                    if (newKey2G.length() < 8 || newKey2G.length() > 63 || !WpaPsdHelper.isMatch(newKey2G)) {
                        isPasswordMatch = false;
                        ToastUtil_m.show(mContext, R.string.setting_wpa_password_error_prompt);
                        return;
                    }
                    mEditedSettings.getAP2G().setWepType(0);
                    mEditedSettings.getAP2G().setWepKey("");
                    mEditedSettings.getAP2G().setWpaType(newEncryption2G);
                    mEditedSettings.getAP2G().setWpaKey(newKey2G);
                }
            }
        }

        // check 5g settings
        if (mSupportMode == C_ENUM.WlanSupportMode.Mode5G.ordinal() || mSupportMode == C_ENUM.WlanSupportMode.Mode2Point4GAnd5G.ordinal()) {
            boolean isAP5GStateChanged = mWifi5GSwitch.isChecked() != mOriginSettings.getAP5G().isApEnabled();
            if (isAP5GStateChanged && !mWifi5GSwitch.isChecked()) {
                mEditedSettings.getAP5G().setApEnabled(false);
            } else {
                mEditedSettings.getAP5G().setApEnabled(true);
                String newSsid5G = mSsid5GEdit.getText().toString().trim();
                int newSecurity5GMode = mSecurity5GSpinner.getSelectedItemPosition();
                int newEncryption5G = mEncryption5GSpinner.getSelectedItemPosition();
                String newKey5G = mKey5GEdit.getText().toString().trim();

                if (newSsid5G.isEmpty()) {
                    ToastUtil_m.show(mContext, getString(R.string.qs_wifi_ssid_prompt));
                    return;
                }

                mEditedSettings.getAP5G().setSsid(newSsid5G);
                mEditedSettings.getAP5G().setSecurityMode(newSecurity5GMode);
                //disable
                if (newSecurity5GMode == 0) {
                    mEditedSettings.getAP5G().setWepKey("");
                    mEditedSettings.getAP5G().setWepType(0);
                    mEditedSettings.getAP5G().setWpaType(0);
                    mEditedSettings.getAP5G().setWpaKey("");
                    //wep
                } else if (newSecurity5GMode == 1) {
                    if (!WepPsdHelper.psdMatch(newKey5G)) {
                        isPasswordMatch = false;
                        ToastUtil_m.showLong(mContext, getString(R.string.setting_wep_password_error_prompt));
                        return;
                    }
                    mEditedSettings.getAP5G().setWepType(newEncryption5G);
                    mEditedSettings.getAP5G().setWepKey(newKey5G);
                    mEditedSettings.getAP5G().setWpaType(0);
                    mEditedSettings.getAP5G().setWpaKey("");
                    //wpa
                } else {
                    if (newKey5G.length() < 8 || newKey5G.length() > 63 || !WpaPsdHelper.isMatch(newKey5G)) {
                        isPasswordMatch = false;
                        ToastUtil_m.show(mContext, getString(R.string.setting_wpa_password_error_prompt));
                        return;
                    }
                    mEditedSettings.getAP5G().setWepType(0);
                    mEditedSettings.getAP5G().setWepKey("");
                    mEditedSettings.getAP5G().setWpaType(newEncryption5G);
                    mEditedSettings.getAP5G().setWpaKey(newKey5G);
                }
            }
        }

        if (isPasswordMatch) {
            String des1 = getString(R.string.setting_wifi_set_success);
            String des2 = getString(R.string.connectedlist_will_be_restarted_to_apply_new_settings);
            String des = des1 + "\n" + des2;
            // TODO: 2018/10/15 0015  
            dgWifiSettingrxOk.setVisibility(View.VISIBLE);
            dgWifiSettingrxOk.setTitle(R.string.restart);
            dgWifiSettingrxOk.setDes(des);
            dgWifiSettingrxOk.setOnBgClickListener(() -> Lgg.t("mainrx").ii("click not area"));
            dgWifiSettingrxOk.setOnCancelClickListener(() -> dgWifiSettingrxOk.setVisibility(View.GONE));
            dgWifiSettingrxOk.setOnOkClickListener(this::setWlanRequest);
        }
    }


    /**
     * 真正发送请求
     */
    private void setWlanRequest() {
        RX.getInstant().setWlanSettings(mEditedSettings, new ResponseObject() {
            @Override
            public void onStart() {
                super.onStart();
                rlWait.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onSuccess(Object result) {
                Log.d("ma_wififragment", "wififragment success");
                // checkLoginState();
                new Handler().postDelayed(() -> {
                    // TOAT: 2018/1/4 0004 此处等待测试验证在做决定是否执行以下逻辑
                    rlWait.setVisibility(View.GONE);
                }, 30 * 1000);
            }

            @Override
            public void onError(Throwable e) {
                rlWait.setVisibility(View.GONE);
                CA.toActivity(getActivity(), RefreshWifiActivity.class, false, true, false, 0);
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                rlWait.setVisibility(View.GONE);
                CA.toActivity(getActivity(), RefreshWifiActivity.class, false, true, false, 0);
            }

            @Override
            protected void onFailure() {
                rlWait.setVisibility(View.GONE);
            }
        });
    }

    /* 列表选项 */
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (adapterView.getId()) {
            case R.id.spinner_security_2g:
                if (i == 0) {
                    mKey2GGroup.setVisibility(View.GONE);
                    mEncryption2GGroup.setVisibility(View.GONE);
                    mEncryption2GSpinner.setSelection(-1);
                } else {
                    mKey2GGroup.setVisibility(View.VISIBLE);
                    mEncryption2GGroup.setVisibility(View.VISIBLE);
                    if (i == 1) {
                        mEncryption2GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
                        int wepType = mOriginSettings.getAP2G().getWepType();
                        mEncryption2GSpinner.setSelection(wepType);

                    } else {
                        mEncryption2GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
                        int wpaType = mOriginSettings.getAP2G().getWpaType();
                        System.out.println("2.4G wpaType: " + wpaType);
                        mEncryption2GSpinner.setSelection(wpaType);
                    }
                }
                break;
            case R.id.spinner_security_5g:
                if (i == 0) {
                    mKey5GGroup.setVisibility(View.GONE);
                    mEncryption5GGroup.setVisibility(View.GONE);
                    mEncryption5GSpinner.setSelection(-1);
                } else {
                    mKey5GGroup.setVisibility(View.VISIBLE);
                    mEncryption5GGroup.setVisibility(View.VISIBLE);
                    if (i == 1) {
                        mEncryption5GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
                        int wepType = mOriginSettings.getAP5G().getWepType();
                        mEncryption5GSpinner.setSelection(wepType);
                    } else {
                        mEncryption5GSpinner.setAdapter(new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
                        int wpaType = mOriginSettings.getAP5G().getWpaType();
                        System.out.println("5G wpaType: " + wpaType);
                        mEncryption5GSpinner.setSelection(wpaType);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /* -------------------------------------------- helper -------------------------------------------- */
    private void error() {
        popDismiss();
        ToastUtil_m.show(mContext, getString(R.string.success));
        CA.toActivity(getActivity(), RefreshWifiActivity.class, false, true, false, 0);
    }

    private void popDismiss() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    /**
     * 隐藏或者显示
     *
     * @param hideViews
     * @param showViews
     */
    private void hideOrShow(View[] hideViews, View[] showViews) {
        for (View hideView : hideViews) {
            hideView.setVisibility(View.GONE);
        }
        for (View showView : showViews) {
            showView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onBackPressed() {
        if (dgWifiSettingrxOk.getVisibility() == View.VISIBLE) {
            dgWifiSettingrxOk.setVisibility(View.GONE);
            return true;
        }
        return false;
    }
}
