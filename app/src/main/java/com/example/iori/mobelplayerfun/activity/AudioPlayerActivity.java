package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.IMusicPlayerService;
import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.service.MusicPlayerService;

public class AudioPlayerActivity extends Activity implements View.OnClickListener {

    private int position;


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


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViews();
        getData();
        bindAndStartService();


    }

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
                    service.openAudio(position);
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
        position = getIntent().getIntExtra("position", 0);
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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
