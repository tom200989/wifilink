package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_MonthlyWidget extends RelativeLayout {

    private View inflate;
    private int blue_color;
    private int gray_color;

    public HH70_MonthlyWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_MonthlyWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_MonthlyWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        blue_color = context.getResources().getColor(R.color.mg_blue);
        gray_color = context.getResources().getColor(R.color.gray);
        inflate = inflate(context, R.layout.hh70_widget_setplan_monthly, this);
        ImageView ivBg = inflate.findViewById(R.id.iv_setPlan_bg);
        EditText etNum = inflate.findViewById(R.id.et_pop_setPlan_rx_monthly_num);
        TextView tvMb = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_mb);
        TextView tvGb = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_gb);
        TextView tvCancel = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_cancel);
        TextView tvOk = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_ok);
        ivBg.setOnClickListener(null);
        tvMb.setOnClickListener(v -> {
            tvMb.setTextColor(blue_color);
            tvGb.setTextColor(gray_color);
        });
        tvGb.setOnClickListener(v -> {
            tvMb.setTextColor(gray_color);
            tvGb.setTextColor(blue_color);
        });
        tvCancel.setOnClickListener(v -> setVisibility(GONE));
        tvOk.setOnClickListener(v -> {
            clickMonthlyOKNext(etNum, tvMb, tvGb);
            setVisibility(GONE);
        });
    }

    private OnClickMonthlyOKListener onClickMonthlyOKListener;

    // Inteerface--> 接口OnClickMonthlyOKListener
    public interface OnClickMonthlyOKListener {
        void clickMonthlyOK(EditText ed, TextView tvMB, TextView tvGB);
    }

    // 对外方式setOnClickMonthlyOKListener
    public void setOnClickMonthlyOKListener(OnClickMonthlyOKListener onClickMonthlyOKListener) {
        this.onClickMonthlyOKListener = onClickMonthlyOKListener;
    }

    // 封装方法clickMonthlyOKNext
    private void clickMonthlyOKNext(EditText ed, TextView tvMB, TextView tvGB) {
        if (onClickMonthlyOKListener != null) {
            onClickMonthlyOKListener.clickMonthlyOK(ed, tvMB, tvGB);
        }
    }
}
