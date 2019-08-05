package com.alcatel.wifilink.root.ue.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.WanSettingsParams;
import com.alcatel.wifilink.root.bean.WanSettingsResult;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.helper.GetWanSettingHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;

public class EthernetWanConnectionActivity extends BaseActivityWithBack implements OnClickListener {
    private static final String TAG = "EthernetWanConnectionActivity";

    private ImageView mSelectedPppopImg;
    private ImageView mSelectedDhcpImg;
    private ImageView mSelectedStaticIpImg;
    private LinearLayout mPppopLayout;
    private TextView mDhcpTextview;
    private LinearLayout mStaticIpLayout;

    //pppoe
    private EditText mPppoeAccount;
    private EditText mPppoePassword;
    private EditText mPppoeMtu;

    //Static Ip
    private EditText mStaticIpAddress;
    private EditText mStaticIpSubnetMask;
    private EditText mStaticIpDefaultGateway;
    private EditText mStaticIpPreferredDns;
    private EditText mStaticIpSecondaryDns;
    private EditText mStaticIpMtu;

    private Button mConnectOrDisconnect;
    private WanSettingsResult mWanSettingsResult;
    private WanSettingsParams mWanSettingsParams;
    private boolean mIsConnecting;
    private int flag = Cons.FLAG_PPPOE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ethernet_wan_connection);
        setTitle(getString(R.string.ethernet_wan_connection));
        mWanSettingsResult = new WanSettingsResult();
        mWanSettingsParams = new WanSettingsParams();
        mSelectedPppopImg = (ImageView) findViewById(R.id.pppoe_selected_img);
        mSelectedDhcpImg = (ImageView) findViewById(R.id.dhcp_selected_img);
        mSelectedStaticIpImg = (ImageView) findViewById(R.id.static_ip_selected_img);
        mPppopLayout = (LinearLayout) findViewById(R.id.linearlayout_pppoe);
        mDhcpTextview = (TextView) findViewById(R.id.textview_dhcp);
        mStaticIpLayout = (LinearLayout) findViewById(R.id.linearlayout_static_ip);

        findViewById(R.id.ethernet_wan_connection_pppoe).setOnClickListener(this);
        findViewById(R.id.ethernet_wan_connection_dhcp).setOnClickListener(this);
        findViewById(R.id.ethernet_wan_connection_static_ip).setOnClickListener(this);

        //pppoe
        mPppoeAccount = (EditText) findViewById(R.id.pppoe_account);
        mPppoePassword = (EditText) findViewById(R.id.pppoe_password);
        mPppoeMtu = (EditText) findViewById(R.id.pppoe_mtu);

        //static ip_phone
        mStaticIpAddress = (EditText) findViewById(R.id.static_ip_address);
        mStaticIpSubnetMask = (EditText) findViewById(R.id.static_ip_subnet_mask);
        mStaticIpDefaultGateway = (EditText) findViewById(R.id.static_ip_default_gateway);
        mStaticIpPreferredDns = (EditText) findViewById(R.id.static_ip_preferred_dns);
        mStaticIpSecondaryDns = (EditText) findViewById(R.id.static_ip_secondary_dns);
        mStaticIpMtu = (EditText) findViewById(R.id.static_ip_mtu);

        mConnectOrDisconnect = (Button) findViewById(R.id.btn_connect);
        mConnectOrDisconnect.setOnClickListener(this);

        mIsConnecting = false;
        getWanSettings();
    }

    private void getWanSettings() {

        GetWanSettingHelper wan = new GetWanSettingHelper();
        wan.setOnGetwansettingsErrorListener(e -> {
        });
        wan.setOnGetWanSettingsFailedListener(() -> {
        });
        wan.setOnGetWanSettingsResultErrorListener(error -> Toast.makeText(EthernetWanConnectionActivity.this, getString(R.string.setting_failed), Toast.LENGTH_SHORT).show());
        wan.setOnGetWanSettingsSuccessListener(result -> {
            mWanSettingsResult = result;
            if (result.getStatus() == 1 && mIsConnecting) {
                getWanSettings();
                return;
            } else if (result.getStatus() == 2) {
                mIsConnecting = false;
            }
            if (result.getConnectType() == 0) {
                showConnectPppoe();
            } else if (result.getConnectType() == 1) {
                showConnectDhcp();
            } else if (result.getConnectType() == 2) {
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
        wan.get();
    }

    private void setWanSettings() {
        RX.getInstant().setWanSettings(mWanSettingsParams, new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
                Toast.makeText(EthernetWanConnectionActivity.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                getWanSettings();
            }

            @Override
            protected void onFailure() {
                mIsConnecting = false;
            }
        });
    }

    private String getEdContent(EditText et) {
        String content = et.getText().toString().trim().replace(" ", "");
        return content;
    }

    @Override
    public void onClick(View v) {

        int nID = v.getId();
        switch (nID) {
            case R.id.ethernet_wan_connection_pppoe:
                showConnectPppoe();
                break;
            case R.id.ethernet_wan_connection_dhcp:
                showConnectDhcp();
                break;
            case R.id.ethernet_wan_connection_static_ip:
                showConnectStaticIp();
                break;
            case R.id.btn_connect:
                if (mConnectOrDisconnect.getCurrentTextColor() == getResources().getColor(R.color.white)) {
                    connectWan();
                } else {
                    Toast.makeText(EthernetWanConnectionActivity.this, R.string.it_is_connected_you_can_switch_other_connection_mode_page, Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void connectWan() {

        // 检测MTU是否符合规则
        int pppoeMtu = Integer.valueOf(TextUtils.isEmpty(getEdContent(mPppoeMtu)) ? "1492" : getEdContent(mPppoeMtu));
        int staticMtu = Integer.valueOf(TextUtils.isEmpty(getEdContent(mStaticIpMtu)) ? "1500" : getEdContent(mStaticIpMtu));
        if (flag == Cons.FLAG_PPPOE) {
            if (pppoeMtu < 576 || pppoeMtu > 1492) {
                ToastUtil_m.show(this, getString(R.string.mtu_not_match).replace("1500", "1492"));
                return;
            }
            mWanSettingsParams.setPppoeMtu(pppoeMtu);
        } else if (flag == Cons.FLAG_STATIC_IP) {

            String str_staticIp = OtherUtils.getEdContent(mStaticIpAddress);
            String str_subnetMask = OtherUtils.getEdContent(mStaticIpSubnetMask);
            String str_defaultGateway = OtherUtils.getEdContent(mStaticIpDefaultGateway);
            String str_preferredDns = OtherUtils.getEdContent(mStaticIpPreferredDns);
            String str_secondaryDns = OtherUtils.getEdContent(mStaticIpSecondaryDns);

            // 非空判断
            if (OtherUtils.isEmptys(mStaticIpAddress, mStaticIpSubnetMask, mStaticIpDefaultGateway, mStaticIpPreferredDns, mStaticIpSecondaryDns)) {
                toast(getString(R.string.not_empty));
                return;
            }

            // static ip_phone
            if (!OtherUtils.ipSuperMatch(str_staticIp)) {
                String ipValid = getString(R.string.ip_address) + "\n" + getString(R.string.connect_failed);
                toast(ipValid);
                return;
            }

            // subnet mask
            if (!OtherUtils.ipSuperMatch(str_subnetMask)) {
                String subnetValid = getString(R.string.subnet_mask) + "\n" + getString(R.string.connect_failed);
                toast(subnetValid);
                return;
            }

            // default gate way
            if (!OtherUtils.ipSuperMatch(str_defaultGateway)) {
                String defaultGateway = getString(R.string.default_gateway) + "\n" + getString(R.string.connect_failed);
                toast(defaultGateway);
                return;
            }

            // preferred dns
            if (!OtherUtils.ipSuperMatch(str_preferredDns)) {
                String preferred_dns = getString(R.string.preferred_dns) + "\n" + getString(R.string.connect_failed);
                toast(preferred_dns);
                return;
            }

            // secondary dns
            if (!OtherUtils.ipSuperMatch(str_secondaryDns)) {
                String secondary_dns = getString(R.string.secondary_dns) + "\n" + getString(R.string.connect_failed);
                toast(secondary_dns);
                return;
            }


            if (staticMtu < 576 || staticMtu > 1500) {
                ToastUtil_m.show(this, getString(R.string.mtu_not_match));
                return;
            }
            mWanSettingsParams.setMtu(staticMtu);
        }

        mWanSettingsParams.setAccount(mPppoeAccount.getText().toString().trim());
        mWanSettingsParams.setPassword(mPppoePassword.getText().toString().trim());
        // mWanSettingsParams.setPppoeMtu(mWanSettingsResult.getPppoeMtu());
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

    private void showConnectStaticIp() {
        flag = Cons.FLAG_STATIC_IP;
        if (mWanSettingsResult.getConnectType() == 2 && mWanSettingsResult.getStatus() == 2) {
            mConnectOrDisconnect.setTextColor(getResources().getColor(R.color.gray));
        } else {
            mConnectOrDisconnect.setTextColor(getResources().getColor(R.color.white));
        }
        mWanSettingsParams.setConnectType(2);
        mSelectedPppopImg.setVisibility(View.GONE);
        mSelectedDhcpImg.setVisibility(View.GONE);
        mSelectedStaticIpImg.setVisibility(View.VISIBLE);
        mPppopLayout.setVisibility(View.GONE);
        mDhcpTextview.setVisibility(View.GONE);
        mStaticIpLayout.setVisibility(View.VISIBLE);
    }

    private void showConnectDhcp() {
        flag = Cons.FLAG_DHCP;
        if (mWanSettingsResult.getConnectType() == 1 && mWanSettingsResult.getStatus() == 2) {
            mConnectOrDisconnect.setTextColor(getResources().getColor(R.color.gray));
        } else {
            mConnectOrDisconnect.setTextColor(getResources().getColor(R.color.white));
        }
        mWanSettingsParams.setConnectType(1);
        mSelectedPppopImg.setVisibility(View.GONE);
        mSelectedDhcpImg.setVisibility(View.VISIBLE);
        mSelectedStaticIpImg.setVisibility(View.GONE);
        mPppopLayout.setVisibility(View.GONE);
        mDhcpTextview.setVisibility(View.VISIBLE);
        mStaticIpLayout.setVisibility(View.GONE);
    }

    private void showConnectPppoe() {
        flag = Cons.FLAG_PPPOE;
        if (mWanSettingsResult.getConnectType() == 0 && mWanSettingsResult.getStatus() == 2) {
            mConnectOrDisconnect.setTextColor(getResources().getColor(R.color.gray));
        } else {
            mConnectOrDisconnect.setTextColor(getResources().getColor(R.color.white));
        }
        mWanSettingsParams.setConnectType(0);
        mSelectedPppopImg.setVisibility(View.VISIBLE);
        mSelectedDhcpImg.setVisibility(View.GONE);
        mSelectedStaticIpImg.setVisibility(View.GONE);
        mPppopLayout.setVisibility(View.VISIBLE);
        mDhcpTextview.setVisibility(View.GONE);
        mStaticIpLayout.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onStart() {

        super.onStart();
    }

    @Override
    public void onPause() {

        super.onPause();
    }

    @Override
    public void onResume() {

        super.onResume();
    }

    @Override
    public void onStop() {

        super.onStop();
    }

    public void toast(String content) {
        ToastUtil_m.show(this, content);
    }


}
