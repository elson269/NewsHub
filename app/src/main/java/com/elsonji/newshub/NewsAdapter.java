package com.elsonji.newshub;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mContext;
    private ArrayList<News> mNews;
    private OnNewsClickListener mNewsClickListener;

    public NewsAdapter(Context context, ArrayList<News> news, OnNewsClickListener listener) {
        mContext = context;
        mNews = news;
        mNewsClickListener = listener;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.news_item, parent, false);
        final NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            int positionClicked = newsViewHolder.getAdapterPosition();
            @Override
            public void onClick(View view) {
                mNewsClickListener.onNewsClick(view, positionClicked);
            }
        });
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        holder.newsTitleTextView.setText(mNews.get(position).getTitle());
        holder.newsDescriptionTextView.setText(mNews.get(position).getDescription());
        Picasso.with(mContext).load(mNews.get(position).getUrlToImage()).into(holder.newsImageView);
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

    public interface OnNewsClickListener {
        void onNewsClick(View view, int position);
    }
}
