package com.alcatel.wifilink.root.ue.activity;

import android.os.Bundle;
import android.widget.ImageView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.network.RX;
import com.alcatel.wifilink.network.ResponseBody;
import com.alcatel.wifilink.network.ResponseObject;
import com.alcatel.wifilink.root.bean.Other_PinPukBean;
import com.alcatel.wifilink.root.helper.CheckBoard;
import com.alcatel.wifilink.root.helper.LogoutHelper;
import com.alcatel.wifilink.root.ue.frag.PinPukPinFragment;
import com.alcatel.wifilink.root.ue.frag.PinPukPukFragment;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.FraHelpers;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 该类是用于登陆时进行PIN|PUK码的处理界面
 */
public class PinPukIndexRxActivity extends BaseActivityWithBack {

    @BindView(R.id.iv_pinpuk_index_rx_banner_back)
    ImageView ivBack;

    public Class[] allClass = {PinPukPinFragment.class, PinPukPukFragment.class};
    private int containId = R.id.fl_pinpuk_index_rx;
    public FraHelpers fraHelper;
    private TimerHelper heartTimer;
    private CheckBoard checkBoard;
    private String flag = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SmartLinkV3App.getContextInstance().add(this);
        setContentView(R.layout.activity_pin_puk_rx_index);
        ButterKnife.bind(this);
        containId = R.id.fl_pinpuk_index_rx;
        EventBus.getDefault().register(this);
        startHeartTimer();
    }

    @Override
    public void onBackPressed() {
        backClick();
    }

    private void startHeartTimer() {
        heartTimer = new TimerHelper(this) {
            @Override
            public void doSomething() {
                RX.getInstant().heartBeat(new ResponseObject() {
                    @Override
                    protected void onSuccess(Object result) {

                    }

                    @Override
                    protected void onResultError(ResponseBody.Error error) {
                        to(RefreshWifiRxActivity.class);
                    }

                    @Override
                    public void onError(Throwable e) {
                        to(RefreshWifiRxActivity.class);
                    }
                });
            }
        };
        heartTimer.start(3000);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void initUi(Other_PinPukBean pp) {
        Class clazz = pp.getFlag() == Cons.PIN_FLAG ? PinPukPinFragment.class : PinPukPukFragment.class;
        fraHelper = new FraHelpers(this, allClass, clazz, containId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getFlag(String flag) {
        this.flag = flag;
    }

    @OnClick(R.id.iv_pinpuk_index_rx_banner_back)
    public void onViewClicked() {
        backClick();
    }

    /**
     * 点击了回退键
     */
    private void backClick() {
        if (Cons.WIZARD_RX.equalsIgnoreCase(flag)) {
            to(WizardRxActivity.class);
        } else {
            new LogoutHelper(this) {
                @Override
                public void logoutFinish() {
                    to(LoginRxActivity.class);
                }
            };
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        fraHelper = null;
        heartTimer.stop();
    }

    public void to(Class ac) {
        CA.toActivity(this, ac, false, true, false, 0);
    }

}
