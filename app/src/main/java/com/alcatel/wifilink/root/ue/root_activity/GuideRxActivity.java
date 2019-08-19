package com.alcatel.wifilink.root.ue.root_activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alcatel.wifilink.R;
import com.alcatel.wifilink.root.helper.Cons;
import com.alcatel.wifilink.root.utils.CA;
import com.alcatel.wifilink.root.utils.SP;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// TOGO 2019/8/17 0017 GuideFrag
@Deprecated
public class GuideRxActivity extends BaseActivityWithBack {

    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.ll_point)
    LinearLayout llPoint;

    // @BindView(R.id.autoVp)
    // AutoViewPager autoVp;

    private View view_guide1;
    private View view_guide2;
    private View view_guide3;
    private List<View> views;
    private Drawable checked;
    private Drawable unchecked;
    private List<ImageView> points = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_rx);
        ButterKnife.bind(this);
        initRes();
        initAdapter();
    }


    private void initRes() {
        // 提交标记
        SP.getInstance(this).putBoolean(Cons.GUIDE_RX, true);
        // 初始化资源
        checked = getResources().getDrawable(R.drawable.dot_checked);// 白色
        unchecked = getResources().getDrawable(R.drawable.dot_unchecked);// 灰色

        views = new ArrayList<>();
        view_guide1 = View.inflate(this, R.layout.guiderx1, null);
        view_guide2 = View.inflate(this, R.layout.guiderx2, null);
        view_guide3 = View.inflate(this, R.layout.guiderx3, null);
        views.add(view_guide1);
        views.add(view_guide2);
        views.add(view_guide3);

        // 生成点
        for (int i = 0; i < views.size(); i++) {
            ImageView iv = new ImageView(this);
            iv.setImageDrawable(i == 0 ? checked : unchecked);
            LinearLayout.LayoutParams pa = new LinearLayout.LayoutParams(12, 12);
            pa.setMargins(12, 0, 0, 0);
            iv.setLayoutParams(pa);
            llPoint.addView(iv);
            points.add(iv);
        }
    }

    private void initAdapter() {
        vp.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = null;
                switch (position) {
                    case 0:
                        view = getLayoutInflater().inflate(R.layout.guiderx1, container, false);
                        break;
                    case 1:
                        view = getLayoutInflater().inflate(R.layout.guiderx2, container, false);
                        break;
                    case 2:
                        view = getLayoutInflater().inflate(R.layout.guiderx3, container, false);
                        view.findViewById(R.id.bt_guide_rx_start).setOnClickListener(v -> {
                            CA.toActivity(GuideRxActivity.this, LoginRxActivity.class, false, true, false, 0);
                        });
                        break;
                }
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        });

        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < points.size(); i++) {
                    points.get(i).setImageDrawable(position == i ? checked : unchecked);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

}
