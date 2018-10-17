package com.example.iori.mobelplayerfun.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.IMusicPlayerService;
import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.activity.AudioPlayerActivity;
import com.example.iori.mobelplayerfun.domain.MediaItem;

import java.io.IOException;
import java.util.ArrayList;
public class MusicPlayerService extends Service {

    public static final String OPENAUTIO = "com.atm.mediaplayer.OPENAUTIO";
    private ArrayList<Object> mediaItems;
    private int position;
    private MediaItem mediaItem;
    private MediaPlayer mediaPlayer;
    private NotificationManager manager;


    @Override
    public void onCreate() {
        super.onCreate();
        getDataFromLocal();
    }

    private void getDataFromLocal() {
        new Thread() {
            @Override
            public void run() {
                super.run();

                mediaItems = new ArrayList<>();
                ContentResolver resolver = getContentResolver();
                Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                String[] objs = {
                        MediaStore.Audio.Media.DISPLAY_NAME,//视频文件在sdcard的名称
                        MediaStore.Audio.Media.DURATION,//视频总时长
                        MediaStore.Audio.Media.SIZE,//视频的文件大小
                        MediaStore.Audio.Media.DATA,//视频的绝对地址
                        MediaStore.Audio.Media.ARTIST,//歌曲的演唱者

                };
                Cursor cursor = resolver.query(uri, objs, null, null, null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {

                        MediaItem mediaItem = new MediaItem();

                        mediaItems.add(mediaItem);//写在上面

                        String name = cursor.getString(0);//视频的名称
                        mediaItem.setName(name);

                        long duration = cursor.getLong(1);//视频的时长
                        mediaItem.setDuration(duration);

                        long size = cursor.getLong(2);//视频的文件大小
                        mediaItem.setSize(size);

                        String data = cursor.getString(3);//视频的播放地址
                        mediaItem.setData(data);

                        String artist = cursor.getString(4);//艺术家
                        mediaItem.setArtist(artist);


                    }

                    cursor.close();

                }

            }
        }.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    IMusicPlayerService.Stub stub = new IMusicPlayerService.Stub() {

        MusicPlayerService service = MusicPlayerService.this;

        @Override
        public void openAudio(int position) throws RemoteException {

            service.openAudio(position);
        }

        @Override
        public void start() throws RemoteException {

            service.start();
        }

        @Override
        public void pause() throws RemoteException {

            service.pause();
        }

        @Override
        public void stop() throws RemoteException {

            service.stop();
        }

        @Override
        public int getCurrentPosition() throws RemoteException {
            return service.getCurrentPosition();
        }

        @Override
        public int getDuration() throws RemoteException {
            return service.getDuration();
        }

        @Override
        public String getArtist() throws RemoteException {
            return service.getArtist();
        }

        @Override
        public String getName() throws RemoteException {
            return service.getName();
        }

        @Override
        public String getAudioPath() throws RemoteException {
            return service.getAudioPath();
        }

        @Override
        public void next() throws RemoteException {

            service.next();
        }

        @Override
        public void pre() throws RemoteException {

            service.pre();
        }

        @Override
        public void setPlayMode(int playmode) throws RemoteException {

            service.setPlayMode(playmode);
        }

        @Override
        public int getPlayMode() throws RemoteException {
            return service.getPlayMode();
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return service.isPlaying();
        }

        @Override
        public void seekTo(int position) throws RemoteException {
            mediaPlayer.seekTo(position);
        }
    };

    /**
     * 根据位置打开对应的音频文件
     * @param position
     */
    private void openAudio(int position){

        this.position = position;
        if(mediaItems != null && mediaItems.size() > 0){
            mediaItem = (MediaItem) mediaItems.get(position);
            if(mediaPlayer != null){
//                mediaPlayer.release();
                mediaPlayer.reset();
            }

            try {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setOnPreparedListener(new MyOnPreparedListener());
                mediaPlayer.setOnCompletionListener(new MyOnCompletionListener());
                mediaPlayer.setOnErrorListener(new MyOnErrorListener());
                mediaPlayer.setDataSource(mediaItem.getData());
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(MusicPlayerService.this,"还没有数据", Toast.LENGTH_SHORT).show();
        }


    }

    class MyOnErrorListener implements MediaPlayer.OnErrorListener{

        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            next();
            return true;
        }
    }

    class MyOnPreparedListener implements MediaPlayer.OnPreparedListener{

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onPrepared(MediaPlayer mp) {

            notifyChange(OPENAUTIO);
            start();
        }
    }

    private void notifyChange(String action) {
        Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    class MyOnCompletionListener implements MediaPlayer.OnCompletionListener{

        @Override
        public void onCompletion(MediaPlayer mp) {
            next();
        }
    }


    /**
     * 播放音乐
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void start(){

        mediaPlayer.start();
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, AudioPlayerActivity.class);
        intent.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.notification_music_playing)
                .setContentTitle("Media Player")
                .setContentText("正在播放：" + getName())
                .setContentIntent(pendingIntent)
                .build();
        manager.notify(1,notification);
    }

    /**
     * 播暂停音乐
     */
    private void pause(){

        mediaPlayer.pause();
        manager.cancel(1);
    }

    /**
     * 停止
     */
    private void stop(){

    }

    /**
     * 得到当前的播放进度
     * @return
     */
    private int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }


    /**
     * 得到当前音频的总时长
     * @return
     */
    private int getDuration(){
        return (int) mediaItem.getDuration();
    }

    /**
     * 得到艺术家
     * @return
     */
    private String getArtist(){
        return mediaItem.getArtist();
    }

    /**
     * 得到歌曲名字
     * @return
     */
    private String getName(){
        return mediaItem.getName();
    }


    /**
     * 得到歌曲播放的路径
     * @return
     */
    private String getAudioPath(){
        return "";
    }

    /**
     * 播放下一个视频
     */
    private void next(){

    }


    /**
     * 播放上一个视频
     */
    private void pre(){

    }

    /**
     * 设置播放模式
     * @param playmode
     */
    private void setPlayMode(int playmode){

    }

    /**
     * 得到播放模式
     * @return
     */
    private int getPlayMode(){
        return 0;
    }

    /**
     * 得到是否播放
     * @return
     */
    private boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }



}
