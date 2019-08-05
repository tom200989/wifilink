package com.alcatel.wifilink.root.ue.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.System_SystemInfo;
import com.alcatel.wifilink.root.bean.WlanLanSettings;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.utils.Logs;

public class AboutActivity extends BaseActivityWithBack implements View.OnClickListener {
    private final static String TAG = "AboutActivity";
    private TextView mDeviceNameTxt;
    private TextView mImeiTxt;
    private TextView mMacAddressTxt;
    private TextView mManagementIpTxt;
    private TextView mSubnetMaskTxt;
    private TextView mWebManager;
    private TextView mQuickGuide;
    private ImageView mIvPoint;
    private TextView mAppVersionTxt;
    private ProgressDialog mProgressDialog;

    private String mCustom;
    private String mProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //control title bar
        mDeviceNameTxt = (TextView) findViewById(R.id.device_name_txt);
        mImeiTxt = (TextView) findViewById(R.id.imei_txt);
        mMacAddressTxt = (TextView) findViewById(R.id.mac_address_txt);
        mManagementIpTxt = (TextView) findViewById(R.id.management_ip_txt);
        mSubnetMaskTxt = (TextView) findViewById(R.id.subnet_mask_txt);
        mWebManager = (TextView) findViewById(R.id.web_manager);
        mQuickGuide = (TextView) findViewById(R.id.quick_guide);
        mIvPoint = (ImageView) findViewById(R.id.iv_point);
        mAppVersionTxt = (TextView) findViewById(R.id.app_version_txt);
        mWebManager.setOnClickListener(this);
        mQuickGuide.setOnClickListener(this);
        setTitle(R.string.setting_about);
        getDataFromNet();
        displayVersion();
    }

    private void getDataFromNet() {
        RX.getInstant().getSystemInfo(new ResponseObject<System_SystemInfo>() {


            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            protected void onSuccess(System_SystemInfo result) {
                dismissLoadingDialog();
                String swVersion = result.getSwVersion();
                String[] split = swVersion.split("_");
                // 注：项目名只取前四位
                // 实例1：软件版本号为HH40_E4_02.00_01，则取出的项目名为HH40,定制ID为E4
                // 实例2：软件版本号为HH40V_00_02.00_11，则取出的项目名为HH40,定制ID为00
                mProject = split[0];
                if (mProject.length() > 4) {
                    mProject = mProject.substring(0, 4);
                }
                mCustom = split[1];
                mDeviceNameTxt.setText(result.getDeviceName());
                mImeiTxt.setText(result.getIMEI());
                mMacAddressTxt.setText(result.getMacAddress());
                // mAppVersionTxt.setText(result.getAppVersion());
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                dismissLoadingDialog();
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                super.onResultError(error);
                dismissLoadingDialog();
            }
        });
        RX.getInstant().getLanSettings(new ResponseObject<WlanLanSettings>() {
            @Override
            protected void onSuccess(WlanLanSettings result) {
                mManagementIpTxt.setText(result.getIPv4IPAddress().isEmpty() ? "0.0.0.0" : result.getIPv4IPAddress());
                mSubnetMaskTxt.setText(result.getSubnetMask().isEmpty() ? "0.0.0.0" : result.getSubnetMask());
            }
        });

    }

    private void showLoadingDialog() {
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.show();
    }

    private void dismissLoadingDialog() {
        mProgressDialog.dismiss();
    }

    private void displayVersion() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            ApplicationInfo appInfo = getApplicationInfo();
            boolean isDebuggable = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            mAppVersionTxt.setText("v" + info.versionName + (isDebuggable ? "(debug)" : ""));
            mIvPoint.setVisibility(View.GONE);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.web_manager:
                toWebUiSite();
                break;
            case R.id.quick_guide:
                userChrome();
                break;
            default:
                break;
        }
    }

    private void toWebUiSite() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://192.168.1.1");
        intent.setData(content_url);
        startActivity(intent);
    }

    /* **** userChrome **** */
    private void userChrome() {
        if (!TextUtils.isEmpty(mProject) && !TextUtils.isEmpty(mCustom)) {
            String lang = getResources().getConfiguration().locale.getLanguage();
            // http://www.alcatel-move.com/um/url.html?project=HH41&custom=00&lang=en
            Logs.t("ma_web").vv("mProject: " + mProject);
            Logs.t("ma_web").vv("mCustom: " + mCustom);
            String url = "http://www.alcatel-move.com/um/url.html?project=" + mProject + "&custom=" + mCustom + "&lang=" + lang;
            // url = "http://www.alcatel-move.com/um/url.html?project=HH41&custom=00&lang=en";
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri content_url = Uri.parse(url);
            String path = content_url.getPath();
            String query = content_url.getQuery();
            intent.setData(content_url);
            startActivity(intent);
        }
    }

}
