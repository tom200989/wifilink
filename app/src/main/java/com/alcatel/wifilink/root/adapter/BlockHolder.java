package com.alcatel.wifilink.root.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

public class BlockHolder extends RecyclerView.ViewHolder {

    public Button unblockBtn;
    public ImageView icon;
    public TextView deviceName;
    public TextView mac;
    public EditText deviceNameEditView;
    public ImageView ivEdit;

    public BlockHolder(View convertView) {
        super(convertView);
        unblockBtn = (Button) convertView.findViewById(R.id.unblock_button);
        icon = (ImageView) convertView.findViewById(R.id.icon);
        deviceName = (TextView) convertView.findViewById(R.id.device_description);
        mac = (TextView) convertView.findViewById(R.id.mac);
        deviceNameEditView = (EditText) convertView.findViewById(R.id.device_description_editview);
        ivEdit = (ImageView) convertView.findViewById(R.id.edit_image);
    }
}
