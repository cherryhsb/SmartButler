package com.ssc.smartbutler;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ssc.smartbutler.fragment.ButlerFragment;
import com.ssc.smartbutler.fragment.GirlFragment;
import com.ssc.smartbutler.fragment.UserFragment;
import com.ssc.smartbutler.fragment.WechatFragment;
import com.ssc.smartbutler.ui.BaseActivity;
import com.ssc.smartbutler.ui.SettingActivity;
import com.ssc.smartbutler.utils.ActivityManager;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_EXIT;
import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_LOGIN;
import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_REGISTER;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager vp_main;
    //Title
    private List<String> mTitles;
    //Fragment
    private List<Fragment> mFragments;
    //悬浮按钮
    private FloatingActionButton fab_setting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

        initDate();
        initView();

        ActivityManager.getInstance().addActivity(this);

        //Bugly测试
        //CrashReport.testJavaCrash();

    }

    //初始化数据
    private void initDate() {
        mTitles = new ArrayList<>();
        mTitles.add(getString(R.string.Butler));
        mTitles.add(getString(R.string.Wechat));
        mTitles.add(getString(R.string.Girl));
        mTitles.add(getString(R.string.User));

        mFragments = new ArrayList<>();
        mFragments.add(new ButlerFragment());
        mFragments.add(new WechatFragment());
        mFragments.add(new GirlFragment());
        mFragments.add(new UserFragment());

    }

    //初始化View
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.mTabLayout);
        vp_main = (ViewPager) findViewById(R.id.vp_main);
        fab_setting = (FloatingActionButton) findViewById(R.id.fab_setting);
        fab_setting.setOnClickListener(this);
        fab_setting.setVisibility(View.GONE);

        //ViewPager的滑动监听
        vp_main.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            //处于哪一页的监听
            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    fab_setting.setVisibility(View.GONE);
                } else {
                    fab_setting.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //预加载
        vp_main.setOffscreenPageLimit(mFragments.size());

        //设置适配器
        vp_main.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            //选中的item
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            //返回item的个数
            @Override
            public int getCount() {
                return mFragments.size();
            }

            //设置标题
            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles.get(position);
            }
        });

        Intent intent = getIntent();
        if (intent.getIntExtra("page",0) == 3){
            vp_main.setCurrentItem(3);
        }


        //绑定
        mTabLayout.setupWithViewPager(vp_main);

        /*if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new
                    String[]{Manifest.permission.READ_PHONE_STATE}, 1001);
        }
        L.i(TAG, "*****************");
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new
                    String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1002);
        }*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                startActivityForResult(new Intent(this, SettingActivity.class),REQUEST_CODE_EXIT);
                //finish();
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "您拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
            case 1002:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "您拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            //default:
        }
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_EXIT:
                //setCurrentItem()需放在setAdapter()后面才有效
                vp_main.setCurrentItem(3);
                break;
            case REQUEST_CODE_LOGIN:
                vp_main.setCurrentItem(3);
                break;
            case REQUEST_CODE_REGISTER:
                vp_main.setCurrentItem(3);
                break;
        }
    }
}
