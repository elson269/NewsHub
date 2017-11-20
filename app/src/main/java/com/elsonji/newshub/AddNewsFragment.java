package com.elsonji.newshub;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AddNewsFragment extends Fragment {
    public static final String DYNAMIC_NEWS_SOURCE = "DYNAMIC_NEWS_SOURCE";
    private ArrayList<String> mDynamicNewsSources;
    private RecyclerView mNewsSourceRecyclerView;
    private NewsSourceAdapter mNewsSourceAdapter;
    private LinearLayoutManager mSourceLayoutManager;

    public static AddNewsFragment newInstance(ArrayList<String> data) {
        Bundle args = new Bundle();
        args.putStringArrayList(DYNAMIC_NEWS_SOURCE, data);
        AddNewsFragment fragment = new AddNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDynamicNewsSources = getArguments().getStringArrayList(DYNAMIC_NEWS_SOURCE);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_news, container, false);
        mNewsSourceRecyclerView = rootView.findViewById(R.id.add_news_recycler_view);

        mNewsSourceAdapter = new NewsSourceAdapter(getContext(), mDynamicNewsSources);
        mNewsSourceRecyclerView.setAdapter(mNewsSourceAdapter);
        mSourceLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceRecyclerView.setLayoutManager(mSourceLayoutManager);
        return rootView;
    }
}
