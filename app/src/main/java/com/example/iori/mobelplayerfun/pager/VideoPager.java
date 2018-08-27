package com.example.iori.mobelplayerfun.pager;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.activity.SystemVideoPlayer;
import com.example.iori.mobelplayerfun.adapter.VideoPagerAdapter;
import com.example.iori.mobelplayerfun.base.BasePager;
import com.example.iori.mobelplayerfun.domain.MediaItem;
import com.example.iori.mobelplayerfun.utils.LogUtil;

import java.util.ArrayList;
public class VideoPager extends BasePager {

    public VideoPager(Context context) {
        super(context);

    }
    private ListView listview;
    private TextView tv_nomedia;
    private ProgressBar pb_loading;
    private ArrayList<MediaItem> mediaItems;
    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mediaItems != null && mediaItems.size() > 0){
                videoPagerAdapter = new VideoPagerAdapter(context, mediaItems);
                listview.setAdapter(videoPagerAdapter);
                tv_nomedia.setVisibility(View.GONE);

            }else {
                tv_nomedia.setVisibility(View.VISIBLE);
            }

            pb_loading.setVisibility(View.GONE);

        }
    };
    private VideoPagerAdapter videoPagerAdapter;
    @Override
    public View initView() {
        LogUtil.e("本地视频页面初始化了");
        View view = View.inflate(context, R.layout.video_pager, null);
        listview = view.findViewById(R.id.listview);
        tv_nomedia = view.findViewById(R.id.tv_nomedia);
        pb_loading = view.findViewById(R.id.pb_loading);

        //设置ListView的item的点击事件
        listview.setOnItemClickListener(new MyOnItemClickListener());
        return view;
    }

    class MyOnItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            MediaItem mediaItem = mediaItems.get(position);
//            Toast.makeText(context, "mediaItem=="+mediaItem.toString(), Toast.LENGTH_SHORT).show();
            //1. 调用系统所有的播放器-隐士意图
//            Intent intent = new Intent();
//            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
//            context.startActivity(intent);
            //2. 调用自己写的播放器-显示意图
            Intent intent = new Intent(context, SystemVideoPlayer.class);
            intent.setDataAndType(Uri.parse(mediaItem.getData()), "video/*");
            context.startActivity(intent);

        }
    }

    @Override
    public void initData() {
        super.initData();
        LogUtil.e("本地视频页面数据初始化了");
        getDataFromLocal();
    }

    private void getDataFromLocal() {

        new Thread(){
            @Override
            public void run() {
                super.run();
                isGrantExternalRW((Activity) context);
//                SystemClock.sleep(2000);
                mediaItems = new ArrayList<>();
                ContentResolver resolver = context.getContentResolver();
                Uri uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Video.Media.DISPLAY_NAME,
                        MediaStore.Video.Media.DURATION,
                        MediaStore.Video.Media.SIZE,
                        MediaStore.Video.Media.DATA,
                        MediaStore.Video.Media.ARTIST
                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if(cursor != null){
                    while(cursor.moveToNext()){
                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);

                        String name = cursor.getString(0);
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);
                        mediaItem.setData(data);

                        String aritst = cursor.getString(4);
                        mediaItem.setArtist(aritst);
                    }
                    cursor.close();
                }

                handler.sendEmptyMessage(10);

            }
        }.start();
    }




    /**
     * 解决安卓6.0以上版本不能读取外部存储权限的问题
     * @param activity
     * @return
     */
    public static boolean isGrantExternalRW(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && activity.checkSelfPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            activity.requestPermissions(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 1);

            return false;
        }

        return true;
    }


}
