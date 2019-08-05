package com.alcatel.wifilink.root.helper;
/*
 * Created by qianli.ma on 2018/8/9 0009.
 */

import com.alcatel.wifilink.root.utils.Logs;

public class WpaPsdHelper {
    public static boolean isMatch(String content) {
        return isASCIIData(content);
    }

    /**
     * 是否为32-127范围
     */
    private static boolean isASCIIData(String content) {
        // 判断是否包含非法字符
        String[] reg = {",", "\"", ":", ";", "&", "\\"};
        for (String str : reg) {
            Logs.t("ma_reg").ii(str);
            if (content.contains(str)) {
                return false;
            }
        }
        // 再判断是否为32 - 127
        for (int i = 0; i < content.length(); i++) {
            int ascii = (int) content.charAt(i);
            if (ascii < 32 | ascii > 127) {
                return false;
            }
        }
        return true;
    }
}
