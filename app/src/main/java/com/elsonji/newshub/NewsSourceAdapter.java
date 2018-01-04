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

import java.util.ArrayList;

public class NewsSourceAdapter extends RecyclerView.Adapter<NewsSourceAdapter.
        NewsSourceViewHolder> {

    private Context mContext;
    private ArrayList<String> mNewsSourceList;
    private OnNewsSourceClickListener mListener;

    public NewsSourceAdapter(Context context, ArrayList<String> newsSourceList,
                             OnNewsSourceClickListener listener) {
        mContext = context;
        mNewsSourceList = removeDashInString(newsSourceList);
        mListener = listener;
    }

    @Override
    public NewsSourceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext)
                .inflate(R.layout.news_source_item, parent, false);
        final NewsSourceViewHolder newsSourceViewHolder = new NewsSourceViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int positionClicked = newsSourceViewHolder.getAdapterPosition();
                Toast.makeText(mContext, mNewsSourceList.get(positionClicked) + " " +
                        mContext.getResources().getString(R.string.news_source_added), Toast.LENGTH_SHORT).show();
                mNewsSourceList.remove(positionClicked);
                notifyItemRemoved(positionClicked);
                notifyItemRangeChanged(positionClicked, mNewsSourceList.size());
                mListener.onSourceItemClick(view, positionClicked);

            }
        });
        return newsSourceViewHolder;
    }

    @Override
    public void onBindViewHolder(NewsSourceViewHolder holder, int position) {
        holder.imageButton.setImageResource(R.drawable.ic_plus_circle_red_outline);
        holder.newsSourceTextView.setText(mNewsSourceList.get(position));
    }

    @Override
    public int getItemCount() {
        if (mNewsSourceList != null) {
            return mNewsSourceList.size();
        } else {
            return 0;
        }
    }

    public class NewsSourceViewHolder extends RecyclerView.ViewHolder {
        ImageView imageButton;
        TextView newsSourceTextView;

        public NewsSourceViewHolder(View itemView) {
            super(itemView);
            imageButton = itemView.findViewById(R.id.news_source_change_button);
            newsSourceTextView = itemView.findViewById(R.id.news_source_text_view);
        }
    }

    public void clearNewsSource() {
        int size = mNewsSourceList.size();
        mNewsSourceList.clear();
        notifyItemChanged(0, size);
    }

    public void addNewsSource(ArrayList<String> newsSourceList) {
        mNewsSourceList.addAll(newsSourceList);
        notifyItemChanged(0, newsSourceList.size() - 1);
    }

    public static ArrayList<String> removeDashInString(ArrayList<String> dataSet) {
        String string;
        ArrayList<String> stringsWithoutDash = new ArrayList<>();
        if (dataSet != null && dataSet.size() != 0) {
            for (String data : dataSet) {
                string = data.replace("-", " ").toUpperCase();
                stringsWithoutDash.add(string);
            }
            return stringsWithoutDash;
        } else {
            return null;
        }
    }

    public interface OnNewsSourceClickListener {
        void onSourceItemClick(View view, int position);
    }
}
