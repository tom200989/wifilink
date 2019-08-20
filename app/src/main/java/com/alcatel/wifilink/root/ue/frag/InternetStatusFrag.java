package com.alcatel.wifilink.root.ue.frag;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.hiber.cons.TimerState;
import com.hiber.hiber.RootFrag;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/19 0019.
 */
public class InternetStatusFrag extends RootFrag {

    // banner-back
    @BindView(R.id.iv_internet_status_back)
    RelativeLayout ivInternetBack;

    // ip_phone address
    @BindView(R.id.tv_internet_content_IPAddress)
    TextView tvInternetContentIPAddress;

    // subnet mask
    @BindView(R.id.tv_internet_content_subnetmask)
    TextView tvInternetContentSubnetmask;

    // default gateway
    @BindView(R.id.tv_internet_content_gateway)
    TextView tvInternetContentGateway;

    // perferred dns
    @BindView(R.id.tv_internet_content_dns)
    TextView tvInternetContentDns;

    // secondary dns
    @BindView(R.id.tv_internet_content_secondDns)
    TextView tvInternetContentSecondDns;

    // renew button
    @BindView(R.id.bt_internet_renew)
    Button btInternetRenew;

    // to connect type mode ui
    @BindView(R.id.tv_internet_ethernetWanConnect)
    TextView tvInternetEthernetWanConnect;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_internet_status;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
        initClick();
    }

    private void initClick() {
        // 回退
        ivInternetBack.setOnClickListener(v -> onBackPresss());
        // renew
        btInternetRenew.setOnClickListener(v -> getWanInfo());
        // ethernet wan connection
        tvInternetEthernetWanConnect.setOnClickListener(v -> toFrag(getClass(), EtherWANFrag.class, null, true));
    }

    @Override
    public void setTimerTask() {
        getWanInfo();
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), mainFrag.class, null, false);
        return true;
    }

    /**
     * A2.重新获取wan口信息
     */
    private void getWanInfo() {
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
            String defaultWan = "0.0.0.0";
            // ip
            String ipAddress = result.getIpAddress();
            tvInternetContentIPAddress.setText(TextUtils.isEmpty(ipAddress) ? defaultWan : ipAddress);
            // net mask
            String subNetMask = result.getSubNetMask();
            tvInternetContentSubnetmask.setText(TextUtils.isEmpty(ipAddress) ? defaultWan : subNetMask);
            // gateway
            String gateway = result.getGateway();
            tvInternetContentGateway.setText(TextUtils.isEmpty(ipAddress) ? defaultWan : gateway);
            // dns
            String primaryDNS = result.getPrimaryDNS();
            tvInternetContentDns.setText(TextUtils.isEmpty(ipAddress) ? defaultWan : primaryDNS);
            // sec dns
            String secondaryDNS = result.getSecondaryDNS();
            tvInternetContentSecondDns.setText(TextUtils.isEmpty(ipAddress) ? defaultWan : secondaryDNS);
        });
        xGetWanSettingsHelper.getWanSettings();
    }
}
