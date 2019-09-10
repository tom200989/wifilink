package com.alcatel.wifilink.root.ue.frag;

import android.annotation.SuppressLint;
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
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPinHelper;

import butterknife.BindView;

public class PinRxFrag extends BaseFrag {
    // public class PinRxFragment extends Fragment  {

    @BindView(R.id.iv_pinrx_back)
    ImageView ivBack;
    @BindView(R.id.et_pin_rx)
    EditText etPinRx;
    @BindView(R.id.iv_pin_rx_deleted)
    ImageView ivPinRxDeleted;
    @BindView(R.id.tv_pin_rx_tip_num)
    TextView tvPinRxTipNum;
    @BindView(R.id.tv_pin_rx_tip_des)
    TextView tvPinRxTipDes;
    @BindView(R.id.iv_pin_rx_checkbox)
    ImageView ivPinRxCheckbox;
    @BindView(R.id.tv_pin_rx_checkbox)
    TextView tvPinRxCheckbox;
    @BindView(R.id.bt_pin_rx_unlock)
    Button btPinRxUnlock;

    private int red_color;
    private int gray_color;
    private Drawable check_pic;
    private Drawable uncheck_pic;
    private boolean isRussia;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_pinpukpin;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        initRes();
        initUi();
        initOnClick();
        getLanguage();
        getRemainTime();
    }

    private void getLanguage() {
        String language = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
        isRussia = language.contains(RootCons.LANGUAGES.RUSSIAN);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initUi();
            getRemainTime();
        }
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        red_color = getRootColor(R.color.red);
        gray_color = getRootColor(R.color.gray);
        check_pic = getRootDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getRootDrawable(R.drawable.general_btn_remember_nor);
    }

    /**
     * 初始化UI
     */
    private void initUi() {
        boolean isRememPin = ShareUtils.get(RootCons.PIN_RX_IS_REMEM_PSD, false);
        String pinCache = ShareUtils.get(RootCons.PIN_RX_REMEM_PSD, "");
        ivPinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPinRx.setText(isRememPin ? pinCache : "");
        etPinRx.setSelection(RootUtils.getEDText(etPinRx).length());
    }

    /**
     * 点击事件
     */
    private void initOnClick() {
        ivBack.setOnClickListener(v -> onBackPresss());
        ivPinRxDeleted.setOnClickListener(v -> etPinRx.setText(""));
        btPinRxUnlock.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(activity);
            unlockPin();
        });
        ivPinRxCheckbox.setOnClickListener(v -> tvPinRxCheckbox.performLongClick());
        tvPinRxCheckbox.setOnClickListener(v -> {
            boolean isCheck = ivPinRxCheckbox.getDrawable() == check_pic;// current is check
            ivPinRxCheckbox.setImageDrawable(isCheck ? uncheck_pic : check_pic);// modify to uncheck
            String pin = RootUtils.getEDText(etPinRx);
            ShareUtils.set(RootCons.PIN_RX_REMEM_PSD, ivPinRxCheckbox.getDrawable() == check_pic ? pin : "");
            ShareUtils.set(RootCons.PIN_RX_IS_REMEM_PSD, ivPinRxCheckbox.getDrawable() == check_pic);
        });
    }

    // /**
    //  * 获取PIN码剩余次数
    //  */
    // private void getRemainTime() {
    //     GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
    //     xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
    //         if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
    //             GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
    //             xGetSimStatusHelper.setOnGetSimStatusSuccessListener(this::toRemain);
    //             xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
    //                 toast(R.string.hh70_sim_not_accessible, 3000);
    //                 toFragActivity(getClass(), SplashActivity.class, RefreshFrag.class, null, true, true, 0);
    //             });
    //             xGetSimStatusHelper.getSimStatus();
    //         } else {
    //             toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true, true, 0);
    //         }
    //     });
    //     xGetLoginStateHelper.getLoginState();
    // }

    // /**
    //  * 处理剩余次数
    //  */
    // private void toRemain(GetSimStatusBean simStatus) {
    //     int pinTime = simStatus.getPinRemainingTimes();
    //     // pinTime = 0;//  测试Puk界面跳转,请将该代码注释
    //     tvPinRxTipNum.setText(String.valueOf(pinTime));
    //     if (pinTime < 3) {
    //         tvPinRxTipNum.setTextColor(red_color);
    //         tvPinRxTipDes.setTextColor(red_color);
    //         if (pinTime <= 0) {
    //             toPukRx();
    //         }
    //     }
    //
    // }

    /**
     * 解PIN
     */
    private void unlockPin() {
        // 空值判断
        String pin = RootUtils.getEDText(etPinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.hh70_pin_empty, 2000);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.hh70_the_pin_code_should_4, 2000);
            return;
        }
        // 请求
        getBoardAndSimStauts();
    }

    private void getBoardAndSimStauts() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
                    int simState = getSimStatusBean.getSIMState();
                    switch (simState) {
                        case GetSimStatusBean.CONS_PUK_REQUIRED:
                        case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                            toPukRx();
                            break;
                        case GetSimStatusBean.CONS_PIN_REQUIRED:
                            unlockPinRequest();
                            break;
                        case GetSimStatusBean.CONS_SIM_CARD_READY:
                            toOtherRx();
                            break;
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                    toast(R.string.hh70_sim_not_accessible, 3000);
                    toFragActivity(getClass(), SplashActivity.class, RefreshFrag.class, null, true, true, 0);
                });
                xGetSimStatusHelper.getSimStatus();
            } else {
                toFragActivity(getClass(), SplashActivity.class, LoginFrag.class, null, true, true, 0);
            }
        });
        xGetLoginStateHelper.getLoginState();
    }

    /**
     * 跳转
     */
    private void toOtherRx() {
        toFrag(getClass(), mainFrag.class, null, false);
    }

    /**
     * 前往puk fragment
     */
    private void toPukRx() {
        toFrag(getClass(), PukRxFrag.class, null, false);
    }

    /**
     * 发送解PIN请求
     */
    private void unlockPinRequest() {
        String pin = RootUtils.getEDText(etPinRx);
        UnlockPinHelper xUnlockPinHelper = new UnlockPinHelper();
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(this::getRemainTime);
        xUnlockPinHelper.setOnUnlockPinSuccessListener(() -> {
            // 是否勾选了记住PIN
            boolean isRememPin = ivPinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pins = RootUtils.getEDText(etPinRx);
                ShareUtils.set(RootCons.PIN_RX_REMEM_PSD, pins);
                ShareUtils.set(RootCons.PIN_RX_IS_REMEM_PSD, isRememPin);
            }
            // 进入其他界面
            toOtherRx();
        });
        xUnlockPinHelper.setOnUnlockPinFailedListener(() -> toast(R.string.hh70_cant_connect, 2000));
        xUnlockPinHelper.unlockPin(pin);
    }

    /**
     * 获取剩余次数
     */
    @SuppressLint("SetTextI18n")
    private void getRemainTime() {
        GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
        xGetSimStatusHelper.setOnGetSimStatusSuccessListener(getSimStatusBean -> {
            int remainTimes = getSimStatusBean.getPinRemainingTimes();
            // pinTime = 0;//  测试Puk界面跳转,请将该代码注释
            tvPinRxTipNum.setText(String.valueOf(remainTimes));
            if (remainTimes < 3) {
                if (remainTimes > 1) {
                    tvPinRxTipNum.setTextColor(red_color);
                    tvPinRxTipDes.setTextColor(red_color);
                    if (isRussia) {// 俄语 -- attemps remaing: 2
                        tvPinRxTipDes.setText(getString(R.string.hh70_attempts_remaing) + ": " + remainTimes);
                        tvPinRxTipNum.setVisibility(View.GONE);
                    } else {// 其他语言, 2 attemps remaing 
                        tvPinRxTipDes.setText(getString(R.string.hh70_attempts_remaing));
                        tvPinRxTipNum.setVisibility(View.VISIBLE);
                    }

                } else if (remainTimes == 1) {
                    tvPinRxTipNum.setVisibility(View.GONE);
                    tvPinRxTipDes.setTextColor(red_color);
                    tvPinRxTipDes.setText(getString(R.string.hh70_pin_remain));
                } else {
                    toFrag(getClass(), PukRxFrag.class, null, true);
                }
            } else {
                tvPinRxTipNum.setTextColor(gray_color);
                tvPinRxTipDes.setTextColor(gray_color);
                if (isRussia) {
                    String text = getString(R.string.hh70_attempts_remaing) + " " + remainTimes;
                    tvPinRxTipDes.setText(text);
                    tvPinRxTipNum.setVisibility(View.GONE);
                }
            }
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.hh70_cant_connect, 5000));
        xGetSimStatusHelper.getSimStatus();
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), mainFrag.class, null, false);
        return true;
    }
}
