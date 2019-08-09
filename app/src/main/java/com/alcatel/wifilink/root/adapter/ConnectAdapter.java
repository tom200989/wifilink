package com.alcatel.wifilink.root.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.bean.ConnectModel;
import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.root.helper.MacHelper;
import com.alcatel.wifilink.root.ue.activity.ActivityDeviceManager;
import com.alcatel.wifilink.root.ue.frag.DeviceConnectFragment;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetConnectedDeviceBlockHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetDeviceNameHelper;

import java.util.List;

public class ConnectAdapter extends RecyclerView.Adapter<ConnectHolder> {

    public ActivityDeviceManager activity;
    private DeviceConnectFragment fragment;
    private List<ConnectModel> connectModelList;
    String m_strEditString = new String();

    public ConnectAdapter(Activity activity, Fragment fragment, List<ConnectModel> connectModelList) {
        this.activity = (ActivityDeviceManager) activity;
        this.fragment = (DeviceConnectFragment) fragment;
        this.connectModelList = connectModelList;
    }

    public void notifys(List<ConnectModel> connectModelList) {
        this.connectModelList = connectModelList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return connectModelList != null ? connectModelList.size() : 0;
    }

    @Override
    public ConnectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ConnectHolder(LayoutInflater.from(activity).inflate(R.layout.device_manage_connected_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ConnectHolder holder, int position) {
        ConnectModel connectModel = connectModelList.get(position);
        ConnectedList.Device device = connectModel.device;

        final String displayName = device.DeviceName;
        holder.deviceNameTextView.setText(displayName);
        holder.ip.setText(String.format(activity.getString(R.string.device_manage_ip), device.IPAddress));
        String mac = device.MacAddress;
        holder.mac.setText(String.format(activity.getString(R.string.device_manage_mac), mac));
        final int type = device.DeviceType;

        // if (mac.equalsIgnoreCase(MacHelper.getLocalMacAddress(activity))) {
        /*  is the host  */
        if (MacHelper.isHost(activity, mac)) {
            holder.host.setVisibility(View.VISIBLE);
            holder.blockBtn.setVisibility(View.GONE);
            holder.deviceNameEditView.setVisibility(View.GONE);
            holder.deviceNameTextView.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.item_pre_phone);
            holder.modifyDeviceName.setVisibility(View.GONE);
        } else {/* not the host  */
            holder.host.setVisibility(View.GONE);
            holder.blockBtn.setVisibility(View.VISIBLE);
            holder.icon.setBackgroundResource(R.drawable.item_pre_phone);
            holder.modifyDeviceName.setVisibility(View.VISIBLE);
            if (connectModel.isEdit) {
                holder.deviceNameEditView.setVisibility(View.VISIBLE);
                holder.deviceNameEditView.setText(m_strEditString);
                holder.deviceNameEditView.requestFocus();
                int nSelection = m_strEditString.length();
                holder.deviceNameEditView.setSelection(nSelection);
                holder.deviceNameTextView.setVisibility(View.GONE);
                holder.modifyDeviceName.setBackgroundResource(R.drawable.connected_finished);
            } else {
                holder.deviceNameEditView.setVisibility(View.GONE);
                holder.deviceNameTextView.setVisibility(View.VISIBLE);
                holder.modifyDeviceName.setBackgroundResource(R.drawable.connected_edit);
            }
        }

        /* deviceNameEditView */
        holder.deviceNameEditView.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                String strText = arg0.toString();
                String strNewText = strText.replaceAll("[,\":;&]", "");
                if (!strNewText.equals(arg0.toString())) {
                    arg0 = arg0.replace(0, arg0.length(), strNewText);
                }
                m_strEditString = strNewText;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });

        /* modifyDeviceName */
        holder.modifyDeviceName.setOnClickListener(v -> {
            fragment.timerHelper.stop();
            if (connectModelList.get(position).isEdit) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                connectModelList.get(position).isEdit = false;
                String strName = m_strEditString.trim();
                if (strName.length() == 0 || strName.length() > 31) {
                    String msgRes = activity.getString(R.string.device_manage_name_empty);
                    Toast.makeText(activity, msgRes, Toast.LENGTH_SHORT).show();
                }
                if (strName.length() != 0 && !strName.equals(displayName)) {
                    setDeviceName(strName, mac, type);
                    connectModelList.get(position).device.DeviceName = strName;
                }
            } else {
                m_strEditString = connectModelList.get(position).device.DeviceName;
                connectModelList.get(position).isEdit = true;
            }

            // notify adapter
            notifys(connectModelList);
        });


        /* click the block button */
        holder.blockBtn.setOnClickListener(v -> setConnectedDeviceBlock(displayName, mac, position));

        /* deviceNameEditView */
        holder.deviceNameEditView.setOnEditorActionListener((v, actionId, event) -> {

            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                fragment.timerHelper.start(3000);
                String strName = v.getText().toString();

                if (!strName.equals(displayName)) {
                    setDeviceName(strName, mac, type);
                }
                connectModelList.get(position).isEdit = false;
                holder.modifyDeviceName.setBackgroundResource(R.drawable.connected_edit);
            }

            return false;
        });
    }

    /* setDeviceName */
    private void setDeviceName(String strDeviceName, String strMac, int nDeviceType) {
        SetDeviceNameParam setDeviceNameParam= new SetDeviceNameParam();
        setDeviceNameParam.setDeviceName(strDeviceName);
        setDeviceNameParam.setMacAddress(strMac);
        setDeviceNameParam.setDeviceType(nDeviceType);

        SetDeviceNameHelper xSetDeviceNameHelper = new SetDeviceNameHelper();
        xSetDeviceNameHelper.setDeviceName(setDeviceNameParam);
    }

    /* setConnectedDeviceBlock */
    private void setConnectedDeviceBlock(String strDeviceName, String strMac, int position) {
        SetConnectedDeviceBlockHelper xSetConnectedDeviceBlockHelper = new SetConnectedDeviceBlockHelper();
        xSetConnectedDeviceBlockHelper.setOnSetConnectDeviceBlockFailListener(() -> {
            int oldSize = connectModelList.size();
            // remove item
            connectModelList.remove(position);
            // refresh the blocked count in action bar
            activity.mblock.setText(activity.blockPre + (oldSize - connectModelList.size()) + activity.blockFix);
            // notify change
            notifys(connectModelList);
        });
        xSetConnectedDeviceBlockHelper.setOnSetConnectDeviceBlockFailListener(() -> {
            ToastUtil_m.show(activity, activity.getString(R.string.Set_connected_device_black_failed));
        });
        xSetConnectedDeviceBlockHelper.setConnectedDeviceBlock(strDeviceName, strMac);
    }
}
