package com.alcatel.wifilink.root.ue.root_activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.CheckBoard;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.HH70_CheckWifiWidget;
import com.alcatel.wifilink.root.widget.RefreshWaitWidget;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TODO: 2019/8/17 0017 refreshFrag 
@Deprecated
public class RefreshWifiRxActivity extends Activity {

    @BindView(R.id.btn_refresh)
    Button btnRefresh;
    @BindView(R.id.rw_refresh_connecting)
    RefreshWaitWidget rwRefreshConnecting;
    @BindView(R.id.rw_refresh_get_conn)
    HH70_CheckWifiWidget rwRefreshGetConn;

    private CheckBoard checkBoard;
    private Handler handler;
    private String TAG = "RefreshWifiRxActivity";

    private final int MODE_ALL_HIDE = -1;
    private final int MODE_CONNECTING = 0;
    private final int MODE_GOT_CONNECT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.refreshrx_activity);
        ButterKnife.bind(this);
        handler = new Handler();
        // 点击事件
        rwRefreshGetConn.setOnRefreshClickOkListener(() -> {
            showOrHideWidget(MODE_ALL_HIDE);
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        stopTimer();// 停止定时器
        showOrHideWidget(MODE_CONNECTING);
        // 重新进入界面时检测硬件连接状态
        handler.postDelayed(this::checkBorads, 2000);
    }


    @OnClick(R.id.btn_refresh)
    public void onViewClicked() {
        checkBorads();
    }

    /**
     * 重新进入界面时检测硬件连接状态
     */
    private void checkBorads() {
        boolean iswifi = OtherUtils.isWifiConnect(this);
        Lgg.t(TAG).ii("is wifi open: " + iswifi);
        if (iswifi) {
            GetSystemInfoHelper xGetSystemInfoHelper = new GetSystemInfoHelper();
            xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(info -> {
                if (!isFinishing()) {
                    showOrHideWidget(MODE_GOT_CONNECT);
                }
                to(LoadingRxActivity.class, true);
            });
            xGetSystemInfoHelper.setOnFwErrorListener(() -> showOrHideWidget(MODE_GOT_CONNECT));
            xGetSystemInfoHelper.setOnAppErrorListener(() -> showOrHideWidget(MODE_GOT_CONNECT));
            xGetSystemInfoHelper.getSystemInfo();
        } else {
            showOrHideWidget(MODE_GOT_CONNECT);
            Lgg.t(TAG).ww("wifi is not open");
        }
    }

    /**
     * 根据模式显示\隐藏面板
     *
     * @param mode 模式
     */
    public void showOrHideWidget(int mode) {
        switch (mode) {
            case MODE_ALL_HIDE:
                rwRefreshConnecting.inVisibleNow();
                rwRefreshGetConn.setVisibility(View.GONE);
                break;
            case MODE_CONNECTING:
                rwRefreshConnecting.visibleNow();
                rwRefreshGetConn.setVisibility(View.GONE);
                break;
            case MODE_GOT_CONNECT:
                rwRefreshConnecting.inVisibleNow();
                rwRefreshGetConn.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 停止定时器
     */
    private void stopTimer() {
        OtherUtils.clearContexts(getClass().getSimpleName());
        OtherUtils.setWifiActive(this, true);
        OtherUtils.clearAllTimer();
        OtherUtils.stopHomeTimer();
        stopHomeHeart();
    }

    public void stopHomeHeart() {
        if (HomeRxActivity.heartTimer != null) {
            HomeRxActivity.heartTimer.stop();
        }
    }

    public void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(this, content);
    }

    public void to(Class ac, boolean isFinish) {
        CA.toActivity(this, ac, false, isFinish, false, 0);
    }
}
