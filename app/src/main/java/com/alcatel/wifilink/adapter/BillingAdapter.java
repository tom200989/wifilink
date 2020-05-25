package com.alcatel.wifilink.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;

import java.util.List;

/**
 * Created by qianli.ma on 2017/12/13 0013.
 */

public abstract class BillingAdapter extends RecyclerView.Adapter<BillingHolder> {

    private Context context;
    private List<String> days;

    public BillingAdapter(Context context, List<String> days) {
        this.context = context;
        this.days = days;
    }

    @NonNull
    @Override
    public BillingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BillingHolder(LayoutInflater.from(context).inflate(R.layout.hh70_item_setplan_billing, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BillingHolder holder, int position) {
        holder.tv_billing.setText(days.get(position));
        holder.tv_billing.setOnClickListener(v -> clickDay(position + 1));
    }

    @Override
    public int getItemCount() {
        return days != null ? days.size() : 0;
    }

    public abstract void clickDay(int day);
}
