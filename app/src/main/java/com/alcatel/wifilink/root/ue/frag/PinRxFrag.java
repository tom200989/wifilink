package com.alcatel.wifilink.root.ue.frag;

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
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.UnlockPinHelper;

import butterknife.BindView;

public class PinRxFrag extends BaseFrag {
    // public class PinRxFragment extends Fragment  {

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
    private BoardSimHelper boardSimHelper;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_pinpukpin;
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
        boolean isRememPin = ShareUtils.get(Cons.PIN_REMEM_FLAG_RX,false);
        String pinCache = ShareUtils.get(Cons.PIN_REMEM_STR_RX,"");
        ivPinRxCheckbox.setImageDrawable(isRememPin ? check_pic : uncheck_pic);
        etPinRx.setText(isRememPin ? pinCache : "");
        etPinRx.setSelection(RootUtils.getEDText(etPinRx).length());
    }

    /**
     * 点击事件
     */
    private void initOnClick(){
        ivPinRxDeleted.setOnClickListener(v -> etPinRx.setText(""));
        btPinRxUnlock.setOnClickListener(v -> {
            RootUtils.hideKeyBoard(getActivity());
            unlockPin();
        });
        ivPinRxCheckbox.setOnClickListener(v -> tvPinRxCheckbox.performLongClick());
        tvPinRxCheckbox.setOnClickListener(v -> {
            boolean isCheck = ivPinRxCheckbox.getDrawable() == check_pic;// current is check
            ivPinRxCheckbox.setImageDrawable(isCheck ? uncheck_pic : check_pic);// modify to uncheck
            String pin = RootUtils.getEDText(etPinRx);
            ShareUtils.set(Cons.PIN_REMEM_STR_RX,ivPinRxCheckbox.getDrawable() == check_pic ? pin : "");
            ShareUtils.set(Cons.PIN_REMEM_FLAG_RX,ivPinRxCheckbox.getDrawable() == check_pic);
        });
    }

    /**
     * 获取PIN码剩余次数
     */
    private void getRemainTime() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnNormalSimstatusListener(this::toRemain);
        boardSimHelper.boardNormal();
    }

    /**
     * 处理剩余次数
     *
     * @param simStatus
     */
    private void toRemain(GetSimStatusBean simStatus) {
        int pinTime = simStatus.getPinRemainingTimes();
        // pinTime = 0;//  测试Puk界面跳转,请将该代码注释
        tvPinRxTipNum.setText(String.valueOf(pinTime));
        if (pinTime < 3) {
            tvPinRxTipNum.setTextColor(red_color);
            tvPinRxTipDes.setTextColor(red_color);
            if (pinTime <= 0) {
                toPukRx();
            }
            return;
        }

    }

    /**
     * 解PIN
     */
    private void unlockPin() {
        // 空值判断
        String pin = RootUtils.getEDText(etPinRx);
        if (TextUtils.isEmpty(pin)) {
            toast(R.string.pin_empty,2000);
            return;
        }
        // 位数判断
        if (pin.length() < 4 | pin.length() > 8) {
            toast(R.string.the_pin_code_should_be_4_8_characters,2000);
            return;
        }
        // 请求
        getBoardAndSimStauts();
    }

    private void getBoardAndSimStauts() {
        boardSimHelper = new BoardSimHelper(getActivity());
        boardSimHelper.setOnPinRequireListener(this::unlockPinRequest);
        boardSimHelper.setOnpukRequireListener(result -> toPukRx());
        boardSimHelper.setOnpukTimeoutListener(result -> toPukRx());
        boardSimHelper.setOnSimReadyListener(result -> toOtherRx());
        boardSimHelper.boardNormal();
    }

    /**
     * 跳转
     */
    private void toOtherRx() {
        toFrag(getClass(),mainFrag.class,null,false);
    }

    /**
     * 前往puk fragment
     */
    private void toPukRx() {
        toFrag(getClass(),PukRxFrag.class,null,false);
    }

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
                ShareUtils.set(Cons.PIN_REMEM_STR_RX,pins);
                ShareUtils.set(Cons.PIN_REMEM_FLAG_RX,isRememPin);
            }
            // 进入其他界面
            toOtherRx();
        });
        xUnlockPinHelper.setOnUnlockPinFailedListener(() -> {
            toast(R.string.pin_error_waring_title,2000);
            getRemainTime();
        });
        xUnlockPinHelper.unlockPin(pin);
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(),mainFrag.class,null,false);
        return true;
    }
}
