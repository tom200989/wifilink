package com.alcatel.wifilink.ue.frag;

import android.os.Handler;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.bean.WlanBean;
import com.alcatel.wifilink.helper.ClickDoubleHelper;
import com.alcatel.wifilink.helper.WepPsdHelper;
import com.alcatel.wifilink.helper.WpaPsdHelper;
import com.alcatel.wifilink.ue.activity.SplashActivity;
import com.alcatel.wifilink.utils.RootUtils;
import com.alcatel.wifilink.widget.HH70_NormalWidget;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWlanSupportModeBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWlanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWlanSupportModeHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWlanSettingsHelper;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */

public class WifiFrag extends BaseFrag {

    @BindView(R.id.ll_settings_2g)
    ViewGroup m2GSettingsGroup;
    @BindView(R.id.ll_settings_5g)
    ViewGroup m5GSettingsGroup;
    @BindView(R.id.switch_wifi_2g)
    SwitchCompat mWifi2GSwitch;// 2.4G wifi开关
    @BindView(R.id.text_advanced_settings_2g)
    TextView m2GAdvancedText;
    @BindView(R.id.switch_wifi_5g)
    SwitchCompat mWifi5GSwitch;// 5G wifi开关
    @BindView(R.id.text_advanced_settings_5g)
    TextView m5GAdvancedText;
    @BindView(R.id.ll_ssid_2g)
    LinearLayout mllSsid2G;
    @BindView(R.id.rl_security_2g)
    RelativeLayout mrlSecurity2G;
    @BindView(R.id.ll_ssid_5g)
    LinearLayout mllSsid5G;
    @BindView(R.id.rl_security_5g)
    RelativeLayout mrlSecurity5G;
    @BindView(R.id.rl_encryption_2g)
    ViewGroup mEncryption2GGroup;
    @BindView(R.id.rl_encryption_5g)
    ViewGroup mEncryption5GGroup;
    @BindView(R.id.ll_key_2g)
    ViewGroup mKey2GGroup;
    @BindView(R.id.ll_key_5g)
    ViewGroup mKey5GGroup;
    @BindView(R.id.spinner_security_2g)
    AppCompatSpinner mSecurity2GSpinner;
    @BindView(R.id.spinner_security_5g)
    AppCompatSpinner mSecurity5GSpinner;
    @BindView(R.id.spinner_encryption_2g)
    AppCompatSpinner mEncryption2GSpinner;
    @BindView(R.id.spinner_encryption_5g)
    AppCompatSpinner mEncryption5GSpinner;
    @BindView(R.id.edit_ssid_2g)
    EditText mSsid2GEdit;
    @BindView(R.id.edit_ssid_5g)
    EditText mSsid5GEdit;
    @BindView(R.id.edit_key_2g)
    EditText mKey2GEdit;
    @BindView(R.id.edit_key_5g)
    EditText mKey5GEdit;
    @BindView(R.id.rl_wifiSettingrx_wait)
    RelativeLayout rlWait;
    @BindView(R.id.dg_wifiSettingrx_ok)
    HH70_NormalWidget wdNormal;
    @BindView(R.id.btn_apply)
    Button btnApply;
    @BindView(R.id.btn_cancel)
    Button btnCancel;
    @BindView(R.id.divider)
    View mDividerView;

    private View[] views_2p4;// 2G 视图
    private View[] views_5G;// 5G 视图

    private GetWlanSettingsBean mOriginSettings;
    private GetWlanSettingsBean mEditedSettings;

    private int mSupportMode;
    private String[] mWpaEncryptionSettings;
    private String[] mWepEncryptionSettings;

    private String deviceName = "hh70";// 默认HH70
    private int wlanState_2g;// 2G是否开启
    private int WLAN_2G_ON = 1;// 2G是否开启
    private ClickDoubleHelper clickDouble;
    private boolean isHH42;// 是否为HH42(外包)产品

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_wifi;
    }

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initRes();
        getSystemInfo();
        //获取各类初始化信息(devices name, wifi on or off)
        getKindState();
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        // 2.4G kind of panel
        views_2p4 = new View[]{mllSsid2G, mrlSecurity2G, mEncryption2GGroup, mKey2GGroup, m2GAdvancedText};
        views_5G = new View[]{mllSsid5G, mrlSecurity5G, mEncryption5GGroup, mKey5GGroup, m5GAdvancedText};
        mWpaEncryptionSettings = activity.getResources().getStringArray(R.array.wlan_settings_wpa_type);
        mWepEncryptionSettings = activity.getResources().getStringArray(R.array.wlan_settings_wep_type);
    }

    /**
     * 获取各类初始化信息(devices name, wifi on or off)
     */
    private void getKindState() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(systeminfo -> {
            // 1.得到设备名
            deviceName = systeminfo.getDeviceName().toLowerCase();
            isHH42 = RootUtils.isHH42(deviceName);
            // 2.获取systemstatus--> 得到目前正在启用的WIFI
            GetSystemStatusHelper xGetSystemStatusHelper = new GetSystemStatusHelper();
            xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(getSystemStatusBean -> {
                wlanState_2g = getSystemStatusBean.getWlanState_2g();
                requestWlanSupportMode();
            });
            xGetSystemStatusHelper.setOnGetSystemStatusFailedListener(this::requestWlanSupportMode);
            xGetSystemStatusHelper.getSystemStatus();
        });
        xGetSystemInfoHelper.setOnFwErrorListener(this::requestWlanSupportMode);
        xGetSystemInfoHelper.setOnAppErrorListener(this::requestWlanSupportMode);
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 获取系统信息
     */
    public void getSystemInfo() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(attr -> {
            deviceName = attr.getDeviceName().toLowerCase();
            isHH42 = RootUtils.isHH42(deviceName);
            initUi();
        });
        xGetSystemInfoHelper.setOnFwErrorListener(this::initUi);
        xGetSystemInfoHelper.setOnAppErrorListener(this::initUi);
        xGetSystemInfoHelper.getSystemInfo();
    }

    /**
     * 初始化点击事件
     */
    private void initClick() {
        // other
        rlWait.setOnClickListener(v -> toast(R.string.hh70_connecting, 2000));

        m2GAdvancedText.setOnClickListener(v -> {
            if (mEditedSettings != null) {
                WlanBean wlanBean = new WlanBean();
                wlanBean.setmSsidBroadcast(mEditedSettings.getAP2G().getSsidHidden() == 0);
                wlanBean.setmChannel(mEditedSettings.getAP2G().getChannel());
                wlanBean.setmCountry(mEditedSettings.getAP2G().getCountryCode());
                wlanBean.setmBandwidth(mEditedSettings.getAP2G().getBandwidth());
                wlanBean.setmMode(mEditedSettings.getAP2G().getWMode());
                wlanBean.setmApIsolation(mEditedSettings.getAP2G().getApIsolation() == 1);
                wlanBean.setmFrequency(2);
                // 前往高级设置界面
                toFrag(getClass(), WlanFrag.class, wlanBean, true);
            }

        });

        m5GAdvancedText.setOnClickListener(v -> {
            if (mEditedSettings != null) {
                WlanBean wlanBean = new WlanBean();
                wlanBean.setmSsidBroadcast(mEditedSettings.getAP5G().getSsidHidden() == 0);
                wlanBean.setmChannel(mEditedSettings.getAP5G().getChannel());
                wlanBean.setmCountry(mEditedSettings.getAP5G().getCountryCode());
                wlanBean.setmBandwidth(mEditedSettings.getAP5G().getBandwidth());
                wlanBean.setmMode(mEditedSettings.getAP5G().getWMode());
                wlanBean.setmApIsolation(mEditedSettings.getAP5G().getApIsolation() == 1);
                wlanBean.setmFrequency(5);
                toFrag(getClass(), WlanFrag.class, wlanBean, true);
            }

        });

        btnApply.setOnClickListener(v -> showApplySettingsDlg());// 检查密码规则
        btnCancel.setOnClickListener(v -> restoreSettings());

        mSecurity2GSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mKey2GGroup.setVisibility(View.GONE);
                    mEncryption2GGroup.setVisibility(View.GONE);
                    mEncryption2GSpinner.setSelection(-1);
                } else {
                    mKey2GGroup.setVisibility(View.VISIBLE);
                    mEncryption2GGroup.setVisibility(View.VISIBLE);
                    // TOAT: 2020/5/28  
                    if (position == 1 & !isHH42) {// 如果用户点击的是第二个条目并且不是HH42(外包)的产品(因为HH42没有WEP选项) - 则按照HH70的WEP规则处理
                        mEncryption2GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
                        int wepType = mOriginSettings.getAP2G().getWepType();
                        mEncryption2GSpinner.setSelection(wepType);

                    } else {
                        mEncryption2GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
                        int wpaType = mOriginSettings.getAP2G().getWpaType();
                        mEncryption2GSpinner.setSelection(wpaType);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSecurity5GSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mKey5GGroup.setVisibility(View.GONE);
                    mEncryption5GGroup.setVisibility(View.GONE);
                    mEncryption5GSpinner.setSelection(-1);
                } else {
                    mKey5GGroup.setVisibility(View.VISIBLE);
                    mEncryption5GGroup.setVisibility(View.VISIBLE);
                    // TOAT: 2020/5/28  
                    if (position == 1 & !isHH42) {// 如果用户点击的是第二个条目并且不是HH42(外包)的产品(因为HH42没有WEP选项) - 则按照HH70的WEP规则处理
                        mEncryption5GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
                        int wepType = mOriginSettings.getAP5G().getWepType();

                        mEncryption5GSpinner.setSelection(wepType);
                    } else {
                        mEncryption5GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
                        int wpaType = mOriginSettings.getAP5G().getWpaType();
                        mEncryption5GSpinner.setSelection(wpaType);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * 网络请求后再初始化相关UI
     */
    private void initUi() {
        // 隐藏WEP选项
        List<String> data_ls = Arrays.asList(activity.getResources().getStringArray(isHH42 ? R.array.wlan_settings_security_hh42 : R.array.wlan_settings_security));
        mSecurity2GSpinner.setAdapter(new ArrayAdapter<>(activity, R.layout.hh70_widget_spinner_item, data_ls));
        mSecurity5GSpinner.setAdapter(new ArrayAdapter<>(activity, R.layout.hh70_widget_spinner_item, data_ls));
        // 是否为MW系列产品
        boolean isMWDev = RootUtils.isMWDEV(deviceName);

        initClick();
        mWifi2GSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isMWDev) {
                hideOrShow(isChecked ? views_5G : views_2p4, isChecked ? views_2p4 : views_5G);
                mWifi5GSwitch.setChecked(!isChecked);
            }
        });
        mWifi5GSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isMWDev) {
                hideOrShow(isChecked ? views_2p4 : views_5G, isChecked ? views_5G : views_2p4);
                mWifi2GSwitch.setChecked(!isChecked);
            }
        });

    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        if (o instanceof WlanBean) {
            WlanBean wlanBean = (WlanBean) o;
            boolean broadcast = wlanBean.ismSsidBroadcast();
            int channel = wlanBean.getmChannel();
            String countryCode = wlanBean.getmCountry();
            int bandwidth = wlanBean.getmBandwidth();
            int mode80211 = wlanBean.getmMode();
            boolean isolation = wlanBean.ismApIsolation();
            if (wlanBean.getmFrequency() == 2) {//如果是2G
                GetWlanSettingsBean.AP2GBean ap2G = mEditedSettings.getAP2G();
                ap2G.setSsidHidden(broadcast ? 0 : 1);
                ap2G.setChannel(channel);
                ap2G.setCountryCode(countryCode);
                ap2G.setBandwidth(bandwidth);
                ap2G.setWMode(mode80211);
                ap2G.setApIsolation(isolation ? 1 : 0);
                mEditedSettings.setAP2G(ap2G);
            } else { //5G
                GetWlanSettingsBean.AP5GBean ap5G = mEditedSettings.getAP5G();
                ap5G.setSsidHidden(broadcast ? 0 : 1);
                ap5G.setChannel(channel);
                ap5G.setCountryCode(countryCode);
                ap5G.setBandwidth(bandwidth);
                ap5G.setWMode(mode80211);
                ap5G.setApIsolation(isolation ? 1 : 0);
                mEditedSettings.setAP5G(ap5G);
            }
        }
    }

    private void requestWlanSettings() {
        GetWlanSettingsHelper xGetWlanSettingsHelper = new GetWlanSettingsHelper();
        xGetWlanSettingsHelper.setOnGetWlanSettingsSuccessListener(settingsBean -> {
            mOriginSettings = settingsBean;
            mEditedSettings = mOriginSettings.deepClone();
            updateUIWithWlanSettings();
        });
        xGetWlanSettingsHelper.getWlanSettings();
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
        mWifi5GSwitch.setChecked(mOriginSettings.getAP5G().getApStatus() == 1);
        mSsid5GEdit.setText(mOriginSettings.getAP5G().getSsid());
        int securityMode = mOriginSettings.getAP5G().getSecurityMode();
        // TOAT: 根据HH42(外包)设备做兼容
        securityMode = isHH42 ? securityMode == 0 ? 0 : securityMode - 1 : securityMode;
        mSecurity5GSpinner.setSelection(securityMode);
        if (mOriginSettings.getAP5G().getSecurityMode() == GetWlanSettingsBean.CONS_SECURITY_MODE_DISABLE) {
            mEncryption5GGroup.setVisibility(View.GONE);
            mKey5GGroup.setVisibility(View.GONE);
            mEncryption5GSpinner.setSelection(-1);
            mKey5GEdit.setText("");
        } else if (mOriginSettings.getAP5G().getSecurityMode() == GetWlanSettingsBean.CONS_SECURITY_MODE_WEP) {
            mEncryption5GGroup.setVisibility(View.VISIBLE);
            mKey5GGroup.setVisibility(View.VISIBLE);
            mEncryption5GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
            mEncryption5GSpinner.setSelection(mOriginSettings.getAP5G().getWepType());
            mKey5GEdit.setText(mOriginSettings.getAP5G().getWepKey());
        } else {
            mEncryption5GGroup.setVisibility(View.VISIBLE);
            mKey5GGroup.setVisibility(View.VISIBLE);
            mEncryption5GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
            mEncryption5GSpinner.setSelection(mOriginSettings.getAP5G().getWpaType());
            mKey5GEdit.setText(mOriginSettings.getAP5G().getWpaKey());
        }
    }

    /* 2G */
    private void set2GData() {
        // 先设置
        mWifi2GSwitch.setChecked(mOriginSettings.getAP2G().getApStatus() == 1);
        mSsid2GEdit.setText(mOriginSettings.getAP2G().getSsid());
        int securityMode = mOriginSettings.getAP2G().getSecurityMode();
        // TOAT: 根据HH42(外包)设备做兼容
        securityMode = isHH42 ? securityMode == 0 ? 0 : securityMode - 1 : securityMode;
        mSecurity2GSpinner.setSelection(securityMode);
        if (securityMode == GetWlanSettingsBean.CONS_SECURITY_MODE_DISABLE) {
            mEncryption2GGroup.setVisibility(View.GONE);
            mKey2GGroup.setVisibility(View.GONE);
            mEncryption2GSpinner.setSelection(-1);
            mKey2GEdit.setText("");
        } else if (securityMode == GetWlanSettingsBean.CONS_SECURITY_MODE_WEP) {
            mEncryption2GGroup.setVisibility(View.VISIBLE);
            mKey2GGroup.setVisibility(View.VISIBLE);
            mEncryption2GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWepEncryptionSettings));
            mEncryption2GSpinner.setSelection(mOriginSettings.getAP2G().getWepType());
            mKey2GEdit.setText(mOriginSettings.getAP2G().getWepKey());
        } else {
            mEncryption2GGroup.setVisibility(View.VISIBLE);
            mKey2GGroup.setVisibility(View.VISIBLE);
            mEncryption2GSpinner.setAdapter(new ArrayAdapter<>(activity, android.R.layout.simple_spinner_dropdown_item, mWpaEncryptionSettings));
            mEncryption2GSpinner.setSelection(mOriginSettings.getAP2G().getWpaType());
            mKey2GEdit.setText(mOriginSettings.getAP2G().getWpaKey());
        }
    }

    private void updateUIWithSupportMode(int supportMode) {

        mSupportMode = supportMode;
        if (supportMode == GetWlanSupportModeBean.CONS_WLAN_2_4G) {
            m2GSettingsGroup.setVisibility(View.VISIBLE);
            m5GSettingsGroup.setVisibility(View.GONE);
            mDividerView.setVisibility(View.GONE);
        } else if (supportMode == GetWlanSupportModeBean.CONS_WLAN_5G) {
            m2GSettingsGroup.setVisibility(View.GONE);
            m5GSettingsGroup.setVisibility(View.VISIBLE);
            mDividerView.setVisibility(View.GONE);
        } else if (supportMode == GetWlanSupportModeBean.CONS_WLAN_2_4G_5G) {
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
        boolean isMW120 = RootUtils.isMWDEV(deviceName);
        if (isMW120) {
            // 隐藏相关布局
            if (wlanState_2g == WLAN_2G_ON) {
                hideOrShow(views_5G, views_2p4);
            } else {
                hideOrShow(views_2p4, views_5G);
            }
        }
    }

    private void restoreSettings() {
        requestWlanSettings();
    }

    private void showApplySettingsDlg() {
        applySettings();
    }

    private void applySettings() {

        // check 2.4g settings
        if (mSupportMode == GetWlanSupportModeBean.CONS_WLAN_2_4G || mSupportMode == GetWlanSupportModeBean.CONS_WLAN_2_4G_5G) {
            boolean isAP2GStateChanged = mWifi2GSwitch.isChecked() != (mOriginSettings.getAP2G().getApStatus() == 1);
            //add by haide.yin 只要点击了Apply都会更新 isAP2GStateChanged
            if (!mWifi2GSwitch.isChecked()) {
                mEditedSettings.getAP2G().setApStatus(0);
            } else {
                mEditedSettings.getAP2G().setApStatus(1);
                String newSsid2G = RootUtils.getEDText(mSsid2GEdit, true);
                // TOAT: 2020/5/28  
                int newSecurity2GMode = mSecurity2GSpinner.getSelectedItemPosition();
                newSecurity2GMode = isHH42 ? newSecurity2GMode == 0 ? 0 : newSecurity2GMode + 1 : newSecurity2GMode;
                System.out.println("fuck hh42 newSecurity2GMode: " + newSecurity2GMode);
                int newEncryption2G = mEncryption2GSpinner.getSelectedItemPosition();
                System.out.println("fuck hh42 newEncryption2G: " + newEncryption2G);
                String newKey2G = RootUtils.getEDText(mKey2GEdit, true);
                //不能为空
                if (newSsid2G.isEmpty() || newSsid2G.length() > 32) {
                    toast(R.string.hh70_the_ssid, 2000);
                    return;
                }
                //不能包含特殊字符
                if (RootUtils.contantSpecialChar(newSsid2G)) {
                    toast(R.string.hh70_ssid_can_contain, 2000);
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
                        toast(R.string.hh70_wep_key_is_5, 2000);
                        return;
                    }
                    mEditedSettings.getAP2G().setWepType(newEncryption2G);
                    mEditedSettings.getAP2G().setWepKey(newKey2G);
                    mEditedSettings.getAP2G().setWpaType(0);
                    mEditedSettings.getAP2G().setWpaKey("");
                    //wpa
                } else {
                    if (newKey2G.length() < 8 || newKey2G.length() > 63 || !WpaPsdHelper.isMatch(newKey2G)) {
                        toast(R.string.hh70_the_wpa_pre, 2000);
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
        if (mSupportMode == GetWlanSupportModeBean.CONS_WLAN_5G || mSupportMode == GetWlanSupportModeBean.CONS_WLAN_2_4G_5G) {
            boolean isOriginOn = mOriginSettings.getAP5G().getApStatus() == 1;// 1. 判断初始化时是否为ON, 如果为1--ON
            boolean isAP5GStateChanged = mWifi5GSwitch.isChecked() != isOriginOn;// 2. 是否与原始状态相反, 如果相反 -- 说明有变动
            //add by haide.yin 只要点击了Apply都会更新
            if (/*isAP5GStateChanged*/true) {
                if (!mWifi5GSwitch.isChecked()) {// 3. 如果有变化, 并且当前是OFF -- 便设置 WIFI 关闭
                    mEditedSettings.getAP5G().setApStatus(0);
                } else {// 4.如果没有变化, 或者WIFI被重新设置成ON -- 便去设置 WIFI 开启
                    mEditedSettings.getAP5G().setApStatus(1);
                    String newSsid5G = RootUtils.getEDText(mSsid5GEdit, true);
                    int newSecurity5GMode = mSecurity5GSpinner.getSelectedItemPosition();
                    newSecurity5GMode = isHH42 ? newSecurity5GMode == 0 ? 0 : newSecurity5GMode + 1 : newSecurity5GMode;
                    int newEncryption5G = mEncryption5GSpinner.getSelectedItemPosition();

                    String newKey5G = RootUtils.getEDText(mKey5GEdit, true);
                    //不能为空
                    if (newSsid5G.isEmpty() || newSsid5G.length() > 32) {
                        toast(R.string.hh70_the_ssid, 2000);
                        return;
                    }
                    //不能包含特殊字符
                    if (RootUtils.contantSpecialChar(newSsid5G)) {
                        toast(R.string.hh70_ssid_can_contain, 2000);
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
                            toast(R.string.hh70_wep_key_is_5, 2000);
                            return;
                        }
                        mEditedSettings.getAP5G().setWepType(newEncryption5G);
                        mEditedSettings.getAP5G().setWepKey(newKey5G);
                        mEditedSettings.getAP5G().setWpaType(0);
                        mEditedSettings.getAP5G().setWpaKey("");
                        //wpa
                    } else {
                        if (newKey5G.length() < 8 || newKey5G.length() > 63 || !WpaPsdHelper.isMatch(newKey5G)) {
                            toast(R.string.hh70_the_wpa_pre, 2000);
                            return;
                        }
                        mEditedSettings.getAP5G().setWepType(0);
                        mEditedSettings.getAP5G().setWepKey("");
                        mEditedSettings.getAP5G().setWpaType(newEncryption5G);
                        mEditedSettings.getAP5G().setWpaKey(newKey5G);
                    }
                }
            }

        }

        String des1 = getString(R.string.hh70_change_wifi);
        String des2 = getString(R.string.hh70_list_will_restart);
        String des = des1 + "\n" + des2;
        wdNormal.setVisibility(View.VISIBLE);
        wdNormal.setTitle(R.string.hh70_restart);
        wdNormal.setDes(des);
        wdNormal.setOnCancelClickListener(() -> wdNormal.setVisibility(View.GONE));
        wdNormal.setOnOkClickListener(this::setWlanRequest);
    }

    /**
     * 真正发送请求
     */
    private void setWlanRequest() {
        SetWlanSettingsHelper xSetWlanSettingsHelper = new SetWlanSettingsHelper();
        xSetWlanSettingsHelper.setOnPrepareHelperListener(() -> rlWait.setVisibility(View.VISIBLE));
        xSetWlanSettingsHelper.setOnDoneHelperListener(() -> rlWait.setVisibility(View.GONE));
        xSetWlanSettingsHelper.setOnSetWlanSettingsSuccessListener(() -> new Handler().postDelayed(() -> rlWait.setVisibility(View.GONE), 30 * 1000));
        xSetWlanSettingsHelper.setOnSetWlanSettingsFailedListener(() -> toFragActivity(getClass(), SplashActivity.class, RefreshFrag.class, null, false, true, 0));
        xSetWlanSettingsHelper.setWlanSettings(mEditedSettings);
    }

    /**
     * 隐藏或者显示
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
        if (wdNormal.getVisibility() == View.VISIBLE) {
            wdNormal.setVisibility(View.GONE);
            return true;
        } else {
            // 登出
            if (clickDouble == null) {
                clickDouble = new ClickDoubleHelper();
                clickDouble.setOnClickOneListener(() -> toast(R.string.hh70_touch_again, 3000));
                clickDouble.setOnClickDoubleListener(this::killAllActivitys);
            }
            clickDouble.click();
            return true;
        }
    }
}
