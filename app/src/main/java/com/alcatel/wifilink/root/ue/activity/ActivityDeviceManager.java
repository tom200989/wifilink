package com.alcatel.wifilink.root.ue.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.FraDeviceHelper;
import com.alcatel.wifilink.root.helper.FragmentDeviceEnum;
import com.alcatel.wifilink.root.utils.ActionbarSetting;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetBlockDeviceListHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityDeviceManager extends BaseActivityWithBack implements OnClickListener {

    public String blockPre;
    public String blockFix = ")";
    public int blockSize;

    @BindView(R.id.fl_device)
    FrameLayout flDevice;

    public int resid = R.id.fl_device;

    public ImageButton mbackBtn;
    public TextView mblock;
    private TextView mTitle;
    public FragmentManager fm;

    public FragmentDeviceEnum tempEn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_manage_view);
        ButterKnife.bind(this);
        fm = getSupportFragmentManager();
        // init
        init();
        // init action bar
        initActionbar();
        // init fragment 
        toFragment(FragmentDeviceEnum.CONNECT);
        // getInstant block count
        getblockCount();
    }

    private void init() {
        blockPre = getString(R.string.Blocked) + " (";
    }

    /* **** getblockCount **** */
    private void getblockCount() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            blockSize = getBlockDeviceListBean.getBlockList().size();
            mblock.setText(String.valueOf(blockPre + blockSize + blockFix));
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    /* **** initActionbar **** */
    private void initActionbar() {
        new ActionbarSetting() {
            @Override
            public void findActionbarView(View view) {
                // back button
                mbackBtn = view.findViewById(R.id.device_back);
                mbackBtn.setOnClickListener(ActivityDeviceManager.this);
                // block
                mblock = view.findViewById(R.id.device_block);
                mblock.setOnClickListener(ActivityDeviceManager.this);
                mblock.setText(String.valueOf(blockPre + "0" + blockFix));
                
                // title
                mTitle = view.findViewById(R.id.device_title);
                mTitle.setText(getString(R.string.ergo_20181010_connections_low));
                
                // 俄语文字大小适配
                String currentLanguage = OtherUtils.getCurrentLanguage();
                if (currentLanguage.equalsIgnoreCase("ru")) {
                    mblock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                    mTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                }

            }
        }.settingActionbarAttr(this, getSupportActionBar(), R.layout.actionbardevices);
    }

    public void onClick(View arg0) {
        switch (arg0.getId()) {

            /* back button */
            case R.id.device_back:
                if (tempEn == FragmentDeviceEnum.CONNECT) {
                    finish();
                } else {
                    toFragment(FragmentDeviceEnum.CONNECT);
                }
                break;

            /* block button */
            case R.id.device_block:
                checkBlockList();
                break;
        }
    }

    private void checkBlockList() {
        GetBlockDeviceListHelper xGetBlockDeviceListHelper = new GetBlockDeviceListHelper();
        xGetBlockDeviceListHelper.setonGetBlockDeviceListSuccessListener(getBlockDeviceListBean -> {
            if (getBlockDeviceListBean.getBlockList().size() > 0) {
                toFragment(FragmentDeviceEnum.BLOCK);
            }
        });
        xGetBlockDeviceListHelper.getBlockDeviceList();
    }

    // helper --> to which fragment
    public void toFragment(FragmentDeviceEnum en) {
        runOnUiThread(() -> {
            if (en == FragmentDeviceEnum.CONNECT) {
                mblock.setVisibility(View.VISIBLE);
                mTitle.setText(getString(R.string.ergo_20181010_connections_low));
                tempEn = FragmentDeviceEnum.CONNECT;
                FraDeviceHelper.commit(this, fm, resid, tempEn);
            } else {
                mblock.setVisibility(View.GONE);
                mTitle.setText(getString(R.string.Blocked));
                tempEn = FragmentDeviceEnum.BLOCK;
                FraDeviceHelper.commit(this, fm, resid, tempEn);
            }
        });
    }
}
