package com.example.iori.mobelplayerfun.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

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
        if(basePager != null && !basePager.isInitData){
            basePager.initData();
            basePager.isInitData = true;

        }
        return basePager;
    }

    //按两次返回方可推出程序
//    private boolean isExit = false;
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if(keyCode == KeyEvent.KEYCODE_BACK){
//            if(position != 0){
//                position = 0;
//                rg_bottom_tag.check(R.id.rb_video);
//            }else if(!isExit){
//                isExit = true;
//                Toast.makeText(MainActivity.this, "再按一次", Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        isExit = true;
//                    }
//                }, 2000);
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
