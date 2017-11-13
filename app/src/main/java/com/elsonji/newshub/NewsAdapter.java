package com.elsonji.newshub;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mContext;
    private ArrayList<News> mNews;

    public NewsAdapter(Context context, ArrayList<News> news) {
        mContext = context;
        mNews = news;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ConstraintLayout itemView = (ConstraintLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.news_item, parent, false);
        NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);

        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.newsTitleTextView.setText(mNews.get(position).getTitle());
        holder.newsDescriptionTextView.setText(mNews.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (mNews != null) {
            return mNews.size();
        } else {
            return 0;
        }
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitleTextView, newsDescriptionTextView;
        ImageView newsImageView;
        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitleTextView = (TextView) itemView.findViewById(R.id.news_title_text_view);
            newsDescriptionTextView = (TextView) itemView.findViewById(R.id.news_description_text_view);
            newsImageView = (ImageView) itemView.findViewById(R.id.news_image_view);
        }
    }

    //This helper method clears all the items in the adapter.
    public void clearNews() {
        int size = mNews.size();
        mNews.clear();
        notifyItemRangeRemoved(0, size);
    }

    //This helper method adds all the items to the adapter.
    public void addNews(ArrayList<News> news) {
        mNews.addAll(news);
        notifyItemRangeInserted(0, news.size() - 1);
    }
}
