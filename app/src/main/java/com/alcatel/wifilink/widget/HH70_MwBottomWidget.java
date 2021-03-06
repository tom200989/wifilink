package com.alcatel.wifilink.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/6/1 0001.
 */

public class HH70_MwBottomWidget extends RelativeLayout {

    private LinearLayout rlAll;
    private RelativeLayout rlConnected;
    private TextView tvConnectedNum;
    private RelativeLayout rlFreeSharing;

    public HH70_MwBottomWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_MwBottomWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_MwBottomWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_mw_bottom, this);
        rlAll = findViewById(R.id.rl_main_mw70_all);
        rlConnected = findViewById(R.id.rl_main_mw70_connected);
        tvConnectedNum = findViewById(R.id.tv_main_mw70_connected_deviceNum);
        rlFreeSharing = findViewById(R.id.rl_main_mw70_freesharing);
        rlAll.setOnClickListener(null);
        rlConnected.setOnClickListener(v -> clickConnectedNext(null));
        rlFreeSharing.setOnClickListener(v -> clickFreeSharingNext(null));
    }

    /**
     * 设置设备数
     */
    public void setDevicesNum(int num) {
        tvConnectedNum.setText(String.valueOf(num));
    }

    private OnClickFreeSharingListener onClickFreeSharingListener;

    // 接口OnClickFreeSharingListener
    public interface OnClickFreeSharingListener {
        void clickFreeSharing(Object o);
    }

    // 对外方式setOnClickFreeSharingListener
    public void setOnClickFreeSharingListener(OnClickFreeSharingListener onClickFreeSharingListener) {
        this.onClickFreeSharingListener = onClickFreeSharingListener;
    }

    // 封装方法clickFreeSharingNext
    private void clickFreeSharingNext(Object attr) {
        if (onClickFreeSharingListener != null) {
            onClickFreeSharingListener.clickFreeSharing(attr);
        }
    }

    private OnClickConnectedListener onClickConnectedListener;

    // 接口OnClickConnectedListener
    public interface OnClickConnectedListener {
        void clickConnected(Object o);
    }

    // 对外方式setOnClickConnectedListener
    public void setOnClickConnectedListener(OnClickConnectedListener onClickConnectedListener) {
        this.onClickConnectedListener = onClickConnectedListener;
    }

    // 封装方法clickNext
    private void clickConnectedNext(Object attr) {
        if (onClickConnectedListener != null) {
            onClickConnectedListener.clickConnected(attr);
        }
    }
}
