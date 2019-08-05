package com.alcatel.wifilink.root.utils;

import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.EditText;

import com.alcatel.wifilink.R;

import java.io.File;
import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class C_CommonUtil {
    /**
     * 转换流量(带单位)
     *
     * @param context
     * @param traffic
     * @return
     */
    public static String ConvertTrafficMB(Context context, long traffic) {
        BigDecimal trafficMB;
        BigDecimal trafficGB;

        BigDecimal temp = new BigDecimal(traffic);
        BigDecimal divide = new BigDecimal(1024);
        BigDecimal divideM = new BigDecimal(1024l * 1024l);
        trafficMB = temp.divide(divideM, 2, BigDecimal.ROUND_HALF_UP);
        if (trafficMB.compareTo(divide) >= 0) {
            trafficGB = trafficMB.divide(divide, 2, BigDecimal.ROUND_HALF_UP);
            return trafficGB + context.getResources().getString(R.string.gb_text);
        } else {
            return trafficMB + context.getResources().getString(R.string.mb_text);
        }
    }


    /**
     * 转换流量(纯数字)
     *
     * @param context
     * @param traffic
     * @return
     */
    public static TrafficBean ConvertTraffic(Context context, long traffic, int dimen) {
        float tempTB = traffic * 1f / 1024 / 1024 / 1024 / 1024;
        float tempGB = traffic * 1f / 1024 / 1024 / 1024;
        float tempMB = traffic * 1f / 1024 / 1024;
        float tempKB = traffic * 1f / 1024;

        String num = "";
        String type = "";
        if (tempTB >= 1) {
            num = String.valueOf(mathRound(tempTB));
            type = "TB";
        } else if (tempGB >= 1) {
            num = String.valueOf(mathRound(tempGB));
            type = "GB";
        } else if (tempMB >= 1) {
            num = String.valueOf(mathRound(tempMB));
            type = "MB";
        } else {
            num = String.valueOf(mathRound(tempKB));
            type = "KB";
        }


        TrafficBean tb = new C_CommonUtil().new TrafficBean();
        tb.num = Float.valueOf(num);
        tb.type = type;

        return tb;
    }

    /* 保留两位小数(float) */
    public static float mathRound(float value) {
        long dimension = 100;// 两位小数此处为100, 4位小数, 此处为10000....依此类推
        return (float) (Math.round(value * dimension)) / dimension;
    }
    
    /* 保留两位小数(double) */
    public static double mathRound(double value) {
        long dimension = 100;// 两位小数此处为100, 4位小数, 此处为10000....依此类推
        return (double) (Math.round(value * dimension)) / dimension;
    }

    public class TrafficBean {
        public float num;
        public String type;
    }

	/*public static String ConvertTrafficToString(Context context,long traffic){
                                BigDecimal trafficB;
		BigDecimal trafficKB;
		BigDecimal trafficMB;
		BigDecimal trafficGB;
		
		BigDecimal temp = new BigDecimal(traffic);
		BigDecimal divide = new BigDecimal(1024);
		//trafficB = temp.divide(divide, 2, 1);
		trafficB = temp;
		if(trafficB.compareTo(divide) >= 0){
			trafficKB = trafficB.divide(divide,2,1);
			if(trafficKB.compareTo(divide) >= 0){
				trafficMB = trafficKB.divide(divide,2,1);
				if(trafficMB.compareTo(divide) >= 0){
					trafficGB = trafficMB.divide(divide,2,1);
					return trafficGB.doubleValue() + context.getResources().getString(R.string.home_GB);
				}else{
					return trafficMB.doubleValue() + context.getResources().getString(R.string.home_MB);
				}
			}else{
				return trafficKB.doubleValue() + context.getResources().getString(R.string.home_KB);
			}
		}else{
			return trafficB.doubleValue() + context.getResources().getString(R.string.home_B);
		}
	}*/


    @SuppressWarnings("deprecation")
    public static String getIp(Context ctx) {
        WifiManager wifi_service = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        return Formatter.formatIpAddress(dhcpInfo.ipAddress);
    }

    //ScreenDimen
    public static int getScreenWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static void openWebPage(Context context, String strWeb) {
        Uri uri = Uri.parse(strWeb);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        context.startActivity(it);
    }

    public static int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    private static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }

    //判断是否快速点击的工具类
    private static long lastClickTime;

    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 700) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    // 生成文件
    public File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
        }
    }

    // 删除SD卡上的单个文件方法
    public static boolean deleteFile(String fileName) {

        File file = new File(fileName);
        if (file == null || !file.exists() || file.isDirectory()) {
            return false;
        }
        file.delete();

        return true;
    }

    /**
     * 禁止EditText输空格+换行键
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText) {
        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n"))
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     *
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText) {

        InputFilter filter = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find())
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符+空格+换行键
     *
     * @param editText
     */
    public static void setEditTextInputFilter(EditText editText) {

        InputFilter filterSpace = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ") || source.toString().contentEquals("\n"))
                    return "";
                else
                    return null;
            }
        };

        InputFilter filterChat = new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if (matcher.find())
                    return "";
                else
                    return null;
            }
        };
        editText.setFilters(new InputFilter[]{filterSpace, filterChat});
    }

}
