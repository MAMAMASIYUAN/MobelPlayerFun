package com.example.iori.mobelplayerfun.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


@SuppressLint("ValidFragment")
public class ReplaceFragment extends Fragment {

    private BasePager currPager;
    @SuppressLint("ValidFragment")
    public ReplaceFragment(BasePager pager){
        this.currPager = pager;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return currPager.rootView;
    }


}
