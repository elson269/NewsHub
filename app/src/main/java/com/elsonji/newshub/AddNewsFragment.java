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

public class AddNewsFragment extends Fragment implements NewsSourceAdditionAdapter.OnNewsSourceClickListener{
    public static final String NEWS_SOURCE_FOR_ADDITION = "NEWS_SOURCE_FOR_ADDITION";
    private ArrayList<String> mNewsSourcesForAddition, mNewsSourceChosen;
    private RecyclerView mNewsSourceAdditionRecyclerView;
    private NewsSourceAdditionAdapter mNewsSourceAdditionAdapter;
    private LinearLayoutManager mSourceAdditionLayoutManager;

    public static AddNewsFragment newInstance(ArrayList<String> data) {
        Bundle args = new Bundle();
        args.putStringArrayList(NEWS_SOURCE_FOR_ADDITION, data);
        AddNewsFragment fragment = new AddNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsSourcesForAddition = getArguments().getStringArrayList(NEWS_SOURCE_FOR_ADDITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_news, container, false);
        mNewsSourceAdditionRecyclerView = rootView.findViewById(R.id.add_news_recycler_view);

        mNewsSourceAdditionAdapter = new NewsSourceAdditionAdapter(getContext(), mNewsSourcesForAddition, this);
        mNewsSourceAdditionRecyclerView.setAdapter(mNewsSourceAdditionAdapter);
        mSourceAdditionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceAdditionRecyclerView.setLayoutManager(mSourceAdditionLayoutManager);
        return rootView;
    }

    @Override
    public void onSourceItemClick(View view, int position) {
        mNewsSourceChosen = new ArrayList<>();
        mNewsSourceChosen.add(mNewsSourcesForAddition.get(position));
    }
}
