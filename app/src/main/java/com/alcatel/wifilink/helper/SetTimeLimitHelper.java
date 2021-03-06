package com.alcatel.wifilink.helper;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alcatel.wifilink.utils.RootUtils;

/**
 * Created by qianli.ma on 2017/12/13 0013.
 */

public class SetTimeLimitHelper {

    /**
     * 增加ED监听器
     */
    public static void addEdwatch(EditText etHour, EditText etMin) {
        // ethour 修改过程中的限定
        etHour.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int hour = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etHour)) ? "0" : RootUtils.getEDText(etHour));
                int min = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etMin)) ? "0" : RootUtils.getEDText(etMin));
                if (hour >= 12 & min > 0) {
                    etMin.setText("0");
                    etHour.setText(String.valueOf("12"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // ethour 得到焦点后的限定
        etHour.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int hour = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etHour)) ? "0" : RootUtils.getEDText(etHour));
                int min = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etMin)) ? "0" : RootUtils.getEDText(etMin));
                if (hour >= 12 & min > 0) {
                    etMin.setText("0");
                    etHour.setText(String.valueOf("12"));
                }
            }
        });

        // etMin 修改过程中的限定 
        etMin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int hour = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etHour)) ? "0" : RootUtils.getEDText(etHour));
                int min = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etMin)) ? "0" : RootUtils.getEDText(etMin));
                if (hour >= 12 & min != 0) {// 防止死循环必须把加入min!=0这个条件, 否则陷入onTextChanged死循环
                    etMin.setText("0");
                } else {
                    if (min > 59) {
                        etMin.setText(String.valueOf("59"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        // etMin 得到焦点后的限定
        etMin.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                int hour = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etHour)) ? "0" : RootUtils.getEDText(etHour));
                int min = Integer.valueOf(TextUtils.isEmpty(RootUtils.getEDText(etMin)) ? "0" : RootUtils.getEDText(etMin));
                if (hour >= 12 & min != 0) {
                    etMin.setText("0");
                } else {
                    if (min > 59) {
                        etMin.setText(String.valueOf("59"));
                    }
                }
            }
        });
    }
}
