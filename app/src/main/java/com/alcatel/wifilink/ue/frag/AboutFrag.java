package com.alcatel.wifilink.ue.frag;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.RootCons;
import com.alcatel.wifilink.utils.RootUtils;
import com.alcatel.wifilink.widget.HH70_LoadWidget;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.utils.Logg;
import com.p_xhelper_smart.p_xhelper_smart.utils.SmartUtils;

import butterknife.BindView;


/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class AboutFrag extends BaseFrag {

    // 回退
    @BindView(R.id.iv_about_back)
    ImageView ivAboutBack;
    @BindView(R.id.device_name_txt)
    TextView mDeviceNameTxt;
    @BindView(R.id.imei_txt)
    TextView mImeiTxt;
    @BindView(R.id.mac_address_txt)
    TextView mMacAddressTxt;
    @BindView(R.id.management_ip_txt)
    TextView mManagementIpTxt;
    @BindView(R.id.subnet_mask_txt)
    TextView mSubnetMaskTxt;
    @BindView(R.id.web_manager)
    TextView mWebManager;
    @BindView(R.id.quick_guide)
    TextView mQuickGuide;
    @BindView(R.id.iv_point)
    ImageView mIvPoint;
    @BindView(R.id.app_version_txt)
    TextView mAppVersionTxt;

    @BindView(R.id.wd_about_load)
    HH70_LoadWidget wdLoad;

    private String mCustom;
    private String mProject;
    private String mVersion;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_about;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        getSystemInfoData();
        displayVersion();
        initClick();
    }

    /**
     * 初始化点击
     */
    private void initClick() {
        // 回退
        ivAboutBack.setOnClickListener(v -> onBackPresss());
        // 前往网页
        mWebManager.setOnClickListener(v -> toWebUiSite());
        // 前往用户手册
        mQuickGuide.setOnClickListener(v -> userChrome());
    }

    /**
     * 跳转到WEB UI
     */
    private void toWebUiSite() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse("http://" + SmartUtils.getWIFIGateWay(activity));
        intent.setData(content_url);
        startActivity(intent);
    }

    /**
     * 开启浏览器
     */
    private void userChrome() {
        if (!TextUtils.isEmpty(mProject) && !TextUtils.isEmpty(mCustom)) {
            String lang = getResources().getConfiguration().locale.getLanguage();
            // 根据HH42(外包) 做语言适配
            lang = lang.equalsIgnoreCase("es") ? "sp_latam" : lang;
            String url = "http://www.alcatel-move.com/um/url.html?project=HH41&custom=00&lang=en";
            // 判断是否为HH42的产品
            boolean isHH42 = RootUtils.isHH42(ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT));
            if (!isHH42) {// TCL自营项目
                // 格式: http://www.alcatel-move.com/um/url.html?project=HH41&custom=00&lang=en
                url = "http://www.alcatel-move.com/um/url.html?project=" + mProject + "&custom=" + mCustom + "&lang=" + lang;

            } else {// HH42(外包)项目
                // 格式: https://www.alcatel-move.com/um/url.html?project=HH42NK&custom=open_market&version=HH42NK_open_market&lang=en
                url = "https://www.alcatel-move.com/um/url.html?project=" + mProject + "&custom=" + mCustom + "&version=" + mVersion + "&lang" + "=" + lang;
            }
            Intent intent = new Intent();
            intent.setAction("android.intent.action.VIEW");
            Uri uri = Uri.parse(url);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    /**
     * 获取系统信息并展示
     */
    private void getSystemInfoData() {
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            Logg.t("HH42_ALAN_SYS").ii(result.toString());
            // 1.判断是否为HH42(外包)产品
            String devname = ShareUtils.get(RootCons.DEVICE_NAME, RootCons.DEVICE_NAME_DEFAULT);
            boolean isHH42 = RootUtils.isHH42(devname);

            if (isHH42) {// 1.1.HH42(外包)产品

                // project: 从WebUiVersion取 [WebUiVersion = "HH42NK_V1.1.0B10T01"]
                // custom: 从SwVersion截取拼接 [SwVersion = "GW42_TCL_NK_OPEN_MARKET_V1.0.0B10T01"]
                // version: project拼接custon [SwVersion = "GW42_TCL_NK_OPEN_MARKET_V1.0.0B10T01"]
                // 格式: https://www.alcatel-move.com/um/url.html?project=HH42NK&custom=open_market&version=HH42NK_open_market&lang=en

                mProject = result.getWebUiVersion().split("_")[0];
                String custom_pre = result.getSwVersion().split("_")[3].toLowerCase();
                String custom_pif = result.getSwVersion().split("_")[4].toLowerCase();
                mCustom = custom_pre + "_" + custom_pif;
                mVersion = mProject + "_" + custom_pre + "_" + custom_pif;

            } else {// 1.1.TCL自营产品
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
            }

            // 2.显示UI
            mDeviceNameTxt.setText(result.getDeviceName());
            mImeiTxt.setText(result.getIMEI());
            mMacAddressTxt.setText(result.getMacAddress());
        });
        xGetSystemInfoHelper.setOnPrepareHelperListener(() -> wdLoad.setVisibles());
        xGetSystemInfoHelper.setOnDoneHelperListener(() -> wdLoad.setGone());
        xGetSystemInfoHelper.getSystemInfo();

        GetLanSettingsHelper xGetLanSettingsHelper = new GetLanSettingsHelper();
        xGetLanSettingsHelper.setOnGetLanSettingsSuccessListener(result -> {
            Logg.t("HH42_ALAN_SYS").ii(result.toString());
            mManagementIpTxt.setText(result.getIPv4IPAddress().isEmpty() ? "0.0.0.0" : result.getIPv4IPAddress());
            mSubnetMaskTxt.setText(result.getSubnetMask().isEmpty() ? "0.0.0.0" : result.getSubnetMask());
        });
        xGetLanSettingsHelper.getLanSettings();
    }

    /**
     * 显示APP版本
     */
    private void displayVersion() {
        PackageManager pm = activity.getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(activity.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            ApplicationInfo appInfo = activity.getApplicationInfo();
            boolean isDebuggable = (appInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
            mAppVersionTxt.setText(String.valueOf("v" + info.versionName + (isDebuggable ? "(debug)" : "")));
            mIvPoint.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onBackPresss() {
        if (wdLoad.getVisibility() == View.VISIBLE) {
            wdLoad.setGone();
            return true;
        }
        toFrag(getClass(), SettingFrag.class, null, false);
        return true;
    }

}
