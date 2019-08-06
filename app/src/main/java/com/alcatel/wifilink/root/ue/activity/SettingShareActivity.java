package com.alcatel.wifilink.root.ue.activity;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.bean.Sharing_DLNASettings;
import com.alcatel.wifilink.root.bean.Sharing_FTPSettings;
import com.alcatel.wifilink.root.bean.Sharing_SambaSettings;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.utils.C_Constants;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemStatusHelper;

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
        mFTPStorageAccessImage.setOnClickListener(this);
        mSambaStorageAccessImage.setOnClickListener(this);
        mDLNAStorageAccessImage.setOnClickListener(this);
    }

    private void initData() {
        requestGetSystemStatus();
        requestGetFTPSettings();
        requestGetSambaSettings();
        requestGetDLNASettings();
    }

    private void requestGetSystemStatus() {

        // TODO: 2019/8/5 0005  新框架示例
        GetSystemStatusHelper xGetSystemStatusHelper = new GetSystemStatusHelper();
        xGetSystemStatusHelper.setOnGetSystemStatusSuccessListener(getSystemStatusBean -> {
            switch (getSystemStatusBean.getUsbStatus()) {
                case C_Constants.DeviceUSBStatus.NOT_INSERT:
                    mUSBStorageText.setText(R.string.not_inserted);
                    break;
                case C_Constants.DeviceUSBStatus.USB_STORAGE:
                    mUSBStorageText.setText(R.string.setting_usb_storage);
                    break;
                case C_Constants.DeviceUSBStatus.USB_PRINT:
                    mUSBStorageText.setText(R.string.usb_printer);
                    break;
            }
        });
        xGetSystemStatusHelper.getSystemStatus();

        // RX.getInstant().getSystemStatus(new ResponseObject<System_SysStatus>() {
        //     @Override
        //     protected void onSuccess(System_SysStatus result) {
        //         runOnUiThread(() -> {
        //             switch (result.getUsbStatus()) {
        //                 case C_Constants.DeviceUSBStatus.NOT_INSERT:
        //                     mUSBStorageText.setText(R.string.not_inserted);
        //                     break;
        //                 case C_Constants.DeviceUSBStatus.USB_STORAGE:
        //                     mUSBStorageText.setText(R.string.setting_usb_storage);
        //                     break;
        //                 case C_Constants.DeviceUSBStatus.USB_PRINT:
        //                     mUSBStorageText.setText(R.string.usb_printer);
        //                     break;
        //             }
        //         });
        //     }
        //
        //     @Override
        //     protected void onResultError(ResponseBody.Error error) {
        //         super.onResultError(error);
        //     }
        //
        //     @Override
        //     protected void onFailure() {
        //         super.onFailure();
        //     }
        // });
    }

    private void requestGetFTPSettings() {
        RX.getInstant().getFTPSettings(new ResponseObject<Sharing_FTPSettings>() {
            @Override
            protected void onSuccess(Sharing_FTPSettings result) {
                mFTPSwitch.setChecked(result.getFtpStatus() == 1);
            }

            @Override
            protected void onFailure() {
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
            }
        });
    }

    private void requestGetSambaSettings() {
        RX.getInstant().getSambaSettings(new ResponseObject<Sharing_SambaSettings>() {
            @Override
            protected void onSuccess(Sharing_SambaSettings result) {
                mSambaSwitch.setChecked(result.getSambaStatus() == 1);
            }

            @Override
            protected void onFailure() {
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
            }
        });
    }

    private void requestGetDLNASettings() {
        RX.getInstant().getDLNASettings(new ResponseObject<Sharing_DLNASettings>() {
            @Override
            protected void onSuccess(Sharing_DLNASettings result) {
                mDLNASwitch.setChecked(result.getDlnaStatus() == 1);
            }

            @Override
            protected void onFailure() {
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
            }
        });
    }

    private void requestSetFTPSettings() {
        Sharing_FTPSettings settings = new Sharing_FTPSettings();
        settings.setFtpStatus(mFTPSwitch.isChecked() ? 1 : 0);
        settings.setAnonymous(0);
        settings.setAuthType(0);
        RX.getInstant().setFTPSettings(settings, new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
            }

            @Override
            protected void onFailure() {
                super.onFailure();
            }
        });
    }

    private void requestSetSambaSettings() {
        Sharing_SambaSettings settings = new Sharing_SambaSettings();
        settings.setSambaStatus(mSambaSwitch.isChecked() ? 1 : 0);
        settings.setAnonymous(0);
        settings.setAuthType(0);
        RX.getInstant().setSambaSettings(settings, new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
            }

            @Override
            protected void onFailure() {
                super.onFailure();
            }
        });
    }

    private void requestSetDLNASettings() {
        Sharing_DLNASettings settings = new Sharing_DLNASettings();
        settings.setDlnaStatus(mDLNASwitch.isChecked() ? 1 : 0);
        settings.setDlnaName("");
        RX.getInstant().setDLNASettings(settings, new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
            }

            @Override
            protected void onFailure() {
                super.onFailure();
            }
        });
    }

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

    @Override
    protected void onPause() {
        super.onPause();
        if (timerHelper != null) {
            timerHelper.stop();
            timerHelper = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


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
