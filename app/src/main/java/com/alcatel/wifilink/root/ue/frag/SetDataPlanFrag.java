package com.alcatel.wifilink.root.ue.frag;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.BillingAdaper;
import com.alcatel.wifilink.root.helper.BillingHelper;
import com.alcatel.wifilink.root.helper.SetTimeLimitHelper;
import com.alcatel.wifilink.root.helper.UsageHelper;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.alcatel.wifilink.root.widget.PopupWindows;
import com.hiber.cons.TimerState;
import com.hiber.tools.ScreenSize;
import com.hiber.tools.ShareUtils;
import com.hiber.tools.layout.PercentRelativeLayout;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageSettingsBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;

import java.util.Arrays;

import butterknife.BindView;

/**
 * Created by wzhiqiang on 2019/8/20
 */
public class SetDataPlanFrag extends BaseFrag {

    @BindView(R.id.iv_setPlan_rx_back)
    ImageView ivBack;
    @BindView(R.id.tv_setPlan_rx_monthly)
    TextView tvMonthly;
    @BindView(R.id.rl_setPlan_rx_monthly)
    PercentRelativeLayout rlMonthly;
    @BindView(R.id.tv_setPlan_rx_billing)
    TextView tvBilling;
    @BindView(R.id.rl_setPlan_rx_billing)
    PercentRelativeLayout rlBilling;
    @BindView(R.id.tv_setPlan_rx_usageAlert)
    TextView tvUsageAlert;
    @BindView(R.id.rl_setPlan_rx_usageAlert)
    PercentRelativeLayout rlUsageAlert;
    @BindView(R.id.iv_setPlan_rx_autoDisconnect)
    ImageView ivAutoDisconnect;
    @BindView(R.id.iv_setPlan_rx_timelimit)
    ImageView ivTimelimit;
    @BindView(R.id.tv_setPlan_rx_setTimelimit)
    TextView tvSetTimelimit;
    @BindView(R.id.tv_setPlan_rx_setTimelimit_tag)
    TextView tvSetTimelimitTag;
    @BindView(R.id.rl_setPlan_rx_setTimelimit)
    PercentRelativeLayout rlSetTimelimit;
    @BindView(R.id.iv_setPlan_rx_usage_button)
    ImageView ivSetPlanRxUsageButton;
    @BindView(R.id.rl_setPlan_rx_usage_button)
    PercentRelativeLayout rlSetPlanRxUsageButton;
    @BindView(R.id.rl_setPlan_rx_all_content)
    PercentRelativeLayout rlSetPlanRxAllContent;
    @BindView(R.id.load_widget)
    HH70_LoadWidget loadWidget;

    private Drawable switch_on;
    private Drawable switch_off;
    private int blue_color;
    private int gray_color;
    private int black_color;
    private String[] days;
    private String[] alerts;
    private String hour;
    private String min;
    private GetSystemInfoHelper xGetSystemInfoHelper;
    private PopupWindows pop_billing;
    private PopupWindows pop_alert;
    private PopupWindows pop_setTimelimit;
    private PopupWindows pop_monthly;
    private Drawable pop_bg;
    private int count = 0;
    private boolean isHH71 = false;
    private GetUsageSettingsBean tempSetting;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_setdataplanrx;
    }

    @Override
    public void initViewFinish(View inflateView) {
        super.initViewFinish(inflateView);
        initRes();
        initClick();
        timer_period = 2000;
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    @Override
    public void setTimerTask() {
        super.setTimerTask();
        initData();
    }

    private void initRes() {
        switch_on = getRootDrawable(R.drawable.switch_on);
        switch_off = getRootDrawable(R.drawable.switch_off);
        blue_color = getRootColor(R.color.mg_blue);
        gray_color = getRootColor(R.color.gray);
        black_color = getRootColor(R.color.black);
        pop_bg = getRootDrawable(R.drawable.bg_pop_conner);
        hour = getString(R.string.hr_s);
        min = getString(R.string.min_s);
        days = RootUtils.getResArr(activity, R.array.settings_data_plan_billing_day);
        alerts = RootUtils.getResArr(activity, R.array.settings_data_plan_usage_alert);
    }

    /**
     * 初始化点击事件
     */
    public void initClick() {
        ivBack.setOnClickListener(v -> onBackPressed());
        rlMonthly.setOnClickListener(v -> clickMonthly());
        rlBilling.setOnClickListener(v -> clickBilling());
        rlUsageAlert.setOnClickListener(v -> clickAlert());
        ivAutoDisconnect.setOnClickListener(v -> clickAutoDisconnect());
        ivTimelimit.setOnClickListener(v -> clickTimelimit());
        rlSetTimelimit.setOnClickListener(v -> clickSetTimeLimit());
        ivSetPlanRxUsageButton.setOnClickListener(v -> clickUsageEnable());
    }

    private void initData() {
        /* 获取设备信息 */
        xGetSystemInfoHelper = new GetSystemInfoHelper();
        xGetSystemInfoHelper.setOnGetSystemInfoSuccessListener(systemInfo -> {
            isHH71 = RootUtils.isHH71(systemInfo.getDeviceName().toLowerCase());
            rlSetPlanRxUsageButton.setVisibility(isHH71 ? View.VISIBLE : View.GONE);
            getUsageSetting();
        });
        xGetSystemInfoHelper.setOnAppErrorListener(() -> {
            rlSetPlanRxUsageButton.setVisibility(View.GONE);
            rlSetPlanRxAllContent.setVisibility(View.VISIBLE);
            getUsageSetting();
        });
        xGetSystemInfoHelper.setOnFwErrorListener(() -> {
            rlSetPlanRxUsageButton.setVisibility(View.GONE);
            rlSetPlanRxAllContent.setVisibility(View.VISIBLE);
            getUsageSetting();
        });
        xGetSystemInfoHelper.getSystemInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        count = 0;
    }

    private void getUsageSetting() {
        /* 获取流量信息 */
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(activity);
        getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(() -> toErrorUi());
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(result -> {
            tempSetting = result;
            // 判断是否为71
            if (isHH71) {
                int status = result.getStatus();
                ivSetPlanRxUsageButton.setImageDrawable(status == GetUsageSettingsBean.CONS_STATUS_ENABLE ? switch_on : switch_off);
                rlSetPlanRxAllContent.setVisibility(status == GetUsageSettingsBean.CONS_STATUS_ENABLE ? View.VISIBLE : View.GONE);
            } else {
                rlSetPlanRxAllContent.setVisibility(View.VISIBLE);
            }

            // monthly limit
            if (result.getMonthlyPlan() == 0) {
                tvMonthly.setText("");
            } else {
                UsageHelper.Usage monthly = UsageHelper.getUsageByte(activity, result.getMonthlyPlan());
                String monthlyUsage = monthly.usage;
                String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
                if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                    monthlyUsage = monthlyUsage.replace(".", ",") + " ";
                }
                tvMonthly.setText(monthlyUsage + monthly.unit);
            }
            // billing day
            tvBilling.setText(days[result.getBillingDay() - 1]);
            // usage alert
            int per = ShareUtils.get(RootCons.USAGE_LIMIT_DEFAULT, 90);
            tvUsageAlert.setText(RootUtils.getAlert(alerts, per));
            // auto disconnect
            ivAutoDisconnect.setImageDrawable(result.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE ? switch_on : switch_off);
            // time limit
            ivTimelimit.setImageDrawable(result.getTimeLimitFlag() == GetUsageSettingsBean.CONS_TIME_LIMIT_ABLE ? switch_on : switch_off);
            // set time limit show or hide
            // rlSetTimelimit.setVisibility(result.getTimeLimitFlag() == Cons.ENABLE ? View.VISIBLE : View.GONE);
            tvSetTimelimitTag.setTextColor(result.getTimeLimitFlag() == GetUsageSettingsBean.CONS_TIME_LIMIT_ABLE ? black_color : gray_color);
            // set time limit
            UsageHelper.Times timelimit_o = UsageHelper.getUsedTimeForMin(activity, result.getTimeLimitTimes());
            if (timelimit_o.hour.equalsIgnoreCase("0") & timelimit_o.min.equalsIgnoreCase("0")) {
                tvSetTimelimit.setText("");
            } else if (timelimit_o.hour.equalsIgnoreCase("0")) {
                tvSetTimelimit.setText(timelimit_o.min + min);
            } else {
                tvSetTimelimit.setText(timelimit_o.hour + hour + timelimit_o.min + min);
            }

        });
        getUsageSettingsHelper.getUsageSetting();
    }

    /**
     * 出错时候的UI显示
     */
    private void toErrorUi() {
        if (tvMonthly != null & tvBilling != null & tvUsageAlert != null & ivAutoDisconnect != null & ivTimelimit != null & tvSetTimelimit != null) {
            tvMonthly.setText("");
            tvBilling.setText("");
            tvUsageAlert.setText("");
            ivAutoDisconnect.setImageDrawable(switch_off);
            ivTimelimit.setImageDrawable(switch_off);
            tvSetTimelimit.setText("");
        }
    }

    @Override
    public boolean onBackPressed() {
        toFrag(getClass(), MobileNetworkFrag.class, null, false);
        return true;
    }

    /**
     * 点击提交
     */
    private void clickUsageEnable() {
        // 修改状态
        loadWidget.setVisibles();
        tempSetting.setStatus(tempSetting.getStatus() == GetUsageSettingsBean.CONS_STATUS_ENABLE ? GetUsageSettingsBean.CONS_STATUS_DISABLE : GetUsageSettingsBean.CONS_STATUS_ENABLE);
        UsageSettingHelper usageSettingHelper = new UsageSettingHelper(activity);
        usageSettingHelper.setOnSetUsageSettingSuccessListener(attr -> {
            toast(R.string.succeed);
            loadWidget.setGone();
        });
        usageSettingHelper.setOnSetUsageSettingFailedListener(() -> {
            toast(R.string.connect_failed);
            loadWidget.setGone();
        });
        usageSettingHelper.setUsageSetting(tempSetting);
    }

    /**
     * 提交月流量计划
     */
    private void clickMonthly() {
        View inflate = View.inflate(activity, R.layout.pop_setdataplan_rx_monthly, null);
        ScreenSize.SizeBean size = ScreenSize.getSize(activity);
        int width = (int) (size.width * 0.85f);
        int height = (int) (size.height * 0.30f);
        RelativeLayout rl = inflate.findViewById(R.id.rl_pop_setPlan_rx_monthly);
        rl.setOnClickListener(null);
        EditText etNum = inflate.findViewById(R.id.et_pop_setPlan_rx_monthly_num);
        TextView tvMb = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_mb);
        TextView tvGb = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_gb);
        TextView tvCancel = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_cancel);
        TextView tvOk = inflate.findViewById(R.id.tv_pop_setPlan_rx_monthly_ok);
        etNum.setHint("0");
        tvMb.setOnClickListener(v -> {
            tvMb.setTextColor(blue_color);
            tvGb.setTextColor(gray_color);
        });
        tvGb.setOnClickListener(v -> {
            tvMb.setTextColor(gray_color);
            tvGb.setTextColor(blue_color);
        });
        tvCancel.setOnClickListener(v -> pop_monthly.dismiss());
        tvOk.setOnClickListener(v -> setMonthly(etNum, tvMb, tvGb));
        pop_monthly = new PopupWindows(activity, inflate, width, height, true, pop_bg);
    }

    /**
     * 请求设置月流量计划
     */
    private void setMonthly(EditText edNum, TextView tvmb, TextView tvgb) {
        pop_monthly.dismiss();// 消隐
        String content = RootUtils.getEDText(edNum);
        // 非空判断
        if (TextUtils.isEmpty(content)) {
            content = "0";
        }
        // 范围鉴定
        int num = Integer.valueOf(content);
        if (num < 0 || num > 1024) {
            toast(R.string.input_a_data_value_between_0_1024);
            return;
        }
        // 请求提交
        loadWidget.setVisibles();
        UsageSettingHelper ush = new UsageSettingHelper(activity);
        // 1.先获取一次usage-setting
        ush.setOnGetUsageSettingsSuccessListener(attr -> {
            attr.setUnit(tvmb.getCurrentTextColor() == blue_color ? GetUsageSettingsBean.CONS_UNIT_MB : GetUsageSettingsBean.CONS_UNIT_GB);
            attr.setMonthlyPlan(tvmb.getCurrentTextColor() == blue_color ? num * 1024l * 1024l : num * 1024l * 1024l * 1024l);
            // 2.在提交usage-setting
            UsageSettingHelper ush1 = new UsageSettingHelper(activity);
            ush1.setOnSetUsageSettingSuccessListener(attr1 -> {
                toast(R.string.success);
                loadWidget.setGone();
            });
            ush1.setOnSetUsageSettingFailedListener(() -> {
                toast(R.string.connect_failed);
                loadWidget.setGone();
            });
            ush1.setUsageSetting(attr);
        });
        ush.setOnGetUsageSettingsFailedListener(() -> loadWidget.setGone());
        ush.getUsageSetting();
    }

    /**
     * 提交日期
     */
    private void clickBilling() {
        View inflate = View.inflate(activity, R.layout.pop_setdataplan_billing, null);
        ScreenSize.SizeBean size = ScreenSize.getSize(activity);
        int width = (int) (size.width * 0.85f);
        int height = (int) (size.height * 0.45f);
        RecyclerView rcv = inflate.findViewById(R.id.rcv_pop_setPlan_rx_billing);
        rcv.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        rcv.setAdapter(new BillingAdaper(activity, Arrays.asList(days)) {
            @Override
            public void clickDay(int day) {
                // 提交请求
                pop_billing.dismiss();
                BillingHelper billingHelper = new BillingHelper(activity);
                billingHelper.setOnSetBillSuccessListener(() -> toast(R.string.success, 3000));
                billingHelper.setBillingDay(day);
            }
        });
        pop_billing = new PopupWindows(activity, inflate, width, height, true, pop_bg);
    }

    /**
     * 提交流量警告
     */
    private void clickAlert() {
        View inflate = View.inflate(activity, R.layout.pop_setdataplan_alert, null);
        ScreenSize.SizeBean size = ScreenSize.getSize(activity);
        int width = (int) (size.width * 0.85f);
        int height = (int) (size.height * 0.54f);
        inflate.findViewById(R.id.tv_pop_setPlan_alert_not_reminder).setOnClickListener(v -> saveAlertAndShowPop(-1));
        inflate.findViewById(R.id.tv_pop_setPlan_alert_90).setOnClickListener(v -> saveAlertAndShowPop(90));
        inflate.findViewById(R.id.tv_pop_setPlan_alert_80).setOnClickListener(v -> saveAlertAndShowPop(80));
        inflate.findViewById(R.id.tv_pop_setPlan_alert_70).setOnClickListener(v -> saveAlertAndShowPop(70));
        inflate.findViewById(R.id.tv_pop_setPlan_alert_60).setOnClickListener(v -> saveAlertAndShowPop(60));
        inflate.findViewById(R.id.tv_pop_setPlan_alert_cancel).setOnClickListener(v -> pop_alert.dismiss());
        pop_alert = new PopupWindows(activity, inflate, width, height, true, pop_bg);
    }

    /**
     * 切换自动断线
     */
    private void clickAutoDisconnect() {
        loadWidget.setVisibles();
        UsageSettingHelper ush = new UsageSettingHelper(activity);
        ush.setOnGetUsageSettingsSuccessListener(attr -> {
            attr.setAutoDisconnFlag(attr.getAutoDisconnFlag() == GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE ? GetUsageSettingsBean.CONS_AUTO_DISCONNECT_DISABLE : GetUsageSettingsBean.CONS_AUTO_DISCONNECT_ENABLE);
            UsageSettingHelper ush1 = new UsageSettingHelper(activity);
            ush1.setOnSetUsageSettingSuccessListener(attr1 -> loadWidget.setGone());
            ush1.setOnSetUsageSettingFailedListener(() -> loadWidget.setGone());
            ush1.setUsageSetting(attr);
        });
        ush.setOnGetUsageSettingsFailedListener(() -> loadWidget.setGone());
        ush.getUsageSetting();
    }

    /**
     * 切换时间限制按钮
     */
    private void clickTimelimit() {
        loadWidget.setVisibles();
        UsageSettingHelper ush = new UsageSettingHelper(activity);
        ush.setOnGetUsageSettingsSuccessListener(attr -> {
            attr.setTimeLimitFlag(attr.getTimeLimitFlag() == GetUsageSettingsBean.CONS_TIME_LIMIT_ABLE ? GetUsageSettingsBean.CONS_TIME_LIMIT_DISABLE : GetUsageSettingsBean.CONS_TIME_LIMIT_ABLE);
            UsageSettingHelper ush1 = new UsageSettingHelper(activity);
            ush1.setOnSetUsageSettingFailedListener(() -> loadWidget.setGone());
            ush1.setOnSetUsageSettingSuccessListener(getUsageSettings -> loadWidget.setGone());
            ush1.setUsageSetting(attr);
        });
        ush.setOnGetUsageSettingsFailedListener(() -> loadWidget.setGone());
        ush.getUsageSetting();
    }

    /**
     * 设置timelimit时间
     */
    private void clickSetTimeLimit() {
        View inflate = View.inflate(activity, R.layout.pop_setdataplan_settimtlimit, null);
        ScreenSize.SizeBean size = ScreenSize.getSize(activity);
        int width = (int) (size.width * 0.85f);
        int height = (int) (size.height * 0.29f);
        EditText etHour = inflate.findViewById(R.id.et_pop_setPlan_rx_settimelimit_hour);
        EditText etMin = inflate.findViewById(R.id.et_pop_setPlan_rx_settimelimit_min);
        etHour.setHint("0");
        etMin.setHint("5");
        SetTimeLimitHelper.addEdwatch(etHour, etMin);// 增加监听器
        View cancel = inflate.findViewById(R.id.tv_pop_setPlan_rx_settimelimit_cancel);
        View ok = inflate.findViewById(R.id.tv_pop_setPlan_rx_settimelimit_ok);
        cancel.setOnClickListener(v -> pop_setTimelimit.dismiss());
        ok.setOnClickListener(v -> {
            // 提交请求
            setSetTimeLimit(etHour, etMin);
        });
        pop_setTimelimit = new PopupWindows(activity, inflate, width, height, true, pop_bg);
    }

    /**
     * 提交set time limit请求
     */
    private void setSetTimeLimit(EditText etHour, EditText etMin) {
        String hour_c = RootUtils.getEDText(etHour);
        String min_c = RootUtils.getEDText(etMin);
        if (TextUtils.isEmpty(hour_c)) {
            hour_c = "0";
        }
        if (TextUtils.isEmpty(min_c)) {
            min_c = "0";
        }
        pop_setTimelimit.dismiss();
        loadWidget.setVisibles();
        int hour = Integer.valueOf(hour_c);
        int min = Integer.valueOf(min_c);
        int total = hour * 60 + min;
        // 用户设定的时间如果为0--> 强制关闭time limit按钮功能
        if (total <= 0) {
            UsageSettingHelper ush = new UsageSettingHelper(activity);
            ush.setOnGetUsageSettingsSuccessListener(attr -> {
                // 强制设定为disable
                attr.setTimeLimitFlag(GetUsageSettingsBean.CONS_TIME_LIMIT_DISABLE);
                UsageSettingHelper ush1 = new UsageSettingHelper(activity);
                ush1.setOnSetUsageSettingFailedListener(() -> loadWidget.setGone());
                ush1.setOnSetUsageSettingSuccessListener(getUsageSettings -> loadWidget.setGone());
                ush1.setUsageSetting(attr);
            });
            ush.setOnGetUsageSettingsFailedListener(() -> loadWidget.setGone());
            ush.getUsageSetting();
            return;
        }
        // 用户设定的时间如果不为0--> 则正常提交
        UsageSettingHelper ush = new UsageSettingHelper(activity);
        ush.setOnGetUsageSettingsSuccessListener(attr -> {
            attr.setTimeLimitTimes(total);
            UsageSettingHelper ush1 = new UsageSettingHelper(activity);
            ush1.setOnSetUsageSettingSuccessListener(getUsageSettings -> loadWidget.setGone());
            ush1.setOnSetUsageSettingFailedListener(() -> loadWidget.setGone());
            ush1.setUsageSetting(attr);
        });
        ush.setOnGetUsageSettingsFailedListener(() -> loadWidget.setGone());
        ush.getUsageSetting();
    }

    /**
     * 保存流量警告值 & 弹出剩余流量提示
     *
     * @param value
     */
    private void saveAlertAndShowPop(int value) {
        // 1.保存警告值
        pop_alert.dismiss();
        ShareUtils.set(RootCons.USAGE_LIMIT_DEFAULT, value);
        if (value != -1) {
            // 2.弹出剩余流量
            loadWidget.setVisibles();
            UsageSettingHelper ush = new UsageSettingHelper(activity);
            ush.setOnGetUsageSettingsFailedListener(() -> loadWidget.setGone());
            ush.setOnGetUsageSettingsSuccessListener(attr -> {
                if (count == 0) {// 1.首次显示
                    float usedData = attr.getUsedData() * 1f;
                    float monthlyPlan = attr.getMonthlyPlan() * 1f;
                    if (monthlyPlan > 0) {// 2.非无限流量的情况下
                        String des = getString(R.string.home_usage_over_redial_message);// 3.超出月流量--> 显示警告
                        if (usedData < monthlyPlan) {// 3.没有超出--> 显示剩余百分比
                            String per_s = (int) (((monthlyPlan - usedData) / monthlyPlan) * 100) + "%";
                            des = String.format(getString(R.string.about_x_data_remain), per_s);
                        }
                        toastLong(des);// 4.吐司提示
                    }
                    count++;
                }
                loadWidget.setGone();
            });
            ush.getUsageSetting();
        }
    }

    /**
     * 弹出吐司
     *
     * @param resId
     */
    private void toast(int resId) {
        toast(resId, 2000);
    }

    /**
     * 弹出吐司
     *
     * @param content
     */
    private void toastLong(String content) {
        toast(content, 3000);
    }

}
