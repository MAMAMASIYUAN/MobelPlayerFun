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
import android.widget.ImageView;

import com.example.iori.mobelplayerfun.IMusicPlayerService;
import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.service.MusicPlayerService;

public class AudioPlayerActivity extends Activity {

    private ImageView iv_icon;
    private int position;
    private IMusicPlayerService service;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findView();
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

    private void findView() {
        setContentView(R.layout.activity_audioplayer);
        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        iv_icon.setBackgroundResource(R.drawable.animation_list);
        AnimationDrawable animationDrawable = (AnimationDrawable) iv_icon.getBackground();
        animationDrawable.start();
    }
}
