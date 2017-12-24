package com.elsonji.newshub;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

public class WidgetNewsAdapter extends RecyclerView.Adapter<WidgetNewsAdapter.WidgetNewsViewHolder>{
    private Context mContext;
    private ArrayList<String> mNewsSourceList;
    private int mLastCheckedPosition = -1;
    private int mAppWidgetId;
    public static final String WIDGET_SELECTED_STRING = "WIDGET_SELECTED_STRING";
    private OnWidgetSourceClickListener mWidgetListener;

    public WidgetNewsAdapter(Context context, ArrayList<String> strings, OnWidgetSourceClickListener listener, int appWidgetId) {
        mContext = context;
        mNewsSourceList = strings;
        mWidgetListener = listener;
        mAppWidgetId = appWidgetId;
    }
    @Override
    public WidgetNewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout itemView = (LinearLayout) LayoutInflater.from(mContext).inflate(
                R.layout.widget_configure_news_item, parent, false);
        WidgetNewsViewHolder viewHolder = new WidgetNewsViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final WidgetNewsViewHolder holder, final int position) {
        holder.newsSourceTextView.setText(mNewsSourceList.get(position));
        holder.radioButton.setChecked(position == mLastCheckedPosition);
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLastCheckedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();

                SharedPreferences widgetSelectedNewsPref = mContext.getSharedPreferences(
                        createTag(mAppWidgetId), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = widgetSelectedNewsPref.edit();
                editor.putString(createTag(mAppWidgetId), mNewsSourceList.get(position));
                editor.apply();

                mWidgetListener.onWidgetSourceClick(view, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mNewsSourceList != null) {
            return mNewsSourceList.size();
        } else {
            return 0;
        }
    }

    public class WidgetNewsViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButton;
        TextView newsSourceTextView;
        public WidgetNewsViewHolder(View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.news_widget_radio_button);
            newsSourceTextView = itemView.findViewById(R.id.news_widget_text_view);
        }
    }

    public interface OnWidgetSourceClickListener {
        void onWidgetSourceClick(View view, int position);
    }

    public static String createTag(int id) {
        return WIDGET_SELECTED_STRING + id;
    }
}
