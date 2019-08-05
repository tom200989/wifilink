package com.alcatel.wifilink.root.ue.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.utils.C_Constants;
import com.alcatel.wifilink.root.ue.frag.SettingFragment;
import com.alcatel.wifilink.root.utils.PreferenceUtil;

import java.util.Locale;

import static java.lang.Character.toUpperCase;

public class SettingLanguageActivity extends BaseActivityWithBack {
    private static final String TAG = "SettingLanguageActivity";
    public static final String IS_SWITCH_LANGUAGE = "is_switch_language";
    private String[] mLanguageStrings = {C_Constants.Language.ENGLISH,// en-1
            C_Constants.Language.ARABIC,// ar-2
            C_Constants.Language.ESPANYOL,// es-3
            C_Constants.Language.GERMENIC,// de-4
            C_Constants.Language.ITALIAN,// it-5
            C_Constants.Language.FRENCH,// fr-6
            C_Constants.Language.SERBIAN,// sr-7
            C_Constants.Language.CROATIAN,// hr-8
            C_Constants.Language.SLOVENIAN,// sl-9
            C_Constants.Language.RUSSIAN,// russian
            C_Constants.Language.POLAND, // pl-10
            C_Constants.Language.CHINA // cn-11
    };
    private ListView mLanguageListView;
    private LanguageAdapter mLanguageAdapter;
    private String mCurrentLanguage;
    private String mChangeLanguage;
    private boolean mIsSwitchLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_language);
        setTitle(R.string.language);
        mCurrentLanguage = PreferenceUtil.getString(C_Constants.Language.LANGUAGE, "");
        if ("".equals(mCurrentLanguage)) {
            mCurrentLanguage = Locale.getDefault().getLanguage();
        }
        mChangeLanguage = mCurrentLanguage;
        mLanguageListView = findViewById(R.id.listview_language);
        mLanguageListView.setOnItemClickListener((parent, view, position, id) -> {
            mChangeLanguage = mLanguageStrings[position];
            mLanguageAdapter.notifyDataSetChanged();
            PreferenceUtil.commitString(C_Constants.Language.LANGUAGE, mChangeLanguage);
            invalidateOptionsMenu();
        });
        mLanguageAdapter = new LanguageAdapter(this, mLanguageStrings);
        mLanguageListView.setAdapter(mLanguageAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mCurrentLanguage.equals(mChangeLanguage)) {
            getMenuInflater().inflate(R.menu.save, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            switchLanguage(mChangeLanguage);
            mIsSwitchLanguage = true;
            setTitle(R.string.language);
            mCurrentLanguage = mChangeLanguage;
            invalidateOptionsMenu();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(IS_SWITCH_LANGUAGE, mIsSwitchLanguage);
        setResult(SettingFragment.SET_LANGUAGE_REQUEST, intent);
        finish();
        super.onBackPressed();
    }

    private class LanguageAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private String[] languages;

        public LanguageAdapter(Context context, String[] languageStrings) {
            super();
            inflater = LayoutInflater.from(context);
            this.languages = languageStrings;
        }

        @Override
        public int getCount() {
            return languages.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.setting_language_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.languageNameTv = (TextView) convertView.findViewById(R.id.language_item_tv);
                viewHolder.languageCheckImg = (ImageView) convertView.findViewById(R.id.language_item_img);

                convertView.setTag(viewHolder);
            }
            viewHolder = (ViewHolder) convertView.getTag();
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            if (languages[position].equals(C_Constants.Language.ENGLISH)) {
                config.locale = Locale.ENGLISH;
                
            } else if (languages[position].equals(C_Constants.Language.ARABIC)) {
                // 阿拉伯语
                config.locale = new Locale(C_Constants.Language.ARABIC);
                
            } else if (languages[position].equals(C_Constants.Language.GERMENIC)) {
                // 德语
                config.locale = Locale.GERMANY;
                
            } else if (languages[position].equals(C_Constants.Language.ESPANYOL)) {
                // 西班牙语
                // config.locale = new Locale(C_Constants.Language.ESPANYOL);
                config.locale = new Locale(C_Constants.Language.ESPANYOL, "MX");
                
            } else if (languages[position].equals(C_Constants.Language.ITALIAN)) {
                // 意大利语
                config.locale = Locale.ITALIAN;
                
            } else if (languages[position].equals(C_Constants.Language.FRENCH)) {
                // 法语
                config.locale = Locale.FRENCH;
                
            } else if (languages[position].equals(C_Constants.Language.SERBIAN)) {
                // 塞尔维亚
                config.locale = new Locale(C_Constants.Language.SERBIAN);
                
            } else if (languages[position].equals(C_Constants.Language.CROATIAN)) {
                // 克罗地亚
                config.locale = new Locale(C_Constants.Language.CROATIAN);
                
            } else if (languages[position].equals(C_Constants.Language.SLOVENIAN)) {
                // 斯洛文尼亚
                config.locale = new Locale(C_Constants.Language.SLOVENIAN);
                
            } else if (languages[position].equals(C_Constants.Language.POLAND)) {
                // 波兰语
                config.locale = new Locale(C_Constants.Language.POLAND);
                
            } else if (languages[position].equals(C_Constants.Language.RUSSIAN)) {
                // 俄语
                config.locale = new Locale(C_Constants.Language.RUSSIAN);
                
            } else if (languages[position].equals(C_Constants.Language.CHINA)) {
                // 台湾
                config.locale = new Locale(C_Constants.Language.CHINA, "TW");
            }

            String displayName = config.locale.getDisplayName(config.locale);
            String refreshLanguageStr = toUpperCase4Index(displayName);
            viewHolder.languageNameTv.setText(refreshLanguageStr);
            if (mChangeLanguage.equals(languages[position])) {
                viewHolder.languageCheckImg.setVisibility(View.VISIBLE);
            } else {
                viewHolder.languageCheckImg.setVisibility(View.GONE);
            }
            return convertView;
        }

        class ViewHolder {
            public TextView languageNameTv;
            public ImageView languageCheckImg;
        }

    }

    /**
     * 首字母大写
     *
     * @param string
     * @return
     */
    public static String toUpperCase4Index(String string) {
        char[] methodName = string.toCharArray();
        methodName[0] = toUpperCase(methodName[0]);
        return String.valueOf(methodName);
    }

}
