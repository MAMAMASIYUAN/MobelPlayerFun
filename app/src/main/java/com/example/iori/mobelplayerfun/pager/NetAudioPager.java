package com.example.iori.mobelplayerfun.pager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.adapter.NetAudioPagerAdapter;
import com.example.iori.mobelplayerfun.base.BasePager;
import com.example.iori.mobelplayerfun.domain.NetAudioPagerData;
import com.example.iori.mobelplayerfun.utils.CacheUtils;
import com.example.iori.mobelplayerfun.utils.Constants;
import com.example.iori.mobelplayerfun.utils.LogUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

public class NetAudioPager extends BasePager {

    @ViewInject(R.id.listview)
    private ListView mListView;


    @ViewInject(R.id.tv_nonet)
    private TextView tv_nonet;


    @ViewInject(R.id.pb_loading)
    private ProgressBar pb_loading;

    private List<NetAudioPagerData.ListBean> datas;
    private NetAudioPagerAdapter adapter;

    public NetAudioPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netaudio_pager,null);
        x.view().inject(NetAudioPager.this,view);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("网络音频页面数据初始化了");
        String saveJson = CacheUtils.getString(context, Constants.ALL_RES_URL);
        if(!TextUtils.isEmpty(saveJson)){
            //解析数据
            processData(saveJson);
        }
        getDataFromNet();
    }

   
    private void getDataFromNet() {

        RequestParams params = new RequestParams(Constants.ALL_RES_URL);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("请求数据成功： " + result);
                CacheUtils.putString(context, Constants.ALL_RES_URL,result);
                processData(result);

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

                LogUtil.e("请求数据失败： " + ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled： " + cex.getMessage());

            }

            @Override
            public void onFinished() {
                LogUtil.e("onFinished： ");

            }
        });
    }

    /**
     * 解析json数据并显示数据
     * @param json
     */
    private void processData(String json) {
        NetAudioPagerData data = parsedJson(json);
        datas = data.getList();
        if(datas != null && datas.size() > 0){
            //有数据
            tv_nonet.setVisibility(View.GONE);
            //设置适配器
            adapter = new NetAudioPagerAdapter(context, datas);
            mListView.setAdapter(adapter);
        }else {
            //没数据
            tv_nonet.setText("没有对应的数据...");
            tv_nonet.setVisibility(View.VISIBLE);

        }

        pb_loading.setVisibility(View.GONE);
    }

    /**
     * Gson 解析数据
     * @param json
     * @return
     */
    private NetAudioPagerData parsedJson(String json) {

        return new Gson().fromJson(json, NetAudioPagerData.class);
    }
}
