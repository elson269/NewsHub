package com.elsonji.newshub;

import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.elsonji.newshub.data.NewsContract.NewsEntry;
import com.squareup.picasso.Picasso;

import static com.elsonji.newshub.data.NewsContract.NewsEntry.CONTENT_URI;

public class SavedNewsCursorAdapter extends CursorAdapter {

    private OnDeleteListener mListener;

    public SavedNewsCursorAdapter(Context context, Cursor cursor, OnDeleteListener listener) {
        super(context, cursor, 0);
        mListener = listener;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context)
                .inflate(R.layout.saved_news_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {
        String newsTitle = cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_NEWS_TITLE));
        String newsDescription = cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_NEWS_DESCRIPTION));
        String newsImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(NewsEntry.COLUMN_NEWS_IMAGE_URL));

        ImageView savedNewsImageView = view.findViewById(R.id.saved_news_image_view);
        Picasso.with(context).load(newsImageUrl).into(savedNewsImageView);

        TextView newsTitleTextView = view.findViewById(R.id.saved_news_title_text_view);
        newsTitleTextView.setText(newsTitle);

        TextView newsDescriptionTextView = view.findViewById(R.id.saved_news_description_text_view);
        newsDescriptionTextView.setText(newsDescription);

        //Declaring rowId outside of onClick is to avoid undesired row id number.
        final long rowId = getItemId(cursor.getPosition());
        final ImageButton deleteImageButton = view.findViewById(R.id.delete_button);
        deleteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri newsToDeletedUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
                showDeleteConfirmationDialog(newsToDeletedUri);
            }

            private void showDeleteConfirmationDialog(final Uri uri) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(R.string.delete_all_news);
                builder.setPositiveButton(context.getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (uri != null) {
                            int rowsDeleted = context.getContentResolver().delete(uri, null, null);
                            if (rowsDeleted == 0) {
                                Toast.makeText(context, context.getString(R.string.error_with_deleting_news), Toast.LENGTH_SHORT).show();
                            } else {
                                mListener.onDelete(cursor);
                                Toast.makeText(context, context.getString(R.string.news_deleted), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                builder.setNegativeButton(context.getString(R.string.cancel), new DialogInterface.OnClickListener() {
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
        });
    }

    public interface OnDeleteListener {
        void onDelete(Cursor cursor);
    }
}
