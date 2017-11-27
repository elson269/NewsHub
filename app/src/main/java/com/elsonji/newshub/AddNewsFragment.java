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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static com.elsonji.newshub.NewsSelectionActivity.MY_NEWS_SOURCE;

public class AddNewsFragment extends Fragment implements NewsSourceAdditionAdapter.OnNewsSourceClickListener {
    public static final String NEWS_SOURCE_FOR_ADDITION = "NEWS_SOURCE_FOR_ADDITION";
    public static final String REMAINING_NEWS_SOURCE = "REMAINING_NEWS_SOURCE";
    public static final String SELECTED_NEWS_SOURCE = "SELECTED_NEWS_SOURCE";
    private ArrayList<String> mNewsSourcesForAddition;
    private ArrayList<String> mNewsSourceChosen;
    private TextView mEmptyTextView;
    private RecyclerView mNewsSourceAdditionRecyclerView;
    private NewsSourceAdditionAdapter mNewsSourceAdditionAdapter;
    private LinearLayoutManager mSourceAdditionLayoutManager;
    private DataUpdateListener mDataUpdateListener;

    public static AddNewsFragment newInstance(ArrayList<String> data, ArrayList<String> myNewsData) {
        Bundle args = new Bundle();
        args.putStringArrayList(NEWS_SOURCE_FOR_ADDITION, data);
        args.putStringArrayList(MY_NEWS_SOURCE, myNewsData);
        AddNewsFragment fragment = new AddNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsSourcesForAddition = getArguments().getStringArrayList(NEWS_SOURCE_FOR_ADDITION);
        mNewsSourceChosen = getArguments().getStringArrayList(MY_NEWS_SOURCE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_add_news, container, false);
        mNewsSourceAdditionRecyclerView = rootView.findViewById(R.id.add_news_recycler_view);
        mEmptyTextView = rootView.findViewById(R.id.add_fragment_emtpy_text_view);
        if (mNewsSourcesForAddition != null && mNewsSourcesForAddition.size() != 0) {
            mEmptyTextView.setVisibility(View.GONE);
        }

        mNewsSourceAdditionAdapter =
                new NewsSourceAdditionAdapter(getContext(), mNewsSourcesForAddition, this);
        mNewsSourceAdditionRecyclerView.setAdapter(mNewsSourceAdditionAdapter);
        mSourceAdditionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceAdditionRecyclerView.setLayoutManager(mSourceAdditionLayoutManager);
        return rootView;
    }

    @Override
    public void onSourceItemClick(View view, int position) {

        mNewsSourceChosen.add(mNewsSourcesForAddition.get(position));
        if (mNewsSourcesForAddition.size() != 0) {
            mNewsSourcesForAddition.remove(mNewsSourcesForAddition.get(position));
            if (mNewsSourcesForAddition.size() == 0) {
                mEmptyTextView = getActivity().findViewById(R.id.add_fragment_emtpy_text_view);
                mEmptyTextView.setVisibility(View.VISIBLE);
            }
        } else {
            mEmptyTextView = getActivity().findViewById(R.id.add_fragment_emtpy_text_view);
            mEmptyTextView.setVisibility(View.VISIBLE);
        }


        SharedPreferences myNewsSharedPreferences = getActivity().
                getSharedPreferences(MY_NEWS_SOURCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = myNewsSharedPreferences.edit();

        //Convert ArrayList to Set so it can be added to SharedPreferences.
        Set<String> set = new HashSet<>();
        set.addAll(mNewsSourceChosen);
        editor.putStringSet(MY_NEWS_SOURCE, set);
        editor.apply();
        mDataUpdateListener.onDataUpdate(mNewsSourceChosen);


        SharedPreferences remainingNewsSharedPreferences = getActivity().
                getSharedPreferences(REMAINING_NEWS_SOURCE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editorForRemainingSources = remainingNewsSharedPreferences.edit();
        Set<String> setForRemainingSources = new HashSet<>();
        setForRemainingSources.addAll(mNewsSourcesForAddition);
        editorForRemainingSources.putStringSet(REMAINING_NEWS_SOURCE, setForRemainingSources);
        editorForRemainingSources.apply();
    }

    public interface DataUpdateListener {
        void onDataUpdate(ArrayList<String> newsSourceChosen);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DataUpdateListener) {
            mDataUpdateListener = (DataUpdateListener) context;
        } else {
            throw new RuntimeException(context.toString() + "must implement DataUpdateListener");
        }
    }
}
