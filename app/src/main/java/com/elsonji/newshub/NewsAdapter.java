package com.elsonji.newshub;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elsonji.newshub.data.NewsContract.NewsEntry;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context mContext;
    private ArrayList<News> mNews;
    private OnNewsClickListener mNewsClickListener;
    private int mFavoriteButtonUnclicked = R.drawable.ic_bookmark_border_24dp;
    private int mFavoriteButtonClicked = R.drawable.ic_bookmark_24dp;

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
    public void onBindViewHolder(final NewsViewHolder holder, final int position) {
        holder.newsTitleTextView.setText(mNews.get(position).getTitle());
        holder.newsDescriptionTextView.setText(mNews.get(position).getDescription());
        Picasso.with(mContext).load(mNews.get(position).getUrlToImage()).into(holder.newsImageView);

        final Cursor favoriteNewsCursor = mContext.getContentResolver().query(NewsEntry.CONTENT_URI,
                null, null, null, null, null);
        holder.favoriteImageButton.setImageResource(mFavoriteButtonUnclicked);
        holder.favoriteImageButton.setTag(mFavoriteButtonUnclicked);
        String savedNewsUrl;
        if (favoriteNewsCursor != null) {
            favoriteNewsCursor.moveToFirst();
            try {
                while (!favoriteNewsCursor.isAfterLast()) {
                    savedNewsUrl = favoriteNewsCursor.getString(favoriteNewsCursor.getColumnIndexOrThrow("url"));
                    if (!savedNewsUrl.equals(mNews.get(position).getUrl())) {
                        favoriteNewsCursor.moveToNext();
                    } else {
                        holder.favoriteImageButton.setImageResource(R.drawable.ic_bookmark_24dp);
                        holder.favoriteImageButton.setTag(R.drawable.ic_bookmark_24dp);
                        break;
                    }
                }
            } finally {
                favoriteNewsCursor.close();
            }
        }

        holder.shareImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, mNews.get(position).getUrl());
                mContext.startActivity(shareIntent);
            }
        });

        holder.favoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newUri;
                if (Integer.parseInt(holder.favoriteImageButton.getTag().toString()) == mFavoriteButtonUnclicked) {
                    ContentValues values = new ContentValues();
                    values.put(NewsEntry.COLUMN_NEWS_TITLE, mNews.get(position).getTitle());
                    values.put(NewsEntry.COLUMN_NEWS_AUTHOR, mNews.get(position).getAuthor());
                    values.put(NewsEntry.COLUMN_NEWS_DESCRIPTION, mNews.get(position).getDescription());
                    values.put(NewsEntry.COLUMN_NEWS_URL, mNews.get(position).getUrl());
                    values.put(NewsEntry.COLUMN_NEWS_IMAGE_URL, mNews.get(position).getUrlToImage());
                    values.put(NewsEntry.COLUMN_NEWS_TIME, mNews.get(position).getPublishTime());
                    newUri = mContext.getContentResolver().insert(NewsEntry.CONTENT_URI, values);
                    if (newUri == null) {
                        Toast.makeText(mContext, mContext.getString(R.string.add_favorite_fail_msg),
                                Toast.LENGTH_SHORT).show();
                    } else {
                        holder.favoriteImageButton.setImageResource(mFavoriteButtonClicked);
                        holder.favoriteImageButton.setTag(mFavoriteButtonClicked);
                        Toast.makeText(mContext, mContext.getString(R.string.add_favorite_success_msg),
                                Toast.LENGTH_SHORT).show();
                    }
                    // The next else block is used to search the saved news' row id in the database.
                } else {
                    Cursor favoriteCursor = mContext.getContentResolver().query(NewsEntry.CONTENT_URI,
                            null, null, null, null, null);
                    int rowId = 1;
                    String newsUrl;
                    if (favoriteCursor != null) {
                        favoriteCursor.moveToFirst();
                        rowId = favoriteCursor.getInt(favoriteCursor.getColumnIndexOrThrow(NewsEntry._ID));
                        try {
                            while (!favoriteCursor.isAfterLast()) {
                                newsUrl = favoriteCursor.getString(favoriteCursor.getColumnIndexOrThrow("url"));
                                if (!newsUrl.equals(mNews.get(position).getUrl())) {
                                    favoriteCursor.moveToNext();
                                    rowId = favoriteCursor.getInt(favoriteCursor.getColumnIndexOrThrow(NewsEntry._ID));
                                } else {
                                    break;
                                }
                            }
                        } finally {
                            favoriteCursor.close();
                        }
                    }
                    Uri newsToDeletedUri = ContentUris.withAppendedId(NewsEntry.CONTENT_URI, rowId);
                    Log.i("aaaaaaaaaa", String.valueOf(rowId));
                    showDeleteConfirmationDialog(newsToDeletedUri, holder.favoriteImageButton);
                }
            }
        });
    }

    private void showDeleteConfirmationDialog(final Uri uri, final ImageButton button) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Delete this news?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (uri != null) {
                    int rowsDeleted = mContext.getContentResolver().delete(uri, null, null);
                    if (rowsDeleted == 0) {
                        Toast.makeText(mContext, "Error with deleting news", Toast.LENGTH_SHORT).show();
                    } else {
                        button.setImageResource(mFavoriteButtonUnclicked);
                        button.setTag(mFavoriteButtonUnclicked);
                        Toast.makeText(mContext, "News deleted", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int id) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
