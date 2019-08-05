package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by qianli.ma on 2018/5/29 0029.
 */

public class OpenCloseExtenderView extends RelativeLayout implements View.OnClickListener {

    private ImageView ivBg;
    private TextView ivCancel;
    private TextView ivOk;

    public OpenCloseExtenderView(Context context) {
        this(context, null, 0);
    }

    public OpenCloseExtenderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OpenCloseExtenderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.widget_extender_open_close, this);
        ivBg = (ImageView) findViewById(R.id.iv_extender_open_close_bg);
        ivCancel = (TextView) findViewById(R.id.tv_extender_open_close_cancel);
        ivOk = (TextView) findViewById(R.id.tv_extender_open_close_ok);
        ivBg.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        ivOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_extender_open_close_bg:
                setVisibility(GONE);
                break;
            case R.id.tv_extender_open_close_cancel:
                setVisibility(GONE);
                break;
            case R.id.tv_extender_open_close_ok:
                clickNext(null);
                setVisibility(GONE);
                break;
        }
    }

    private OnClickokListener onClickokListener;

    // 接口OnClickokListener
    public interface OnClickokListener {
        void click(Object attr);
    }

    // 对外方式setOnClickokListener
    public void setOnClickokListener(OnClickokListener onClickokListener) {
        this.onClickokListener = onClickokListener;
    }

    // 封装方法clickNext
    private void clickNext(Object attr) {
        if (onClickokListener != null) {
            onClickokListener.click(attr);
        }
    }
}
