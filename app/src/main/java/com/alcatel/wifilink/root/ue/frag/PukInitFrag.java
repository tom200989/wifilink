package com.alcatel.wifilink.root.ue.frag;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPukHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class PukInitFrag extends BaseFrag {

    @BindView(R.id.rl_puk_rx)
    PercentRelativeLayout rlPukRx;
    @BindView(R.id.et_puk_rx)
    EditText etPukRx;
    @BindView(R.id.tv_puk_rx_tip_num)
    TextView tvPukRxTipNum;
    @BindView(R.id.tv_puk_rx_tip_des)
    TextView tvPukRxTipDes;
    @BindView(R.id.et_puk_resetpin_rx)
    EditText etPukResetpinRx;
    @BindView(R.id.et_puk_resetpin_rx_confirm)
    EditText etPukResetpinRxConfirm;
    @BindView(R.id.iv_puk_remempin_rx_checkbox)
    ImageView ivPukRemempinRxCheckbox;
    @BindView(R.id.tv_puk_remempin_rx_checkbox)
    TextView tvPukRemempinRxCheckbox;
    @BindView(R.id.bt_puk_rx_unlock)
    Button btPukRxUnlock;
    @BindView(R.id.tv_puk_rx_alert)
    TextView tvPukRxAlert;

    private int red_color;
    private int gray_color;
    private Drawable check_pic;
    private Drawable uncheck_pic;
    private String pukTimeout_string;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_puk_init;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        initRes();
        initUi();
        initClick();
        getRemainTime();
    }

    /**
     * 获取PUK码剩余次数
     */
    private void getRemainTime() {
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
            int pukTime = getSimStatusBean.getPukRemainingTimes();
            tvPukRxTipNum.setText(String.valueOf(pukTime));
            if (pukTime < 3) {
                tvPukRxTipNum.setTextColor(red_color);
                tvPukRxTipDes.setTextColor(red_color);
                if (pukTime <= 1) {
                    tvPukRxAlert.setVisibility(View.VISIBLE);
                    if (pukTime == 0) {
                        // 设置所有的编辑域不可点
                        setAllEdittextDisable(etPukRx, etPukResetpinRx, etPukResetpinRxConfirm);
                        // 切换提示语
                        tvPukRxAlert.setVisibility(View.VISIBLE);
                        tvPukRxAlert.setText(pukTimeout_string);
                    }
                }
            } else {
                tvPukRxTipNum.setTextColor(gray_color);
                tvPukRxTipDes.setTextColor(gray_color);
            }
        });
        xGetSimStatusHelper.getSimStatus();
    }

    /**
     * 设置所有的编辑域不可点
     *
     * @param eds 编辑数组
     */
    private void setAllEdittextDisable(EditText... eds) {
        for (EditText ed : eds) {
            ed.setKeyListener(null);
            ed.setBackgroundColor(Color.GRAY);
        }
    }

    @Override
    public boolean isReloadData() {
        return true;
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        red_color = getRootColor(R.color.red);
        gray_color = getRootColor(R.color.gray);
        check_pic = getRootDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getRootDrawable(R.drawable.edit_normal);
        pukTimeout_string = getString(R.string.puk_alarm_des1);
    }

    /**
     * 初始化UI
     */
    private void initUi() {
        boolean isRememPin = ShareUtils.get(RootCons.PIN_INIT_IS_REMEM_PSD, false);
        String pinCache = ShareUtils.get(RootCons.PIN_INIT_REMEM_PSD, "");
        ivPukRemempinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPukResetpinRx.setText(isRememPin ? pinCache : "");
        etPukResetpinRx.setSelection(RootUtils.getEDText(etPukResetpinRx).length());
        etPukResetpinRxConfirm.setText(etPukResetpinRx.getText().toString());
        etPukResetpinRxConfirm.setSelection(RootUtils.getEDText(etPukResetpinRx).length());
    }

    /**
     * 点击
     */
    private void initClick() {
        // 记住PIN码
        ivPukRemempinRxCheckbox.setOnClickListener(v -> rememberPin());
        tvPukRemempinRxCheckbox.setOnClickListener(v -> rememberPin());
        // 解锁
        btPukRxUnlock.setOnClickListener(v -> clickPuk());
    }

    /**
     * 解锁
     */
    private void clickPuk() {
        // 空值判断
        String puk = RootUtils.getEDText(etPukRx);
        if (TextUtils.isEmpty(puk)) {
            toast(R.string.puk_empty, 5000);
            return;
        }

        String pin = RootUtils.getEDText(etPukResetpinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty, 5000);
            return;
        }
        String pinConfirm = RootUtils.getEDText(etPukResetpinRxConfirm);
        if (TextUtils.isEmpty(pinConfirm)) {
            toast(R.string.pin_confirm_empty, 5000);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters, 5000);
            return;
        }
        if (pinConfirm.length() < 4 | pinConfirm.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters, 5000);
            return;
        }
        // 匹配
        if (!pin.equalsIgnoreCase(pinConfirm)) {
            toast(R.string.puk_pinDontMatch, 5000);
            return;
        }
        // 发起请求
        commitRequest();
    }

    /**
     * 发起请求
     */
    private void commitRequest() {

        // 获取登陆状态
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                // 处于登陆 -- 则获取SIM状态
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                    int simState = getSimStatusBean.getSIMState();
                    switch (simState) {
                        case GetSimStatusBean.CONS_PIN_REQUIRED:
                            toFrag(getClass(), PinInitFrag.class, null, true);
                            break;

                        case GetSimStatusBean.CONS_PUK_REQUIRED:
                            unlockPukRequest();
                            break;

                        case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                            getRemainTime();
                            break;
                        case GetSimStatusBean.CONS_SIM_CARD_READY:
                            skip();
                            break;
                    }
                });
                xGetSimStatusHelper.getSimStatus();
            } else {
                toast(R.string.log_out, 5000);
                toFrag(getClass(), LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> toast(R.string.connect_failed, 5000));
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 跳转
     */
    private void skip() {
        // 是否进入过流量设置页
        boolean isDataPlanInit = ShareUtils.get(RootCons.SP_DATA_PLAN_INIT, false);
        boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);
        if (isDataPlanInit) {
            // 是否进入过wifi初始设置页
            if (isWifiInit) {
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            } else {
                checkWPS();// 检测WPS模式
            }
        } else {
            toFrag(getClass(), DataPlanInitFrag.class, null, true);
        }
    }

    /**
     * 检查WPS
     */
    private void checkWPS() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(isWPS -> {
            if (isWPS)
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            else
                toFrag(getClass(), WifiInitFrag.class, null, true);
        });
        wpsHelper.setOnGetWPSFailedListener(() ->  toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true));
        wpsHelper.getWpsStatus();
    }

    /**
     * 请求解PUK
     */
    private void unlockPukRequest() {
        String puk = RootUtils.getEDText(etPukRx);
        String pin = RootUtils.getEDText(etPukResetpinRx);
        UnlockPukHelper xUnlockPukHelper = new UnlockPukHelper();
        xUnlockPukHelper.setOnUnlockPukSuccessListener(() -> {
            // 是否勾选了记住PIN
            boolean isRememPin = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pins = RootUtils.getEDText(etPukResetpinRx);
                ShareUtils.set(RootCons.PIN_INIT_REMEM_PSD, pins);
                ShareUtils.set(RootCons.PIN_INIT_IS_REMEM_PSD, isRememPin);
            }
            // 进入其他界面
            skip();
        });
        xUnlockPukHelper.setOnUnlockPukFailedListener(() -> {
            etPukRx.setText("");
            toast(R.string.puk_error_waring_title, 5000);
            getRemainTime();
        });
        xUnlockPukHelper.unlockPuk(puk, pin);
    }

    /**
     * 记住PIN码
     */
    private void rememberPin() {
        Drawable checkBox = ivPukRemempinRxCheckbox.getDrawable() == check_pic ? uncheck_pic : check_pic;
        ivPukRemempinRxCheckbox.setImageDrawable(checkBox);
        String pin = RootUtils.getEDText(etPukResetpinRx);
        ShareUtils.set(RootCons.PIN_INIT_REMEM_PSD, ivPukRemempinRxCheckbox.getDrawable() == check_pic ? pin : "");
        ShareUtils.set(RootCons.PIN_INIT_IS_REMEM_PSD, ivPukRemempinRxCheckbox.getDrawable() == check_pic);
    }

    @Override
    public boolean onBackPresss() {
        if (lastFrag == WizardFrag.class) {
            toFrag(getClass(), WizardFrag.class, null, true);
        } else {
            LogoutHelper xLogouthelper = new LogoutHelper();
            xLogouthelper.setOnLogoutSuccessListener(() -> toFrag(getClass(), LoginFrag.class, null, true));
            xLogouthelper.setOnLogOutFailedListener(() -> toast(R.string.login_logout_failed, 5000));
            xLogouthelper.logout();
        }
        return true;
    }
}
