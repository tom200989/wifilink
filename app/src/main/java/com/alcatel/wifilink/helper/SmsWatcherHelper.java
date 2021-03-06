package com.alcatel.wifilink.helper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.utils.ToastTool;

/**
 * Created by qianli.ma on 2018/1/5 0005.
 */

public class SmsWatcherHelper implements TextWatcher {

    private int maxLength = 1530;
    private Context context;

    public SmsWatcherHelper(Context context, EditText et) {
        this.context = context;
        et.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() >= maxLength) {
            String smsLength = context.getString(R.string.hh70_sms_enter_char);
            String content = String.format(smsLength, maxLength);
            ToastTool.show(context, content);
        }
    }
}
