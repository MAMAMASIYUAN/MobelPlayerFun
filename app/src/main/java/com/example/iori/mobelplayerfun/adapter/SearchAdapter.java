package com.example.iori.mobelplayerfun.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iori.mobelplayerfun.R;
import com.example.iori.mobelplayerfun.domain.SearchBean;
import com.squareup.picasso.Picasso;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SearchAdapter extends BaseAdapter {

    private final Context context;
    private final List<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> mediaItems;
    private List<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> imageMediaItems;
    private ViewHolder viewHolder;

    public SearchAdapter(Context context, List<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean> mediaItems){
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
        SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean mediaItem = mediaItems.get(position);
        imageMediaItems = new List<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean>() {
            @Override
            public int size() {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public Iterator<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> iterator() {
                return null;
            }

            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @Override
            public <T> T[] toArray(T[] a) {
                return null;
            }

            @Override
            public boolean add(SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean imageurlsBean) {
                return false;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> c) {
                return false;
            }

            @Override
            public boolean addAll(int index, @NonNull Collection<? extends SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> c) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> c) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> c) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean get(int index) {
                return null;
            }

            @Override
            public SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean set(int index, SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean element) {
                return null;
            }

            @Override
            public void add(int index, SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean element) {

            }

            @Override
            public SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean remove(int index) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @NonNull
            @Override
            public ListIterator<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> listIterator(int index) {
                return null;
            }

            @NonNull
            @Override
            public List<SearchBean.ShowapiResBodyBean.PagebeanBean.ContentlistBean.ImageurlsBean> subList(int fromIndex, int toIndex) {
                return null;
            }
        };
        imageMediaItems = mediaItems.get(position).getImageurls();


        viewHolder.tv_name.setText(mediaItem.getTitle());
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


        String pach = "";
        if(imageMediaItems != null && imageMediaItems.size() > 0){
            pach = imageMediaItems.get(0).getUrl();
        }
        else{
            pach = String.valueOf(R.drawable.video_default);
        }

        //Use Picasso
        Picasso.with(context)
                .load(pach)
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
