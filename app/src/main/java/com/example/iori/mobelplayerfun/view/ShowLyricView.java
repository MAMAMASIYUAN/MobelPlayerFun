package com.example.iori.mobelplayerfun.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

@SuppressLint("AppCompatCustomView")
public class ShowLyricView extends TextView {
    private ArrayList<Lyric> lyrics;
    private Paint paint;
    private int width;
    private int height;
    private int index;
    private int heightText = 20;
    private Paint whitepaint;

    public ShowLyricView(Context context) {
        this(context, null);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowLyricView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(lyrics != null && lyrics.size() > 0){
            //绘制歌词,current, past, future
            String currentContent = lyrics.get(index).getContent();
            canvas.drawText(currentContent, width/2, height/2,paint);

            float heightY = height / 2;
            for(int i = index - 1; i >= 0; i--){

                String preContent = lyrics.get(i).getContent();
                heightY = heightY - heightText;
                if(heightY < 0){
                    break;
                }
                canvas.drawText(preContent, width/2, heightY,whitepaint);
            }
            heightY = height / 2;
            for(int i = index; i < lyrics.size(); i++){

                String pastContent = lyrics.get(i).getContent();
                heightY = heightY + heightText;
                if(heightY > height){
                    break;
                }
                canvas.drawText(pastContent, width/2, heightY,whitepaint);
            }




        }else{
            //绘制提示
            canvas.drawText("没有歌词", width/2, height/2,paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }

    private void initView() {


        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(30);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);

        whitepaint = new Paint();
        whitepaint.setColor(Color.WHITE);
        whitepaint.setTextSize(30);
        whitepaint.setAntiAlias(true);
        whitepaint.setTextAlign(Paint.Align.CENTER);

        lyrics = new ArrayList<>();
        for (int i = 0; i < 1000; i++){
            Lyric lyric = new Lyric();
            lyric.setTimePoint(1000 * i);
            lyric.setSleepTime(1500 + i);
            lyric.setContent(i + "aaaaaaaaaaaaaa" + i);
            lyrics.add(lyric);
        }
    }
}
