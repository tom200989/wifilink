package com.alcatel.wifilink.root.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.Extender_GetHotspotListBean;
import com.alcatel.wifilink.root.utils.RootUtils;
import com.alcatel.wifilink.root.utils.ToastTool;

/**
 * Created by qianli.ma on 2018/5/28 0028.
 */

public class HH70_HotPotKeyWidget extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout rlAll;// 总布局
    private ImageView ivBackground;// 背景
    private TextView tvSSid;// SSID
    private EditText etPassword;// password
    private ImageView ivShowHide;// 显示或隐藏按钮
    private TextView tvCancel;// 取消
    private TextView tvOk;// 确认
    private Extender_GetHotspotListBean.HotspotListBean hb;
    private Drawable hide;
    private Drawable show;
    private Context context;


    public HH70_HotPotKeyWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_HotPotKeyWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_HotPotKeyWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        View.inflate(context, R.layout.hh70_widget_extend_hotpot_key, this);
        show = context.getResources().getDrawable(R.drawable.password_show);
        hide = context.getResources().getDrawable(R.drawable.password_hide);
        rlAll = findViewById(R.id.rl_widget_hotpot_key_all);
        ivBackground = findViewById(R.id.iv_widget_hotpot_key_background);
        tvSSid = findViewById(R.id.tv_widget_hotpot_key_ssid);
        etPassword = findViewById(R.id.et_widget_hotpot_key_password);
        ivShowHide = findViewById(R.id.iv_widget_hotpot_key_showOrhide);
        ivShowHide.setImageDrawable(hide);
        tvCancel = findViewById(R.id.tv_widget_hotpot_key_cancel);
        tvOk = findViewById(R.id.tv_widget_hotpot_key_ok);
        ivBackground.setOnClickListener(this);
        ivShowHide.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_widget_hotpot_key_background:// 背景
                clear();
                break;
            case R.id.iv_widget_hotpot_key_showOrhide:// 显示或隐藏密码
                clickEyes();
                break;
            case R.id.tv_widget_hotpot_key_cancel:// 取消
                clear();
                break;
            case R.id.tv_widget_hotpot_key_ok:// OK
                clickOk();
                break;
        }
    }

    /**
     * 点击OK
     */
    private void clickOk() {
        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            ToastTool.show(context, R.string.hh70_pw_not_allow_empty);
        } else {
            clickNext(hb, password);
        }
    }

    /**
     * 点击密码显示或隐藏
     */
    private void clickEyes() {
        int inputType = etPassword.getInputType();
        ivShowHide.setImageDrawable(ivShowHide.getDrawable() == hide ? show : hide);
        etPassword.setInputType(inputType != 128 ? 128 : 129);// 128:隐藏密码 129:显示密码
        etPassword.setSelection(etPassword.getText().length());
    }

    /**
     * 显示密码面板
     */
    public void show(Extender_GetHotspotListBean.HotspotListBean hb) {
        this.hb = hb;
        tvSSid.setText(hb.getSSID());
        etPassword.setText("");
        rlAll.setVisibility(VISIBLE);
    }

    /**
     * 清空编辑域
     */
    public void clear() {
        etPassword.setText("");
        rlAll.setVisibility(GONE);
        setVisibility(GONE);
        hb = null;
        RootUtils.hideKeyBoard((Activity) context);
    }

    private OnClickOkListener onClickOkListener;

    // 接口OnClickOkListener
    public interface OnClickOkListener {
        void click(Extender_GetHotspotListBean.HotspotListBean hb, String inputPassword);
    }

    // 对外方式setOnClickOkListener
    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    // 封装方法clickNext
    private void clickNext(Extender_GetHotspotListBean.HotspotListBean hb, String inputPassword) {
        if (onClickOkListener != null) {
            onClickOkListener.click(hb, inputPassword);
        }
    }
}
