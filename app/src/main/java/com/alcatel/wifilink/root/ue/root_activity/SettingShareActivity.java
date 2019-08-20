package com.alcatel.wifilink.root.ue.root_activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetDLNASettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetFtpSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSambaSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSystemStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetDLNASettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetFtpSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetSambaSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetDLNASettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetFtpSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSambaSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDLNASettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetFtpSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetSambaSettingsHelper;

// TOGO 2019/8/20 0020 settingsharefrag
@Deprecated
public class SettingShareActivity extends BaseActivityWithBack implements OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = "SettingShareActivity";
    private TextView mUSBStorageText;
    private SwitchCompat mFTPSwitch;
    private SwitchCompat mSambaSwitch;
    private SwitchCompat mDLNASwitch;
    private ImageView mFTPStorageAccessImage;
    private ImageView mSambaStorageAccessImage;
    private ImageView mDLNAStorageAccessImage;
    private TimerHelper timerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_share);
        initView();
        initData();
    }

    private void initView() {
        // TOGO 2019/8/20 0020 
        mUSBStorageText = (TextView) findViewById(R.id.tv_usb_storage);
        mFTPSwitch = (SwitchCompat) findViewById(R.id.switch_ftp);
        mSambaSwitch = (SwitchCompat) findViewById(R.id.switch_samba);
        mDLNASwitch = (SwitchCompat) findViewById(R.id.switch_dlna);
        mFTPStorageAccessImage = (ImageView) findViewById(R.id.iv_ftp_access_storage);
        mSambaStorageAccessImage = (ImageView) findViewById(R.id.iv_samba_access_storage);
        mDLNAStorageAccessImage = (ImageView) findViewById(R.id.iv_dlna_access_storage);
        mFTPSwitch.setOnCheckedChangeListener(this);
        mSambaSwitch.setOnCheckedChangeListener(this);
        mDLNASwitch.setOnCheckedChangeListener(this);

        // TOGO 2019/8/20 0020 
        mFTPStorageAccessImage.setOnClickListener(this);
        mSambaStorageAccessImage.setOnClickListener(this);
        mDLNAStorageAccessImage.setOnClickListener(this);
    }

    // TOGO 2019/8/20 0020 
    private void initData() {
        requestGetSystemStatus();
        requestGetFTPSettings();
        requestGetSambaSettings();
        requestGetDLNASettings();
    }

    // TOGO 2019/8/20 0020 
    private void requestGetSystemStatus() {

        GetSystemStatusHelper xGetSystemStatusHelper = new GetSystemStatusHelper();
        xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(getSystemStatusBean -> {
            switch (getSystemStatusBean.getUsbStatus()) {
                case GetSystemStatusBean.CONS_NOT_INSERT:
                    mUSBStorageText.setText(R.string.not_inserted);
                    break;
                case GetSystemStatusBean.CONS_USB_STORAGE:
                    mUSBStorageText.setText(R.string.setting_usb_storage);
                    break;
                case GetSystemStatusBean.CONS_USB_PRINT:
                    mUSBStorageText.setText(R.string.usb_printer);
                    break;
            }
        });
        xGetSystemStatusHelper.getSystemStatus();
    }

    // TOGO 2019/8/20 0020 
    private void requestGetFTPSettings() {
        GetFtpSettingsHelper xGetFtpSettingsHelper = new GetFtpSettingsHelper();
        xGetFtpSettingsHelper.setOnGetFtpSettingsSuccessListener(result -> mFTPSwitch.setChecked(result.getFtpStatus() == GetFtpSettingsBean.CONS_FTPSTATUS_ENABLE));
        xGetFtpSettingsHelper.getFtpSettings();
    }

    // TOGO 2019/8/20 0020 
    private void requestGetSambaSettings() {
        GetSambaSettingsHelper xGetSambaSettingsHelper = new GetSambaSettingsHelper();
        xGetSambaSettingsHelper.setOnGetSambaSettingsSuccessListener(result -> mSambaSwitch.setChecked(result.getSambaStatus() == GetSambaSettingsBean.CONS_SAMBASTATUS_DISABLE));
        xGetSambaSettingsHelper.getSambaSettings();
    }

    // TOGO 2019/8/20 0020 
    private void requestGetDLNASettings() {
        GetDLNASettingsHelper xGetDLNASettingsHelper = new GetDLNASettingsHelper();
        xGetDLNASettingsHelper.setOnGetDLNASettingsSuccessListener(result -> mDLNASwitch.setChecked(result.getDlnaStatus() == GetDLNASettingsBean.CONS_ENABLE));
        xGetDLNASettingsHelper.getDLNASettings();
    }

    private void requestSetFTPSettings() {
        SetFtpSettingsParam param = new SetFtpSettingsParam();
        param.setFtpStatus(mFTPSwitch.isChecked() ? 1 : 0);
        param.setAnonymous(0);
        param.setAuthType(0);
        SetFtpSettingsHelper xSetFtpSettingsHelper = new SetFtpSettingsHelper();
        xSetFtpSettingsHelper.setFtpSettings(param);
    }

    private void requestSetSambaSettings() {
        SetSambaSettingsParam param = new SetSambaSettingsParam();
        param.setSambaStatus(mSambaSwitch.isChecked() ? 1 : 0);
        param.setAnonymous(0);
        param.setAuthType(0);
        SetSambaSettingsHelper xSetSambaSettingsHelper = new SetSambaSettingsHelper();
        xSetSambaSettingsHelper.setSambaSettings(param);
    }

    private void requestSetDLNASettings() {
        SetDLNASettingsParam param = new SetDLNASettingsParam();
        param.setDlnaStatus(mDLNASwitch.isChecked() ? 1 : 0);
        param.setDlnaName("");
        SetDLNASettingsHelper xSetDLNASettingsHelper = new SetDLNASettingsHelper();
        xSetDLNASettingsHelper.setDLNASettings(param);
    }

    // TOGO 2019/8/20 0020 
    @Override
    protected void onResume() {
        super.onResume();
        if (timerHelper == null) {

            timerHelper = new TimerHelper(SettingShareActivity.this) {
                @Override
                public void doSomething() {
                    requestGetSystemStatus();
                }
            };
            timerHelper.start(10 * 1000);
        }
    }

    // TOGO 2019/8/20 0020 
    @Override
    protected void onPause() {
        super.onPause();
        if (timerHelper != null) {
            timerHelper.stop();
            timerHelper = null;
        }
    }

    // TOGO 2019/8/20 0020 
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    // TOGO 2019/8/20 0020 
    @Override
    public void onClick(View v) {
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.switch_ftp:
                requestSetFTPSettings();
                break;
            case R.id.switch_samba:
                requestSetSambaSettings();
                break;
            case R.id.switch_dlna:
                requestSetDLNASettings();
                break;
        }
    }
}
