package com.alcatel.wifilink.root.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.Other_DeviceBean;

import java.util.List;

/**
 * Created by qianli.ma on 2018/5/21 0021.
 */

public class LoginMWDevAdapter extends RecyclerView.Adapter<LoginDevicesHolder> {

    private Context context;
    private List<Other_DeviceBean> dbs;

    public LoginMWDevAdapter(Context context, List<Other_DeviceBean> dbs) {
        this.context = context;
        this.dbs = dbs;
    }

    public void notifys(List<Other_DeviceBean> dbs) {
        this.dbs = dbs;
        notifyDataSetChanged();
    }

    @Override
    public LoginDevicesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LoginDevicesHolder(LayoutInflater.from(context).inflate(R.layout.item_pre_devices, parent, false));
    }

    @Override
    public void onBindViewHolder(LoginDevicesHolder holder, int position) {
        Drawable phoneLogo = context.getResources().getDrawable(R.drawable.item_pre_phone);
        Drawable computerLogo = context.getResources().getDrawable(R.drawable.item_pre_computer);

        if (dbs != null) {
            Other_DeviceBean db = dbs.get(position);
            holder.ivLogo.setImageDrawable(db.isPhone() ? phoneLogo : computerLogo);
            holder.tvTitle.setText(db.getDeviceName());
            holder.tvIp.setText(db.getDeviceIP());
            holder.tvMac.setText(db.getDeviceMac());
            holder.tvHost.setVisibility(db.isHost() ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return dbs != null ? dbs.size() : 0;
    }
}
