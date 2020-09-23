package com.alcatel.wifilink.ue.frag;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.RootUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetWanSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetWanSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetWanSettingsHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetWanSettingsHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class EtherWANFrag extends BaseFrag {

    // back
    @BindView(R.id.iv_ether_wan_back)
    ImageView ivEtherWanBack;

    @BindView(R.id.pppoe_selected_img)
    ImageView mSelectedPppopImg;
    @BindView(R.id.dhcp_selected_img)
    ImageView mSelectedDhcpImg;
    @BindView(R.id.static_ip_selected_img)
    ImageView mSelectedStaticIpImg;
    @BindView(R.id.linearlayout_pppoe)
    LinearLayout mPppopLayout;
    @BindView(R.id.textview_dhcp)
    TextView mDhcpTextview;
    @BindView(R.id.linearlayout_static_ip)
    LinearLayout mStaticIpLayout;

    @BindView(R.id.ethernet_wan_connection_pppoe)
    RelativeLayout etherWAN_PPPOE;
    @BindView(R.id.ethernet_wan_connection_dhcp)
    RelativeLayout etherWAN_DHCP;
    @BindView(R.id.ethernet_wan_connection_static_ip)
    RelativeLayout etherWAN_StaticIP;

    // pppoe
    @BindView(R.id.pppoe_account)
    EditText mPppoeAccount;
    @BindView(R.id.pppoe_password)
    EditText mPppoePassword;
    @BindView(R.id.pppoe_mtu)
    EditText mPppoeMtu;

    // static ip
    @BindView(R.id.static_ip_address)
    EditText mStaticIpAddress;
    @BindView(R.id.static_ip_subnet_mask)
    EditText mStaticIpSubnetMask;
    @BindView(R.id.static_ip_default_gateway)
    EditText mStaticIpDefaultGateway;
    @BindView(R.id.static_ip_preferred_dns)
    EditText mStaticIpPreferredDns;
    @BindView(R.id.static_ip_secondary_dns)
    EditText mStaticIpSecondaryDns;
    @BindView(R.id.static_ip_mtu)
    EditText mStaticIpMtu;

    @BindView(R.id.btn_connect)
    Button mConnectOrDisconnect;

    private GetWanSettingsBean mWanSettingsResult = new GetWanSettingsBean();
    private SetWanSettingsParam mWanSettingsParams = new SetWanSettingsParam();
    private boolean mIsConnecting;
    private int flag = GetWanSettingsBean.CONS_PPPOE;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_etherwan;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        mIsConnecting = false;
        getWanSettings();
        initClick();
    }

    private void initClick() {
        // 回退
        ivEtherWanBack.setOnClickListener(v -> onBackPresss());
        // 点击条目PPPOE
        etherWAN_PPPOE.setOnClickListener(v -> showConnectPppoe());
        // 点击条目DHCP
        etherWAN_DHCP.setOnClickListener(v -> showConnectDhcp());
        // 点击条目Static ip
        etherWAN_StaticIP.setOnClickListener(v -> showConnectStaticIp());
        // 点击连接
        mConnectOrDisconnect.setOnClickListener(v -> {
            if (mConnectOrDisconnect.getCurrentTextColor() == getRootColor(R.color.white)) {
                connectWan();
            } else {
                toast(R.string.hh70_switch_other_connect_mode, 3000);
            }
        });
    }

    private void connectWan() {

        // 检测MTU是否符合规则
        int pppoeMtu = Integer.parseInt(TextUtils.isEmpty(RootUtils.getEDText(mPppoeMtu)) ? "1492" : RootUtils.getEDText(mPppoeMtu));
        int staticMtu = Integer.parseInt(TextUtils.isEmpty(RootUtils.getEDText(mStaticIpMtu)) ? "1500" : RootUtils.getEDText(mStaticIpMtu));
        if (flag == GetWanSettingsBean.CONS_PPPOE) {
            if (pppoeMtu < 576 || pppoeMtu > 1492) {
                String content = getRootString(R.string.hh70_mtu_between_576).replace("1500", "1492");
                toast(content, 3000);
                return;
            }
            mWanSettingsParams.setPppoeMtu(pppoeMtu);

        } else if (flag == GetWanSettingsBean.CONS_STATIC) {
            String str_staticIp = RootUtils.getEDText(mStaticIpAddress);
            String str_subnetMask = RootUtils.getEDText(mStaticIpSubnetMask);
            String str_defaultGateway = RootUtils.getEDText(mStaticIpDefaultGateway);
            String str_preferredDns = RootUtils.getEDText(mStaticIpPreferredDns);
            String str_secondaryDns = RootUtils.getEDText(mStaticIpSecondaryDns);

            // 非空判断
            if (RootUtils.isEdEmpty(mStaticIpAddress, mStaticIpSubnetMask, mStaticIpDefaultGateway, mStaticIpPreferredDns)) {
                toast(getRootString(R.string.hh70_not_permit_empty), 3000);
                return;
            }

            // static ip_phone
            if (!RootUtils.isStaticIPMatch(str_staticIp)) {
                String ipValid = getRootString(R.string.hh70_ip_address) + "\n" + getString(R.string.hh70_cant_connect);
                toast(ipValid, 3000);
                return;
            }

            // subnet mask
            if (!RootUtils.isSubnetMaskMatch(str_subnetMask)) {
                String subnetValid = getRootString(R.string.hh70_subnet_mask) + "\n" + getRootString(R.string.hh70_cant_connect);
                toast(subnetValid, 3000);
                return;
            }

            // default gate way
            if (!RootUtils.isAllMatch(str_defaultGateway)) {
                String defaultGateway = getRootString(R.string.hh70_default_gateway) + "\n" + getRootString(R.string.hh70_cant_connect);
                toast(defaultGateway, 3000);
                return;
            }

            // preferred dns
            if (!RootUtils.isAllMatch(str_preferredDns)) {
                String preferred_dns = getRootString(R.string.hh70_prefer_dns) + "\n" + getRootString(R.string.hh70_cant_connect);
                toast(preferred_dns, 3000);
                return;
            }

            // secondary dns 可选项
            if (!TextUtils.isEmpty(str_secondaryDns) && !RootUtils.isAllMatch(str_secondaryDns)) {
                String secondary_dns = getRootString(R.string.hh70_second_dns) + "\n" + getRootString(R.string.hh70_cant_connect);
                toast(secondary_dns, 3000);
                return;
            }


            if (staticMtu < 576 || staticMtu > 1500) {
                toast(getRootString(R.string.hh70_mtu_between_576), 3000);
                return;
            }
            mWanSettingsParams.setMtu(staticMtu);
        }

        mWanSettingsParams.setAccount(mPppoeAccount.getText().toString().trim());
        mWanSettingsParams.setPassword(mPppoePassword.getText().toString().trim());
        mWanSettingsParams.setStaticIpAddress(mStaticIpAddress.getText().toString().trim());
        mWanSettingsParams.setIpAddress(mWanSettingsResult.getIpAddress());
        mWanSettingsParams.setSubNetMask(mStaticIpSubnetMask.getText().toString().trim());
        mWanSettingsParams.setGateway(mStaticIpDefaultGateway.getText().toString());
        mWanSettingsParams.setPrimaryDNS(mStaticIpPreferredDns.getText().toString());
        mWanSettingsParams.setSecondaryDNS(mStaticIpSecondaryDns.getText().toString());
        mWanSettingsParams.setMtu(mWanSettingsResult.getMtu());
        mIsConnecting = true;
        setWanSettings();
    }

    private void setWanSettings() {
        SetWanSettingsHelper xSetWanSettingsHelper = new SetWanSettingsHelper();
        xSetWanSettingsHelper.setOnSetWanSettingsSuccessListener(() -> {
            toast(R.string.hh70_success, 3000);
            getWanSettings();
        });
        xSetWanSettingsHelper.setOnSetWanSettingsFailedListener(() -> mIsConnecting = false);
        xSetWanSettingsHelper.setWanSettings(mWanSettingsParams);
    }

    private void getWanSettings() {
        GetWanSettingsHelper xGetWanSettingsHelper = new GetWanSettingsHelper();
        xGetWanSettingsHelper.setOnGetWanSettingsSuccessListener(result -> {
            mWanSettingsResult = result;
            if (result.getStatus() == GetWanSettingsBean.CONS_CONNECTING && mIsConnecting) {
                getWanSettings();
                return;
            } else if (result.getStatus() == GetWanSettingsBean.CONS_CONNECTED) {
                mIsConnecting = false;
            }
            if (result.getConnectType() == GetWanSettingsBean.CONS_PPPOE) {
                showConnectPppoe();
            } else if (result.getConnectType() == GetWanSettingsBean.CONS_DHCP) {
                showConnectDhcp();
            } else if (result.getConnectType() == GetWanSettingsBean.CONS_STATIC) {
                showConnectStaticIp();
            }

            mPppoeAccount.setText(result.getAccount());
            mPppoePassword.setText(result.getPassword());
            mPppoeMtu.setText(String.valueOf(result.getPppoeMtu()));
            mStaticIpAddress.setText(result.getIpAddress());
            mStaticIpSubnetMask.setText(result.getSubNetMask());
            mStaticIpDefaultGateway.setText(result.getGateway());
            mStaticIpPreferredDns.setText(result.getPrimaryDNS());
            mStaticIpSecondaryDns.setText(result.getSecondaryDNS());
            mStaticIpMtu.setText(String.valueOf(result.getMtu()));
        });
        xGetWanSettingsHelper.setOnGetWanSettingFailedListener(() -> toast(R.string.hh70_failed, 3000));
        xGetWanSettingsHelper.getWanSettings();
    }

    private void showConnectStaticIp() {
        flag = GetWanSettingsBean.CONS_STATIC;
        if (mWanSettingsResult.getConnectType() == GetWanSettingsBean.CONS_STATIC && mWanSettingsResult.getStatus() == GetWanSettingsBean.CONS_CONNECTED) {
            mConnectOrDisconnect.setTextColor(getRootColor(R.color.gray));
        } else {
            mConnectOrDisconnect.setTextColor(getRootColor(R.color.white));
        }
        mWanSettingsParams.setConnectType(flag);
        mSelectedPppopImg.setVisibility(View.GONE);
        mSelectedDhcpImg.setVisibility(View.GONE);
        mSelectedStaticIpImg.setVisibility(View.VISIBLE);
        mPppopLayout.setVisibility(View.GONE);
        mDhcpTextview.setVisibility(View.GONE);
        mStaticIpLayout.setVisibility(View.VISIBLE);
    }

    private void showConnectDhcp() {
        flag = GetWanSettingsBean.CONS_DHCP;
        if (mWanSettingsResult.getConnectType() == GetWanSettingsBean.CONS_DHCP && mWanSettingsResult.getStatus() == GetWanSettingsBean.CONS_CONNECTED) {
            mConnectOrDisconnect.setTextColor(getRootColor(R.color.gray));
        } else {
            mConnectOrDisconnect.setTextColor(getRootColor(R.color.white));
        }
        mWanSettingsParams.setConnectType(flag);
        mSelectedPppopImg.setVisibility(View.GONE);
        mSelectedDhcpImg.setVisibility(View.VISIBLE);
        mSelectedStaticIpImg.setVisibility(View.GONE);
        mPppopLayout.setVisibility(View.GONE);
        mDhcpTextview.setVisibility(View.VISIBLE);
        mStaticIpLayout.setVisibility(View.GONE);
    }

    private void showConnectPppoe() {
        flag = GetWanSettingsBean.CONS_PPPOE;
        if (mWanSettingsResult.getConnectType() == GetWanSettingsBean.CONS_PPPOE && mWanSettingsResult.getStatus() == GetWanSettingsBean.CONS_CONNECTED) {
            mConnectOrDisconnect.setTextColor(getRootColor(R.color.gray));
        } else {
            mConnectOrDisconnect.setTextColor(getRootColor(R.color.white));
        }
        mWanSettingsParams.setConnectType(flag);
        mSelectedPppopImg.setVisibility(View.VISIBLE);
        mSelectedDhcpImg.setVisibility(View.GONE);
        mSelectedStaticIpImg.setVisibility(View.GONE);
        mPppopLayout.setVisibility(View.VISIBLE);
        mDhcpTextview.setVisibility(View.GONE);
        mStaticIpLayout.setVisibility(View.GONE);
    }

    @Override
    public boolean onBackPresss() {
        if(lastFrag != null){
            toFrag(getClass(), lastFrag, null, false);
        }else{
            toFrag(getClass(), InternetStatusFrag.class, null, false);
        }
        return true;
    }
}
