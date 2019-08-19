package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/17 0017.
 */
public class HH70_DataUnitWidget extends RelativeLayout {

    private View inflate;
    private TextView tvMB;
    private TextView tvGB;

    private int check_color;
    private int uncheck_color;
    private RelativeLayout rlBg;

    public HH70_DataUnitWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_DataUnitWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_DataUnitWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        check_color = getResources().getColor(R.color.mg_blue);
        uncheck_color = getResources().getColor(R.color.gray);
        inflate = inflate(context, R.layout.pop_dataplan_rx, this);
        rlBg = inflate.findViewById(R.id.rl_dataplan_bg);
        tvMB = inflate.findViewById(R.id.tv_dataplan_rx_pop_mb);
        tvGB = inflate.findViewById(R.id.tv_dataplan_rx_pop_gb);
        initClick();
    }

    /**
     * 初始化点击
     */
    private void initClick() {
        rlBg.setOnClickListener(v -> setVisibility(GONE));
        tvMB.setOnClickListener(v -> clickUnitNext(DATAUNIT.MB.unit));
        tvGB.setOnClickListener(v -> clickUnitNext(DATAUNIT.GB.unit));
    }

    /**
     * 设置字体颜色
     *
     * @param DATAUNIT 单位
     */
    public void setCheck(DATAUNIT DATAUNIT) {
        tvMB.setTextColor(DATAUNIT == HH70_DataUnitWidget.DATAUNIT.MB ? check_color : uncheck_color);
        tvGB.setTextColor(DATAUNIT == HH70_DataUnitWidget.DATAUNIT.GB ? check_color : uncheck_color);
    }

    /**
     * 单位
     */
    public enum DATAUNIT {

        MB(0), GB(1);

        public int unit;

        DATAUNIT(int unit) {
            this.unit = unit;
        }
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnClickUnitListener onClickUnitListener;

    // Inteerface--> 接口OnClickUnitListener
    public interface OnClickUnitListener {
        void clickUnit(int unit);
    }

    // 对外方式setOnClickUnitListener
    public void setOnClickUnitListener(OnClickUnitListener onClickUnitListener) {
        this.onClickUnitListener = onClickUnitListener;
    }

    // 封装方法clickUnitNext
    private void clickUnitNext(int unit) {
        if (onClickUnitListener != null) {
            onClickUnitListener.clickUnit(unit);
        }
    }
}
