package com.example.iori.mobelplayerfun.adapter;

import android.content.Context;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.domain.MediaItem;
import com.example.iori.mobelplayerfun.pager.VideoPager;
import com.example.iori.mobelplayerfun.utils.Utils;

import java.util.ArrayList;

public class VideoPagerAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<MediaItem> mediaItems;
    private final Utils utils;
    private ViewHolder viewHolder;

    public VideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
        utils = new Utils();
    }



    @Override
    public int getCount() {
        return mediaItems.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_video_pager, null);
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_time = convertView.findViewById(R.id.tv_time);
            viewHolder.tv_size = convertView.findViewById(R.id.tv_size);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Get data trough position
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_time.setText(Formatter.formatFileSize(context, mediaItem.getSize()));
        viewHolder.tv_size.setText(utils.stringForTime((int) mediaItem.getDuration()));
        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_time;
        TextView tv_size;
    }
}
