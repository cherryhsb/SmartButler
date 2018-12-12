package com.ssc.smartbutler.activity;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/9 20:54
 *  描述：     引导页
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.fragment.GuideOneFragment;
import com.ssc.smartbutler.fragment.GuideThreeFragment;
import com.ssc.smartbutler.fragment.GuideTwoFragment;
import com.ssc.smartbutler.utils.L;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "GuideActivity";

    private Button btn_skip, btn_guide_enter;

    private ImageView iv_dot1, iv_dot2, iv_dot3;

    private ViewPager vp_guide;
    //容器
    private List<Fragment> mFragments_guide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initDate();
        initView();
    }

    private void initDate() {
        mFragments_guide = new ArrayList<>();
        mFragments_guide.add(new GuideOneFragment());
        mFragments_guide.add(new GuideTwoFragment());
        mFragments_guide.add(new GuideThreeFragment());
    }

    private void initView() {
        vp_guide = (ViewPager) findViewById(R.id.vp_guide);
        btn_skip = (Button) findViewById(R.id.btn_skip);
        btn_guide_enter = (Button) findViewById(R.id.btn_guide_enter);
        iv_dot1 = (ImageView) findViewById(R.id.iv_dot1);
        iv_dot2 = (ImageView) findViewById(R.id.iv_dot2);
        iv_dot3 = (ImageView) findViewById(R.id.iv_dot3);

        setDotAndBtn(0);
        btn_skip.setVisibility(View.VISIBLE);
        btn_guide_enter.setVisibility(View.GONE);
        btn_skip.setOnClickListener(this);
        btn_guide_enter.setOnClickListener(this);

        vp_guide.setOffscreenPageLimit(mFragments_guide.size());
        vp_guide.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments_guide.get(position);
            }

            @Override
            public int getCount() {
                return mFragments_guide.size();
            }
        });

        //ViewPager的滑动监听
        vp_guide.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //处于哪一页的监听
            @Override
            public void onPageSelected(int position) {
                setDotAndBtn(position);
                L.i(TAG,"" + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_skip:
            case R.id.btn_guide_enter:
                startActivity(new Intent(GuideActivity.this, MainActivity.class));
                finish();
                break;
        }
    }

    private void setDotAndBtn(int position) {
        switch (position) {
            case 0:
                iv_dot1.setBackgroundResource(R.drawable.point_on);
                iv_dot2.setBackgroundResource(R.drawable.point_off);
                iv_dot3.setBackgroundResource(R.drawable.point_off);
                btn_skip.setVisibility(View.VISIBLE);
                btn_guide_enter.setVisibility(View.GONE);
                break;
            case 1:
                iv_dot1.setBackgroundResource(R.drawable.point_off);
                iv_dot2.setBackgroundResource(R.drawable.point_on);
                iv_dot3.setBackgroundResource(R.drawable.point_off);
                btn_skip.setVisibility(View.VISIBLE);
                btn_guide_enter.setVisibility(View.GONE);
                break;
            case 2:
                iv_dot1.setBackgroundResource(R.drawable.point_off);
                iv_dot2.setBackgroundResource(R.drawable.point_off);
                iv_dot3.setBackgroundResource(R.drawable.point_on);
                btn_skip.setVisibility(View.GONE);
                btn_guide_enter.setVisibility(View.VISIBLE);
                break;
        }
    }
}
