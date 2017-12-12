package com.elsonji.newshub.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class NewsContract {
    private NewsContract() {}

    public static final String CONTENT_AUTHORITY = "com.elsonji.newshub";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_NEWS = "news";

    public static final class NewsEntry implements BaseColumns{
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NEWS);
        public static final String TABLE_NAME = "news";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_NEWS_TITLE = "title";
        public static final String COLUMN_NEWS_AUTHOR = "author";
        public static final String COLUMN_NEWS_DESCRIPTION = "description";
        public static final String COLUMN_NEWS_URL = "url";
        public static final String COLUMN_NEWS_IMAGE_URL = "imageUrl";
        public static final String COLUMN_NEWS_TIME = "time";
    }
}
