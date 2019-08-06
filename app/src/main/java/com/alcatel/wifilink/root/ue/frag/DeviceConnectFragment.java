package com.alcatel.wifilink.root.ue.frag;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.ConnectModel;
import com.alcatel.wifilink.root.bean.BlockList;
import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.ue.activity.ActivityDeviceManager;
import com.alcatel.wifilink.root.adapter.ConnectAdapter;
import com.alcatel.wifilink.root.helper.ModelHelper;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetBlockDeviceListHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class DeviceConnectFragment extends Fragment {

    @BindView(R.id.rcv_deviceconnect)
    RecyclerView rcv_deviceConnect;
    Unbinder unbinder;
    private View inflate;
    private ConnectAdapter rvAdapter;
    private List<ConnectModel> connectModelList = new ArrayList<>();
    private ActivityDeviceManager activity;
    public TimerHelper timerHelper;

    public DeviceConnectFragment(Activity activity) {
        this.activity = (ActivityDeviceManager) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = View.inflate(getActivity(), R.layout.fragment_deviceconnect, null);
        unbinder = ButterKnife.bind(this, inflate);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_deviceConnect.setLayoutManager(lm);
        rvAdapter = new ConnectAdapter(activity, this, connectModelList);
        rcv_deviceConnect.setAdapter(rvAdapter);
        getDevicesStatus();// init
        
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        // start timer
        startTime();
    }

    /* **** startTime **** */
    private void startTime() {
        timerHelper = new TimerHelper(getActivity()) {
            @Override
            public void doSomething() {
                getDevicesStatus();
            }
        };
        timerHelper.start(3000);
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHelper.stop();
    }

    /* **** getDevicesStatus **** */
    private void getDevicesStatus() {
        // getInstant connect devices
        updateConnectedDeviceUI();
        // refresh blocked count
        updateBlockCount();
    }

    /* **** updateConnectedDeviceUI **** */
    private void updateConnectedDeviceUI() {
        RX.getInstant().getConnectedDeviceList(new ResponseObject<ConnectedList>() {
            @Override
            protected void onSuccess(ConnectedList result) {
                connectModelList = ModelHelper.getConnectModel(result);
                if (connectModelList.size() > 0) {
                    rvAdapter.notifys(connectModelList);
                }
            }
        });
    }

    /* **** updateBlockCount **** */
    private void updateBlockCount() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            int blockSize = getBlockDeviceListBean.getBlockList().size();
            activity.mblock.setText(activity.blockPre + blockSize + activity.blockFix);
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
