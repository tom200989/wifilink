package com.alcatel.wifilink.ue.frag;

import android.annotation.SuppressLint;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.bean.ConnectBean;
import com.alcatel.wifilink.adapter.HH70ConnectAdapter;
import com.alcatel.wifilink.utils.RootCons;
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
        super.onNexts(o, view, s);
        initUI();
        //开启定时器
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    @SuppressLint("SetTextI18n")
    private void initUI() {
        LinearLayoutManager lm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rcv_deviceConnect.setLayoutManager(lm);
        rvAdapter = new HH70ConnectAdapter(activity, this, connectBeanList);
        rcv_deviceConnect.setAdapter(rvAdapter);
        //titlebar
        blockPre = getRootString(R.string.hh70_Blocked) + " (";
        mbackBtn.setOnClickListener(v -> onBackPresss());
        mblock.setOnClickListener(v -> checkBlockList());
        mblock.setText(blockPre + "0" + blockFix);
        mTitle.setText(getRootString(R.string.hh70_connect_small));
        // 俄语文字大小适配
        String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
        if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
            mblock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    //提供方法给Adapter调用
    public void setBlockText(String content) {
        mblock.setText(content);
    }

    @Override
    public void setTimerTask() {
        // refresh all
        if (!isStopGetDeviceStatus) {
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
                toFrag(getClass(), DeviceBlockFrag.class, null, false);
            }
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    /* **** updateConnectedDeviceUI **** */
    private void updateConnectedDeviceUI() {
        GetConnectedDeviceListHelper xGetConnectedDeviceListHelper = new GetConnectedDeviceListHelper();
        xGetConnectedDeviceListHelper.setOnGetDeviceListSuccessListener(bean -> {
            connectBeanList.clear();// clear一定要放在这个回调监听里做 , 不能放在外面, 否则rcv有可能出现加载数据错误
            List<GetConnectDeviceListBean.ConnectedDeviceBean> connectedList = bean.getConnectedList();
            for (GetConnectDeviceListBean.ConnectedDeviceBean connectedDeviceBean : connectedList) {
                connectBeanList.add(new ConnectBean(connectedDeviceBean, false));
            }
            rvAdapter.notifys(connectBeanList);
        });
        xGetConnectedDeviceListHelper.getConnectDeviceList();
    }

    /* **** updateBlockCount **** */
    private void updateBlockCount() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            blockSize = getBlockDeviceListBean.getBlockList().size();
            mblock.setText(blockPre + blockSize + blockFix);
            if (blockSize > 0) {
                mblock.setTextColor(getRootColor(R.color.white));
            } else {
                mblock.setTextColor(getRootColor(R.color.gray));
            }
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), mainFrag.class, null, false);
        return true;
    }
}
