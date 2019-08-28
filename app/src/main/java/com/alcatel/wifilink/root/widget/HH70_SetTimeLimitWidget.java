package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootUtils;

/**
 * Created by wzhiqiang on 2019/8/28
 */
public class HH70_SetTimeLimitWidget extends RelativeLayout {

    private EditText hrEt;
    private EditText minEt;

    public HH70_SetTimeLimitWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_SetTimeLimitWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_SetTimeLimitWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_set_time_limit, this);
        ImageView ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> { });

        Button tv_delete_cancel = findViewById(R.id.tv_smsdetail_detele_cancel);
        Button tv_delete_confirm = findViewById(R.id.tv_smsdetail_detele_ok);
        tv_delete_cancel.setOnClickListener(v -> setVisibility(GONE));
        tv_delete_confirm.setOnClickListener(v -> {
            clickOkNext();
        });

        hrEt = findViewById(R.id.dialog_time_limit_hr);
        minEt = findViewById(R.id.dialog_time_limit_min);
    }

    public void setHrString(String hourString) {
        hrEt.setText(hourString);
    }

    public void setMinString(String minString) {
        minEt.setText(minString);
    }

    public String getHrEd() {
        return RootUtils.getEDText(hrEt, true);
    }

    public String  getMinEd() {
        return RootUtils.getEDText(minEt, true);
    }


    public interface OnClickOkListener {
        void clickOk();
    }

    private OnClickOkListener onClickOkListener;

    //对外方式setOnClickOkListener
    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    //封装方法clickOkNext
    private void clickOkNext() {
        if (onClickOkListener != null) {
            onClickOkListener.clickOk();
        }
    }
}
