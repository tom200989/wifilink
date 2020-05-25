package com.alcatel.wifilink.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.adapter.BillingAdapter;

import java.util.Arrays;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_BillWidget extends RelativeLayout {
    
    private View inflate;
    private Context context;
    private RecyclerView rcv;

    public HH70_BillWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_BillWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_BillWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        inflate = inflate(context, R.layout.hh70_widget_setplan_bill, this);
        ImageView ivBg = inflate.findViewById(R.id.iv_bill_bg);
        rcv = inflate.findViewById(R.id.rcv_pop_setPlan_rx_billing);
        ivBg.setOnClickListener(null);
    }

    public void setVisibles(String[] days) {
        setVisibility(VISIBLE);
        rcv.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        rcv.setAdapter(new BillingAdapter(context, Arrays.asList(days)) {
            @Override
            public void clickDay(int day) {
                // 提交请求
                setVisibility(GONE);
                clickBillItemNext(day);
            }
        });
    }

    private OnClickBillItemListener onClickBillItemListener;

    // Inteerface--> 接口OnClickBillItemListener
    public interface OnClickBillItemListener {
        void clickBillItem(int day);
    }

    // 对外方式setOnClickBillItemListener
    public void setOnClickBillItemListener(OnClickBillItemListener onClickBillItemListener) {
        this.onClickBillItemListener = onClickBillItemListener;
    }

    // 封装方法clickBillItemNext
    private void clickBillItemNext(int day) {
        if (onClickBillItemListener != null) {
            onClickBillItemListener.clickBillItem(day);
        }
    }
}
