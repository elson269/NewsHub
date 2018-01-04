package com.elsonji.newshub.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.elsonji.newshub.data.NewsContract.NewsEntry;

public class NewsDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news.db";
    private static final int DATABASE_VERSION = 1;
    private static final String DROP_TABLE_IF_EXISTS = "DROP TABLE IF EXISTS ";

    public NewsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SQL_CREATE_NEWS_TABLE = "CREATE TABLE " + NewsEntry.TABLE_NAME + " ("
                + NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NewsEntry.COLUMN_NEWS_TITLE + " TEXT, "
                + NewsEntry.COLUMN_NEWS_AUTHOR + " TEXT, "
                + NewsEntry.COLUMN_NEWS_DESCRIPTION + " TEXT, "
                + NewsEntry.COLUMN_NEWS_URL + " TEXT, "
                + NewsEntry.COLUMN_NEWS_IMAGE_URL + " TEXT, "
                + NewsEntry.COLUMN_NEWS_TIME + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_NEWS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(DROP_TABLE_IF_EXISTS + NewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
