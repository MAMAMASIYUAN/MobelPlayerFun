package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.domain.MediaItem;
import com.example.iori.mobelplayerfun.utils.Utils;
import com.example.iori.mobelplayerfun.view.VideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SystemVideoPlayer extends Activity implements View.OnClickListener {


    /**
     * Is using the system to listen buffering
     */
    private boolean isUseSystem = true;

    /**
     * Update video progress
     */
    private static final int PROGRESS = 1;
    private static final int HIDE_MEDIACONTROLLER = 2;
    private static final int SHOW_SPEED = 3;
    /**
     * Full screen
     */
    private static final int FULL_SCREEN = 1;
    /**
     * Default screen
     */
    private static final int DEFAULT_SCREEN = 2;
    private int screenWidth = 0;//Width of the screen
    private int screenHeight = 0;//Height of the screen
    private boolean isFullScreen = false;
    private int videoWidth = 0;//Width of the video
    private int videoHeight = 0;//Height of the video
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
    private RelativeLayout media_controller;
    private TextView tv_buffer_netspeed;
    private LinearLayout ll_buffer;
    private TextView tv_loading_netspeed;
    private LinearLayout ll_loading;

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

    /**
     * 定义收拾识别器
     */
    private GestureDetector detector;
    private boolean isShowMediaController;

    /**
     * 调用声音
     */
    private AudioManager am;

    /**
     * Current voice
     */
    private int currentVoice;

    /**
     * Max voice
     */
    private int maxVoice;

    /**
     * Is mute or not
     */
    private boolean isMute = false;

    /**
     * last playing position
     */
    private int preCurrentPosition;


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
        media_controller = (RelativeLayout) findViewById(R.id.media_controller);
        tv_buffer_netspeed = (TextView) findViewById(R.id.tv_buffer_netspeed);
        ll_buffer = (LinearLayout) findViewById(R.id.ll_buffer);
        ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
        tv_loading_netspeed = (TextView) findViewById(R.id.tv_loading_netspeed);


        btnVoice.setOnClickListener( this );
        btnSwitchPlayer.setOnClickListener( this );
        btnExit.setOnClickListener( this );
        btnVideoPre.setOnClickListener( this );
        btnVideoStartPause.setOnClickListener( this );
        btnVideoNext.setOnClickListener( this );
        btnVideoSwitchScreen.setOnClickListener( this );

        seekbarVoice.setMax(maxVoice);//关联最大音量
        seekbarVoice.setProgress(currentVoice);//设置当前音量

        handler.sendEmptyMessage(SHOW_SPEED);
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
            isMute = !isMute;
            updateVoice(currentVoice, isMute);
        } else if ( v == btnSwitchPlayer ) {
            // Handle clicks for btnSwitchPlayer
            switchVitamioPlayer();
        } else if ( v == btnExit ) {
            // Handle clicks for btnExit
            finish();
        } else if ( v == btnVideoPre ) {
            // Handle clicks for btnVideoPre
            playPreVideo();
        } else if ( v == btnVideoStartPause ) {
            startAndPause();

        } else if ( v == btnVideoNext ) {
            // Handle clicks for btnVideoNext
            playNextVideo();
        } else if ( v == btnVideoSwitchScreen ) {
            // Handle clicks for btnVideoSwitchScreen
            setFullAndDefaultScreen();
        }
        handler.removeMessages(HIDE_MEDIACONTROLLER);
        handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
    }

    private void switchVitamioPlayer() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当你发现视频只有声音没有画面时，请使用万能播放器！");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startVitamioPlayer();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }

    private void startAndPause() {
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
    }

    /**
     * Play pre video
     */
    private void playPreVideo() {
        if(mediaItems != null && mediaItems.size() > 0){
            //Play next
            position--;
            if(position >= 0){
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
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
                ll_loading.setVisibility(View.VISIBLE);
                MediaItem mediaItem = mediaItems.get(position);
                tvName.setText(mediaItem.getName());
                isNetUri = utils.isNetUri(mediaItem.getData());
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
                case SHOW_SPEED:
                    //Get net speed
                    String netSpeed = utils.getNetSpeed(SystemVideoPlayer.this);
                    //Display net spped
                    tv_loading_netspeed.setText("Loading..." + netSpeed);
                    tv_buffer_netspeed.setText("Buffering..." + netSpeed);
                    //Update the progress every 2 second
                    handler.removeMessages(SHOW_SPEED);
                    handler.sendEmptyMessageDelayed(SHOW_SPEED, 2000);
                    break;
                case HIDE_MEDIACONTROLLER:
                    hideMediaController();
                    break;
                case PROGRESS:
                    //1.Get current progress
                    int currentPosition = videoView.getCurrentPosition();
                    //2. SeekBar.setProgress(current);
                    seekbarVideo.setProgress(currentPosition);
                    tvCurrentTime.setText(utils.stringForTime(currentPosition));

                    //Update system time
                    tvSystemTime.setText(getSystemTime());

                    //Update net video buffer
                    if(isNetUri){
                        int buffer = videoView.getBufferPercentage();
                        int totalBuffer = buffer * seekbarVideo.getMax();
                        int secondaryProgress = totalBuffer / 100;
                        seekbarVideo.setSecondaryProgress(secondaryProgress);
                    }else {
                        seekbarVideo.setSecondaryProgress(0);
                    }

                    //Listen net video buffering
                    if(!isUseSystem){
                        int buffer = currentPosition - preCurrentPosition;

                        if(videoView.isPlaying()){
                            if (buffer < 500){
                                ll_buffer.setVisibility(View.VISIBLE);
                            }else{
                                ll_buffer.setVisibility(View.GONE);
                            }
                        }else{
                            ll_buffer.setVisibility(View.GONE);
                        }

                    }
                    preCurrentPosition = currentPosition;

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
            isNetUri = utils.isNetUri(mediaItem.getData());
            videoView.setVideoPath(mediaItem.getData());
        }else if(uri != null){
            isNetUri = utils.isNetUri(uri.toString());
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
        /**
         * Remove all message
         *
         */
        handler.removeCallbacksAndMessages(null);
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }
        super.onDestroy();
    }

    private float startY;
    private float endY;
    private float startX;
    private float endX;
    private float touchRang;// Height of the screen
    private int mVol;//Voice when pressing

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        detector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startY = event.getY();
                startX = event.getX();
                mVol = am.getStreamVolume(AudioManager.STREAM_MUSIC);
                touchRang = Math.max(screenHeight, screenWidth);
                handler.removeMessages(HIDE_MEDIACONTROLLER);
                break;
            case MotionEvent.ACTION_MOVE:
                endY = event.getY();
                endX = event.getX();
                float distanceY = startY - endY;
                float delta = (distanceY / touchRang) * maxVoice;

                if(endX < screenWidth / 2){
                    //左侧，调节亮度
                    final double FLING_MIN_DISTANCE = 0.5;
                    final double FLING_MIN_VELOCITY = 0.5;
                    if (distanceY > FLING_MIN_DISTANCE
                            && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                        setBrightness(20);
                    }
                    if(distanceY < FLING_MIN_DISTANCE && Math.abs(distanceY) > FLING_MIN_VELOCITY){

                        setBrightness(-20);
                    }

                }else {
                    //右侧，调节声音
                    int voice = (int) Math.min(Math.max(mVol + delta,0),maxVoice);
                    if(delta != 0){
                        isMute = false;
                        updateVoice(voice, isMute);
                    }
                }


                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 50000);
                break;
        }
        return super.onTouchEvent(event);
    }

    private Vibrator vibrator;
    /*
     *
     * 设置屏幕亮度 lp = 0 全暗 ，lp= -1,根据系统设置， lp = 1; 最亮
     */
    private void setBrightness(float brightness) {

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // if (lp.screenBrightness <= 0.1) {
        // return;
        // }
        lp.screenBrightness = lp.screenBrightness + brightness / 255.0f;
        if (lp.screenBrightness > 1) {
            lp.screenBrightness = 1;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        } else if (lp.screenBrightness < 0.2) {
            lp.screenBrightness = (float) 0.2;
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            long[] pattern = { 10, 200 }; // OFF/ON/OFF/ON...
            vibrator.vibrate(pattern, -1);
        }
//        Log.e(TAG, "lp.screenBrightness= " + lp.screenBrightness);
        getWindow().setAttributes(lp);
    }

    private void initData() {
        utils = new Utils();
        //Register 广播
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(receiver,intentFilter);

        detector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
//                Toast.makeText(SystemVideoPlayer.this, "Long press", Toast.LENGTH_SHORT).show();
                startAndPause();
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayer.this, "Double click", Toast.LENGTH_SHORT).show();
                setFullAndDefaultScreen();

                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
//                Toast.makeText(SystemVideoPlayer.this, "Single click", Toast.LENGTH_SHORT).show();
                if(isShowMediaController){
                    //Hide the controller
                    hideMediaController();
                    handler.sendEmptyMessage(HIDE_MEDIACONTROLLER);
                }else{
                    //Show the controller
                    showMediaController();
                    handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
                }
                return super.onSingleTapConfirmed(e);
            }
        });

        //Get the width and height of the screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        //Get current voice and max voice
        am = (AudioManager) getSystemService(AUDIO_SERVICE);
        currentVoice = am.getStreamVolume(AudioManager.STREAM_MUSIC);
        maxVoice = am.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

    }

    private void setFullAndDefaultScreen() {
        if(isFullScreen){
            //Default
            setVideoType(DEFAULT_SCREEN);
        }else {
            //Full
            setVideoType(FULL_SCREEN);
        }
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

        //Set voice SeekBar listener
        seekbarVoice.setOnSeekBarChangeListener(new VoiceOnSeekBarChangeListener());

        //Set web buffering listener
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            videoView.setOnInfoListener(new MyOnInfoListener());
        }
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

    class MyOnInfoListener implements MediaPlayer.OnInfoListener {

        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            switch (what){
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
                    ll_buffer.setVisibility(View.VISIBLE);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
                    ll_buffer.setVisibility(View.GONE);
                    break;
            }
            return false;
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

            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);
        }
    }

    class VoiceOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            if(fromUser){
                if(progress > 0){
                    isMute = false;
                }else {
                    isMute = true;
                }
            }
            updateVoice(progress, isMute);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

            handler.removeMessages(HIDE_MEDIACONTROLLER);
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 5000);

        }
    }

    private void updateVoice(int progress, boolean isMute) {

        if(isMute){
            am.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
            seekbarVoice.setProgress(0);

        }else{
            am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            seekbarVoice.setProgress(progress);
            currentVoice = progress;
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            videoWidth = mediaPlayer.getVideoWidth();
            videoHeight = mediaPlayer.getVideoHeight();
            videoView.start();//Start playing
            //Get duration
            int duration = videoView.getDuration();
            seekbarVideo.setMax(duration);
            tvDuration.setText(utils.stringForTime(duration));
            hideMediaController();
            //Send message
            handler.sendEmptyMessage(PROGRESS);

//            videoView.setVideoSise(200, 200);

            setVideoType(DEFAULT_SCREEN);

//            mediaPlayer.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {
//                @Override
//                public void onSeekComplete(MediaPlayer mp) {
//                    Toast.makeText(SystemVideoPlayer.this, "Seek done.", Toast.LENGTH_SHORT).show();
//                }
//            });

            //Close the loading page
            ll_loading.setVisibility(View.GONE);
        }
    }

    private void setVideoType(int defaultScreen) {
        switch (defaultScreen){
            case FULL_SCREEN:
                //1. Set video size - same as the screen
                videoView.setVideoSise(screenWidth, screenHeight);

                //2. Set state of the button - default
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_default_selector);
                isFullScreen = true;
                break;
            case DEFAULT_SCREEN:
                //1. Set video size - default size
                int mVideoWidth = videoWidth;
                int mVideoHeight = videoHeight;
                int width = screenWidth;//Width of the screen
                int height = screenHeight;//Height of the screen
                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight;
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth;
                }
                videoView.setVideoSise(width, height);

                //2. Set state of the button - full
                btnVideoSwitchScreen.setBackgroundResource(R.drawable.btn_video_switch_screen_full_selector);
                isFullScreen = false;
                break;
        }
    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener {

        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//            Toast.makeText(SystemVideoPlayer.this, "播放出错了！", Toast.LENGTH_SHORT).show();
            //1. 如果视频格式不支持，跳转万能播放器
            startVitamioPlayer();
            //2. 播放网络视频时，如果网络中断。 1，判断是否真的断了，然后提示。2，网络不稳定，重新播放
            //3. 本地文件中间有空白，下载完成
            return true;
        }
    }

    private void startVitamioPlayer() {

        if(videoView != null){
            videoView.stopPlayback();
        }

        Intent intent = new Intent(this, VitamioVideoPlayer.class);
        if(mediaItems != null && mediaItems.size() > 0){
            Bundle bundle = new Bundle();
            bundle.putSerializable("videolist", mediaItems);
            intent.putExtras(bundle);
            intent.putExtra("position", position);
        }else if(uri != null){
            intent.setData(uri);
        }
        startActivity(intent);
        finish();

    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
//            Toast.makeText(SystemVideoPlayer.this,"播放完成=" + uri, Toast.LENGTH_SHORT).show();
            playNextVideo();
        }
    }


    /**
     * Show Media Controller
     */
    private void showMediaController(){
        media_controller.setVisibility(View.VISIBLE);
        isShowMediaController = true;
    }

    /**
     * Hide Media Controller
     */
    private void hideMediaController(){
        media_controller.setVisibility(View.GONE);
        isShowMediaController = false;
    }

    /**
     *
     * 监听物理键，实现音量控制
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            currentVoice --;
            updateVoice(currentVoice, false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 50000);
            return true;
        }else if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            currentVoice ++;
            updateVoice(currentVoice, false);
            handler.removeMessages(HIDE_MEDIACONTROLLER);
            handler.sendEmptyMessageDelayed(HIDE_MEDIACONTROLLER, 50000);
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }
}
