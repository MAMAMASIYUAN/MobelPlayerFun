package com.example.iori.mobelplayerfun.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.iori.mobelplayerfun.R;

public class TitleBar extends LinearLayout implements View.OnClickListener {

    private View tv_search;
    private View rl_game;
    private View iv_record;
    private Context context;

    /**
     * 在代码中实例化该类的时候使用这个方法
     * @param context
     */
    public TitleBar(Context context) {
        this(context, null);
    }

    /**
     * 当在布局文件中使用该类的时候，android系统通过这个构造方法实例化该类
     * @param context
     * @param attrs
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 当需要设置样式的时候，可以使用该方法
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        /**
         * 得到孩子的实例
         */
        tv_search = getChildAt(1);
        rl_game = getChildAt(2);
        iv_record =getChildAt(3);

        /**
         * 注册点击时间
         */
        tv_search.setOnClickListener(this);
        rl_game.setOnClickListener(this);
        iv_record.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_search:
                Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rl_game:
                Toast.makeText(context, "Game", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_record:
                Toast.makeText(context, "Record", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
