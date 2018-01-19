package com.elsonji.newshub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PageFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<News>>,
        NewsAdapter.OnNewsClickListener {

    private static final String ARG_PAGE = "ARG_PAGE";
    public static final String NEWS_BASE_URL = "https://newsapi.org/v1/articles?";
    public static final String SOURCE_PARAM = "source";
    public static final String SORT_BY_PARAM = "sortBy";
    public static final String API_KEY_PARAM = "apiKey";
    public static final String LOADER_ID = "LOADER_ID";
    public static final String NEWS_URL = "NEWS_URL";
    public static final String VISIBLE_POSITION_KEY = "VISIBLE_POSITION_KEY";
    private String mNewsTabContent;
    private int mLoaderId;
    private ArrayList<News> mNews;
    private RecyclerView mNewsRecyclerView;
    private NewsAdapter mNewsAdapter;
    private GridLayoutManager mGridLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private static Resources mResources;
    private ProgressBar mProgressBar;
    private int mTopView;

    public static PageFragment newInstance(String newsTabContent, int id) {
        Bundle args = new Bundle();
        args.putString(ARG_PAGE, newsTabContent);
        args.putInt(LOADER_ID, id);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mNewsTabContent = getArguments().getString(ARG_PAGE);
        mLoaderId = getArguments().getInt(LOADER_ID);
        mResources = getResources();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        mNewsRecyclerView = rootView.findViewById(R.id.news_list_recycler_view);
        mNewsAdapter = new NewsAdapter(getContext(), new ArrayList<News>(), this);

        mNewsRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar = rootView.findViewById(R.id.progress_bar);

        float minScreenWidth = getMinWidth();
        if (minScreenWidth > 600) {
            mGridLayoutManager = new GridLayoutManager(getContext(), 2);
        } else {
            mGridLayoutManager = new GridLayoutManager(getContext(), 1);
        }

        mNewsRecyclerView.setAdapter(mNewsAdapter);
        mNewsRecyclerView.setFocusable(true);
        mNewsRecyclerView.setLayoutManager(mGridLayoutManager);
        getActivity().getSupportLoaderManager().initLoader(mLoaderId, null, this);


        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe_refresher);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mNewsRecyclerView.setAdapter(mNewsAdapter);
                mNewsRecyclerView.setLayoutManager(mGridLayoutManager);
                getActivity().getSupportLoaderManager().initLoader(mLoaderId, null, PageFragment.this);
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        return rootView;
    }

    @Override
    public Loader<ArrayList<News>> onCreateLoader(int i, Bundle bundle) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if (isConnected) {
            return new NewsLoader(getActivity(), createNewsUrl(mNewsTabContent, "top").toString());
        } else {
            Toast.makeText(getActivity(), getString(R.string.network_connection_error_message),
                    Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<News>> loader, ArrayList<News> news) {
        mProgressBar.setVisibility(View.INVISIBLE);
        mNewsRecyclerView.setVisibility(View.VISIBLE);
        mNews = news;
        mNewsAdapter.clearNews();
        if (news != null && !news.isEmpty()) {
            mNewsAdapter.addNews(mNews);
        } else {
            Toast.makeText(getActivity(), getString(R.string.news_not_available_warning),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<News>> loader) {
        mNewsAdapter.clearNews();
    }

    private URL createNewsUrl(String sourceParam, String sortByParam) {
        URL newsUrl = null;
        Uri builtUri = Uri.parse(NEWS_BASE_URL)
                .buildUpon()
                .appendQueryParameter(SOURCE_PARAM, sourceParam)
                .appendQueryParameter(SORT_BY_PARAM, sortByParam)
                .appendQueryParameter(API_KEY_PARAM, mResources.getString(R.string.api_key))
                .build();

        try {
            newsUrl = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return newsUrl;
    }

    @Override
    public void onNewsClick(View view, int position) {
        String urlString = mNews.get(position).getUrl();
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra(NEWS_URL, urlString);
        startActivity(intent);
    }

    private float getMinWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);

        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;

        float scaleFactor = metrics.density;

        float widthDp = widthPixels / scaleFactor;
        float heightDp = heightPixels / scaleFactor;

        return Math.min(widthDp, heightDp);
    }

    @Override
    public void onPause() {
        super.onPause();
        int lastFirstVisiblePosition = mGridLayoutManager.findFirstVisibleItemPosition();
        //VISIBLE_POSITION_KEY + mLoaderId is used to distinguish each SharedPreferences in each page view.
        //onPause is called multiple times based on setOffscreenPageLimit(), so the last page view's SharedPreferences
        //will override the first page view's SharedPreferences. To make each page view's SharedPreferences' key unique,
        //mLoaderId (unique) is added to VISIBLE_POSITION_KEY to distinguish it.
        SharedPreferences visiblePositionPref = getContext().getSharedPreferences(VISIBLE_POSITION_KEY + mLoaderId,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = visiblePositionPref.edit();
        editor.putInt(VISIBLE_POSITION_KEY, lastFirstVisiblePosition);
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences visiblePositionPref = getContext().getSharedPreferences(VISIBLE_POSITION_KEY + mLoaderId,
                Context.MODE_PRIVATE);
        final int lastFirstVisiblePosition = visiblePositionPref.getInt(VISIBLE_POSITION_KEY, -1);
        //postDelayed is used to force recycler view to go to the desired position; if not used,
        //it will go the the top of the recycler view.
        mNewsRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mNewsRecyclerView.scrollToPosition(lastFirstVisiblePosition);
            }
        }, 50);
    }
}

