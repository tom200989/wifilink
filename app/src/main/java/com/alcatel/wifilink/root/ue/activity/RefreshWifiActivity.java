package com.alcatel.wifilink.root.ue.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.User_LoginState;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.utils.OtherUtils;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RefreshWifiActivity extends AppCompatActivity {

    @BindView(R.id.iv_detect_logo)
    ImageView iv_logo;
    @BindView(R.id.tv_tip)
    TextView tv_noDevice;
    @BindView(R.id.pb_refreshing)
    ProgressBar pg;
    @BindView(R.id.btn_refresh)
    Button bt_refresh;

    ProgressDialog pdDialog;// wait dialog
    AlertDialog connectDialog;// connect dialog
    Activity refreshContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh);
        ButterKnife.bind(this);
        pdDialog = new ProgressDialog(this);
        pdDialog.setCanceledOnTouchOutside(false);
        refreshContext = this;
    }

    @Override
    protected void onResume() {
        super.onResume();
        OtherUtils.setWifiActive(this, true);
        OtherUtils.stopHomeTimer();
        OtherUtils.clearContexts(getClass().getSimpleName());
        initDate();
        logout();
    }

    private void logout() {
        RX.getInstant().logout(new ResponseObject() {
            @Override
            protected void onSuccess(Object result) {
                
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                
            }

            @Override
            public void onError(Throwable e) {
                
            }
        });
    }

    private void initDate() {
        checkBoardIsConn();
    }

    @OnClick(R.id.btn_refresh)
    public void refreshClick() {
        initDate();
    }

    /* -------------------------------------------- helper -------------------------------------------- */

    /**
     * 显示连接失败对话框
     */
    public void showConnDialog() {
        if (connectDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.refresh_get_connected);
            builder.setMessage(R.string.refresh_manage_device_tips);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                // 前往wifi选择界面
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            });
            connectDialog = builder.create();
            connectDialog.setOnDismissListener(dialog -> connectDialog = null);
            connectDialog.show();
        }
    }

    /**
     * 检测路由器端口
     */
    private void checkBoardIsConn() {
        // 1.检测接口是否有数据返回--> success连接正确 failed连接不正确
        RX.getInstant().getLoginState(new ResponseObject<User_LoginState>() {

            @Override
            public void onStart() {
                pdDialog.setMessage(getString(R.string.connecting));
                pdDialog.show();
            }

            @Override
            protected void onSuccess(User_LoginState result) {
                pdDialog.dismiss();
                // CA.toActivity(refreshContext, LoginActivity.class, false, true, false, 0);
            }

            @Override
            public void onError(Throwable e) {
                pdDialog.dismiss();
                showConnDialog();
                if (e instanceof SocketTimeoutException) {/* 连接超时 */
                    // to RefreshWifiActivity
                } else if (e instanceof ConnectException) {
                    // to RefreshWifiActivity
                } else {
                }
            }

            @Override
            protected void onResultError(ResponseBody.Error error) {
                pdDialog.dismiss();
            }
        });
    }


}
