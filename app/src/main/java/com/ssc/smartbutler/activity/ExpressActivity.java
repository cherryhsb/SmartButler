package com.ssc.smartbutler.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.ssc.smartbutler.R;
import com.ssc.smartbutler.adapter.ExpressAdapter;
import com.ssc.smartbutler.entity.ExpressData;
import com.ssc.smartbutler.utils.IEditTextChangeListener;
import com.ssc.smartbutler.utils.L;
import com.ssc.smartbutler.utils.StaticClass;
import com.ssc.smartbutler.utils.WorksSizeCheckUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/*
 *  项目名：    SmartButler
 *  包名:       SmartButler
 *  文件名：    com.ssc.smartbutler.ui
 *  创建者：    SSC
 *  创建时间：   2018/7/27 21:32
 *  描述：     快递查询
 */

public class ExpressActivity extends BaseActivity {

    private static final String TAG = "ExpressActivity";

    private Spinner spinner_company;

    private EditText et_express_num;

    private Button btn_express;

    private ListView lv_express;

    private List<ExpressData> dataList = new ArrayList<>();

    private String company;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);

        initView();
    }

    private void initView() {
        et_express_num = findViewById(R.id.et_express_num);
        btn_express = findViewById(R.id.btn_express);
        lv_express = findViewById(R.id.lv_express);
        spinner_company = findViewById(R.id.spinner_company);

        spinner_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        company = "sf";
                        break;
                    case 1:
                        company = "sto";
                        break;
                    case 2:
                        company = "yt";
                        break;
                    case 3:
                        company = "yd";
                        break;
                    case 4:
                        company = "tt";
                    case 5:
                        company = "ems";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //根据editText是否为空设置button样式
        //2.判断是否为空
        setButtonEnabled();

        btn_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据去请求数据
                 * 4.解析json
                 * 5.listview适配器
                 * 6.实体类
                 * 7.设置数据/显示效果
                 * */
                //1.获取输入框的内容
                //String company = et_company.getText().toString().trim();
                String number = et_express_num.getText().toString().trim();
                //拼接我们的url
                String url = "http://v.juhe.cn/exp/index?key=" + StaticClass.EXPRESS_ID + "&com=" + company + "&no=" + number;
                //http://v.juhe.cn/exp/index?key=f1a24917ef158c9d6da188cf55dc1b67&com=sf&no=283600908548
                //3.拿到数据去请求数据
                RxVolley.get(url, new HttpCallback() {
                    @Override
                    public void onSuccess(String t) {
                        super.onSuccess(t);
                        //Toast.makeText(ExpressActivity.this, t,Toast.LENGTH_SHORT).show();
                        L.i(TAG, "Json" + t);
                        //4.解析json
                        parsingJson(t);
                    }

                    @Override
                    public void onFailure(int errorNo, String strMsg) {
                        super.onFailure(errorNo, strMsg);
                    }
                });
            }
        });
    }

    //解析json
    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int error_code = jsonObject.getInt("error_code");
            if (error_code == 0) {
                JSONObject jsonResult = jsonObject.getJSONObject("result");
                JSONArray jsonArray = jsonResult.getJSONArray("list");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = (JSONObject) jsonArray.get(i);

                    ExpressData data = new ExpressData();
                    data.setRemark(json.getString("remark"));
                    data.setZone(json.getString("zone"));
                    data.setDatetime(json.getString("datetime"));

                    dataList.add(data);
                }
                //倒叙
                Collections.reverse(dataList);

                ExpressAdapter adapter = new ExpressAdapter(this, dataList);
                lv_express.setAdapter(adapter);
            } else {
                Toast.makeText(ExpressActivity.this, getString(R.string.no_express), Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setButtonEnabled() {
        //1.创建工具类对象 把要改变颜色的tv先传过去
        WorksSizeCheckUtil.textChangeListener textChangeListener = new WorksSizeCheckUtil.textChangeListener(btn_express);

        //2.把所有要监听的edittext都添加进去
        textChangeListener.addAllEditText(et_express_num);

        //3.接口回调 在这里拿到boolean变量 根据isHasContent的值决定 tv 应该设置什么颜色
        WorksSizeCheckUtil.setChangeListener(new IEditTextChangeListener() {
            @Override
            public void textChange(boolean isHasContent) {
                if (isHasContent) {
                    btn_express.setAlpha(1);
                    btn_express.setEnabled(true);
                } else {
                    btn_express.setAlpha(0.5f);
                    btn_express.setEnabled(false);
                }
            }
        });
    }
}
