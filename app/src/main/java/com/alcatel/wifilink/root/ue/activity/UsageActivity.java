package com.alcatel.wifilink.root.ue.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.utils.ActionbarSetting;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.C_CommonUtil;
import com.alcatel.wifilink.root.utils.DataUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageRecordBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetUsageRecordHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageRecordClearHelper;

import static com.alcatel.wifilink.R.id.tv_network_settings;


public class UsageActivity extends BaseActivityWithBack implements View.OnClickListener {

    private Context mContext;
    private TextView mHomeData;
    private TextView mTvHomeTime;
    private TextView mRoamingData;
    private TextView mTvRoamingTime;
    private Button mBtReset;
    private TextView mTvNetworkSettings;

    private ImageButton mbackBtn;
    private Button mMoreBtn;
    private TimerHelper timerHelper;
    // private boolean mIsLimited;

    public UsageActivity() {
        this.mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_usage);
        // init action bar
        //        initActionBar();
        // init layout ui
        initView();
        // init status
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTimer();
    }


    /**
     * 接收定时器接口
     */
    private void initTimer() {
        timerHelper = new TimerHelper(this) {
            @Override
            public void doSomething() {
                updateUI();
            }
        };
        timerHelper.start(5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHelper.stop();
    }

    /**
     * 初始化Actionbar
     */
    private void initActionBar() {
        // initView action bar
        new ActionbarSetting() {
            @Override
            public void findActionbarView(View view) {
                mbackBtn = (ImageButton) view.findViewById(R.id.usage_back);
                mMoreBtn = (Button) view.findViewById(R.id.usage_more);
                mbackBtn.setOnClickListener(UsageActivity.this);
                mMoreBtn.setVisibility(View.GONE);
            }
        }.settingActionbarAttr(this, getSupportActionBar(), R.layout.actionbarusage);
    }

    protected void initView() {
        mHomeData = (TextView) findViewById(R.id.home_data);
        mTvHomeTime = (TextView) findViewById(R.id.tv_home_time);
        mRoamingData = (TextView) findViewById(R.id.roaming_data);
        mTvRoamingTime = (TextView) findViewById(R.id.tv_roaming_time);
        mBtReset = (Button) findViewById(R.id.bt_reset);
        mTvNetworkSettings = (TextView) findViewById(tv_network_settings);
        mBtReset.setOnClickListener(this);
        mTvNetworkSettings.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.usage_back:
                finish();
                break;
            case R.id.bt_reset:
                resetDataUsage();
                break;
            case tv_network_settings:
                goToMobileNetworkSettingPage();
                break;
            default:
                break;
        }
    }

    /* **** resetDataUsage **** */
    private void resetDataUsage() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(R.string.reset_monthly_data_usage_statistics);
        dialog.setNegativeButton(R.string.cancel, (dialog1, which) -> dialog1.dismiss());
        dialog.setPositiveButton(R.string.reset, (dialog1, which) -> {
            SetUsageRecordClearHelper xSetUsageRecordClearHelper = new SetUsageRecordClearHelper();
            xSetUsageRecordClearHelper.setOnSetUsageRecordClearSuccessListener(this::updateUI);
            xSetUsageRecordClearHelper.setUsageRecordClear(DataUtils.getCurrent());
        });
        dialog.show();
    }


    /* update ui */
    private void updateUI() {
        getUsageRecord();
    }

    /* **** goToMobileNetworkSettingPage **** */
    private void goToMobileNetworkSettingPage() {
        CA.toActivity(this, SettingNetworkActivity.class, false, true, false, 0);
    }

    /* **** getUsageRecord **** */
    private void getUsageRecord() {
        GetUsageRecordHelper xGetUsageRecordHelper = new GetUsageRecordHelper();
        xGetUsageRecordHelper.setOnGetUsageRecordSuccess(bean -> updateUsageRecord(bean));
        xGetUsageRecordHelper.getUsageRecord(DataUtils.getCurrent());
    }

    /* **** updateUsageRecord **** */
    private void updateUsageRecord(GetUsageRecordBean result) {
        long useData = result.getHUseData();
        mHomeData.setText(C_CommonUtil.ConvertTrafficMB(this, useData));
        mRoamingData.setText(C_CommonUtil.ConvertTrafficMB(this, (long) result.getRoamUseData()));

        String durationformat = getResources().getString(R.string.usage_duration);
        long connectTime = result.getCurrConnTimes();
        long totalTime = result.getTConnTimes();
        String strCurrDuration = String.format(durationformat, totalTime / 3600, (totalTime % 3600) / 60);
        mTvHomeTime.setText(strCurrDuration);

        GetNetworkInfoHelper xGetNetworkInfoHelper = new GetNetworkInfoHelper();
        xGetNetworkInfoHelper.setOnGetNetworkInfoSuccessListener(networkInfoBean -> {
            //0: roaming   1: no roaming
            if (networkInfoBean.getRoaming() == 0) {
                String strTotalDuration = String.format(durationformat, connectTime / 3600, (connectTime % 3600) / 60);
                mTvRoamingTime.setText(strTotalDuration);
            } else {
                String strTotalDuration = String.format(durationformat, 0, 0);
                mTvRoamingTime.setText(strTotalDuration);
            }
        });
        xGetNetworkInfoHelper.getNetworkInfo();

    }
}

