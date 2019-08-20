package com.alcatel.wifilink.root.ue.frag;

import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.hiber.cons.TimerState;
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

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class SettingShareFrag extends BaseFrag {

    @BindView(R.id.iv_setting_share_back)
    ImageView ivSettingShareBack;

    @BindView(R.id.tv_usb_storage)
    TextView mUSBStorageText;
    @BindView(R.id.switch_ftp)
    SwitchCompat mFTPSwitch;
    @BindView(R.id.switch_samba)
    SwitchCompat mSambaSwitch;
    @BindView(R.id.switch_dlna)
    SwitchCompat mDLNASwitch;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_setting_share;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
        initClick();
    }

    private void initClick() {
        // 点击FTP按钮
        mFTPSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setFTP());
        // 点击Samba按钮
        mSambaSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setSamba());
        // 点击DLNA按钮
        mDLNASwitch.setOnCheckedChangeListener((buttonView, isChecked) -> setDLNA());
    }

    private void setFTP() {
        SetFtpSettingsParam param = new SetFtpSettingsParam();
        param.setFtpStatus(mFTPSwitch.isChecked() ? 1 : 0);
        param.setAnonymous(0);
        param.setAuthType(0);
        SetFtpSettingsHelper xSetFtpSettingsHelper = new SetFtpSettingsHelper();
        xSetFtpSettingsHelper.setFtpSettings(param);
    }

    private void setSamba() {
        SetSambaSettingsParam param = new SetSambaSettingsParam();
        param.setSambaStatus(mSambaSwitch.isChecked() ? 1 : 0);
        param.setAnonymous(0);
        param.setAuthType(0);
        SetSambaSettingsHelper xSetSambaSettingsHelper = new SetSambaSettingsHelper();
        xSetSambaSettingsHelper.setSambaSettings(param);
    }

    private void setDLNA() {
        SetDLNASettingsParam param = new SetDLNASettingsParam();
        param.setDlnaStatus(mDLNASwitch.isChecked() ? 1 : 0);
        param.setDlnaName("");
        SetDLNASettingsHelper xSetDLNASettingsHelper = new SetDLNASettingsHelper();
        xSetDLNASettingsHelper.setDLNASettings(param);
    }

    @Override
    public void setTimerTask() {
        getSystemStatus();
        getFTP();
        getSamba();
        getDLNA();
    }

    private void getSystemStatus() {
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

    private void getFTP() {
        GetFtpSettingsHelper xGetFtpSettingsHelper = new GetFtpSettingsHelper();
        xGetFtpSettingsHelper.setOnGetFtpSettingsSuccessListener(result -> mFTPSwitch.setChecked(result.getFtpStatus() == GetFtpSettingsBean.CONS_FTPSTATUS_ENABLE));
        xGetFtpSettingsHelper.getFtpSettings();
    }

    private void getSamba() {
        GetSambaSettingsHelper xGetSambaSettingsHelper = new GetSambaSettingsHelper();
        xGetSambaSettingsHelper.setOnGetSambaSettingsSuccessListener(result -> mSambaSwitch.setChecked(result.getSambaStatus() == GetSambaSettingsBean.CONS_SAMBASTATUS_DISABLE));
        xGetSambaSettingsHelper.getSambaSettings();
    }

    private void getDLNA() {
        GetDLNASettingsHelper xGetDLNASettingsHelper = new GetDLNASettingsHelper();
        xGetDLNASettingsHelper.setOnGetDLNASettingsSuccessListener(result -> mDLNASwitch.setChecked(result.getDlnaStatus() == GetDLNASettingsBean.CONS_ENABLE));
        xGetDLNASettingsHelper.getDLNASettings();
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), SettingFrag.class, null, false);
        return true;
    }
}
