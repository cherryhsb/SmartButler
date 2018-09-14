package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    ScanActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/11 20:29
 *  描述：     TODO自定义二维码扫描
 */

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.StaticClass;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import static com.ssc.smartbutler.utils.StaticClass.IMAGE_REQUEST_CODE;

public class ScanActivity extends PermissionActivity implements View.OnClickListener {

    private Button btn_scan_cancel;

    private Switch switch_flash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

        //ActionBar显示返回键
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initView();

        requestCameraPermission();

        //在Activity中执行Fragment的初始化操作
        //执行扫面Fragment的初始化操作
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.item_scan);
        captureFragment.setAnalyzeCallback(analyzeCallback);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
    }

    //ActionBar菜单栏返回键操作
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void requestCameraPermission() {
        if (!hasPermission(Manifest.permission.CAMERA)){//如果没有权限
            requestPermission(StaticClass.CAMERA_CODE,Manifest.permission.CAMERA);
        }
    }

    private void initView() {
        btn_scan_cancel = findViewById(R.id.btn_scan_cancel);
        switch_flash = findViewById(R.id.switch_flash);
        btn_scan_cancel.setOnClickListener(this);
        switch_flash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    /**
                     * 打开闪光灯
                     */
                    CodeUtils.isLightEnable(true);
                } else {
                    /**
                     * 关闭闪光灯
                     */
                    CodeUtils.isLightEnable(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_scan_cancel:
                //finish();
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, IMAGE_REQUEST_CODE);
                break;
        }
    }

    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
            bundle.putString(CodeUtils.RESULT_STRING, result);
            resultIntent.putExtras(bundle);
            ScanActivity.this.setResult(RESULT_OK, resultIntent);
            ScanActivity.this.finish();
        }

        @Override
        public void onAnalyzeFailed() {
            Intent resultIntent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
            bundle.putString(CodeUtils.RESULT_STRING, "");
            resultIntent.putExtras(bundle);
            ScanActivity.this.setResult(RESULT_OK, resultIntent);
            ScanActivity.this.finish();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从相册扫描二维码
        if (requestCode == IMAGE_REQUEST_CODE) {
            if (data != null) {
                Uri uri = data.getData();
                ContentResolver cr = getContentResolver();
                try {
                    Bitmap mBitmap = MediaStore.Images.Media.getBitmap(cr, uri);//显得到bitmap图片

                    CodeUtils.analyzeBitmap(String.valueOf(mBitmap), new CodeUtils.AnalyzeCallback() {
                        @Override
                        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                            Toast.makeText(ScanActivity.this, getString(R.string.analytical_results) + ":" + result, Toast.LENGTH_LONG).show();
                            /*Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
                            bundle.putString(CodeUtils.RESULT_STRING, result);
                            resultIntent.putExtras(bundle);
                            ScanActivity.this.setResult(RESULT_OK, resultIntent);
                            ScanActivity.this.finish();*/
                        }

                        @Override
                        public void onAnalyzeFailed() {
                            Toast.makeText(ScanActivity.this, getString(R.string.parse_failure)+":", Toast.LENGTH_LONG).show();
                            /*Intent resultIntent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                            bundle.putString(CodeUtils.RESULT_STRING, "");
                            resultIntent.putExtras(bundle);
                            ScanActivity.this.setResult(RESULT_OK, resultIntent);
                            ScanActivity.this.finish();*/
                        }
                    });

                    if (mBitmap != null) {
                        mBitmap.recycle();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
