package com.alcatel.wifilink.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/5/21 0021.
 */

public class LoginDevicesHolder extends RecyclerView.ViewHolder {

    public ImageView ivLogo;// 手機或電腦
    public TextView tvTitle;// 標題
    public TextView tvIp;// IP地址
    public TextView tvMac;// MAC地址
    public TextView tvHost;// 主機名

    public LoginDevicesHolder(View itemView) {
        super(itemView);
        ivLogo = itemView.findViewById(R.id.iv_pre_devices_item_logo);
        tvTitle = itemView.findViewById(R.id.tv_pre_devices_item_title);
        tvIp = itemView.findViewById(R.id.tv_pre_devices_item_ip);
        tvMac = itemView.findViewById(R.id.tv_pre_devices_item_macAddress);
        tvHost = itemView.findViewById(R.id.tv_pre_devices_item_host);
    }
}
