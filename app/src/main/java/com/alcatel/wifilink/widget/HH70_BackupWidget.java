package com.alcatel.wifilink.widget;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/27 0027.
 */
public class HH70_BackupWidget extends RelativeLayout {
    private View inflate;
    private TextView mFirstTxt;
    private TextView mSecondTxt;
    private TextView mCancelTxt;

    public HH70_BackupWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_BackupWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_BackupWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_backup, this);
        inflate.findViewById(R.id.iv_backup_bg).setOnClickListener(v -> setVisibility(GONE));
        mFirstTxt = inflate.findViewById(R.id.first_txt);
        mSecondTxt = inflate.findViewById(R.id.second_txt);
        mCancelTxt = inflate.findViewById(R.id.cancel_txt);
        mCancelTxt.setOnClickListener(v -> setVisibility(GONE));
        mFirstTxt.setOnClickListener(v -> {
            clickFirstTextNext();
            setVisibility(GONE);
        });
        mSecondTxt.setOnClickListener(v -> {
            clickSecTextNext();
            setVisibility(GONE);
        });
        mCancelTxt.setOnClickListener(v -> {
            clickCancelNext();
            setVisibility(GONE);
        });
    }

    public void setFirstText(@StringRes int text) {
        mFirstTxt.setText(text);
    }

    public void setSecondText(@StringRes int text) {
        mSecondTxt.setText(text);
    }

    private OnClickFirstTextListener onClickFirstTextListener;

    // Inteerface--> 接口OnClickFirstTextListener
    public interface OnClickFirstTextListener {
        void clickFirstText();
    }

    // 对外方式setOnClickFirstTextListener
    public void setOnClickFirstTextListener(OnClickFirstTextListener onClickFirstTextListener) {
        this.onClickFirstTextListener = onClickFirstTextListener;
    }

    // 封装方法clickFirstTextNext
    private void clickFirstTextNext() {
        if (onClickFirstTextListener != null) {
            onClickFirstTextListener.clickFirstText();
        }
    }

    private OnClickSecTextListener onClickSecTextListener;

    // Inteerface--> 接口OnClickSecTextListener
    public interface OnClickSecTextListener {
        void clickSecText();
    }

    // 对外方式setOnClickSecTextListener
    public void setOnClickSecTextListener(OnClickSecTextListener onClickSecTextListener) {
        this.onClickSecTextListener = onClickSecTextListener;
    }

    // 封装方法clickSecTextNext
    private void clickSecTextNext() {
        if (onClickSecTextListener != null) {
            onClickSecTextListener.clickSecText();
        }
    }

    private OnClickCancelListener onClickCancelListener;

    // Inteerface--> 接口OnClickCancelListener
    public interface OnClickCancelListener {
        void clickCancel();
    }

    // 对外方式setOnClickCancelListener
    public void setOnClickCancelListener(OnClickCancelListener onClickCancelListener) {
        this.onClickCancelListener = onClickCancelListener;
    }

    // 封装方法clickCancelNext
    private void clickCancelNext() {
        if (onClickCancelListener != null) {
            onClickCancelListener.clickCancel();
        }
    }

}
