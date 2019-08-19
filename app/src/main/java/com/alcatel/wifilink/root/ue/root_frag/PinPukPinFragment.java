package com.alcatel.wifilink.root.ue.root_frag;

import android.annotation.SuppressLint;
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
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPinHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qianli.ma on 2017/11/16 0016.
 */

// TOGO 2019/8/17 0017 pinInitFrag
@Deprecated
public class PinPukPinFragment extends Fragment {

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
    Unbinder unbinder;

    private View inflate;
    private int red_color;
    private int gray_color;
    private Drawable check_pic;
    private Drawable uncheck_pic;
    private BoardSimHelper boardSimHelper;
    private PinPukIndexRxActivity activity;
    private boolean isRussia;// 是否为俄语
    private boolean isTaiwan;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Lgg.t("pin_question").vv("PinPukPinFragment");
        activity = (PinPukIndexRxActivity) getActivity();
        inflate = View.inflate(getActivity(), R.layout.fragment_pinpukpin, null);
        unbinder = ButterKnife.bind(this, inflate);
        // TOGO 2019/8/17 0017 
        isRussia = OtherUtils.getCurrentLanguage().equalsIgnoreCase("ru");
        isTaiwan = OtherUtils.getCurrentLanguage().equalsIgnoreCase("zh");
        initRes();
        initUi();
        return inflate;
    }

    // TOGO 2019/8/17 0017 roothiber onnext
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initUi();
        }
    }

    // TOGO 2019/8/17 0017 

    /**
     * 初始化资源
     */
    private void initRes() {
        red_color = getResources().getColor(R.color.red);
        gray_color = getResources().getColor(R.color.gray);
        check_pic = getResources().getDrawable(R.drawable.general_btn_remember_pre);
        uncheck_pic = getResources().getDrawable(R.drawable.edit_normal);
    }

    // TOGO 2019/8/17 0017 

    /**
     * 初始化UI
     */
    private void initUi() {
        boolean isRememPin = SP.getInstance(getActivity()).getBoolean(Cons.PIN_REMEM_FLAG_RX, false);
        String pinCache = SP.getInstance(getActivity()).getString(Cons.PIN_REMEM_STR_RX, "");
        ivPinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPinRx.setText(isRememPin ? pinCache : "");
        etPinRx.setSelection(RootUtils.getEDText(etPinRx).length());
        // 适配俄语
        if (isRussia) {
            tvPinRxTipNum.setVisibility(View.GONE);
        }
    }

    // TOGO 2019/8/17 0017 roothiber-isReloadData
    @Override
    public void onResume() {
        super.onResume();
        // 获取剩余次数
        getRemainTime();
    }

    // TOGO 2019/8/17 0017 

    /**
     * 获取PIN码剩余次数
     */
    private void getRemainTime() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnNormalSimstatusListener(this::toRemain);
        boardSimHelper.boardNormal();
    }

    // TOGO 2019/8/17 0017 getRemainTime

    /**
     * 处理剩余次数
     *
     * @param simStatus
     */
    @SuppressLint("SetTextI18n")
    private void toRemain(GetSimStatusBean simStatus) {
        int pinTime = simStatus.getPinRemainingTimes();
        Lgg.t("pin_question").vv("pintime: " + pinTime);
        // pinTime = 0;//  测试Puk界面跳转,请将该代码注释
        tvPinRxTipNum.setText(String.valueOf(pinTime));
        if (pinTime < 3) {
            if (pinTime > 1) {
                tvPinRxTipNum.setTextColor(red_color);
                tvPinRxTipDes.setTextColor(red_color);
                if (isRussia) {
                    tvPinRxTipDes.setText(getString(R.string.sim_unlocked_attempts));
                } else {
                    tvPinRxTipDes.setText(getString(R.string.sim_unlocked_attempts) + " " + pinTime);
                }
                if (isTaiwan) {
                    tvPinRxTipNum.setVisibility(View.GONE);
                }

            } else if (pinTime == 1) {
                tvPinRxTipNum.setVisibility(View.GONE);
                tvPinRxTipDes.setTextColor(red_color);
                tvPinRxTipDes.setText(getString(R.string.ergo_20181010_pin_remained));
            } else {
                toPukfragment(simStatus);
            }
        } else {
            if (isRussia) {
                tvPinRxTipDes.setText(getString(R.string.sim_unlocked_attempts) + " " + pinTime);
            }
        }

    }

    // TOGO 2019/8/17 0017 roothiber
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    // TOGO 2019/8/17 0017 
    @OnClick({R.id.iv_pin_rx_deleted, R.id.bt_pin_rx_unlock, R.id.iv_pin_rx_checkbox, R.id.tv_pin_rx_checkbox})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pin_rx_deleted:
                etPinRx.setText("");
                break;
            case R.id.bt_pin_rx_unlock:
                unlockPin();
                break;
            case R.id.iv_pin_rx_checkbox:
            case R.id.tv_pin_rx_checkbox:
                boolean isCheck = ivPinRxCheckbox.getDrawable() == check_pic;
                ivPinRxCheckbox.setImageDrawable(isCheck ? uncheck_pic : check_pic);
                String pin = RootUtils.getEDText(etPinRx);
                SP.getInstance(getActivity()).putString(Cons.PIN_REMEM_STR_RX, ivPinRxCheckbox.getDrawable() == check_pic ? pin : "");
                SP.getInstance(getActivity()).putBoolean(Cons.PIN_REMEM_FLAG_RX, ivPinRxCheckbox.getDrawable() == check_pic);
                break;
        }
    }

    // TOGO 2019/8/17 0017 

    /**
     * 解PIN
     */
    private void unlockPin() {
        // 空值判断
        String pin = RootUtils.getEDText(etPinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters);
            return;
        }
        // 请求
        getBoardAndSimStauts();
    }

    // TOGO 2019/8/17 0017 commitUnlock
    private void getBoardAndSimStauts() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnPinRequireListener(this::unlockPinRequest);
        boardSimHelper.setOnpukRequireListener(this::toPukfragment);
        boardSimHelper.setOnpukTimeoutListener(this::toPukfragment);
        boardSimHelper.setOnSimReadyListener(result -> toAc());
        boardSimHelper.boardNormal();
    }

    // TOGO 2019/8/17 0017 skip

    /**
     * 跳转
     */
    private void toAc() {
        // 是否进入过流量设置页
        if (SP.getInstance(getActivity()).getBoolean(Cons.DATAPLAN_RX, false)) {
            // 是否进入过wifi初始设置页
            if (SP.getInstance(getActivity()).getBoolean(Cons.WIFIINIT_RX, false)) {
                to(HomeRxActivity.class);
            } else {
                checkWps();// 检测是否开启了WPS模式
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

    // TOGO 2019/8/17 0017 roothiber

    /**
     * 前往puk fragment
     *
     * @param result
     */
    private void toPukfragment(GetSimStatusBean result) {
        activity.fraHelper.transfer(activity.allClass[1]);
    }

    // TOGO 2019/8/17 0017 

    /**
     * 发送解PIN请求
     *
     * @param result
     */
    private void unlockPinRequest(GetSimStatusBean result) {


        String pin = RootUtils.getEDText(etPinRx);

        UnlockPinHelper xUnlockPinHelper = new UnlockPinHelper();
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(() -> {
            // 是否勾选了记住PIN
            boolean isRememPin = ivPinRxCheckbox.getDrawable() == check_pic;
            if (isRememPin) {
                String pins = RootUtils.getEDText(etPinRx);
                SP.getInstance(getActivity()).putString(Cons.PIN_REMEM_STR_RX, pins);
                SP.getInstance(getActivity()).putBoolean(Cons.PIN_REMEM_FLAG_RX, isRememPin);
            }
            // 进入其他界面
            toAc();
        });
        xUnlockPinHelper.setOnUnlockPinRemainTimeFailedListener(() -> {
            etPinRx.setText("");
            toast(R.string.pin_error_waring_title);
            getRemainTime();
        });
        xUnlockPinHelper.unlockPin(pin);
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
