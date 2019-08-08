package com.alcatel.wifilink.root.ue.frag;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.helper.UsageHelper;
import com.alcatel.wifilink.root.helper.UsageSettingHelper;
import com.alcatel.wifilink.root.ue.activity.HomeRxActivity;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.C_Constants;
import com.alcatel.wifilink.root.utils.FraHelpers;
import com.alcatel.wifilink.root.utils.Lgg;
import com.alcatel.wifilink.root.utils.Logs;
import com.alcatel.wifilink.root.utils.OtherUtils;
import com.alcatel.wifilink.root.utils.ToastUtil_m;
import com.alcatel.wifilink.root.utils.fraghandler.FragmentBackHandler;
import com.alcatel.wifilink.root.widget.DialogOkWidget;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetUsageRecordHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.SetUsageRecordClearHelper;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by qianli.ma on 2017/12/8 0008.
 */

public class UsageRxFragment extends Fragment implements FragmentBackHandler {

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
    DialogOkWidget dgUsageRxOk;
    Unbinder unbinder;

    private View inflate;
    private HomeRxActivity activity;
    private FraHelpers fraHelpers;
    private Class[] clazz;
    private TimerHelper timerHelper;
    private String popTitle;
    private String popCancel;
    private String popReset;
    private ProgressDialog pgd;
    private String resetFailed;
    private String reseting;
    private String resetSuccess;
    private UsageHelper usageHelper;
    private long usedData_l = 0L;
    private long monthly_l = 0L;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = (HomeRxActivity) getActivity();
        inflate = View.inflate(getActivity(), R.layout.fragment_usagerx, null);
        unbinder = ButterKnife.bind(this, inflate);
        resetUi();
        initRes();
        initData();
        return inflate;
    }

    private void initRes() {
        fraHelpers = activity.fraHelpers;
        clazz = activity.clazz;
        popTitle = getString(R.string.reset_monthly_data_usage_statistics);
        popCancel = getString(R.string.cancel);
        popReset = getString(R.string.reset);
        reseting = getString(R.string.resetting);
        resetFailed = getString(R.string.couldn_t_reset_try_again);
        resetSuccess = getString(R.string.success);
        usageHelper = new UsageHelper(getActivity());
    }

    private void initData() {
        timerHelper = new TimerHelper(getActivity()) {
            @Override
            public void doSomething() {
                getHomeNetworkMonthly();// 已经使用 / 月计划流量
                getRoamingAndConnTime();// 获取漫游信息
            }
        };
        timerHelper.start(3000);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            initData();
            resetUi();
        } else {
            stopTimer();
        }
    }

    private void resetUi() {
        activity.tabFlag = Cons.TAB_USAGE;
        activity.llNavigation.setVisibility(View.GONE);
        activity.rlBanner.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopTimer();
    }

    /**
     * 获取漫游信息
     */
    private void getRoamingAndConnTime() {
        usageHelper.setOnRoamingListener(result -> {// 有漫游
            long roamUseData = result.getRoamUseData();
            UsageHelper.Usage usageByte = UsageHelper.getUsageByte(getActivity(), roamUseData);
            String roamUsage = usageByte.usage;
            if (OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN)) {
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
            tvRoamingTime.setText(isNoRHour ? rmin + getString(R.string.min_s) : rhour + getString(R.string.hr_s) + rmin + getString(R.string.min_s));
            // 获取总连接时间
            int tConnTimes = (int) result.getTConnTimes();
            UsageHelper.Times usedTime = UsageHelper.getUsedTimeForSec(getActivity(), tConnTimes);
            String nhour = usedTime.hour;
            String nmin = usedTime.min;
            boolean isNoTHour = "0".equalsIgnoreCase(nhour);
            boolean isRussian = OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN);
            String hour = nhour + getString(R.string.hr_s);
            String min = nmin + getString(R.string.min_s);
            if (isRussian) {
                hour = nhour + getString(R.string.hr_s);
                min = nmin + getString(R.string.min_s);
            }
            String time = isNoTHour ? min : hour + min;
            tvNetworkTime.setText(time);
        });
        usageHelper.setOnNoRoamingListener(result -> {// 没有漫游
            boolean isRussian = OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN);
            int tConnTimes = (int) result.getTConnTimes();
            Logs.t("ma_usages").ii("tConnTimes: " + tConnTimes);
            String noRoamingUsage = "0.00" + getString(R.string.mb_text);
            if (isRussian) {
                noRoamingUsage = "0.00" + " " + getString(R.string.mb_text);
            }
            String noRoamingTime = "0" + getString(R.string.min_s);
            if (isRussian) {
                noRoamingTime = "0 " + getString(R.string.min_s);
            }
            UsageHelper.Times usedTime = UsageHelper.getUsedTimeForSec(getActivity(), tConnTimes);
            String nhour = usedTime.hour;
            String nmin = usedTime.min;
            boolean isNoTHour = "0".equalsIgnoreCase(nhour);

            String hour = nhour + getString(R.string.hr_s);
            String min = nmin + getString(R.string.min_s);
            if (isRussian) {
                noRoamingUsage = noRoamingUsage.replace(".", ",");
                hour = nhour + getString(R.string.hr_s) ;
                min = nmin + getString(R.string.min_s);
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
        GetUsageRecordHelper getUsageRecordHelper = new GetUsageRecordHelper();
        getUsageRecordHelper.setOnGetUsageRecordSuccess(result -> {
            // 处理已经使用的流量
            usedData_l = result.getHUseData();
            UsageHelper.Usage hUseDataByte = UsageHelper.getUsageByte(getActivity(), usedData_l);
            String used = hUseDataByte.usage;
            if (OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN)) {
                used = used.replace(".", ",") + " ";
            }
            String used_unit = hUseDataByte.unit;
            String usedData_s = used + used_unit;
            // 处理月流量
            UsageHelper.Usage monthByte = UsageHelper.getUsageByte(getActivity(), monthly_l);
            String month = monthByte.usage;
            if (OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN)) {
                month = month.replace(".", ",") + " ";
            }
            String month_unit = monthByte.unit;
            String monthly_s = month + month_unit;
            // 显示流量使用情况
            String normal = usedData_s + "/" + monthly_s;
            tvNetworkTraffic.setText(monthly_l <= 0 ? usedData_s : normal);
        });
        getUsageRecordHelper.getUsageRecord(UsageHelper.getCurrentTime());


        // 获取月流量
        UsageSettingHelper helper = new UsageSettingHelper(activity);
        helper.setOngetSuccessListener(result -> {
            //  月流量计划
            monthly_l = result.getMonthlyPlan();
            UsageHelper.Usage monthByte = UsageHelper.getUsageByte(getActivity(), monthly_l);
            String month = monthByte.usage;
            if (OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN)) {
                month = month.replace(".", ",") + " ";
            }
            String month_unit = monthByte.unit;
            String monthly_s = month + month_unit;
            // 处理已经使用流量
            UsageHelper.Usage hUseDataByte = UsageHelper.getUsageByte(getActivity(), usedData_l);
            String used = hUseDataByte.usage;
            if (OtherUtils.getCurrentLanguage().equalsIgnoreCase(C_Constants.Language.RUSSIAN)) {
                used = used.replace(".", ",") + " ";
            }
            String used_unit = hUseDataByte.unit;
            String usedData_s = used + used_unit;
            // 显示流量使用情况
            String normal = usedData_s + "/" + monthly_s;
            tvNetworkTraffic.setText(monthly_l <= 0 ? usedData_s : normal);
        });
        helper.setOnErrorListener(() -> {

        });
        helper.getUsageSetting();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_usage_rx_back,// 回退
            R.id.bt_usage_rx_resetStatist,// 重设
            R.id.tv_usage_rx_mobileNetworkSetting})// 跳转到设置
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_usage_rx_back:
                stopTimer();
                fraHelpers.transfer(clazz[0]);
                break;
            case R.id.bt_usage_rx_resetStatist:
                clickResetButton();
                break;
            case R.id.tv_usage_rx_mobileNetworkSetting:
                EventBus.getDefault().postSticky(Cons.TAB_USAGE);
                activity.fraHelpers.transfer(activity.clazz[Cons.TAB_MOBILE_NETWORK]);
                break;
        }
    }

    /**
     * 点击了reset按钮
     */
    private void clickResetButton() {
        dgUsageRxOk.setVisibility(View.VISIBLE);
        dgUsageRxOk.setTitle(popReset);
        dgUsageRxOk.setDes(popTitle);
        dgUsageRxOk.setOnBgClickListener(() -> Lgg.t("usage").ii("click not area"));
        dgUsageRxOk.setOnCancelClickListener(() -> dgUsageRxOk.setVisibility(View.GONE));
        dgUsageRxOk.setOnOkClickListener(this::resetRecord);
    }

    /**
     * 清空记录
     */
    private void resetRecord() {
        pgd = OtherUtils.showProgressPop(getActivity());
        String currentTime = UsageHelper.getCurrentTime();
        SetUsageRecordClearHelper xSetUsageRecordClearHelper = new SetUsageRecordClearHelper();
        xSetUsageRecordClearHelper.setOnSetUsageRecordClearSuccessListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            toast(resetSuccess);
        });
        xSetUsageRecordClearHelper.setOnSetUsageRecordClearFailListener(() -> {
            OtherUtils.hideProgressPop(pgd);
            toast(resetFailed);
        });
        xSetUsageRecordClearHelper.setUsageRecordClear(currentTime);
    }

    public void toast(int resId) {
        ToastUtil_m.show(getActivity(), resId);
    }

    public void toastLong(int resId) {
        ToastUtil_m.showLong(getActivity(), resId);
    }

    public void toast(String content) {
        ToastUtil_m.show(getActivity(), content);
    }

    public void to(Class ac, boolean isFinish) {
        CA.toActivity(getActivity(), ac, false, isFinish, false, 0);
    }

    public void stopTimer() {
        if (timerHelper != null) {
            timerHelper.stop();
        }
    }

    @Override
    public boolean onBackPressed() {
        if (dgUsageRxOk.getVisibility() == View.VISIBLE) {
            dgUsageRxOk.setVisibility(View.GONE);
            return true;
        }
        // 返回主页
        activity.fraHelpers.transfer(clazz[0]);
        return true;
    }
}
