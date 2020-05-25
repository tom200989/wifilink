package com.alcatel.wifilink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.bean.Extender_GetHotspotListBean;
import com.alcatel.wifilink.utils.RootUtils;

import java.util.List;

/**
 * Created by qianli.ma on 2018/2/6 0006.
 */

public class WifiExtenderAdapter extends RecyclerView.Adapter<WifiExtenderHolder> {

    private Context context;
    private List<Extender_GetHotspotListBean.HotspotListBean> hotspotList;

    public WifiExtenderAdapter(Context context, List<Extender_GetHotspotListBean.HotspotListBean> hotspotList) {
        this.context = context;
        this.hotspotList = hotspotList;
    }

    public void notifys(List<Extender_GetHotspotListBean.HotspotListBean> hotspotList) {
        this.hotspotList = hotspotList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public WifiExtenderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WifiExtenderHolder(LayoutInflater.from(context).inflate(R.layout.hh70_item_wifi_extender, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WifiExtenderHolder holder, int position) {
        
        int NO_SECURITY = 0;//No Security
        int WEP = 1;//WEP
        int WPA = 2;//WPA
        int WPA2 = 3;// WPA2
        int WPA_WPA2 = 4;// WPA/WPA2
        
        Extender_GetHotspotListBean.HotspotListBean hb = hotspotList.get(position);
        try {
            holder.tv_wifiEx_ssid.setText(hb.getSSID());
            holder.iv_wifiEx_password.setVisibility(hb.getSecurityMode() != NO_SECURITY ? View.VISIBLE : View.GONE);
            holder.iv_wifiEx_signal.setImageDrawable(RootUtils.transferWifiExtenderSignal(context,hb.getSignal()));
            holder.rl_wifiEx_all.setOnClickListener(v -> clickNext(hb));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return hotspotList != null ? hotspotList.size() : 0;
    }

    private OnClickHotPotListener onClickHotPotListener;

    // 接口OnClickHotPotListener
    public interface OnClickHotPotListener {
        void click(Extender_GetHotspotListBean.HotspotListBean hotspotListBean);
    }

    // 对外方式setOnClickHotPotListener
    public void setOnClickHotPotListener(OnClickHotPotListener onClickHotPotListener) {
        this.onClickHotPotListener = onClickHotPotListener;
    }

    // 封装方法clickNext
    private void clickNext(Extender_GetHotspotListBean.HotspotListBean hotspotListBean) {
        if (onClickHotPotListener != null) {
            onClickHotPotListener.click(hotspotListBean);
        }
    }
}
