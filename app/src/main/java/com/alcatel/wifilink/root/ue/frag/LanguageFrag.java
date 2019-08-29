package com.alcatel.wifilink.root.ue.frag;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.adapter.LanguageAdapter;
import com.alcatel.wifilink.root.bean.LanguageBean;
import com.alcatel.wifilink.root.utils.RootCons;
import com.hiber.hiber.language.LangHelper;
import com.hiber.tools.ShareUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/*
 * Created by qianli.ma on 2019/8/20 0020.
 */
public class LanguageFrag extends BaseFrag {

    @BindView(R.id.tv_language_back)
    ImageView tvLanguageBack;
    @BindView(R.id.tv_language_done)
    TextView tvLanguageDone;
    @BindView(R.id.listview_language)
    RecyclerView rcvLanguage;

    private List<LanguageBean> languageBeans = new ArrayList<>();
    private LanguageAdapter adapter;

    @Override
    public int onInflateLayout() {
        return R.layout.hh70_frag_language;
    }

    @Override
    public void onNexts(Object o, View view, String s) {
        super.onNexts(o, view, s);
        languageBeans = getInitLanguage();
        initAdapter();
        initClick();
    }

    private void initClick() {
        // 回退
        tvLanguageBack.setOnClickListener(v -> onBackPresss());
        // 提交
        tvLanguageDone.setOnClickListener(v -> {
            LanguageBean languageBean = getChoiceLanguage();
            if (languageBean != null) {
                String[] lang_coun = languageBean.getLanguage_country().split("-");
                String language = lang_coun[0];
                String country = lang_coun[1];
                country = TextUtils.isEmpty(country) | country.equals("default") ? "" : country;
                LangHelper.transfer(activity, language, country);
                // 保存到缓存
                ShareUtils.set(RootCons.LOCALE_LANGUAGE_COUNTRY, language + "-" + country);
                // 重启Activity(必须)
                activity.recreate();
            }
        });
    }

    /**
     * 获取被选中的语言
     */
    private LanguageBean getChoiceLanguage() {
        for (LanguageBean languageBean : languageBeans) {
            if (languageBean.isCurrent()) {
                return languageBean;
            }
        }
        return null;
    }

    private void initAdapter() {
        LinearLayoutManager lm = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rcvLanguage.setLayoutManager(lm);
        adapter = new LanguageAdapter(activity, languageBeans);
        adapter.setOnClickLanguageListener(languageBean -> {
            changeLang(languageBean);
            adapter.notifys(languageBeans);
        });
        rcvLanguage.setAdapter(adapter);
    }

    /**
     * 修改选中
     */
    private void changeLang(LanguageBean languageBean) {
        for (LanguageBean bean : languageBeans) {
            String lang_coun = languageBean.getLanguage_country();
            String lang_coun1 = bean.getLanguage_country();
            bean.setCurrent(lang_coun.equals(lang_coun1));
        }
    }

    /**
     * 获取初始化语言
     */
    private List<LanguageBean> getInitLanguage() {
        // 从shareutils.get(LOCALE_LANGUAGE_COUNTRY)取出来, 如es-MX, es-default
        String language_country = ShareUtils.get(RootCons.LOCALE_LANGUAGE_COUNTRY, "");
        // 封装
        List<LanguageBean> langs = new ArrayList<>();
        for (String lang_coun : RootCons.LANGUAGE_COUNTRY_LIST) {
            LanguageBean languageBean = new LanguageBean();
            languageBean.setLanguage_country(language_country);
            languageBean.setCurrent(lang_coun.equals(language_country));
            langs.add(languageBean);
        }
        return langs;
    }

    @Override
    public boolean onBackPresss() {
        toFrag(getClass(), SettingFrag.class, null, false);
        return true;
    }

}
