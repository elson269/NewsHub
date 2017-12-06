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
import android.widget.TextView;

import com.elsonji.newshub.NewsSourceAdapter.OnNewsSourceClickListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MyNewsFragment extends Fragment implements OnNewsSourceClickListener {

    public static final String NEWS_SOURCE_FOR_DELETION = "NEWS_SOURCE_FOR_DELETION";
    public static final String DELETED_MY_NEWS = "DELETED_MY_NEWS";
    public static final String REMAINING_MY_NEWS = "REMAINING_MY_NEWS";
    private ArrayList<String> mNewsSourcesForDeletion, mSourceDeletedFromMyNews;
    private Set<String> mSourceDeletedFromMyNewsSet;
    private RecyclerView mNewsSourceDeletionRecyclerView;
    private NewsSourceAdapter mNewsSourceDeletionAdapter;
    private LinearLayoutManager mSourceDeletionLayoutManager;
    private TextView mEmptyTextView;
    private NewsUpdateListener mNewsUpdateListener;


    public static MyNewsFragment newInstance(ArrayList<String> data, ArrayList<String> deletedNewsData) {
        Bundle args = new Bundle();
        args.putStringArrayList(NEWS_SOURCE_FOR_DELETION, data);
        args.putStringArrayList(DELETED_MY_NEWS, deletedNewsData);
        MyNewsFragment fragment = new MyNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsSourcesForDeletion = getArguments().getStringArrayList(NEWS_SOURCE_FOR_DELETION);
        if (mNewsSourcesForDeletion != null) {
            Collections.sort(mNewsSourcesForDeletion);
        }
        if (mNewsSourcesForDeletion != null) {
            Log.i("bbbbbb", String.valueOf(mNewsSourcesForDeletion.size()));
        }
        mSourceDeletedFromMyNews = getArguments().getStringArrayList(DELETED_MY_NEWS);
        if (mSourceDeletedFromMyNews != null) {
            Collections.sort(mSourceDeletedFromMyNews);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mNewsSourcesForDeletion != null) {
            Log.i("bbbbbb55", String.valueOf(mNewsSourcesForDeletion.size()));
        }

        View rootView = inflater.inflate(R.layout.fragment_my_news, container, false);
        mNewsSourceDeletionRecyclerView = rootView.findViewById(R.id.my_news_recycler_view);
        mEmptyTextView = rootView.findViewById(R.id.my_news_fragment_emtpy_text_view);
        if (mNewsSourcesForDeletion != null && mNewsSourcesForDeletion.size() != 0) {
            mEmptyTextView.setVisibility(View.GONE);
        }

        mNewsSourceDeletionAdapter = new NewsSourceAdapter(getContext(),
                mNewsSourcesForDeletion, this);
        mNewsSourceDeletionRecyclerView.setAdapter(mNewsSourceDeletionAdapter);
        mSourceDeletionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceDeletionRecyclerView.setLayoutManager(mSourceDeletionLayoutManager);
        return rootView;
    }

    @Override
    public void onSourceItemClick(View view, int position) {
        Log.i("bbbbbb44", String.valueOf(mNewsSourcesForDeletion.size()));
        //mSourceDeletedFromMyNewsSet = new HashSet<>(mSourceDeletedFromMyNews);
        mSourceDeletedFromMyNews.add(mNewsSourcesForDeletion.get(position));
        if (mNewsSourcesForDeletion.size() != 0) {
            mNewsSourcesForDeletion.remove(mNewsSourcesForDeletion.get(position));
            Collections.sort(mNewsSourcesForDeletion);
            Log.i("bbbbbbDeletedFromMyNews", String.valueOf(mSourceDeletedFromMyNews.size()));

            if (mNewsSourcesForDeletion.size() == 0) {
                mEmptyTextView = getActivity().findViewById(R.id.my_news_fragment_emtpy_text_view);
                mEmptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            mEmptyTextView = getActivity().findViewById(R.id.my_news_fragment_emtpy_text_view);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }

        SharedPreferences deletedMyNewsSourcePref = getActivity().
                getSharedPreferences(DELETED_MY_NEWS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = deletedMyNewsSourcePref.edit();

        //Convert ArrayList to Set so it can be added to SharedPreferences.
        Set<String> set = new HashSet<>();
        set.addAll(mSourceDeletedFromMyNews);
        editor.putStringSet(DELETED_MY_NEWS, set);
        editor.apply();
        //mSourceDeletedFromMyNews.clear();


        //mSourceDeletedFromMyNews needs to be zero....!!!!!!!!

        //Collections.sort(mSourceDeletedFromMyNews);
        mNewsUpdateListener.onNewsUpdate(set);
        Log.i("gggggOnNewsUpdate", String.valueOf(set.size()));

        SharedPreferences remainingMyNews = getActivity().
                getSharedPreferences(REMAINING_MY_NEWS, Context.MODE_PRIVATE);
        SharedPreferences.Editor remainingMyNewsEditor = remainingMyNews.edit();
        Set<String> remainingMyNewsSet = new HashSet<>();
        remainingMyNewsSet.addAll(mNewsSourcesForDeletion);
        remainingMyNewsEditor.putStringSet(REMAINING_MY_NEWS, remainingMyNewsSet);
        remainingMyNewsEditor.apply();


//        SharedPreferences selectedNewsPref = getActivity().
//                getSharedPreferences(REMAINING_NEWS_SOURCE, MODE_PRIVATE);
//        Set<String> selectedMyNewsSet = selectedNewsPref.getStringSet(REMAINING_NEWS_SOURCE, null);
//        if (selectedMyNewsSet != null) {
//            selectedMyNewsSet.clear();
//        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateData(Set<String> stringSet) {
        //Convert mNewsSourcesForDeletion to Set to avoid duplicated items.
        Set<String> newsForDeletionSet = new HashSet<>(mNewsSourcesForDeletion);
        newsForDeletionSet.addAll(stringSet);
        Log.i("bbbbbb66", String.valueOf(mNewsSourcesForDeletion.size()));

        ArrayList<String> strings = new ArrayList<>(newsForDeletionSet);
        Collections.sort(strings);
//        mNewsSourcesForDeletion.clear();
//        mNewsSourcesForDeletion.addAll(strings);

        mNewsSourceDeletionAdapter = new NewsSourceAdapter(getContext(), strings, this);
        if (strings.size() != 0) {
            mEmptyTextView = getActivity().findViewById(R.id.my_news_fragment_emtpy_text_view);
            mEmptyTextView.setVisibility(View.GONE);
        }
        mNewsSourceDeletionRecyclerView.setAdapter(mNewsSourceDeletionAdapter);
        mSourceDeletionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceDeletionRecyclerView.setLayoutManager(mSourceDeletionLayoutManager);
    }

    public void clearData() {
        mSourceDeletedFromMyNews.clear();
    }

    public interface NewsUpdateListener {
        void onNewsUpdate(Set<String> myNewsDeleted);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof NewsUpdateListener) {
            mNewsUpdateListener = (NewsUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement NewsUpdateListener");
        }
    }

}
