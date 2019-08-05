package com.alcatel.wifilink.root.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/2/6 0006
 */

public class WifiExtenderHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rl_wifiEx_all;
    public TextView tv_wifiEx_ssid;
    public ImageView iv_wifiEx_signal;
    public ImageView iv_wifiEx_password;

    public WifiExtenderHolder(View itemView) {
        super(itemView);
        rl_wifiEx_all = itemView.findViewById(R.id.rl_wifiExtender_item_all);
        tv_wifiEx_ssid = itemView.findViewById(R.id.tv_wifiExtender_item_hotDot_name);
        iv_wifiEx_signal = itemView.findViewById(R.id.iv_wifiExtender_item_wifi);
        iv_wifiEx_password = itemView.findViewById(R.id.iv_wifiExtender_item_lock);
    }
}
