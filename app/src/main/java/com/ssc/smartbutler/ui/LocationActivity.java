package com.ssc.smartbutler.ui;

/*
 *  项目名：    SmartButler
 *  包名:       com.ssc.smartbutler.ui
 *  文件名：    LocationActivity
 *  创建者：    SSC
 *  创建时间：   2018/8/1 0:55
 *  描述：     TODO:归属地查询
 */

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.ssc.smartbutler.MainActivity;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.ShareUtil;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;

import org.json.JSONException;
import org.json.JSONObject;

import static com.ssc.smartbutler.utils.StaticClass.SHARE_IS_FIRST;
import static com.ssc.smartbutler.utils.StaticClass.SHARE_IS_HINT_LOCATION;

public class LocationActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LocationActivity";

    private EditText et_location_phone;

    private ImageView iv_company;

    private TextView tv_location_info;

    private Button btn_1, btn_2, btn_3, btn_4, btn_5, btn_6, btn_7, btn_8, btn_9,
            btn_0, btn_del, btn_location_query;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        initView();

        L.i(TAG,isHint()+"");
        if (isHint()){
            showDialog();
        }
    }

    private void initView() {
        et_location_phone = findViewById(R.id.et_location_phone);
        iv_company = findViewById(R.id.iv_company);
        tv_location_info = findViewById(R.id.tv_location_info);
        btn_1 = findViewById(R.id.btn_1);
        btn_2 = findViewById(R.id.btn_2);
        btn_3 = findViewById(R.id.btn_3);
        btn_4 = findViewById(R.id.btn_4);
        btn_5 = findViewById(R.id.btn_5);
        btn_6 = findViewById(R.id.btn_6);
        btn_7 = findViewById(R.id.btn_7);
        btn_8 = findViewById(R.id.btn_8);
        btn_9 = findViewById(R.id.btn_9);
        btn_0 = findViewById(R.id.btn_0);
        btn_del = findViewById(R.id.btn_del);
        btn_location_query = findViewById(R.id.btn_location_query);

        btn_1.setOnClickListener(this);
        btn_2.setOnClickListener(this);
        btn_3.setOnClickListener(this);
        btn_4.setOnClickListener(this);
        btn_5.setOnClickListener(this);
        btn_6.setOnClickListener(this);
        btn_7.setOnClickListener(this);
        btn_8.setOnClickListener(this);
        btn_9.setOnClickListener(this);
        btn_0.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_location_query.setOnClickListener(this);

        //禁止弹出输入法
        //输入类型为没有指定明确的类型的特殊内容类型
        et_location_phone.setInputType(InputType.TYPE_NULL);

        //设置button是否可以点击
        setButtonEnabled();

        //长按事件
        btn_del.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                et_location_phone.setText("");
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        String s = et_location_phone.getText().toString();
        switch (v.getId()) {
            case R.id.btn_1:
            case R.id.btn_2:
            case R.id.btn_3:
            case R.id.btn_4:
            case R.id.btn_5:
            case R.id.btn_6:
            case R.id.btn_7:
            case R.id.btn_8:
            case R.id.btn_9:
            case R.id.btn_0:
                et_location_phone.setText(s + ((Button) v).getText());
                //移动光标
                et_location_phone.setSelection(s.length() + 1);
                break;
            case R.id.btn_del:
                if (!TextUtils.isEmpty(s) && s.length() > 0) {
                    et_location_phone.setText(s.substring(0, s.length() - 1));
                    //移动光标
                    et_location_phone.setSelection(s.length() - 1);
                }
                break;
            case R.id.btn_location_query:
                /*
                 * 逻辑
                 * 1.获取输入框内容
                 * 2.判断是否为空
                 * 3.网络请求
                 * 4.解析Json
                 * 5.结果显示
                 * */
                getLocation(s);
                break;
        }
    }

    private void getLocation(String s) {
        String url = "http://apis.juhe.cn/mobile/get?phone=" + s + "&key=" + StaticClass.LOCATION_ID;
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                //super.onSuccess(t);
                L.i(TAG, t);
                parsingJson(t);
            }
        });
    }

    private void parsingJson(String t) {
        /*{
            "resultcode":"200",
                "reason":"Return Successd!",
                "result":{
            "province":"浙江",
                    "city":"杭州",
                    "areacode":"0571",
                    "zip":"310000",
                    "company":"中国移动",
                    "card":""
        }
        }*/
        try {
            JSONObject jsonObject = new JSONObject(t);
            int resultcode = jsonObject.getInt("resultcode");

            if (resultcode == 200) {
                JSONObject result = jsonObject.getJSONObject("result");
                String province = result.getString("province");
                String city = result.getString("city");
                String areacode = result.getString("areacode");
                String zip = result.getString("zip");
                String company = result.getString("company");
                tv_location_info.setText("归属地:" + province + city + "\n" +
                        "区号:" + areacode + "\n" + "邮编:" + zip + "\n");
                /*tv_location_info.append("归属地:"+province+city+"\n");
                tv_location_info.append("区号:"+areacode+"\n");
                tv_location_info.append("邮编:"+zip+"\n");*/
                switch (company) {
                    case "移动":
                        iv_company.setBackgroundResource(R.drawable.china_mobile);
                        break;
                    case "联通":
                        iv_company.setBackgroundResource(R.drawable.china_unicom);
                        break;
                    case "电信":
                        iv_company.setBackgroundResource(R.drawable.china_telecom);
                        break;
                }
            }else {
                String reason = jsonObject.getString("reason");
                Toast.makeText(this, reason,Toast.LENGTH_SHORT).show();
                tv_location_info.setText(null);
                iv_company.setBackground(null);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("长按DEL全部清除");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("不再提示", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ShareUtil.putBoolean(LocationActivity.this,SHARE_IS_HINT_LOCATION,false);
            }
        });
        builder.show();
    }

    //判断程序是否提示
    private boolean isHint() {
        return ShareUtil.getBoolean(this, SHARE_IS_HINT_LOCATION, true);
    }

    private void setButtonEnabled() {
        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(btn_location_query);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(et_location_phone);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                L.i(TAG, isHasContent + "");
                if (isHasContent) {
                    btn_location_query.setAlpha(1);
                    btn_location_query.setEnabled(true);
                } else {
                    btn_location_query.setAlpha(0.5f);
                    btn_location_query.setEnabled(false);
                }
            }
        });
    }
}
