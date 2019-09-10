package com.alcatel.wifilink.root.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.BlockBean;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.ue.frag.DeviceBlockFrag;
import com.alcatel.wifilink.root.ue.frag.DeviceConnectFrag;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetBlockDeviceListBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetDeviceNameParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceNameHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceUnblockHelper;

import java.util.List;

/**
 * Created by qianli.ma on 2017/6/30.
 */

public class BlockAdapter extends RecyclerView.Adapter<BlockHolder> {

    public HomeActivity activity;
    private List<BlockBean> blockBeanList;
    private boolean isEditing = false;
    private DeviceBlockFrag fragment;

    public BlockAdapter(Activity activity, DeviceBlockFrag fragment, List<BlockBean> blockBeanList) {
        this.activity = (HomeActivity) activity;
        this.blockBeanList = blockBeanList;
        this.fragment = fragment;
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
        final int type = block.getDeviceType();
        final String displayName = block.getDeviceName();
        holder.deviceName.setText(displayName);
        holder.deviceNameEditView.setText(displayName);
        String mac = block.getMacAddress();
        holder.mac.setText(String.format(activity.getString(R.string.hh70_mac), mac));
        // Click the unblock button
        holder.unblockBtn.setOnClickListener(v -> setDeviceUnlock(displayName, mac, position));
        holder.ivEdit.setOnClickListener(v -> {
            isEditing = !isEditing;
            if(isEditing){
                holder.deviceNameEditView.setVisibility(View.VISIBLE);
                holder.deviceName.setVisibility(View.INVISIBLE);
                holder.ivEdit.setBackgroundResource(R.drawable.connected_finished);
            }else{
                fragment.isStopGetDeviceStatus = true;
                holder.deviceNameEditView.setVisibility(View.INVISIBLE);
                holder.deviceName.setVisibility(View.VISIBLE);
                holder.ivEdit.setBackgroundResource(R.drawable.connected_edit);
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                String strName = holder.deviceNameEditView.getText().toString().trim();
                if (strName.length() == 0 || strName.length() > 31) {
                    String msgRes = activity.getString(R.string.hh70_device_name_invalid);
                    Toast.makeText(activity, msgRes, Toast.LENGTH_SHORT).show();
                }
                if (strName.length() != 0 && !strName.equals(displayName)) {
                    blockBeanList.get(position).getBlock().setDeviceName(strName);
                    setDeviceName(strName, mac, type);
                }
            }
        });
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

    /* setDeviceName */
    private void setDeviceName(String strDeviceName, String strMac, int nDeviceType) {
        SetDeviceNameParam setDeviceNameParam= new SetDeviceNameParam();
        setDeviceNameParam.setDeviceName(strDeviceName);
        setDeviceNameParam.setMacAddress(strMac);
        setDeviceNameParam.setDeviceType(nDeviceType);
        SetDeviceNameHelper xSetDeviceNameHelper = new SetDeviceNameHelper();
        xSetDeviceNameHelper.setDeviceName(setDeviceNameParam);
        xSetDeviceNameHelper.setOnSetDeviceNameSuccessListener(() -> {
            notifys(blockBeanList);
            fragment.isStopGetDeviceStatus = true;
        });
        xSetDeviceNameHelper.setOnSetDeviceNameFailListener(() -> {

        });
    }
}
