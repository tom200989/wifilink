package com.alcatel.wifilink.root.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.ArrayRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.app.SmartLinkV3App;
import com.alcatel.wifilink.root.bean.ConnectedList;
import com.alcatel.wifilink.root.bean.Extender_GetHotspotListResult;
import com.alcatel.wifilink.root.bean.FeedbackPhotoBean;
import com.alcatel.wifilink.root.bean.Other_DeviceBean;
import com.alcatel.wifilink.root.bean.Other_SMSContactSelf;
import com.alcatel.wifilink.root.bean.SMSContactList;
import com.alcatel.wifilink.root.bean.SMSContentList;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.helper.TimerHelper;
import com.alcatel.wifilink.root.ue.activity.HomeRxActivity;
import com.alcatel.wifilink.root.ue.activity.LoginRxActivity;
import com.alcatel.wifilink.root.widget.PopupWindows;
import com.p_xhelper_smart.p_xhelper_smart.bean.GetLoginStateBean;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetLoginStateHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.GetSystemInfoHelper;
import com.p_xhelper_smart.p_xhelper_smart.helper.HeartBeatHelper;
import com.tcl.token.ndk.JniTokenUtils;

import java.io.File;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qianli.ma on 2017/7/10.
 */

public class OtherUtils {

    public static boolean TEST = true;

    private OnSwVersionListener onSwVersionListener;
    private OnHwVersionListener onHwVersionListener;
    private OnCustomizedVersionListener onCustomizedVersionListener;
    public static List<Object> timerList = new ArrayList<>();
    public static List<Object> homeTimerList = new ArrayList<>();// 仅存放自动退出定时器
    public static List<PopupWindows> popList = new ArrayList<>();
    public static OnHeartBeatListener onHeartBeatListener;

    /**
     * 获取当前正在运行的Activity名(全名)
     */
    public static String getCurrentActivityFullName(Context context) {
        ActivityManager am = (ActivityManager) context.getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTasks = am.getRunningTasks(5);
        return runningTasks.get(0).topActivity.getClass().getSimpleName();
    }

    /**
     * 获取文件后缀名 (.png .mp4 ...)
     *
     * @param filename
     */
    public static String getExe(String filename) {
        return filename.substring(filename.lastIndexOf("."), filename.length());
    }

    /**
     * 是否为MW120新型设备
     *
     * @param deviceName 设备名称
     * @return true:是
     */
    public static boolean isMw120(String deviceName) {
        if (!TextUtils.isEmpty(deviceName)) {
            return deviceName.toLowerCase().startsWith(Cons.MW_SERIAL);
        } else {
            return false;
        }

    }

    /**
     * 转换FID集合
     *
     * @param fids
     * @return
     */
    public static String getFidAppendString(List<String> fids) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < fids.size(); i++) {
            buffer.append(fids.get(i));
            if (i > 0) {
                buffer.append(";");
            }
        }
        return buffer.toString();
    }


    /**
     * 获取提交feedback时需要拼接的加密字符
     *
     * @param uid
     * @return
     */
    public static String getCommitFeedbackHead(String uid) {
        String encrypt = "";
        try {
            JniTokenUtils.EncryptInfo encryptInfo = JniTokenUtils.newEncryptInfo();
            JniTokenUtils.getEncryptInfo(uid.getBytes("UTF-8"), encryptInfo);
            // (; sign=YOUR_SIGN; timestamp=YOUR_TIMESTAMP; newtoken=YOUR_NEW_TOKEN)
            encrypt = ";sign=" + encryptInfo.random + ";timestamp=" + encryptInfo.timestamp + ";newtoken=" + encryptInfo.encryptkey + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    /**
     * 排除当前连接的热点
     *
     * @param ori_hotpots 搜索到的热点集合
     * @param currentSSID 当前连接的热点SSID
     * @return
     */
    public static List<Extender_GetHotspotListResult.HotspotListBean> excludeCurrentHotpot(List<Extender_GetHotspotListResult.HotspotListBean> ori_hotpots, String currentSSID) {
        List<Extender_GetHotspotListResult.HotspotListBean> newLists = new ArrayList<>();
        for (Extender_GetHotspotListResult.HotspotListBean ori : ori_hotpots) {
            if (ori.getSSID().equals(currentSSID)) {
                if (ori.getConnectState() == 1) {
                    continue;
                }
            }
            newLists.add(ori);
        }
        return newLists;
    }

    /**
     * 批量转换SSID并提出空字段的WIFI
     *
     * @param hotspotListBeans
     * @return
     */
    public static List<Extender_GetHotspotListResult.HotspotListBean> turnSSISBatch(List<Extender_GetHotspotListResult.HotspotListBean> hotspotListBeans) {
        List<Extender_GetHotspotListResult.HotspotListBean> rehbs = new ArrayList<>();
        for (Extender_GetHotspotListResult.HotspotListBean hb : hotspotListBeans) {
            if (hb.getSSID().toLowerCase().contains("\\x00\\x00\\x00")) {
                continue;
            } else {
                rehbs.add(hb);
            }

        }
        for (Extender_GetHotspotListResult.HotspotListBean hb : rehbs) {
            Logs.t("ma_ssid").ii(hb.getSSID());
            hb.setSSID(turnUrlCode(hb.getSSID()));
        }

        return rehbs;
    }


    /**
     * 进行url decode
     *
     * @param content
     * @return
     */
    public static String turnUrlCode(String content) {
        String backup = content;
        try {
            // 1.生成正则表达式
            String regex = "(\\\\x\\w[0-9A-Fa-f]){3}";
            Pattern pattern = Pattern.compile(regex);
            Matcher match = pattern.matcher(content);
            // 2.轮询查找匹配
            while (match.find()) {
                // 3.每循环得到匹配的字符串--> \xE8\xBF\x99
                String substring = content.substring(match.start(), match.end());
                // 4.把匹配出来的字符串进行 [ \x ] 替换 [ % ]--> %E8%BF%99
                String replace = substring.replace("\\x", "%");
                // 5.把转义好的字符进行URL解码--> 得到中文
                String decode = URLDecoder.decode(replace, "UTF-8");
                // 6. 把截取的原文「\xE8\xBF\x99」直接替换成中文 --> 中
                backup = backup.replace(substring, decode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return backup;
    }

    /**
     * 获取wifi extender signal 强度
     *
     * @param signalStrength
     * @return
     */
    public static Drawable transferWifiExtenderSignal(int signalStrength) {
        SmartLinkV3App context = SmartLinkV3App.getInstance();
        if (signalStrength <= 0) {
            return context.getResources().getDrawable(R.drawable.wifi_ex_signal0);
        } else if (signalStrength == 1) {
            return context.getResources().getDrawable(R.drawable.wifi_ex_signal1);
        } else if (signalStrength == 2) {
            return context.getResources().getDrawable(R.drawable.wifi_ex_signal2);
        } else if (signalStrength == 3) {
            return context.getResources().getDrawable(R.drawable.wifi_ex_signal3);
        } else if (signalStrength >= 4) {
            return context.getResources().getDrawable(R.drawable.wifi_ex_signal4);
        }
        return context.getResources().getDrawable(R.drawable.wifi_ex_signal0);
    }

    /**
     * 转换设备列表
     *
     * @param connectedList
     * @return
     */
    public static List<Other_DeviceBean> transferDevicesbean(ConnectedList connectedList) {
        SmartLinkV3App context = SmartLinkV3App.getInstance();
        List<Other_DeviceBean> dbs = new ArrayList<>();
        String ip_field = context.getString(R.string.device_manage_ip);
        String mac_field = context.getString(R.string.device_manage_mac);
        String localIp = NetUtils.getLocalIPAddress().getHostAddress();

        List<ConnectedList.Device> ccls = connectedList.getConnectedList();
        if (ccls != null) {
            for (ConnectedList.Device ccl : ccls) {
                Other_DeviceBean ddb = new Other_DeviceBean();
                ddb.setDeviceIP(String.format(ip_field, ccl.getIPAddress()));
                ddb.setDeviceMac(String.format(mac_field, ccl.getMacAddress()));
                ddb.setDeviceName(ccl.getDeviceName());
                ddb.setPhone(ccl.getConnectMode() == Cons.WIFI_CONNECT);
                ddb.setHost(localIp.equals(ccl.getIPAddress()));
                if (localIp.equals(ccl.getIPAddress())) {
                    dbs.add(0, ddb);
                } else {
                    dbs.add(ddb);
                }

            }
        }
        return dbs;
    }

    /**
     * 获取后缀
     *
     * @param file
     * @return
     */
    private static String getSuffix(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return null;
        }
        String fileName = file.getName();
        if (fileName.equals("") || fileName.endsWith(".")) {
            return null;
        }
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index + 1).toLowerCase(Locale.US);
        } else {
            return null;
        }
    }

    /**
     * 获取元数据
     *
     * @param file
     * @return
     */
    public static String getMimeType(File file) {
        String suffix = getSuffix(file);
        if (suffix == null) {
            return "file/*";
        }
        String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(suffix);
        if (type != null || !type.isEmpty()) {
            return type;
        }
        return "file/*";
    }

    /**
     * 获取当前时间20180618181818
     *
     * @return
     */
    public static String getCurrentDateForString() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

    }

    /**
     * 获取一个随机会话id
     *
     * @return
     */
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 获取文件名
     *
     * @param url
     * @return
     */
    public static String getFileName(String url) {
        File file = new File(url);
        return file.getName();
    }

    /**
     * 判断recycle
     *
     * @param recyclerView
     * @return true:到底了
     */
    public static boolean isVisBottom(RecyclerView recyclerView) {
        GridLayoutManager layoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个完全可见子项的 position (注意:是完全可见)
        int lastVisibleItemPosition = layoutManager.findLastCompletelyVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前 RecyclerView 的所有子项个数O
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView 的滑动状态
        int state = recyclerView.getScrollState();
        if (visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == RecyclerView.SCROLL_STATE_IDLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取rcv满屏展示item个数
     *
     * @param context
     * @param rcvParent
     * @return
     */
    public static int getRcvMaxCount(Context context, RelativeLayout rcvParent) {
        ScreenSize.SizeBean size = ScreenSize.getSize(context);
        int itemHeight = (int) (size.width * 0.25f);// 单个item的高度(注意:要与配置文件填入的百分比相等)
        int rcvHeight = rcvParent.getMeasuredHeight();
        int rcvqHeight = rcvParent.getHeight();
        int line = rcvHeight / itemHeight;
        return rcvHeight % itemHeight > 0 ? (line + 1) * 4 : line * 4;
    }

    /**
     * 转换成图片连接地址
     *
     * @param context
     * @param bbs     图片对象
     * @return 地址集合
     */
    public static ArrayList<String> transferString(Context context, List<FeedbackPhotoBean> bbs) {
        ArrayList<String> urls = new ArrayList<>();
        for (FeedbackPhotoBean bb : bbs) {
            urls.add(bb.getUrl());
        }
        return urls;
    }

    /**
     * 把地址转换成bitmap
     *
     * @param urls 图片连接集合
     * @return FeedbackPhotoBean
     */
    public static List<FeedbackPhotoBean> transferBitmap(Context context, List<String> urls) {
        List<FeedbackPhotoBean> bbs = new ArrayList<>();
        for (String url : urls) {
            FeedbackPhotoBean ftb = new FeedbackPhotoBean();
            ftb.setBitmap(FormatTools.getInstance().file2ThumboBitmap(context, url, -1));
            ftb.setUrl(url);
            bbs.add(ftb);
        }
        return bbs;
    }

    /**
     * 获取一张默认的图片
     *
     * @param context
     * @return
     */
    private static Bitmap getDefaultFrame(Context context) {
        return FormatTools.getInstance().drawable2Bitmap(context.getResources().getDrawable(R.drawable.test_feedback));
    }

    /**
     * 把毫秒转换成xx:yy形式表达
     *
     * @param duration
     * @return
     */
    public static String transferLong2Duration(long duration) {
        long sec = duration / 1000;// 总毫秒转总秒
        long min = sec / 60;// 分
        long remain_sec = sec % 60;// 剩余的秒
        return min + ":" + remain_sec;
    }

    /**
     * 获取某个contactId下所有的smsid
     *
     * @param scList
     * @return
     */
    public static List<Long> getAllSmsIdByOneSession(SMSContentList scList) {
        List<Long> smsIds = new ArrayList<>();
        for (SMSContentList.SMSContentBean scb : scList.getSMSContentList()) {
            smsIds.add(scb.getSMSId());
        }
        return smsIds;
    }

    /**
     * 根据SP读取到的值获取数组中的字符
     *
     * @param arr     需要匹配的字符数组
     * @param include 数组中可能包含的字符
     * @return
     */
    public static String getAlert(String[] arr, int include) {
        if (include == -1) {
            return SmartLinkV3App.getInstance().getString(R.string.ergo_20181010_not_reminded);
        }
        String alert = "90%";
        for (String s : arr) {
            if (s.contains(String.valueOf(include))) {
                alert = s;
                break;
            }
        }
        return alert;
    }

    /**
     * 获取字符数组资源
     *
     * @param context
     * @return
     */
    public static String[] getResArr(Context context, @ArrayRes int resId) {
        return context.getResources().getStringArray(resId);
    }


    /**
     * 隐藏软键盘
     */
    public static void hideKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 显示软键盘
     */
    public static void showKeyBoard(Activity context) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInputFromInputMethod(context.getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    /**
     * 设置编辑域编辑状态
     *
     * @param et
     * @param isEditable
     */
    public static void setEdittextEditable(EditText et, boolean isEditable) {
        et.setFocusable(isEditable);
        et.setFocusableInTouchMode(isEditable);
    }


    /**
     * 判断编辑域是否为空
     *
     * @param strs
     * @return
     */
    public static boolean isEmptys(String... strs) {
        for (String str : strs) {
            if (TextUtils.isEmpty(str)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断编辑域是否为空
     *
     * @param ets
     * @return
     */
    public static boolean isEmptys(EditText... ets) {
        for (EditText editText : ets) {
            if (TextUtils.isEmpty(getEdContent(editText))) {
                return true;
            }
        }
        return false;
    }

    /**
     * IP地址匹配
     *
     * @param content
     * @return
     */
    public static boolean ipMatch(String content) {
        String ipRule = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."// 1
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."// 2
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."// 3
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";   // 4
        return Pattern.matches(ipRule, content);
    }

    /**
     * IP、submask高级匹配
     *
     * @param ipAddress
     */
    public static boolean ipSuperMatch(String ipAddress) {
        if (!ipMatch(ipAddress)) {
            Logs.t("ma_ip").vv("ip_phone address: " + "init check not match");
            return false;
        }
        Logs.t("ma_ip").vv("ip_phone address: " + ipAddress);
        if (TextUtils.isEmpty(ipAddress)) {// 空值
            Logs.t("ma_ip").vv("ip_phone address: " + "empty");
            return false;
        }
        if (!ipAddress.contains(".")) {// 不含点
            Logs.t("ma_ip").vv("ip_phone address: " + "not dot");
            return false;
        }
        String[] address = ipAddress.split("\\.");
        for (String s : address) {// 全部是点没有数字
            if (TextUtils.isEmpty(s)) {
                return false;
            }
        }
        Logs.t("ma_ip").vv("ip_phone address: " + "normal");
        List<Integer> ips = new ArrayList<>();
        for (String s : address) {
            ips.add(Integer.valueOf(s));
        }

        int num0 = 0;
        try {// 避免位数不够
            num0 = ips.get(0);
        } catch (Exception e) {
            num0 = 0;
        }

        int num1 = 0;
        try {// 避免位数不够
            num1 = ips.get(1);
        } catch (Exception e) {
            num1 = 0;
        }

        int num2 = 0;
        try {// 避免位数不够
            num2 = ips.get(2);
        } catch (Exception e) {
            num2 = 0;
        }

        int num3 = 0;
        try {// 避免位数不够
            num3 = ips.get(3);
        } catch (Exception e) {
            num3 = 0;
        }

        Logs.t("ma_ip").vv(num0 + ":" + num1 + ":" + num2 + ":" + num3);
        if ((num0 <= 0 || num0 == 127 || num0 > 223) || // num0
                    (num1 < 0 || num1 > 255) || // num1
                    (num2 < 0 || num2 > 255) || // num2
                    (num3 <= 0 || num3 >= 255)) {// num3
            return false;
        } else {
            return true;
        }
    }

    /**
     * 启动心跳定时器
     *
     * @param oriActivity 当前context
     * @param acTimeout   出错时的目标地址
     * @return 定时器辅助
     */
    public static TimerHelper startHeartBeat(Activity oriActivity, Class acTimeout, Class acLogout) {

        TimerHelper heartTimerAll = new TimerHelper(oriActivity) {
            @Override
            public void doSomething() {
                // is wifi effect?
                boolean isWifi = OtherUtils.isWifiConnect(oriActivity);
                if (isWifi) {
                    HeartBeatHelper xHeartBeatHelper = new HeartBeatHelper();
                    xHeartBeatHelper.setOnHeartBeatSuccessListener(() -> {
                        if (onHeartBeatListener != null) {
                            onHeartBeatListener.onSucess();
                        }
                    });
                    xHeartBeatHelper.setOnHeartbeanFailedListener(() -> clear(oriActivity, acTimeout));
                    xHeartBeatHelper.heartbeat();
                    
                } else {// wifi失效
                    Logs.v("ma_unknown", "startHeartBeat1: is not Wifi");
                    Logs.v("ma_couldn_connect", "no wifi");
                    clear(oriActivity, acTimeout);
                }
            }
        };
        heartTimerAll.start(2000);
        return heartTimerAll;
    }

    /**
     * 清理并跳转
     *
     * @param oriActivity
     * @param acTimeout
     */
    private static void clear(Activity oriActivity, Class acTimeout) {
        // 出错, 跳转到目标界面
        Log.v("ma_couldn_connect", "clear startHeartBeat error");

        // 获取当前顶层的activity
        String currentActivitySimpleName = AppInfo.getCurrentActivitySimpleName(oriActivity);
        String loginName = LoginRxActivity.class.getSimpleName();
        String saveName = SPUtils.getInstance(oriActivity).getString(Cons.CURRENT_ACTIVITY, "");
        // 如果当前是处于登陆页面则不跳转
        if (!currentActivitySimpleName.equalsIgnoreCase(loginName) & // 不相等
                    !currentActivitySimpleName.contains(loginName) & // 不包含
                    !loginName.contains(currentActivitySimpleName) & // 不包含
                    !loginName.equalsIgnoreCase(saveName) & // 不相等
                    !loginName.contains(saveName) & // 不包含
                    !saveName.contains(loginName) // 不包含
        ) {
            CA.toActivity(oriActivity, acTimeout, false, true, false, 0);
        } else {
            Logs.t("ma_unknown").vv("OtherUtils--> startHeartBeat--> clear--> current is login");
        }

    }

    /**
     * 获取短信联系人列表并添加长按标记
     *
     * @param smsContactList
     * @return
     */
    public static List<Other_SMSContactSelf> getSMSSelfList(SMSContactList smsContactList) {
        List<Other_SMSContactSelf> smscs = new ArrayList<>();
        for (SMSContactList.SMSContact smsContact : smsContactList.getSMSContactList()) {
            // 新建自定义SMS Contact对象
            Other_SMSContactSelf scs = new Other_SMSContactSelf();
            scs.setSmscontact(smsContact);
            scs.setLongClick(false);
            smscs.add(scs);
        }
        return smscs;
    }

    /**
     * 修改联系人列表是否进入可删除状态(islongClick==true则为可删除)
     *
     * @param isLongClick
     */
    public static List<Other_SMSContactSelf> modifySMSContactSelf(List<Other_SMSContactSelf> smsContactSelves, boolean isLongClick) {
        for (Other_SMSContactSelf otherSmsContactSelf : smsContactSelves) {
            otherSmsContactSelf.setLongClick(isLongClick);
        }
        return smsContactSelves;
    }

    public interface OnHeartBeatListener {
        void onSucess();
    }

    public static void setOnHeartBeatListener(OnHeartBeatListener onHeartBeatListener) {
        OtherUtils.onHeartBeatListener = onHeartBeatListener;
    }

    /**
     * 停止心跳定时器
     *
     * @param timerHelper
     */
    public static void stopHeartBeat(TimerHelper timerHelper) {
        if (timerHelper != null) {
            timerHelper.stop();
        }
    }


    /**
     * 获取编辑域内容(去除空格)
     *
     * @param ed
     * @return
     */
    public static String getEdContent(EditText ed) {
        return ed.getText().toString().trim().replace(" ", "");
    }

    /**
     * 获取编辑域内容(不去除空格)
     *
     * @param ed
     * @return
     */
    public static String getEdContentIncludeSpace(EditText ed) {
        return ed.getText().toString().trim();
    }

    /**
     * 线程自关
     */
    public static void kill() {
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * 把光标置后
     *
     * @param et
     */
    public static void setLastSelection(EditText et) {
        String content = et.getText().toString();
        et.setSelection(content.length());//将光标移至文字末尾
    }


    /**
     * 拼接电话号码
     *
     * @param context
     * @param phoneNumber
     * @return
     */
    public static String stitchPhone(Context context, List<String> phoneNumber) {
        if (phoneNumber.size() == 0) {
            return context.getString(R.string.error_info);
        } else if (phoneNumber.size() == 1) {
            return phoneNumber.get(0);
        } else {
            StringBuffer sb = new StringBuffer();
            for (String s : phoneNumber) {
                sb.append(s).append(";");
            }
            return sb.toString();
        }
    }

    /**
     * 转换日期
     *
     * @param oriDate
     * @return
     */
    public static String transferDate(String oriDate) {
        Date summaryDate = C_DataUti.formatDateFromString(oriDate);// sms date
        String strTimeText = new String();
        if (summaryDate != null) {
            Date now = new Date();// now date
            if (now.getYear() == summaryDate.getYear() && now.getMonth() == summaryDate.getMonth() && now.getDate() == summaryDate.getDate()) {
                SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                strTimeText = format.format(summaryDate);
            } else {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                strTimeText = format.format(summaryDate);
            }
        }
        return strTimeText;
    }

    /**
     * 是否为定制的版本
     */
    public void isCustomVersion() {
        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            String customId = result.getSwVersion().split("_")[1];
            if (onCustomizedVersionListener != null) {
                onCustomizedVersionListener.getCustomizedStatus(customId.equalsIgnoreCase(Cons.SW_VERSION_E1));
            }
        });
        getSystemInfoHelper.setOnFwErrorListener(() -> {
            if (onCustomizedVersionListener != null) {
                onCustomizedVersionListener.getCustomizedStatus(false);
            }
        });
        getSystemInfoHelper.setOnAppErrorListener(() -> {
            if (onCustomizedVersionListener != null) {
                onCustomizedVersionListener.getCustomizedStatus(false);
            }
        });
        getSystemInfoHelper.getSystemInfo();
    }

    /**
     * 获取设备的软件版本号
     */
    public void getDeviceSwVersion() {

        // 1.需要加密的 [ 定制版本 ]
        String needEncryptVersionCustomId = Cons.SW_VERSION_E1;

        GetLoginStateHelper xGetLoginStateHelper = new GetLoginStateHelper();
        xGetLoginStateHelper.setOnGetLoginStateSuccessListener(getLoginStateBean -> {
            // 字段值为1: 一定需要加密
            if (getLoginStateBean.getPwEncrypt() ==  GetLoginStateBean.CONS_PWENCRYPT_ON) {
                onSwVersionListener.getVersion(true);
            } else {
                // 否则--> 获取系统信息:能使用systeminfo接口--> 判断是否为E1版本
                // 否则--> 获取系统信息:不能使用systeminfo接口--> 一定需要加密
                getSystemInfoImpl(needEncryptVersionCustomId);
            }
        });
        xGetLoginStateHelper.setOnGetLoginStateFailedListener(() -> {
            
        });
        xGetLoginStateHelper.getLoginState();
    }

            /* 访问systeminfo接口 */
    private void getSystemInfoImpl(String needEncryptVersionCustomId) {

        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            // 2.获取当前版本
            String currentVersion = result.getSwVersion();
            String customId = currentVersion.split("_")[1];// customId:E1、IA、01....
            if (onSwVersionListener != null) {
                // 如能获取到版本,则判断是否为[E1]定制版本
                if (customId.equalsIgnoreCase(needEncryptVersionCustomId) || customId.contains(needEncryptVersionCustomId)) {
                    onSwVersionListener.getVersion(true);
                } else {
                    onSwVersionListener.getVersion(false);
                }
            }
        });
        getSystemInfoHelper.setOnAppErrorListener(() -> {
            // 如获取不到则一定是需要加密的
            if (onSwVersionListener != null) {
                onSwVersionListener.getVersion(true);
            }
        });
        getSystemInfoHelper.setOnFwErrorListener(() -> {
            // 如获取不到则一定是需要加密的
            if (onSwVersionListener != null) {
                onSwVersionListener.getVersion(true);
            }
        });
        getSystemInfoHelper.getSystemInfo();
    }
    //     });
    // }


    /**
     * 获取硬件版本: HH70 OR HH40
     */
    public void getDeviceHWVersion() {
        // 1.需要加密的版本
        List<String> needEncryptVersions = new ArrayList<String>();
        needEncryptVersions.add("HH70");
        // 2.获取当前版本
        GetSystemInfoHelper getSystemInfoHelper = new GetSystemInfoHelper();
        getSystemInfoHelper.setOnGetSystemInfoSuccessListener(result -> {
            if (onHwVersionListener != null) {
                onHwVersionListener.getVersion(result.getSwVersion());
            }
        });
        getSystemInfoHelper.getSystemInfo();
    }


    /**
     * 获取引导页的view
     *
     * @param context
     * @return
     */
    public static List<View> getGuidePages(Context context) {
        List<View> pages = new ArrayList<View>();
        View page1 = View.inflate(context, R.layout.what_new_one, null);
        View page2 = View.inflate(context, R.layout.what_new_two, null);
        View page3 = View.inflate(context, R.layout.what_new_three, null);
        pages.add(page1);
        pages.add(page2);
        pages.add(page3);
        return pages;
    }

    /**
     * 检查WIFI是否有链接
     *
     * @param context
     * @return
     */
    public static boolean isWifiConnect(Context context) {
        SmartLinkV3App instance = SmartLinkV3App.getInstance();
        ConnectivityManager connMgr = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State wifiState = networkInfo.getState();
        return NetworkInfo.State.CONNECTED == wifiState;
    }

    /**
     * WIFI是否有连接
     *
     * @param context
     * @return
     */
    public static boolean isWiFiActive(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifi.getConnectionInfo();
        int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();
        if (wifi.isWifiEnabled() && ipAddress != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开并连接WIFI
     *
     * @param context
     * @return
     */
    public static boolean setWifiActive(Context context, boolean open) {
        if (open) {
            return false;/* 由于测试要求, 这里设置为:「不强制开启WIFI」 */
        }
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        return wifi.setWifiEnabled(open);
    }

    /**
     * 获取wifi网关
     *
     * @param context
     * @return
     */
    public static String getWifiGateWay(Context context) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcp = wifi.getDhcpInfo();
        String gateWay = transferNetMask(dhcp.gateway);
        return gateWay;
    }

    /**
     * 转换16进制的WIFI网关
     *
     * @param gateWayHex 16进制的网关
     * @return
     */
    private static String transferNetMask(long gateWayHex) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((int) (gateWayHex & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((gateWayHex >> 8) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((gateWayHex >> 16) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((gateWayHex >> 24) & 0xff)));
        return sb.toString();
    }


    /**
     * 清除域
     */
    public static void clearContexts(String clazz) {
        List<Activity> contexts = SmartLinkV3App.getContextInstance();
        for (Activity activity : contexts) {
            if (activity.getClass().getSimpleName().equalsIgnoreCase(clazz)) {// 与指定的类相同--> 跳过
                continue;
            }
            if (activity != null & !activity.isFinishing()) {
                activity.finish();
            }
        }
        contexts.clear();
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param sClass   服务的类名
     * @return true:代表正在运行，false代表服务没有正在运行
     */
    public static boolean isServiceWork(Context mContext, Class sClass) {
        String serviceName = sClass.getName();// 获取服务的全类名
        ActivityManager myAM = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);// 获取系统服务对象
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(1000);// 获取正在运行中的服务集合 -->1000为可能获取到的个数
        if (myList.size() <= 0) {// 判断集合是否有对象
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {// 遍历每一个服务对象
            String mName = myList.get(i).service.getClassName().toString();// 获取服务的类名
            if (mName.equalsIgnoreCase(serviceName) || mName.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 某些界面的跳转
     *
     * @param context
     */
    public static void loginSkip(Context context) {
        // boolean isWifiGuide = SP.getInstance(context).getBoolean(Cons.WIFI_GUIDE_FLAG, false);
        // if (isWifiGuide) {/* 进入过了 */
        //     // 是否进入过流量设置界面
        //     boolean isDataPlan = SP.getInstance(context).getBoolean(Cons.DATA_PLAN_FLAG, false);
        //     if (isDataPlan) {
        //         CA.toActivity(context, HomeActivity.class, false, true, false, 0);
        //     } else {
        //         CA.toActivity(context, DataPlanActivity.class, false, true, false, 0);
        //     }
        // } else {/* 没有进入过 */
        //     CA.toActivity(context, WifiGuideActivity.class, false, true, false, 0);
        // }
    }

    /**
     * 清除全部的定时器
     */
    public static void clearAllTimer() {
        for (Object o : timerList) {
            // 辅助类的情况
            if (o instanceof TimerHelper) {
                TimerHelper th = (TimerHelper) o;
                th.stop();
            }

            // 任务
            if (o instanceof TimerTask) {
                TimerTask tk = (TimerTask) o;
                tk.cancel();
                tk = null;
            }

            // 定时器
            if (o instanceof Timer) {
                Timer t = (Timer) o;
                t.cancel();
                t.purge();
                t = null;
            }

            // 任务集
            if (o instanceof Map) {
                Map<Activity, Intent> map = (Map<Activity, Intent>) o;
                Set<Activity> acts = map.keySet();
                for (Activity act : acts) {
                    act.stopService(map.get(act));
                }
            }

        }
        timerList.clear();
    }

    /**
     * 停止全局定时器
     */
    public static void stopHomeTimer() {

        for (Object o : homeTimerList) {
            if (o instanceof TimerTask) {
                TimerTask tk = (TimerTask) o;
                tk.cancel();
                tk = null;
            }
            if (o instanceof Timer) {
                Timer t = (Timer) o;
                t.cancel();
                t.purge();
                t = null;
            }
        }
        homeTimerList.clear();

        if (HomeRxActivity.autoLogoutTask != null) {
            HomeRxActivity.autoLogoutTask.cancel();
            HomeRxActivity.autoLogoutTask = null;
        }

        if (HomeRxActivity.autoLogoutTimer != null) {
            HomeRxActivity.autoLogoutTimer.cancel();
            HomeRxActivity.autoLogoutTimer.purge();
            HomeRxActivity.autoLogoutTimer = null;
        }
    }

    /**
     * 显示等待进度条
     *
     * @param context
     */
    public static ProgressDialog showProgressPop(Context context) {
        ProgressDialog pgd = new ProgressDialog(context);
        pgd.setMessage(context.getString(R.string.connecting));
        pgd.setCanceledOnTouchOutside(false);
        if (!((Activity) context).isFinishing()) {
            pgd.show();
        }
        return pgd;
    }

    /**
     * 显示等待进度条
     *
     * @param context
     */
    public static ProgressDialog showProgressPop(Context context, String content) {
        ProgressDialog pgd = new ProgressDialog(context);
        pgd.setMessage(content);
        pgd.setCanceledOnTouchOutside(false);
        pgd.show();
        return pgd;
    }

    /**
     * 隐藏等待进度条
     *
     * @param pd
     */
    public static void hideProgressPop(ProgressDialog pd) {
        if (pd != null) {
            pd.dismiss();
            pd = null;
        }
    }

    public static String getLanguageFromPhone() {
        Locale dl = Locale.getDefault();
        return dl.getLanguage();
    }

    /**
     * 切换语言
     *
     * @param activity
     */
    public static void transferLanguage(Activity activity) {
        /* 一共有3个地方用到切换语言的方法, BaseActivityWithBack.java、OtherUtils.java、SettingLanguageActivity.java */
        /* 修改时需要这三个地方同时修改 */
        // 初始化PreferenceUtil
        PreferenceUtil.init(activity);
        // 根据上次的语言设置，重新设置语言
        String language = PreferenceUtil.getString(C_Constants.Language.LANGUAGE, "");
        Lgg.t("language_ma").vv("get language == " + language);
        if (!"".equals(language)) {
            // 设置应用语言类型
            Resources resources = activity.getResources();
            Configuration config = resources.getConfiguration();
            DisplayMetrics dm = resources.getDisplayMetrics();
            if (language.equals(C_Constants.Language.ENGLISH)) {
                config.locale = Locale.ENGLISH;
            } else if (language.equals(C_Constants.Language.ARABIC)) {
                // 阿拉伯语
                config.locale = new Locale(C_Constants.Language.ARABIC);
            } else if (language.equals(C_Constants.Language.GERMENIC)) {
                // 德语
                config.locale = Locale.GERMANY;
            } else if (language.equals(C_Constants.Language.ESPANYOL)) {
                // 西班牙语
                // config.locale = new Locale(C_Constants.Language.ESPANYOL);
                config.locale = new Locale(C_Constants.Language.ESPANYOL, "MX");
            } else if (language.equals(C_Constants.Language.ITALIAN)) {
                // 意大利语
                config.locale = Locale.ITALIAN;
            } else if (language.equals(C_Constants.Language.FRENCH)) {
                // 法语
                config.locale = Locale.FRENCH;
            } else if (language.equals(C_Constants.Language.SERBIAN)) {
                // 塞尔维亚
                config.locale = new Locale(C_Constants.Language.SERBIAN);
            } else if (language.equals(C_Constants.Language.CROATIAN)) {
                // 克罗地亚
                config.locale = new Locale(C_Constants.Language.CROATIAN);
            } else if (language.equals(C_Constants.Language.SLOVENIAN)) {
                // 斯洛文尼亚
                config.locale = new Locale(C_Constants.Language.SLOVENIAN);
            } else if (language.equals(C_Constants.Language.POLAND)) {
                // 波兰语
                config.locale = new Locale(C_Constants.Language.POLAND);
            } else if (language.equals(C_Constants.Language.RUSSIAN)) {
                // 俄语
                config.locale = new Locale(C_Constants.Language.RUSSIAN);
            } else if (language.equals(C_Constants.Language.CHINA)) {
                // 台湾
                config.locale = new Locale(C_Constants.Language.CHINA, "TW");
            }
            resources.updateConfiguration(config, dm);

            // 保存设置语言的类型
            PreferenceUtil.commitString(C_Constants.Language.LANGUAGE, language);
        }
    }

    /**
     * @return 当前语言
     */
    public static String getCurrentLanguage() {
        return PreferenceUtil.getString(C_Constants.Language.LANGUAGE, "");
    }

    /* -------------------------------------------- INTERFACE -------------------------------------------- */
    public interface OnSwVersionListener {
        void getVersion(boolean needToEncrypt);
    }

    public void setOnSwVersionListener(OnSwVersionListener onSwVersionListener) {
        this.onSwVersionListener = onSwVersionListener;
    }

    public interface OnHwVersionListener {
        void getVersion(String deviceVersion);
    }

    public void setOnHwVersionListener(OnHwVersionListener onHwVersionListener) {
        this.onHwVersionListener = onHwVersionListener;
    }

    public interface OnCustomizedVersionListener {
        void getCustomizedStatus(boolean isCustomized);
    }

    public void setOnCustomizedVersionListener(OnCustomizedVersionListener onCustomizedVersionListener) {
        this.onCustomizedVersionListener = onCustomizedVersionListener;
    }
}

