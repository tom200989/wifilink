package com.alcatel.wifilink.root.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.BlockBean;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.ue.frag.DeviceConnectFrag;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceUnblockHelper;

import java.util.List;

/**
 * Created by qianli.ma on 2017/6/30.
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockHolder> {

    public HomeActivity activity;
    private List<BlockBean> blockBeanList;

    public BlockAdapter(Activity activity, List<BlockBean> blockBeanList) {
        this.activity = (HomeActivity) activity;
        this.blockBeanList = blockBeanList;
    }

    public void notifys(List<BlockBean> blockBeanList) {
        this.blockBeanList = blockBeanList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return blockBeanList != null ? blockBeanList.size() : 0;
    }

    @Override
    public BlockHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlockHolder(LayoutInflater.from(activity).inflate(R.layout.hh70_item_device_block, parent, false));
    }

    @Override
    public void onBindViewHolder(BlockHolder holder, int position) {
        BlockBean blockBean = blockBeanList.get(position);
        GetBlockDeviceListBean.BlockDeviceBean block = blockBean.block;

        final String displayName = block.getDeviceName();
        holder.deviceName.setText(displayName);
        String mac = block.getMacAddress();
        holder.mac.setText(String.format(activity.getString(R.string.hh70_mac), mac));
        // Click the unblock button
        holder.unblockBtn.setOnClickListener(v -> setDeviceUnlock(displayName, mac, position));
    }

    /* setDeviceUnlock */
    private void setDeviceUnlock(String strDeviceName, String strMac, int position) {
        SetDeviceUnblockHelper xSetDeviceUnblockHelper = new SetDeviceUnblockHelper();
        xSetDeviceUnblockHelper.setOnSetDeviceUnBlockSuccessListener(() -> {
            blockBeanList.remove(position);
            if (blockBeanList.size() <= 0) {// if block name is empty then go to connect ui
                activity.toFrag(getClass(), DeviceConnectFrag.class, null, false, 0);
            }
            // refresh ui
            notifys(blockBeanList);
        });
        xSetDeviceUnblockHelper.setDeviceUnblock(strDeviceName, strMac);
    }
}
