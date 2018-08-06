package com.ssc.smartbutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.WechatData;

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

    private WechatData wechatData;

    private List<WechatData> mList;

    public WechatAdapter(Context mContext, List<WechatData> mList) {
        this.mContext = mContext;
        this.mList = mList;

        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        wechatData = mList.get(position);
        viewHolder.tv_wechat_title.setText(wechatData.getTitle());
        viewHolder.tv_wechat_source.setText(wechatData.getSource());

        /*convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "打开网页",Toast.LENGTH_SHORT).show();
            }
        });*/

        return convertView;
    }

    public class ViewHolder{
        private ImageView iv_wechat;
        private TextView tv_wechat_title,tv_wechat_source;
    }
}
