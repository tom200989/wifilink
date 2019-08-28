package com.alcatel.wifilink.root.ue.frag;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.UsageHelper;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.utils.RootCons;
import com.alcatel.wifilink.root.widget.HH70_LoadWidget;
import com.alcatel.wifilink.root.widget.NormalWidget;
import com.hiber.cons.TimerState;
import com.hiber.tools.ShareUtils;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetUsageRecordHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageRecordClearHelper;

import butterknife.BindView;

/**
 * Created by qianli.ma on 2017/12/8 0008.
 */

public class UsageRxFrag extends BaseFrag {

    @BindView(R.id.iv_usage_rx_back)
    ImageView ivBack;
    @BindView(R.id.tv_usage_rx_homeNetwork_traffic)
    TextView tvNetworkTraffic;
    @BindView(R.id.tv_usage_rx_homeNetwork_time)
    TextView tvNetworkTime;
    @BindView(R.id.tv_usage_rx_roaming_traffic)
    TextView tvRoamingTraffic;
    @BindView(R.id.tv_usage_rx_roaming_time)
    TextView tvRoamingTime;
    @BindView(R.id.bt_usage_rx_resetStatist)
    Button btResetStatist;
    @BindView(R.id.tv_usage_rx_mobileNetworkSetting)
    TextView tvMobileNetworkSetting;
    @BindView(R.id.dg_usage_rx_ok)
    NormalWidget dgUsageRxOk;
    @BindView(R.id.hh70_loading)
    HH70_LoadWidget hh70LoadWidget;

    private String popTitle;
    private String popCancel;
    private String popReset;
    private String resetFailed;
    private String reseting;
    private String resetSuccess;
    private UsageHelper usageHelper;
    private long usedData_l = 0L;
    private long monthly_l = 0L;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_usagerx;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o,view,s);
        initRes();
        initOnClick();
        //开启定时器
        timerState = TimerState.ON_BUT_OFF_WHEN_HIDE_AND_PAUSE;
    }

    @Override
    public void setTimerTask(){
        getHomeNetworkMonthly();// 已经使用 / 月计划流量
        getRoamingAndConnTime();// 获取漫游信息
    }

    private void initRes() {
        popTitle = getString(R.string.hh70_reset_month_usage);
        popCancel = getString(R.string.hh70_cancel);
        popReset = getString(R.string.hh70_reset);
        reseting = getString(R.string.hh70_resetting);
        resetFailed = getString(R.string.hh70_reset_try_again);
        resetSuccess = getString(R.string.hh70_success);
        usageHelper = new UsageHelper(getActivity());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {

        }
    }

    /**
     * 获取漫游信息
     */
    private void getRoamingAndConnTime() {
        usageHelper.setOnRoamingListener(result -> {// 有漫游
            long roamUseData = result.getRoamUseData();
            UsageHelper.Usage usageByte = UsageHelper.getUsageByte(getActivity(), roamUseData);
            String roamUsage = usageByte.usage;
            String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
            if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                roamUsage = roamUsage.replace(".", ",") + " ";
            }
            String roam_unit = usageByte.unit;
            if (TextUtils.isEmpty(roamUsage)) {
                roamUsage = "0";
            }
            tvRoamingTraffic.setText(String.valueOf(roamUsage + roam_unit));
            // 获取当前连接时间
            int currTime = (int) result.getCurrConnTimes();
            UsageHelper.Times currTime_s = UsageHelper.getUsedTimeForSec(getActivity(), currTime);
            String rhour = currTime_s.hour;
            String rmin = currTime_s.min;
            boolean isNoRHour = "0".equalsIgnoreCase(rhour);
            if (TextUtils.isEmpty(rmin)) {
                rmin = "0";
            }
            tvRoamingTime.setText(isNoRHour ? rmin + getString(R.string.hh70_min_s) : rhour + getString(R.string.hh70_hr_s) + rmin + getString(R.string.hh70_min_s));
            // 获取总连接时间
            int tConnTimes = (int) result.getTConnTimes();
            UsageHelper.Times usedTime = UsageHelper.getUsedTimeForSec(getActivity(), tConnTimes);
            String nhour = usedTime.hour;
            String nmin = usedTime.min;
            boolean isNoTHour = "0".equalsIgnoreCase(nhour);
            String currentLanguage1 = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
            boolean isRussian = currentLanguage1.contains(RootCons.LANGUAGES.RUSSIAN);
            String hour = nhour + getString(R.string.hh70_hr_s);
            String min = nmin + getString(R.string.hh70_min_s);
            if (isRussian) {
                hour = nhour + getString(R.string.hh70_hr_s);
                min = nmin + getString(R.string.hh70_min_s);
            }
            String time = isNoTHour ? min : hour + min;
            tvNetworkTime.setText(time);
        });
        usageHelper.setOnNoRoamingListener(result -> {// 没有漫游
            String currentLanguage1 = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
            boolean isRussian = currentLanguage1.contains(RootCons.LANGUAGES.RUSSIAN);
            int tConnTimes = (int) result.getTConnTimes();
            String noRoamingUsage = "0.00" + getString(R.string.hh70_mb);
            if (isRussian) {
                noRoamingUsage = "0.00" + " " + getString(R.string.hh70_mb);
            }
            String noRoamingTime = "0" + getString(R.string.hh70_min_s);
            if (isRussian) {
                noRoamingTime = "0 " + getString(R.string.hh70_min_s);
            }
            UsageHelper.Times usedTime = UsageHelper.getUsedTimeForSec(getActivity(), tConnTimes);
            String nhour = usedTime.hour;
            String nmin = usedTime.min;
            boolean isNoTHour = "0".equalsIgnoreCase(nhour);

            String hour = nhour + getString(R.string.hh70_hr_s);
            String min = nmin + getString(R.string.hh70_min_s);
            if (isRussian) {
                noRoamingUsage = noRoamingUsage.replace(".", ",");
                hour = nhour + getString(R.string.hh70_hr_s);
                min = nmin + getString(R.string.hh70_min_s);
            }
            String time = isNoTHour ? min : hour + min;
            tvNetworkTime.setText(time);
            tvRoamingTraffic.setText(noRoamingUsage);
            tvRoamingTime.setText(noRoamingTime);
        });
        usageHelper.getRoamingInfo();
    }

    /**
     * 已经使用 / 月计划流量
     */
    private void getHomeNetworkMonthly() {
        // 获取已使用流量
        GetUsageRecordHelper xGetUsageRecordHelper = new GetUsageRecordHelper();
        xGetUsageRecordHelper.setOnGetUsageRecordSuccess(result -> {
            // 处理已经使用的流量
            usedData_l = result.getHUseData();
            UsageHelper.Usage hUseDataByte = UsageHelper.getUsageByte(getActivity(), usedData_l);
            String used = hUseDataByte.usage;
            String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
            if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                used = used.replace(".", ",") + " ";
            }
            String used_unit = hUseDataByte.unit;
            String usedData_s = used + used_unit;
            // 处理月流量
            UsageHelper.Usage monthByte = UsageHelper.getUsageByte(getActivity(), monthly_l);
            String month = monthByte.usage;
            if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                month = month.replace(".", ",") + " ";
            }
            String month_unit = monthByte.unit;
            String monthly_s = month + month_unit;
            // 显示流量使用情况
            String normal = usedData_s + "/" + monthly_s;
            tvNetworkTraffic.setText(monthly_l <= 0 ? usedData_s : normal);
        });
        xGetUsageRecordHelper.getUsageRecord(UsageHelper.getCurrentTime());
        // 获取月流量
        UsageSettingHelper helper = new UsageSettingHelper(activity);
        helper.setOnGetUsageSettingsSuccessListener(result -> {
            //  月流量计划
            monthly_l = result.getMonthlyPlan();
            UsageHelper.Usage monthByte = UsageHelper.getUsageByte(getActivity(), monthly_l);
            String month = monthByte.usage;
            String currentLanguage = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY,"");
            if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                month = month.replace(".", ",") + " ";
            }
            String month_unit = monthByte.unit;
            String monthly_s = month + month_unit;
            // 处理已经使用流量
            UsageHelper.Usage hUseDataByte = UsageHelper.getUsageByte(getActivity(), usedData_l);
            String used = hUseDataByte.usage;
            if (currentLanguage.contains(RootCons.LANGUAGES.RUSSIAN)) {
                used = used.replace(".", ",") + " ";
            }
            String used_unit = hUseDataByte.unit;
            String usedData_s = used + used_unit;
            // 显示流量使用情况
            String normal = usedData_s + "/" + monthly_s;
            tvNetworkTraffic.setText(monthly_l <= 0 ? usedData_s : normal);
        });
        helper.getUsageSetting();
    }

    /**
     * 点击事件
     */
    private void initOnClick(){
        ivBack.setOnClickListener(v -> {
            toFrag(getClass(),mainFrag.class,null,false);
        });
        btResetStatist.setOnClickListener(v -> clickResetButton());
        tvMobileNetworkSetting.setOnClickListener(v -> {
            lastFrag = UsageRxFrag.class;
            toFrag(getClass(),MobileNetworkFrag.class,null,false);
        });
    }

    /**
     * 点击了reset按钮
     */
    private void clickResetButton() {
        dgUsageRxOk.setVisibility(View.VISIBLE);
        dgUsageRxOk.setTitle(popReset);
        dgUsageRxOk.setDes(popTitle);
        dgUsageRxOk.setOnBgClickListener(() -> {});
        dgUsageRxOk.setOnCancelClickListener(() -> dgUsageRxOk.setVisibility(View.GONE));
        dgUsageRxOk.setOnOkClickListener(this::resetRecord);
    }

    /**
     * 清空记录
     */
    private void resetRecord() {
        hh70LoadWidget.setVisibles();
        String currentTime = UsageHelper.getCurrentTime();
        SetUsageRecordClearHelper xSetUsageRecordClearHelper = new SetUsageRecordClearHelper();
        xSetUsageRecordClearHelper.setOnSetUsageRecordClearSuccessListener(() -> {
            hh70LoadWidget.setGone();
            toast(resetSuccess,2000);
        });
        xSetUsageRecordClearHelper.setOnSetUsageRecordClearFailListener(() -> {
            hh70LoadWidget.setGone();
            toast(resetFailed,2000);
        });
        xSetUsageRecordClearHelper.setUsageRecordClear(currentTime);
    }

    @Override
    public boolean onBackPresss() {
        if (dgUsageRxOk.getVisibility() == View.VISIBLE) {
            dgUsageRxOk.setVisibility(View.GONE);
            return true;
        }
        // 返回主页
        toFrag(getClass(),mainFrag.class,null,false);
        return true;
    }
}
