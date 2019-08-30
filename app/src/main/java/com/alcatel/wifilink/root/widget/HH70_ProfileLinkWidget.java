package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/**
 * Created by wzhiqiang on 2019/8/28
 */
public class HH70_ProfileLinkWidget extends RelativeLayout {

    public HH70_ProfileLinkWidget(Context context) {
        this(context,null,0);
    }

    public HH70_ProfileLinkWidget(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public HH70_ProfileLinkWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View.inflate(context, R.layout.hh70_widget_profile_link, this);
        ImageView ivWidgetBg = findViewById(R.id.iv_dialogok_widget_bg);
        ivWidgetBg.setOnClickListener(v -> {});
        TextView tvContent = findViewById(R.id.tv_web_edit_profile);
        tvContent.setOnClickListener(v -> clickContentNext());
        TextView tvWidgetCancel = findViewById(R.id.tv_dialogok_widget_cancel);
        tvWidgetCancel.setOnClickListener(v -> {
            setVisibility(GONE);
            clickCancelNext();
        });
    }

    public interface OnClickContentListener {
        void clickContent();
    }

    private OnClickContentListener onClickContentListener;

    //对外方式setOnClickContentListener
    public void setOnClickContentListener(OnClickContentListener onClickContentListener) {
        this.onClickContentListener = onClickContentListener;
    }

    //封装方法clickContentNext
    private void clickContentNext() {
        if (onClickContentListener != null) {
            onClickContentListener.clickContent();
        }
    }

    /*-----------------------------------cancel 監聽*/
    public interface OnClickCancelListener {
        void clickCancel();
    }

    private OnClickCancelListener onClickCancelListener;

    //对外方式setOnClickCancelListener
    public void setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
    }

    //封装方法clickCancelNext
    private void clickCancelNext() {
        if (onClickCancelListener != null) {
            onClickCancelListener.clickCancel ();
        }
    }
}
