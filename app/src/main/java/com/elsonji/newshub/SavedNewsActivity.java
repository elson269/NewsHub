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
import android.widget.TextView;
import android.widget.Toast;

import com.elsonji.newshub.data.NewsContract;

import static com.elsonji.newshub.PageFragment.NEWS_URL;
import static com.elsonji.newshub.data.NewsContract.NewsEntry.CONTENT_URI;

public class SavedNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,
        SavedNewsCursorAdapter.OnDeleteListener {

    private Cursor mCursor;
    private ListView mListView;
    private TextView mEmptyTextView;
    private SavedNewsCursorAdapter mAdapter;
    private static final int SAVED_NEWS_LOADER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);

        mEmptyTextView = findViewById(R.id.saved_news_empty_text_view);
        mListView = findViewById(R.id.saved_news_recycler_view);
        mAdapter = new SavedNewsCursorAdapter(this, null, this);

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

            if (mCursor != null && mCursor.getCount() != 0) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.delete_all_news));
                builder.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
                        if (rowsDeleted == 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.error_with_deleting_news),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            mAdapter.swapCursor(null);
                            mEmptyTextView.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(), getString(R.string.news_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int id) {
                        if (dialogInterface != null) {
                            dialogInterface.dismiss();
                        }
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.no_news_to_delete),
                        Toast.LENGTH_SHORT).show();
            }
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
        if (mCursor != null && mCursor.getCount() != 0) {
            mListView.setAdapter(mAdapter);
            mEmptyTextView.setVisibility(View.INVISIBLE);
        } else {
            mListView.setVisibility(View.INVISIBLE);
        }
        mAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onDelete(Cursor cursor) {
        //Setting count to 1 is because in SavedNewsCursorAdapter the count of the cursor has not been
        //updated to new count yet. Inside onClick, the count still remains the same until bindView gets
        //executed again.
        if (cursor.getCount() == 1) {
            mEmptyTextView.setVisibility(View.VISIBLE);
        }
    }
}
