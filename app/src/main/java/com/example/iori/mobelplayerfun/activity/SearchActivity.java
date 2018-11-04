package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.adapter.SearchAdapter;
import com.example.iori.mobelplayerfun.domain.SearchBean;
import com.example.iori.mobelplayerfun.utils.Constants;
import com.example.iori.mobelplayerfun.utils.JsonParser;
import com.google.gson.Gson;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.show.api.ShowApiRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

public class SearchActivity extends Activity {

    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    private EditText etInput;
    private ImageView ivVoice;
    private TextView tvSearch;
    private ListView listview;
    private ProgressBar progressBar;
    private TextView tvNodata;
    private String url;
    private List<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> items;
    private SearchAdapter adapter;
    private String res;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-10-30 21:51:27 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        etInput = (EditText)findViewById( R.id.et_input );
        ivVoice = (ImageView)findViewById( R.id.iv_voice );
        tvSearch = (TextView)findViewById( R.id.tv_search );
        listview = (ListView)findViewById( R.id.listview );
        progressBar = (ProgressBar)findViewById( R.id.progressBar );
        tvNodata = (TextView)findViewById( R.id.tv_nodata );

        MyOnClickListener myOnClickListener = new MyOnClickListener();
        ivVoice.setOnClickListener(myOnClickListener);
        tvSearch.setOnClickListener(myOnClickListener);
    }

    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_voice://语音输入

                    showDialog();
//                    Toast.makeText(SearchActivity.this, "语音输入", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_search://搜索
                    searchText();
//                    Toast.makeText(SearchActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void searchText() {
        String text = etInput.getText().toString().trim();
        if(!TextUtils.isEmpty(text)){
            if(items != null && items.size() >0){
                items.clear();
            }

            url = text;
            getDataFromNet();
        }
    }

    private void getDataFromNet() {

        progressBar.setVisibility(View.VISIBLE);
        new Thread(){
            //在新线程中发送网络请求
            public void run() {
                String appid="79216";//要替换成自己的
                String secret="0b4e4cf157464dc3bd802f17f72b2f2f";//要替换成自己的
                 res = new ShowApiRequest(Constants.SEARCH_URL, appid, secret)
                        .addTextPara("channelId", "")
                        .addTextPara("channelName", "")
                        .addTextPara("title", url)
                        .addTextPara("page", "1")
                        .addTextPara("needContent", "0")
                        .addTextPara("needHtml", "0")
                        .addTextPara("needAllList", "0")
                        .addTextPara("maxResult", "20")
                        .addTextPara("id", "")
                        .post();
                System.out.println(res);

            }
        }.start();

        progressBar.setVisibility(View.GONE);
        if(res != null){
            processData(res);
        }


//        RequestParams parms = new RequestParams(url);
//        x.http().get(parms, new Callback.CommonCallback<String>() {
//            @Override
//            public void onSuccess(String result) {
//
//                processData(result);
//            }
//
//            @Override
//            public void onError(Throwable ex, boolean isOnCallback) {
//
//                progressBar.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onCancelled(CancelledException cex) {
//
//            }
//
//            @Override
//            public void onFinished() {
//
//                progressBar.setVisibility(View.GONE);
//            }
//        });
    }

    private void processData(String res) {
        SearchBean searchBean = parsedJson(res);
        items = searchBean.getShowapi_res_body().getPagebean().getContentlist();
        showData();
    }

    private void showData() {
        if(items != null && items.size() > 0){
            //设置适配器
            adapter = new SearchAdapter(this, items);
            listview.setAdapter(adapter);
            tvNodata.setVisibility(View.GONE);
        }else {
            tvNodata.setVisibility(View.VISIBLE);
        }

        progressBar.setVisibility(View.GONE);
    }

    private SearchBean parsedJson(String result) {

        return new Gson().fromJson(result, SearchBean.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();
    }




    private void showDialog() {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(this, new MyInitListener());
        //2.设置accent、 language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解
        //结果
        // mDialog.setParameter("asr_sch", "1");
        // mDialog.setParameter("nlp_version", "2.0");
        //3.设置回调接口
        mDialog.setListener(new MyRecognizerDialogListener());
        //4.显示dialog，接收语音输入
        mDialog.show();
    }

    class MyRecognizerDialogListener implements RecognizerDialogListener {

        /**
         * @param recognizerResult
         * @param b                是否说话结束
         */
        @Override
        public void onResult(RecognizerResult recognizerResult, boolean b) {
            String result = recognizerResult.getResultString();
            Log.e("MainActivity", "result ==" + result);
            String text = JsonParser.parseIatResult(result);
            //解析好的
            Log.e("MainActivity", "text ==" + text);

            String sn = null;
            // 读取json结果中的sn字段
            try {
                JSONObject resultJson = new JSONObject(recognizerResult.getResultString());
                sn = resultJson.optString("sn");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mIatResults.put(sn, text);

            StringBuffer resultBuffer = new StringBuffer();//拼成一句
            for (String key : mIatResults.keySet()) {
                resultBuffer.append(mIatResults.get(key));
            }

            etInput.setText(resultBuffer.toString());
            etInput.setSelection(etInput.length());

        }

        /**
         * 出错了
         *
         * @param speechError
         */
        @Override
        public void onError(SpeechError speechError) {
            Log.e("MainActivity", "onError ==" + speechError.getMessage());

        }
    }


    class MyInitListener implements InitListener {

        @Override
        public void onInit(int i) {
            if (i != ErrorCode.SUCCESS) {
                Toast.makeText(SearchActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
