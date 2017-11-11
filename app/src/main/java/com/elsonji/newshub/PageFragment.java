package com.elsonji.newshub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "com.elsonji.newshub.ARG_PAGE";
    private int mPage;

//    public static PageFragment newInstance(int page) {
//        Bundle args = new Bundle();
//        args.putInt(ARG_PAGE, page);
//        PageFragment fragment = new PageFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//            mPage = getArguments().getInt(ARG_PAGE);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
//        TextView textView = (TextView) rootView;
//        textView.setText("Fragment #" + mPage);
        return rootView;
    }
}

