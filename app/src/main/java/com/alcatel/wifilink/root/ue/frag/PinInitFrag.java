package com.alcatel.wifilink.root.ue.frag;

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
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPinHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class PinInitFrag extends BaseFrag {

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
    private boolean isRussia;// 是否为俄语
    private boolean isTaiwan;// 是否为台湾

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_pin_init;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        getLanguage();// 获取当前语言环境
        initRes();
        initUi();
        initClick();
        getRemainTime();// 获取剩余次数
    }

    /**
     * 获取剩余次数
     */
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
                    if (isRussia) {
                        tvPinRxTipDes.setText(getString(R.string.sim_unlocked_attempts));
                    } else {
                        String text = getString(R.string.sim_unlocked_attempts) + " " + remainTimes;
                        tvPinRxTipDes.setText(text);
                    }
                    if (isTaiwan) {
                        tvPinRxTipNum.setVisibility(View.GONE);
                    }

                } else if (remainTimes == 1) {
                    tvPinRxTipNum.setVisibility(View.GONE);
                    tvPinRxTipDes.setTextColor(red_color);
                    tvPinRxTipDes.setText(getString(R.string.ergo_20181010_pin_remained));
                } else {
                    toFrag(getClass(), PukInitFrag.class, null, true);
                }
            } else {
                tvPinRxTipNum.setTextColor(gray_color);
                tvPinRxTipDes.setTextColor(gray_color);
                if (isRussia) {
                    String text = getString(R.string.sim_unlocked_attempts) + " " + remainTimes;
                    tvPinRxTipDes.setText(text);
                }
            }
        });
        xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.connect_failed, 5000));
        xGetSimStatusHelper.getSimStatus();
    }

    @Override
    public boolean isReloadData() {
        return true;
    }

    /**
     * 获取当前语言环境
     */
    private void getLanguage() {
        String language = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
        isRussia = language.contains(RootCons.LANGUAGES.RUSSIAN);
        isTaiwan = language.contains(RootCons.LANGUAGES.CHINA);
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        red_color = getResources().getColor(R.color.red);
        gray_color = getResources().getColor(R.color.gray);
        check_pic = getResources().getDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getResources().getDrawable(R.drawable.edit_normal);
    }

    /**
     * 初始化UI
     */
    private void initUi() {
        boolean isRememPin = ShareUtils.get(RootCons.PIN_INIT_IS_REMEM_PSD, false);
        String pinCache = ShareUtils.get(RootCons.PIN_INIT_REMEM_PSD, "");
        ivPinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPinRx.setText(isRememPin ? pinCache : "");
        etPinRx.setSelection(RootUtils.getEDText(etPinRx).length());
        tvPinRxTipNum.setVisibility(isRussia ? View.GONE : View.VISIBLE);// 适配俄语
    }

    /**
     * 点击
     */
    private void initClick() {
        // 清除
        ivPinRxDeleted.setOnClickListener(v -> etPinRx.setText(""));
        // 解锁
        btPinRxUnlock.setOnClickListener(v -> unlockPin());
        // 记住PIN码
        ivPinRxCheckbox.setOnClickListener(v -> savePin());
        tvPinRxCheckbox.setOnClickListener(v -> savePin());
    }

    /**
     * 记住PIN码
     */
    private void savePin() {
        boolean isCheck = ivPinRxCheckbox.getDrawable() == check_pic;
        ivPinRxCheckbox.setImageDrawable(isCheck ? uncheck_pic : check_pic);
        String pin = RootUtils.getEDText(etPinRx);
        ShareUtils.set(RootCons.PIN_INIT_REMEM_PSD, ivPinRxCheckbox.getDrawable() == check_pic ? pin : "");
        ShareUtils.set(RootCons.PIN_INIT_IS_REMEM_PSD, ivPinRxCheckbox.getDrawable() == check_pic);
    }

    /**
     * 解PIN
     */
    private void unlockPin() {
        // 空值判断
        String pin = RootUtils.getEDText(etPinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty, 5000);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters, 5000);
            return;
        }
        // 提交请求
        commitUnlock();
    }

    /**
     * 提交请求
     */
    private void commitUnlock() {
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
                            unlockPinRequest();
                            break;
                        case GetSimStatusBean.CONS_PUK_REQUIRED:
                        case GetSimStatusBean.CONS_PUK_TIMES_USED_OUT:
                            toFrag(getClass(), PukInitFrag.class, null, true);
                            break;
                        case GetSimStatusBean.CONS_SIM_CARD_READY:
                            skip();
                            break;
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> toast(R.string.connect_failed, 5000));
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
     * 发送解PIN请求
     */
    private void unlockPinRequest() {
        String pin = RootUtils.getEDText(etPinRx);
        UnlockPinHelper xUnlockPinHelper = new UnlockPinHelper();
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(() -> {
            // 是否勾选了记住PIN
            boolean isRememPin = ivPinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pinText = RootUtils.getEDText(etPinRx);
                ShareUtils.set(RootCons.PIN_INIT_REMEM_PSD, pinText);
                ShareUtils.set(RootCons.PIN_INIT_IS_REMEM_PSD, isRememPin);
            }
            skip();// 跳转
        });
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(() -> {
            etPinRx.setText("");
            toast(R.string.pin_error_waring_title, 5000);
            getRemainTime();
        });
        xUnlockPinHelper.unlockPin(pin);
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
                checkWps();// 检测是否开启了WPS模式
            }
        } else {
            toFrag(getClass(), DataPlanInitFrag.class, null, true);
        }
    }

    /**
     * 检测是否开启了WPS模式
     */
    private void checkWps() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(isWPS -> {
            if (isWPS)
                toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
            else
                toFrag(getClass(), WifiInitFrag.class, null, true);
        });
        wpsHelper.setOnGetWPSFailedListener(() -> toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true));
        wpsHelper.getWpsStatus();
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
