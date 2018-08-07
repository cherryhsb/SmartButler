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
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.HttpParams;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.ChatAdapter;
import com.ssc.smartbutler.entity.ChatData;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;

import static com.ssc.smartbutler.application.BaseApplication.userInfo;
import static com.ssc.smartbutler.utils.StaticClass.TTS_ID;
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

        // 将“12345678”替换成您申请的APPID，申请地址：http://www.xfyun.cn
        // 请勿在“=”与appid之间添加任何空字符或者转义符
        SpeechUtility.createUtility(getActivity(), SpeechConstant.APPID +"="+TTS_ID);
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

        initTTS();

        btn_chat.setOnClickListener(this);

        //设置适配器
        chatDataList = new ArrayList<>();
        adapter = new ChatAdapter(getActivity(), chatDataList);
        lv_chat.setAdapter(adapter);

        addLeftItem("你好我是小管家",true);

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
                    addLeftItem(text,true);
                    break;
                case "200000":
                    //链接类
                    String url = jsonObject.getString("url");
                    addLeftItem(text,true);
                    addLeftItem(url,false);
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
    private void addLeftItem(String text,boolean isSpeak) {
        if (ShareUtil.getBoolean(getActivity(),"IS_TTS",false) && isSpeak){
            mTts.startSpeaking(text,mTtsListener);
        }
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


    /*
     * TTS
     * */
    private SpeechSynthesizer mTts;

    //TTS
    private void initTTS() {
        //1.初始化
        mTts = SpeechSynthesizer.createSynthesizer(getActivity(), mTtsInitListener);
        //2.设置参数
        setParam();
        //3.播放
        //mTts.startSpeaking("哈哈哈哈哈哈哈哈哈",mTtsListener);
    }

    /**
     * 初始化监听。
     */
    private InitListener mTtsInitListener = new InitListener() {
        @Override
        public void onInit(int code) {
            L.i(TAG, "InitListener init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                //showTip("初始化失败,错误码："+code);
            } else {
                // 初始化成功，之后可以调用startSpeaking方法
                // 注：有的开发者在onCreate方法中创建完合成对象之后马上就调用startSpeaking进行合成，
                // 正确的做法是将onCreate中的startSpeaking调用移至这里
            }
        }
    };

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    // 默认发音人
    private String voicer = "xiaoyan";
    /**
     * 参数设置
     *
     * @return
     */
    private void setParam() {
        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);
        // 根据合成引擎设置相应参数
        if (mEngineType.equals(SpeechConstant.TYPE_CLOUD)) {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
            //onevent回调接口实时返回音频流数据
            //mTts.setParameter(SpeechConstant.TTS_DATA_NOTIFY, "1");
            // 设置在线合成发音人
            mTts.setParameter(SpeechConstant.VOICE_NAME, voicer);
            //设置合成语速
            mTts.setParameter(SpeechConstant.SPEED, "50");
            //设置合成音调
            mTts.setParameter(SpeechConstant.PITCH, "50");
            //设置合成音量
            mTts.setParameter(SpeechConstant.VOLUME, "50");
        } else {
            mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
            // 设置本地合成发音人 voicer为空，默认通过语记界面指定发音人。
            mTts.setParameter(SpeechConstant.VOICE_NAME, "");
            /**
             * TODO 本地合成不设置语速、音调、音量，默认使用语记设置
             * 开发者如需自定义参数，请参考在线合成参数设置
             */
        }
        //设置播放器音频流类型
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "pcm");
        mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts.pcm");
    }

    /**
     * 合成回调监听。
     */
    private SynthesizerListener mTtsListener = new SynthesizerListener() {

        @Override
        public void onSpeakBegin() {
            //showTip("开始播放");
        }

        @Override
        public void onSpeakPaused() {
            //showTip("暂停播放");
        }

        @Override
        public void onSpeakResumed() {
            //showTip("继续播放");
        }

        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos,
                                     String info) {
            // 合成进度
            /*mPercentForBuffering = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));*/
        }

        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            // 播放进度
            /*mPercentForPlaying = percent;
            showTip(String.format(getString(R.string.tts_toast_format),
                    mPercentForBuffering, mPercentForPlaying));

            SpannableStringBuilder style=new SpannableStringBuilder(texts);
            if(!"henry".equals(voicer)||!"xiaoyan".equals(voicer)||
                    !"xiaoyu".equals(voicer)||!"catherine".equals(voicer))
                endPos++;
            Log.e(TAG,"beginPos = "+beginPos +"  endPos = "+endPos);
            style.setSpan(new BackgroundColorSpan(Color.RED),beginPos,endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ((EditText) findViewById(R.id.tts_text)).setText(style);*/
        }

        /*@Override
        public void onCompleted(SpeechError speechError) {

        }*/

        @Override
        public void onCompleted(SpeechError error) {
            /*if (error == null) {
                showTip("播放完成");
            } else if (error != null) {
                showTip(error.getPlainDescription(true));
            }*/
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            /*Log.e(TAG,"TTS Demo onEvent >>>"+eventType);
            if (SpeechEvent.EVENT_SESSION_ID == eventType) {
                String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
                Log.d(TAG, "session id =" + sid);
            }*/
        }
    };
}
