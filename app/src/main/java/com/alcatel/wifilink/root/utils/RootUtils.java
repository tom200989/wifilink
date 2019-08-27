package com.alcatel.wifilink.root.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.net.wifi.WifiManager;
import android.support.annotation.ArrayRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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
import com.tcl.token.ndk.JniTokenUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
@SuppressLint("SimpleDateFormat")
public class RootUtils {

    /**
     * 是否为［MW］设备
     *
     * @param devName 设备名
     * @return T:是
     */
    public static boolean isMWDEV(String devName) {
        // 先转换成小写
        devName = devName.toLowerCase();
        // 再遍历判断
        for (String dev : RootCons.FREE_SHARING_DEVICE) {
            if (devName.contains(dev)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为71设备
     *
     * @param devName 设备名
     * @return T:是
     */
    public static boolean isHH71(String devName) {
        // 先转换成小写
        devName = devName.toLowerCase();
        // 再遍历判断
        for (String dev : RootCons.HH71_DEVICE) {
            if (devName.contains(dev)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否为4X设备
     *
     * @param devName 设备名
     * @return T:是
     */
    public static boolean isHH4X(String devName) {
        // 先转换成小写
        devName = devName.toLowerCase();
        // 再遍历判断
        for (String dev : RootCons.HH4X_DEVICE) {
            if (devName.contains(dev)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取edittext文本内容并去除空格
     *
     * @param editText 文本域
     * @return 内容
     */
    public static String getEDText(EditText editText) {
        return editText.getText().toString().trim().replace(" ", "");
    }

    /**
     * 获取edittext文本内容并去除空格
     *
     * @param editText 文本域
     * @param isSpace  是否保留空格(只消除两端)
     * @return 内容
     */
    public static String getEDText(EditText editText, boolean isSpace) {
        if (isSpace) {
            return editText.getText().toString().trim();
        } else {
            return editText.getText().toString().trim().replace(" ", "");
        }
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
     */
    public static void setEdEnable(EditText et, boolean enable) {
        et.setFocusable(enable);
        et.setFocusableInTouchMode(enable);
    }

    /**
     * 开启或关闭WIFI
     *
     * @param context 域
     * @param open    T:开
     * @return 是否已经开启
     */
    public static void setWifiOn(Context context, boolean open) {
        WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(open);
    }

    /**
     * 编辑与是否为空
     */
    public static boolean isEdEmpty(EditText... eds) {
        for (EditText editText : eds) {
            if (TextUtils.isEmpty(getEDText(editText))) {
                return true;
            }
        }
        return false;
    }

    /**
     * IP是否符合规则
     *
     * @param ip IP
     */
    private static boolean ipMatch(String ip) {
        String ipRule = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."// 1
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."// 2
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."// 3
                                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";   // 4
        return Pattern.matches(ipRule, ip);
    }

    /**
     * 是否全部达标
     *
     * @param ipAddress IP地址
     * @return T:达标
     */
    public static boolean isAllMatch(String ipAddress) {
        if (!ipMatch(ipAddress)) {
            return false;
        }
        if (TextUtils.isEmpty(ipAddress)) {// 空值
            return false;
        }
        if (!ipAddress.contains(".")) {// 不含点
            return false;
        }
        String[] address = ipAddress.split("\\.");
        for (String s : address) {// 全部是点没有数字
            if (TextUtils.isEmpty(s)) {
                return false;
            }
        }
        List<Integer> ips = new ArrayList<>();
        for (String s : address) {
            ips.add(Integer.valueOf(s));
        }

        int num0;
        try {// 避免位数不够
            num0 = ips.get(0);
        } catch (Exception e) {
            num0 = 0;
        }

        int num1;
        try {// 避免位数不够
            num1 = ips.get(1);
        } catch (Exception e) {
            num1 = 0;
        }

        int num2;
        try {// 避免位数不够
            num2 = ips.get(2);
        } catch (Exception e) {
            num2 = 0;
        }

        int num3;
        try {// 避免位数不够
            num3 = ips.get(3);
        } catch (Exception e) {
            num3 = 0;
        }

        return (num0 > 0 && num0 != 127 && num0 <= 223) && // num0
                       (num1 >= 0 && num1 <= 255) && // num1
                       (num2 >= 0 && num2 <= 255) && // num2
                       (num3 > 0 && num3 < 255);
    }

    /**
     * 是否符合正则
     */
    public static boolean isMatchRule(String content) {
        String splChrs = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[\\-\\+\\!\\^\\$\\@\\#\\&\\*])[A-Za-z0-9\\-\\+\\!\\^\\$\\@\\#\\&\\*]{4,16}$";
        Pattern pattern = Pattern.compile(splChrs);
        Matcher matcher = pattern.matcher(content);
        return matcher.find();
    }

    /**
     * 获取提交feedback时需要拼接的加密字符
     */
    public static String getCommitFeedbackHead(String uid) {
        String encrypt = "";
        try {
            JniTokenUtils.EncryptInfo encryptInfo = JniTokenUtils.newEncryptInfo();
            JniTokenUtils.getEncryptInfo(uid.getBytes(StandardCharsets.UTF_8), encryptInfo);
            // (; sign=YOUR_SIGN; timestamp=YOUR_TIMESTAMP; newtoken=YOUR_NEW_TOKEN)
            encrypt = ";sign=" + encryptInfo.random + ";timestamp=" + encryptInfo.timestamp + ";newtoken=" + encryptInfo.encryptkey + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encrypt;
    }

    /**
     * 转换FID集合
     */
    public static String getFidAppendString(List<String> fids) {
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < fids.size(); i++) {
            buffer.append(fids.get(i));
            if (i > 0) {
                buffer.append(";");
            }
        }
        return buffer.toString();
    }

    /**
     * 转换成图片连接地址
     *
     * @param bbs 图片对象
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
     * 根据SP读取到的值获取数组中的字符
     *
     * @param arr     需要匹配的字符数组
     * @param include 数组中可能包含的字符
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
     * 获取短信联系人列表并添加长按标记
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
     */
    public static List<Other_SMSContactSelf> modifySMSContactSelf(List<Other_SMSContactSelf> smsContactSelves, boolean isLongClick) {
        for (Other_SMSContactSelf otherSmsContactSelf : smsContactSelves) {
            otherSmsContactSelf.setLongClick(isLongClick);
        }
        return smsContactSelves;
    }

    /**
     * 获取字符数组资源
     */
    public static String[] getResArr(Context context, @ArrayRes int resId) {
        return context.getResources().getStringArray(resId);
    }

    /**
     * 进行url decode
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
     */
    public static Drawable transferWifiExtenderSignal(int signalStrength) {
        SmartLinkV3App context = SmartLinkV3App.getInstance();
        if (signalStrength <= 0) {
            return ContextCompat.getDrawable(context, R.drawable.wifi_ex_signal0);
        } else if (signalStrength == 1) {
            return ContextCompat.getDrawable(context, R.drawable.wifi_ex_signal1);
        } else if (signalStrength == 2) {
            return ContextCompat.getDrawable(context, R.drawable.wifi_ex_signal2);
        } else if (signalStrength == 3) {
            return ContextCompat.getDrawable(context, R.drawable.wifi_ex_signal3);
        } else {
            return ContextCompat.getDrawable(context, R.drawable.wifi_ex_signal4);
        }
    }

    /**
     * 排除当前连接的热点
     *
     * @param ori_hotpots 搜索到的热点集合
     * @param currentSSID 当前连接的热点SSID
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
     */
    public static List<Extender_GetHotspotListResult.HotspotListBean> turnSSISBatch(List<Extender_GetHotspotListResult.HotspotListBean> hotspotListBeans) {
        List<Extender_GetHotspotListResult.HotspotListBean> rehbs = new ArrayList<>();
        for (Extender_GetHotspotListResult.HotspotListBean hb : hotspotListBeans) {
            if (!hb.getSSID().toLowerCase().contains("\\x00\\x00\\x00")) {
                rehbs.add(hb);
            }
        }
        for (Extender_GetHotspotListResult.HotspotListBean hb : rehbs) {
            hb.setSSID(turnUrlCode(hb.getSSID()));
        }

        return rehbs;
    }

    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param sClass 服务的类名
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
            String mName = myList.get(i).service.getClassName();// 获取服务的类名
            if (mName.equalsIgnoreCase(serviceName) || mName.contains(serviceName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 检查WIFI是否有链接
     */
    public static boolean isWifiConnect() {
        SmartLinkV3App instance = SmartLinkV3App.getInstance();
        ConnectivityManager connMgr = (ConnectivityManager) instance.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo.State wifiState = networkInfo.getState();
        return NetworkInfo.State.CONNECTED == wifiState;
    }

    /**
     * 转换日期
     */
    public static String transferDate(String oriDate) {
        Date summaryDate = RootUtils.transferDateForText(oriDate);// sms date
        String strTimeText = "";
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
     * 拼接电话号码
     */
    public static String stitchPhone(Context context, List<String> phoneNumber) {
        if (phoneNumber.size() == 0) {
            return context.getString(R.string.error_info);
        } else if (phoneNumber.size() == 1) {
            return phoneNumber.get(0);
        } else {
            StringBuilder sb = new StringBuilder();
            for (String s : phoneNumber) {
                sb.append(s).append(";");
            }
            return sb.toString();
        }
    }

    /**
     * 判断编辑域是否为空
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
     * 获取某个contactId下所有的smsid
     */
    public static List<Long> getAllSmsIdByOneSession(SMSContentList scList) {
        List<Long> smsIds = new ArrayList<>();
        for (SMSContentList.SMSContentBean scb : scList.getSMSContentList()) {
            smsIds.add(scb.getSMSId());
        }
        return smsIds;
    }

    /**
     * 转换设备列表
     */
    public static List<Other_DeviceBean> transferDevicesbean(ConnectedList connectedList) {
        SmartLinkV3App context = SmartLinkV3App.getInstance();
        List<Other_DeviceBean> dbs = new ArrayList<>();
        String ip_field = context.getString(R.string.device_manage_ip);
        String mac_field = context.getString(R.string.device_manage_mac);
        String localIp = Objects.requireNonNull(NetUtils.getLocalIPAddress()).getHostAddress();

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
     * 转换日期为字符
     */
    public static Date transferDateForText(String time) {
        String pattern;
        if (time.matches("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "dd-MM-yyyy HH:mm:ss";
        } else if (time.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "yyyy-MM-dd HH:mm:ss";
        } else if (time.matches("\\d{2}/\\d{2}/\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "dd/MM/yyyy HH:mm:ss";
        } else if (time.matches("\\d{4}/\\d{2}/\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "yyyy/MM/dd HH:mm:ss";
        } else if (time.matches("\\d{2}\\.\\d{2}\\.\\d{4} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "dd.MM.yyyy HH:mm:ss";
        } else if (time.matches("\\d{4}\\.\\d{2}\\.\\d{2} \\d{2}:\\d{2}:\\d{2}")) {
            pattern = "yyyy.MM.dd HH:mm:ss";
        } else {
            return null;
        }
        SimpleDateFormat sDate = new SimpleDateFormat(pattern);

        Date smsDate = null;
        try {
            smsDate = sDate.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return smsDate;
    }

    /**
     * @return 获取当前日期
     */
    public static String getCurrentDate() {
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date now = new Date();
        return sDate.format(now);
    }
}
