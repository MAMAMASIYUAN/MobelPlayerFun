package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.IMusicPlayerService;
import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.service.MusicPlayerService;
import com.example.iori.mobelplayerfun.utils.Utils;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {

    private static final int PROGRESS = 1;
    private int position;
    /**
     * 1. True, 来自于状态栏
     * 2. Faults，来自于列表
     */
    private boolean notification;

    private ImageView ivIcon;
    private TextView tvArtist;
    private TextView tvName;
    private LinearLayout llBottom;
    private TextView tvTime;
    private SeekBar seekbarAudio;
    private Button btnAudioPlaymode;
    private Button btnAudioPre;
    private Button btnAudioStartPause;
    private Button btnAudioNext;
    private Button btnLyrc;

    private IMusicPlayerService service;
    private MyReceiver receiver;
    private Utils utils;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        findViews();
        getData();
        bindAndStartService();


    }

    private void initData() {

        utils = new Utils();
        receiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MusicPlayerService.OPENAUTIO);
        registerReceiver(receiver, intentFilter);

    }

    class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            showViewData();
            checkPlayMode();
        }
    }

    private void showViewData() {
        try {
            tvArtist.setText(service.getArtist());
            tvName.setText(service.getName());
            seekbarAudio.setMax(service.getDuration());
            //发送消息
            handler.sendEmptyMessage(PROGRESS);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case PROGRESS:
                    try {
                        int currentPosition = service.getCurrentPosition();
                        seekbarAudio.setProgress(currentPosition);
                        tvTime.setText(utils.stringForTime(service.getCurrentPosition()) + "/" + utils.stringForTime(service.getDuration()));
                        handler.removeMessages(PROGRESS);
                        handler.sendEmptyMessageDelayed(PROGRESS, 1000);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private ServiceConnection con = new ServiceConnection() {

        /**
         * When the connection successful
         * @param name
         * @param iBinder
         */
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            service = IMusicPlayerService.Stub.asInterface(iBinder);

            if(service != null){
                try {
                    if(!notification){
                        service.openAudio(position);
                    }else{
                        showViewData();
                    }

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * When the connection break down
         * @param name
         */
        @Override
        public void onServiceDisconnected(ComponentName name) {

            if(service != null){
                try {
                    service.stop();
                    service = null;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

        }
    };

    private void bindAndStartService() {
        Intent intent = new Intent(this, MusicPlayerService.class);
        intent.setAction("com.atma.mobileplayer_OPENAUDIO");
        bindService(intent, con, Context.BIND_AUTO_CREATE);
        startService(intent);
    }

    private void getData() {
        notification = getIntent().getBooleanExtra("notification", false);
        if(!notification){
            position = getIntent().getIntExtra("position", 0);
        }

    }

     /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2018-10-11 23:25:13 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        setContentView(R.layout.activity_audioplayer);
        ivIcon = (ImageView) findViewById(R.id.iv_icon);
        ivIcon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) ivIcon.getBackground();
        animationDrawable.start();


        tvArtist = (TextView)findViewById( R.id.tv_artist );
        tvName = (TextView)findViewById( R.id.tv_name );
        llBottom = (LinearLayout)findViewById( R.id.ll_bottom );
        tvTime = (TextView)findViewById( R.id.tv_time );
        seekbarAudio = (SeekBar)findViewById( R.id.seekbar_audio );
        btnAudioPlaymode = (Button)findViewById( R.id.btn_audio_playmode );
        btnAudioPre = (Button)findViewById( R.id.btn_audio_pre );
        btnAudioStartPause = (Button)findViewById( R.id.btn_audio_start_pause );
        btnAudioNext = (Button)findViewById( R.id.btn_audio_next );
        btnLyrc = (Button)findViewById( R.id.btn_lyrc );

        btnAudioPlaymode.setOnClickListener( this );
        btnAudioPre.setOnClickListener( this );
        btnAudioStartPause.setOnClickListener( this );
        btnAudioNext.setOnClickListener( this );
        btnLyrc.setOnClickListener( this );

        seekbarAudio.setOnSeekBarChangeListener(new MyOnSeekBarChangeListener());
    }

    class MyOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if(fromUser){
                try {
                    service.seekTo(progress);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2018-10-11 23:25:13 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btnAudioPlaymode ) {
            // Handle clicks for btnAudioPlaymode
            setPlayMode();

        } else if ( v == btnAudioPre ) {
            // Handle clicks for btnAudioPre
        } else if ( v == btnAudioStartPause ) {
            // Handle clicks for btnAudioStartPause
            if(service != null){
                try {
                    if(service.isPlaying()){
                        service.pause();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_start_selector);
                    }else {
                        service.start();
                        btnAudioStartPause.setBackgroundResource(R.drawable.btn_audio_pause_selector);
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        } else if ( v == btnAudioNext ) {
            // Handle clicks for btnAudioNext
        } else if ( v == btnLyrc ) {
            // Handle clicks for btnLyrc
        }
    }

    private void setPlayMode() {

        try {
            int playMode = service.getPlayMode();
            if(playMode == MusicPlayerService.REPREAT_NORMAL){
                playMode = MusicPlayerService.REPREAT_SINGLE;
            }else if(playMode == MusicPlayerService.REPREAT_SINGLE){
                playMode = MusicPlayerService.REPREAT_ALL;
            }else if(playMode == MusicPlayerService.REPREAT_ALL){
                playMode = MusicPlayerService.REPREAT_NORMAL;
            }else {
                playMode = MusicPlayerService.REPREAT_NORMAL;
            }

            service.setPlayMode(playMode);
            showPlayMode();
        } catch (RemoteException e) {
            e.printStackTrace();
        }


    }

    private void showPlayMode() {

        try {
            int playMode = service.getPlayMode();
            if(playMode == MusicPlayerService.REPREAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }else if(playMode == MusicPlayerService.REPREAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
                Toast.makeText(AudioPlayerActivity.this, "单曲循环", Toast.LENGTH_SHORT).show();
            }else if(playMode == MusicPlayerService.REPREAT_ALL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
                Toast.makeText(AudioPlayerActivity.this, "全部循环", Toast.LENGTH_SHORT).show();
            }else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
                Toast.makeText(AudioPlayerActivity.this, "顺序播放", Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    private void checkPlayMode() {

        try {
            int playMode = service.getPlayMode();
            if(playMode == MusicPlayerService.REPREAT_NORMAL){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }else if(playMode == MusicPlayerService.REPREAT_SINGLE){
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_single_selector);
            }else if(playMode == MusicPlayerService.REPREAT_ALL) {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_all_selector);
            }else {
                btnAudioPlaymode.setBackgroundResource(R.drawable.btn_audio_playmode_normal_selector);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        handler.removeCallbacksAndMessages(null);
        if(receiver != null){
            unregisterReceiver(receiver);
            receiver = null;
        }

        if(con != null){
            unbindService(con);
            con = null;
        }
    }
}
