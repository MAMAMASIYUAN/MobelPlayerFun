package com.example.iori.mobelplayerfun.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.base.BasePager;
import com.example.iori.mobelplayerfun.utils.Constants;
import com.example.iori.mobelplayerfun.utils.LogUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class NetVideoPager extends BasePager {

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
        return view;
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
}
