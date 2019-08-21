package com.alcatel.wifilink.root.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.bean.LanguageBean;

import java.util.List;
import java.util.Locale;

import static java.lang.Character.toUpperCase;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class LanguageAdapter extends RecyclerView.Adapter<LanguageHolder> {

    private Context context;
    private List<LanguageBean> langBeans;

    public LanguageAdapter(Context context, List<LanguageBean> langBeans) {
        this.context = context;
        this.langBeans = langBeans;
    }

    public void notifys(List<LanguageBean> langBeans) {
        this.langBeans = langBeans;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public LanguageHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        return new LanguageHolder(LayoutInflater.from(context).inflate(R.layout.hh70_item_language, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageHolder holder, int i) {
        LanguageBean languageBean = langBeans.get(i);
        holder.tvItem.setText(handlerLanguage(languageBean));// 中文简体, English
        holder.ivItem.setVisibility(languageBean.isCurrent() ? View.VISIBLE : View.GONE);
        holder.rlItem.setOnClickListener(v -> clickLanguageNext(languageBean));
    }

    /**
     * 处理语言展示名
     */
    private String handlerLanguage(LanguageBean languageBean) {
        String display = "";
        // 获取展示名
        String[] lang_coun = languageBean.getLanguage_country().split("-");
        if (lang_coun.length > 1) {
            String language = lang_coun[0];
            String country = lang_coun[1];
            Locale locale;
            if (TextUtils.isEmpty(country) | country.equals("default")) {
                locale = new Locale(language);
            } else {
                locale = new Locale(language, country);
            }
            display = locale.getDisplayName(locale);
        } else {
            String language = lang_coun[0];
            Locale locale = new Locale(language);
            display = locale.getDisplayName(locale);
        }
        // 首字母大写
        char[] chars = display.toCharArray();
        chars[0] = toUpperCase(chars[0]);
        return String.valueOf(chars);
    }

    @Override
    public int getItemCount() {
        return langBeans.size();
    }

    private OnClickLanguageListener onClickLanguageListener;

    // Inteerface--> 接口OnClickLanguageListener
    public interface OnClickLanguageListener {
        void clickLanguage(LanguageBean languageBean);
    }

    // 对外方式setOnClickLanguageListener
    public void setOnClickLanguageListener(OnClickLanguageListener onClickLanguageListener) {
        this.onClickLanguageListener = onClickLanguageListener;
    }

    // 封装方法clickLanguageNext
    private void clickLanguageNext(LanguageBean languageBean) {
        if (onClickLanguageListener != null) {
            onClickLanguageListener.clickLanguage(languageBean);
        }
    }
}
