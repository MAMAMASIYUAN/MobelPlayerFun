package com.example.iori.mobelplayerfun.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.domain.MediaItem;
import com.squareup.picasso.Picasso;

import org.xutils.x;

import java.util.ArrayList;

public class NetVideoPagerAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<MediaItem> mediaItems;
    private ViewHolder viewHolder;

    public NetVideoPagerAdapter(Context context, ArrayList<MediaItem> mediaItems){
        this.context = context;
        this.mediaItems = mediaItems;
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
            convertView = View.inflate(context, R.layout.item_netvideo_pager, null);
            viewHolder.iv_icon = convertView.findViewById(R.id.iv_icon);
            viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
            viewHolder.tv_desc = convertView.findViewById(R.id.tv_desc);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        //Get data trough position
        MediaItem mediaItem = mediaItems.get(position);
        viewHolder.tv_name.setText(mediaItem.getName());
        viewHolder.tv_desc.setText(mediaItem.getDesc());
        //Use xUtils3
//        x.image().bind(viewHolder.iv_icon, mediaItem.getImageUrl());
        //Use Glide
//        Glide.with(context)
//                .load(mediaItem.getImageUrl())
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .placeholder(R.drawable.video_default)
//                .error(R.drawable.video_default)
//                .into(viewHolder.iv_icon);

        //Use Picasso
        Picasso.with(context)
                .load(mediaItem.getImageUrl())
                .placeholder(R.drawable.video_default)
                .error(R.drawable.video_default)
                .into(viewHolder.iv_icon);


        return convertView;
    }

    static class ViewHolder{
        ImageView iv_icon;
        TextView tv_name;
        TextView tv_desc;
    }
}
