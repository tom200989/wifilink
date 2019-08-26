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
import com.alcatel.wifilink.root.helper.BoardSimHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
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
    private BoardSimHelper boardSimHelper;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_pinpukpuk;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o,view,s);
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
        pukTimeout_string = getString(R.string.puk_alarm_des1);
    }

    private void initUi() {
        boolean isRememPin = ShareUtils.get(Cons.PIN_REMEM_FLAG_RX,false);
        String pinCache = ShareUtils.get(Cons.PIN_REMEM_STR_RX, "");
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
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnNormalSimstatusListener(this::toShowRemain);
        boardSimHelper.boardNormal();
    }

    /**
     * 显示超出限制的ui
     *
     * @param simStatus
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
    private void initOnClick(){
        ivPukRemempinRxCheckbox.setOnClickListener(v -> tvPukRemempinRxCheckbox.performLongClick());
        tvPukRemempinRxCheckbox.setOnClickListener(v -> {
            boolean isCheck = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
            Drawable checkBox = isCheck ? uncheck_pic : check_pic;
            ivPukRemempinRxCheckbox.setImageDrawable(checkBox);
            String pin = RootUtils.getEDText(etPukResetpinRx);
            ShareUtils.set(Cons.PIN_REMEM_STR_RX, ivPukRemempinRxCheckbox.getDrawable() == check_pic ? pin : "");
            ShareUtils.set(Cons.PIN_REMEM_FLAG_RX, ivPukRemempinRxCheckbox.getDrawable() == check_pic);
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
            toast(R.string.puk_empty,2000);
            return;
        }

        String pin = RootUtils.getEDText(etPukResetpinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty,2000);
            return;
        }
        String pinConfirm = RootUtils.getEDText(etPukResetpinRxConfirm);
        if (TextUtils.isEmpty(pinConfirm)) {
            toast(R.string.pin_confirm_empty,2000);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters,2000);
            return;
        }
        if (pinConfirm.length() < 4 | pinConfirm.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters,2000);
            return;
        }
        // 匹配
        if (!pin.equalsIgnoreCase(pinConfirm)) {
            toast(R.string.puk_pinDontMatch,2000);
            return;
        }
        // 发起请求
        getBoardAndSimStauts();
    }

    private void getBoardAndSimStauts() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnPinRequireListener(result -> toPinRx());
        boardSimHelper.setOnpukRequireListener(result -> unlockPukRequest());
        boardSimHelper.setOnpukTimeoutListener(this::pukTimeout);
        boardSimHelper.setOnSimReadyListener(result -> toOtherRx());
        boardSimHelper.boardNormal();
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
                ShareUtils.set(Cons.PIN_REMEM_STR_RX, pins);
                ShareUtils.set(Cons.PIN_REMEM_FLAG_RX, isRememPin);
            }
            // 2.进入其他界面
            toOtherRx();
        });
        xUnlockPukHelper.setOnUnlockPukFailedListener(() -> {
            toast(R.string.puk_unlock_failed,2000);
            getRemainTime();
        });
        xUnlockPukHelper.unlockPuk(puk, pin);
    }

    private void toOtherRx() {
        toFragment(mainFrag.class);
    }

    private void toPinRx() {
        toFrag(getClass(),PinRxFrag.class,null,true);
    }

    public void toFragment(Class clazz) {
        toFrag(getClass(),clazz,null,false);
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(),mainFrag.class,null,false);
        return true;
    }
}
