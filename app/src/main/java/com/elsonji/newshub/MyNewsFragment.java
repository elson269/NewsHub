package com.elsonji.newshub;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private RecyclerView mNewsSourceDeletionRecyclerView;
    private MyNewsAdapter mNewsSourceDeletionAdapter;
    private LinearLayoutManager mSourceDeletionLayoutManager;
    private TextView mEmptyTextView;
    private NewsUpdateListener mNewsUpdateListener;
    private SharedPreferences mRemainingNewsInMyNews;
    private SharedPreferences.Editor mRemainingMyNewsEditor;
    private SharedPreferences mDeletedMyNewsSourcePref;
    private SharedPreferences.Editor mDeletedMyNewsEditor;
    private Set<String> mRemainingMyNewsSet;
    private Set<String> mSourceDeletedFromMyNewsSet;

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
        mSourceDeletedFromMyNews = getArguments().getStringArrayList(DELETED_MY_NEWS);
        if (mSourceDeletedFromMyNews != null) {
            Collections.sort(mSourceDeletedFromMyNews);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mRemainingMyNewsSet = new HashSet<>();
        mRemainingNewsInMyNews = getActivity().
                getSharedPreferences(REMAINING_MY_NEWS, Context.MODE_PRIVATE);
        mRemainingMyNewsEditor = mRemainingNewsInMyNews.edit();
        mRemainingMyNewsEditor.apply();

        mDeletedMyNewsSourcePref = getActivity().getSharedPreferences(DELETED_MY_NEWS, Context.MODE_PRIVATE);
        mDeletedMyNewsEditor = mDeletedMyNewsSourcePref.edit();
        mDeletedMyNewsEditor.apply();

        View rootView = inflater.inflate(R.layout.fragment_my_news, container, false);
        mNewsSourceDeletionRecyclerView = rootView.findViewById(R.id.my_news_recycler_view);
        mEmptyTextView = rootView.findViewById(R.id.my_news_fragment_emtpy_text_view);
        if (mNewsSourcesForDeletion != null && mNewsSourcesForDeletion.size() != 0) {
            mEmptyTextView.setVisibility(View.GONE);
        }

        mNewsSourceDeletionAdapter = new MyNewsAdapter(getContext(),
                mNewsSourcesForDeletion, this);
        mNewsSourceDeletionRecyclerView.setAdapter(mNewsSourceDeletionAdapter);
        mSourceDeletionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceDeletionRecyclerView.setLayoutManager(mSourceDeletionLayoutManager);
        return rootView;
    }

    @Override
    public void onSourceItemClick(View view, int position) {
        mSourceDeletedFromMyNews.add(mNewsSourcesForDeletion.get(position));
        if (mNewsSourcesForDeletion.size() != 0) {
            mNewsSourcesForDeletion.remove(mNewsSourcesForDeletion.get(position));
            Collections.sort(mNewsSourcesForDeletion);
            if (mNewsSourcesForDeletion.size() == 0) {
                mEmptyTextView = getActivity().findViewById(R.id.my_news_fragment_emtpy_text_view);
                mEmptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            mEmptyTextView = getActivity().findViewById(R.id.my_news_fragment_emtpy_text_view);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }

        //Convert ArrayList to Set so it can be added to SharedPreferences.
        mSourceDeletedFromMyNewsSet = new HashSet<>();
        mSourceDeletedFromMyNewsSet.addAll(mSourceDeletedFromMyNews);
        mDeletedMyNewsEditor.putStringSet(DELETED_MY_NEWS, mSourceDeletedFromMyNewsSet);
        mDeletedMyNewsEditor.apply();
        mNewsUpdateListener.onNewsUpdate(mSourceDeletedFromMyNewsSet);

        Set<String> remainingNewsSet = new HashSet<>(mNewsSourcesForDeletion);
        mRemainingMyNewsEditor.putStringSet(REMAINING_MY_NEWS, remainingNewsSet);
        mRemainingMyNewsEditor.apply();

    }

    public void updateData(Set<String> stringSet) {

        mRemainingMyNewsSet.addAll(stringSet);
        ArrayList<String> strings = new ArrayList<>(mRemainingMyNewsSet);
        Collections.sort(strings);

        mRemainingMyNewsEditor.putStringSet(REMAINING_MY_NEWS, mRemainingMyNewsSet);
        mRemainingMyNewsEditor.apply();

        //When updateData is called in NewsSourceFragment, mSourceDeletedFromMyNewsSet needs to exclude
        //the new selected item in NewsSourceFragment, otherwise MyNewsFragment will display the remaining
        //ny news source from the last update.

        SharedPreferences myNewsDeletedPref = getActivity().getSharedPreferences(DELETED_MY_NEWS,
                Context.MODE_PRIVATE);
        Set<String> myNewsDeletedSet = myNewsDeletedPref.getStringSet(DELETED_MY_NEWS, null);
        if (myNewsDeletedSet != null) {
            stringSet.removeAll(myNewsDeletedSet);
            mDeletedMyNewsEditor.putStringSet(DELETED_MY_NEWS, stringSet);
            mDeletedMyNewsEditor.apply();
        }

        mNewsSourceDeletionAdapter = new MyNewsAdapter(getContext(), strings, this);
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
            throw new RuntimeException(context.toString() + getString(R.string.must_implement_news_update_listener));
        }
    }
}
