package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/12 13:32
 *  描述：     TODO
 */

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
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.ActivityManager;
import com.ssc.smartbutler.utils.ImageUtil;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.view.CustomDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.CAMERA_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.DESC_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.PICTURE_REQUEST_CODE;
import static com.ssc.smartbutler.utils.StaticClass.ZOOM_REQUEST_CODE;

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "UserInfoActivity";

    private Button btn_change_password;

    private TextView tv_info_username, tv_info_gender, tv_info_age, tv_info_desc,tv_info_nickname;

    private LinearLayout ll_icon, ll_gender, ll_age, ll_desc,ll_nickname;

    private ImageView iv_info_icon;

    private int index;

    private CustomDialog dialog;

    private Button btn_camera, btn_picture, btn_cancel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();

        ActivityManager.getInstance().addActivity(this);
    }

    private void initView() {
        btn_change_password = findViewById(R.id.btn_change_password);
        tv_info_username = findViewById(R.id.tv_info_username);
        tv_info_gender = findViewById(R.id.tv_info_gender);
        tv_info_age = findViewById(R.id.tv_info_age);
        tv_info_desc = findViewById(R.id.tv_info_desc);
        tv_info_nickname =findViewById(R.id.tv_info_nickname);
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

        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            //设置用户信息显示
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
            if (userInfo.getImgString() != null) {
                //利用Base64将String转化为byte数组
                byte[] bytes = Base64.decode(userInfo.getImgString(), Base64.DEFAULT);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                //生成bitmap
                iv_info_icon.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…

        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.ll_icon:
                dialog.show();
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
                toCamera();
                dialog.dismiss();
                break;
            case R.id.btn_picture:
                toPicture();
                dialog.dismiss();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
        }
    }

    public static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";



    //跳转相册
    private void toPicture() {
        //Intent intent = new Intent("android.intent.action.GET_CONTENT");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICTURE_REQUEST_CODE);
    }


    private Uri imageUri;//相机uri
    //File cropImage = new File(getExternalCacheDir(), "crop_image.jpg");
    //private Uri cropUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.ssc.smrtbutler.fileprovider", cropImage);
    private String absolutePath;

    //跳转相机
    private void toCamera() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //SD卡：这里的SD卡是指内置的SD卡，路径为：Environment.getExternalStorageDirectory()
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent,CAMERA_REQUEST_CODE);
        dialog.dismiss();*/


        //创建File对象,用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        absolutePath = outputImage.getAbsolutePath();
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(UserInfoActivity.this, "com.ssc.smrtbutler.fileprovider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        //启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
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
                //Toast.makeText(UserInfoActivity.this, "您选择了" + genders[index], Toast.LENGTH_SHORT).show();
                /*MyUser newUser = new MyUser();
                if (index == 0){
                    newUser.setGender(true);
                }else {
                    newUser.setGender(false);
                }
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //toast("更新用户信息成功");
                            if (index == 0){
                                tv_info_gender.setText("男");
                            }else {
                                tv_info_gender.setText("女");
                            }
                        }else{
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(UserInfoActivity.this, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
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
                //Toast.makeText(UserInfoActivity.this, "您输入的是：" + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                /*MyUser newUser = new MyUser();
                newUser.setAge(Integer.parseInt(editText.getText().toString()));
                //Toast.makeText(UserInfoActivity.this, newUser.getAge()+"", Toast.LENGTH_SHORT).show();
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //toast("更新用户信息成功");
                            tv_info_age.setText(editText.getText().toString());
                        }else{
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(UserInfoActivity.this, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                // 修改用户的邮箱为xxx@163.com
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
                //Toast.makeText(UserInfoActivity.this, "您输入的是：" + editText.getText().toString(), Toast.LENGTH_SHORT).show();
                /*MyUser newUser = new MyUser();
                newUser.setAge(Integer.parseInt(editText.getText().toString()));
                //Toast.makeText(UserInfoActivity.this, newUser.getAge()+"", Toast.LENGTH_SHORT).show();
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                newUser.update(bmobUser.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if(e==null){
                            //toast("更新用户信息成功");
                            tv_info_age.setText(editText.getText().toString());
                        }else{
                            //toast("更新用户信息失败:" + e.getMessage());
                            Toast.makeText(UserInfoActivity.this, "更新用户信息失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
                MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
                // 修改用户的邮箱为xxx@163.com
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

    //activity回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
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
            //相机回调
            case CAMERA_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    /*try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//原始拿到图片
                        Bitmap bitmap1 = ImageUtil.decodeSampleBitmapFromFilePath(absolutePath, 600, 600);//拿到小图
                        iv_info_icon.setImageBitmap(ImageUtil.rotateBitmap(bitmap1, ImageUtil.readPictureDegree(absolutePath)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }*/
                    startPhotoZoom(imageUri);
                }
                break;
            //相册回调
            case PICTURE_REQUEST_CODE:
                handleImageOnKitKat(data);
                break;
            //裁剪回调
            case ZOOM_REQUEST_CODE:
                //有可能点击舍弃
                if (data != null) {
                    setImageToView(data);
                    putImgString();
                }
                break;
            default:
        }
    }

    String imagePath;

    //4.4及以上相册
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        //Log.i(TAG, data.toString());
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                //如果是media
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                //如果是downloads
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri,则使用普通方式处理
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = uri.getPath();
        }
        L.i(TAG + "--------------------", imagePath);
        startPhotoZoom(uri);
        //displayImage(imagePath);
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            //iv_picture.setImageBitmap(bitmap);
            Bitmap bitmap1 = ImageUtil.decodeSampleBitmapFromFilePath(imagePath, 1000, 1000);
            iv_info_icon.setImageBitmap(ImageUtil.rotateBitmap(bitmap1, ImageUtil.readPictureDegree(imagePath)));

        } else {
            Toast.makeText(this, getString(R.string.failure), Toast.LENGTH_SHORT).show();
        }
    }

    private String getImagePath(Uri externalContentUri, String selection) {
        String path = null;
        //通过Uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(externalContentUri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        //7.0以上:来对目标应用临时授权该Uri所代表的文件
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高
        intent.putExtra("aspextX", 1);
        intent.putExtra("aspextY", 1);
        //设置裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //intent.putExtra("circleCrop", "true");
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, ZOOM_REQUEST_CODE);

    }

    //裁剪后设置图片
    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");

            //iv_info_icon.setImageBitmap(bitmap);
            if (imagePath != null) {
                iv_info_icon.setImageBitmap(ImageUtil.rotateBitmap(bitmap, ImageUtil.readPictureDegree(imagePath)));
                imagePath = null;
            } else {
                iv_info_icon.setImageBitmap(bitmap);
            }
        }
    }

    private void putImgString() {
        //保存
        BitmapDrawable drawable = (BitmapDrawable) iv_info_icon.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步:将bitmap压缩成字节数组输出流
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
        //第二步:利用Base64将我们的字节数组输出流转换成String
        byte[] bytes = outputStream.toByteArray();
        String imgString = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
        //第三步:将String保存
        MyUser bmobUser = BmobUser.getCurrentUser(MyUser.class);
        // 修改用户的头像为imgString
        bmobUser.setImgString(imgString);
        bmobUser.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //toast("更新用户信息成功");
                } else {
                    //toast("更新用户信息失败:" + e.getMessage());
                    Toast.makeText(UserInfoActivity.this, getString(R.string.update_user_information_failed) + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
