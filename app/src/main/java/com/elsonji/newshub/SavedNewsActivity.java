package com.elsonji.newshub;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.elsonji.newshub.data.NewsContract;

import static com.elsonji.newshub.PageFragment.NEWS_URL;
import static com.elsonji.newshub.data.NewsContract.NewsEntry.CONTENT_URI;

public class SavedNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private Cursor mCursor;
    private ListView mListView;
    private SavedNewsCursorAdapter mAdapter;
    private static final int SAVED_NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);

        mListView = findViewById(R.id.saved_news_recycler_view);
        mAdapter = new SavedNewsCursorAdapter(this, null);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, final long id) {
                Intent webViewIntent = new Intent(getApplicationContext(), SavedNewsWebViewActivity.class);
                mCursor.moveToPosition(position);
                webViewIntent.putExtra(NEWS_URL, mCursor.getString(mCursor.
                        getColumnIndexOrThrow(NewsContract.NewsEntry.COLUMN_NEWS_URL)));
                getApplicationContext().startActivity(webViewIntent);
            }
        });

        getLoaderManager().initLoader(SAVED_NEWS_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_news_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClickedId = item.getItemId();

        if (itemClickedId == R.id.action_delete_all_news) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete all news?");
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int id) {
                        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
                        if (rowsDeleted == 0) {
                            Toast.makeText(getApplicationContext(), "Error with deleting news",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mAdapter = new SavedNewsCursorAdapter(getApplicationContext(), null);
                            mListView.setAdapter(mAdapter);
                            Toast.makeText(getApplicationContext(), "News deleted",
                                    Toast.LENGTH_SHORT).show();
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                NewsContract.NewsEntry._ID,
                NewsContract.NewsEntry.COLUMN_NEWS_TITLE,
                NewsContract.NewsEntry.COLUMN_NEWS_AUTHOR,
                NewsContract.NewsEntry.COLUMN_NEWS_DESCRIPTION,
                NewsContract.NewsEntry.COLUMN_NEWS_URL,
                NewsContract.NewsEntry.COLUMN_NEWS_IMAGE_URL,
                NewsContract.NewsEntry.COLUMN_NEWS_TIME
        };
        return new CursorLoader(this, NewsContract.NewsEntry.CONTENT_URI, projection, null,
                null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursor = cursor;
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
