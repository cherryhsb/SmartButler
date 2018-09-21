package com.ssc.smartbutler.adapter;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.adapter
 *  文件名：    ChatAdapter
 *  创建者：    SSC
 *  创建时间：   2018/7/31 9:27
 *  描述：     对话adapter
 */

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ssc.smartbutler.R;
import com.ssc.smartbutler.entity.ChatData;
import com.ssc.smartbutler.entity.MyUser;
import com.ssc.smartbutler.utils.L;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

import static com.ssc.smartbutler.application.BaseApplication.getContext;
import static com.ssc.smartbutler.application.BaseApplication.userInfo;

public class ChatAdapter extends BaseAdapter {

    private static final String TAG = "ChatAdapter";

    public static final int VALUE_LEFT_TEXT = 1;
    public static final int VALUE_RIGHT_TEXT = 2;

    private Context mContext;

    private LayoutInflater inflater;

    private ChatData chatData;

    private List<ChatData> mList;

    //private Bitmap bitmap = null;

    private boolean isIcon;

    private String mCopiedText;



    public ChatAdapter(Context mContext, List<ChatData> mList) {
        this.mContext = mContext;
        this.mList = mList;
        //布局加载器获取系统服务
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        initPopupWindow();

        //userInfo = BmobUser.getCurrentUser(MyUser.class);
        L.i(TAG, userInfo+"");
        if (userInfo != null) {
            BmobFile icon = userInfo.getIcon();
            //L.i(TAG, icon.getUrl()+"hahaha");
            if (icon != null) {//设置过头像
                isIcon = true;
            } else {//没有设置过头像,显示默认图标
                isIcon = false;
            }
        } else {
            //缓存用户对象为空
            isIcon = false;
        }
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

        ViewHolderLeft viewHolderLeft = null;
        ViewHolderRight viewHolderRight = null;
        //获取当前要显示的type,根据这个type来区分数据的加载
        int type = getItemViewType(position);
        if (convertView == null) {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    convertView = inflater.inflate(R.layout.item_chat_left, null);

                    viewHolderLeft = new ViewHolderLeft();
                    viewHolderLeft.tv_chat_left = convertView.findViewById(R.id.tv_chat_left);

                    //然后为ListView中的TextView设置长按事件，在TextView下方显示PopupWindow：
                    viewHolderLeft.tv_chat_left.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // 方法1：LongClick事件 + PopupWindow
                            mPopupWindow.showAsDropDown(v);
                            mCopiedText = ((TextView) v).getText().toString();
                            L.i(TAG, "弹出");
                            return true;
                        }
                    });

                    convertView.setTag(viewHolderLeft);
                    break;
                case VALUE_RIGHT_TEXT:
                    convertView = inflater.inflate(R.layout.item_chat_right, null);

                    viewHolderRight = new ViewHolderRight();
                    viewHolderRight.tv_chat_right = convertView.findViewById(R.id.tv_chat_right);
                    viewHolderRight.iv_chat_right = convertView.findViewById(R.id.iv_chat_right);

                    //然后为ListView中的TextView设置长按事件，在TextView下方显示PopupWindow：
                    viewHolderRight.tv_chat_right.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            // 方法1：LongClick事件 + PopupWindow
                            mPopupWindow.showAsDropDown(v);
                            mCopiedText = ((TextView) v).getText().toString();
                            L.i(TAG, "弹出");
                            return true;
                        }
                    });

                    convertView.setTag(viewHolderRight);
                    break;
            }
        } else {
            switch (type) {
                case VALUE_LEFT_TEXT:
                    viewHolderLeft = (ViewHolderLeft) convertView.getTag();
                    break;
                case VALUE_RIGHT_TEXT:
                    viewHolderRight = (ViewHolderRight) convertView.getTag();
                    break;
            }
        }

        //赋值
        chatData = mList.get(position);
        switch (type) {
            case VALUE_LEFT_TEXT:
                viewHolderLeft.tv_chat_left.setText(chatData.getText());
                break;
            case VALUE_RIGHT_TEXT:
                viewHolderRight.tv_chat_right.setText(chatData.getText());

                //viewHolderRight.tv_chat_right.setInputType();

                if (isIcon) {
                    if (userInfo.getUsername()!=null){
                        String iconCompressPath=getContext().getExternalFilesDir(userInfo.getUsername()).getAbsolutePath()+ "/icon/" + userInfo.getUsername() + "(compress).jpg";
                        viewHolderRight.iv_chat_right.setImageURI(Uri.fromFile(new File(iconCompressPath)));
                    }else {
                        viewHolderRight.iv_chat_right.setImageDrawable(mContext.getResources().getDrawable((R.drawable.user)));
                    }

                } else {
                    viewHolderRight.iv_chat_right.setImageDrawable(mContext.getResources().getDrawable((R.drawable.user)));
                }

                L.i(TAG, "");
                break;
        }

        return convertView;
    }

    //根据数据源的position来返回要显示的item
    @Override
    public int getItemViewType(int position) {
        int type = mList.get(position).getType();
        return type;
    }

    //返回所有的layout数量
    @Override
    public int getViewTypeCount() {
        return 3;//mlist.size()+1
    }

    public class ViewHolderLeft {
        private TextView tv_chat_left;
    }

    public class ViewHolderRight {
        private TextView tv_chat_right;
        private ImageView iv_chat_right;
    }


    private PopupWindow mPopupWindow;

    private void initPopupWindow() {
        //先初始化PopupWindow，弹出窗口上会有复制按钮（也可以自定义其它需要的按钮）：
        View popupView = inflater.inflate(R.layout.layout_popup_window, null);

        mPopupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(mContext.getResources(), (Bitmap) null));

        //弹出的复制按钮
        Button btnCopy = (Button) popupView.findViewById(R.id.btn_copy);
        btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyText(mCopiedText);

                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
            }
        });
    }

    //最后为弹出窗口中的复制按钮设置响应事件，利用ClipboardManager复制TextView内容：
    private void copyText(String copiedText) {
        ClipboardManager clipboardManager = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, copiedText));

        Toast.makeText(mContext, "已复制", Toast.LENGTH_SHORT).show();
    }


}
