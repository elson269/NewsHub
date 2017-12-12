package com.elsonji.newshub;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
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

        CardView itemView = (CardView) LayoutInflater.from(mContext)
                .inflate(R.layout.news_item, parent, false);
        final NewsViewHolder newsViewHolder = new NewsViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionClicked = newsViewHolder.getAdapterPosition();
                mNewsClickListener.onNewsClick(view, positionClicked);
            }
        });
        return newsViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {
        holder.newsTitleTextView.setText(mNews.get(position).getTitle());
        holder.newsDescriptionTextView.setText(mNews.get(position).getDescription());
        Picasso.with(mContext).load(mNews.get(position).getUrlToImage()).into(holder.newsImageView);
        holder.shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mNews.get(position).getUrl());
                mContext.startActivity(shareIntent);
            }
        });
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
        ImageButton favoriteImageButton, shareImageButton;
        public NewsViewHolder(View itemView) {
            super(itemView);
            newsTitleTextView = itemView.findViewById(R.id.news_title_text_view);
            newsDescriptionTextView = itemView.findViewById(R.id.news_description_text_view);
            newsImageView = itemView.findViewById(R.id.news_image_view);
            favoriteImageButton = itemView.findViewById(R.id.favorite_button);
            shareImageButton = itemView.findViewById(R.id.share_button);
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
