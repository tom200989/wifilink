package com.alcatel.wifilink.root.ue.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.BlockAdapter;
import com.alcatel.wifilink.root.bean.BlockList;
import com.alcatel.wifilink.root.bean.BlockModel;
import com.alcatel.wifilink.root.utils.RootCons;
import com.hiber.cons.TimerState;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetBlockDeviceListHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DeviceBlockFrag extends BaseFrag {

    @BindView(R.id.rcv_deviceBlock)
    RecyclerView rcv_block;
    @BindView(R.id.device_back)
    ImageButton mbackBtn;
    @BindView(R.id.device_title)
    TextView mTitle;

    private BlockAdapter blockAdapter;
    private List<BlockModel> blockList = new ArrayList<>();
    private List<BlockModel> blockModelList;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_deviceblock;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o,view,s);
        initUI();
        updateBlockDeviceUI();// init ui
        //onResume
        //开启定时器
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    @Override
    public void setTimerTask(){
        // refresh all
        updateBlockDeviceUI();
    }

    private void initUI(){
        LinearLayoutManager lm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcv_block.setLayoutManager(lm);
        blockAdapter = new BlockAdapter(activity, blockList);
        rcv_block.setAdapter(blockAdapter);
        //titlebar
        mbackBtn.setOnClickListener(v -> {
            onBackPresss();
        });
        mTitle.setText(getString(R.string.Blocked));
        // 俄语文字大小适配
        String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
        if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
            mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
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
    public boolean onBackPresss() {
        toFrag(getClass(),DeviceConnectFrag.class,null,false);
        return true;
    }
}
