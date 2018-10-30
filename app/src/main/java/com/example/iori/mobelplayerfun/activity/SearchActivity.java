package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.R;

public class SearchActivity extends Activity {

    private EditText etInput;
    private ImageView ivVoice;
    private TextView tvSearch;
    private ListView listview;
    private ProgressBar progressBar;
    private TextView tvNodata;

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
                    Toast.makeText(SearchActivity.this, "语音输入", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.tv_search://搜索
                    Toast.makeText(SearchActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        findViews();
    }
}
