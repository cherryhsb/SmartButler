package com.ssc.smartbutler.takephoto;

import android.content.Context;
import android.net.Uri;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import com.ssc.smartbutler.R;

import java.io.File;


/**
 * 头像
 */


public class CustomHelper {

    Context context;
    String username;

    public CustomHelper(Context context, String username) {
        this.context = context;
        this.username = username;
    }

    public void onClick(int view, TakePhoto takePhoto) {
        /*File file = new File(Environment.getExternalStorageDirectory(),//获取SD卡根目录
                "/icon/" + System.currentTimeMillis() + ".jpg");*/
        //原图(未压缩)
        File file = new File(context.getExternalFilesDir(username),//获取私有目录
                "/icon/" +username + ".jpg");

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view) {
            case R.id.btn_picture:
                //从相册中获取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;
            case R.id.btn_camera:
                //从相机获取图片并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                break;
            default:
                break;
        }
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setWithOwnGallery(false);//不使用TakePhoto自带相册
        builder.setCorrectImage(true);//纠正拍照的照片旋转角度
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void configCompress(TakePhoto takePhoto) {
        /**
         * 是否压缩:是
         * 压缩工具:luban
         * 显示压缩进度:是
         * 大小102400,宽高800
         */
        int maxSize = 102400;//大小
        int width = 800;//宽
        int height = 800;//高
        CompressConfig config;
        LubanOptions option = new LubanOptions.Builder().setMaxHeight(height).setMaxWidth(width).setMaxSize(maxSize).create();
        config = CompressConfig.ofLuban(option);
        boolean enableRawFile = true;
        config.enableReserveRaw(enableRawFile);
        boolean showProgressBar = true;
        takePhoto.onEnableCompress(config, showProgressBar);


    }

    private CropOptions getCropOptions() {
        boolean withWonCrop = false;//裁剪工具:第三方

        CropOptions.Builder builder = new CropOptions.Builder();

        int height = 800;
        int width = 800;
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
