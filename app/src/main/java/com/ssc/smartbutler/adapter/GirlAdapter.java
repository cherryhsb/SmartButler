package com.ssc.smartbutler.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.GirlData;
import com.ssc.smartbutler.utils.PicassoUtil;

import java.util.List;

import javax.net.ssl.SSLContext;


/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter
 *  文件名：    GirlAdapter
 *  创建者：    SSC
 *  创建时间：   2018/8/6 14:07
 *  描述：     TODO美女图片适配器
 */

public class GirlAdapter extends BaseAdapter {

    private Context mContext;

    private List<GirlData> mList;

    private LayoutInflater inflater;

    private GirlData girlData;

    private WindowManager windowManager;

    //屏幕宽
    private int width;

    public GirlAdapter(Context mContext, List<GirlData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = windowManager.getDefaultDisplay().getWidth();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView ==null){
            convertView = inflater.inflate(R.layout.item_girl,null);

            viewHolder = new ViewHolder();
            viewHolder.imageView = convertView.findViewById(R.id.iv_girl);
            viewHolder.textView = convertView.findViewById(R.id.tv_girl);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //赋值
        girlData = mList.get(position);
        String imgUrl = girlData.getImgUrl();
        String textUrl = girlData.getDesc();

        PicassoUtil.loadImageViewSize(mContext, imgUrl,viewHolder.imageView,width/2,600);
        viewHolder.textView.setText(textUrl);
        return convertView;
    }

    class ViewHolder {
        private ImageView imageView;
        private TextView textView;
    }
}
