package com.ssc.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.retrofit.wechat.WechatItem;

import java.util.List;


/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter
 *  文件名：    WechatAdapter
 *  创建者：    SSC
 *  创建时间：   2018/8/3 18:10
 *  描述：     TODO:
 */

public class WechatAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private Context mContext;


    private List<WechatItem> wechatItemList;

    /*public WechatAdapter(Context mContext, List<WechatData> wechatDataList) {
        this.mContext = mContext;
        this.wechatDataList = wechatDataList;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }*/

    public WechatAdapter(Context mContext, List<WechatItem> wechatItemList) {
        this.mContext = mContext;
        this.wechatItemList = wechatItemList;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /*@Override
    public int getCount() {
        return wechatDataList.size();
    }*/

    @Override
    public int getCount() {
        return wechatItemList.size();
    }

    /*@Override
    public Object getItem(int position) {
        return wechatDataList.get(position);
    }*/

    @Override
    public Object getItem(int position) {
        return wechatItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_wechat,null);
            //viewHolder.iv_wechat = convertView.findViewById(R.id.iv_wechat);
            viewHolder.tv_wechat_title = convertView.findViewById(R.id.tv_wechat_title);
            viewHolder.tv_wechat_source = convertView.findViewById(R.id.tv_wechat_source);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //设置数据
        /*wechatData = wechatDataList.get(position);
        viewHolder.tv_wechat_title.setText(wechatData.getTitle());
        viewHolder.tv_wechat_source.setText(wechatData.getSource());*/

        //Toast.makeText(mContext,"xixixi"+wechatItemList.toString(),Toast.LENGTH_SHORT).show();
        viewHolder.tv_wechat_title.setText(wechatItemList.get(position).getTitle());
        viewHolder.tv_wechat_source.setText(wechatItemList.get(position).getSource());

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "打开网页",Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }

    public class ViewHolder{
        private TextView tv_wechat_title,tv_wechat_source;
    }
}
