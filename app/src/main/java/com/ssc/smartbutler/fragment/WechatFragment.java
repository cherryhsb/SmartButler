package com.ssc.smartbutler.fragment;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.fragment
 *  创建者：    SSC
 *  创建时间：   2018/7/8 3:05
 *  描述：     TODO
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.WechatAdapter;
import com.ssc.smartbutler.entity.WechatData;
import com.ssc.smartbutler.ui.WebViewActivity;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WechatFragment extends Fragment {

    private static final String TAG = "WechatFragment";

    private ListView lv_wechat;

    private WechatAdapter adapter;

    private List<WechatData> wechatDataList;

    private WechatData wechatData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);

        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_wechat = view.findViewById(R.id.lv_wechat);

        wechatDataList = new ArrayList<>();
        //http://v.juhe.cn/weixin/query?key=您申请的KEY
        String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_ID + "&ps=50";
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //super.onSuccess(t);
                //Toast.makeText(ExpressActivity.this, t,Toast.LENGTH_SHORT).show();
                //L.i(TAG, "Json" + t);
                //4.解析json
                parsingJson(t);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
            }
        });

        lv_wechat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("title",wechatDataList.get(position).getTitle());
                intent.putExtra("url",wechatDataList.get(position).getNewUrl());
                startActivity(intent);
            }
        });

        //L.i(TAG,wechatDataList.size()+"initView");
        /*adapter = new WechatAdapter(getActivity(),wechatDataList);
        lv_wechat.setAdapter(adapter);*/
        //L.i(TAG,wechatDataList.size()+"initView2");
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = (JSONObject) jsonArray.get(i);
                wechatData = new WechatData();
                wechatData.setTitle(json.getString("title"));
                wechatData.setSource(json.getString("source"));
                wechatData.setImgUrl(json.getString("firstImg"));
                wechatData.setNewUrl(json.getString("url"));
                wechatDataList.add(wechatData);
            }
            //L.i(TAG,wechatDataList.size()+"parse");
            adapter = new WechatAdapter(getActivity(), wechatDataList);
            lv_wechat.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
