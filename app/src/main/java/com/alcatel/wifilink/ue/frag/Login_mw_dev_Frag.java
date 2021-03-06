package com.alcatel.wifilink.ue.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.bean.DeviceBean;
import com.alcatel.wifilink.helper.ConnectDeviceHelper;
import com.alcatel.wifilink.adapter.LoginMWDevAdapter;
import com.alcatel.wifilink.utils.RootUtils;
import com.hiber.cons.TimerState;
import com.hiber.hiber.RootFrag;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;

import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class Login_mw_dev_Frag extends RootFrag {

    @BindView(R.id.rl_login_devices)
    LinearLayout rlLoginDevices;// 显示设备总布局
    @BindView(R.id.iv_pre_devices_back)
    ImageView ivPreDevicesBack;// 返回
    @BindView(R.id.rcv_pre_devices)
    RecyclerView rcv;// 设备列表

    private LoginMWDevAdapter devAdapter;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_login_mw_device;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        timerState = TimerState.OFF_ALL_BUT_KEEP_CURRENT_OFF_WHEN_PAUSE;
        initAdapter();
        initClick();
        lastFrag = getClass();
    }

    /**
     * 初始化设备列表适配器
     */
    private void initAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(activity, 1, false);
        devAdapter = new LoginMWDevAdapter(activity, null);
        rcv.setLayoutManager(lm);
        rcv.setAdapter(devAdapter);
    }


    private void initClick() {
        ivPreDevicesBack.setOnClickListener(v -> onBackPresss());
    }

    @Override
    public void setTimerTask() {
        getDevicesInfo();// 获取在线设备的信息
    }

    /**
     * 获取在线设备的信息
     */
    private void getDevicesInfo() {
        // 1.初始化设备类型
        GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(attr -> {
            // 2.是MW120新设备
            if (RootUtils.isMWDEV(attr.getDeviceName())) {
                // 3.获取连接的设备
                ConnectDeviceHelper connDevHelper = new ConnectDeviceHelper();
                connDevHelper.setOnDevicesSuccessListener(connectedList -> {
                    // 4.更新适配器
                    List<DeviceBean> ddbs = RootUtils.transferDevicesbean(activity, connectedList);
                    devAdapter.notifys(ddbs);
                });
                connDevHelper.get();
            }
        });
        xGetSystemInfoHelper.getSystemInfo();
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), Login_mw_Frag.class, null, false);
        return true;
    }
}
