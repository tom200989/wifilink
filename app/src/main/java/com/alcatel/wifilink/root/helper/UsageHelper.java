package com.alcatel.wifilink.root.helper;

import android.content.Context;

import com.alcatel.wifilink.R;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetNetworkInfoBean;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetUsageRecordBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetNetworkRegisterStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetUsageRecordHelper;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by qianli.ma on 2017/11/24 0024.
 */

public class UsageHelper {

    private Context context;

    public UsageHelper(Context context) {
        this.context = context;
    }

    /**
     * 格式化日期
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        long time_l = System.currentTimeMillis();
        return sdf.format(time_l);
    }


    /**
     * 获取已经使用流量比率
     *
     * @param used
     * @param monthly
     * @return
     */
    public static int getRateUsed(double used, double monthly) {
        if (monthly <= 0) {
            return 10;// 最低10
        }
        double rate = mathRound(used * 1f / monthly);
        int v = (int) (rate * 100);
        v = v > 92 ? 92 : v;// 最大92
        return v;
    }

    /**
     * 获取单位以及换算(带小数)
     *
     * @param context
     * @param bytes
     * @return
     */
    public static Usage getUsageByte(Context context, double bytes) {
        double tempGB = bytes * 1f / 1024 / 1024 / 1024;
        double tempMB = bytes * 1f / 1024 / 1024;
        Usage usage = new Usage();
        if (tempGB >= 1) {
            usage.usage = String.valueOf(mathRound(tempGB));
            usage.unit = context.getString(R.string.hh70_gb);
        } else {
            int d = (int) mathRound(tempMB);
            usage.usage = String.valueOf(d);
            usage.unit = context.getString(R.string.hh70_mb);
        }
        return usage;
    }

    /**
     * 获取时间对象(传入时间以秒为单位)
     *
     * @param context
     * @param sec     总秒数
     * @return
     */
    public static Times getUsedTimeForSec(Context context, int sec) {
        int hours = sec / 3600;
        int mins = (sec % 3600) / 60;

        Times times = new Times();
        times.hour = String.valueOf(hours);
        times.min = String.valueOf(mins);

        return times;
    }

    /**
     * 获取时间对象(传入时间以分钟为单位)
     *
     * @param context
     * @param min     总分钟数
     * @return
     */
    public static Times getUsedTimeForMin(Context context, int min) {
        int hours = min / 60;
        int mins = min % 60;

        Times times = new Times();
        times.hour = String.valueOf(hours);
        times.min = String.valueOf(mins);

        return times;
    }

    /* 保留两位小数 */
    private static double mathRound(double value) {
        long dimension = 10;// 两位小数此处为100, 4位小数, 此处为10000....依此类推
        return (double) (Math.round(value * dimension)) / dimension;
    }

    /* 保留N位小数 */
    private static float mathRoundByDimension(float value, int dimen) {
        long dimension = dimen;// 两位小数此处如为100,则表示保留两位小数, 4位小数, 此处为10000....依此类推
        return (float) (Math.round(value * dimension)) / dimension;
    }

    public static class Usage {
        public String unit;
        public String usage;
    }

    public static class Times {
        public String hour;
        public String min;
    }

    /* -------------------------------------------- method2 -------------------------------------------- */

    /**
     * 获取流量超限状况
     */
    public void getOverUsage() {
        UsageSettingHelper getUsageSettingsHelper = new UsageSettingHelper(context);
        getUsageSettingsHelper.setOnGetUsageSettingsSuccessListener(result -> {
            long monthlyPlan = result.getMonthlyPlan();
            long usedData = result.getUsedData();
            if (usedData >= monthlyPlan) {
                overMonthlyNext();
            }
        });
        getUsageSettingsHelper.setOnGetUsageSettingsFailedListener(this::getUsageErrorNext);
        getUsageSettingsHelper.getUsageSetting();
    }

    private OnOverMonthlyListener onOverMonthlyListener;

    // 接口OnOverMonthlyListener
    public interface OnOverMonthlyListener {
        void overMonthly();
    }

    // 对外方式setOnOverMonthlyListener
    public void setOnOverMonthlyListener(OnOverMonthlyListener onOverMonthlyListener) {
        this.onOverMonthlyListener = onOverMonthlyListener;
    }

    // 封装方法overMonthlyNext
    private void overMonthlyNext() {
        if (onOverMonthlyListener != null) {
            onOverMonthlyListener.overMonthly();
        }
    }

    private OnGetUsageErrorListener onGetUsageErrorListener;

    // 接口OnGetUsageErrorListener
    public interface OnGetUsageErrorListener {
        void getUsageError();
    }

    // 对外方式setOnGetUsageErrorListener
    public void setOnGetUsageErrorListener(OnGetUsageErrorListener onGetUsageErrorListener) {
        this.onGetUsageErrorListener = onGetUsageErrorListener;
    }

    // 封装方法getUsageErrorNext
    private void getUsageErrorNext() {
        if (onGetUsageErrorListener != null) {
            onGetUsageErrorListener.getUsageError();
        }
    }

    /* -------------------------------------------- method3 -------------------------------------------- */

    /**
     * 获取漫游信息
     */
    public void getRoamingInfo() {
        String currentTime = UsageHelper.getCurrentTime();
        GetNetworkRegisterStateHelper xGetNetworkRegisterStateHelper = new GetNetworkRegisterStateHelper();
        xGetNetworkRegisterStateHelper.setOnRegisterSuccessListener(() -> {

            GetNetworkInfoHelper xGetNetworkInfoHelper = new GetNetworkInfoHelper();
            xGetNetworkInfoHelper.setOnGetNetworkInfoSuccessListener(networkInfoBean -> {

                            GetUsageRecordHelper xGetUsageRecordHelper = new GetUsageRecordHelper();
                            xGetUsageRecordHelper.setOnGetUsageRecordSuccess(result -> {
                                if (networkInfoBean.getRoaming() == GetNetworkInfoBean.CONS_ROAMING) {
                                    roamingNext(result);
                                } else {
                                    noRoamingNext(result);
                                }
                            });
                            xGetUsageRecordHelper.getUsageRecord(currentTime);

            });
            xGetNetworkInfoHelper.setOnGetNetworkInfoFailedListener(() -> {
                
            });
            xGetNetworkInfoHelper.getNetworkInfo();

        });
        xGetNetworkRegisterStateHelper.getNetworkRegisterState();
    }

    private OnNoRoamingListener onNoRoamingListener;

    // 接口OnNoRoamingListener
    public interface OnNoRoamingListener {
        void noRoaming(GetUsageRecordBean attr);
    }

    // 对外方式setOnNoRoamingListener
    public void setOnNoRoamingListener(OnNoRoamingListener onNoRoamingListener) {
        this.onNoRoamingListener = onNoRoamingListener;
    }

    // 封装方法noRoamingNext
    private void noRoamingNext(GetUsageRecordBean attr) {
        if (onNoRoamingListener != null) {
            onNoRoamingListener.noRoaming(attr);
        }
    }

    private OnRoamingListener onRoamingListener;

    // 接口OnRoamingListener
    public interface OnRoamingListener {
        void roaming(GetUsageRecordBean result);
    }

    // 对外方式setOnRoamingListener
    public void setOnRoamingListener(OnRoamingListener onRoamingListener) {
        this.onRoamingListener = onRoamingListener;
    }

    // 封装方法roamingNext
    private void roamingNext(GetUsageRecordBean attr) {
        if (onRoamingListener != null) {
            onRoamingListener.roaming(attr);
        }
    }

}
