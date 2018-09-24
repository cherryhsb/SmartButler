package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/12 13:32
 *  描述：     TODO
 */

import android.Manifest;
import android.app.ActionBar;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.takephoto.CustomHelper;
import com.ssc.smartbutler.utils.ActivityManager;
import com.ssc.smartbutler.utils.ImageUtil;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.view.CustomDialog;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.CAMERA_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.DESC_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.ICON_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.PICTURE_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.ZOOM_REQUEST_CODE;

public class UserInfoActivity extends TakePhotoActivity implements View.OnClickListener {

    private static final String TAG = "UserInfoActivity";

    private Button btn_change_password;

    private TextView tv_info_username, tv_info_gender, tv_info_age, tv_info_desc, tv_info_nickname;

    private LinearLayout ll_icon, ll_gender, ll_age, ll_desc, ll_nickname;

    //private CircleImageView iv_info_icon;

    private ImageView iv_info_icon;

    private int index;

    private CustomDialog dialog;

    private CustomDialog dialogProgress;

    private Button btn_camera, btn_picture, btn_cancel;

    private CustomHelper customHelper;

    private String username;

    private String iconCompressPath;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();

        customHelper = new CustomHelper(this,username);

        ActivityManager.getInstance().addActivity(this);

    }

    private void initView() {
        btn_change_password = findViewById(R.id.btn_change_password);
        tv_info_username = findViewById(R.id.tv_info_username);
        tv_info_gender = findViewById(R.id.tv_info_gender);
        tv_info_age = findViewById(R.id.tv_info_age);
        tv_info_desc = findViewById(R.id.tv_info_desc);
        tv_info_nickname = findViewById(R.id.tv_info_nickname);
        ll_icon = findViewById(R.id.ll_icon);
        ll_gender = findViewById(R.id.ll_gender);
        ll_age = findViewById(R.id.ll_age);
        ll_desc = findViewById(R.id.ll_desc);
        iv_info_icon = findViewById(R.id.iv_info_icon);
        ll_nickname = findViewById(R.id.ll_nickname);


        //dialog = new CustomDialog(this, 0, 0,
        //                R.layout.dialog_photo, R.style.pop_anim_style, Gravity.BOTTOM, 0);R.style.Theme_dialog
        dialog = new CustomDialog(this, 0, 0, R.layout.dialog_photo, R.style.Theme_dialog, Gravity.BOTTOM, R.style.pop_anim_style);
        //屏幕外点击无效
        dialog.setCancelable(false);

        dialogProgress = new CustomDialog(this, 100, 100, R.layout.dialog_loding, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        //屏幕外点击无效
        dialogProgress.setCancelable(false);

        btn_change_password.setOnClickListener(this);
        ll_icon.setOnClickListener(this);
        ll_gender.setOnClickListener(this);
        ll_age.setOnClickListener(this);
        ll_desc.setOnClickListener(this);
        ll_nickname.setOnClickListener(this);


        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_camera.setOnClickListener(this);
        btn_picture.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        //userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            //设置用户信息显示
            username = userInfo.getUsername();
            tv_info_username.setText(userInfo.getUsername());
            tv_info_nickname.setText(userInfo.getNickname());
            if (userInfo.isGender()) {
                tv_info_gender.setText(getString(R.string.male));
                index = 0;
            } else {
                tv_info_gender.setText(getString(R.string.female));
                index = 1;
            }
            tv_info_age.setText(Integer.toString(userInfo.getAge()));
            tv_info_desc.setText(userInfo.getDesc());

            iconCompressPath =getExternalFilesDir(userInfo.getUsername()).getAbsolutePath()+ "/icon/" + userInfo.getUsername() + "(compress).jpg";
            BmobFile icon = userInfo.getIcon();
            //L.i(TAG, icon.getUrl()+"hahaha");
            if (icon != null) {//设置过头像
                if (iconCompressPath != null) {//从本机设置过头像
                    if (new File(iconCompressPath).exists()) {//本地图片存在
                        //从本地直接设置
                        iv_info_icon.setImageURI(Uri.fromFile(new File(iconCompressPath)));
                    } else {//本地图片已经被删除,需要下载
                        downloadIcon(icon);
                    }
                } else {//从其他手机设置过图片,需要下载
                    downloadIcon(icon);
                }
            } else {//没有设置过头像,显示默认图标
                iv_info_icon.setImageResource(R.drawable.user);
            }

        } else {
            //缓存用户对象为空时， 可打开用户注册界面…

        }

        String s = getExternalFilesDir(username).getAbsolutePath()+ "/icon/" + username + "(compress).jpg";
        L.i(TAG,s);

    }

    /**
     * 设置头像
     */
    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        showImg(result.getImages());
    }

    private void showImg(final ArrayList<TImage> images) {
        /*Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("images", images);
        startActivity(intent);*/
        //iv.setImageURI(Uri.fromFile(new File(images.get(0).getCompressPath())));

        //dialogProgress.show();

        uploadIcon(images);

    }

    private void copyIconCompress(ArrayList<TImage> images) {
        File file = new File(getExternalFilesDir(username),//获取私有目录
                "/icon/" + username + "(compress).jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(new File(images.get(0).getCompressPath())));
            bos = new BufferedOutputStream(new FileOutputStream(file));
            int read;
            while ((read = bis.read()) !=-1){
                bos.write(read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                try {
                    if (bos != null) {
                        bos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadIcon(final ArrayList<TImage> images) {
        final BmobFile icon = new BmobFile(new File(images.get(0).getCompressPath()));

        icon.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                //dialogProgress.dismiss();
                if (e == null) {
                    L.w(TAG, "上传成功。。。。。。。。。。。。。。。");
                    String iconStringUrl = icon.getFileUrl();

                    MyUser myUser = BmobUser.getCurrentUser(MyUser.class);
                    myUser.setIcon(icon);
                    myUser.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //toast("更新用户信息成功");
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        copyIconCompress(images);//把压缩后的图片存储到专属目录下
                                    }
                                }).start();
                                iv_info_icon.setImageURI(Uri.fromFile(new File(images.get(0).getCompressPath())));
                                Toast.makeText(UserInfoActivity.this, "设置头像成功", Toast.LENGTH_SHORT).show();
                                iconCompressPath = images.get(0).getCompressPath();
                                L.i(TAG, iconCompressPath+"上传裁剪后图片");
                            } else {
                                //toast("更新用户信息失败:" + e.getMessage());
                                L.w(TAG, "设置头像失败" + e.getMessage());
                                Toast.makeText(UserInfoActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    L.w(TAG, "上传头像失败" + e.getMessage());
                    Toast.makeText(UserInfoActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void downloadIcon(BmobFile bmobFile){
        //允许设置下载文件的存储路径，默认下载文件的目录为：context.getApplicationContext().getCacheDir()+"/bmob/"
        //File saveFile = new File(Environment.getExternalStorageDirectory(), file.getFilename());
        final File file = new File(getExternalFilesDir(username),//获取私有目录
                "/icon/" +username + "(compress).jpg");
        bmobFile.download(file, new DownloadFileListener() {

            @Override
            public void onStart() {
                //toast("开始下载...");
                dialogProgress.show();
            }

            @Override
            public void done(String savePath,BmobException e) {
                dialogProgress.dismiss();
                if(e==null){
                    //toast("下载成功,保存路径:"+savePath);
                    iconCompressPath = file.getPath();
                    L.i(TAG, iconCompressPath+"下载裁剪后图片");
                    iv_info_icon.setImageURI(Uri.fromFile(file));
                }else{
                    //toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                }
            }

            @Override
            public void onProgress(Integer value, long newworkSpeed) {
                //Log.i("bmob","下载进度："+value+","+newworkSpeed);
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.ll_icon:
                dialog.show();
                /*Intent intentIcon = new Intent(this, IconActivity.class);
                startActivityForResult(intentIcon,ICON_REQUEST_CODE);*/
                break;
            case R.id.ll_nickname:
                showNicknameDialog();
                break;
            case R.id.ll_gender:
                showListDialog();
                break;
            case R.id.ll_age:
                showAgeDialog();
                break;
            case R.id.ll_desc:
                Intent intent = new Intent(this, DescActivity.class);
                intent.putExtra("desc", tv_info_desc.getText().toString());
                startActivityForResult(intent, DESC_REQUEST_CODE);
                break;
            case R.id.btn_camera:
                customHelper.onClick(R.id.btn_camera, getTakePhoto());
                dialog.dismiss();
                break;
            case R.id.btn_picture:
                customHelper.onClick(R.id.btn_picture, getTakePhoto());
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    //性别dialog
    private void showListDialog() {
        final String[] genders = {getString(R.string.male), getString(R.string.female)};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.gender));
        builder.setSingleChoiceItems(genders, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(MainActivity.this, "您选择了" + stars[which], Toast.LENGTH_SHORT).show();
                index = which;
            }
        });
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                if (index == 0) {
                    bmobUser.setGender(true);
                } else {
                    bmobUser.setGender(false);
                }
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //toast("更新用户信息成功");
                            if (index == 0) {
                                tv_info_gender.setText(getString(R.string.male));
                            } else {
                                tv_info_gender.setText(getString(R.string.female));
                            }
                        } else {
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(UserInfoActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.show();
    }

    //年龄dialog
    private void showAgeDialog() {
        final EditText editText = new EditText(this);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.age));
        builder.setView(editText);
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                bmobUser.setAge(Integer.parseInt(editText.getText().toString()));
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //toast("更新用户信息成功");
                            tv_info_age.setText(editText.getText().toString());
                        } else {
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(UserInfoActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.show();
    }

    //昵称dialog
    private void showNicknameDialog() {
        final EditText editText = new EditText(this);
        editText.setGravity(Gravity.CENTER_HORIZONTAL);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.nickname));
        builder.setView(editText);
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                bmobUser.setNickname(editText.getText().toString());
                bmobUser.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            //toast("更新用户信息成功");
                            tv_info_nickname.setText(editText.getText().toString());
                        } else {
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(UserInfoActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        L.i(TAG, "" + requestCode);
        switch (requestCode) {
            //简介页回调
            case DESC_REQUEST_CODE:
                //L.i(TAG, "" + resultCode);
                if (resultCode == RESULT_OK) {
                    tv_info_desc.setText(data.getStringExtra("desc_return"));
                    L.i(TAG, data.getStringExtra("desc_return"));
                }
                break;
            default:
                break;
        }
    }


}
