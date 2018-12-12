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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.GirlAdapter;
import com.ssc.smartbutler.retrofit.girl.GirlItem;
import com.ssc.smartbutler.retrofit.girl.GirlPicture;
import com.ssc.smartbutler.retrofit.girl.GirlService;
import com.ssc.smartbutler.utils.ScreenUtils;
import com.ssc.smartbutler.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GirlFragment extends Fragment {

    private static final String TAG = "GirlFragment";

    public RecyclerView rv_girl;

    private List<GirlItem> girlItemList = new ArrayList<>();

    //提示框
    private CustomDialog dialog;

    private PhotoView photo_view;

    //上拉刷更新
    private RefreshLayout refreshLayout;

    private GirlAdapter girlAdapter;

    final Integer[] page = {1};


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
        final StaggeredGridLayoutManager layoutManager = new
                StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);//解决滑动到顶端时Item左右切换的问题
        rv_girl.setLayoutManager(layoutManager);
        //rv_girl.setItemAnimator(null);
        rv_girl.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //防止第一行到顶部有空白区域
                layoutManager.invalidateSpanAssignments();
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        dialog = new CustomDialog(getActivity(), WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT,R.layout.dialog_girl,R.style.Theme_dialog, Gravity.CENTER,R.style.pop_anim_style);
        //iv_girl_img = dialog.findViewById(R.id.iv_girl_img);
        photo_view = dialog.findViewById(R.id.photo_view);

        //动态设置photo_view高度
        LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) photo_view.getLayoutParams(); //取控件textView当前的布局参数
        linearParams.height = ScreenUtils.getScreenHeight(requireActivity()) * 4 / 5;;// 控件的宽强制设成
        photo_view.setLayoutParams(linearParams); //使设置好的布局参数应用到控件

        girlAdapter = new GirlAdapter(getActivity(), girlItemList,dialog,photo_view);

        getGirlWithRetrofit(page[0] +"",true);


        refreshLayout = view.findViewById(R.id.refreshLayout);
        //refreshLayout.setEnableRefresh(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                girlAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                getGirlWithRetrofit(++page[0] +"",false);

                refreshlayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });


    }

    private void getGirlWithRetrofit(final String page, final boolean isFirst) {
        //"https://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://gank.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GirlService girlService = retrofit.create(GirlService.class);
        Call<GirlPicture> call = girlService.getGirl(page);
        call.enqueue(new Callback<GirlPicture>() {
            @Override
            public void onResponse(Call<GirlPicture> call, Response<GirlPicture> response) {
                girlItemList.addAll(response.body().getGirlItemList());
                if (isFirst){
                    rv_girl.setAdapter(girlAdapter);
                }else {
                    //girlAdapter.notifyDataSetChanged();
                    int i = Integer.parseInt(page)*20+1;
                    girlAdapter.notifyItemInserted(i);
                }
            }

            @Override
            public void onFailure(Call<GirlPicture> call, Throwable t) {
                Toast.makeText(getActivity(), "请求失败:"+call.request().url(),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
            }
        });

    }

    public void toTop() {
        rv_girl.scrollToPosition(0);
    }
}
