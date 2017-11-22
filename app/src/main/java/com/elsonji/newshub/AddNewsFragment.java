package com.elsonji.newshub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AddNewsFragment extends Fragment implements NewsSourceAdditionAdapter.OnNewsSourceClickListener {
    public static final String NEWS_SOURCE_FOR_ADDITION = "NEWS_SOURCE_FOR_ADDITION";
    public static final String SELECTED_NEWS_SOURCE = "SELECTED_NEWS_SOURCE";
    public static final String REMAINING_NEWS_SOURCE = "REMAINING_NEWS_SOURCE";
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

        mNewsSourceAdditionAdapter =
                new NewsSourceAdditionAdapter(getContext(), mNewsSourcesForAddition, this);
        mNewsSourceAdditionRecyclerView.setAdapter(mNewsSourceAdditionAdapter);
        mSourceAdditionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceAdditionRecyclerView.setLayoutManager(mSourceAdditionLayoutManager);
        Log.i("33333333", "33333333");
        return rootView;
    }

    @Override
    public void onSourceItemClick(View view, int position) {
        mNewsSourceChosen = new ArrayList<>();
        mNewsSourceChosen.add(mNewsSourcesForAddition.get(position));
        mNewsSourcesForAddition.remove(mNewsSourcesForAddition.get(position));

        Log.i("222222222222", "222222222");

        SharedPreferences myNewsSharedPreferences = getActivity().
                getSharedPreferences(SELECTED_NEWS_SOURCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myNewsSharedPreferences.edit();

        //Convert ArrayList to Set so it can be added to SharedPreferences.
        Set<String> set = new HashSet<>();
        set.addAll(mNewsSourceChosen);
        editor.putStringSet(SELECTED_NEWS_SOURCE, set);
        editor.apply();


        SharedPreferences remainingNewsSharedPreferences = getActivity().
                getSharedPreferences(REMAINING_NEWS_SOURCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorForRemainingSources = remainingNewsSharedPreferences.edit();
        Set<String> setForRemainingSources = new HashSet<>();
        setForRemainingSources.addAll(mNewsSourcesForAddition);
        editorForRemainingSources.putStringSet(REMAINING_NEWS_SOURCE, setForRemainingSources);
        editorForRemainingSources.apply();
    }
}
