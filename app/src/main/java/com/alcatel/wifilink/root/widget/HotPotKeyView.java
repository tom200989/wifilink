package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.Extender_GetHotspotListResult;
import com.alcatel.wifilink.root.app.wifiApp;
import com.alcatel.wifilink.root.utils.ToastTool;

/**
 * Created by qianli.ma on 2018/5/28 0028.
 */

public class HotPotKeyView extends RelativeLayout implements View.OnClickListener {

    private RelativeLayout rlAll;// 总布局
    private ImageView ivBackground;// 背景
    private TextView tvSSid;// SSID
    private EditText etPassword;// password
    private ImageView ivShowHide;// 显示或隐藏按钮
    private TextView tvCancel;// 取消
    private TextView tvOk;// 确认
    private Extender_GetHotspotListResult.HotspotListBean hb;
    private Drawable hide;
    private Drawable show;


    public HotPotKeyView(Context context) {
        this(context, null, 0);
    }

    public HotPotKeyView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HotPotKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.widget_extender_hotpot_key_view, this);
        show = context.getResources().getDrawable(R.drawable.password_show);
        hide = context.getResources().getDrawable(R.drawable.password_hide);
        rlAll = (RelativeLayout) findViewById(R.id.rl_widget_hotpot_key_all);
        ivBackground = (ImageView) findViewById(R.id.iv_widget_hotpot_key_background);
        tvSSid = (TextView) findViewById(R.id.tv_widget_hotpot_key_ssid);
        etPassword = (EditText) findViewById(R.id.et_widget_hotpot_key_password);
        ivShowHide = (ImageView) findViewById(R.id.iv_widget_hotpot_key_showOrhide);
        ivShowHide.setImageDrawable(hide);
        tvCancel = (TextView) findViewById(R.id.tv_widget_hotpot_key_cancel);
        tvOk = (TextView) findViewById(R.id.tv_widget_hotpot_key_ok);
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
            ToastTool.show(wifiApp.getInstance(), R.string.password_is_not_allowed_to_be_empty);
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
    public void show(Extender_GetHotspotListResult.HotspotListBean hb) {
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
        hideKeyBoard();
    }

    /**
     * 隐藏软键盘
     */
    private void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) wifiApp.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(this.getWindowToken(), 0);
        }
    }

    private OnClickOkListener onClickOkListener;

    // 接口OnClickOkListener
    public interface OnClickOkListener {
        void click(Extender_GetHotspotListResult.HotspotListBean hb, String inputPassword);
    }

    // 对外方式setOnClickOkListener
    public void setOnClickOkListener(OnClickOkListener onClickOkListener) {
        this.onClickOkListener = onClickOkListener;
    }

    // 封装方法clickNext
    private void clickNext(Extender_GetHotspotListResult.HotspotListBean hb, String inputPassword) {
        if (onClickOkListener != null) {
            onClickOkListener.click(hb, inputPassword);
        }
    }
}
