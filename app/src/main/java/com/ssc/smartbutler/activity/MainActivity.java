package com.ssc.smartbutler.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.fragment.ButlerFragment;
import com.ssc.smartbutler.fragment.GirlFragment;
import com.ssc.smartbutler.fragment.UserFragment;
import com.ssc.smartbutler.fragment.WechatFragment;
import com.ssc.smartbutler.utils.ActivityManager;

import java.util.ArrayList;
import java.util.List;


import static com.ssc.smartbutler.utils.StaticClass.REQUEST_CODE_LOGIN;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private  final String TAG = "MainActivity";

    //TabLayout
    private TabLayout mTabLayout;
    //ViewPager
    private ViewPager vp_main;
    //Title
    private List<String> mTitles;
    //Fragment
    private List<Fragment> mFragments;
    //悬浮按钮
    public static FloatingActionButton fab_setting;

    private ButlerFragment butlerFragment;
    private WechatFragment wechatFragment;
    private GirlFragment girlFragment;

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
        butlerFragment = new ButlerFragment();
        mFragments.add(butlerFragment);
        wechatFragment = new WechatFragment();
        mFragments.add(wechatFragment);
        girlFragment = new GirlFragment();
        mFragments.add(girlFragment);
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
                    if (position == 3){
                        fab_setting.setImageResource(R.drawable.icon_setting);
                    }else {
                        fab_setting.setImageResource(R.drawable.to_top);
                    }
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
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getPosition();
                // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
                vp_main.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

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

    int currentItem;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_setting:
                currentItem = vp_main.getCurrentItem();
                if (currentItem == 1){
                    //lv_wechat.setSelection(0);
                    wechatFragment.toTop();
                }else if (currentItem == 2){
                    girlFragment.toTop();
                }else if (currentItem == 3){
                    startActivity(new Intent(this,SettingActivity.class));
                }
                //startActivityForResult(new Intent(this, SettingActivity.class),REQUEST_CODE_EXIT);
                //finish();
                break;
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
        super.onActivityResult(requestCode, resultCode, data);
        //L.i(TAG,requestCode+"");
        switch (requestCode){
            /*case REQUEST_CODE_EXIT:
                vp_main.setCurrentItem(currentItem);
                break;*/
            case REQUEST_CODE_LOGIN:
                vp_main.setCurrentItem(3);
                //this.onRestart();
                break;
        }

        /*L.i(TAG,(null == data) +"");
        if (requestCode == SCAN_REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    L.i(TAG,result);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }*/
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
    }*/

    //双击退出
    //记录用户首次点击返回键的时间
    private long firstTime = 0;
    /**
     * 第二种办法
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                firstTime = secondTime;
                return true;
            } else {
                System.exit(0);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
