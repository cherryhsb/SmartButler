package com.ssc.smartbutler.fragment;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.fragment
 *  创建者：    SSC
 *  创建时间：   2018/7/8 3:04
 *  描述：     TODO美女相册
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.GirlAdapter;
import com.ssc.smartbutler.entity.GirlData;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.view.CustomDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GirlFragment extends Fragment {

    private static final String TAG = "GirlFragment";

    //private GridView gv_girl;

    private RecyclerView rv_girl;

    private List<GirlData> girlDataList = new ArrayList<>();

    //提示框
    private CustomDialog dialog;
    //预览图片
    private ImageView iv_girl_img;
    //photoView
    private PhotoViewAttacher mAttacher;
    private PhotoView photo_view;

    //上拉刷更新
    private RefreshLayout refreshLayout;

    //private GirlAdapterOld girlAdapter;

    private GirlAdapter girlAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_girl,null);
        initView(view);
        return view;

    }

    /*
    * photoView流程
    * 1.监听点击事件
    * 2.提示框
    * 3.加载图片
    * 4.photoView缩放功能
    * */
    private void initView(View view) {
        //gv_girl = view.findViewById(R.id.gv_girl);
        rv_girl = view.findViewById(R.id.rv_girl);
        StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        //防止item 交换位置
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        rv_girl.setLayoutManager(layoutManager);


        dialog = new CustomDialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,R.layout.dialog_girl,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        //iv_girl_img = dialog.findViewById(R.id.iv_girl_img);
        photo_view = dialog.findViewById(R.id.photo_view);

        //girlAdapter = new GirlAdapterOld(getActivity(), girlDataList);

        girlAdapter = new GirlAdapter(getActivity(), girlDataList,dialog,photo_view);

        final Integer[] page = {1};

        requestJson(page[0] +"",true);

        /*gv_girl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //解析图片
                *//*PicassoUtil.loadImageView(getActivity(),girlDataList.get(position).getImgUrl(),iv_girl_img);
                //缩放
                mAttacher = new PhotoViewAttacher(iv_girl_img);
                mAttacher.update();*//*
                PicassoUtil.loadImageView(getActivity(),girlDataList.get(position).getImgUrl(),photo_view);
                dialog.show();
            }
        });*/

        refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setEnableRefresh(false);
        /*refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000*//*,false*//*);//传入false表示刷新失败
            }
        });*/
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                requestJson(String.valueOf(++page[0]),false);
                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    private void requestJson(String page, final boolean isFirst){
        RxVolley.get(StaticClass.GIRL_URL+page, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                super.onSuccess(t);
                //L.i(TAG,t);
                parsingJson(t,isFirst);
            }

            @Override
            public void onFailure(int errorNo, String strMsg) {
                super.onFailure(errorNo, strMsg);
                L.i(TAG,strMsg);
            }
        });
    }

    private void parsingJson(String t,boolean isFirst) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONArray results = jsonObject.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject json = (JSONObject) results.get(i);
                String url = json.getString("url");
                String desc = json.getString("desc");
                GirlData girlData = new GirlData();
                girlData.setImgUrl(url);//图片url
                girlData.setDesc(desc);//图片描述
                girlDataList.add(girlData);
            }
            if (isFirst){
                rv_girl.setAdapter(girlAdapter);
            }else {
                girlAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
