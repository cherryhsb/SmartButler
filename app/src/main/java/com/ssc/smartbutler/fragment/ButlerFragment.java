package com.ssc.smartbutler.fragment;

/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.fragment
 *  创建者：    SSC
 *  创建时间：   2018/7/8 3:04
 *  描述：     TODO
 */

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.ChatAdapter;
import com.ssc.smartbutler.entity.ChatData;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.ssc.smartbutler.utils.StaticClass.TULING_ID;

public class ButlerFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "ButlerFragment";

    private EditText et_chat;

    private ListView lv_chat;

    private Button btn_chat;

    private List<ChatData> chatDataList;

    private ChatAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_butler, null);

        initView(view);

        return view;
    }

    private void initView(View view) {
        et_chat = view.findViewById(R.id.et_chat);
        lv_chat = view.findViewById(R.id.lv_chat);
        btn_chat = view.findViewById(R.id.btn_chat);

        /*iv_chat_right = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
                inflate(R.layout.item_chat_left, null).findViewById(R.id.tv_chat_right);
        //当前用户
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo != null){
            //已经登陆显示用户头像
            if (userInfo.getImgString()!=null){
                //利用Base64将String转化为byte数组
                byte[] bytes = Base64.decode(userInfo.getImgString(),Base64.DEFAULT);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                //生成bitmap,显示出来
                iv_chat_right.setImageBitmap(BitmapFactory.decodeStream(inputStream));
            }
        }else{
            //缓存用户对象为空时，什么都不做
        }*/

        /*userInfo = BmobUser.getCurrentUser(MyUser.class);
        if (userInfo != null) {
            if (userInfo.getImgString() != null) {
                //利用Base64将String转化为byte数组
                byte[] bytes = Base64.decode(userInfo.getImgString(), Base64.DEFAULT);
                ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
                //生成bitmap
                //iv_info_icon.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                ImageView imageView = inflater.inflate(R.layout.item_chat_right, null).findViewById(R.id.iv_chat_right);
                //imageView.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                iv_111.setImageBitmap(BitmapFactory.decodeStream(inputStream));
                L.i(TAG,userInfo.getUsername());
            }
        }else {
            //缓存用户对象为空
        }*/

        btn_chat.setOnClickListener(this);

        //设置适配器
        chatDataList = new ArrayList<>();
        adapter = new ChatAdapter(getActivity(), chatDataList);
        lv_chat.setAdapter(adapter);

        addLeftItem("你好我是小管家");

        //设置button是否可以点击
        setButtonEnabled();
    }

    private void post(String s) {
        String url = "http://www.tuling123.com/openapi/api";
        HttpParams params = new HttpParams();
        params.put("key", TULING_ID);
        params.put("info", s);
        RxVolley.post(url, params, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //super.onSuccess(t);
                L.i(TAG, t);
                parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            String code = jsonObject.getString("code");
            String text = jsonObject.getString("text");
            L.i(TAG, code);
            switch (code) {
                case "100000":
                    //文本类
                    addLeftItem(text);
                    break;
                case "200000":
                    //链接类
                    String url = jsonObject.getString("url");
                    addLeftItem(text);
                    addLeftItem(url);
                    break;
                case "302000":
                    //新闻类
                    break;
                case "308000":
                    //菜谱类
                    break;
                case "313000":
                    //儿歌类
                    break;
                case "314000":
                    //诗词类
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat:
                String s = et_chat.getText().toString();
                addRightItem(s);
                et_chat.setText(null);
                post(s);
                break;
        }
    }

    //添加左边文本
    private void addLeftItem(String text) {
        ChatData chatData = new ChatData();
        chatData.setType(ChatAdapter.VALUE_LEFT_TEXT);
        chatData.setText(text);
        chatDataList.add(chatData);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        lv_chat.setSelection(lv_chat.getBottom());
    }

    //添加右边文本
    private void addRightItem(String text) {

        //添加右边
        ChatData chatData = new ChatData();
        chatData.setType(ChatAdapter.VALUE_RIGHT_TEXT);
        chatData.setText(text);
        chatDataList.add(chatData);
        //通知adapter刷新
        adapter.notifyDataSetChanged();
        //滚动到底部
        lv_chat.setSelection(lv_chat.getBottom());
    }

    private void setButtonEnabled() {
        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(btn_chat);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(et_chat);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    btn_chat.setAlpha(1);
                    btn_chat.setEnabled(true);
                } else {
                    btn_chat.setAlpha(0.5f);
                    btn_chat.setEnabled(false);
                }
            }
        });
    }
}
