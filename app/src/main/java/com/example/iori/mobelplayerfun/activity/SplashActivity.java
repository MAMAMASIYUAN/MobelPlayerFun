package com.example.iori.mobelplayerfun.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.example.iori.mobelplayerfun.R;

public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getSimpleName();
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Execute after 2 seconds.
                //Executed in main process
                startMainActivity();
                Log.e(TAG,"Current process name ==" + Thread.currentThread().getName());
            }
        }, 2000);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "onTouchEvent==" + event.getAction());
        startMainActivity();
        return super.onTouchEvent(event);
    }
    private boolean isStartMain = false;
    //Start the MainActivity and then close the current activity
    private void startMainActivity() {
        if(!isStartMain){
            isStartMain = true;
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }
}
