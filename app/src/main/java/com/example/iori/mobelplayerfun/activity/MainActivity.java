package com.example.iori.mobelplayerfun.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.base.BasePager;
import com.example.iori.mobelplayerfun.base.ReplaceFragment;
import com.example.iori.mobelplayerfun.pager.AudioPager;
import com.example.iori.mobelplayerfun.pager.NetAudioPager;
import com.example.iori.mobelplayerfun.pager.NetVideoPager;
import com.example.iori.mobelplayerfun.pager.VideoPager;

import java.util.ArrayList;

public  class MainActivity extends FragmentActivity{

    private FrameLayout fl_main_content;
    private RadioGroup rg_bottom_tag;

    /**
     * 页面集合
     */
    private ArrayList<BasePager> basePagers;

    /**
     * 选中的位置
     */
    private int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fl_main_content = (FrameLayout) findViewById(R.id.fl_main_content);
        rg_bottom_tag = (RadioGroup) findViewById(R.id.rg_bottom_tag);


        basePagers = new ArrayList<>();
        basePagers.add(new VideoPager(this));// 0
        basePagers.add(new AudioPager(this));// 1
        basePagers.add(new NetVideoPager(this));// 2
        basePagers.add(new NetAudioPager(this));// 3

        /**
         * 设置RedioGroup监听
         */
        rg_bottom_tag.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
        rg_bottom_tag.check(R.id.rb_video);

        }
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

            switch (checkedId){
                default:
                    position = 0;
                    break;
                case R.id.rb_audio:
                    position = 1;
                    break;
                case R.id.rb_net_video:
                    position = 2;
                    break;
                case R.id.rb_net_aduio:
                    position = 3;
                    break;
            }

            setFragment();

        }
    }

    private void setFragment() {
        // 1. 获取FragmentManagement
        FragmentManager manager = getSupportFragmentManager();
        // 2. 开启事务
        FragmentTransaction ft = manager.beginTransaction();
        // 3. 替换
        ft.replace(R.id.fl_main_content, new ReplaceFragment(getBasePager()));
        // 4. 提交事务
        ft.commit();
    }

    /**
     * 根据位置获得相应的页面
     * @return
     */
    private BasePager getBasePager() {
        BasePager basePager = basePagers.get(position);
        if(basePager != null){
            basePager.initData();
        }
        return basePager;
    }

}
