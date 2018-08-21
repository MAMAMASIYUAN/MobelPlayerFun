package com.example.iori.mobelplayerfun.pager;

import android.content.Context;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.base.BasePager;
import com.example.iori.mobelplayerfun.utils.LogUtil;

import org.w3c.dom.Text;

import java.util.List;

public class VideoPager extends BasePager {

    public VideoPager(Context context) {
        super(context);
    }

    private ListView listview;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    @Override
    public View initView() {
        LogUtil.e("本地视频页面初始化了");
        View view = View.inflate(context, R.layout.video_pager, null);
        listview = view.findViewById(R.id.listview);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);
        return view;
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频页面数据初始化了");

    }
}
