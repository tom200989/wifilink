package com.alcatel.wifilink.root.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.RootUtils;

/*
 * Created by qianli.ma on 2019/8/28 0028.
 */
public class HH70_BackupPathWidget extends RelativeLayout {

    private View inflate;
    private View ivbg;
    private EditText etPath;
    private TextView tvCancel;
    private TextView tvBackup;

    public HH70_BackupPathWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_BackupPathWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_BackupPathWidget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inflate = inflate(context, R.layout.hh70_widget_backup_path, this);
        ivbg = inflate.findViewById(R.id.iv_backup_path_bg);
        ivbg.setOnClickListener(null);
        etPath = inflate.findViewById(R.id.et_backup_path);
        tvCancel = inflate.findViewById(R.id.tv_backup_path_cancel);
        tvCancel.setOnClickListener(v -> setVisibility(GONE));
        tvBackup = inflate.findViewById(R.id.tv_backup_path_ok);
        tvBackup.setOnClickListener(v -> {
            clickBackupNext(RootUtils.getEDText(etPath));
            setVisibility(GONE);
        });
    }
    
    public void setPath(String path) {
        etPath.setText(path);
    }

    private OnClickBackupListener onClickBackupListener;

    // Inteerface--> 接口OnClickBackupListener
    public interface OnClickBackupListener {
        void clickBackup(String path);
    }

    // 对外方式setOnClickBackupListener
    public void setOnClickBackupListener(OnClickBackupListener onClickBackupListener) {
        this.onClickBackupListener = onClickBackupListener;
    }

    // 封装方法clickBackupNext
    private void clickBackupNext(String path) {
        if (onClickBackupListener != null) {
            onClickBackupListener.clickBackup(path);
        }
    }
}
