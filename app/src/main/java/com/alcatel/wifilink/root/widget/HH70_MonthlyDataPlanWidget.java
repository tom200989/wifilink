package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootUtils;

/**
 * Created by wzhiqiang on 2019/8/28
 */
public class HH70_MonthlyDataPlanWidget extends RelativeLayout {

    private RadioButton radioButtonGb;
    private RadioButton radioButtonMb;
    private RadioGroup radioGroup;
    private EditText monthlyNumber;

    public HH70_MonthlyDataPlanWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_MonthlyDataPlanWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_MonthlyDataPlanWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_monthly_data_plan, this);
        ImageView ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(null);

        Button tv_delete_cancel = findViewById(R.id.tv_smsdetail_detele_cancel);
        Button tv_delete_confirm = findViewById(R.id.tv_smsdetail_detele_ok);
        tv_delete_cancel.setOnClickListener(v -> setVisibility(GONE));
        tv_delete_confirm.setOnClickListener(v -> clickOkNext());

        monthlyNumber = findViewById(R.id.monthly_number);
        radioGroup = findViewById(R.id.radiogroup_monthly_plan);
        radioButtonGb = findViewById(R.id.radio_monthly_plan_gb);
        radioButtonMb = findViewById(R.id.radio_monthly_plan_mb);

    }

    public void setMonthlyNumberString(String monthPlan) {
        monthlyNumber.setText(monthPlan);
    }

    public String getMonthLyPLanEd() {
        return RootUtils.getEDText(monthlyNumber, true);
    }

    public void setRadioMbCheck() {
        radioButtonMb.setChecked(true);
    }

    public void setRadioGbCheck() {
        radioButtonGb.setChecked(true);
    }

    public boolean isRadioMbChecked() {
        return radioButtonMb.getId() == radioGroup.getCheckedRadioButtonId();
    }

    public boolean isRadioGbChecked() {
        return radioButtonGb.getId() == radioGroup.getCheckedRadioButtonId();
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
