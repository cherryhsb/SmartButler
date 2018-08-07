package com.ssc.smartbutler.adapter;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter
 *  文件名：    ExpressAdapter
 *  创建者：    SSC
 *  创建时间：   2018/7/31 7:33
 *  描述：     快递查询适配器
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.ExpressData;

import java.util.List;

public class ExpressAdapter extends BaseAdapter{

    private Context mContext;

    private List<ExpressData> mList;

    //布局加载器
    private LayoutInflater inflater;

    private ExpressData expressData;

    public ExpressAdapter(Context mContext,List<ExpressData> mList){
        this.mContext = mContext;
        this.mList = mList;
        //布局加载器获取系统服务
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
            //第一次加载
            convertView = inflater.inflate(R.layout.item_express,null);

            viewHolder = new ViewHolder();
            viewHolder.tv_remark = convertView.findViewById(R.id.tv_remark);
            viewHolder.tv_zone = convertView.findViewById(R.id.tv_zone);
            viewHolder.tv_datetime = convertView.findViewById(R.id.tv_datetime);
            //设置缓存
            convertView.setTag(viewHolder);
        }else {
            //不是第一次加载
            viewHolder = (ViewHolder) convertView.getTag();
        }

        //设置数据
        expressData = mList.get(position);
        viewHolder.tv_remark.setText(expressData.getRemark());
        viewHolder.tv_zone.setText(expressData.getZone());
        viewHolder.tv_datetime.setText(expressData.getDatetime());

        if (viewHolder.tv_zone.getText().toString().isEmpty()){
            viewHolder.tv_zone.setVisibility(View.GONE);
        }else {
            viewHolder.tv_zone.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    class ViewHolder{
        private TextView tv_remark,tv_zone,tv_datetime;

    }
}
