package com.ssc.smartbutler.utils;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.utils
 *  文件名：    PicassoUtil
 *  创建者：    SSC
 *  创建时间：   2018/8/6 14:31
 *  描述：     TODOpicasso加载工具
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoUtil {

    //加载图片
    public static void loadImageView(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(url).into(imageView);
    }

    //加载图片(指定大小)
    public static void loadImageViewSize(Context context, String url, ImageView imageView, int width, int height) {
        Picasso.with(context)
                .load(url)
                .resize(width, height)
                .centerCrop()
                .into(imageView);
    }

    //加载图片(有默认图片)
    public static void loadImageViewHolder(Context context, String url, int loadImg, int errorImg, ImageView imageView, int width, int height) {
        Picasso.with(context)
                .load(url)
                .placeholder(loadImg)
                .error(errorImg)
                .resize(width, width)
                .centerCrop()
                .into(imageView);
    }

    //裁剪图片
    public static void loadImageViewCrop(Context context, String url, ImageView imageView){
        Picasso.with(context)
                .load(url)
                .transform(new CropSquareTransformation())
                .into(imageView);
    }

    //按比例裁剪 矩形
    public static class CropSquareTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                //回收
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
}
