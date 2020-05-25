package com.alcatel.wifilink.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alcatel.wifilink.R;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by qianli.ma on 2019/8/15 0015.
 */
public class HH70_GuideWidget extends RelativeLayout {

    private Context context;// 域
    private View inflate;// 布局
    private ViewPager vpGuide;// 轮播图
    private LinearLayout llPoint;// 点布局
    private List<View> pages= new ArrayList<>();// 视图集合
    private List<ImageView> dots = new ArrayList<>();// 视图集合
    private Drawable dot_check;// 选中点
    private Drawable dot_uncheck;// 非选中点

    public HH70_GuideWidget(Context context) {
        this(context, null, 0);
    }

    public HH70_GuideWidget(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HH70_GuideWidget(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        inflate = View.inflate(context, R.layout.hh70_widget_guide, this);
        vpGuide = inflate.findViewById(R.id.vp_guide);
        llPoint = inflate.findViewById(R.id.ll_point_guide);
        pages = inflatePages(context);

        dot_check = getResources().getDrawable(R.drawable.dot_checked);// 白色
        dot_uncheck = getResources().getDrawable(R.drawable.dot_unchecked);// 灰色
        createPoint();// 创建点

        setVp();// 设置VP的适配器和监听
    }

    /**
     * 填充视图集合
     *
     * @param context 域
     * @return 集合
     */
    private List<View> inflatePages(Context context) {
        pages = new ArrayList<>();
        pages.add(View.inflate(context, R.layout.hh70_widget_guide_page1, null));
        pages.add(View.inflate(context, R.layout.hh70_widget_guide_page2, null));
        pages.add(View.inflate(context, R.layout.hh70_widget_guide_page3, null));
        return pages;
    }


    /**
     * 创建点
     */
    private void createPoint() {
        // 生成点
        for (int i = 0; i < pages.size(); i++) {
            ImageView iv = new ImageView(context);
            iv.setImageDrawable(i == 0 ? dot_check : dot_uncheck);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(12, 12);
            pa.setMargins(12, 0, 0, 0);
            iv.setLayoutParams(pa);
            llPoint.addView(iv);
            dots.add(iv);
        }
    }

    /**
     * 设置VP的适配器和监听
     */
    private void setVp() {
        // 设置适配器
        vpGuide.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pages == null ? 0 : pages.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = pages.get(position);
                if (position == pages.size() - 1) {
                    view.findViewById(R.id.bt_guide_rx_start).setOnClickListener(v -> clickGuideNext());
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });

        // 设置监听
        vpGuide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < dots.size(); i++) {
                    dots.get(i).setImageDrawable(position == i ? dot_check : dot_uncheck);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    /* -------------------------------------------- impl -------------------------------------------- */

    private OnClickGuideListener onClickGuideListener;

    // Inteerface--> 接口OnClickGuideListener
    public interface OnClickGuideListener {
        void clickGuide();
    }

    // 对外方式setOnClickGuideListener
    public void setOnClickGuideListener(OnClickGuideListener onClickGuideListener) {
        this.onClickGuideListener = onClickGuideListener;
    }

    // 封装方法clickGuideNext
    private void clickGuideNext() {
        if (onClickGuideListener != null) {
            onClickGuideListener.clickGuide();
        }
    }
}
