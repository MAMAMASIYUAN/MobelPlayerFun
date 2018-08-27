package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.iori.mobelplayerfun.R;

public class SystemVideoPlayer extends Activity{
    private VideoView videoView;
    private Uri uri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_video_player);

        videoView = (VideoView) findViewById(R.id.videoView);

        //准备好播放的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());

        //准备好出错了的监听
        videoView.setOnErrorListener(new MyOnErrorListener());

        //准备好完成了的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        //得到播放地址
        uri = getIntent().getData();
        if(uri != null){
            videoView.setVideoURI(uri);
        }

        //设置播放控制面板
        videoView.setMediaController(new MediaController(this));


    }
    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();//Start playing
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Toast.makeText(SystemVideoPlayer.this, "播放出错了！", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Toast.makeText(SystemVideoPlayer.this,"播放完成=" + uri, Toast.LENGTH_SHORT).show();
        }
    }
}
