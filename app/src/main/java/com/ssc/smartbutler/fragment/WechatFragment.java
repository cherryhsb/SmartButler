package com.ssc.smartbutler.fragment;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.fragment
 *  创建者：    SSC
 *  创建时间：   2018/7/8 3:05
 *  描述：     微信精选
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
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.WechatAdapter;
import com.ssc.smartbutler.retrofit.wechat.WechatNews;
import com.ssc.smartbutler.retrofit.wechat.WechatItem;
import com.ssc.smartbutler.retrofit.wechat.WechatService;
import com.ssc.smartbutler.activity.WebViewActivity;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.StaticClass;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WechatFragment extends Fragment {

    private static final String TAG = "WechatFragment";

    public ListView lv_wechat;

    private WechatAdapter adapter;

    private List<WechatItem> wechatItemList = new ArrayList<>();

    //上拉加载更多
    private RefreshLayout refreshLayout;

    final Integer[] page = {1};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wechat, null);

        initView(view);
        return view;
    }

    private void initView(View view) {
        lv_wechat = view.findViewById(R.id.lv_wechat);


        adapter = new WechatAdapter(getActivity(), wechatItemList);

        final String url = "http://v.juhe.cn/weixin/query?key=" + StaticClass.WECHAT_ID + "&ps=50";
        final String url1 = "http://v.juhe.cn";

        getWechatWithRetrofit(url1,true,1);


        lv_wechat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                intent.putExtra("title", wechatItemList.get(position).getTitle());
                intent.putExtra("url", wechatItemList.get(position).getUrl());
                startActivity(intent);
            }
        });


        refreshLayout = view.findViewById(R.id.refreshLayout_wechat);
        //refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
                adapter.notifyDataSetChanged();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败

                getWechatWithRetrofit(url1,false,++page[0]);
            }
        });

    }

    private void getWechatWithRetrofit(String url1, final boolean isFirst, int pno) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://v.juhe.cn")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WechatService wechatService = retrofit.create(WechatService.class);
        Call<WechatNews> call = wechatService.getWechatNews(pno, 50);
        call.enqueue(new Callback<WechatNews>() {
            @Override
            public void onResponse(Call<WechatNews> call, Response<WechatNews> response) {

                WechatNews wechat = response.body();
                if (wechat != null) {
                    if (wechat.getError_code() == 0){
                        wechatItemList.addAll(wechat.getWechatResult().getWechatItemList());

                        if (isFirst){
                            L.i(TAG,wechatItemList.toString());
                            //adapter = new WechatAdapter(getActivity(), wechatItemList);
                            lv_wechat.setAdapter(adapter);
                        }else {
                            adapter.notifyDataSetChanged();
                        }
                    }else {
                        Toast.makeText(getActivity(), "请求失败",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<WechatNews> call, Throwable t) {
                Toast.makeText(getActivity(), "请求失败:"+call.request().url(),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    public void toTop(){
        lv_wechat.setSelection(0);
    };




}
