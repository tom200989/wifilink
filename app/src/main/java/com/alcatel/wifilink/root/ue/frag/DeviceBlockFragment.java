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
import com.alcatel.wifilink.root.bean.BlockModel;
import com.alcatel.wifilink.root.bean.BlockList;
import com.alcatel.wifilink.root.ue.activity.ActivityDeviceManager;
import com.alcatel.wifilink.root.adapter.BlockAdapter;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetBlockDeviceListHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@SuppressLint("ValidFragment")
public class DeviceBlockFragment extends Fragment {

    @BindView(R.id.rcv_deviceBlock)
    RecyclerView rcv_block;
    Unbinder unbinder;
    private View inflate;
    private ActivityDeviceManager activity;
    private BlockAdapter blockAdapter;
    private List<BlockModel> blockList = new ArrayList<>();
    private List<BlockModel> blockModelList;
    public TimerHelper timerHelper;

    public DeviceBlockFragment(Activity activity) {
        this.activity = (ActivityDeviceManager) activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        inflate = View.inflate(getActivity(), R.layout.fragment_deviceblock, null);
        unbinder = ButterKnife.bind(this, inflate);
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_block.setLayoutManager(lm);
        blockAdapter = new BlockAdapter(activity, blockList);
        rcv_block.setAdapter(blockAdapter);
        updateBlockDeviceUI();// init ui
        return inflate;
    }

    @Override
    public void onResume() {
        super.onResume();
        // start timer
        startTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHelper.stop();
    }

    private void startTimer() {
        timerHelper = new TimerHelper(activity) {
            @Override
            public void doSomething() {
                // refresh all
                updateBlockDeviceUI();
            }
        };
        timerHelper.start(3000);
    }

    /* **** updateBlockDeviceUI **** */
    private void updateBlockDeviceUI() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            List<BlockModel> tempBlockModelList = new ArrayList<>();
            List<GetBlockDeviceListBean.BlockDeviceBean> blockDeviceBeans = getBlockDeviceListBean.getBlockList();
            if(blockDeviceBeans != null && blockDeviceBeans.size() > 0){
                for(GetBlockDeviceListBean.BlockDeviceBean blockDeviceBean : blockDeviceBeans){
                    BlockList.BlockDevice blockDevice = new BlockList.BlockDevice();
                    blockDevice.setId(blockDeviceBean.getId());
                    blockDevice.setDeviceName(blockDeviceBean.getDeviceName());
                    blockDevice.setMacAddress(blockDeviceBean.getMacAddress());
                    tempBlockModelList.add(new BlockModel(blockDevice,false));
                }
            }
            blockModelList = tempBlockModelList;
            if (blockModelList.size() > 0) {
                blockAdapter.notifys(blockModelList);
            }
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
