package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.domain.MediaItem;
import com.example.iori.mobelplayerfun.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemVideoPlayer extends Activity implements View.OnClickListener {
    /**
     * Update video progress
     */
    private static final int PROGRESS = 1;
    private VideoView videoView;
    private Uri uri;
    private LinearLayout llTop;
    private TextView tvName;
    private ImageView ivBattery;
    private TextView tvSystemTime;
    private Button btnVoice;
    private SeekBar seekbarVoice;
    private Button btnSwitchPlayer;
    private LinearLayout llBottom;
    private TextView tvCurrentTime;
    private SeekBar seekbarVideo;
    private TextView tvDuration;
    private Button btnExit;
    private Button btnVideoPre;
    private Button btnVideoStartPause;
    private Button btnVideoNext;
    private Button btnVideoSwitchScreen;

    private Utils utils;
    private MyReceiver receiver;
    /**
     * 传入进来的视频列表
     */
    private ArrayList<MediaItem> mediaItems;
    /**
     * 要播放的列表中的具体位置
     */
    private int position;
    /**
     * 是否网络uri
     */
    private boolean isNetUri;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-09-07 22:25:36 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_system_video_player);
        videoView = (VideoView) findViewById(R.id.videoView);
        llTop = (LinearLayout)findViewById( R.id.ll_top );
        tvName = (TextView)findViewById( R.id.tv_name );
        ivBattery = (ImageView)findViewById( R.id.iv_battery );
        tvSystemTime = (TextView)findViewById( R.id.tv_system_time );
        btnVoice = (Button)findViewById( R.id.btn_voice );
        seekbarVoice = (SeekBar)findViewById( R.id.seekbar_voice );
        btnSwitchPlayer = (Button)findViewById( R.id.btn_switch_player );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvCurrentTime = (TextView)findViewById( R.id.tv_current_time );
        seekbarVideo = (SeekBar)findViewById( R.id.seekbar_video );
        tvDuration = (TextView)findViewById( R.id.tv_duration );
        btnExit = (Button)findViewById( R.id.btn_exit );
        btnVideoPre = (Button)findViewById( R.id.btn_video_pre );
        btnVideoStartPause = (Button)findViewById( R.id.btn_video_start_pause );
        btnVideoNext = (Button)findViewById( R.id.btn_video_next );
        btnVideoSwitchScreen = (Button)findViewById( R.id.btn_video_switch_screen );


        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-09-07 22:25:36 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnVoice ) {
            // Handle clicks for btnVoice
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
            finish();
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
            playPreVideo();
        } else if ( v == btnVideoStartPause ) {
            // Handle clicks for btnVideoStartPause
            if(videoView.isPlaying()){
                //Video is playing, set to pause
                videoView.pause();
                //Set button to play
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_start_selector);
            }else{
                //Video is pause, set to play
                videoView.start();
                //Set button to pause
                btnVideoStartPause.setBackgroundResource(R.drawable.btn_video_pause_selector);

            }
        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if ( v == btnVideoSwitchScreen ) {
            // Handle clicks for btnVideoSwitchScreen
        }
    }

    /**
     * Play pre video
     */
    private void playPreVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            //Play next
            position--;
            if(position >= 0){
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                videoView.setVideoPath(mediaItem.getData());

                //Set Button state
                setButtonState();
            }
        }else if(uri != null){
            //Set Button state
            setButtonState();
        }
    }

    /**
     * Play next video
     */
    private void playNextVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            //Play next
            position++;
            if(position < mediaItems.size()){
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                videoView.setVideoPath(mediaItem.getData());

                //Set Button state
                setButtonState();
            }
        }else if(uri != null){
            //Set Button state
            setButtonState();
        }
    }

    private void setButtonState() {

        if(mediaItems != null && mediaItems.size() > 0){
            if(mediaItems.size() == 1){
                setEnable(false);
            }else if(mediaItems.size() == 2){
                if(position == 0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else if(position == mediaItems.size() - 1){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }
            }else {
                if(position == 0){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
                    btnVideoPre.setEnabled(false);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
                    btnVideoNext.setEnabled(true);
                }else if(position == mediaItems.size() - 1){
                    btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
                    btnVideoPre.setEnabled(true);

                    btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
                    btnVideoNext.setEnabled(false);
                }else {
                    setEnable(true);
                }
            }
        }else if (uri != null){
            //Both button set to dim
            setEnable(false);
        }
    }

    private void setEnable(boolean isEnable) {
        if(isEnable){
            //Set both button enable
            btnVideoPre.setBackgroundResource(R.drawable.btn_video_pre_selector);
            btnVideoPre.setEnabled(true);
            btnVideoNext.setBackgroundResource(R.drawable.btn_video_next_selector);
            btnVideoNext.setEnabled(true);
        }else{
            //Set both button dim
            btnVideoPre.setBackgroundResource(R.drawable.btn_pre_gray);
            btnVideoPre.setEnabled(false);
            btnVideoNext.setBackgroundResource(R.drawable.btn_next_gray);
            btnVideoNext.setEnabled(false);

        }
    }


    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case PROGRESS:
                    //1.Get current progress
                    int currentPosition = videoView.getCurrentPosition();
                    //2. SeekBar.setProgress(current);
                    seekbarVideo.setProgress(currentPosition);
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //Update system time
                    tvSystemTime.setText(getSystemTime());

                    //3. Update the progress every 1 second
                    handler.removeMessages(PROGRESS);
                    handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    break;
            }
        }
    };

    private String getSystemTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

        return format.format(new Date());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        setListener();
        getDate();
        setData();

        //设置播放控制面板
//        videoView.setMediaController(new MediaController(this));


    }

    private void setData() {
        if(mediaItems != null && mediaItems.size() >= 0){
            MediaItem mediaItem = mediaItems.get(position);
            tvName.setText(mediaItem.getName());
//            isNetUri = utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        }else if(uri != null){
            tvName.setText(uri.toString());
            videoView.setVideoURI(uri);
        }else{
            Toast.makeText(SystemVideoPlayer.this, "No data.", Toast.LENGTH_SHORT).show();
        }

        setButtonState();
    }

    private void getDate() {
        //得到播放地址
        uri = getIntent().getData();
        mediaItems = (ArrayList<MediaItem>) getIntent().getSerializableExtra("videolist");
        position = getIntent().getIntExtra("position", 0);

    }

    @Override
    protected void onDestroy() {
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    private void initData() {
        utils = new Utils();
        //Register 广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);
    }

    private void setListener() {
        //准备好播放的监听
        videoView.setOnPreparedListener(new MyOnPreparedListener());

        //准备好出错了的监听
        videoView.setOnErrorListener(new MyOnErrorListener());

        //准备好完成了的监听
        videoView.setOnCompletionListener(new MyOnCompletionListener());

        //设置SeekBar拖动的监听
        seekbarVideo.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
    }

    public void setBattery(int level) {
        if(level <= 0){
            ivBattery.setImageResource(R.drawable.ic_battery_0);
        }else if(level <= 10){
            ivBattery.setImageResource(R.drawable.ic_battery_10);
        }else if(level <= 20){
            ivBattery.setImageResource(R.drawable.ic_battery_20);
        }else if(level <= 40){
            ivBattery.setImageResource(R.drawable.ic_battery_40);
        }else if(level <= 60){
            ivBattery.setImageResource(R.drawable.ic_battery_60);
        }else if(level <= 80){
            ivBattery.setImageResource(R.drawable.ic_battery_80);
        }else if(level <= 100){
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }else {
            ivBattery.setImageResource(R.drawable.ic_battery_100);
        }
    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra("level",0);
            setBattery(level);

        }
    }

    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                videoView.seekTo(progress);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoView.start();//Start playing
            //Get duration
            int duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            tvDuration.setText(utils.stringForTime(duration));
            //Send message
            handler.sendEmptyMessage(PROGRESS);

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
//            Toast.makeText(SystemVideoPlayer.this,"播放完成=" + uri, Toast.LENGTH_SHORT).show();
            playNextVideo();
        }
    }
}
