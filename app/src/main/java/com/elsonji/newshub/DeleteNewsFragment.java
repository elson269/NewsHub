package com.elsonji.newshub;

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

import com.elsonji.newshub.NewsSourceAdditionAdapter.OnNewsSourceClickListener;

import java.util.ArrayList;

public class DeleteNewsFragment extends Fragment implements OnNewsSourceClickListener {

    public static final String NEWS_SOURCE_FOR_DELETION = "NEWS_SOURCE_FOR_DELETION";
    private ArrayList<String> mNewsSourcesForDeletion, mNewsSourceChosen;
    private RecyclerView mNewsSourceDeletionRecyclerView;
    private NewsSourceDeletionAdapter mNewsSourceDeletionAdapter;
    private LinearLayoutManager mSourceDeletionLayoutManager;
    private TextView mEmptyTextView;


    public static DeleteNewsFragment newInstance(ArrayList<String> data) {
        Bundle args = new Bundle();
        args.putStringArrayList(NEWS_SOURCE_FOR_DELETION, data);
        DeleteNewsFragment fragment = new DeleteNewsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsSourcesForDeletion = getArguments().getStringArrayList(NEWS_SOURCE_FOR_DELETION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_delete_news, container, false);
        mNewsSourceDeletionRecyclerView = rootView.findViewById(R.id.delete_news_recycler_view);

        if (mNewsSourcesForDeletion != null && mNewsSourcesForDeletion.size() != 0) {
            mEmptyTextView = rootView.findViewById(R.id.delete_fragment_emtpy_text_view);
            mEmptyTextView.setVisibility(View.GONE);
        }

        mNewsSourceDeletionAdapter = new NewsSourceDeletionAdapter(getContext(),
                mNewsSourcesForDeletion, this);
        mNewsSourceDeletionRecyclerView.setAdapter(mNewsSourceDeletionAdapter);
        mSourceDeletionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceDeletionRecyclerView.setLayoutManager(mSourceDeletionLayoutManager);
        return rootView;
    }

    @Override
    public void onSourceItemClick(View view, int position) {
        mNewsSourceChosen = new ArrayList<>();
        mNewsSourceChosen.remove(mNewsSourcesForDeletion.get(position));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void updateData(ArrayList<String> strings) {
        Log.i("ooooooo", "ooooo");
        mNewsSourceDeletionAdapter = new NewsSourceDeletionAdapter(getContext(),
                strings, this);
        if (strings != null && strings.size() != 0) {
            mEmptyTextView = getActivity().findViewById(R.id.delete_fragment_emtpy_text_view);
            mEmptyTextView.setVisibility(View.GONE);
        }
        mNewsSourceDeletionRecyclerView.setAdapter(mNewsSourceDeletionAdapter);
        mSourceDeletionLayoutManager = new LinearLayoutManager(getContext());
        mNewsSourceDeletionRecyclerView.setLayoutManager(mSourceDeletionLayoutManager);
    }

//    @Override
//    public void onDataUpdate() {
//        SharedPreferences selectedNews =
//                getActivity().getSharedPreferences(SELECTED_NEWS_SOURCE, Context.MODE_PRIVATE);
//
//        Set<String> selectedNewsSet = selectedNews.getStringSet(SELECTED_NEWS_SOURCE, null);
//        ArrayList<String> selectedNewsList;
//        if (selectedNewsSet != null && selectedNewsSet.size() != 0) {
//            selectedNewsList = new ArrayList<>(selectedNewsSet);
//            mNewsSourcesForDeletion = selectedNewsList;
//        }
//
//    }
}
