package com.ssc.smartbutler.takephoto;

import android.net.Uri;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import com.ssc.smartbutler.R;

import java.io.File;


/**
 * - 支持通过相机拍照获取图片
 * - 支持从相册选择图片
 * - 支持从文件选择图片
 * - 支持多图选择
 * - 支持批量图片裁切
 * - 支持批量图片压缩
 * - 支持对图片进行压缩
 * - 支持对图片进行裁剪
 * - 支持对裁剪及压缩参数自定义
 * - 提供自带裁剪工具(可选)
 * - 支持智能选取及裁剪异常处理
 * - 支持因拍照Activity被回收后的自动恢复
 * Author: crazycodeboy
 * Date: 2016/9/21 0007 20:10
 * Version:4.0.0
 * 技术博文：http://www.devio.org
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class CustomHelper {
    private View rootView;
    /*private RadioGroup rgCrop, rgCompress, rgFrom, rgCropSize, rgCropTool, rgShowProgressBar, rgPickTool, rgCompressTool, rgCorrectTool,
            rgRawFile;
    private EditText etCropHeight, etCropWidth, etLimit, etSize, etHeightPx, etWidthPx;*/

    private Button btn_camera, btn_picture;

    public static CustomHelper of(View rootView) {
        return new CustomHelper(rootView);
    }

    private CustomHelper(View rootView) {
        this.rootView = rootView;
        init();
    }

    private void init() {
        /*//是否裁切
        rgCrop = (RadioGroup) rootView.findViewById(R.id.rgCrop);
        //是否压缩
        rgCompress = (RadioGroup) rootView.findViewById(R.id.rgCompress);
        //压缩工具
        rgCompressTool = (RadioGroup) rootView.findViewById(R.id.rgCompressTool);
        //尺寸/比例
        rgCropSize = (RadioGroup) rootView.findViewById(R.id.rgCropSize);
        //从哪选择
        rgFrom = (RadioGroup) rootView.findViewById(R.id.rgFrom);
        //使用TakePhoto自带相册
        rgPickTool = (RadioGroup) rootView.findViewById(R.id.rgPickTool);
        //拍照压缩后是否保存原图
        rgRawFile = (RadioGroup) rootView.findViewById(R.id.rgRawFile);
        //纠正拍照的照片旋转角度
        rgCorrectTool = (RadioGroup) rootView.findViewById(R.id.rgCorrectTool);
        //显示压缩进度条
        rgShowProgressBar = (RadioGroup) rootView.findViewById(R.id.rgShowProgressBar);
        //裁切工具
        rgCropTool = (RadioGroup) rootView.findViewById(R.id.rgCropTool);
        //尺寸/比例     宽/高
        etCropWidth = (EditText) rootView.findViewById(R.id.etCropWidth);
        etCropHeight = (EditText) rootView.findViewById(R.id.etCropHeight);
        //最多选择
        etLimit = (EditText) rootView.findViewById(R.id.etLimit);
        //大小不超过
        etSize = (EditText) rootView.findViewById(R.id.etSize);
        //大小不超过      宽/高
        etWidthPx = (EditText) rootView.findViewById(R.id.etWidthPx);
        etHeightPx = (EditText) rootView.findViewById(R.id.etHeightPx);*/

        btn_camera = rootView.findViewById(R.id.btn_camera);
        btn_picture = rootView.findViewById(R.id.btn_picture);

    }

    public void takePhoto(View view, TakePhoto takePhoto) {
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

        configCompress(takePhoto);
        configTakePhotoOption(takePhoto);
        switch (view.getId()) {
            case R.id.btn_picture:
                /**
                 * 选择照片
                 */
                /*int limit = Integer.parseInt(etLimit.getText().toString());//最多选择
                if (limit > 1) {
                    if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {//裁剪
                        //图片多选，并裁切
                        takePhoto.onPickMultipleWithCrop(limit, getCropOptions());
                    } else {//不裁剪
                        //图片多选（不裁剪）
                        takePhoto.onPickMultiple(limit);
                    }
                    return;
                }
                if (rgFrom.getCheckedRadioButtonId() == R.id.rbFile) {//从文件选择
                    if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {//裁剪
                        //从文件中获取图片并裁剪
                        takePhoto.onPickFromDocumentsWithCrop(imageUri, getCropOptions());
                    } else {//不裁剪
                        //从文件中获取图片（不裁剪）
                        takePhoto.onPickFromDocuments();
                    }
                    return;
                } else {//从相册选择
                    if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {//裁剪
                        //从相册中获取图片并裁剪
                        takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                    } else {//不裁剪
                        //从相册中获取图片（不裁剪）
                        takePhoto.onPickFromGallery();
                    }
                }*/
                //从相册中获取图片并裁剪
                takePhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
                break;
            case R.id.btn_camera:
                //拍照
                /*if (rgCrop.getCheckedRadioButtonId() == R.id.rbCropYes) {//裁剪
                    //从相机获取图片并裁剪
                    takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                } else {
                    //从相机获取图片(不裁剪)
                    takePhoto.onPickFromCapture(imageUri);
                }*/
                //从相机获取图片并裁剪
                takePhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                break;
            default:
                break;
        }
    }

    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        /*if (rgPickTool.getCheckedRadioButtonId() == R.id.rbPickWithOwn) {//使用TakePhoto自带相册
            builder.setWithOwnGallery(true);
        }
        if (rgCorrectTool.getCheckedRadioButtonId() == R.id.rbCorrectYes) {//纠正拍照的照片旋转角度
            builder.setCorrectImage(true);
        }*/
        builder.setWithOwnGallery(false);//不使用TakePhoto自带相册
        builder.setCorrectImage(true);//纠正拍照的照片旋转角度
        takePhoto.setTakePhotoOptions(builder.create());

    }

    private void configCompress(TakePhoto takePhoto) {
        /*if (rgCompress.getCheckedRadioButtonId() != R.id.rbCompressYes) {//不压缩
            takePhoto.onEnableCompress(null, false);
            return;
        }*/
        //压缩
        /*int maxSize = Integer.parseInt(etSize.getText().toString());//大小
        int width = Integer.parseInt(etWidthPx.getText().toString());//宽
        int height = Integer.parseInt(etHeightPx.getText().toString());//高
        //显示压缩进度条
        boolean showProgressBar = rgShowProgressBar.getCheckedRadioButtonId() == R.id.rbShowYes ? true : false;
        //拍照压缩后是否保存原图
        boolean enableRawFile = rgRawFile.getCheckedRadioButtonId() == R.id.rbRawYes ? true : false;
        CompressConfig config;
        //压缩工具
        if (rgCompressTool.getCheckedRadioButtonId() == R.id.rbCompressWithOwn) {//自带
            config = new CompressConfig.Builder().setMaxSize(maxSize)
                .setMaxPixel(width >= height ? width : height)
                .enableReserveRaw(enableRawFile)
                .create();
        } else {//luban
            LubanOptions option = new LubanOptions.Builder().setMaxHeight(height).setMaxWidth(width).setMaxSize(maxSize).create();
            config = CompressConfig.ofLuban(option);
            config.enableReserveRaw(enableRawFile);
        }*/
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
        /*if (rgCrop.getCheckedRadioButtonId() != R.id.rbCropYes) {//不裁剪
            return null;
        }*/
        /*int height = Integer.parseInt(etCropHeight.getText().toString());
        int width = Integer.parseInt(etCropWidth.getText().toString());*/
        //boolean withWonCrop = rgCropTool.getCheckedRadioButtonId() == R.id.rbCropOwn ? true : false;
        boolean withWonCrop = false;//裁剪工具:第三方

        CropOptions.Builder builder = new CropOptions.Builder();

        /*if (rgCropSize.getCheckedRadioButtonId() == R.id.rbAspect) {//自定义宽高比
            builder.setAspectX(width).setAspectY(height);
        } else {//默认宽高比
            builder.setOutputX(width).setOutputY(height);
        }*/
        int height = 800;
        int width = 800;
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

}
