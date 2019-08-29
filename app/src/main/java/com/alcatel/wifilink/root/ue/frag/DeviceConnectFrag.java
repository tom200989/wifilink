package com.alcatel.wifilink.root.ue.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.HH70ConnectAdapter;
import com.alcatel.wifilink.root.bean.ConnectBean;
import com.alcatel.wifilink.root.bean.ConnectedListBean;
import com.alcatel.wifilink.root.helper.ModelHelper;
import com.alcatel.wifilink.root.utils.RootCons;
import com.hiber.cons.TimerState;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetConnectDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetBlockDeviceListHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetConnectedDeviceListHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DeviceConnectFrag extends BaseFrag {

    @BindView(R.id.rcv_deviceconnect)
    RecyclerView rcv_deviceConnect;
    @BindView(R.id.device_back)
    ImageButton mbackBtn;
    @BindView(R.id.device_title)
    TextView mTitle;
    @BindView(R.id.device_block)
    TextView mblock;

    public String blockPre;
    public String blockFix = ")";
    public int blockSize;

    private HH70ConnectAdapter rvAdapter;
    private List<ConnectBean> connectBeanList = new ArrayList<>();
    public boolean isStopGetDeviceStatus;//是否停止刷新读取设备状态,默认false表示需要刷新状态

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_deviceconnect;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o,view,s);
        initUI();
        getDevicesStatus();// init
        //开启定时器
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    private void initUI(){
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_deviceConnect.setLayoutManager(lm);
        rvAdapter = new HH70ConnectAdapter(activity, this, connectBeanList);
        rcv_deviceConnect.setAdapter(rvAdapter);
        //titlebar
        blockPre = getString(R.string.hh70_Blocked) + " (";
        mbackBtn.setOnClickListener(v -> {
            onBackPresss();
        });
        mblock.setOnClickListener(v -> {
            checkBlockList();
        });
        mblock.setText(String.valueOf(blockPre + "0" + blockFix));
        mTitle.setText(getString(R.string.hh70_connect_small));
        // 俄语文字大小适配
        String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
        if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
            mblock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    //提供方法给Adapter调用
    public void setBlockText(String content){
        mblock.setText(content);
    }

    @Override
    public void setTimerTask(){
        // refresh all
        if(!isStopGetDeviceStatus){
            getDevicesStatus();
        }
    }

    /* **** getDevicesStatus **** */
    private void getDevicesStatus() {
        // getInstant connect devices
        updateConnectedDeviceUI();
        // refresh blocked count
        updateBlockCount();
    }

    private void checkBlockList() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            if (getBlockDeviceListBean.getBlockList().size() > 0) {
                toFrag(getClass(),DeviceBlockFrag.class,null,false);
            }
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    /* **** updateConnectedDeviceUI **** */
    private void updateConnectedDeviceUI() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(bean -> {
            //将xsmart框架内部的Bean转为旧的Bean
            ConnectedListBean tempConnectedListBean = new ConnectedListBean();
            //框架内部返回的列表
            List<GetConnectDeviceListBean.ConnectedDeviceBean> connectedList = bean.getConnectedList();
            if(connectedList != null && connectedList.size() > 0){
                //旧的Bean列表
                List<ConnectedListBean.Device> tempDeviceArrayList = new ArrayList<>();
                for(GetConnectDeviceListBean.ConnectedDeviceBean connectedDeviceBean : connectedList){
                    //旧的Bean
                    ConnectedListBean.Device device = new ConnectedListBean.Device();
                    device.setAssociationTime(connectedDeviceBean.getAssociationTime());
                    device.setConnectMode(connectedDeviceBean.getConnectMode());
                    device.setDeviceName(connectedDeviceBean.getDeviceName());
                    device.setDeviceType(connectedDeviceBean.getDeviceType());
                    device.setId(connectedDeviceBean.getId());
                    device.setInternetRight(connectedDeviceBean.getInternetRight());
                    device.setIPAddress(connectedDeviceBean.getIPAddress());
                    device.setMacAddress(connectedDeviceBean.getMacAddress());
                    device.setStorageRight(connectedDeviceBean.getStorageRight());
                    //旧的Bean列表
                    tempDeviceArrayList.add(device);
                }
                //填充旧的bean
                tempConnectedListBean.setConnectedList(tempDeviceArrayList);
            }
            //向外抛出
            connectBeanList = ModelHelper.getConnectModel(tempConnectedListBean);
            if (connectBeanList.size() > 0) {
                rvAdapter.notifys(connectBeanList);
            }
        });
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    /* **** updateBlockCount **** */
    private void updateBlockCount() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            blockSize = getBlockDeviceListBean.getBlockList().size();
            mblock.setText(blockPre + blockSize + blockFix);
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(),mainFrag.class,null,false);
        return true;
    }
}
