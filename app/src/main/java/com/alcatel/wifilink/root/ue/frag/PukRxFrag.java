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
import com.alcatel.wifilink.root.ue.activity.SplashActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPukHelper;

import butterknife.BindView;

/**
 * Created by qianli.ma on 2017/11/21 0021.
 */

public class PukRxFrag extends BaseFrag {

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
    @BindView(R.id.rl_puk_rx)
    PercentRelativeLayout rlPukRx;

    private int red_color;
    private int gray_color;
    private Drawable check_pic;
    private Drawable uncheck_pic;
    private String pukTimeout_string;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_pinpukpuk;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        initRes();
        initUi();
        initOnClick();
        //onResume
        getRemainTime();
    }

    private void initRes() {
        red_color = getRootColor(R.color.red);
        gray_color = getRootColor(R.color.gray);
        check_pic = getRootDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getRootDrawable(R.drawable.general_btn_remember_nor);
        pukTimeout_string = getString(R.string.hh70_you_have_incorrect);
    }

    private void initUi() {
        boolean isRememPin = ShareUtils.get(RootCons.PIN_RX_IS_REMEM_PSD, false);
        String pinCache = ShareUtils.get(RootCons.PIN_RX_REMEM_PSD, "");
        ivPukRemempinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPukResetpinRx.setText(isRememPin ? pinCache : "");
        etPukResetpinRx.setSelection(RootUtils.getEDText(etPukResetpinRx).length());
        etPukResetpinRxConfirm.setText(etPukResetpinRx.getText().toString());
        etPukResetpinRxConfirm.setSelection(RootUtils.getEDText(etPukResetpinRx).length());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initUi();
            getRemainTime();
        }
    }

    /**
     * 获取PUK码剩余次数
     */
    private void getRemainTime() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(SplashActivity.class, LoginFrag.class);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(this::toShowRemain);
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                toast(R.string.hh70_sim_not_accessible, 2000);
                to(SplashActivity.class, RefreshFrag.class);
            });
            xGetSimStatusHelper.getSimStatus();
        });

        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
            toast(R.string.hh70_cant_connect, 2000);
            to(SplashActivity.class, RefreshFrag.class);
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 跳转activity
     */
    private void to(Class targetAc, Class targetFr) {
        toFragActivity(getClass(), targetAc, targetFr, null, false, true, 0);

    }

    /**
     * 显示超出限制的ui
     */
    private void toShowRemain(GetSimStatusBean simStatus) {
        int pukTime = simStatus.getPukRemainingTimes();
        tvPukRxTipNum.setText(String.valueOf(pukTime));
        if (pukTime < 3) {
            tvPukRxTipNum.setTextColor(red_color);
            tvPukRxTipDes.setTextColor(red_color);
            if (pukTime <= 1) {
                tvPukRxAlert.setVisibility(View.VISIBLE);
                if (pukTime == 0) {
                    // 设置所有的编辑域不可点
                    setEdNotFocus(etPukRx, etPukResetpinRx, etPukResetpinRxConfirm);
                    // 切换提示语
                    tvPukRxAlert.setVisibility(View.VISIBLE);
                    tvPukRxAlert.setText(pukTimeout_string);
                }
            }
        }
    }

    /**
     * 设置所有的编辑域不可点
     *
     * @param eds
     */
    public void setEdNotFocus(EditText... eds) {
        for (EditText ed : eds) {
            ed.setKeyListener(null);
            ed.setBackgroundColor(Color.GRAY);
        }
    }

    /**
     * 点击事件
     */
    private void initOnClick() {
        ivPukRemempinRxCheckbox.setOnClickListener(v -> tvPukRemempinRxCheckbox.performLongClick());
        tvPukRemempinRxCheckbox.setOnClickListener(v -> {
            boolean isCheck = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
            Drawable checkBox = isCheck ? uncheck_pic : check_pic;
            ivPukRemempinRxCheckbox.setImageDrawable(checkBox);
            String pin = RootUtils.getEDText(etPukResetpinRx);
            ShareUtils.set(RootCons.PIN_RX_REMEM_PSD, ivPukRemempinRxCheckbox.getDrawable() == check_pic ? pin : "");
            ShareUtils.set(RootCons.PIN_RX_IS_REMEM_PSD, ivPukRemempinRxCheckbox.getDrawable() == check_pic);
        });
        btPukRxUnlock.setOnClickListener(v -> unlockPukClick());
    }

    /**
     * 点击解PUK按钮行为
     */
    private void unlockPukClick() {
        // 空值判断
        String puk = RootUtils.getEDText(etPukRx);
        if (TextUtils.isEmpty(puk)) {
            toast(R.string.hh70_puk_empty, 2000);
            return;
        }

        String pin = RootUtils.getEDText(etPukResetpinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.hh70_pin_empty, 2000);
            return;
        }
        String pinConfirm = RootUtils.getEDText(etPukResetpinRxConfirm);
        if (TextUtils.isEmpty(pinConfirm)) {
            toast(R.string.hh70_pin_confirm_empty, 2000);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.hh70_the_pin_code_should_4, 2000);
            return;
        }
        if (pinConfirm.length() < 4 | pinConfirm.length() > 8) {
            toast(R.string.hh70_the_pin_code_should_4, 2000);
            return;
        }
        // 匹配
        if (!pin.equalsIgnoreCase(pinConfirm)) {
            toast(R.string.hh70_pin_dont_match, 2000);
            return;
        }
        // 发起请求
        getBoardAndSimStauts();
    }

    private void getBoardAndSimStauts() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGOUT) {
                to(SplashActivity.class, LoginFrag.class);
                return;
            }
            GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
            xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                int simState = result.getSIMState();
                switch (simState) {
                    case GetSimStatusBean.CONS_PIN_REQUIRED:
                        toPinRx();
                        break;
                    case GetSimStatusBean.CONS_PUK_REQUIRED:
                        unlockPukRequest();
                        break;
                    case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                        pukTimeout(result);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_READY:
                        toOtherRx();
                        break;
                    case GetSimStatusBean.CONS_NOWN:
                        toast(R.string.hh70_no_sim_insert,2000);
                        break;
                    case GetSimStatusBean.CONS_SIM_LOCK_REQUIRED:
                        toast(R.string.hh70_sim_locked,2000);
                        break;
                    case GetSimStatusBean.CONS_SIM_CARD_ILLEGAL:
                        toast(R.string.hh70_invalid_sim,2000);
                        break;
                }
            });
            xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                toast(R.string.hh70_sim_not_accessible, 2000);
                to(SplashActivity.class, RefreshFrag.class);
            });
            xGetSimStatusHelper.getSimStatus();
        });

        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
            toast(R.string.hh70_cant_connect, 2000);
            to(SplashActivity.class, RefreshFrag.class);
        });
        xGetLoginStateHelper.getLoginState();
    }

    private void pukTimeout(GetSimStatusBean result) {
        toShowRemain(result);
    }

    private void unlockPukRequest() {
        String puk = RootUtils.getEDText(etPukRx);
        String pin = RootUtils.getEDText(etPukResetpinRx);

        UnlockPukHelper xUnlockPukHelper = new UnlockPukHelper();
        xUnlockPukHelper.setOnUnlockPukSuccessListener(() -> {
            // 1.是否勾选了记住PIN
            boolean isRememPin = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pins = RootUtils.getEDText(etPukResetpinRx);
                ShareUtils.set(RootCons.PIN_RX_REMEM_PSD, pins);
                ShareUtils.set(RootCons.PIN_RX_IS_REMEM_PSD, isRememPin);
            }
            // 2.进入其他界面
            toOtherRx();
        });
        xUnlockPukHelper.setOnUnlockPukFailedListener(() -> {
            toast(R.string.hh70_cant_unlock_puk, 2000);
            getRemainTime();
        });
        xUnlockPukHelper.unlockPuk(puk, pin);
    }

    private void toOtherRx() {
        toFragment(mainFrag.class);
    }

    private void toPinRx() {
        toFrag(getClass(), PinRxFrag.class, null, true);
    }

    public void toFragment(Class clazz) {
        toFrag(getClass(), clazz, null, false);
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), mainFrag.class, null, false);
        return true;
    }
}
