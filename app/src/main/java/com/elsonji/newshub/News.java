package com.elsonji.newshub;

public class News {
    private String mAuthor, mDescription, mTitle, mUrl, mUrlToImage, mPublishTime;

    public News(String author, String description, String title, String url,
                String urlToImage, String publishTime) {
        mAuthor = author;
        mDescription = description;
        mTitle = title;
        mUrl = url;
        mUrlToImage = urlToImage;
        mPublishTime = publishTime;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getUrlToImage() {
        return mUrlToImage;
    }

    public String getPublishTime() {
        return mPublishTime;
    }
}
