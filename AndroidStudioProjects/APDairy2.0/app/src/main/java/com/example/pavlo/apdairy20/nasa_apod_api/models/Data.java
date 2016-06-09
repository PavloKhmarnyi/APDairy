package com.example.pavlo.apdairy20.nasa_apod_api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pavlo on 08.06.16.
 */
public class Data {

    @SerializedName("date")
    private String date;

    @SerializedName("explanation")
    private String explanation;

    @SerializedName("media_type")
    private String mediaType;

    @SerializedName("title")
    private String title;

    @SerializedName("url")
    private String url;

    public String getDate() {
        return this.date;
    }

    public String getExplanation() {
        return this.explanation;
    }

    public String getMediaType() {
        return this.mediaType;
    }

    public String getTitle() {
        return this.title;
    }

    public String getUrl() {
        return this.url;
    }
}
