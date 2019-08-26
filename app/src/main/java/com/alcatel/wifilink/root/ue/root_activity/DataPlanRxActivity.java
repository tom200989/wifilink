package com.alcatel.wifilink.root.ue.root_activity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.bean.Other_PinPukBean;
import com.alcatel.wifilink.root.helper.BoardSimHelper;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.UsageHelper;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.utils.SP;
import com.alcatel.wifilink.root.utils.ScreenSize;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.widget.PopupWindows;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageSettingsHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

// TOGO 2019/8/17 0017 DataplanFrag
@Deprecated
public class DataPlanRxActivity extends BaseActivityWithBack {

    @BindView(R.id.iv_dataplan_rx_back)
    ImageView ivDataplanRxBack;// 回退键
    @BindView(R.id.tv_setPlan_rx_skip)
    TextView tvSkip;// 跳过
    @BindView(R.id.iv_dataplan_rx_limit_arrow)
    ImageView ivDataplanRxLimitArrow;// 下拉箭头
    @BindView(R.id.tv_dataplan_rx_limit_unit)
    TextView tvDataplanRxLimitUnit;// 单元(MB|GB)
    @BindView(R.id.et_dataplan_rx_limit)
    EditText etDataplanRxLimit;// 编辑域
    @BindView(R.id.iv_dataplan_rx_auto)
    ImageView ivDataplanRxAuto;// 自动连接按钮
    @BindView(R.id.bt_dataplan_rx_done)
    Button btDataplanRxDone;// 提交生效
    @BindView(R.id.tv_dataplan_rx_limit_des)
    TextView tvDataplanRxLimitDes;// 描述1
    @BindView(R.id.tv_dataplan_rx_limit_unlimit_des)
    TextView tvDataplanRxLimitUnlimitDes;// 描述2

    private TimerHelper heartTimer;
    private BoardSimHelper boardSimHelper;
    private int unit = 0;// 流量单位
    private PopupWindows pop;
    private int check_color;
    private int uncheck_color;
    private String MB;
    private String GB;
    private ProgressDialog pgd;
    private GetUsageSettingsBean result;
    private String flag = "";

    private void startHeartTimer() {
        heartTimer = OtherUtils.startHeartBeat(this, RefreshWifiRxActivity.class, LoginRxActivity.class);
    }

    private Drawable switch_on;
    private Drawable switch_off;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_data_plan_rx);
        SmartLinkV3App.getContextInstance().add(this);
        ButterKnife.bind(this);
        adapterRussia();// 适配俄语
        startHeartTimer();
        initRes();
        initBean();
    }

    // TOGO 2019/8/16 0016 

    /**
     * 适配俄语
     */
    private void adapterRussia() {
        if (OtherUtils.getCurrentLanguage().equalsIgnoreCase("ru")) {
            float tvLimitDes_size = tvDataplanRxLimitDes.getTextSize();
            float tvLimitUnlimitDes_size = tvDataplanRxLimitUnlimitDes.getTextSize();
            tvDataplanRxLimitDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvDataplanRxLimitUnlimitDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            Lgg.t("russia_lang").vv("tvLimitDes_size: " + tvLimitDes_size);
            Lgg.t("russia_lang").vv("tvLimitUnlimitDes_size: " + tvLimitUnlimitDes_size);
        }
    }

    // TOGO: 使用roothiber的Lastfrag进行记录
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getSkipFlag(String flag) {
        this.flag = flag;
    }

    // TOGO 2019/8/16 0016 
    private void initRes() {
        switch_on = getResources().getDrawable(R.drawable.pwd_switcher_on);
        switch_off = getResources().getDrawable(R.drawable.pwd_switcher_off);
        check_color = getResources().getColor(R.color.mg_blue);
        uncheck_color = getResources().getColor(R.color.gray);
        MB = getString(R.string.mb_text);
        GB = getString(R.string.gb_text);
    }

    // TOGO 2019/8/17 0017 
    private void initBean() {
        pgd = OtherUtils.showProgressPop(this);
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(this);
        getUsageSettingsHelper.setOnSetUsageSettingSuccessListener(getUsageSettings -> {
            OtherUtils.hideProgressPop(pgd);
            DataPlanRxActivity.this.result = result;
            // 设置原有的月计划
            UsageHelper.Usage usageByte = UsageHelper.getUsageByte(DataPlanRxActivity.this, result.getMonthlyPlan());
            float aFloat = Float.valueOf(usageByte.usage);
            String usage = String.valueOf(aFloat);
            etDataplanRxLimit.setText(result.getMonthlyPlan() == 0 ? "0" : usage);
            // 设置单位
            tvDataplanRxLimitUnit.setText(result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB ? MB : GB);
            // 设置自动断线
            boolean isAuto = result.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE;
            ivDataplanRxAuto.setImageDrawable(isAuto ? switch_on : switch_off);
        });
        getUsageSettingsHelper.setOnSetUsageSettingFailedListener(() -> {
            failed();
            Logs.e("ma_conn_error", "getUsageSetting onResultError: ");
        });
        getUsageSettingsHelper.getUsageSetting();
    }

    // TOGO 2019/8/17 0017 
    @Override
    public void onBackPressed() {
        clickBackButton();
    }

    // TOGO 2019/8/17 0017 onbackpress

    /**
     * 点击了回退按钮
     */
    private void clickBackButton() {
        if (Cons.WIZARD_RX.equalsIgnoreCase(flag)) {
            to(WizardRxActivity.class);

        } else {
            LogoutHelper xLogoutHelper = new LogoutHelper();
            xLogoutHelper.setOnLogoutSuccessListener(() -> to(LoginRxActivity.class));
            xLogoutHelper.setOnLogOutFailedListener(() -> ToastUtil_m.show(this, getString(R.string.login_logout_failed)));
            xLogoutHelper.logout();
        }
    }

    // TOGO 2019/8/17 0017 roothiber + mainacticity
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        OtherUtils.stopHeartBeat(heartTimer);
    }

    // TOGO 2019/8/17 0017 
    @OnClick({R.id.iv_dataplan_rx_back,// 回退
            R.id.tv_setPlan_rx_skip,// 跳过
            R.id.iv_dataplan_rx_limit_arrow,// 限制箭头
            R.id.tv_dataplan_rx_limit_unit,// 单位
            R.id.iv_dataplan_rx_auto,// 自动开关
            R.id.bt_dataplan_rx_done})// done
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_dataplan_rx_back:
                clickBackButton();
                break;
            case R.id.tv_setPlan_rx_skip:// 跳过
                clickSkip();
                break;
            case R.id.iv_dataplan_rx_limit_arrow:
            case R.id.tv_dataplan_rx_limit_unit:
                choseUnit();
                break;
            case R.id.iv_dataplan_rx_auto:
                setAutoDisconnect();
                break;
            case R.id.bt_dataplan_rx_done:
                doneClick();
                break;
        }
    }


    // TOGO 2019/8/17 0017 skip()
    /**
     * 点击跳过
     */
    private void clickSkip() {
        toAc();
    }

    // TOGO 2019/8/17 0017 clickdone
    /**
     * 点击了done按钮的操作
     */
    private void doneClick() {
        if (OtherUtils.isWifiConnect(this)) {

            // 是否掉线
            if (result == null) {
                toast(R.string.connect_failed);
                return;
            }
            // 空值判断
            if (TextUtils.isEmpty(RootUtils.getEDText(etDataplanRxLimit))) {
                toast(R.string.not_empty);
                return;
            }
            // 范围判断
            double limit = Double.valueOf(RootUtils.getEDText(etDataplanRxLimit));
            if (limit < 0 | limit > 1024) {
                toast(R.string.input_a_data_value_between_0_1024);
                return;
            }
            // 提交请求
            pullRequest();
        } else {
            failed();
            Logs.e("ma_conn_error", "wifi error");
        }
    }

    // TOGO 2019/8/17 0017 failedHandle
    private void failed() {
        toast(R.string.connect_failed);
        OtherUtils.hideProgressPop(pgd);
        to(RefreshWifiRxActivity.class);
        Logs.t("ma_unknown").vv("DataPlanRx--> failed");
    }

    // TOGO 2019/8/17 0017 toRequest
    /**
     * 提交请求
     */
    private void pullRequest() {
        boardSimHelper = new BoardSimHelper(this);
        boardSimHelper.setOnPinRequireListener(result -> toPinPukUi(new Other_PinPukBean(Cons.PIN_FLAG)));
        boardSimHelper.setOnpukRequireListener(result -> toPinPukUi(new Other_PinPukBean(Cons.PUK_FLAG)));
        boardSimHelper.setOnSimReadyListener(result -> usageRequest());// 发起提交请求
        boardSimHelper.boardNormal();
    }

    // TOGO 2019/8/17 0017 toCommitSetting
    /**
     * 发起提交请求
     */
    private void usageRequest() {
        String monthly_s = RootUtils.getEDText(etDataplanRxLimit);
        double monthly_c = Double.valueOf(TextUtils.isEmpty(monthly_s) ? "0" : monthly_s);
        double monthly_b = result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB ? monthly_c * 1024l * 1024l : monthly_c * 1024l * 1024l * 1024l;
        result.setMonthlyPlan((long) monthly_b);
        SetUsageSettingsParam param = new SetUsageSettingsParam();
        param.copy(result);
        SetUsageSettingsHelper xSetUsageSettingsHelper = new SetUsageSettingsHelper();
        xSetUsageSettingsHelper.setOnSetUsageSettingsSuccessListener(() -> {
            toast(R.string.success);
            toAc();
        });
        xSetUsageSettingsHelper.setOnSetUsageSettingsFailListener(() -> failed());
        xSetUsageSettingsHelper.setUsageSettings(param);
    }

    // TOGO 2019/8/17 0017 skip
    private void toAc() {
        // 提交向导标记
        SP.getInstance(this).putBoolean(Cons.WIZARD_RX, true);
        SP.getInstance(this).putBoolean(Cons.DATAPLAN_RX, true);
        // 是否进入过wifi初始向导页
        if (SP.getInstance(this).getBoolean(Cons.WIFIINIT_RX, false)) {
            // --> 主页
            to(HomeRxActivity.class);
        } else {
            // 检测是否开启了WPS模式
            checkWps();
        }
    }

    // TOGO 2019/8/17 0017 skip()

    /**
     * 检测是否开启了WPS模式
     */
    private void checkWps() {
        WpsHelper wpsHelper = new WpsHelper();
        wpsHelper.setOnGetWPSSuccessListener(attr -> to(attr ? HomeRxActivity.class : WifiInitRxActivity.class));
        wpsHelper.setOnGetWPSFailedListener(() -> to(HomeRxActivity.class));
        wpsHelper.getWpsStatus();
    }

    // TOGO 2019/8/17 0017 roothiber.tofrag()
    /**
     * 前往PIN|PUK界面
     *
     * @param otherPinPukBean
     */
    private void toPinPukUi(Other_PinPukBean otherPinPukBean) {
        EventBus.getDefault().postSticky(otherPinPukBean);
        to(PinPukIndexRxActivity.class);
    }

    // TOGO 2019/8/17 0017 
    /**
     * 设置自动断线
     */
    private void setAutoDisconnect() {
        if (result != null) {
            int disable_auto = Cons.DISABLE_NOTAUTODISCONNECT;
            int enable_auto = Cons.ENABLE_AUTODISCONNECT;
            result.setAutoDisconnFlag(result.getAutoDisconnFlag() == enable_auto ? disable_auto : enable_auto);
            ivDataplanRxAuto.setImageDrawable(result.getAutoDisconnFlag() == enable_auto ? switch_on : switch_off);
        }
    }

    // TOGO 2019/8/17 0017 handleUnit

    /**
     * 显示单位弹窗
     */
    private void choseUnit() {
        ScreenSize.SizeBean size = ScreenSize.getSize(this);
        int width = (int) (size.width * 0.85f);
        int height = (int) (size.height * 0.18f);
        View inflate = View.inflate(this, R.layout.pop_dataplan_rx, null);
        TextView mb = inflate.findViewById(R.id.tv_dataplan_rx_pop_mb);
        TextView gb = inflate.findViewById(R.id.tv_dataplan_rx_pop_gb);
        // 初始化颜色
        boolean isMb = result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB;
        mb.setTextColor(isMb ? check_color : uncheck_color);
        gb.setTextColor(isMb ? uncheck_color : check_color);
        mb.setOnClickListener(v -> setUnit(GetUsageSettingsBean.CONS_UNIT_MB));
        gb.setOnClickListener(v -> setUnit(GetUsageSettingsBean.CONS_UNIT_GB));
        pop = new PopupWindows(this, inflate, width, height, new ColorDrawable(Color.TRANSPARENT));
    }

    // TOGO 2019/8/17 0017 handleUnit
    private void setUnit(int unit) {
        if (result != null) {
            result.setUnit(unit);
            tvDataplanRxLimitUnit.setText(unit == GetUsageSettingsBean.CONS_UNIT_MB ? MB : GB);
        }
        pop.dismiss();
    }

    // TOGO 2019/8/17 0017 roothiber
    public void toast(int resId) {
        ToastUtil_m.show(this, resId);
    }

    // TOGO 2019/8/17 0017 roothiber
    private void to(Class clazz) {
        CA.toActivity(this, clazz, false, true, false, 0);
    }
}