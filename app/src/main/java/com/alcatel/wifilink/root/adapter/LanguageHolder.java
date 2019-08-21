package com.alcatel.wifilink.root.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alcatel.wifilink.R;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class LanguageHolder extends RecyclerView.ViewHolder {

    public RelativeLayout rlItem;// 条目
    public ImageView ivItem;// 勾
    public TextView tvItem;// 文本

    public LanguageHolder(@NonNull View itemView) {
        super(itemView);
        rlItem = itemView.findViewById(R.id.rl_language_item);
        tvItem = itemView.findViewById(R.id.language_item_tv);
        ivItem = itemView.findViewById(R.id.language_item_img);
    }
}
