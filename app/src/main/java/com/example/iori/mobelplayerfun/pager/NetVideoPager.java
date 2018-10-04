package com.example.iori.mobelplayerfun.pager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.activity.SystemVideoPlayer;
import com.example.iori.mobelplayerfun.adapter.NetVideoPagerAdapter;
import com.example.iori.mobelplayerfun.adapter.VideoPagerAdapter;
import com.example.iori.mobelplayerfun.base.BasePager;
import com.example.iori.mobelplayerfun.domain.MediaItem;
import com.example.iori.mobelplayerfun.utils.Constants;
import com.example.iori.mobelplayerfun.utils.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

public class NetVideoPager extends BasePager {

    /**
     * 用于装数据的集合
     */
    private ArrayList<MediaItem> mediaItems;
    private NetVideoPagerAdapter adapter;

    @ViewInject(R.id.listview)
    private ListView mListView;

    @ViewInject(R.id.tv_nonet)
    private TextView mTv_nonet;

    @ViewInject(R.id.pb_loading)
    private ProgressBar mPb_loading;

    public NetVideoPager(Context context) {
        super(context);
    }

    @Override
    public View initView() {
        View view = View.inflate(context, R.layout.netvideo_pager, null);
        x.view().inject(this, view);
        //设置ListView的item的点击事件
        mListView.setOnItemClickListener(new NetVideoPager.MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            //3. 传递列表数据-对象-序列化
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
            context.startActivity(intent);
        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("网络视频页面数据初始化了");
        //联网
        //视频内容

        RequestParams requestParams = new RequestParams(Constants.NET_URL);
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                LogUtil.e("Net Connection success" + result);
                processData(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtil.e("Net Connection failed" + ex.getMessage());

            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtil.e("onCancelled" + cex);

            }

            @Override
            public void onFinished() {
                LogUtil.e("On Finished");

            }
        });
    }

    private void processData(String json) {

        mediaItems = parseJson(json);
        if(mediaItems != null && mediaItems.size() > 0){
            adapter = new NetVideoPagerAdapter(context, mediaItems);
            mListView.setAdapter(adapter);
            mTv_nonet.setVisibility(View.GONE);

        }else {
            mTv_nonet.setVisibility(View.VISIBLE);
        }

        mPb_loading.setVisibility(View.GONE);

    }

    private ArrayList<MediaItem> parseJson(String json) {

        ArrayList<MediaItem> mediaItems = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray jsonArray = jsonObject.optJSONArray("trailers");
            if(jsonArray != null && jsonArray.length() > 0){
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObjectItem = (JSONObject) jsonArray.get(i);

                    MediaItem mediaItem = new MediaItem();
                    //数据添加到集合
                    mediaItems.add(mediaItem);
                    String movieName = jsonObjectItem.optString("movieName");
                    mediaItem.setName(movieName);

                    String videoTitle = jsonObjectItem.optString("videoTitle");
                    mediaItem.setDesc(videoTitle);

                    String coverImg = jsonObjectItem.optString("coverImg");
                    mediaItem.setImageUrl(coverImg);

                    String hightUrl = jsonObjectItem.optString("hightUrl");
                    mediaItem.setData(hightUrl);



                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mediaItems;
    }
}
