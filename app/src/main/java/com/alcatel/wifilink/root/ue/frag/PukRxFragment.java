package com.alcatel.wifilink.root.ue.frag;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.BoardSimHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.ue.activity.HomeRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPukHelper;
import com.zhy.android.percent.support.PercentRelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qianli.ma on 2017/11/21 0021.
 */

public class PukRxFragment extends Fragment implements FragmentBackHandler {

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
    Unbinder unbinder;


    private View inflate;
    private int red_color;
    private int gray_color;
    private Drawable check_pic;
    private Drawable uncheck_pic;
    private HomeRxActivity activity;
    private String pukTimeout_string;
    private BoardSimHelper boardSimHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (HomeRxActivity) getActivity();
        inflate = View.inflate(getActivity(), R.layout.fragment_pinpukpuk, null);
        unbinder = ButterKnife.bind(this, inflate);
        resetUI();
        initRes();
        initUi();
        return inflate;
    }


    private void initRes() {
        red_color = getResources().getColor(R.color.red);
        gray_color = getResources().getColor(R.color.gray);
        check_pic = getResources().getDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getResources().getDrawable(R.drawable.general_btn_remember_nor);
        pukTimeout_string = getString(R.string.puk_alarm_des1);
    }

    private void initUi() {
        boolean isRememPin = SP.getInstance(getActivity()).getBoolean(Cons.PIN_REMEM_FLAG_RX, false);
        String pinCache = SP.getInstance(getActivity()).getString(Cons.PIN_REMEM_STR_RX, "");
        ivPukRemempinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPukResetpinRx.setText(isRememPin ? pinCache : "");
        etPukResetpinRx.setSelection(OtherUtils.getEdContent(etPukResetpinRx).length());
        etPukResetpinRxConfirm.setText(etPukResetpinRx.getText().toString());
        etPukResetpinRxConfirm.setSelection(OtherUtils.getEdContent(etPukResetpinRx).length());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initUi();
            getRemainTime();
            resetUI();
        }
    }

    private void resetUI() {
        activity.tabFlag = Cons.TAB_PUK;
        activity.llNavigation.setVisibility(View.GONE);
        activity.rlBanner.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRemainTime();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_puk_remempin_rx_checkbox, R.id.tv_puk_remempin_rx_checkbox, R.id.bt_puk_rx_unlock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_puk_remempin_rx_checkbox:
            case R.id.tv_puk_remempin_rx_checkbox:
                boolean isCheck = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
                Drawable checkBox = isCheck ? uncheck_pic : check_pic;
                ivPukRemempinRxCheckbox.setImageDrawable(checkBox);
                String pin = OtherUtils.getEdContent(etPukResetpinRx);
                SP.getInstance(getActivity()).putString(Cons.PIN_REMEM_STR_RX, ivPukRemempinRxCheckbox.getDrawable() == check_pic ? pin : "");
                SP.getInstance(getActivity()).putBoolean(Cons.PIN_REMEM_FLAG_RX, ivPukRemempinRxCheckbox.getDrawable() == check_pic);
                break;
            case R.id.bt_puk_rx_unlock:
                unlockPukClick();
                break;
        }
    }

    /**
     * 点击解PUK按钮行为
     */
    private void unlockPukClick() {
        // 空值判断
        String puk = OtherUtils.getEdContent(etPukRx);
        if (TextUtils.isEmpty(puk)) {
            toast(R.string.puk_empty);
            return;
        }

        String pin = OtherUtils.getEdContent(etPukResetpinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty);
            return;
        }
        String pinConfirm = OtherUtils.getEdContent(etPukResetpinRxConfirm);
        if (TextUtils.isEmpty(pinConfirm)) {
            toast(R.string.pin_confirm_empty);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters);
            return;
        }
        if (pinConfirm.length() < 4 | pinConfirm.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters);
            return;
        }
        // 匹配
        if (!pin.equalsIgnoreCase(pinConfirm)) {
            toast(R.string.puk_pinDontMatch);
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
        String puk = OtherUtils.getEdContent(etPukRx);
        String pin = OtherUtils.getEdContent(etPukResetpinRx);

        UnlockPukHelper xUnlockPukHelper = new UnlockPukHelper();
        xUnlockPukHelper.setOnUnlockPukSuccessListener(() -> {
            // 1.是否勾选了记住PIN
            boolean isRememPin = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pins = OtherUtils.getEdContent(etPukResetpinRx);
                SP.getInstance(getActivity()).putString(Cons.PIN_REMEM_STR_RX, pins);
                SP.getInstance(getActivity()).putBoolean(Cons.PIN_REMEM_FLAG_RX, isRememPin);
            }
            // 2.进入其他界面
            toOtherRx();
        });
        xUnlockPukHelper.setOnUnlockPukFailedListener(() -> {
            toast(R.string.puk_unlock_failed);
            getRemainTime();
        });
        xUnlockPukHelper.unlockPuk(puk, pin);
    }

    private void toOtherRx() {
        int position = SP.getInstance(getActivity()).getInt(Cons.TAB_FRA, Cons.TAB_MAIN);
        toFragment(activity.clazz[position]);
    }

    private void toPinRx() {
        toFragment(activity.clazz[4]);
    }

    /* -------------------------------------------- HELPER -------------------------------------------- */

    public void toFragment(Class clazz) {
        activity.fraHelpers.transfer(clazz);
    }

    public void toast(int resId) {
        ToastUtil_m.show(getActivity(), resId);
    }

    public void to(Class ac) {
        CA.toActivity(getActivity(), ac, false, true, false, 0);
    }

    @Override
    public boolean onBackPressed() {
        int anInt = SP.getInstance(getActivity()).getInt(Cons.TAB_FRA, Cons.TAB_MAIN);
        activity.fraHelpers.transfer(activity.clazz[anInt]);
        return true;
    }
}
