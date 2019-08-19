package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.UsageHelper;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.helper.WpsHelper;
import com.alcatel.wifilink.root.ue.activity.HomeActivity;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_DataUnitWidget;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetSimStatusBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.SetUsageSettingsParam;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSimStatusHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.LogoutHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageSettingsHelper;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/16 0016.
 */
public class DataPlanInitFrag extends BaseFrag {

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

    @BindView(R.id.wd_dataplan_init_load)
    HH70_LoadWidget wdLoad;// 等待
    @BindView(R.id.wd_dataplan_init_unit)
    HH70_DataUnitWidget wdUnit;// 单位选择

    private String MB;
    private String GB;
    private GetUsageSettingsBean result;
    private Drawable switch_on;
    private Drawable switch_off;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_dataplan_init;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        adapterRussia();// 适配俄语
        initRes();// 初始化资源
        initProperty();// 初始化配置
        initClick();// 初始化点击
    }

    /**
     * 初始化资源
     */
    private void initRes() {
        switch_on = getRootDrawable(R.drawable.pwd_switcher_on);
        switch_off = getRootDrawable(R.drawable.pwd_switcher_off);
        MB = getString(R.string.mb_text);
        GB = getString(R.string.gb_text);
    }

    /**
     * 适配俄语
     */
    private void adapterRussia() {
        String language = ShareUtils.get(RootCons.LANGUAGES.LANGUAGE, "");
        if (language.equalsIgnoreCase(RootCons.LANGUAGES.RUSSIAN)) {
            tvDataplanRxLimitDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            tvDataplanRxLimitUnlimitDes.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        }
    }

    /**
     * 初始化配置
     */
    private void initProperty() {
        wdLoad.setVisibles();
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(activity);
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(getUsageSettings -> {
            wdLoad.setGone();
            result = getUsageSettings;
            // 设置原有的月计划
            UsageHelper.Usage usageByte = UsageHelper.getUsageByte(activity, result.getMonthlyPlan());
            float aFloat = Float.valueOf(usageByte.usage);
            String usage = String.valueOf(aFloat);
            etDataplanRxLimit.setText(result.getMonthlyPlan() == 0 ? "0" : usage);
            // 设置单位
            tvDataplanRxLimitUnit.setText(result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB ? MB : GB);
            // 设置自动断线
            boolean isAuto = result.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE;
            ivDataplanRxAuto.setImageDrawable(isAuto ? switch_on : switch_off);
        });
        getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(this::failedHandle);
        getUsageSettingsHelper.getUsageSetting();
    }

    /**
     * 对失败的处理
     */
    private void failedHandle() {
        toast(R.string.connect_failed, 5000);
        wdLoad.setGone();
        toFrag(getClass(), RefreshFrag.class, null, true);
    }

    /**
     * 初始化点击
     */
    private void initClick() {
        // 回退
        ivDataplanRxBack.setOnClickListener(v -> onBackPresss());
        // 跳过
        tvSkip.setOnClickListener(v -> skip());
        // limit
        ivDataplanRxLimitArrow.setOnClickListener(v -> handleUnit());
        tvDataplanRxLimitUnit.setOnClickListener(v -> handleUnit());
        // auto disconnect
        ivDataplanRxAuto.setOnClickListener(v -> setAutoDisconnect());
        // done
        btDataplanRxDone.setOnClickListener(v -> clickDone());
    }

    /**
     * 点击DONE
     */
    private void clickDone() {
        // 是否掉线
        if (result == null) {
            toast(R.string.connect_failed, 5000);
            return;
        }
        // 空值判断
        if (TextUtils.isEmpty(RootUtils.getEDText(etDataplanRxLimit))) {
            toast(R.string.not_empty, 5000);
            return;
        }
        // 范围判断
        double limit = Double.valueOf(RootUtils.getEDText(etDataplanRxLimit));
        if (limit < 0 | limit > 1024) {
            toast(R.string.input_a_data_value_between_0_1024, 5000);
            return;
        }
        // 提交请求
        toRequest();
    }

    /**
     * 提交请求
     */
    private void toRequest() {
        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            if (getLoginStateBean.getState() == GetLoginStateBean.CONS_LOGIN) {
                GetSimStatusHelper xGetSimStatusHelper = new GetSimStatusHelper();
                xGetSimStatusHelper.setOnGetSimStatusSuccessListener(result -> {
                    int simState = result.getSIMState();
                    switch (simState) {
                        case GetSimStatusBean.CONS_PIN_REQUIRED:
                            toFrag(getClass(), PinInitFrag.class, null, true);
                            break;
                        case GetSimStatusBean.CONS_PUK_REQUIRED:
                            toFrag(getClass(), PukInitFrag.class, null, true);
                            break;
                        case GetSimStatusBean.CONS_SIM_CARD_READY:
                            toCommitSetting();
                            break;
                    }
                });
                xGetSimStatusHelper.setOnGetSimStatusFailedListener(() -> {
                    toast(R.string.connect_failed, 5000);
                    toFrag(getClass(), RefreshFrag.class, null, true);
                });
                xGetSimStatusHelper.getSimStatus();
            } else {
                toast(R.string.log_out, 5000);
                toFrag(getClass(), LoginFrag.class, null, true);
            }
        });
        xGetLoginStateHelper.getLoginState();

    }

    /**
     * 提交设置
     */
    private void toCommitSetting() {
        String monthly_s = RootUtils.getEDText(etDataplanRxLimit);
        double monthly_c = Double.valueOf(TextUtils.isEmpty(monthly_s) ? "0" : monthly_s);
        double monthly_b = result.getUnit() == GetUsageSettingsBean.CONS_UNIT_MB ? monthly_c * 1024L * 1024L : monthly_c * 1024L * 1024L * 1024L;
        result.setMonthlyPlan((long) monthly_b);
        SetUsageSettingsParam param = new SetUsageSettingsParam();
        param.copy(result);
        SetUsageSettingsHelper xSetUsageSettingsHelper = new SetUsageSettingsHelper();
        xSetUsageSettingsHelper.setOnSetUsageSettingsSuccessListener(() -> {
            toast(R.string.success, 5000);
            skip();
        });
        xSetUsageSettingsHelper.setOnSetUsageSettingsFailListener(this::failedHandle);
        xSetUsageSettingsHelper.setUsageSettings(param);
    }

    /**
     * 设置自动断线
     */
    private void setAutoDisconnect() {
        if (result != null) {
            int disable = GetUsageSettingsBean.CONS_AUTO_DISCONNECT_DISABLE;
            int enable = GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE;
            result.setAutoDisconnFlag(result.getAutoDisconnFlag() == enable ? disable : enable);
            ivDataplanRxAuto.setImageDrawable(result.getAutoDisconnFlag() == enable ? switch_on : switch_off);
        }
    }

    /**
     * 处理单位选择
     */
    private void handleUnit() {
        wdUnit.setVisibility(View.VISIBLE);
        wdUnit.setCheck(result.getUnit() == 0 ? HH70_DataUnitWidget.DATAUNIT.MB : HH70_DataUnitWidget.DATAUNIT.GB);
        wdUnit.setOnClickUnitListener(unit -> {
            result.setUnit(unit);
            tvDataplanRxLimitUnit.setText(unit == GetUsageSettingsBean.CONS_UNIT_MB ? MB : GB);
            wdUnit.setVisibility(View.GONE);
        });
    }

    /**
     * 跳转
     */
    private void skip() {
        // 提交向导标记
        ShareUtils.set(RootCons.SP_WIZARD, true);
        ShareUtils.set(RootCons.SP_DATA_PLAN_INIT, true);
        // 是否进入过wifi初始向导页
        boolean isWifiInit = ShareUtils.get(RootCons.SP_WIFI_INIT, false);
        if (isWifiInit) {
            // --> 主页
            toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
        } else {
            // 检测是否开启了WPS模式
            WpsHelper wpsHelper = new WpsHelper();
            wpsHelper.setOnGetWPSSuccessListener(isWPS -> {
                if (isWPS) {
                    toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true);
                } else {
                    toFrag(getClass(), WifiInitFrag.class, null, true);
                }
            });
            wpsHelper.setOnGetWPSFailedListener(() ->  toFragActivity(getClass(), HomeActivity.class, mainFrag.class, null, true));
            wpsHelper.getWpsStatus();
        }
    }

    @Override
    public boolean onBackPresss() {
        if (lastFrag == WizardFrag.class) {
            toFrag(getClass(), WizardFrag.class, null, true);
        } else {
            LogoutHelper xLogoutHelper = new LogoutHelper();
            xLogoutHelper.setOnLogoutSuccessListener(() -> toFrag(getClass(), LoginFrag.class, null, true));
            xLogoutHelper.setOnLogOutFailedListener(() -> toast(R.string.login_logout_failed, 5000));
            xLogoutHelper.logout();
        }
        return true;
    }
}
