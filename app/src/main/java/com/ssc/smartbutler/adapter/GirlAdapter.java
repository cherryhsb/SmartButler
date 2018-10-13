package com.ssc.smartbutler.adapter;

/*
 *  项目名：    helloworld
 *  包名:       com.ssc.gridviewdemo
 *  文件名：    GirlAdapter
 *  创建者：    SSC
 *  创建时间：   2018/9/23 7:39
 *  描述：     TODO
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.GirlData;
import com.ssc.smartbutler.view.CustomDialog;

import java.util.List;

public class GirlAdapter extends RecyclerView.Adapter<GirlAdapter.ViewHolder> {

    private Context mContext;

    private List<GirlData> mGirlDataList;

    private WindowManager windowManager;

    //提示框
    private CustomDialog mDialog;

    private PhotoView mPhoto_view;

    //屏幕宽
    private int width;

    //子项的类
    public class ViewHolder extends RecyclerView.ViewHolder {

        View girlView;
        ImageView iv_girl;
        TextView tv_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            girlView = itemView;
            iv_girl = itemView.findViewById(R.id.iv_girl);
            tv_desc = itemView.findViewById(R.id.tv_desc);
        }
    }

    public GirlAdapter(Context mContext, List<GirlData> mGirlDataList, CustomDialog dialog, PhotoView photo_view) {
        this.mContext = mContext;
        this.mGirlDataList = mGirlDataList;
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();//- 20
        /*dialog = new CustomDialog(mContext, WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,R.layout.dialog_girl,R.style.Theme_dialog, Gravity.CENTER);
        //iv_girl_img = dialog.findViewById(R.id.iv_girl_img);
        photo_view = dialog.findViewById(R.id.photo_view);*/
        this.mDialog = dialog;
        this.mPhoto_view = photo_view;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.item_girl, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.iv_girl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PicassoUtil.loadImageView(getActivity(),girlDataList.get(position).getImgUrl(),photo_view);
                int position = viewHolder.getAdapterPosition();
                showGlideMatch(mGirlDataList.get(position).getImgUrl(),mPhoto_view);
                mDialog.show();
            }
        });
        return viewHolder;
    }

    //当Item被回收的时候调用
    /*@Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        if (holder !=null) {

            if (holder.iv_girl !=null) {

                Glide.clear(holder.img);
            }

            if (holder.linearLayout !=null) {

                holder.linearLayout.setVisibility(View.GONE);

            }

        }
        super.onViewRecycled(holder);
    }*/

    //填充onCreateViewHolder方法返回的holder中的控件
    //设置子项具体的东西
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        GirlData girlData = mGirlDataList.get(i);
        String imgUrl = girlData.getImgUrl();
        String desc = girlData.getDesc();
        //PicassoUtil.loadImageViewSize(mContext, imgUrl,viewHolder.iv_girl,width/2,800);
        /*Transformation transformation = new Transformation() {


            @Override
            public Bitmap transform(Bitmap source) {

                int targetWidth = width;
                if (source.getWidth() == 0) {
                    return source;
                }
                //如果图片大小大于等于设置的宽度，则按照设置的宽度比例来缩放
                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                if (targetHeight != 0 && targetWidth != 0) {
                    Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                    if (result != source) {
                        // Same bitmap is returned if sizes are the same
                        source.recycle();
                    }
                    return result;
                } else {
                    return source;
                }

            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };*/
        //Picasso.get().load(imgUrl).resize(width / 2 - 5 * 4, 720).into(viewHolder.iv_girl);
        //Picasso.get().load(imgUrl).transform(transformation).into(viewHolder.iv_girl);
        //Picasso.get().setIndicatorsEnabled(true);
        /*Glide
                .with(mContext)
                .load(imgUrl)
                //.centerCrop()
                //.placeholder(R.drawable.loading_spinner)
                .into(viewHolder.iv_girl);*/
        //先设置图片占位符
        viewHolder.iv_girl.setImageResource(R.drawable.image_glide);
        //为imageView设置Tag,内容是该imageView等待加载的图片url
        viewHolder.iv_girl.setTag(R.id.tag_iv_girl,imgUrl);

        //加载完毕后判断该imageView等待的图片url是不是加载完毕的这张
        //如果是则为imageView设置图片,否则说明imageView已经被重用到其他item
        if(imgUrl.equals(viewHolder.iv_girl.getTag(R.id.tag_iv_girl))) {
            showGlide(imgUrl,viewHolder.iv_girl);
        }


        //showGlide(imgUrl,viewHolder.iv_girl);
        viewHolder.tv_desc.setText(desc);
    }

    @Override
    public int getItemCount() {
        return mGirlDataList.size();
    }

    private void showGlide(String imgUrl, ImageView iv_girl) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.image_glide)    //加载成功之前占位图
                .error(R.drawable.image_glide)    //加载错误之后的错误图
                .override(width, 600)    //指定图片的尺寸
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                //.fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                //.centerCrop()
                //.circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                //.skipMemoryCache(true)    //跳过内存缓存
                //.diskCacheStrategy(DiskCacheStrategy.NONE)    //跳过磁盘缓存
                //.diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
                //.diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
                ;
        Glide.with(mContext)
                .load(imgUrl)
                .apply(options)
                .into(iv_girl);

    }

    private void showGlideMatch(String imgUrl, ImageView iv_girl) {
        RequestOptions options = new RequestOptions()
                //.placeholder(R.drawable.image_glide)    //加载成功之前占位图
                .error(R.drawable.image_glide)    //加载错误之后的错误图
                //.override(width, 600)    //指定图片的尺寸
                //指定图片的缩放类型为fitCenter （等比例缩放图片，宽或者是高等于ImageView的宽或者是高。）
                //.fitCenter()
                //指定图片的缩放类型为centerCrop （等比例缩放图片，直到图片的狂高都大于等于ImageView的宽度，然后截取中间的显示。）
                //.centerCrop()
                //.circleCrop()//指定图片的缩放类型为centerCrop （圆形）
                //.skipMemoryCache(true)    //跳过内存缓存
                //.diskCacheStrategy(DiskCacheStrategy.NONE)    //跳过磁盘缓存
                //.diskCacheStrategy(DiskCacheStrategy.DATA)    //只缓存原来分辨率的图片
                //.diskCacheStrategy(DiskCacheStrategy.RESOURCE)    //只缓存最终的图片
                ;
        Glide.with(mContext)
                .load(imgUrl)
                .apply(options)
                .into(iv_girl);

    }
}
