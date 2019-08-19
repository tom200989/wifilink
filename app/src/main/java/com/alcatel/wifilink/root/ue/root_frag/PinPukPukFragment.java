package com.alcatel.wifilink.root.ue.root_frag;

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
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.ue.root_activity.DataPlanRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.HomeRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.PinPukIndexRxActivity;
import com.alcatel.wifilink.root.ue.root_activity.WifiInitRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPukHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// TOGO 2019/8/17 0017 pukinitFrag
@Deprecated 
public class PinPukPukFragment extends Fragment {

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
    private PinPukIndexRxActivity activity;
    private int red_color;
    private int gray_color;
    private Drawable check_pic;
    private Drawable uncheck_pic;
    private String pukTimeout_string;
    private BoardSimHelper boardSimHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (PinPukIndexRxActivity) getActivity();
        inflate = View.inflate(getActivity(), R.layout.fragment_pinpukpuk, null);
        unbinder = ButterKnife.bind(this, inflate);
        initRes();
        initUi();
        return inflate;
    }

    // TOGO 2019/8/17 0017 
    private void initRes() {
        red_color = getResources().getColor(R.color.red);
        gray_color = getResources().getColor(R.color.gray);
        check_pic = getResources().getDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getResources().getDrawable(R.drawable.edit_normal);
        pukTimeout_string = getString(R.string.puk_alarm_des1);
    }

    // TOGO 2019/8/17 0017 
    private void initUi() {
        boolean isRememPin = SP.getInstance(getActivity()).getBoolean(Cons.PIN_REMEM_FLAG_RX, false);
        String pinCache = SP.getInstance(getActivity()).getString(Cons.PIN_REMEM_STR_RX, "");
        ivPukRemempinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPukResetpinRx.setText(isRememPin ? pinCache : "");
        etPukResetpinRx.setSelection(RootUtils.getEDText(etPukResetpinRx).length());
        etPukResetpinRxConfirm.setText(etPukResetpinRx.getText().toString());
        etPukResetpinRxConfirm.setSelection(RootUtils.getEDText(etPukResetpinRx).length());
    }

    // TOGO 2019/8/17 0017 roothiber isreloaddata
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initUi();
        }
    }

    // TOGO 2019/8/17 0017 roothiber onnext
    @Override
    public void onResume() {
        super.onResume();
        getRemainTime();
    }

    // TOGO 2019/8/17 0017 

    /**
     * 获取PUK码剩余次数
     */
    private void getRemainTime() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnNormalSimstatusListener(this::toShowRemain);
        boardSimHelper.boardNormal();
    }

    // TOGO 2019/8/17 0017 getRemainTime

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

    // TOGO 2019/8/17 0017 setAllEdittextDisable

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

    // TOGO 2019/8/17 0017 roothiber
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // TOGO 2019/8/17 0017 
    @OnClick({R.id.iv_puk_remempin_rx_checkbox, R.id.tv_puk_remempin_rx_checkbox, R.id.bt_puk_rx_unlock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_puk_remempin_rx_checkbox:
            case R.id.tv_puk_remempin_rx_checkbox:
                Drawable checkBox = ivPukRemempinRxCheckbox.getDrawable() == check_pic ? uncheck_pic : check_pic;
                ivPukRemempinRxCheckbox.setImageDrawable(checkBox);
                String pin = RootUtils.getEDText(etPukResetpinRx);
                SP.getInstance(getActivity()).putString(Cons.PIN_REMEM_STR_RX, ivPukRemempinRxCheckbox.getDrawable() == check_pic ? pin : "");
                SP.getInstance(getActivity()).putBoolean(Cons.PIN_REMEM_FLAG_RX, ivPukRemempinRxCheckbox.getDrawable() == check_pic);
                break;
            case R.id.bt_puk_rx_unlock:
                unlockPukClick();
                break;
        }
    }

    // TOGO 2019/8/17 0017 

    /**
     * 点击解PUK按钮行为
     */
    private void unlockPukClick() {
        // 空值判断
        String puk = RootUtils.getEDText(etPukRx);
        if (TextUtils.isEmpty(puk)) {
            toast(R.string.puk_empty);
            return;
        }

        String pin = RootUtils.getEDText(etPukResetpinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty);
            return;
        }
        String pinConfirm = RootUtils.getEDText(etPukResetpinRxConfirm);
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

    // TOGO 2019/8/17 0017 commitRequest
    private void getBoardAndSimStauts() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnPinRequireListener(result -> toPinFragment());
        boardSimHelper.setOnpukRequireListener(result -> unlockPukRequest());
        boardSimHelper.setOnpukTimeoutListener(this::pukTimeout);
        boardSimHelper.setOnSimReadyListener(result -> toAc());
        boardSimHelper.boardNormal();
    }

    // TOGO 2019/8/17 0017 skip
    private void toAc() {
        // 是否进入过流量设置页
        if (SP.getInstance(getActivity()).getBoolean(Cons.DATAPLAN_RX, false)) {
            // 是否进入过wifi初始设置页
            if (SP.getInstance(getActivity()).getBoolean(Cons.WIFIINIT_RX, false)) {
                to(HomeRxActivity.class);
            } else {
                checkWps();// 检测WPS模式
            }
        } else {
            to(DataPlanRxActivity.class);
        }
    }

    // TOGO 2019/8/17 0017 

    /**
     * 检测是否开启了WPS模式
     */
    private void checkWps() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(attr -> to(attr ? HomeRxActivity.class : WifiInitRxActivity.class));
        wpsHelper.setOnGetWPSFailedListener(() -> to(HomeRxActivity.class));
        wpsHelper.getWpsStatus();
    }

    // TOGO 2019/8/17 0017 getRemainTime
    private void pukTimeout(GetSimStatusBean result) {
        toShowRemain(result);
    }

    // TOGO 2019/8/17 0017 
    private void unlockPukRequest() {
        String puk = RootUtils.getEDText(etPukRx);
        String pin = RootUtils.getEDText(etPukResetpinRx);


        UnlockPukHelper xUnlockPukHelper = new UnlockPukHelper();
        xUnlockPukHelper.setOnUnlockPukSuccessListener(() -> {
            // 是否勾选了记住PIN
            boolean isRememPin = ivPukRemempinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pins = RootUtils.getEDText(etPukResetpinRx);
                SP.getInstance(getActivity()).putString(Cons.PIN_REMEM_STR_RX, pins);
                SP.getInstance(getActivity()).putBoolean(Cons.PIN_REMEM_FLAG_RX, isRememPin);
            }
            // 进入其他界面
            toAc();
        });
        xUnlockPukHelper.setOnUnlockPukFailedListener(() -> {
            etPukRx.setText("");
            toast(R.string.puk_error_waring_title);
            getRemainTime();
        });
        xUnlockPukHelper.unlockPuk(puk, pin);
    }

    // TOGO 2019/8/17 0017 roothiber
    private void toPinFragment() {
        activity.fraHelper.transfer(activity.allClass[0]);
    }

    // TOGO 2019/8/17 0017 roothiber
    /* -------------------------------------------- HELPER -------------------------------------------- */
    public void toast(int resId) {
        ToastUtil_m.show(getActivity(), resId);
    }

    // TOGO 2019/8/17 0017 roothiber
    public void to(Class ac) {
        CA.toActivity(getActivity(), ac, false, true, false, 0);
    }
}
