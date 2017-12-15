package com.elsonji.newshub;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.elsonji.newshub.NewsSourceAdapter.OnNewsSourceClickListener;

import java.util.ArrayList;

import static com.elsonji.newshub.NewsSourceAdapter.removeDashInString;

public class MyNewsAdapter extends RecyclerView.Adapter<MyNewsAdapter.NewsSourceDeletionViewHolder>{

    private Context mContext;
    private ArrayList<String> mNewsSourceDeletionList;
    private OnNewsSourceClickListener mListener;

    public MyNewsAdapter(Context context, ArrayList<String> newsSourceList,
                         OnNewsSourceClickListener listener) {
        mContext = context;
        mNewsSourceDeletionList = removeDashInString(newsSourceList);
        mListener = listener;
    }

    @Override
    public NewsSourceDeletionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.news_source_item, parent, false);
        final NewsSourceDeletionViewHolder newsSourceViewHolder = new NewsSourceDeletionViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionClicked = newsSourceViewHolder.getAdapterPosition();
                Toast.makeText(mContext, mNewsSourceDeletionList.get(positionClicked) + " " +
                                mContext.getResources().getString(R.string.news_source_deleted),
                        Toast.LENGTH_SHORT).show();
                mNewsSourceDeletionList.remove(positionClicked);
                notifyItemRemoved(positionClicked);
                notifyItemRangeChanged(positionClicked, mNewsSourceDeletionList.size());
                mListener.onSourceItemClick(view, positionClicked);
            }
        });

        return newsSourceViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsSourceDeletionViewHolder holder, int position) {
        holder.imageButton.setImageResource(R.drawable.ic_minus_circle_red_outline);
        holder.newsSourceTextView.setText(mNewsSourceDeletionList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mNewsSourceDeletionList != null) {
            return mNewsSourceDeletionList.size();
        } else {
            return 0;
        }
    }

    public class NewsSourceDeletionViewHolder extends RecyclerView.ViewHolder {
        ImageView imageButton;
        TextView newsSourceTextView;

        public NewsSourceDeletionViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.news_source_change_button);
            newsSourceTextView = itemView.findViewById(R.id.news_source_text_view);
        }
    }


    public void clearNewsSource() {
        int size = mNewsSourceDeletionList.size();
        mNewsSourceDeletionList.clear();
        notifyItemChanged(0, size);
    }

    public void addNewsSource(ArrayList<String> newsSourceList) {
        mNewsSourceDeletionList.addAll(newsSourceList);
        notifyItemChanged(0, newsSourceList.size() - 1);
    }
}
